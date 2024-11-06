package com.ulutman.service;

import com.ulutman.mapper.AuthMapper;
import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import com.ulutman.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;

//    @PostConstruct
//    public void initAdmin() {
//        User user = new User();
//        user.setName("Admin");
//        user.setEmail("admin@gmail.com");
//        user.setStatus(Status.АКТИВНЫЙ);
//        user.setRole(Role.ADMIN);
//        user.setPassword(passwordEncoder.encode("admin123"));
//        userRepository.save(user);
//    }


    public AuthResponse saveAdmin(AuthRequest request) {
        User user = authMapper.mapToEntity(request);
        user.setCreateDate(LocalDate.now());
        log.info("Админ успешно создан");
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(request.getConfirmPassword()));
        user.setRole(Role.ADMIN); // По умолчанию USER, если роль не указана
        Favorite basket = new Favorite();
        Set<Favorite> favorites = new HashSet<>();
        favorites.add(basket);


        user.setFavorites(favorites);

        userRepository.save(user);
        return authMapper.mapToResponse(user);
    }
}
