package com.example.demo.common.error;

import com.example.demo.common.constants.ErrorCode;
import com.example.demo.common.constants.MessageKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (a, b) -> a
                ));
        String message = messageSource.getMessage(MessageKey.INVALID_REQUEST, null, "Invalid request", LocaleContextHolder.getLocale());
        return ResponseEntity.badRequest().body(
                new ApiError(ErrorCode.VALIDATION_ERROR.name(), message, 400, Instant.now(), fieldErrors)
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex) {
        String resolvedMessage = messageSource.getMessage(
                ex.getMessageKey(),
                null,
                ex.getMessageKey(),
                LocaleContextHolder.getLocale()
        );

        Map<String, String> fieldErrors = Map.of();

        if (ex.getField() != null) {
            fieldErrors = Map.of(ex.getField(), resolvedMessage);
        }



        return ResponseEntity.status(ex.getStatus()).body(
                new ApiError(
                        ex.getCode().name(),
                        resolvedMessage,
                        ex.getStatus().value(),
                        Instant.now(),
                        fieldErrors
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        log.error("[GlobalExceptionHandler].[handleUnexpected]: " + ex.getLocalizedMessage());
        return ResponseEntity.internalServerError().body(
                new ApiError("INTERNAL_ERROR", "Unexpected error", 500, Instant.now(), Map.of())
        );
    }
}
