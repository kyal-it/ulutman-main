package com.ulutman.model.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "BankCards")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String bankName;

    String cardNumber;
}
