package fr.lacombe;

public interface SubscriberDaoInterface {
    void addModificationAddressMovement(AdvisorId advisorId, SubscriberId subscriberId, MovementType susbcriberInfo, MovementDate movementDate);
}
