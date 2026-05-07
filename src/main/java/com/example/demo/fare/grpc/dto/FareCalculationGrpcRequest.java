package com.example.demo.fare.grpc.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record FareCalculationGrpcRequest(
    @NotNull @DecimalMin("0.0") BigDecimal baseFare,
    @NotNull @Min(0) Integer distanceKm,
    @NotNull @Min(0) Integer waitingMinutes,
    @NotNull @DecimalMin("0.0") BigDecimal surcharge
) {}
