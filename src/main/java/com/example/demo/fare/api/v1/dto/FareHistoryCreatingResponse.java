package com.example.demo.fare.api.v1.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FareHistoryCreatingResponse(
        Long id,
        BigDecimal fare,
        Long userId,
        LocalDateTime createdAt
) {
}
