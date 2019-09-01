import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Subscriber {
    public Subscriber(SubscriberId subscriberId, SubscriberAddress address) {
    }

    public void modifyAddress(SubscriberAddress newAddress, EffectiveDate effectiveDate) {
        throw new NotImplementedException();
    }
}
