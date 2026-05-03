package com.example.demo.fare.api.v1.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FareHistoryItemResponse(
        Long id,
        Long userId,
        BigDecimal fare,
        LocalDateTime createdAt
) {
}
