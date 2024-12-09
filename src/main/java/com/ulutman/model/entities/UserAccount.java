package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    private String email;
    private String number;
    private String lastName;
    private String name;

    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private List<Message> messages;

    @JsonManagedReference // Это поле будет ссылаться на myPublishes
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private List<MyPublish> myPublishes; // Убедитесь, что это List<MyPublish>

    @JsonBackReference // Обратная связь с User
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // Указываем имя столбца для связи
    private User user; // Это поле связывает UserAccount с User
}
