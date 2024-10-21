package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishDetailsResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagePublishService {

    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;
    private final UserRepository userRepository;

    public List<PublishDetailsResponse> getAllPublish() {
        return publishRepository.findAll().stream().map(publishMapper::mapToDetailsResponse).collect(Collectors.toList());
    }

    public PublishDetailsResponse updatePublishStatus(Long id, @RequestBody PublishStatus newStatus) {
        Publish publish = publishRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Публикация по идентификатору " + id + " не найдена"));

        if (newStatus != null && publish.getPublishStatus() != newStatus) {
            publish.setPublishStatus(newStatus);
            publishRepository.save(publish);
        }
        return publishMapper.mapToDetailsResponse(publish);
    }

    public List<PublishDetailsResponse> filterPublishes(List<Category> categories,
                                                        List<PublishStatus> publishStatuses,
                                                        List<LocalDate> createDates,
                                                        String names) {
        List<Publish> filteredPublishes = new ArrayList<>();

        // Проверка на нулевые значения категорий
        if (categories != null && categories.stream().anyMatch(category -> category == null)) {
            throw new IllegalArgumentException("Категории не могут содержать нулевых значений.");
        } else if (categories != null && !categories.isEmpty()) {
            filteredPublishes.addAll(publishRepository.filterPublishesByCategory(categories));
        }

        // Проверка на нулевые значения статусов
        if (publishStatuses != null && publishStatuses.stream().anyMatch(status -> status == null)) {
            throw new IllegalArgumentException("Статусы публикаций не могут содержать нулевых значений.");
        } else if (publishStatuses != null && !publishStatuses.isEmpty()) {
            filteredPublishes.addAll(publishRepository.filterPublishesByStatus(publishStatuses));
        }

        // Проверка на нулевые значения дат создания
        if (createDates != null && createDates.stream().anyMatch(date -> date == null)) {
            throw new IllegalArgumentException("Даты создания не могут содержать нулевых значений.");
        } else if (createDates != null && !createDates.isEmpty()) {
            filteredPublishes.addAll(publishRepository.filterPublishesByCreateDate(createDates));
        }

        // Фильтрация по именам пользователей
        if (names != null && !names.trim().isEmpty()) {
            List<User> filteredUsers = userRepository.findByUserName(names);
            if (filteredUsers.isEmpty()) {
                return Collections.emptyList();  // Если нет пользователей, возвращаем пустой массив
            }

            for (User user : filteredUsers) {
                List<Publish> userPublications = publishRepository.filterPublishesByUserName(user.getName());
                if (!userPublications.isEmpty()) {
                    filteredPublishes.addAll(userPublications);
                }
            }
        }

        // Убираем дубликаты публикаций
        filteredPublishes = filteredPublishes.stream().distinct().collect(Collectors.toList());

        // Если нет отфильтрованных публикаций, возвращаем пустой массив
        if (filteredPublishes.isEmpty()) {
            return Collections.emptyList();
        }

        // Маппинг
        return filteredPublishes.stream()
                .map(publishMapper::mapToDetailsResponse) // Используем маппер
                .collect(Collectors.toList());
    }

    public void deletePublicationsByIds(List<Long> ids) {
        List<Publish> publishes = publishRepository.findAllById(ids);

        if (publishes.isEmpty()) {
            throw new NotFoundException("Публикации с такими ID не найдены");
        }

        publishRepository.deleteAll(publishes);
    }
}
