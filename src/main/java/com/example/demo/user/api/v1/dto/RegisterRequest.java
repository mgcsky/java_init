package com.example.demo.user.api.v1.dto;

import com.example.demo.common.constants.UserConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Length(min = 6, max = 100, message = "Username must be 6 to 100 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Length(max = 100, message = "Email must be at most 100 characters")
        @Email(message = "Email format is invalid")
        String email,

        @NotBlank(message = "Password is required")
        @Length(min = 8, max = 100, message = "Password must be 8 to 100 characters")
        String password,

        @NotBlank(message = "Phone is required")
        @Length(max = 14, message = "Phone must be at most 14 characters")
        String phone
) {}
