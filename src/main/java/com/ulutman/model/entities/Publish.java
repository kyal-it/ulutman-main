package com.ulutman.model.entities;

import com.ulutman.model.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "publishes")
@Getter
@Setter
@NoArgsConstructor
public class Publish {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Subcategory subCategory;

    @Enumerated(EnumType.STRING)
    private Bank bank;

    @Enumerated(EnumType.STRING)
    private Metro metro;

    private String address;

    private String phone;

    private String image;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private PublishStatus publishStatus;

    @Enumerated(EnumType.STRING)
    private CategoryStatus categoryStatus;

    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(
            name = "payment_id"
    )
    private Payment payment;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "publishes")
    List<Favorite> favorites;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
