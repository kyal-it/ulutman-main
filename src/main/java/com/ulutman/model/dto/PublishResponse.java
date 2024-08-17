package com.ulutman.model.dto;

import com.ulutman.model.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublishResponse {
    Long id;
    String description;
    Metro metro;
    String address;
    String phoneNumber;
    String image;
    Category category;
    Subcategory subcategory;
    Bank bank;
    PublishStatus publishStatus;
    LocalDate createDate;
    AuthResponse user;
    CategoryStatus categoryStatus;
}