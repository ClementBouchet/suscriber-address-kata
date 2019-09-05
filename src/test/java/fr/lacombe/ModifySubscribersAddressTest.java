package fr.lacombe;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ModifySubscribersAddressTest {

    @Ignore
    @Test
    public void when_we_change_the_subscriber_s_address_then_the_address_on_his_contract_is_modified() {
        SubscriberAddress expectedAddress = new SubscriberAddress(Country.FRANCE, "paris", 75005, "10, rue souflot", true);
        SubscriberId anyId = new SubscriberId("id");
        Contract contract = new Contract(new ContractId("test"), anyId);
        List<Contract> contractList = new ArrayList<>();
        contractList.add(contract);
        ContractList contracts = new ContractList(contractList);
        EffectiveDate effectiveDate = null;
        SubscriberAddress initialAddress = new SubscriberAddress(Country.FRANCE, "paris", 75006, "12, rue vavin", true);
        Subscriber subscriber = new Subscriber(anyId, initialAddress, contracts);

//        subscriber.modifyAddressOnAllContracts(expectedAddress);

        Assertions.assertThat(expectedAddress).isEqualTo(contract.getSubscriberAddress());
    }

    @Ignore
    @Test
    public void when_we_change_the_subscriber_s_address_then_all_his_contracts_are_modified_with_his_new_address() {
        SubscriberAddress expectedAddress = new SubscriberAddress(Country.FRANCE, "paris", 75005, "10, rue souflot", true);
        SubscriberId anyId = new SubscriberId("id");
        Contract contract = new Contract(new ContractId("test"), anyId);
        Contract contract2 = new Contract(new ContractId("test2"), anyId);
        EffectiveDate effectiveDate = null;
        SubscriberAddress initialAddress = new SubscriberAddress(Country.FRANCE, "paris", 75006, "12, rue vavin", true);
        List<Contract> contractList = new ArrayList<>();
        contractList.add(contract);
        contractList.add(contract2);
        ContractList contracts = new ContractList(contractList);
        Subscriber subscriber = new Subscriber(anyId, initialAddress, contracts);

//        subscriber.modifyAddressOnAllContracts(expectedAddress);

        for(Contract currentContract : contractList){
            Assertions.assertThat(expectedAddress).isEqualTo(currentContract.getSubscriberAddress());
        }
    }

}
