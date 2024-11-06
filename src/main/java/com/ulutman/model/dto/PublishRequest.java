package com.ulutman.model.dto;

import com.ulutman.model.entities.BankCard;
import com.ulutman.model.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.util.List;
import java.util.Optional;

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

     List<String> images;

//    String image;

    double price;

    Category category;

    Subcategory subcategory;

    Optional<String> bank = Optional.empty(); // Выбор банка

    Optional<File> paymentReceiptFile = Optional.empty(); // Чек оплаты


    PublishStatus publishStatus;

    Long userId;

    CategoryStatus categoryStatus;

    PropertyDetailsRequest propertyDetails;

    ConditionsRequest conditions;
}
