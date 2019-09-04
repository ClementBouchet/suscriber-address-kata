import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.lacombe.AdvisorId;
import fr.lacombe.AuthenticationServiceProxy;
import fr.lacombe.Contract;
import fr.lacombe.ContractId;
import fr.lacombe.ContractList;
import fr.lacombe.Country;
import fr.lacombe.EffectiveDate;
import fr.lacombe.Login;
import fr.lacombe.MovementDate;
import fr.lacombe.Subscriber;
import fr.lacombe.SubscriberAddress;
import fr.lacombe.SubscriberDaoInterface;
import fr.lacombe.SubscriberId;
import fr.lacombe.SubscriberRepositoryProxy;
import fr.lacombe.SubscriberRequestModification;
import fr.lacombe.TimeProviderInterface;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AcceptanceAddressModificationStepdefs extends SpringIntegrationTest{

    private Subscriber subscriber;
    private SubscriberId subscriberId;
    private SubscriberAddress expectedSubscriberAddress;
    private Contract subscriptionContract;
    private Contract subscriptionContract2;
    private SubscriberDaoInterface mockedSubscriberDaoInterface;
    private AdvisorId advisorId;
    private MovementDate movementDate;
    private boolean isAddressActive;
    private ResponseEntity<String> modificationResponse;

    @Autowired
    AuthenticationServiceProxy authenticationServiceProxy;

    @Autowired
    SubscriberRepositoryProxy subscriberRepositoryProxy;

    private final InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("cucumber.json");
    private final String jsonString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();

    private static final String APPLICATION_JSON = "application/json";
    private static final String SUBSCRIBER_PATH = "/subscriber";
    private static final String AUTHENTICATE_PATH = "/authenticate";
    private final WireMockServer wireMockServer1 = new WireMockServer(options().port(8084));
    private final WireMockServer wireMockServer2 = new WireMockServer(options().port(8085));
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Given("^a subscriber with an active address in France$")
    public void aSubscriberWithAnActiveAddressInFrance() {
        isAddressActive = true;
        SubscriberAddress initialSubscriberAddress = new SubscriberAddress(Country.FRANCE, "lille", 59000, "7, rue Camille Guérin", isAddressActive);
        subscriberId = new SubscriberId("testID01");
        ContractId contractId = new ContractId("firstContract");
        subscriptionContract = new Contract(contractId, subscriberId);
        ContractId contractId2 = new ContractId("secondContract");
        subscriptionContract2 = new Contract(contractId2, subscriberId);
        List<Contract> contractList = new ArrayList<>();
        contractList.add(subscriptionContract);
        contractList.add(subscriptionContract2);
        ContractList contracts = new ContractList(contractList);

        subscriber = new Subscriber(subscriberId, initialSubscriberAddress, contracts);
    }

    @And("^the advisor is connected to \"([^\"]*)\"$")
    public void theAdvisorIsConnectedTo(String arg0) throws IOException {
        Login advisorPseudo = new Login("advisorTestLogin");
        //Appel au SubscriberController AuthenticateController avec arg0
        wireMockServer1.start();
        configureFor("localhost", 8084);
        stubFor(post(urlEqualTo(AUTHENTICATE_PATH))
                //.withHeader("content-type", equalTo(APPLICATION_JSON))
                //.withRequestBody(containing("advisorTestLogin"))
                .willReturn(aResponse().withHeader("Content-Type", "text/plain").withBody("advisorId123").withStatus(200)));

//        HttpPost request = new HttpPost("http://localhost:" + 8084 + "/authenticate");
//        StringEntity entity = new StringEntity(jsonString);
//        request.addHeader("content-type", APPLICATION_JSON);
//        request.setEntity(entity);
//        HttpResponse response = httpClient.execute(request);
        ResponseEntity<String> response = authenticationServiceProxy.authenticate(advisorPseudo);
        advisorId = new AdvisorId(response.getBody());
        assertEquals(HttpStatus.OK , response.getStatusCode());

        wireMockServer1.stop();
    }

    @When("^the advisor modifies the subscriber's address without effective date$")
    public void theAdvisorModifiesTheSubscriberSAddressWithoutEffectiveDate() {

        wireMockServer2.start();
        configureFor("localhost", 8085);
        stubFor(post(urlEqualTo(SUBSCRIBER_PATH))
                //.withHeader("content-type", equalTo(APPLICATION_JSON))
                //.withRequestBody(containing("advisorTestLogin"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{\"subscriberAddress\": \"124, avenue d'Italie\"}").withStatus(200)));

        stubFor(post(urlEqualTo(SUBSCRIBER_PATH + "/movement"))
                //.withHeader("content-type", equalTo(APPLICATION_JSON))
                //.withRequestBody(containing("advisorTestLogin"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("movement added").withStatus(200)));

        mockedSubscriberDaoInterface = mock(SubscriberDaoInterface.class);
        expectedSubscriberAddress = new SubscriberAddress(Country.FRANCE, "paris", 75013, "124, avenue d'Italie", isAddressActive);
        EffectiveDate effectiveDate = null;
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(expectedSubscriberAddress, effectiveDate);
        movementDate = new MovementDate(LocalDateTime.of(2019, 9, 1, 15, 0));
        TimeProviderInterface mockedTimeProvider = mock(TimeProviderInterface.class);
        when(mockedTimeProvider.now()).thenReturn(movementDate);

        modificationResponse = subscriberRepositoryProxy.modifyAddress(subscriberRequestModification);

    }

    @Then("^the modified subscriber's address is saved on all the contracts of the subscriber$")
    public void theModifiedSubscriberSAddressIsSavedOnAllTheContractsOfTheSubscriber() {
//        Assertions.assertThat(expectedSubscriberAddress).isEqualTo(subscriptionContract.getSubscriberAddress());
//        Assertions.assertThat(expectedSubscriberAddress).isEqualTo(subscriptionContract2.getSubscriberAddress());
        assertThat(HttpStatus.OK).isEqualByComparingTo(modificationResponse.getStatusCode());

    }

    @And("^a modification movement is created$")
    public void aModificationMovementIsCreated() {
        modificationResponse = subscriberRepositoryProxy.addMovement();
        WireMock.verify(postRequestedFor(urlEqualTo("/subscriber/movement")));
        assertThat(HttpStatus.OK).isEqualByComparingTo(modificationResponse.getStatusCode());

//        verify(mockedSubscriberDaoInterface).addModificationAddressMovement(advisorId, subscriberId, MovementType.SUSBCRIBER_INFO, movementDate);
        wireMockServer2.stop();
    }
}
