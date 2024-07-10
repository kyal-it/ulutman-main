package com.ulutman.model.dto;

import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
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

    AuthResponse authResponse;
    List<PublishResponse> publishResponses;
}
