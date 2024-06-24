package com.ulutman.model.enums;

import com.ulutman.model.entities.SubCategory;

public enum ListOfService implements SubCategory {
    MEDICAL,
    LEGAL,
    COURSE,
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
