package fr.lacombe.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lacombe.Model.ContractList;
import fr.lacombe.Model.Country;
import fr.lacombe.Model.MovementType;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.Request.SubscriberRequestMovement;
import fr.lacombe.Model.SubscriberId;
import fr.lacombe.Proxies.AddressRepository;
import fr.lacombe.Proxies.ContractRepository;
import fr.lacombe.Proxies.SubscriberRepositoryProxy;
import fr.lacombe.Utils.TimeProvider;
import fr.lacombe.Utils.TimeProviderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SubscriberController {

    @Autowired
    private TimeProviderInterface timeProvider;

    @Autowired
    SubscriberRepositoryProxy subscriberRepositoryProxy;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ContractList contractList;

    @Autowired
    private ContractRepository contractRepository;

    @PostMapping(value = "/address/modification")
    public ResponseEntity<String> modifyAddress(SubscriberRequestModification subscriberRequestModification) throws IOException {

        SubscriberId subscriberId = subscriberRequestModification.getSubscriberId();
        ResponseEntity<String> addressRepositoryResponse = addressRepository.getCountryAddress(subscriberId);
        Country country = mapJsonToCountry(addressRepositoryResponse);
        if(country.isFrance()){
            ResponseEntity<String> contractRepositoryResponse = contractRepository.getAllContractsFromSubscriber(subscriberId.getId());
            mapJsonToContractList(contractRepositoryResponse);
            contractList.modifySubscriberAddressOnAllContracts(subscriberRequestModification.getSubscriberAddress());
        }
        //SubscriberRequestMovement subscriberRequestMovement = setUpSubscriberRequestMovement(subscriberRequestModification);
        //subscriberRepositoryProxy.addMovement(subscriberRequestMovement);
        //return subscriberRepositoryProxy.modifyAddressOnAllContracts(subscriberRequestModification);
        return null;
    }

    private void mapJsonToContractList(ResponseEntity<String> contractRepositoryResponse) throws IOException {
    }

    private Country mapJsonToCountry(ResponseEntity<String> addressRepositoryResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(addressRepositoryResponse.getBody(), Country.class);
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
