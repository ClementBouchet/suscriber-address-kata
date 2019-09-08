package fr.lacombe.Controller;

import fr.lacombe.Model.AdvisorId;
import fr.lacombe.Model.ContractList;
import fr.lacombe.Model.Country;
import fr.lacombe.Model.MovementType;
import fr.lacombe.Model.Request.ContractListRequest;
import fr.lacombe.Model.Request.HistoryRequest;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.Request.SubscriberRequestMovement;
import fr.lacombe.Model.SubscriberId;
import fr.lacombe.Proxies.AddressRepository;
import fr.lacombe.Proxies.ContractRepository;
import fr.lacombe.Proxies.HistoryRepository;
import fr.lacombe.Utils.JsonMapper;
import fr.lacombe.Utils.SubscriberControllerContext;
import fr.lacombe.Utils.TimeProvider;
import fr.lacombe.Utils.TimeProviderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SubscriberController {

    @Autowired
    private TimeProviderInterface timeProvider;

//    @Autowired
//    SubscriberRepository subscriberRepository;

    @Autowired
    AddressRepository addressRepository;

//    @Autowired
//    ContractList contractList;

    @Autowired
    JsonMapper jsonMapper;

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private SubscriberControllerContext applicationContext;

    @PostMapping(value = "/address/modification")
    public ResponseEntity<String> modifyAddress(SubscriberRequestModification subscriberRequestModification) throws IOException {

        SubscriberId subscriberId = subscriberRequestModification.getSubscriberId();
        ResponseEntity<String> addressRepositoryResponse = addressRepository.getCountryAddress(subscriberId.getId());
        Country country = jsonMapper.mapJsonToCountry(addressRepositoryResponse);
        ResponseEntity<String> contractRepositoryResponse = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        if(country.isFrance()){
            contractRepositoryResponse = contractRepository.getAllContractsFromSubscriber(subscriberId.getId());
            ContractList contractList = jsonMapper.mapJsonToContractList(contractRepositoryResponse);
            contractList.modifySubscriberAddressOnAllContracts(subscriberRequestModification.getSubscriberAddress());
            ContractListRequest contractListRequest = new ContractListRequest(contractList);
            contractRepositoryResponse = contractRepository.saveContracts(contractListRequest);
        }
        if(contractRepositoryResponse.getStatusCode().equals(HttpStatus.OK)){
            AdvisorId advisorId = applicationContext.getAdvisorId();
            HistoryRequest historyRequest = new HistoryRequest(timeProvider.now(), advisorId, subscriberId, MovementType.SUSBCRIBER_INFO);
            return historyRepository.createMovement(historyRequest);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
