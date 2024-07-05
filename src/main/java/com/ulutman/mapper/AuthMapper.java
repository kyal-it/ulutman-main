package com.ulutman.mapper;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AuthMapper {

    public User mapToEntity(AuthRequest authRequest) {
        User user = new User();
        user.setName(authRequest.getName());
        user.setEmail(authRequest.getEmail());
        if (!authRequest.getPassword().equals(authRequest.getConfirmPassword())) {
            throw new RuntimeException("Пароли не совпадают");
        }
        user.setRole(Role.USER);
        return user;
    }

    public AuthResponse mapToResponse(User user) {
        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(user.getRole())
                .createDate(LocalDate.now())
                .build();
    }
}
