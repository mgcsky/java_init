package com.example.demo.fare.grpc.mapper;

import com.example.demo.fare.application.FareCalculationCommand;
import com.example.demo.fare.application.FareCalculationResult;
import com.example.demo.fare.grpc.dto.FareCalculationGrpcRequest;
import com.example.demo.fare.grpc.dto.FareCalculationGrpcResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class FareGrpcMapperTest {

    @InjectMocks
    FareGrpcMapper mapper;

    @Test
    void mapper_shouldReturnCommand_WhenProcessRequest () {
        FareCalculationGrpcRequest request = new FareCalculationGrpcRequest(
                BigDecimal.valueOf(10),
                1,
                5,
                BigDecimal.valueOf(10)
        );

        FareCalculationCommand command = mapper.toCommand(request);

        Assertions.assertNotNull(command);
        Assertions.assertEquals(BigDecimal.valueOf(10), command.getBaseFare());
        Assertions.assertEquals(BigDecimal.valueOf(10), command.getSurcharge());
        Assertions.assertEquals(1, command.getDistanceKm());
        Assertions.assertEquals(5, command.getWaitingMinutes());
    }

    @Test
    void mapper_shouldReturnResponse_WhenProcessResult() {
        FareCalculationResult result = new FareCalculationResult(
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(15)
        );

        FareCalculationGrpcResponse response = mapper.toResponse(result);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(BigDecimal.valueOf(10), response.subtotal());
        Assertions.assertEquals(BigDecimal.valueOf(5), response.tax());
        Assertions.assertEquals(BigDecimal.valueOf(15), response.total());
    }
}
