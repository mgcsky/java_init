package com.example.demo.fare.api.v1;

import com.example.demo.fare.api.v1.dto.*;
import com.example.demo.fare.application.FareCalculationService;
import com.example.demo.fare.application.FareHistoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/v1/fares")
public class FareController {

    private static final Logger log = LoggerFactory.getLogger(FareController.class);

    private final FareCalculationService fareCalculationService;
    private final FareHistoryService fareHistoryService;

    public FareController(FareCalculationService fareCalculationService, FareHistoryService fareHistoryService) {
        this.fareCalculationService = fareCalculationService;
        this.fareHistoryService = fareHistoryService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<FareCalculationResponse> calculate(@Valid @RequestBody FareCalculationRequest fareCalculationRequest) {
        log.info("Fare calculate request received: {}", fareCalculationRequest);

        FareCalculationResponse response = fareCalculationService.calculate(fareCalculationRequest);

        log.info("Fare calculate request response: {}", fareCalculationRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/records")
    public ResponseEntity<FareHistoryCreatingResponse> record(@Valid @RequestBody FareHistoryCreatingRequest fareHistoryCreatingRequest) {
        FareHistoryCreatingResponse response = fareHistoryService.insert(fareHistoryCreatingRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/histories-collection-example")
    public ResponseEntity<FareHistoryListResponse> collectionHistories(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) BigDecimal minFare,
            @RequestParam(defaultValue = "created,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        FareHistoryListResponse histories = fareHistoryService.getHistoriesWithCollection(userId, minFare, sort, page, size);

        return ResponseEntity.ok(histories);
    }

    @GetMapping("/histories")
    public  ResponseEntity<FareHistoryListResponse> histories(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) BigDecimal minFare,
            @RequestParam(defaultValue = "created,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        FareHistoryListResponse histories = fareHistoryService.getHistories(userId, minFare, sort, page, size);

        return ResponseEntity.ok(histories);
    }

    @GetMapping("/histories-with-specification")
    public  ResponseEntity<FareHistoryListResponse> historiesWithSpecification(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) BigDecimal minFare,
            @RequestParam(defaultValue = "created,desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        FareHistoryListResponse histories = fareHistoryService.getHistoriesWithSpecification(userId, minFare, sort, page, size);

        return ResponseEntity.ok(histories);
    }
}
