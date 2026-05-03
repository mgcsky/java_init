package com.example.demo.fare.api.v1.dto;

import java.math.BigDecimal;

public record FareCalculationResponse(
    BigDecimal subtotal,
    BigDecimal tax,
    BigDecimal total
) {}
