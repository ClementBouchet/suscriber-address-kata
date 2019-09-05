package fr.lacombe.Proxies;

import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.Request.SubscriberRequestMovement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "subscriber", url="http://localhost:8085/subscriber")
public interface SubscriberRepositoryProxy {

    @PostMapping(value = "")
    ResponseEntity<String> modifyAddressOnAllContracts(SubscriberRequestModification subscriberRequestModification);


    @PostMapping(value = "/movement")
    ResponseEntity<String> addMovement(SubscriberRequestMovement subscriberRequestMovement);
}
