package com.ulutman.service;

import com.ulutman.mapper.ConditionsMapper;
import com.ulutman.mapper.PropertyDetailsMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.*;
import com.ulutman.model.enums.*;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublishService {

    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;
    private final UserRepository userRepository;
    private final PropertyDetailsMapper propertyDetailsMapper;
    private final ConditionsMapper conditionsMapper;


    public PublishResponse createPublish(PublishRequest publishRequest) {
        if (publishRequest.getCategory() == null || publishRequest.getSubcategory() == null) {
            throw new IllegalArgumentException("Необходимо выбрать категорию и подкатегорию");
        }

        if (!Category.getAllCategories().contains(publishRequest.getCategory())) {
            throw new IllegalArgumentException("Неверная категория");
        }

        Publish publish = publishMapper.mapToEntity(publishRequest);

        User user = userRepository.findById(publishRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден " + publishRequest.getUserId()));
        publish.setUser(user);
        publish.setPublishStatus(PublishStatus.ОДОБРЕН);
        publish.setCategoryStatus(CategoryStatus.АКТИВНО);
        publish.setActive(true);


        // Проверяем, если данные корректные и создаем публикацию
        Publish savedPublish;
        try {
            savedPublish = publishRepository.save(publish);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Ошибка при сохранении публикации: " + e.getMessage());
        }

        // Логируем успешное создание
        log.info("Publication created successfully: {}", savedPublish);

        PublishResponse publishResponse = publishMapper.mapToResponse(savedPublish);
        return publishResponse;
    }


    public PublishResponse createPublishDetails(PublishRequest publishRequest) {
        // Проверка на наличие категории и подкатегории
        if (publishRequest.getCategory() == null || publishRequest.getSubcategory() == null) {
            throw new IllegalArgumentException("Необходимо выбрать категорию и подкатегорию");
        }

        // Проверка корректности категории и подкатегории
        if (!Category.getAllSubcategories(publishRequest.getCategory()).contains(publishRequest.getSubcategory())) {
            throw new IllegalArgumentException("Неверная подкатегория для выбранной категории");
        }

        if (!Category.getAllCategories().contains(publishRequest.getCategory())) {
            throw new IllegalArgumentException("Неверная категория");
        }

        // Создание публикации
        Publish publish = publishMapper.mapToEntity(publishRequest);
        User user = userRepository.findById(publishRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден " + publishRequest.getUserId()));
        publish.setUser(user);

        if (publish.getId() == null) {
            publish.setCreateDate(LocalDate.now());
        }

        publish.setPublishStatus(PublishStatus.ОДОБРЕН);
        publish.setCategoryStatus(CategoryStatus.АКТИВНО);

        // Проверка необходимости PropertyDetails для категории
        if (publishRequest.getCategory() == Category.REAL_ESTATE || publishRequest.getCategory() == Category.RENT) {
            if (publishRequest.getPropertyDetails() == null) {
                throw new IllegalArgumentException("Необходимо заполнить данные о недвижимости (PropertyDetails) для категории REAL_ESTATE или RENT.");
            }
            PropertyDetails propertyDetails = propertyDetailsMapper.mapToEntity(publishRequest.getPropertyDetails());
            publish.setPropertyDetails(propertyDetails);
        }

        // Проверка на наличие условий
        if (publishRequest.getConditions() == null) {
            throw new IllegalArgumentException("Необходимо заполнить данные о условиях (Conditions) для категории REAL_ESTATE или RENT.");
        }
        Conditions conditions = conditionsMapper.mapToEntity(publishRequest.getConditions());
        publish.setConditions(conditions);

        Publish savedPublish = publishRepository.save(publish);

        return publishMapper.mapToResponse(savedPublish);
    }

    public Integer getNumberOfPublications(Long userId) {
        return publishRepository.countPublicationsByUserId(userId);
    }

//    public List<PublishResponse> getAll(Principal principal) {
//        // Получаем текущего пользователя
//        User user = userRepository.findByEmail(principal.getName())
//                .orElseThrow(() -> new NotFoundException("User not found"));
//
//        // Получаем избранное для этого пользователя
//        Favorite favorite = user.getFavorites();
//        Set<Publish> favoritePublishes = (favorite != null) ? favorite.getPublishes() : new HashSet<>();
//
//        // Возвращаем все публикации, устанавливая detailFavorite в зависимости от того, добавлено ли в избранное
//        return publishRepository.findAll().stream()
//                .peek(publish -> {
//                    // Если публикация в избранном, устанавливаем detailFavorite в true, иначе false
//                    if (favoritePublishes.contains(publish)) {
//                        publish.setDetailFavorite(true);
//                    } else {
//                        publish.setDetailFavorite(false);
//                    }
//                })
//                .map(publishMapper::mapToResponse)
//                .collect(Collectors.toList());
//    }

//    public List<PublishResponse> getAll() {
//        return publishRepository.findAll().stream()
//                .peek(publish -> publish.setDetailFavorite(false)) // Устанавливаем detailFavorite в false
//                .map(publishMapper::mapToResponse)
//                .collect(Collectors.toList());
//    }

//


    public PublishResponse findById(Long id) {
        Publish publish = publishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Публикация по идентификатору " + id + " не найдена"));
        return publishMapper.mapToResponse(publish);
    }

    public PublishResponse updatePublish(Long id, PublishRequest publishRequest) {
        Publish existingPublish = publishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Публикация не найдена по идентификатору: " + id));
        existingPublish.setDescription(publishRequest.getDescription());
        existingPublish.setMetro(publishRequest.getMetro());
        existingPublish.setAddress(publishRequest.getAddress());
        existingPublish.setImages(publishRequest.getImages());
//        existingPublish.setImage(publishRequest.getImage());
        existingPublish.setCategory(publishRequest.getCategory());
        existingPublish.setSubCategory(publishRequest.getSubcategory());
        publishRepository.save(existingPublish);
        return publishMapper.mapToResponse(existingPublish);
    }

    public void deletePublish(Long productId) {
        this.publishRepository.findById(productId).orElseThrow(() -> {
            return new EntityNotFoundException("Публикация  по идентификатору " + productId + " успешно удалено");
        });
        this.publishRepository.deleteById(productId);
    }

    public List<PublishResponse> getAll() {
        return publishRepository.findAll().stream()
                .peek(publish -> publish.setDetailFavorite(false)) // Устанавливаем detailFavorite в false
                .filter(Publish::isActive)
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PublishResponse> filterPublishes(
            Double minTotalArea,
            Double maxTotalArea,
            Double minKitchenArea,
            Double maxKitchenArea,
            Double minLivingArea,
            Double maxLivingArea,
            Integer minYear,
            Integer maxYear,
            TransportType transportType,
            Double walkingDistance,
            Double transportDistance
    ) {
        List<Publish> publishes = publishRepository.filterPublishes(
                minTotalArea,
                maxTotalArea,
                minKitchenArea,
                maxKitchenArea,
                minLivingArea,
                maxLivingArea,
                minYear,
                maxYear,
                transportType,
                walkingDistance,
                transportDistance
        );

        return publishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}