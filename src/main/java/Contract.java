public class Contract {

    private SubscriberAddress subscriberAddress;

    public Contract(ContractId contractId, SubscriberId subscriberId) {

    }

    public SubscriberAddress getSubscriberAddress() {
        return subscriberAddress;
    }

    public void setSubscriberAddress(SubscriberAddress address) {
        subscriberAddress = address;
    }
}
