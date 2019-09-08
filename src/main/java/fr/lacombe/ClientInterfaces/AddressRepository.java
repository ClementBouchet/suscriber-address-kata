package fr.lacombe.ClientInterfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "address", url="http://localhost:8008")
public interface AddressRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/address/{subscriberId}")
    ResponseEntity<String> getCountryAddress(@PathVariable(name = "subscriberId") String subscriberId);

}
