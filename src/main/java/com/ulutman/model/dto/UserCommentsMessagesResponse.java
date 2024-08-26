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
public class UserCommentsMessagesResponse {

    private Long userId;

    private String username;

    private List<ModeratorCommentResponse> comments;

    private List<ModeratorMessageResponse> messages;

    public UserCommentsMessagesResponse(Long userId, String username,
                                        List<ModeratorCommentResponse> comments,
                                        List<ModeratorMessageResponse> messages) {
        this.userId = userId;
        this.username = username;
        this.comments = comments;
        this.messages = messages;
    }
}
