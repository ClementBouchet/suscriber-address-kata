package fr.lacombe;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "subscriber", url="http://localhost:8085/subscriber")
public interface SubscriberRepositoryProxy {

    @PostMapping(value = "")
    ResponseEntity<String> modifyAddressOnAllContracts(SubscriberRequestModification subscriberRequestModification);

    @PostMapping(value = "/movement")
    void addMovement(@RequestParam(value = "advisorId") AdvisorId advisorId,
                     @RequestParam(value = "subscriberId") SubscriberId subscriberId,
                     @RequestParam(value = "movementType") MovementType movementType,
                     @RequestParam(value = "movementDate") MovementDate movementDate);

    @PostMapping(value = "/movement")
    ResponseEntity<String> addMovement();
}
