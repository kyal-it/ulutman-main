package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "favorites")
@Getter
@Setter
@NoArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Many favorites to one User
    @JoinColumn(name = "user_id")
    private User user;

    // @JsonBackReference // Обратная связь с userAccount
    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "user_account_id")
    // private UserAccount userAccount;

    @JsonBackReference // Обратная связь с Publish
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "favorites_publishes",
            joinColumns = @JoinColumn(name = "favorite_id"),
            inverseJoinColumns = @JoinColumn(name = "publish_id"))
    Set<Publish> publishes = new LinkedHashSet<>();


}