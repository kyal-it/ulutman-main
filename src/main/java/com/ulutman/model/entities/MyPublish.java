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
    @ManyToOne // Изменено на ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @JsonManagedReference //  ссылаться на Publish
    @ManyToOne
    @JoinColumn(name = "publish_id", nullable = false)
    private Publish publish;
}
