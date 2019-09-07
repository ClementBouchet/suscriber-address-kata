package fr.lacombe;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import fr.lacombe.Controller.SubscriberController;
import fr.lacombe.Model.AdvisorId;
import fr.lacombe.Model.ContractList;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.SubscriberId;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

    @Autowired
    private SubscriberController subcriberController;

    private static UUID id;

    @BeforeClass
    public static void generateRandomId(){
        id = UUID.randomUUID();
    }

    @Test
    public void when_the_subscriber_lives_in_France_then_modify_his_address() throws IOException {

        StubMapping stubMapping = instanceRule.stubFor(post(urlEqualTo("/address"))
                .withId(id)
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"country\": \"FRANCE\"}")));
        ContractList mockedContractList = Mockito.mock(ContractList.class);

        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(null, new SubscriberId("anySubscriberId"), null, new AdvisorId("anyAdvisorId"));
        subcriberController.setContractList(mockedContractList);
        subcriberController.modifyAddress(subscriberRequestModification);

        verify(mockedContractList).modifySubscriberAddressOnAllContracts(any());

        removeStub(stubMapping);
    }


    @Test
    public void when_the_subscriber_does_not_lives_in_France_then_do_not_modify_his_address() throws IOException {

        ContractList mockedContractList = Mockito.mock(ContractList.class);
        SubscriberRequestModification subscriberRequestModification = new SubscriberRequestModification(null, new SubscriberId("anySubscriberId"), null, new AdvisorId("anyAdvisorId"));

        instanceRule.stubFor(post(urlEqualTo("/address"))
                .withId(id)
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"country\": \"ITALIA\"}")));

        subcriberController.setContractList(mockedContractList);
        subcriberController.modifyAddress(subscriberRequestModification);

        verify(mockedContractList, never()).modifySubscriberAddressOnAllContracts(any());
    }
}
