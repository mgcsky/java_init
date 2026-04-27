package com.example.demo.user.api.v1.dto;

import com.example.demo.common.constants.UserConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RegisterRequest(
        @NotBlank @Length(min = 6, max = 100, message = "Username should be 6 to 100 character") String username,
        @NotBlank @Length(min = 0, max = 100, message = "Email should be upto 100 character") @Email String email,
        @NotBlank @Length(min = 0, max = 100, message = "Email should be upto 100 character") String password,
        @NotBlank @Length(min = 0, max = 14, message = "Email should be upto 14 character") String phone
        ) {}
