package fr.lacombe.ClientInterfaces;

import fr.lacombe.Model.Login;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="authenticator", url = "http://localhost:8007")
public interface AuthenticationService {

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate/{canal}")
    ResponseEntity<String> authenticate(Login login, @PathVariable(name = "canal") String canal);
}
