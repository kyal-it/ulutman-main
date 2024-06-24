package com.ulutman.model.enums;

import com.ulutman.model.entities.SubCategory;

public enum RENT_SUB_CATEGORY implements SubCategory {
    I_RentRoom,
    I_RentBed,
    I_RenApartment,
    RentRoom,
    RentBed,
    RentApartment,
    RentOffice;

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
