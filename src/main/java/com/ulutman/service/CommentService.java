package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.CommentMapper;
import com.ulutman.model.dto.CommentRequest;
import com.ulutman.model.dto.CommentResponse;
import com.ulutman.model.entities.Comment;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.repository.CommentRepository;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentService {

    private  final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentResponse addComment(CommentRequest commentRequest) {
        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(()-> new NotFoundException("Пользователь по идентификатору " + commentRequest.getUserId() +" не найден"));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent(commentRequest.getContent());
        comment.setModeratorStatus(ModeratorStatus.ОЖИДАЕТ);
        comment.setCreateDate(LocalDate.now());
        commentRepository.save(comment);
        return  commentMapper.mapToResponse(comment);
    }

    public CommentResponse updateCommentStatus(Long commentId,ModeratorStatus moderatorStatus) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundException("Пользователь по идентификатору " + commentId +" не найден"));
        comment.setModeratorStatus(moderatorStatus);
        commentRepository.save(comment);
        return commentMapper.mapToResponse(comment);
    }
}
