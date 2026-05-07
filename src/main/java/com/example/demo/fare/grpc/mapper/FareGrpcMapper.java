package com.example.demo.fare.grpc.mapper;

import com.example.demo.fare.application.FareCalculationCommand;
import com.example.demo.fare.application.FareCalculationResult;
import com.example.demo.fare.grpc.dto.FareCalculationGrpcRequest;
import com.example.demo.fare.grpc.dto.FareCalculationGrpcResponse;
import org.springframework.stereotype.Component;

@Component
public class FareGrpcMapper {

    public FareCalculationCommand toCommand (FareCalculationGrpcRequest request) {
        return new FareCalculationCommand(
                request.baseFare(),
                request.distanceKm(),
                request.waitingMinutes(),
                request.surcharge()
        );
    }

    public FareCalculationGrpcResponse toResponse (FareCalculationResult result) {
        return new FareCalculationGrpcResponse(
                result.subTotal(),
                result.tax(),
                result.total()
        );
    }
}
