package com.example.demo.fare.api.v1;

import com.example.demo.fare.api.v1.dto.FareRequest;
import com.example.demo.fare.api.v1.dto.FareResponse;
import com.example.demo.fare.application.FareServices;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/fares")
public class FareController {

    private static final Logger log = LoggerFactory.getLogger(FareController.class);

    private final FareServices fareServices;

    public FareController(FareServices fareServices) {
        this.fareServices = fareServices;
    }

    @PostMapping("/calculate")
    public ResponseEntity<FareResponse> calculate(@Valid @RequestBody FareRequest fareRequest) {
        log.info("Fare calculate request received: {}", fareRequest);

        FareResponse response = fareServices.calculate(fareRequest);

        log.info("Fare calculate request response: {}", fareRequest);

        return ResponseEntity.ok(response);
    }
}
