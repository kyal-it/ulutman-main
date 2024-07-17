package com.ulutman.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="myPublishes",schema = "my_schema")
@Getter
@Setter
@NoArgsConstructor
public class MyPublish {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
