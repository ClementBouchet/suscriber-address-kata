import fr.lacombe.AdvisorAuthenticatorInterface;
import fr.lacombe.AdvisorId;
import fr.lacombe.Contract;
import fr.lacombe.ContractId;
import fr.lacombe.Country;
import fr.lacombe.EffectiveDate;
import fr.lacombe.Login;
import fr.lacombe.MovementDate;
import fr.lacombe.MovementType;
import fr.lacombe.Subscriber;
import fr.lacombe.SubscriberAddress;
import fr.lacombe.SubscriberDaoInterface;
import fr.lacombe.SubscriberId;
import fr.lacombe.TimeProviderInterface;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SuscriberAddressAcceptanceTest {

    @Ignore
    @Test
    public void modify_address_of_subscriber_acceptance_test(){
        //Arrange
        boolean isAddressActive = true;
        SubscriberAddress initialSubscriberAddress = new SubscriberAddress(Country.FRANCE, "lille", 59000, "7, rue Camille Guérin", isAddressActive);
        SubscriberId subscriberId = new SubscriberId("testID01");
        Subscriber subscriber = new Subscriber(subscriberId, initialSubscriberAddress);
        Login advisorPseudo = new Login("advisorTestLogin");
        AdvisorAuthenticatorInterface mockedAuthenticator = mock(AdvisorAuthenticatorInterface.class);
        when(mockedAuthenticator.authenticate(advisorPseudo)).thenReturn(new AdvisorId("advisorTestId"));
        AdvisorId advisorId = mockedAuthenticator.authenticate(advisorPseudo);
        SubscriberDaoInterface mockedSubscriberDaoInterface = mock(SubscriberDaoInterface.class);
        SubscriberAddress expectedSubscriberAddress = new SubscriberAddress(Country.FRANCE, "paris", 75013, "124, avenue d'Italie", isAddressActive);
        ContractId contractId = new ContractId("firstContract");
        Contract subscriptionContract = new Contract(contractId, subscriberId);
        ContractId contractId2 = new ContractId("secondContract");
        Contract subscriptionContract2 = new Contract(contractId2, subscriberId);
        EffectiveDate effectiveDate = null;
        MovementDate movementDate = new MovementDate(LocalDateTime.of(2019, 9, 1, 15, 0));
        TimeProviderInterface mockedTimeProvider = mock(TimeProviderInterface.class);
        when(mockedTimeProvider.now()).thenReturn(movementDate);

        //Act
//        subscriber.modifyAddress(expectedSubscriberAddress);

        //Assert
        verify(mockedSubscriberDaoInterface).addModificationAddressMovement(advisorId, subscriberId, MovementType.SUSBCRIBER_INFO, movementDate);
        Assertions.assertThat(expectedSubscriberAddress).isEqualTo(subscriptionContract.getSubscriberAddress());
        Assertions.assertThat(expectedSubscriberAddress).isEqualTo(subscriptionContract2.getSubscriberAddress());

    }

}
