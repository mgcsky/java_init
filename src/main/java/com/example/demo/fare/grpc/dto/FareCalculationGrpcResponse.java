package com.example.demo.fare.grpc.dto;

import java.math.BigDecimal;

public record FareCalculationGrpcResponse(
    BigDecimal subtotal,
    BigDecimal tax,
    BigDecimal total
) {}
