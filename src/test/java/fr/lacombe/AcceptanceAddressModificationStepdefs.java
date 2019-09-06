package fr.lacombe;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.lacombe.Controller.SubscriberController;
import fr.lacombe.Model.AdvisorId;
import fr.lacombe.Model.Country;
import fr.lacombe.Model.EffectiveDate;
import fr.lacombe.Model.Login;
import fr.lacombe.Model.MovementDate;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.SubscriberAddress;
import fr.lacombe.Model.SubscriberId;
import fr.lacombe.Proxies.AuthenticationServiceProxy;
import fr.lacombe.Proxies.SubscriberRepositoryProxy;
import fr.lacombe.Utils.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AcceptanceAddressModificationStepdefs extends SpringIntegrationTest{

    private SubscriberId subscriberId;
    private AdvisorId advisorId;
    private boolean isAddressActive;
    private ResponseEntity<String> modificationResponse;

    @Autowired
    AuthenticationServiceProxy authenticationServiceProxy;

    @Autowired
    SubscriberRepositoryProxy subscriberRepositoryProxy;

    @Autowired
    SubscriberController subscriberController;

    private static final String SUBSCRIBER_PATH = "/subscriber";
    private static final String AUTHENTICATE_PATH = "/authenticate";
    private final WireMockServer wireMockServer1 = new WireMockServer(options().port(8084));
    private final WireMockServer wireMockServer2 = new WireMockServer(options().port(8085));

    @Before
    public void SetUpMockServers(){
        wireMockServer1.start();
        configureFor("localhost", 8084);
        wireMockServer1.stubFor(post(urlEqualTo(AUTHENTICATE_PATH))
                .willReturn(aResponse().withHeader("Content-Type", "text/plain").withBody("advisorId123").withStatus(200)));

        wireMockServer2.start();
        configureFor("localhost", 8085);
        wireMockServer2.stubFor(post(urlEqualTo(SUBSCRIBER_PATH))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("address changed on all contracts").withStatus(200)));

        wireMockServer2.stubFor(post(urlEqualTo(SUBSCRIBER_PATH + "/movement"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("movement added").withStatus(200)));

    }

    @Given("^a subscriber with an active address in France$")
    public void aSubscriberWithAnActiveAddressInFrance() {
        isAddressActive = true;
        subscriberId = new SubscriberId("testID01");
    }

    @And("^the advisor is connected to \"([^\"]*)\"$")
    public void theAdvisorIsConnectedTo(String arg0) {
        Login advisorPseudo = new Login("advisorTestLogin");

        ResponseEntity<String> response = authenticationServiceProxy.authenticate(advisorPseudo);
        advisorId = new AdvisorId(response.getBody());
        assertEquals(HttpStatus.OK , response.getStatusCode());

        wireMockServer1.stop();
    }

    @When("^the advisor modifies the subscriber's address without effective date$")
    public void theAdvisorModifiesTheSubscriberSAddressWithoutEffectiveDate() {

        SubscriberAddress expectedSubscriberAddress = new SubscriberAddress(Country.FRANCE, "paris", 75013, "124, avenue d'Italie", isAddressActive);
        EffectiveDate effectiveDate = null;
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(expectedSubscriberAddress,subscriberId, effectiveDate, advisorId);
        MovementDate movementDate = new MovementDate(LocalDateTime.of(2019, 9, 1, 15, 0));
        TimeProvider mockedTimeProvider = mock(TimeProvider.class);
        when(mockedTimeProvider.now()).thenReturn(movementDate);
        subscriberController.setTimeProvider(mockedTimeProvider);

        //modificationResponse = subscriberController.modifyAddress(subscriberRequestModification);
    }

    @Then("^the modified subscriber's address is saved on all the contracts of the subscriber$")
    public void theModifiedSubscriberSAddressIsSavedOnAllTheContractsOfTheSubscriber() {
        assertThat(HttpStatus.OK).isEqualByComparingTo(modificationResponse.getStatusCode());
    }

    @And("^a modification movement is created$")
    public void aModificationMovementIsCreated() {
        WireMock.verify(postRequestedFor(urlEqualTo("/subscriber/movement")));
        wireMockServer2.stop();
    }
}
