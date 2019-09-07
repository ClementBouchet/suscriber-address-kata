package fr.lacombe;

import fr.lacombe.Model.ContractList;
import fr.lacombe.Model.CountryEnum;
import fr.lacombe.Model.SubscriberAddress;
import fr.lacombe.Model.SubscriberContract;
import fr.lacombe.Model.SubscriberId;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContractListTest {

    @Test
    public void the_modification_of_the_address_is_done_on_all_the_given_contracts_of_the_subscribers() {
        SubscriberAddress newSubscriberAddress = new SubscriberAddress(CountryEnum.FRANCE, "PARIS", 75006, "28, boulevard Saint-Germain", true);
        SubscriberId subscriberId = new SubscriberId("anyId");
        SubscriberAddress oldSubscriberAddress = new SubscriberAddress(CountryEnum.FRANCE, "PARIS", 75018, "15, rue Marx Dormoy", true);

        List<SubscriberContract> contracts = new ArrayList<>();
        ContractId contractId01 = new ContractId("anyContractId");
        ContractId contractId02 = new ContractId("anyContractId02");
        ContractId contractId03 = new ContractId("anyContractId03");
        contracts.add(new SubscriberContract(subscriberId, oldSubscriberAddress, contractId01));
        contracts.add(new SubscriberContract(subscriberId, oldSubscriberAddress, contractId02));
        contracts.add(new SubscriberContract(subscriberId, oldSubscriberAddress, contractId03));
        ContractList contractList = new ContractList(contracts);

        contractList.modifySubscriberAddressOnAllContracts(newSubscriberAddress);

        List<SubscriberAddress> addressList = contractList.contracts.stream().map(SubscriberContract::getAddress).collect(Collectors.toList());
        Assertions.assertThat(addressList).containsOnly(newSubscriberAddress);
    }
}
