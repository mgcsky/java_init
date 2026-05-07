package com.example.demo.fare.grpc;

import com.example.demo.fare.application.FareCalculationResult;
import com.example.demo.fare.application.FareCalculationService;
import com.example.demo.fare.grpc.dto.FareCalculationGrpcRequest;
import com.example.demo.fare.grpc.dto.FareCalculationGrpcResponse;
import com.example.demo.fare.grpc.mapper.FareGrpcMapper;
import com.example.demo.grpc.fare.v1.CalculateFareRequest;
import com.example.demo.grpc.fare.v1.CalculateFareResponse;
import com.example.demo.grpc.fare.v1.FareServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class FareGrpcService extends FareServiceGrpc.FareServiceImplBase {
    private final FareCalculationService fareCalculationService;
    private final Validator validator;
    private final FareGrpcMapper mapper;

    public FareGrpcService(
            FareCalculationService fareCalculationService,
            Validator validator,
            FareGrpcMapper mapper
    ) {
        this.fareCalculationService = fareCalculationService;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public void calculate(
            CalculateFareRequest request,
            StreamObserver<CalculateFareResponse> responseObserver
    ) {
        try {

            FareCalculationGrpcRequest dto = new FareCalculationGrpcRequest(
                    parseDecimal(request.getBaseFare(), "base_fare"),
                    request.getDistanceKm(),
                    request.getWaitingMinutes(),
                    parseDecimal(request.getSurcharge(), "surcharge")
            );

            Set<ConstraintViolation<FareCalculationGrpcRequest>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                String msg = violations.iterator().next().getMessage();
                responseObserver.onError(
                        Status.INVALID_ARGUMENT.withDescription(msg).asRuntimeException()
                );

                return;
            }

            FareCalculationResult result = fareCalculationService.calculate(mapper.toCommand(dto));
            FareCalculationGrpcResponse response = mapper.toResponse(result);

            CalculateFareResponse gRPCResponse = CalculateFareResponse.newBuilder()
                    .setSubtotal(response.subtotal().toPlainString())
                    .setTax(response.tax().toPlainString())
                    .setTotal(response.total().toPlainString())
                    .build();

            responseObserver.onNext(gRPCResponse);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException iaex) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription(iaex.getMessage()).asRuntimeException()
            );
        } catch (Exception ex) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Internal server error").withCause(ex).asRuntimeException()
            );
        }
    }

    private static BigDecimal parseDecimal(String raw, String fieldName) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        try {
            return new BigDecimal(raw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid decimal");
        }
    }
}
