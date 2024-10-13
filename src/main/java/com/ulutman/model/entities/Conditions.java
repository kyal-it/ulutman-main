package com.ulutman.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@NoArgsConstructor
public class Conditions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double pricePerMonth;

    private String utilitiesIncluded; // ЖКХ

    private Double deposit; // Залог

    private String commission; // Комиссия

    private String prepayment; // Предоплата

    private String leaseTerm; // Срок аренды

    private boolean showPhoneNumber;

    private String realtor;

    private Double realtorRating; // Рейтинг риэлтора

    @OneToOne(mappedBy = "conditions")
    private Publish publish;
}
