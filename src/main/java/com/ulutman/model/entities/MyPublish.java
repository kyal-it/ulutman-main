package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "myPublishes")
@Getter
@Setter
@NoArgsConstructor
public class MyPublish {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    Long id;
    @JsonBackReference // Обратная связь с userAccount
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @JsonManagedReference // Это поле будет ссылаться на Publish
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publish_id")
    private Publish publish;
}
