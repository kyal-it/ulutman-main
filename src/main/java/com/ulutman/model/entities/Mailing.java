package com.ulutman.model.entities;

import com.ulutman.model.enums.MailingStatus;
import com.ulutman.model.enums.MailingType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "mailing")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mailing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    @Enumerated(EnumType.STRING)
    MailingType mailingType;
    @Enumerated(EnumType.STRING)
    MailingStatus mailingStatus;
    String message;
    String image;
    @Column(name = "promotion_start_date")
    LocalDate promotionStartDate;
    @Column(name = "promotion_end_date")
    LocalDate promotionEndDate;
    LocalDate createDate;

    @ManyToMany(mappedBy = "mailings")
    private Set<User> users;
}