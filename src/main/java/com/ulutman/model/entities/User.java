package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(unique = true)
    String email;

    String password;

    String confirmPassword;
    @Transient
    int numberOfPublications;

    @Enumerated(EnumType.STRING)
    Role role;

    @Enumerated(EnumType.STRING)
    Status status;

    @Column(name = "create_date")
    LocalDate createDate;

    int pinCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    @JsonManagedReference // Это поле будет ссылаться на UserAccount
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserAccount userAccount;

    @JsonManagedReference // Это поле будет ссылаться на Publish
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Publish> publishes = new ArrayList<>();

    @JsonManagedReference // Это поле будет ссылаться на Complaint
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Complaint> complaints;

    @JsonManagedReference // Это поле будет ссылаться на Comment
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @JsonManagedReference // Это поле будет ссылаться на Favorite
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Favorite favorites;

    @JsonManagedReference // Это поле будет ссылаться на Mailing
    @ManyToMany(mappedBy = "recipients")
    private List<Mailing> mailings = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
