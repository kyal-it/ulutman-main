package com.ulutman.model.dto;

import com.ulutman.model.entities.ConcreteSubCategory;
import com.ulutman.model.entities.SubCategory;
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
    Category category; //Enum
    ConcreteSubCategory subCategory;//интерфейс поли:     Category getCategory(); // Возвращает категорию, к которой относится подкатегорияString getName(); //
    Bank bank;
}
