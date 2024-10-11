package com.ulutman.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPublishesResponse {

    String name;

    Integer numberOfPublications;

    AuthResponse authResponse;

    List<PublishResponse> publishResponses;

    public UserPublishesResponse(AuthResponse user, List<PublishResponse> publications) {
        this.authResponse = user;
        this.publishResponses = publications;
    }

}
