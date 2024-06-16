package com.ulutman.controller;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.service.AutService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AutService autService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> save(@RequestBody AuthRequest authRequest) {
        AuthResponse response = autService.save(authRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
