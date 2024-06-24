package com.ulutman.model.enums;

import com.ulutman.model.entities.SubCategory;

public enum SELL_SUB_CATEGORY implements SubCategory {
    Clothes,
    HouseAppliances,
    Electronics;

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
