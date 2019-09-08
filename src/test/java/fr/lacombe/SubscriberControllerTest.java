package fr.lacombe;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import fr.lacombe.Controller.SubscriberController;
import fr.lacombe.Model.AdvisorId;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.SubscriberId;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.removeStub;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@RunWith(SpringRunner.class)
public class SubscriberControllerTest extends SpringIntegrationTest{

    @ClassRule
    public static WireMockClassRule addressRepositoryWireMockClassRule = new WireMockClassRule(options().port(8008).bindAddress("localhost"));
    @Rule
    public WireMockClassRule addressRepositoryInstanceRule = addressRepositoryWireMockClassRule;
    @ClassRule
    public static WireMockClassRule contractRepositoryWireMockClassRule = new WireMockClassRule(options().port(8009).bindAddress("localhost"));
    @Rule
    public WireMockClassRule contractRepositoryInstanceRule = contractRepositoryWireMockClassRule;
    @ClassRule
    public static WireMockClassRule historyRepositoryWireMockClassRule = new WireMockClassRule(options().port(8010).bindAddress("localhost"));
    @Rule
    public WireMockClassRule historyRepositoryInstanceRule = historyRepositoryWireMockClassRule;


    @Autowired
    private SubscriberController subcriberController;

    private static UUID id;


    @BeforeClass
    public static void generateRandomId(){
        id = UUID.randomUUID();
    }


    @Test
    public void when_the_subscriber_does_not_lives_in_France_then_do_not_get_all_his_contracts() throws IOException {
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(null, new SubscriberId("anySubscriberId"), null, new AdvisorId("anyAdvisorId"));
        StubMapping stubMappingForAddressRepository = stubAddressRepositoryToReturnCountry("ITALIA");
        StubMapping stubMappingForGettingContractsFromContractRepository = stubContractRepositoryToGetContractList();
        StubMapping stubMappingForSavingContractsInContractRepository = stubContractRepositoryToSaveModifiedContracts();
        StubMapping stubHistoryRepositoryToCreateMovement = stubHistoryRepositoryToCreateMovement();

        subcriberController.modifyAddress(subscriberRequestModification);

        addressRepositoryInstanceRule.verify(0,getRequestedFor(urlMatching("address/.*")));

        removeStub(stubMappingForAddressRepository);
        removeStub(stubMappingForGettingContractsFromContractRepository);
        removeStub(stubMappingForSavingContractsInContractRepository);
        removeStub(stubHistoryRepositoryToCreateMovement);
    }

    @Test
    public void when_the_subscriber_lives_in_France_then_get_all_his_contracts() throws IOException {
        StubMapping stubMappingForAddressRepository = stubAddressRepositoryToReturnCountry("FRANCE");
        StubMapping stubMappingForGettingContractsFromContractRepository = stubContractRepositoryToGetContractList();
        StubMapping stubMappingForSavingContractsInContractRepository = stubContractRepositoryToSaveModifiedContracts();
        StubMapping stubHistoryRepositoryToCreateMovement = stubHistoryRepositoryToCreateMovement();
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(null, new SubscriberId("anySubscriberId"), null, new AdvisorId("anyAdvisorId"));

        subcriberController.modifyAddress(subscriberRequestModification);

        contractRepositoryInstanceRule.verify(getRequestedFor(urlMatching("/contract/.*")));

        removeStub(stubMappingForAddressRepository);
        removeStub(stubMappingForGettingContractsFromContractRepository);
        removeStub(stubMappingForSavingContractsInContractRepository);
        removeStub(stubHistoryRepositoryToCreateMovement);
    }

    @Test
    public void when_the_subscriber_lives_in_France_then_save_all_the_modified_contracts() throws IOException {
        StubMapping stubMappingForAddressRepository = stubAddressRepositoryToReturnCountry("FRANCE");
        StubMapping stubMappingForGettingContractsFromContractRepository = stubContractRepositoryToGetContractList();
        StubMapping stubMappingForSavingContractsInContractRepository = stubContractRepositoryToSaveModifiedContracts();
        StubMapping stubHistoryRepositoryToCreateMovement = stubHistoryRepositoryToCreateMovement();
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(null, new SubscriberId("anySubscriberId"), null, new AdvisorId("anyAdvisorId"));

        subcriberController.modifyAddress(subscriberRequestModification);

        contractRepositoryInstanceRule.verify(postRequestedFor(urlEqualTo("/contract")));

        removeStub(stubMappingForAddressRepository);
        removeStub(stubMappingForGettingContractsFromContractRepository);
        removeStub(stubMappingForSavingContractsInContractRepository);
        removeStub(stubHistoryRepositoryToCreateMovement);
    }


    @Test
    public void when_the_contracts_have_been_saved_and_modified_then_create_modification_movement() throws IOException {
        StubMapping stubMappingForAddressRepository = stubAddressRepositoryToReturnCountry("FRANCE");
        StubMapping stubMappingForGettingContractsFromContractRepository = stubContractRepositoryToGetContractList();
        StubMapping stubMappingForSavingContractsInContractRepository = stubContractRepositoryToSaveModifiedContracts();
        StubMapping stubHistoryRepositoryToCreateMovement = stubHistoryRepositoryToCreateMovement();
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(null, new SubscriberId("anySubscriberId"), null, new AdvisorId("anyAdvisorId"));

        subcriberController.modifyAddress(subscriberRequestModification);

        historyRepositoryInstanceRule.verify(postRequestedFor(urlEqualTo("/movement")));

        removeStub(stubMappingForAddressRepository);
        removeStub(stubMappingForGettingContractsFromContractRepository);
        removeStub(stubMappingForSavingContractsInContractRepository);
        removeStub(stubHistoryRepositoryToCreateMovement);
    }

    private StubMapping stubAddressRepositoryToReturnCountry(String countryName) {
        return addressRepositoryInstanceRule.stubFor(get(urlMatching("/address/.*"))
                .withId(id)
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"country\": \"" + countryName + "\"}")));
    }

    private StubMapping stubContractRepositoryToGetContractList() {
        return contractRepositoryInstanceRule.stubFor(get(urlMatching("/contract/.*"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "text/plain")));
    }
    private StubMapping stubContractRepositoryToSaveModifiedContracts() {
        return contractRepositoryInstanceRule.stubFor(post(urlEqualTo("/contract"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "text/plain")));
    }

    private StubMapping stubHistoryRepositoryToCreateMovement() {
        return historyRepositoryInstanceRule.stubFor(post(urlEqualTo("/movement"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "text/plain")));
    }
}
