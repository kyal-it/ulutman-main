package com.ulutman.model.entities;

import com.ulutman.model.enums.Bank;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.Metro;
import com.ulutman.model.enums.Subcategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="publishes")
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

    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(
            name = "payment_id"
    )
    private Payment payment;

    @OneToMany(mappedBy = "publish", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "products")
    List<Favorite> favorites;




}
