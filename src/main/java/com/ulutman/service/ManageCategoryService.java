package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final AuthMapper authMapper;

    public List<PublishResponse> getAllPublishesByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь по идентификатору " + userId + " не найден");
        }

        List<Publish> publishes = publishRepository.findAllByUserId(userId);

        return publishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AuthResponse> getAllUsersWithPublishes() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    List<PublishResponse> publishes = publishRepository.findAllByUserId(user.getId())
                            .stream()
                            .map(publishMapper::mapToResponse)
                            .collect(Collectors.toList());

                    AuthResponse authResponse = authMapper.mapToResponse(user);

                    authResponse.setPublishes(publishes);
                    authResponse.setNumberOfPublications(publishes.size()); // Устанавливаем количество публикаций

                    return authResponse;
                })
                .collect(Collectors.toList());
    }

    public PublishResponse updateCategoryStatus(Long id, CategoryStatus newStatus) {
        Publish publish = publishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Публикация по идентификатору " + id + " не найдена"));

        if (newStatus != null && !newStatus.equals(publish.getCategoryStatus())) {
            publish.setCategoryStatus(newStatus);
            publishRepository.save(publish);
        }
        return publishMapper.mapToResponse(publish);
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
    public List<PublishResponse> filterPublishesByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Название не может быть пустым или содержать только пробелы.");
        }

        return publishRepository.filterPublishesByTitle(title).stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());

    }
    // Фильтрация по title по нижнему регистру
//    public  List<PublishResponse> filterPublishesByTitle(String title) {
//        if (title == null || title.trim().isEmpty()) {
//            throw new IllegalArgumentException("Название не может быть пустым или содержать только пробелы.");
//        }
//        return publishRepository.findAll().stream()
//                .filter(p -> p.getTitle().toLowerCase().startsWith(title.toLowerCase()))
//                .map(publishMapper::mapToResponse)
//                .collect(Collectors.toList());
//    }

    public List<PublishResponse> filterPublicationsByCategoryAndStatus(List<Category> categories,
                                                                       List<CategoryStatus> categoryStatuses) {
        if (categories != null && categories.stream().anyMatch(category -> category == null)) {
            throw new IllegalArgumentException("Категории не могут содержать нулевых значений.");
        }

        if (categoryStatuses != null && categoryStatuses.stream().anyMatch(category -> category == null)) {
            throw new IllegalArgumentException("Список статусов категорий не может содержать пробелы или пустые строки.");

        }

        List<Publish> filteredPublishes = publishRepository.categoryFilter(categories, categoryStatuses);

        return filteredPublishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PublishResponse> getProductsByPublicationCount(Integer minCount, Integer maxCount) {
        List<Publish> results = publishRepository.findProductsByPublicationCount(minCount, maxCount);

        return results.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
