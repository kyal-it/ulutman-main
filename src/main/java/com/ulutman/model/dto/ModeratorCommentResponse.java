package com.ulutman.model.dto;

import com.ulutman.model.enums.ModeratorStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModeratorCommentResponse {

    Long commentId;

    String username;

    String commentContent;

    LocalDate createDate;

    ModeratorStatus moderatorStatus;

    public ModeratorCommentResponse(Long commentId, String username, String content, LocalDate createDate, ModeratorStatus moderatorStatus) {
        this.commentId = commentId;
        this.username = username;
        this.commentContent = content;
        this.createDate = createDate;
        this.moderatorStatus = moderatorStatus;
    }
}
