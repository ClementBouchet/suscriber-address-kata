import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ModifySubscribersAddressTest {

    @Test
    public void when_we_change_the_subscriber_s_address_then_the_address_on_his_contract_is_modified() {
        SubscriberAddress expectedAddress = new SubscriberAddress(Country.FRANCE, "paris", 75005, "10, rue souflot", true);
        SubscriberId anyId = new SubscriberId("id");
        Contract contract = new Contract(new ContractId("test"), anyId);
        List<Contract> contracts = new ArrayList<>();
        contracts.add(contract);
        EffectiveDate effectiveDate = null;
        SubscriberAddress initialAddress = new SubscriberAddress(Country.FRANCE, "paris", 75006, "12, rue vavin", true);
        Subscriber subscriber = new Subscriber(anyId, initialAddress, contracts);

        subscriber.modifyAddress(expectedAddress, effectiveDate);

        Assertions.assertThat(expectedAddress).isEqualTo(contract.getSubscriberAddress());
    }

    @Test
    public void when_we_change_the_subscriber_s_address_then_all_his_contracts_are_modified_with_his_new_address() {
        SubscriberAddress expectedAddress = new SubscriberAddress(Country.FRANCE, "paris", 75005, "10, rue souflot", true);
        SubscriberId anyId = new SubscriberId("id");
        Contract contract = new Contract(new ContractId("test"), anyId);
        Contract contract2 = new Contract(new ContractId("test2"), anyId);
        EffectiveDate effectiveDate = null;
        SubscriberAddress initialAddress = new SubscriberAddress(Country.FRANCE, "paris", 75006, "12, rue vavin", true);
        List<Contract> contracts = new ArrayList<>();
        contracts.add(contract);
        contracts.add(contract2);
        Subscriber subscriber = new Subscriber(anyId, initialAddress, contracts);

        subscriber.modifyAddress(expectedAddress, effectiveDate);
        for(Contract currentContract : contracts){
            Assertions.assertThat(expectedAddress).isEqualTo(currentContract.getSubscriberAddress());
        }
    }

}
