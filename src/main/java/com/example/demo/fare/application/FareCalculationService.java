package com.example.demo.fare.application;

import com.example.demo.fare.api.v1.dto.FareCalculationRequest;
import com.example.demo.fare.api.v1.dto.FareCalculationResponse;
import com.example.demo.fare.domain.FareCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FareCalculationService {

    private static final Logger log = LoggerFactory.getLogger(FareCalculationService.class);
    private final FareCalculator fareCalculator = new FareCalculator();

    public FareCalculationResponse calculate(FareCalculationRequest fareCalculationRequest) {
        var calculationResult = fareCalculator.calculate(
                fareCalculationRequest.baseFare(),
                fareCalculationRequest.distanceKm(),
                fareCalculationRequest.waitingMinutes(),
                fareCalculationRequest.surcharge()
        );

        return new FareCalculationResponse(
                calculationResult.subtotal(),
                calculationResult.tax(),
                calculationResult.total()
        );
    }
}
