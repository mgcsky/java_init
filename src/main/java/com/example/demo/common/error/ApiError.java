package com.example.demo.common.error;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        String code,
        String message,
        int status,
        Instant timestamp,
        Map<String, String> fieldErrors
) {
}
