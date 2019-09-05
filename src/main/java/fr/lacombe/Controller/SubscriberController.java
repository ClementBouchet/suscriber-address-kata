package fr.lacombe.Controller;

import fr.lacombe.Model.MovementType;
import fr.lacombe.Proxies.SubscriberRepositoryProxy;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.Request.SubscriberRequestMovement;
import fr.lacombe.Utils.TimeProvider;
import fr.lacombe.Utils.TimeProviderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriberController {

    @Autowired
    private TimeProviderInterface timeProvider;


    @Autowired
    SubscriberRepositoryProxy subscriberRepositoryProxy;

    @PostMapping(value = "/address/modification")
    public ResponseEntity<String> modifyAddress(SubscriberRequestModification subscriberRequestModification){
        SubscriberRequestMovement subscriberRequestMovement = setUpSubscriberRequestMovement(subscriberRequestModification);
        subscriberRepositoryProxy.addMovement(subscriberRequestMovement);
        return subscriberRepositoryProxy.modifyAddressOnAllContracts(subscriberRequestModification);
    }

    private SubscriberRequestMovement setUpSubscriberRequestMovement(SubscriberRequestModification subscriberRequestModification) {
        SubscriberRequestMovement subscriberRequestMovement = new SubscriberRequestMovement();
        subscriberRequestMovement.setAdvisorId(subscriberRequestModification.getAdvisorId());
        subscriberRequestMovement.setMovementDate(timeProvider.now());
        subscriberRequestMovement.setSubscriberId(subscriberRequestModification.getSubscriberId());
        subscriberRequestMovement.setMovementType(MovementType.SUSBCRIBER_INFO);
        return subscriberRequestMovement;
    }

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }
}
