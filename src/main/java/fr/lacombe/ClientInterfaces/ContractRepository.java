package fr.lacombe.ClientInterfaces;

import fr.lacombe.Model.Request.ContractListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "contracts", url="http://localhost:8009")
public interface ContractRepository {

    @GetMapping("/contract/{subscriberID}")
    ResponseEntity<String> getAllContractsFromSubscriber(@PathVariable(name = "subscriberID") String subscriberID);

    @PostMapping("/contract")
    ResponseEntity<String> saveContracts(ContractListRequest contractListRequest);
}
