package fr.lacombe.Model.Request;

import fr.lacombe.Model.AdvisorId;
import fr.lacombe.Model.MovementDate;
import fr.lacombe.Model.MovementType;
import fr.lacombe.Model.SubscriberId;

public class SubscriberRequestMovement extends SubscriberRequest{

    private AdvisorId advisorId;
    private SubscriberId subscriberId;
    private MovementType movementType;
    private MovementDate movementDate;

    public SubscriberRequestMovement() {
    }

    public SubscriberRequestMovement(AdvisorId advisorId, SubscriberId subscriberId, MovementType movementType, MovementDate movementDate) {
        this.advisorId = advisorId;
        this.subscriberId = subscriberId;
        this.movementType = movementType;
        this.movementDate = movementDate;
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

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public MovementDate getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(MovementDate movementDate) {
        this.movementDate = movementDate;
    }
}
