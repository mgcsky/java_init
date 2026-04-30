package com.example.demo.user.application;

import com.example.demo.common.constants.ErrorCode;
import com.example.demo.common.constants.MessageKey;
import com.example.demo.common.error.ConflictException;
import com.example.demo.user.api.v1.dto.RegisterRequest;
import com.example.demo.user.api.v1.dto.RegisterResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserField;
import com.example.demo.user.infrastructure.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserService {

    public final UserRepository userRepository;
    public final BCryptPasswordEncoder passwordEncoder;
    public final UserConstraintViolationMapper userConstraintViolationMapper = new UserConstraintViolationMapper();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException(
                    ErrorCode.USER_EMAIL_EXISTS,
                    MessageKey.USER_EMAIL_EXISTS,
                    UserField.EMAIL.value());
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException(
                    ErrorCode.USERNAME_EXISTS,
                    MessageKey.USERNAME_EXISTS,
                    UserField.USERNAME.value());
        }
        if (userRepository.existsByPhone(request.phone())) {
            throw new ConflictException(
                    ErrorCode.USER_PHONE_EXISTS,
                    MessageKey.USER_PHONE_EXISTS,
                    UserField.PHONE.value());
        }

        try {
            String hashedPassword = passwordEncoder.encode(request.password());

            User user = new User(
                    request.username(),
                    request.email(),
                    request.phone(),
                    hashedPassword
            );

            User savedUser = userRepository.saveAndFlush(user);

            return new RegisterResponse(
                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getEmail(),
                    savedUser.getPhone(),
                    savedUser.getCreatedAt());

        } catch (DataIntegrityViolationException ex) {
            throw userConstraintViolationMapper.parseToConflictException(ex);
        }

    }
}
