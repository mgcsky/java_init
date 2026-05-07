package com.example.demo.fare.application;

import java.math.BigDecimal;

public record FareCalculationResult (
        BigDecimal subTotal,
        BigDecimal tax,
        BigDecimal total
) {}
