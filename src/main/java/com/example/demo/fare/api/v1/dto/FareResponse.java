package com.example.demo.fare.api.v1.dto;

import java.math.BigDecimal;

public record FareResponse(
    BigDecimal subtotal,
    BigDecimal tax,
    BigDecimal total
) {}
