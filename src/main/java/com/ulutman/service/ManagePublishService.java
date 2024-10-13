package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagePublishService {

    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;
    private final AuthMapper authMapper;
    private final UserRepository userRepository;

    public List<PublishResponse> getAllPublish() {
        return publishRepository.findAll().stream().map(publishMapper::mapToResponse).collect(Collectors.toList());
    }

    public PublishResponse updatePublish(Long id, @RequestBody PublishRequest publishRequest) {
        Publish publish = publishRepository.findById(id).orElseThrow(() -> new NotFoundException("Публикация по идентификатору " + id + " не найдена"));
        publish.setDescription(publishRequest.getDescription());
        publish.setMetro(publishRequest.getMetro());
        publish.setAddress(publishRequest.getAddress());
        publish.setPhone(publish.getPhone());
        publish.setImage(publishRequest.getImage());
        publish.setCategory(publishRequest.getCategory());
        publish.setSubCategory(publishRequest.getSubcategory());
        publishRepository.save(publish);
        return publishMapper.mapToResponse(publish);
    }

    public PublishResponse updatePublishStatus(Long id, @RequestBody PublishStatus newStatus) {
        Publish publish = publishRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Публикация по идентификатору " + id + " не найдена"));

        if (newStatus != null && publish.getPublishStatus() != newStatus) {
            publish.setPublishStatus(newStatus);
            publishRepository.save(publish);
        }
        return publishMapper.mapToResponse(publish);
    }

    public List<PublishResponse> getAllPublishesByUser(Long userId) {
        List<Publish> publishes = publishRepository.findAllByUserId(userId);
        return publishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deletePublicationsByUserId(Long userId) {
        List<Publish> userPublications = publishRepository.findByUserId(userId);

        if (userPublications.isEmpty()) {
            throw new EntityNotFoundException("Публикации для пользователя с идентификатором " + userId + " не найдены.");
        }

        publishRepository.deleteAll(userPublications);
        log.info("Все публикации для пользователя с идентификатором " + userId + " успешно удалены.");
    }

    public void deletePublish(Long productId) {
        this.publishRepository.findById(productId).orElseThrow(() -> {
            return new EntityNotFoundException("Публикация  по идентификатору " + productId + " успешно удалена");
        });
        this.publishRepository.deleteById(productId);
        log.info("Публикация  по идентификатору" + productId + " успешна удалена");
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

    public List<PublishResponse> filterPublishes(List<Category> categories,
                                                 List<PublishStatus> publishStatuses,
                                                 List<LocalDate> createDates) {

        if (categories != null && categories.stream().anyMatch(category -> category == null)) {
            throw new IllegalArgumentException("Категории не могут содержать нулевых значений.");
        }

        if (publishStatuses != null && publishStatuses.stream().anyMatch(status -> status == null)) {
            throw new IllegalArgumentException("Статусы публикаций не могут содержать нулевых значений.");
        }

        if (createDates != null && createDates.stream().anyMatch(date -> date == null)) {
            throw new IllegalArgumentException("Даты создания не могут содержать нулевых значений.");
        }

        List<Publish> publishes = publishRepository.filterPublishes(categories, publishStatuses, createDates);

        return publishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
