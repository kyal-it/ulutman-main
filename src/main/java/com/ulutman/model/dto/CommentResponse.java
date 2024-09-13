package com.ulutman.model.dto;

import com.ulutman.model.enums.ModeratorStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {

    Long id;

    AuthResponse authResponse;

    String content;

    ModeratorStatus moderatorStatus;

    LocalDate createDate;
}
