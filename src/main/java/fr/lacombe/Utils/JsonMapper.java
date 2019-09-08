package fr.lacombe.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lacombe.Model.ContractList;
import fr.lacombe.Model.Country;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonMapper extends ObjectMapper {


    public ContractList mapJsonToContractList(ResponseEntity<String> contractRepositoryResponse) throws IOException {
        return null;
    }

    public Country mapJsonToCountry(ResponseEntity<String> addressRepositoryResponse) throws IOException {
        return this.readValue(addressRepositoryResponse.getBody(), Country.class);
    }
}
