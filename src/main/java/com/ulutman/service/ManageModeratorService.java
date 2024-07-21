package com.ulutman.service;

import com.ulutman.model.dto.ModeratorCommentResponse;
import com.ulutman.model.dto.ModeratorMessageResponse;
import com.ulutman.model.dto.UserCommentsMessagesResponse;
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

     public List<ModeratorCommentResponse> getUserComments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
                .orElseThrow(() -> new RuntimeException("User not found"));

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
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ModeratorCommentResponse> comments = getUserComments(userId);
        List<ModeratorMessageResponse> messages = getUserMessages(userId);

        return new UserCommentsMessagesResponse(
                user.getId(),
                user.getUsername(),
                comments,
                messages
        );
    }
}

