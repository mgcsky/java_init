package com.example.demo.common.error;

import com.example.demo.common.constants.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ConflictException extends BusinessException{
    public ConflictException(ErrorCode code, String messageKey, String field) {
        super(code, HttpStatus.CONFLICT, messageKey, field);
    }
}
