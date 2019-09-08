package fr.lacombe.ClientInterfaces;

import fr.lacombe.Model.Request.HistoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "history", url="http://localhost:8010")
public interface HistoryRepository {

    @PostMapping("/movement")
    ResponseEntity<String> createMovement(HistoryRequest historyRequest);
}
