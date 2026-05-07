package com.example.demo.fare.api.v1.mapper;

import com.example.demo.fare.api.v1.dto.FareCalculationRequest;
import com.example.demo.fare.api.v1.dto.FareCalculationResponse;
import com.example.demo.fare.application.FareCalculationCommand;
import com.example.demo.fare.application.FareCalculationResult;
import org.springframework.stereotype.Component;

@Component
public class FareRestMapper {

    public FareCalculationCommand toCommand (FareCalculationRequest request) {
        return new FareCalculationCommand(
                request.baseFare(),
                request.distanceKm(),
                request.waitingMinutes(),
                request.surcharge()
        );
    }

    public FareCalculationResponse toResponse (FareCalculationResult result) {
        return new FareCalculationResponse(
                result.subTotal(),
                result.tax(),
                result.total()
        );
    }
}
