package com.ulutman.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCommentsResponse {

    private Long userId;

    private String username;

    private List<ModeratorCommentResponse> comments;

    public UserCommentsResponse(Long userId, String username,
                                List<ModeratorCommentResponse> comments) {
        this.userId = userId;
        this.username = username;
    }
}
