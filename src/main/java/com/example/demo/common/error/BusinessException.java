package com.example.demo.common.error;

import com.example.demo.common.constants.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class BusinessException extends RuntimeException {
    private final ErrorCode code;
    private final HttpStatus status;
    private final String messageKey;
    private final String field;
    private final Map<String, String> fieldErrors;

    public BusinessException(ErrorCode code, HttpStatus status, String messageKey, String field) {
        super(messageKey);
        this.code = code;
        this.status = status;
        this.messageKey = messageKey;
        this.field = field;
        this.fieldErrors = Map.of();
    }

    public ErrorCode getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getField() {
        return field;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
