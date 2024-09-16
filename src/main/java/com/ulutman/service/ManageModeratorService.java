package com.ulutman.service;

import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.CommentMapper;
import com.ulutman.mapper.MessageMapper;
import com.ulutman.model.dto.*;
import com.ulutman.model.entities.Comment;
import com.ulutman.model.entities.Message;
import com.ulutman.model.entities.User;
import com.ulutman.repository.CommentRepository;
import com.ulutman.repository.MessageRepository;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageModeratorService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final CommentMapper commentMapper;
    private final MessageMapper messageMapper;
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

    public List<ModeratorMessageResponse> getUserMessages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь по идентификатору " + userId + " не найден"));

        List<Message> messages = messageRepository.findByUserId(userId);

        return messages.stream()
                .map(message -> ModeratorMessageResponse.builder()
                        .messageId(message.getId()) // messageId
                        .username(user.getUsername()) // username
                        .authResponse(authMapper.mapToResponse(message.getUser())) // authResponse
                        .content(message.getContent()) // content
                        .createDate(message.getCreateDate()) // createDate
                        .moderatorStatus(message.getModeratorStatus()) // moderatorStatus
                        .build())
                .collect(Collectors.toList());
    }

    public UserCommentsMessagesResponse getUserCommentsAndMessages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь по идентификатору " + userId + " не найден"));

        List<ModeratorCommentResponse> comments = getUserComments(userId);
        List<ModeratorMessageResponse> messages = getUserMessages(userId);

        return new UserCommentsMessagesResponse(
                user.getId(),
                user.getUsername(),
                comments,
                messages
        );
    }

    public List<UserCommentsMessagesResponse> getAllUserCommentsAndMessages() {
        List<User> users = userRepository.findAll(); // Получаем всех пользователей
        List<UserCommentsMessagesResponse> responses = new ArrayList<>(); // Создаем список для хранения ответов

        for (User user : users) {

            List<Comment> userComments = commentRepository.findByUserId(user.getId());
            List<Message> userMessages = messageRepository.findByUserId(user.getId());

            List<ModeratorCommentResponse> commentResponses = userComments.stream()
                    .map(comment -> ModeratorCommentResponse.builder()
                            .commentId(comment.getId())
                            .username(user.getUsername()) // Устанавливаем username здесь
                            .authResponse(authMapper.mapToResponse(comment.getUser()))
                            .commentContent(comment.getContent())
                            .createDate(comment.getCreateDate())
                            .moderatorStatus(comment.getModeratorStatus())
                            .build())
                    .collect(Collectors.toList());

            List<ModeratorMessageResponse> messageResponses = userMessages.stream()
                    .map(message -> ModeratorMessageResponse.builder()
                            .messageId(message.getId())
                            .username(user.getUsername()) // Устанавливаем username здесь
                            .authResponse(authMapper.mapToResponse(message.getUser()))
                            .content(message.getContent())
                            .createDate(message.getCreateDate())
                            .moderatorStatus(message.getModeratorStatus())
                            .build())
                    .collect(Collectors.toList());

            UserCommentsMessagesResponse userResponse = new UserCommentsMessagesResponse(
                    user.getId(),
                    user.getUsername(),
                    commentResponses,
                    messageResponses
            );

            responses.add(userResponse);
        }

        return responses;
    }

    public List<CommentResponse> getCommentsByFilters(List<User> users, List<String> content, List<LocalDate> createDates, List<String> moderatorStatus) {
        List<Comment> comments = commentRepository.findCommentsByFilters(users, content, createDates, moderatorStatus);
        return comments.stream().map(commentMapper::mapToResponse).collect(Collectors.toList());
    }

    public List<MessageResponse> getMessagesByFilters(List<User> users, List<String> content, List<LocalDate> createDate, List<String> moderatorStatus) {
        List<Message> messages = messageRepository.findMessagesByFilters(users, content, createDate, moderatorStatus);
        return messages.stream().map(messageMapper::mapToResponse).collect(Collectors.toList());
    }
}

