package fr.lacombe.Model.Request;

import fr.lacombe.Model.AdvisorId;
import fr.lacombe.Model.EffectiveDate;
import fr.lacombe.Model.SubscriberAddress;
import fr.lacombe.Model.SubscriberId;

import java.io.Serializable;

public class SubscriberModificationRequest extends Request implements Serializable {

    private SubscriberAddress subscriberAddress;
    private SubscriberId subscriberId;
    private EffectiveDate effectiveDate;
    private AdvisorId advisorId;

    public SubscriberModificationRequest(SubscriberAddress subscriberAddress, SubscriberId subscriberId, EffectiveDate effectiveDate, AdvisorId advisorId) {
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
