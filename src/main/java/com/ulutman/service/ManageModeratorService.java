package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
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
import java.util.Collections;
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

    public List<FilteredCommentResponse> getAllComments() {
        return commentRepository.findAll().stream().map(commentMapper::mapToFilterResponse).collect(Collectors.toList());
    }

    public FilteredCommentResponse updateCommentStatus(Long id, ModeratorStatus newStatus) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий по идентификатору " + id + " не найден"));
        if (comment.getModeratorStatus() != newStatus) {
            comment.setModeratorStatus(newStatus);
            commentRepository.save(comment);
        }

        return commentMapper.mapToFilterResponse(comment);
    }

    public List<FilteredCommentResponse> filterComments(
            List<LocalDate> createDates,
            List<ModeratorStatus> moderatorStatuses,
            String content,
            String names) {

        List<Comment> filteredComments = new ArrayList<>();

        // Фильтрация по содержимому (content)
        if (content != null && !content.trim().isEmpty()) {
            List<Comment> commentsByContent = commentRepository.commentsFilterByContents(content);
            if (!commentsByContent.isEmpty()) {
                filteredComments.addAll(commentsByContent);
            }
        }

        // Проверка на нулевые значения дат создания
        if (createDates != null && createDates.stream().anyMatch(date -> date == null)) {
            throw new IllegalArgumentException("Даты создания не могут содержать нулевых значений.");
        } else if (createDates != null && !createDates.isEmpty()) {
            filteredComments.addAll(commentRepository.findByModeratorByCreateDate(createDates));
        }

        // Фильтрация по имени пользователя
        if (names != null && !names.trim().isEmpty()) {
            List<User> users = userRepository.findByUserName(names);
            if (!users.isEmpty()) {
                List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
                filteredComments.addAll(commentRepository.findByUserIdIn(userIds));
            }
        }

        // Проверка на нулевые значения статусов
        if (moderatorStatuses != null && moderatorStatuses.stream().anyMatch(status -> status == null)) {
            throw new IllegalArgumentException("Статусы публикаций не могут содержать нулевых значений.");
        } else if (moderatorStatuses != null && !moderatorStatuses.isEmpty()) {
            filteredComments.addAll(commentRepository.filterCommentByModeratorStatus(moderatorStatuses));
        }

        // Убираем дубликаты
        filteredComments = filteredComments.stream().distinct().collect(Collectors.toList());

        // Если нет ни одного комментария, возвращаем пустой массив
        if (filteredComments.isEmpty()) {
            return Collections.emptyList();
        }

        // Маппинг
        return filteredComments.stream()
                .map(comment -> {
                    User user = comment.getUser();
                    String userNameResult = user != null ? user.getName() : "Неизвестно";

                    return FilteredCommentResponse.builder()
                            .userName(userNameResult)
                            .content(comment.getContent())
                            .createDate(comment.getCreateDate())
                            .moderatorStatus(comment.getModeratorStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void deleteCommentsByIds(List<Long> ids) {
        List<Comment> comments = commentRepository.findAllById(ids);

        if (comments.isEmpty()) {
            throw new NotFoundException("Комментарии с такими ID не найдены");
        }

        commentRepository.deleteAll(comments);
    }
}

