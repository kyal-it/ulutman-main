package com.ulutman.mapper;

import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.dto.UserPublishesResponse;
import com.ulutman.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserPublishesMapper {

    private final AuthMapper authMapper;

    public UserPublishesResponse mapToResponse(User user, List<PublishResponse> publishes,Integer numberOfPublications) {
        AuthResponse authResponse = authMapper.mapToResponse(user);

        List<PublishResponse> publishResponses = publishes; // Публикации уже преобразованы

        UserPublishesResponse userPublishesResponse = new UserPublishesResponse();
        userPublishesResponse.setNumberOfPublications(numberOfPublications);
        userPublishesResponse.setPublishResponses(publishResponses);

        return userPublishesResponse;
    }
}

