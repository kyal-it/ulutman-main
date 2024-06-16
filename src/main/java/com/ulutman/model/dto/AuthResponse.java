package com.ulutman.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {

    Long id;
    String name;
    String email;
}
