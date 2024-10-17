package com.ulutman.mapper;

import com.ulutman.model.dto.CommentRequest;
import com.ulutman.model.dto.CommentResponse;
import com.ulutman.model.dto.ModeratorCommentResponse;
import com.ulutman.model.entities.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final AuthMapper authMapper;

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
                .createDate(comment.getCreateDate())
//                .authResponse(comment.getUser()!=null?authMapper.mapToResponse(comment.getUser()):null)
                .build();
    }

    public ModeratorCommentResponse mapToModeratorCommentResponse(Comment comment) {
        return ModeratorCommentResponse.builder()
                .commentId(comment.getId())
                .commentContent(comment.getContent())
                .moderatorStatus(comment.getModeratorStatus())
                .createDate(comment.getCreateDate())
//                .authResponse(comment.getUser()!=null?authMapper.mapToResponse(comment.getUser()):null)
                .build();
    }
}
