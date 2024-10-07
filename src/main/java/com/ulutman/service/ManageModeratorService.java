package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.CommentMapper;
import com.ulutman.model.dto.*;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageModeratorService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final AuthMapper authMapper;

    public List<ModeratorCommentResponse> getUserComments(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь по идентификатору " + userId + " не найден"));

        List<Comment> comments = commentRepository.findByUserId(userId);

        return comments.stream()
                .map(comment -> ModeratorCommentResponse.builder()
                        .commentId(comment.getId()) // commentId
                        .username(user.getUsername()) // username
                        .authResponse(authMapper.mapToResponse(comment.getUser())) // authResponse
                        .commentContent(comment.getContent()) // content
                        .createDate(comment.getCreateDate()) // createDate
                        .moderatorStatus(comment.getModeratorStatus()) // moderatorStatus
                        .build())
                .collect(Collectors.toList());
    }

    public CommentResponse updateCommentStatus(Long id, ModeratorStatus newStatus) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий по идентификатору " + id + " не найден"));
        if (comment.getModeratorStatus() != newStatus) {
            comment.setModeratorStatus(newStatus);
            commentRepository.save(comment);
        }

        return commentMapper.mapToResponse(comment);
    }

    public List<AuthResponse> filterUsersByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым или содержать только пробелы.");
        }

        name = name.toLowerCase() + "%";

        return userRepository.userFilterByName(name).stream()
                .map(authMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> filterCommentsByContent(String content) {

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Содержимое комментариев не может содержать только пробелы или быть пустым.");
        }

        List<Comment> comments = commentRepository.CommentsFilterByContents(content);

        return comments.stream()
                .map(commentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> getCommentsByFilters(List<LocalDate> createDates, List<ModeratorStatus> moderatorStatuses) {

        if (createDates != null && createDates.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Даты создания комментариев не могут содержать нулевых значений.");
        }

        if (moderatorStatuses != null && moderatorStatuses.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Статусы модерации не могут содержать нулевых значений.");
        }

        if ((createDates == null || createDates.isEmpty()) &&
            (moderatorStatuses == null || moderatorStatuses.isEmpty())) {
            throw new IllegalArgumentException("Должен быть указан хотя бы один фильтр: дата создания или статус модерации.");
        }

        List<Comment> comments = commentRepository.findCommentsByFilters(createDates, moderatorStatuses);

        return comments.stream()
                .map(commentMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}

