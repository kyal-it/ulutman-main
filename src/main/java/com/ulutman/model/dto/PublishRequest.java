package com.ulutman.model.dto;

import com.ulutman.model.enums.Bank;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.Metro;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishRequest {
    String description;
    Metro metro;
    String address;
    String phoneNumber;
    String image;
    Category category;
    Bank bank;
}
