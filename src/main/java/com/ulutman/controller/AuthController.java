package com.ulutman.controller;

import com.ulutman.model.dto.*;
import com.ulutman.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Auth")
@SecurityRequirement(name = "Authorization")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        AuthResponse response = authService.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Create a new User")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> save(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.save(authRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Login a new User")
    @ApiResponse(responseCode = "201", description = "User login successfully")
    @PostMapping("/sign-in")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // Метод для аутентификации и регистрации через Google
    @PostMapping("/google-login")
    public ResponseEntity<AuthWithGoogleResponse> googleLogin(@RequestParam String token) {
        AuthWithGoogleResponse authResponse = authService.registerUserWithGoogle(token);
        return ResponseEntity.ok(authResponse);
    }
}
