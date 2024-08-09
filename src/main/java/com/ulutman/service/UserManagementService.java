package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.entities.User;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserManagementService {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse save(AuthRequest request) {
        User user = authMapper.mapToEntity(request);
        user.setCreateDate(LocalDate.now());
        log.info("User is created");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(request.getConfirmPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        return authMapper.mapToResponse(user);
    }

    public List<AuthResponse> getAllUsers() {
        List<AuthResponse> authResponses = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            authResponses.add(authMapper.mapToResponse(user));
        }
        return authResponses;
    }

    public AuthResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден " + id));
        return authMapper.mapToResponse(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public AuthResponse updateUser(Long id, AuthRequest authRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден " + id));
        user.setName(authRequest.getName());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user = userRepository.save(user);
        return authMapper.mapToResponse(user);
    }

    public AuthResponse updateUserRole(Long id, @RequestBody AuthRequest authRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден " + id));
        user.setRole(authRequest.getRole());
        user = userRepository.save(user);
        return authMapper.mapToResponse(user);
    }

    public List<AuthResponse> getFilteredUser(
            List<String> names,
            List<String> roles,
            List<LocalDate> createDate,
            List<String> status) {
        List<User> attribute = userRepository.userFilter(names, roles, createDate, status);
        return attribute.stream().map(authMapper::mapToResponse).toList();
    }
}
