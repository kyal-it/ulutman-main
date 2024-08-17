package com.ulutman.model.dto;

import com.ulutman.model.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishRequest {
    String description;
    Metro metro;
    String address;
    String phoneNumber;
    String image;
    Category category; //Enum
    Subcategory subcategory; //Enum
    Bank bank;
    PublishStatus publishStatus;
    Long userId;
    CategoryStatus categoryStatus;
}