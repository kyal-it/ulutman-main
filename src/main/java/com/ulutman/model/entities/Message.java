package com.ulutman.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
