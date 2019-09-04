package fr.lacombe;

import java.io.Serializable;

public class SubscriberRequestModification extends SubscriberRequest implements Serializable {

    private SubscriberAddress subscriberAddress;
    private EffectiveDate effectiveDate;

    public SubscriberRequestModification(SubscriberAddress subscriberAddress, EffectiveDate effectiveDate) {
        this.subscriberAddress = subscriberAddress;
        this.effectiveDate = effectiveDate;
    }

    public SubscriberAddress getSubscriberAddress() {
        return subscriberAddress;
    }

    public void setSubscriberAddress(SubscriberAddress subscriberAddress) {
        this.subscriberAddress = subscriberAddress;
    }

    public EffectiveDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(EffectiveDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
