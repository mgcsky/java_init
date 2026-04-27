package com.example.demo.user.api.v1.dto;

import java.time.LocalDateTime;
import java.util.Date;

public record RegisterResponse(
        Long id,
        String username,
        String email,
        String phone,
        LocalDateTime createdAt
) {
}
