package com.ulutman.mapper;

import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.dto.UserPublishesResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPublishesMapper {

    private final AuthMapper authMapper;
    private final PublishMapper publishMapper;

    public UserPublishesResponse mapToResponse(User user, List<Publish> publishes) {
        AuthResponse authResponse = authMapper.mapToResponse(user);
        List<PublishResponse> publishResponses = publishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
        UserPublishesResponse userPublishesResponse = new UserPublishesResponse();
        userPublishesResponse.setAuthResponse(authResponse);
        userPublishesResponse.setPublishResponses(publishResponses);
        return userPublishesResponse;
    }
}

