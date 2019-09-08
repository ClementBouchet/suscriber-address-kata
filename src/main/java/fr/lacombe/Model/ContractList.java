package fr.lacombe.Model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractList {

    public List<SubscriberContract> contracts;

    public ContractList(List<SubscriberContract> contracts) {
        this.contracts = contracts;
    }

    public void modifySubscriberAddressOnAllContracts(SubscriberAddress subscriberAddress) {
        for(SubscriberContract subscriberContract : contracts){
            subscriberContract.modifySubscriberAddress(subscriberAddress);
        }
    }

}
