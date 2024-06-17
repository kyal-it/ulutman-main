package com.ulutman.controller;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.LoginRequest;
import com.ulutman.model.dto.LoginResponse;
import com.ulutman.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> save(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.save(authRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
