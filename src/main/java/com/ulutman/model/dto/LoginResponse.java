package com.ulutman.model.dto;

import com.ulutman.model.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {

    private String token;

    private Role roleName;
}
