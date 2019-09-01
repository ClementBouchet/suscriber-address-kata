import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Contract {
    public Contract(ContractId contractId, SubscriberId subscriberId) {

    }

    public SubscriberAddress getSubscriberAddress() {
        throw new NotImplementedException();
    }
}
