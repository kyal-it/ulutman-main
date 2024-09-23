package com.ulutman.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "userAccounts")
@Getter
@Setter
@NoArgsConstructor
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String number;
    private String lastName;
    private String gmail;

    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private List<Message> messages;

//    @OneToOne(mappedBy = "userAccount",cascade = CascadeType.ALL)
//    private Favorite favorite;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private MyPublish myPublish;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private User user;

//     @Column(name = "user_id")
//     private Long userId;



}
