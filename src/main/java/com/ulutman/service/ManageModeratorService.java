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

    public List<UserCommentsResponse> getAllUserComments() {
        // Получаем всех пользователей
        List<User> users = userRepository.findAll();

        // Создаем список для хранения ответов
        return users.stream().map(user -> {
            // Получаем комментарии пользователя
            List<Comment> userComments = commentRepository.findByUserId(user.getId());

            // Проверка, есть ли комментарии
            if (userComments.isEmpty()) {
                System.out.println("No comments found for user: " + user.getUsername());
            }

            // Преобразуем комментарии в ModeratorCommentResponse
            List<ModeratorCommentResponse> commentResponses = userComments.stream()
                    .map(comment -> {
                        // Проверка, есть ли пользователь для комментария
                        User commentUser = comment.getUser();
                        if (commentUser == null) {
                            System.out.println("User for comment " + comment.getId() + " is null");
                        }

                        return ModeratorCommentResponse.builder()
                                .commentId(comment.getId())
                                .username(user.getUsername()) // Устанавливаем username здесь
                                .authResponse(authMapper.mapToResponse(commentUser))
                                .commentContent(comment.getContent())
                                .createDate(comment.getCreateDate())
                                .moderatorStatus(comment.getModeratorStatus())
                                .build();
                    })
                    .collect(Collectors.toList());

            // Создаем объект UserCommentsResponse для текущего пользователя
            return new UserCommentsResponse(
                    user.getId(),
                    user.getUsername(),
                    commentResponses
            );
        }).collect(Collectors.toList()); // Сбор всех UserCommentsResponse в список
    }

    public CommentResponse updateCommentStatus(Long id, ModeratorStatus newStatus) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий по идентификатору " + id + " не найден"));

        // Проверяем, нужно ли обновлять статус
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

        if (content != null && content.trim().isEmpty()) {
            throw new IllegalArgumentException("Содержимое комментариев не может содержать нулевых значений.");
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

        List<Comment> comments = commentRepository.findCommentsByFilters(createDates, moderatorStatuses);

        return comments.stream()
                .map(commentMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}

