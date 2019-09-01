import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ModifySubscribersAddressTest {

    @Test
    public void when_we_change_the_subscriber_s_address_then_the_address_on_his_contract_is_modified() {
        SubscriberAddress expectedAddress = new SubscriberAddress(Country.FRANCE, "paris", 75005, "10, rue souflot", true);
        SubscriberId anyId = new SubscriberId("id");
        Contract contract = new Contract(new ContractId("test"), anyId);
        EffectiveDate effectiveDate = null;
        SubscriberAddress initialAddress = new SubscriberAddress(Country.FRANCE, "paris", 75006, "12, rue vavin", true);
        Subscriber subscriber = new Subscriber(anyId, initialAddress, contract);

        subscriber.modifyAddress(expectedAddress, effectiveDate);

        Assertions.assertThat(expectedAddress).isEqualTo(contract.getSubscriberAddress());
    }

}
