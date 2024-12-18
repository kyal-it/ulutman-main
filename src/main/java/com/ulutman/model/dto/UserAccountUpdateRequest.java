package com.ulutman.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountUpdateRequest {

    private String email;

    private String lastName;

    private String phoneNumber;

    private String name;
}
