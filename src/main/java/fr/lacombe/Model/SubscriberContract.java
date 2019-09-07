package fr.lacombe.Model;

import fr.lacombe.ContractId;

public class SubscriberContract {
    private SubscriberAddress subscriberAddress;

    public SubscriberContract(SubscriberId subscriberId, SubscriberAddress subscriberAddress, ContractId contractId) {
        this.subscriberAddress = subscriberAddress;
    }

    public SubscriberAddress getAddress() {
        return subscriberAddress;
    }

    public void modifySubscriberAddress(SubscriberAddress subscriberAddress) {
        this.subscriberAddress = subscriberAddress;
    }
}
