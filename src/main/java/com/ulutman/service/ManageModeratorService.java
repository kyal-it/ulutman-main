package com.ulutman.service;

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

    public List<ModeratorCommentResponse> getUserComments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь по идентификатору " + userId + " не нвйден"));

        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(comment -> new ModeratorCommentResponse(
                        comment.getId(),
                        user.getUsername(),
                        comment.getContent(),
                        comment.getCreateDate(),
                        comment.getModeratorStatus()
                ))
                .collect(Collectors.toList());
    }

    public List<ModeratorMessageResponse> getUserMessages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь по идентификатору " + userId + " не найден"));

        List<Message> messages = messageRepository.findByUserId(userId);
        return messages.stream()
                .map(message -> new ModeratorMessageResponse(
                        message.getId(),
                        user.getUsername(),
                        message.getContent(),
                        message.getCreateDate(),
                        message.getModeratorStatus()
                ))
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

    public List<CommentResponse> getCommentsByFilters(List<User> users, List<String> content, List<LocalDate> createDates, List<String> moderatorStatus) {
        List<Comment> comments = commentRepository.findCommentsByFilters(users, content, createDates, moderatorStatus);
        return comments.stream().map(commentMapper::mapToResponse).collect(Collectors.toList());
    }

    public List<MessageResponse> getMessagesByFilters(List<User> users, List<String> content, List<LocalDate> createDate, List<String> moderatorStatus) {
        List<Message> messages = messageRepository.findMessagesByFilters(users, content, createDate, moderatorStatus);
        return messages.stream().map(messageMapper::mapToResponse).collect(Collectors.toList());
    }
}

