import java.util.List;

public class Subscriber {

    private Contract contract;
    private List<Contract> contracts;

    public Subscriber(SubscriberId subscriberId, SubscriberAddress address) {
    }

    public Subscriber(SubscriberId subscriberId, SubscriberAddress address, Contract contract) {
        this.contract = contract;
    }

    public Subscriber(SubscriberId anyId, SubscriberAddress initialAddress, List<Contract> contracts) {
        this.contracts = contracts;
    }

    public void modifyAddress(SubscriberAddress newAddress, EffectiveDate effectiveDate) {
        for(Contract contract : contracts){
            contract.setSubscriberAddress(newAddress);
        }
    }
}
