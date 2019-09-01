import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AcceptanceAddressModificationStepdefs {

    private Subscriber subscriber;
    private SubscriberId subscriberId;
    private SubscriberAddress expectedSubscriberAddress;
    private Contract subscriptionContract;
    private Contract subscriptionContract2;
    private SubscriberDaoInterface mockedSubscriberDaoInterface;
    private AdvisorId advisorId;
    private MovementDate movementDate;
    private boolean isAddressActive;

    @Given("^a subscriber with an active address in France$")
    public void aSubscriberWithAnActiveAddressInFrance() {
        isAddressActive = true;
        SubscriberAddress initialSubscriberAddress = new SubscriberAddress(Country.FRANCE, "lille", 59000, "7, rue Camille Guérin", isAddressActive);
        subscriberId = new SubscriberId("testID01");
        subscriber = new Subscriber(subscriberId, initialSubscriberAddress);
    }

    @When("^the advisor connected to canal modifies the subscriber's address without effective date$")
    public void theAdvisorConnectedToCanalModifiesTheSubscriberSAddressWithoutEffectiveDate() {
        Login advisorPseudo = new Login("advisorTestLogin");
        AdvisorAuthenticatorInterface mockedAuthenticator = mock(AdvisorAuthenticatorInterface.class);
        when(mockedAuthenticator.authenticate(advisorPseudo)).thenReturn(new AdvisorId("advisorTestId"));
        advisorId = mockedAuthenticator.authenticate(advisorPseudo);
        mockedSubscriberDaoInterface = mock(SubscriberDaoInterface.class);
        expectedSubscriberAddress = new SubscriberAddress(Country.FRANCE, "paris", 75013, "124, avenue d'Italie", isAddressActive);
        ContractId contractId = new ContractId("firstContract");
        subscriptionContract = new Contract(contractId, subscriberId);
        ContractId contractId2 = new ContractId("secondContract");
        subscriptionContract2 = new Contract(contractId2, subscriberId);
        EffectiveDate effectiveDate = null;
        movementDate = new MovementDate(LocalDateTime.of(2019, 9, 1, 15, 0));
        TimeProviderInterface mockedTimeProvider = mock(TimeProviderInterface.class);
        when(mockedTimeProvider.now()).thenReturn(movementDate);

        subscriber.modifyAddress(expectedSubscriberAddress, effectiveDate);
    }

    @Then("^the modified subscriber's address is saved on all the contracts of the subscriber$")
    public void theModifiedSubscriberSAddressIsSavedOnAllTheContractsOfTheSubscriber() {
        Assertions.assertThat(expectedSubscriberAddress).isEqualTo(subscriptionContract.getSubscriberAddress());
        Assertions.assertThat(expectedSubscriberAddress).isEqualTo(subscriptionContract2.getSubscriberAddress());
    }

    @And("^a modification movement is created$")
    public void aModificationMovementIsCreated() {
        verify(mockedSubscriberDaoInterface).addModificationAddressMovement(advisorId, subscriberId, MovementType.SUSBCRIBER_INFO, movementDate);
    }
}
