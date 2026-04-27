package com.example.demo.fare.application;

import com.example.demo.fare.api.v1.FareController;
import com.example.demo.fare.api.v1.dto.FareRequest;
import com.example.demo.fare.api.v1.dto.FareResponse;
import com.example.demo.fare.domain.FareCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FareServices {

    private static final Logger log = LoggerFactory.getLogger(FareServices.class);
    private final FareCalculator fareCalculator = new FareCalculator();

    public FareResponse calculate(FareRequest fareRequest) {
        var calculationResult = fareCalculator.calculate(
                fareRequest.baseFare(),
                fareRequest.distanceKm(),
                fareRequest.waitingMinutes(),
                fareRequest.surcharge()
        );

        return new FareResponse(
                calculationResult.subtotal(),
                calculationResult.tax(),
                calculationResult.total()
        );
    }
}
