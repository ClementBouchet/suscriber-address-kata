package fr.lacombe;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import fr.lacombe.Controller.SubscriberController;
import fr.lacombe.Model.AdvisorId;
import fr.lacombe.Model.ContractList;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.SubscriberId;
import fr.lacombe.Proxies.ContractRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.removeStub;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class SubscriberControllerTest extends SpringIntegrationTest{

    @ClassRule
    public static WireMockClassRule wireMockClassRule = new WireMockClassRule(options().port(8008).bindAddress("localhost"));
    @Rule
    public WireMockClassRule instanceRule = wireMockClassRule;
    @ClassRule
    public static WireMockClassRule wireMockClassRule2  =new WireMockClassRule(options().port(8009).bindAddress("localhost"));;
    @Rule
    public WireMockClassRule instanceRule2 = wireMockClassRule2;

    @Mock
    private ContractRepository mockedContractRepository;

    @Autowired
    @InjectMocks
    private SubscriberController subcriberController;

    private static UUID id;

    @BeforeClass
    public static void generateRandomId(){
        id = UUID.randomUUID();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void when_the_subscriber_lives_in_France_then_modify_his_address() throws IOException {

        StubMapping stubMappingForAddressRepository = instanceRule.stubFor(post(urlEqualTo("/address"))
                .withId(id)
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"country\": \"FRANCE\"}")));
        ContractList mockedContractList = Mockito.mock(ContractList.class);
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(null, new SubscriberId("anySubscriberId"), null, new AdvisorId("anyAdvisorId"));
        subcriberController.setContractList(mockedContractList);

        subcriberController.modifyAddress(subscriberRequestModification);

        verify(mockedContractList).modifySubscriberAddressOnAllContracts(any());

        removeStub(stubMappingForAddressRepository);
    }


    @Test
    public void when_the_subscriber_does_not_lives_in_France_then_do_not_modify_his_address() throws IOException {

        ContractList mockedContractList = Mockito.mock(ContractList.class);
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(null, new SubscriberId("anySubscriberId"), null, new AdvisorId("anyAdvisorId"));
        StubMapping stubMappingForAddressRepository = instanceRule.stubFor(post(urlEqualTo("/address"))
                .withId(id)
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"country\": \"ITALIA\"}")));
        subcriberController.setContractList(mockedContractList);

        subcriberController.modifyAddress(subscriberRequestModification);

        verify(mockedContractList, never()).modifySubscriberAddressOnAllContracts(any());

        removeStub(stubMappingForAddressRepository);
    }

    @Test
    public void when_the_subscriber_lives_in_France_then_get_all_his_contracts() throws IOException {
        StubMapping stubMappingForAddressRepository = instanceRule.stubFor(post(urlEqualTo("/address"))
                .withId(id)
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"country\": \"FRANCE\"}")));
        ContractList mockedContractList = Mockito.mock(ContractList.class);
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(null, new SubscriberId("anySubscriberId"), null, new AdvisorId("anyAdvisorId"));
        subcriberController.setContractList(mockedContractList);
        subcriberController.modifyAddress(subscriberRequestModification);

        verify(mockedContractRepository).getAllContractsFromSubscriber(any());

        removeStub(stubMappingForAddressRepository);
    }
}
