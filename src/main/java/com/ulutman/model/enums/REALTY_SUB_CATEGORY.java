package com.ulutman.model.enums;

import com.ulutman.model.entities.SubCategory;

public enum REALTY_SUB_CATEGORY implements SubCategory {
    House,
    Apartment,
    PartOfLand,
    Space;

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
