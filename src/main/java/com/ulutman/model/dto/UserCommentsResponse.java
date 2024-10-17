package com.ulutman.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCommentsResponse {

    private Long userId;

    private String username;

    private String email;

    private List<ModeratorCommentResponse> comments;

    public UserCommentsResponse(Long userId, String username,String email, List<ModeratorCommentResponse> comments) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.comments = comments;
    }
}
