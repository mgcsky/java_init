package com.example.demo.fare.api.v1.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record FareCalculationRequest(
    @NotNull @DecimalMin("0.0") BigDecimal baseFare,
    @NotNull @Min(0) Integer distanceKm,
    @NotNull @Min(0) Integer waitingMinutes,
    @NotNull @DecimalMin("0.0") BigDecimal surcharge
) {}
