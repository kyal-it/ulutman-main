package com.ulutman.model.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPublishesResponse {

    Long id;

    String name;

    String email;

    Role role;

    Status status;

    LocalDate createDate;

    @JsonManagedReference
    List<PublishResponse> publishResponses;

    Integer numberOfPublications;

    public UserPublishesResponse(Long id, String name, String email, Role role, Status status,
                                 LocalDate createDate, Integer numberOfPublications,
                                 List<PublishResponse> publishResponses) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createDate = createDate;
        this.numberOfPublications = numberOfPublications;
        this.publishResponses = publishResponses;
    }
}
