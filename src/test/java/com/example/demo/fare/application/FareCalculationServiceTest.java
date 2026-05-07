package com.example.demo.fare.application;

import com.example.demo.fare.domain.FareCalculator;
import com.example.demo.fare.domain.FareCalculator.FareResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FareCalculationServiceTest {

    @Mock
    FareCalculator fareCalculator;

    @InjectMocks
    FareCalculationService fareCalculationService;

    @Test
    void calculatorService_shouldReturnResponse_whenValidRequest() {
        FareCalculationCommand command = new FareCalculationCommand(
                BigDecimal.valueOf(2.0),
                10,
                10,
                BigDecimal.valueOf(4.0)
        );

        FareResult fareResult = new FareResult(
                BigDecimal.valueOf(20.00),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(25.0)
        );

        when(fareCalculator.calculate(
                any(),
                anyInt(),
                anyInt(),
                any()))
                .thenReturn(fareResult);

        FareCalculationResult result = fareCalculationService.calculate(command);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.valueOf(20.00), result.subTotal());
        Assertions.assertEquals(BigDecimal.valueOf(5.0), result.tax());
        Assertions.assertEquals(BigDecimal.valueOf(25.0),result.total());
    }
}
