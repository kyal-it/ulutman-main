package com.ulutman.service;

import com.ulutman.exception.IncorrectCodeException;
import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.LoginMapper;
import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.LoginRequest;
import com.ulutman.model.dto.LoginResponse;
import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.User;
import com.ulutman.repository.UserRepository;
import com.ulutman.security.jwt.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    UserRepository userRepository;
    AuthMapper authMapper;
    JwtUtil jwtUtil;
    PasswordEncoder passwordEncoder;
    AuthenticationManager manager;
    LoginMapper loginMapper;

    public AuthResponse save(AuthRequest request) {
        User user = authMapper.mapToEntity(request);
        user.setCreateDate(LocalDate.now());
        log.info("User is created");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(request.getConfirmPassword()));
        user.setRole(request.getRole());
        Favorite basket = new Favorite();
        user.setFavorites(basket);
        basket.setUser(user);
        userRepository.save(user);
        return authMapper.mapToResponse(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь с такой почтой: " + request.getEmail() + " не найден !"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectCodeException("Введен неверный пароль!");
        }
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String jwt = jwtUtil.generateToken(user);
        return loginMapper.mapToResponse(jwt, user);
    }
}
