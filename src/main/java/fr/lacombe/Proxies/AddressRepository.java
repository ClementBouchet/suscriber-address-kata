package fr.lacombe.Proxies;

import fr.lacombe.Model.SubscriberId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "address", url="http://localhost:8008")
public interface AddressRepository {

    @RequestMapping(method = RequestMethod.POST, value = "/address")
    ResponseEntity<String> getCountryAddress(SubscriberId subscriberId);

    @GetMapping(value = "/address")
    ResponseEntity<String> getCountryAddress();
}
