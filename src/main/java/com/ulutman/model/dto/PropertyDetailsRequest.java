package com.ulutman.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDetailsRequest {

    Double totalArea;

    Double livingArea;

    Double ceilingHeight;

    String layout;

    String bathroomType;

    Boolean hasBalcony;

    String viewFromWindow;

    Integer yearOfConstruction;

    Boolean hasGarbageChute;

    Integer numberOfElevators;

    String buildingType;

    String overlappingType;

    Boolean hasParking;

    Integer entrances;

    String heatingType;

    Boolean isEmergency;

    Boolean hasRefrigerator;

    Boolean hasWashingMachine;

    Boolean hasTelevision;

    Boolean hasShower;

    Boolean hasFurnitureInRooms;

    Boolean hasDishWasher;

    Boolean hasAirConditioner;

    Boolean hasInternet;

    Boolean hasKitchenFurniture;
}
