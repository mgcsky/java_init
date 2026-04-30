package com.example.demo.user.application;

import com.example.demo.common.constants.ErrorCode;
import com.example.demo.common.constants.MessageKey;
import com.example.demo.common.error.ConflictException;
import com.example.demo.user.domain.UserField;
import org.springframework.dao.DataIntegrityViolationException;

public class UserConstraintViolationMapper {

    public ConflictException parseToConflictException(DataIntegrityViolationException ex) {
        String message = getContentMessage(ex);

        if (message.contains("uk_users_email")) {
            return new ConflictException(
                    ErrorCode.USER_EMAIL_EXISTS,
                    MessageKey.USER_EMAIL_EXISTS,
                    UserField.EMAIL.value()
            );
        } else if (message.contains("uk_users_username")) {
            return new ConflictException(
                    ErrorCode.USERNAME_EXISTS,
                    MessageKey.USERNAME_EXISTS,
                    UserField.USERNAME.value()
            );
        } else if (message.contains("uk_users_phone")) {
            return new ConflictException(
                    ErrorCode.USER_PHONE_EXISTS,
                    MessageKey.USER_PHONE_EXISTS,
                    UserField.PHONE.value()
            );
        }

        return new ConflictException(
                ErrorCode.INTERNAL_ERROR,
                MessageKey.INTERNAL_ERROR,
                null
        );
    }

    private String getContentMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }

        return current.getMessage() == null ? "" : current.getMessage();
    }

}
