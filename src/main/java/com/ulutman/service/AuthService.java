package com.ulutman.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.ulutman.exception.IncorrectCodeException;
import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.LoginMapper;
import com.ulutman.model.dto.*;
import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.User;
import com.ulutman.model.entities.UserAccount;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import com.ulutman.repository.UserRepository;
import com.ulutman.security.jwt.JwtUtil;
import io.jsonwebtoken.io.IOException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.CLIENT_ID;

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

    public AuthResponse saveUser(AuthRequest request) {

        User user = authMapper.mapToEntity(request);
        user.setCreateDate(LocalDate.now());
        log.info("Пользователь успешно создан!");

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(request.getConfirmPassword()));

        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        user.setStatus(Status.АКТИВНЫЙ);

        Favorite basket = new Favorite();
        user.setFavorites(basket);
        basket.setUser(user);

        UserAccount userAccount = new UserAccount();
        user.setUserAccount(userAccount);
        userAccount.setUsername(user.getUsername());

        userRepository.save(user);

        String jwt = jwtUtil.generateToken(user);

        return authMapper.mapToResponseWithToken(jwt, user);
    }


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

        UserAccount userAccount = new UserAccount();
        user.setUserAccount(userAccount);
        userAccount.setUsername(user.getUsername());

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

    // Метод для аутентификации через Google и регистрации пользователя
    public AuthWithGoogleResponse registerUserWithGoogle(String token) {
        FirebaseToken firebaseToken;
        try {
            firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
            log.info("FirebaseToken успешно проверен");
        } catch (FirebaseAuthException firebaseAuthException) {
            log.error("Во время аутентификации произошла ошибка", firebaseAuthException);
            throw new BadCredentialsException("Во время аутентификации произошла ошибка");
        }

        String email = firebaseToken.getEmail();
        User user = userRepository.findByEmail(email).orElseGet(() -> {

            User newUser = new User();
            String fullName = firebaseToken.getName();
            int spaceIndex = fullName.indexOf(" ");
            if (spaceIndex != -1) {
                newUser.setName(fullName.substring(0, spaceIndex));
                newUser.setLastName(fullName.substring(spaceIndex + 1));
            } else {
                newUser.setName(fullName);
            }
            newUser.setEmail(email);
//            newUser.setPhoneNumber("+996700000000"); // Укажите номер телефона
            newUser.setPassword(passwordEncoder.encode(firebaseToken.getEmail()));
            newUser.setRole(Role.USER);
            return userRepository.save(newUser);
        });

        // Генерируем токен для пользователя
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        String userAccountToken = jwtUtil.createToken(claims, user.getEmail());

        log.info("Аутентификация через Google завершена успешно, токен: {}", userAccountToken);


        return AuthWithGoogleResponse.builder()
                .googleId(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName() + " " + user.getLastName())
                .picture(user.getPicture())
                .locale(user.getLocale())
                .build();
    }
}
