package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ulutman.model.enums.ModeratorStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String content;

    @Enumerated(EnumType.STRING)
    ModeratorStatus moderatorStatus;

    @Column(name = "create_date")
    LocalDate createDate;

    @JsonBackReference // Обратная связь с User
    @ManyToOne // Обратная связь с User
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
