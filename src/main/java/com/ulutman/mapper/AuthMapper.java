package com.ulutman.mapper;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final PublishMapper publishMapper;

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

        List<PublishResponse> publishResponses = user.getPublishes().stream()
                .map(publishMapper::mapToResponse) // Используем publishMapper
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(user.getRole())
                .createDate(user.getCreateDate())
                .publishes(publishResponses)
                .build();
    }

    public AuthResponse mapToComplaintResponse(User user) {
        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
