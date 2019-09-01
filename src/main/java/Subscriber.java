public class Subscriber {

    private Contract contract;

    public Subscriber(SubscriberId subscriberId, SubscriberAddress address) {
    }

    public Subscriber(SubscriberId subscriberId, SubscriberAddress address, Contract contract) {
        this.contract = contract;
    }

    public void modifyAddress(SubscriberAddress newAddress, EffectiveDate effectiveDate) {
        contract.setSubscriberAddress(newAddress);
    }
}
