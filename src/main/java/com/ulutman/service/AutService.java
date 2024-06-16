package com.ulutman.service;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.entities.User;
import com.ulutman.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutService {
    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private UserRepository userRepository;


        public AuthResponse save (AuthRequest request) {
        User user = authMapper.mapToEntity(request);
        user.setCreateDate(LocalDate.now());
        log.info("save user: {}", user);
        user.setPassword(request.getPassword());
        userRepository.save(user);
        return authMapper.mapToResponse(user);

    }

}
