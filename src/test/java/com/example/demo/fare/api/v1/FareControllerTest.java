package com.example.demo.fare.api.v1;

import com.example.demo.fare.api.v1.dto.FareCalculationRequest;
import com.example.demo.fare.api.v1.dto.FareCalculationResponse;
import com.example.demo.fare.api.v1.mapper.FareRestMapper;
import com.example.demo.fare.application.FareCalculationCommand;
import com.example.demo.fare.application.FareCalculationResult;
import com.example.demo.fare.application.FareCalculationService;
import com.example.demo.fare.application.FareHistoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FareControllerTest {

    @Mock
    FareCalculationService fareCalculationService;

    @Mock
    FareHistoryService fareHistoryService;

    @Mock
    FareRestMapper mapper;

    @InjectMocks
    FareController fareController;

    @Test
    void calculation_shouldReturnResponse_WhenValidCall() {
        FareCalculationRequest request = new FareCalculationRequest(
                BigDecimal.valueOf(10),
                1,
                5,
                BigDecimal.valueOf(5)
        );

        FareCalculationCommand command = new FareCalculationCommand(
                BigDecimal.valueOf(10),
                1,
                5,
                BigDecimal.valueOf(5)
        );

        FareCalculationResult result = new FareCalculationResult(
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(20)
        );

        FareCalculationResponse res = new FareCalculationResponse(
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(20)
        );

        when(mapper.toCommand(request)).thenReturn(command);
        when(fareCalculationService.calculate(command)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(res);

        ResponseEntity<FareCalculationResponse> response = fareController.calculate(request);

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(BigDecimal.valueOf(15), response.getBody().subtotal());
        Assertions.assertEquals(BigDecimal.valueOf(5), response.getBody().tax());
        Assertions.assertEquals(BigDecimal.valueOf(20), response.getBody().total());
    }

}
