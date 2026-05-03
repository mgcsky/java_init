package com.example.demo.fare.api.v1.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FareHistoryCreatingRequest(
        @NotNull @DecimalMin("0.0") BigDecimal fare,
        @NotNull Long userId
) {}
