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
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public UserCommentsResponse getUserWithComments(Long userId) {

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            List<ModeratorCommentResponse> comments = commentRepository.findByUserId(userId)
                    .stream()
                    .map(commentMapper::mapToModeratorCommentResponse) // Маппинг комментариев
                    .collect(Collectors.toList());

            return new UserCommentsResponse(user.getId(), user.getUsername(), user.getEmail(), comments);
        } else {
            throw new EntityNotFoundException("Пользователь с идентификатором " + userId + " не найден");
        }
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

    public List<CommentResponse> filterComments(
            List<ModeratorStatus> moderatorStatuses,
            List<LocalDate> createDates,
            String content) {
        List<Comment> filteredComments = new ArrayList<>();

        if (content != null && !content.trim().isEmpty()) {
            filteredComments.addAll(commentRepository.commentsFilterByContents(content));
        }

        if (moderatorStatuses != null && !moderatorStatuses.isEmpty()) {
            filteredComments.addAll(commentRepository.filterCommentByModeratorStatus(moderatorStatuses));
        }

        if (createDates != null && !createDates.isEmpty()) {
            filteredComments.addAll(commentRepository.findByModeratorByCreateDate(createDates));
        }

        filteredComments = filteredComments.stream().distinct().collect(Collectors.toList());

        return filteredComments.stream()
                .map(commentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId) {

        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Комментарий с идентификатором " + commentId + " не найден");
        }

        commentRepository.deleteById(commentId);
    }
}

