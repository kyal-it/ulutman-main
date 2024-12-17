package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "myPublishes")
@Getter
@Setter
public class MyPublish {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    Long id;

    @JsonBackReference // Обратная связь с userAccount
    @ManyToOne // Изменено на ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publish_id", nullable = false, foreignKey = @ForeignKey(name = "fk_mypublish_publish", value = ConstraintMode.CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (publish_id) REFERENCES publishes(id) ON DELETE CASCADE"))
    private Publish publish;


}
