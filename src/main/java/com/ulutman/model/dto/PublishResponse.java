package com.ulutman.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ulutman.model.entities.Conditions;
import com.ulutman.model.entities.PropertyDetails;
import com.ulutman.model.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    List<String> images;

//    String image;

    double price;

    Category category;

    Integer numberOfPublications;

    Subcategory subcategory;

    //Bank bank;

    PublishStatus publishStatus;

    LocalDate createDate;

    boolean detailFavorite;

    @JsonBackReference
    AuthResponse user;

    CategoryStatus categoryStatus;

    PropertyDetails propertyDetails;

    boolean active;

    Conditions conditions;

    private LocalDateTime nextBoostTime;
    private String timeToNextBoost; // Добавляем поле для строки с временем до следующего бустинга

}
