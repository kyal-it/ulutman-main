package com.ulutman.model.enums;

import com.ulutman.model.entities.SubCategory;

public enum CAR_SUB_CATEGORY implements SubCategory {
    SaleOfCar,
    RentOfCar;

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
