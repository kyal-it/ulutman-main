package com.ulutman.model.dto;

import com.ulutman.model.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {

    Long id;
    String name;
    String email;
    Role role;
    LocalDate createDate;
}
