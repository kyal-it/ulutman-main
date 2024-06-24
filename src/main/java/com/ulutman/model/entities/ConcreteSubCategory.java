package com.ulutman.model.entities;

import com.ulutman.model.enums.Category;

public class ConcreteSubCategory implements SubCategory{
    private Category category;
    private String name;

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
