package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ulutman.model.enums.TransportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "propertyDetails")
@Getter
@Setter
@NoArgsConstructor
public class PropertyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalArea; // Общая площадь

    private Double livingArea; // Жилая площадь

    private Double ceilingHeight; // Высота потолков

    private String layout; // Планировка

    private String bathroomType; // Санузел

    private Boolean hasBalcony; // Балкон/Лоджия

    private String viewFromWindow; // Вид из окна

    private Integer yearOfConstruction; // Год постройки

    private Boolean hasGarbageChute; // Мусоропровод

    private Integer numberOfElevators; // Количество лифтов

    private String buildingType; // Тип дома

    private String overlappingType; // Тип перекрвтия

    private Boolean hasParking; // Парковка

    private Integer entrances; // Подъезды

    private String heatingType; // Отопление

    private Boolean isEmergency; // Аварийность

    //Дополнительные поля про "в квартире есть"
    private Boolean hasRefrigerator; // Холодильник

    private Boolean hasWashingMachine; // Стиральная машина

    private Boolean hasTelevision; // Телевизор

    private Boolean hasShower; // Душевая кабина

    private Boolean hasFurnitureInRooms; // Мебель в комнатах

    private Boolean hasDishWasher; // Посудомоечная

    private Boolean hasAirConditioner; // Кондиционер

    private Boolean hasInternet; // Интернет

    private Boolean hasKitchenFurniture;  // Мебель на кухне

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    private Double kitchenArea;

    private Integer walkingDistance;

    private Integer transportDistance;

    private String district;

    private String quantity;

    private Integer floor;

    @JsonBackReference // Обратная связь с publish
    @OneToOne(mappedBy = "propertyDetails", fetch = FetchType.LAZY)
    private Publish publish;
}
