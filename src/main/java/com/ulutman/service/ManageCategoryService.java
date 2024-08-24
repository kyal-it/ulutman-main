package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.mapper.UserPublishesMapper;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.dto.UserPublishesResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
import com.ulutman.repository.PublishRepository;
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
public class ManageCategoryService {

    private final PublishRepository publishRepository;
    private final PublishMapper publishMapper;
    private final UserRepository userRepository;
    private final UserPublishesMapper userPublishesMapper;


    public PublishResponse updateCategoryStatus(Long id, CategoryStatus newStatus) {
        Publish publish = publishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Публикация по идентификатору " + id + " не найдена"));

        if (newStatus != null && !newStatus.equals(publish.getCategoryStatus())) {
            publish.setCategoryStatus(newStatus);
            publishRepository.save(publish);
        }
        return publishMapper.mapToResponse(publish);
    }

    public UserPublishesResponse getUserWithPublications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь по идентификатору " + userId + " не найден"));

        List<Publish> publishes = publishRepository.findAllByUserId(userId);

        List<PublishResponse> publishResponses = publishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());

        Integer numberOfPublications = publishes.size();

        return userPublishesMapper.mapToResponse(user, publishResponses, numberOfPublications);
    }


    public UserPublishesResponse getUserWithFilteredPublications(Long userId, List<Category> categories,
                                                                 List<CategoryStatus> categoryStatuses, Integer minPublications) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь по идентификатору " + userId + " не найден"));

        if (categories != null && categories.stream().anyMatch(category -> category.toString().trim().isEmpty())) {
            throw new IllegalArgumentException("Список категорий содержит пробелы или пустые строки.");
        }

        if (categoryStatuses != null && categoryStatuses.stream().anyMatch(status -> status.toString().trim().isEmpty())) {
            throw new IllegalArgumentException("Список статусов категорий содержит пробелы или пустые строки.");
        }

        List<Publish> filteredPublishes = publishRepository.categoryFilter(categories, categoryStatuses);

        List<PublishResponse> publishResponses = filteredPublishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());

        Integer numberOfPublications = filteredPublishes.size();
        if (minPublications != null && numberOfPublications < minPublications) {
            throw new IllegalArgumentException("Количество публикаций меньше минимального порога: " + minPublications);
        }

        return userPublishesMapper.mapToResponse(user, publishResponses,numberOfPublications);
    }

}
