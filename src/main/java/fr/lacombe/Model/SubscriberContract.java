package fr.lacombe.Model;

import fr.lacombe.ContractId;

public class SubscriberContract {
    private SubscriberId subscriberId;
    private SubscriberAddress subscriberAddress;
    private ContractId contractId;

    public SubscriberContract(SubscriberId subscriberId, SubscriberAddress subscriberAddress, ContractId contractId) {
        this.subscriberId = subscriberId;
        this.subscriberAddress = subscriberAddress;
        this.contractId = contractId;
    }

    public SubscriberAddress getAddress() {
        return subscriberAddress;
    }

    public void modifySubscriberAddress(SubscriberAddress subscriberAddress) {
        this.subscriberAddress = subscriberAddress;
    }
}
