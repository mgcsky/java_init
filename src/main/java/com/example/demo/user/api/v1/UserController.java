package com.example.demo.user.api.v1;

import com.example.demo.user.api.v1.dto.RegisterRequest;
import com.example.demo.user.api.v1.dto.RegisterResponse;
import com.example.demo.user.application.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register (@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request);

        return ResponseEntity.ok(response);
    }
}
