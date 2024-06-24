package com.ulutman.model.enums;

import com.ulutman.model.entities.SubCategory;

public enum JOB_SUB_CATEGORY implements SubCategory {
    PartTime,
    FullTime;

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
