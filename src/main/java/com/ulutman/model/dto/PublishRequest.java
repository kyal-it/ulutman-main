package com.ulutman.model.dto;

import com.ulutman.model.entities.Conditions;
import com.ulutman.model.entities.PropertyDetails;
import com.ulutman.model.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublishRequest {

    String title;

    String description;

    Metro metro;

    String address;

    String phoneNumber;

    String image;

    double price;

    Category category;

    Subcategory subcategory;

    Bank bank;

    PublishStatus publishStatus;

    Long userId;

    CategoryStatus categoryStatus;

    PropertyDetails propertyDetails;

    Conditions conditions;
}
