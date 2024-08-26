package com.ulutman.mapper;

import com.ulutman.model.dto.CommentRequest;
import com.ulutman.model.dto.CommentResponse;
import com.ulutman.model.entities.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public Comment mapToEntity(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        return comment;
    }

    public CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .moderatorStatus(comment.getModeratorStatus())
                .username(comment.getUser().getUsername())
                .createDate(LocalDate.now())
                .build();
    }
}
