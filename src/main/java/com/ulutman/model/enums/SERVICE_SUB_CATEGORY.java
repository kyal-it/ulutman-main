package com.ulutman.model.enums;

import com.ulutman.model.entities.SubCategory;

public enum SERVICE_SUB_CATEGORY implements SubCategory {
    MEDICAL,
    LEGAL,
    BEAUTY,
    AIRTICKET,
    TAXIANDTRACK,
    REPAIR,
    DIFFERENT;

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
