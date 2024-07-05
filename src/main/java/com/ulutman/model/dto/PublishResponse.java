package com.ulutman.model.dto;

import com.ulutman.model.enums.Bank;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.Metro;
import com.ulutman.model.enums.Subcategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
}
