package com.example.demo.user.api.v1;

import com.example.demo.user.api.v1.dto.RegisterRequest;
import com.example.demo.user.api.v1.dto.RegisterResponse;
import com.example.demo.user.application.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Test
    void register_shouldReturnResponse_whenValidRequest() {
        RegisterRequest request = new RegisterRequest(
                "Albion",
                "albion@mail.com",
                "test1234",
                "+123243243123"
        );

        when(userService.register(request)).thenReturn(
                new RegisterResponse(
                        Long.parseLong("1"),
                        "Albion",
                        "albion@mail.com",
                        "+123243243123",
                        LocalDateTime.now()
                )
        );

        ResponseEntity<RegisterResponse> response = userController.register(request);

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(request.phone(), response.getBody().phone());
        Assertions.assertEquals(request.email(), response.getBody().email());
        Assertions.assertEquals(request.username(), response.getBody().username());
    }
}
