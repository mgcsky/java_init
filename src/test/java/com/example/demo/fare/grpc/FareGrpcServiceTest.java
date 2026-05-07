package com.example.demo.fare.grpc;

import com.example.demo.fare.application.FareCalculationCommand;
import com.example.demo.fare.application.FareCalculationResult;
import com.example.demo.fare.application.FareCalculationService;
import com.example.demo.fare.grpc.dto.FareCalculationGrpcRequest;
import com.example.demo.fare.grpc.dto.FareCalculationGrpcResponse;
import com.example.demo.fare.grpc.mapper.FareGrpcMapper;
import com.example.demo.grpc.fare.v1.CalculateFareRequest;
import com.example.demo.grpc.fare.v1.CalculateFareResponse;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FareGrpcServiceTest {

    @Mock
    FareCalculationService fareCalculationService;

    @Mock
    Validator validator;

    @Mock
    FareGrpcMapper mapper;

    @InjectMocks
    FareGrpcService service;

    @Test
    void calculation_shouldReturnResponse_WhenValidRequest() {
        CalculateFareRequest request = CalculateFareRequest.newBuilder()
                .setBaseFare("10")
                .setDistanceKm(1)
                .setWaitingMinutes(5)
                .setSurcharge("2")
                .build();

        FareCalculationGrpcRequest grpcRequest = new FareCalculationGrpcRequest(
                BigDecimal.valueOf(10),
                1,
                5,
                BigDecimal.valueOf(2)
        );
        FareCalculationCommand command = new FareCalculationCommand(
                BigDecimal.valueOf(10),
                1,
                5,
                BigDecimal.valueOf(2)
        );
        FareCalculationResult result = new FareCalculationResult(
                BigDecimal.valueOf(17),
                BigDecimal.valueOf(1.7),
                BigDecimal.valueOf(18.7)
        );
        FareCalculationGrpcResponse mappedResponse = new FareCalculationGrpcResponse(
                BigDecimal.valueOf(17),
                BigDecimal.valueOf(1.7),
                BigDecimal.valueOf(18.7)
        );
        @SuppressWarnings("unchecked")
        StreamObserver<CalculateFareResponse> responseObserver = mock(StreamObserver.class);

        when(validator.validate(grpcRequest)).thenReturn(Collections.emptySet());
        when(mapper.toCommand(any(FareCalculationGrpcRequest.class))).thenReturn(command);
        when(fareCalculationService.calculate(command)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(mappedResponse);

        service.calculate(request, responseObserver);

        verify(responseObserver).onNext(any(CalculateFareResponse.class));
        verify(responseObserver).onCompleted();
        verify(responseObserver, never()).onError(any());
    }

    @Test
    void calculation_shouldReturnValidationError_WhenInvalidRequest() {
        CalculateFareRequest request = CalculateFareRequest.newBuilder()
                .setBaseFare("10")
                .setDistanceKm(1)
                .setWaitingMinutes(5)
                .setSurcharge("2")
                .build();

        @SuppressWarnings("unchecked")
        StreamObserver<CalculateFareResponse> responseObserver = mock(StreamObserver.class);
        @SuppressWarnings("unchecked")
        ConstraintViolation<FareCalculationGrpcRequest> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("baseFare must be greater than or equal to 0.0");
        when(validator.validate(any(FareCalculationGrpcRequest.class))).thenReturn(Set.of(violation));

        service.calculate(request, responseObserver);

        verify(responseObserver).onError(any(StatusRuntimeException.class));
        verify(responseObserver, never()).onNext(any(CalculateFareResponse.class));
        verify(responseObserver, never()).onCompleted();
    }

    @Test
    void calculation_shouldReturnInvalidArgument_WhenInvalidDecimalFormat() {
        CalculateFareRequest request = CalculateFareRequest.newBuilder()
                .setBaseFare("abc")
                .setDistanceKm(1)
                .setWaitingMinutes(5)
                .setSurcharge("2")
                .build();

        @SuppressWarnings("unchecked")
        StreamObserver<CalculateFareResponse> responseObserver = mock(StreamObserver.class);

        service.calculate(request, responseObserver);

        verify(responseObserver).onError(any(StatusRuntimeException.class));
        verify(responseObserver, never()).onNext(any(CalculateFareResponse.class));
        verify(responseObserver, never()).onCompleted();
    }
}
