package fr.lacombe.Controller;

import fr.lacombe.ClientInterfaces.AddressRepository;
import fr.lacombe.ClientInterfaces.ContractRepository;
import fr.lacombe.ClientInterfaces.HistoryRepository;
import fr.lacombe.Model.AdvisorId;
import fr.lacombe.Model.ContractList;
import fr.lacombe.Model.Country;
import fr.lacombe.Model.MovementType;
import fr.lacombe.Model.Request.ContractListRequest;
import fr.lacombe.Model.Request.HistoryRequest;
import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Model.SubscriberId;
import fr.lacombe.Utils.JsonMapper;
import fr.lacombe.Utils.SubscriberControllerContext;
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

    @Autowired
    AddressRepository addressRepository;

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

        ResponseEntity<String> contractRepositoryResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        SubscriberId subscriberId = subscriberRequestModification.getSubscriberId();

        Country country = getCountryAddressFromRepositoryBy(subscriberId);

        if(country.isFrance()){
            ContractList contractList = getContractListFromRepositoryBy(subscriberId);
            contractList.modifySubscriberAddressOnAllContracts(subscriberRequestModification.getSubscriberAddress());
            contractRepositoryResponse = contractRepository.saveContracts(new ContractListRequest(contractList));
        }
        if(contractRepositoryResponse.getStatusCode().equals(HttpStatus.OK)){
            HistoryRequest historyRequest = buildHistoryRequest(subscriberId);
            return historyRepository.createMovement(historyRequest);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    private ContractList getContractListFromRepositoryBy(SubscriberId subscriberId) throws IOException {
        ResponseEntity<String> contractRepositoryResponse;
        contractRepositoryResponse = contractRepository.getAllContractsFromSubscriber(subscriberId.getId());
        return jsonMapper.mapJsonToContractList(contractRepositoryResponse);
    }

    private HistoryRequest buildHistoryRequest(SubscriberId subscriberId) {
        AdvisorId advisorId = applicationContext.getAdvisorId();
        return new HistoryRequest(timeProvider.now(), advisorId, subscriberId, MovementType.SUSBCRIBER_INFO);
    }

    private Country getCountryAddressFromRepositoryBy(SubscriberId subscriberId) throws IOException {
        ResponseEntity<String> addressRepositoryResponse = addressRepository.getCountryAddress(subscriberId.getId());
        return jsonMapper.mapJsonToCountry(addressRepositoryResponse);
    }

}
