package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "favorites")
@Getter
@Setter
@NoArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonBackReference // Обратная связь с User
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id") // Убедитесь, что у вас есть это поле
    private User user;


//    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    User user;

    @JsonBackReference // Обратная связь с userAccount
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @JsonBackReference // Обратная связь с Publish
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "favorites_publishes",
            joinColumns = @JoinColumn(name = "favorite_id"),
            inverseJoinColumns = @JoinColumn(name = "publish_id"))
    List<Publish> publishes;
}