package com.ulutman.model.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "AdVertising")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdVersiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String imagePath;
}
