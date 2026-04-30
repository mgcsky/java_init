package com.example.demo.user.application;

import com.example.demo.common.error.ConflictException;
import com.example.demo.user.api.v1.dto.RegisterRequest;
import com.example.demo.user.api.v1.dto.RegisterResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void register_shouldThrow_whenEmailExist() {
        RegisterRequest request = new RegisterRequest(
                "abion01",
                "abion@gmail.com",
                "apc12345",
                "+10293423233");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        Assertions.assertThrows(ConflictException.class, () -> userService.register(request));
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void register_shouldThrow_WhenUsernameExist() {
        RegisterRequest request = new RegisterRequest(
                "abion01",
                "abion@gmail.com",
                "apc12345",
                "+10293423233");
        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        Assertions.assertThrows(ConflictException.class, ()->userService.register(request));
        verify(userRepository, never()).saveAndFlush(any());
    }


    @Test
    void register_shouldThrow_WhenPhoneExist() {
        RegisterRequest request = new RegisterRequest(
                "abion01",
                "abion@gmail.com",
                "apc12345",
                "+10293423233");
        when(userRepository.existsByPhone(request.phone())).thenReturn(true);

        Assertions.assertThrows(ConflictException.class, ()->userService.register(request));
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void register_shouldReturnResponse_whenValidRequest() {
        RegisterRequest request = new RegisterRequest(
                "abion01",
                "abion@gmail.com",
                "apc12345",
                "+10293423233");
        User savedUser = User.builder()
                .id(Long.parseLong("1"))
                .username(request.username())
                .email(request.email())
                .phone(request.phone())
                .build();

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.existsByPhone(request.phone())).thenReturn(false);
        when(userRepository.saveAndFlush(any())).thenReturn(savedUser);

        RegisterResponse response = userService.register(request);

        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(request.username(), response.username());
        Assertions.assertEquals(request.email(), response.email());
        Assertions.assertEquals(request.phone(), response.phone());
        verify(userRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void register_shouldThrow_whenRaceConditionMet() throws InterruptedException {
        RegisterRequest request = new RegisterRequest(
                "abion01",
                "abion@gmail.com",
                "apc12345",
                "+10293423233");
        User savedUser = User.builder()
                .id(Long.parseLong("1"))
                .username(request.username())
                .email(request.email())
                .phone(request.phone())
                .status(UserStatus.INITIATED)
                .build();

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.existsByPhone(request.phone())).thenReturn(false);

        when(userRepository.saveAndFlush(any()))
                .thenReturn(savedUser)
                .thenThrow(new DataIntegrityViolationException("Duplicate entry uk_users_email"));

        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        List<Exception> errors = Collections.synchronizedList(new ArrayList<>());
        List<Object> success = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
               try {
                    latch.await();
                    var response = userService.register(request);
                    success.add(response);
               } catch (Exception e) {
                    errors.add(e);
               }
            });
        }

        latch.countDown();
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);


        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(1, success.size());
        Assertions.assertInstanceOf(ConflictException.class, errors.get(0));
    }

    void register_whenDuplicateKeyFromDB_shouldThrowCustomException() {
        RegisterRequest request = new RegisterRequest(
                "abion01",
                "abion@gmail.com",
                "apc12345",
                "+10293423233");
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByPhone(any())).thenReturn(false);
        when(userRepository.saveAndFlush(any()))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                userService.register(request));
    }
}
