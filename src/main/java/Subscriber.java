public class Subscriber {

    private ContractList contracts;

    public Subscriber(SubscriberId subscriberId, SubscriberAddress address) {
    }

    public Subscriber(SubscriberId anyId, SubscriberAddress initialAddress, ContractList contracts) {
        this.contracts = contracts;
    }

    public void modifyAddress(SubscriberAddress newAddress, EffectiveDate effectiveDate) {
        contracts.applyAddressChangeOnAll(newAddress);
    }
}
