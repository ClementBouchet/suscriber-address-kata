package fr.lacombe;

import org.springframework.beans.factory.annotation.Autowired;

public class SubscriberController {

    private ContractList contracts;

    @Autowired
    private SubscriberRepositoryProxy subscriberRepository;

    public SubscriberController(SubscriberId subscriberId, SubscriberAddress address) {
    }

    public SubscriberController(SubscriberId anyId, SubscriberAddress initialAddress, ContractList contracts) {
        this.contracts = contracts;
    }

    public String modifyAddress(SubscriberRequestModification subscriberRequestModification) {
//        contracts.applyAddressChangeOnAll(newAddress);
        return subscriberRepository.modifyAddress(subscriberRequestModification).getBody();
    }
}
