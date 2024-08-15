package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageUserService {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse save(AuthRequest request) {
        User user = authMapper.mapToEntity(request);
        user.setCreateDate(LocalDate.now());
        log.info("Пользователь успешно создан!");
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(request.getConfirmPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER); // По умолчанию USER, если роль не указана
        user.setStatus(Status.АКТИВНЫЙ);
        Favorite basket = new Favorite();
        user.setFavorites(basket);
        basket.setUser(user);
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

    public AuthResponse updateUserStatus(Long id, Status newStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден " + id));

        if (newStatus != null && user.getStatus() != newStatus) {
            user.setStatus(newStatus);
            userRepository.save(user);
        }
        return authMapper.mapToResponse(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public List<AuthResponse> filterUsers(List<String> names,
                                          List<String> roles,
                                          List<LocalDate> createDate,
                                          List<String> statuses) {
        List<User> users = userRepository.userFilter(names, roles, createDate, statuses);
        return users.stream()
                .map(authMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}



