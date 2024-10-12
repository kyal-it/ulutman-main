package com.ulutman.model.dto;

import com.ulutman.model.entities.PropertyDetails;
import com.ulutman.model.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublishResponse {

    Long id;

    String title;

    String description;

    Metro metro;

    String address;

    String phoneNumber;

    String image;

    double price;

    Category category;

    Integer numberOfPublications;

    Subcategory subcategory;

    Bank bank;

    PublishStatus publishStatus;

    LocalDate createDate;

    AuthResponse user;

    CategoryStatus categoryStatus;

    PropertyDetails propertyDetails; // Добавьте это поле

    private List<CommentResponse> comments;
}
