package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ulutman.model.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String title;

    private String description;

    private double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Subcategory subCategory;

//    @Enumerated(EnumType.STRING)
//    private Bank bank;

    @Enumerated(EnumType.STRING)
    private Metro metro;

    private String address;

    public Publish(Long id, LocalDateTime createdAt, String title, String description, double price, Category category, Subcategory subCategory, Metro metro, String address, String phone, boolean active, String chatId, File paymentReceipt, String bank, List<String> images, LocalDate createDate, PublishStatus publishStatus, boolean detailFavorite, CategoryStatus categoryStatus, LocalDateTime lastBoostedAt, Payment payment, List<Favorite> favorites, User user, PropertyDetails propertyDetails, Conditions conditions) {
        this.id = id;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
        this.metro = metro;
        this.address = address;
        this.phone = phone;
        this.active = active;
        this.chatId = chatId;
        this.paymentReceipt = paymentReceipt;
        this.bank = bank;
        this.images = images;
        this.createDate = createDate;
        this.publishStatus = publishStatus;
        this.detailFavorite = detailFavorite;
        this.categoryStatus = categoryStatus;
        this.lastBoostedAt = lastBoostedAt;
        this.payment = payment;
        this.favorites = favorites;
        this.user = user;
        this.propertyDetails = propertyDetails;
        this.conditions = conditions;
    }

    private String phone;

    private boolean active;

    private String chatId;

    private File paymentReceipt;

    private String bank;

    @ElementCollection
    @CollectionTable(name = "publish_images")  // Отдельная таблица для хранения изображений
    @Column(name = "image")
    private List<String> images;

//    private String image;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private PublishStatus publishStatus;

    @Column(nullable = false)
    private boolean detailFavorite;

    @Enumerated(EnumType.STRING)
    private CategoryStatus categoryStatus;

    private LocalDateTime lastBoostedAt;

    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(
            name = "payment_id"
    )
    private Payment payment;

    @JsonManagedReference // Это поле будет ссылаться на Favorite
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "publishes")
    List<Favorite> favorites;

    @JsonBackReference // Обратная связь с User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @JsonManagedReference // Это поле будет ссылаться на PropertyDetails
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_details_id",referencedColumnName = "id")
    private PropertyDetails propertyDetails;

    @JsonManagedReference // Это поле будет ссылаться на Conditions
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conditions_id",referencedColumnName = "id")
    private Conditions conditions;

    @OneToMany(mappedBy = "publish", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MyPublish> myPublishes;

}
