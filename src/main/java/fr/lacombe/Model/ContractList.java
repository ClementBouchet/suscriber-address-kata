package fr.lacombe.Model;

import java.util.List;

public class ContractList {

    List<Contract> contracts;

    public ContractList(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public void applyAddressChangeOnAll(SubscriberAddress newAddress) {
        for(Contract contract : contracts){
            contract.setSubscriberAddress(newAddress);
        }
    }
}
