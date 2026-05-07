package com.example.demo.fare.api.v1.mapper;

import com.example.demo.fare.api.v1.dto.FareCalculationRequest;
import com.example.demo.fare.api.v1.dto.FareCalculationResponse;
import com.example.demo.fare.application.FareCalculationCommand;
import com.example.demo.fare.application.FareCalculationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class FareRestMapperTest {

    @InjectMocks
    FareRestMapper mapper;

    @Test
    void mapper_shouldReturnCommand_WhenProcessRequest() {
        FareCalculationRequest request = new FareCalculationRequest(
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

        FareCalculationResponse response = mapper.toResponse(result);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(BigDecimal.valueOf(10), response.subtotal());
        Assertions.assertEquals(BigDecimal.valueOf(5), response.tax());
        Assertions.assertEquals(BigDecimal.valueOf(15), response.total());
    }
}
