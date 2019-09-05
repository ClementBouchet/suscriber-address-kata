package fr.lacombe.Model;

import fr.lacombe.Model.Request.SubscriberRequestModification;
import fr.lacombe.Proxies.SubscriberRepositoryProxy;
import org.springframework.beans.factory.annotation.Autowired;


public class Subscriber {

    private ContractList contracts;

    @Autowired
    private SubscriberRepositoryProxy subscriberRepository;

    public Subscriber(SubscriberId subscriberId, SubscriberAddress address) {
    }

    public Subscriber(SubscriberId anyId, SubscriberAddress initialAddress, ContractList contracts) {
        this.contracts = contracts;
    }

    public String modifyAddress(SubscriberRequestModification subscriberRequestModification) {
//        contracts.applyAddressChangeOnAll(newAddress);
        return subscriberRepository.modifyAddressOnAllContracts(subscriberRequestModification).getBody();
    }
}
