package fr.lacombe;

import java.io.Serializable;

public class SubscriberRequestModification extends SubscriberRequest implements Serializable {

    private SubscriberAddress subscriberAddress;
    private SubscriberId subscriberId;
    private EffectiveDate effectiveDate;
    private AdvisorId advisorId;

    public SubscriberRequestModification(SubscriberAddress subscriberAddress, EffectiveDate effectiveDate) {
        this.subscriberAddress = subscriberAddress;
        this.effectiveDate = effectiveDate;
    }

    public SubscriberRequestModification(SubscriberAddress subscriberAddress, SubscriberId subscriberId, EffectiveDate effectiveDate, AdvisorId advisorId) {
        this.subscriberAddress = subscriberAddress;
        this.subscriberId = subscriberId;
        this.effectiveDate = effectiveDate;
        this.advisorId = advisorId;
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

    public AdvisorId getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(AdvisorId advisorId) {
        this.advisorId = advisorId;
    }

    public SubscriberId getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(SubscriberId subscriberId) {
        this.subscriberId = subscriberId;
    }
}
