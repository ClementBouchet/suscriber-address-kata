package fr.lacombe.Proxies;

import fr.lacombe.Model.Login;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="authenticator", url = "http://localhost:8084")
public interface AuthenticationService {

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    ResponseEntity<String> authenticate(Login login);

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    ResponseEntity authenticate();
}
