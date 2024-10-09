package com.ulutman.model.dto;

import com.ulutman.model.entities.PropertyDetails;
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
}
