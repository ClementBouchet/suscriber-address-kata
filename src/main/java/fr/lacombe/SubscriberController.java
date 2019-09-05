package fr.lacombe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriberController {

    public SubscriberController() {
    }

    @Autowired
    SubscriberRepositoryProxy subscriberRepositoryProxy;

    @PostMapping(value = "/address/modification")
    public ResponseEntity<String> modifyAddress(SubscriberRequestModification subscriberRequestModification){
        return subscriberRepositoryProxy.modifyAddressOnAllContracts(subscriberRequestModification);
    }
}
