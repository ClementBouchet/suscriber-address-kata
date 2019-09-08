package fr.lacombe.Controller;

import fr.lacombe.Model.ContractList;
import fr.lacombe.Model.Country;
import fr.lacombe.Model.MovementType;
import fr.lacombe.Model.Request.ContractListRequest;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.Request.SubscriberRequestMovement;
import fr.lacombe.Model.SubscriberId;
import fr.lacombe.Proxies.AddressRepository;
import fr.lacombe.Proxies.ContractRepository;
import fr.lacombe.Proxies.SubscriberRepository;
import fr.lacombe.Utils.JsonMapper;
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
    SubscriberRepository subscriberRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ContractList contractList;

    @Autowired
    JsonMapper jsonMapper;

    @Autowired
    private ContractRepository contractRepository;

    @PostMapping(value = "/address/modification")
    public ResponseEntity<String> modifyAddress(SubscriberRequestModification subscriberRequestModification) throws IOException {

        SubscriberId subscriberId = subscriberRequestModification.getSubscriberId();
        ResponseEntity<String> addressRepositoryResponse = addressRepository.getCountryAddress(subscriberId.getId());
        Country country = jsonMapper.mapJsonToCountry(addressRepositoryResponse);
        if(country.isFrance()){
            ResponseEntity<String> contractRepositoryResponse = contractRepository.getAllContractsFromSubscriber(subscriberId.getId());
            jsonMapper.mapJsonToContractList(contractRepositoryResponse);
            contractList.modifySubscriberAddressOnAllContracts(subscriberRequestModification.getSubscriberAddress());
            ContractListRequest contractListRequest = new ContractListRequest(contractList);
            contractRepositoryResponse = contractRepository.saveContracts(contractListRequest);
        }
        //SubscriberRequestMovement subscriberRequestMovement = setUpSubscriberRequestMovement(subscriberRequestModification);
        //subscriberRepositoryProxy.addMovement(subscriberRequestMovement);
        //return subscriberRepositoryProxy.modifyAddressOnAllContracts(subscriberRequestModification);
        return null;
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
