package fr.lacombe;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.lacombe.Controller.SubscriberController;
import fr.lacombe.Model.AdvisorId;
import fr.lacombe.Model.Country;
import fr.lacombe.Model.CountryEnum;
import fr.lacombe.Model.EffectiveDate;
import fr.lacombe.Model.Login;
import fr.lacombe.Model.MovementDate;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.SubscriberAddress;
import fr.lacombe.Model.SubscriberId;
import fr.lacombe.Proxies.AddressRepository;
import fr.lacombe.Proxies.AuthenticationService;
import fr.lacombe.Utils.JsonMapper;
import fr.lacombe.Utils.SubscriberControllerContext;
import fr.lacombe.Utils.TimeProvider;
import org.assertj.core.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.mockito.MockitoAnnotations.initMocks;

public class AcceptanceAddressModificationStepdefs extends SpringIntegrationTest{

    private SubscriberId subscriberId;
    private AdvisorId advisorId;
    private boolean isAddressActive;
    private ResponseEntity<String> modificationResponse;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AddressRepository addressRepository;

    @Mock
    SubscriberControllerContext subscriberControllerContext;
    @Mock
    TimeProvider timeProvider;

    @InjectMocks
    @Autowired
    SubscriberController subscriberController;

    @Before
    private void setUpMocks(){
        initMocks(this);
        Mockito.when(timeProvider.now()).thenReturn(new MovementDate(LocalDateTime.of(2019, 1, 1, 12, 0)));
        Mockito.when(subscriberControllerContext.getAdvisorId()).thenReturn(new AdvisorId("anyAdvisorId"));
    }

    private static final String GET_SUBSCRIBER_CONTRACTS_PATH = "/contract/.*";
    private static final String SAVE_SUBSCRIBER_CONTRACTS = "/contract";
    private static final String ADDRESS_PATH = "/address/.*";
    private static final String MOVEMENT_PATH = "/movement";
    private static final String AUTHENTICATION_PATH = "/authenticate/.*";
    private String addressRepositoryJsonResponse = "{\"country\": \"FRANCE\"}";

    private final WireMockServer wireMockServerAuthenticationRepository = new WireMockServer(options().port(8007));
    private final WireMockServer wireMockServerAddressRepository = new WireMockServer(options().port(8008));
    private final WireMockServer wireMockServerContractRepository = new WireMockServer(options().port(8009));
    private final WireMockServer wireMockServerHistoryRepository = new WireMockServer(options().port(8010));

    @Before
    public void SetUpMockServers(){
        wireMockServerAuthenticationRepository.start();
        wireMockServerAddressRepository.start();
        wireMockServerContractRepository.start();
        wireMockServerHistoryRepository.start();
        configureFor("localhost", 8007);
        configureFor("localhost", 8008);
        configureFor("localhost", 8009);
        configureFor("localhost", 8010);

        wireMockServerAuthenticationRepository.stubFor(post(urlMatching(AUTHENTICATION_PATH))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "text/plain")));
        wireMockServerAddressRepository.stubFor(get(urlMatching(ADDRESS_PATH))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody(addressRepositoryJsonResponse).withStatus(200)));
        wireMockServerContractRepository.stubFor(get(urlMatching(GET_SUBSCRIBER_CONTRACTS_PATH))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "text/plain")));
        wireMockServerContractRepository.stubFor(post(urlEqualTo(SAVE_SUBSCRIBER_CONTRACTS))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "text/plain")));
        wireMockServerHistoryRepository.stubFor(post(urlEqualTo(MOVEMENT_PATH))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "text/plain")));
    }

    @Given("^a subscriber with an active address in France$")
    public void aSubscriberWithAnActiveAddressInFrance() throws IOException {
        subscriberId = new SubscriberId("aSubscriberId01");
        ResponseEntity<String> response = addressRepository.getCountryAddress(subscriberId.getId());
        JsonMapper jsonMapper = new JsonMapper();
        Country country = jsonMapper.mapJsonToCountry(response);

        wireMockServerAddressRepository.verify(getRequestedFor(urlMatching(ADDRESS_PATH)));
        Assertions.assertThat(country.isFrance()).isTrue();
    }

    @And("^the advisor is connected to \"([^\"]*)\"$")
    public void theAdvisorIsConnectedTo(String canal) {
        Login advisorLogin = new Login("advisorLogin");
        authenticationService.authenticate(advisorLogin, canal);

        wireMockServerAuthenticationRepository.verify(postRequestedFor(urlMatching(AUTHENTICATION_PATH)));
    }

    @When("^the advisor modifies the subscriber's address without effective date$")
    public void theAdvisorModifiesTheSubscriberSAddressWithoutEffectiveDate() throws IOException {

        SubscriberAddress expectedSubscriberAddress = new SubscriberAddress(CountryEnum.FRANCE, "paris", 75013, "124, avenue d'Italie", isAddressActive);
        EffectiveDate effectiveDate = null;
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(expectedSubscriberAddress,subscriberId, effectiveDate, advisorId);

        subscriberController.modifyAddress(subscriberRequestModification);
    }

    @Then("^the modified subscriber's address is saved on all the contracts of the subscriber$")
    public void theModifiedSubscriberSAddressIsSavedOnAllTheContractsOfTheSubscriber() {
        wireMockServerContractRepository.verify(getRequestedFor(urlMatching(GET_SUBSCRIBER_CONTRACTS_PATH)));
        wireMockServerContractRepository.verify(postRequestedFor(urlEqualTo(SAVE_SUBSCRIBER_CONTRACTS)));
    }

    @And("^a modification movement is created$")
    public void aModificationMovementIsCreated() {
        wireMockServerHistoryRepository.verify(postRequestedFor(urlEqualTo(MOVEMENT_PATH)));
        wireMockServerAuthenticationRepository.stop();
        wireMockServerAddressRepository.stop();
        wireMockServerContractRepository.stop();
        wireMockServerHistoryRepository.stop();
    }
}
