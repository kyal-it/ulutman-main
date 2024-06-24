package com.ulutman.model.enums;

import com.ulutman.model.entities.SubCategory;

public enum HOTEL_SUB_CATEGORY implements SubCategory {
    DailyRent,
    LongTermRent;

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
