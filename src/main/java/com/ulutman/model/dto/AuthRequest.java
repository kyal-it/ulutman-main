package com.ulutman.model.dto;

import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.ServiceRole;
import com.ulutman.model.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRequest {

    @NotBlank
    @Size(min = 3, max = 20, message = "Name should be between 6 and 20 characters!")
    String name;

    @Email(message = "email should be valid")
    @Size(min = 5, max = 30, message = "email must be between 5 and 30 characters!")
    String email;

    @NotBlank(message = "password is mandatory!")
    @Pattern(regexp = "^[a-zA-Z0-9a-яА-Я.,;: _?!+=/'\\\\\"*(){}\\[\\]\\-]{8,100}$", message = "incorrect password")
    @Size(min = 6, max = 20, message = "password must be between 6 and 20 characters!")
    String password;

    @NotBlank(message = "password is mandatory!")
    String confirmPassword;

    @NotBlank
    Role role;

    @NotBlank
    Status status;

    @CreatedDate
    LocalDate createDate;

}
