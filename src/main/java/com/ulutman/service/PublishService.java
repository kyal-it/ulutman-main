package com.ulutman.service;

import com.ulutman.mapper.ConditionsMapper;
import com.ulutman.mapper.PropertyDetailsMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.*;
import com.ulutman.model.enums.*;
import com.ulutman.repository.MyPublishRepository;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final MyPublishRepository myPublishRepository;
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

//    public PublishResponse createPublish(PublishRequest publishRequest) {
//
//        if (publishRequest.getCategory() == null || publishRequest.getSubcategory() == null) {
//            throw new IllegalArgumentException("Необходимо выбрать категорию и подкатегорию");
//        }
//
//        if (!Category.getAllCategories().contains(publishRequest.getCategory())) {
//            throw new IllegalArgumentException("Неверная категория");
//        }
//
//        Publish publish = publishMapper.mapToEntity(publishRequest);
////        publish.setDetailFavorite(!publish.isDetailFavorite());
//
//        User user = userRepository.findById(publishRequest.getUserId())
//                .orElseThrow(() -> new RuntimeException("Пользователь не найден " + publishRequest.getUserId()));
//        publish.setUser(user);
//
//        publish.setPublishStatus(PublishStatus.ОДОБРЕН);
//        publish.setCategoryStatus(CategoryStatus.АКТИВНО);
//
//        if (publishRequest.getCategory() == Category.REAL_ESTATE || publishRequest.getCategory() == Category.RENT) {
//
//            if (publishRequest.getPropertyDetails() == null) {
//                throw new IllegalArgumentException("Необходимо заполнить данные о недвижимости (PropertyDetails) для категории REAL_ESTATE или RENT.");
//            }
//
//            PropertyDetails propertyDetails = propertyDetailsMapper.mapToEntity(publishRequest.getPropertyDetails());
//            publish.setPropertyDetails(propertyDetails);
//            if (publishRequest.getConditions() == null) {
//                throw new IllegalArgumentException("Необходимо заполнить данные о условиях (Conditions) для категории REAL_ESTATE или RENT.");
//            }
//            Conditions conditions = conditionsMapper.mapToEntity(publishRequest.getConditions());
//            publish.setConditions(conditions);
//        } else {
//            // Если категория не совпадает с RENT или REAL_ESTATE, устанавливаем null
//            publish.setPropertyDetails(null);
//            publish.setConditions(null);
//        }
//        if (publishRequest.getPropertyDetails() != null) {
//            throw new IllegalArgumentException("Необходимо заполнить данные о недвижимости (Conditions) для категории REAL_ESTATE или RENT.");
//        }
//
//        Publish savedPublish = publishRepository.save(publish);
//
//        MyPublish myPublish = new MyPublish();
//        myPublish.setUserAccount(user.getUserAccount());
//        myPublish.setPublish(savedPublish);
//
//        myPublishRepository.save(myPublish);
//
//        Integer numberOfPublications = getNumberOfPublications(user.getId());
//
//        PublishResponse publishResponse = publishMapper.mapToResponse(savedPublish);
//        publishResponse.setNumberOfPublications(numberOfPublications);
//
//        return publishResponse;
//    }

    public PublishResponse createPublishDetails(PublishRequest publishRequest) {

        if (publishRequest.getCategory() == null || publishRequest.getSubcategory() == null) {
            throw new IllegalArgumentException("Необходимо выбрать категорию и подкатегорию");
        }

        if (!Category.getAllSubcategories(publishRequest.getCategory()).contains(publishRequest.getSubcategory())) {
            throw new IllegalArgumentException("Неверная подкатегория для выбранной категории");
        }

        if (!Category.getAllCategories().contains(publishRequest.getCategory())) {
            throw new IllegalArgumentException("Неверная категория");
        }

        Publish publish = publishMapper.mapToEntity(publishRequest);

        User user = userRepository.findById(publishRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден " + publishRequest.getUserId()));
        publish.setUser(user);

        if (publish.getId() == null) {
            publish.setCreateDate(LocalDate.now());
        }

        publish.setPublishStatus(PublishStatus.ОДОБРЕН);
        publish.setCategoryStatus(CategoryStatus.АКТИВНО);

        if (publishRequest.getCategory() == Category.REAL_ESTATE || publishRequest.getCategory() == Category.RENT) {
            if (publishRequest.getPropertyDetails() == null) {
                throw new IllegalArgumentException("Необходимо заполнить данные о недвижимости (PropertyDetails) для категории REAL_ESTATE или RENT.");
            }
            PropertyDetails propertyDetails = propertyDetailsMapper.mapToEntity(publishRequest.getPropertyDetails());
            publish.setPropertyDetails(propertyDetails);
        } else {
            throw new IllegalArgumentException("Неверная категория. Для этой категории не требуются данные PropertyDetails.");
        }

        if (publishRequest.getConditions() == null) {
            throw new IllegalArgumentException("Необходимо заполнить данные о условиях (Conditions) для категории REAL_ESTATE или RENT.");
        }
        Conditions conditions = conditionsMapper.mapToEntity(publishRequest.getConditions());
        publish.setConditions(conditions);

        Publish savedPublish = publishRepository.save(publish);

        MyPublish myPublish = new MyPublish();
        myPublish.setUserAccount(user.getUserAccount());
        myPublish.setPublish(savedPublish);

        myPublishRepository.save(myPublish);

        Integer numberOfPublications = getNumberOfPublications(user.getId());
        System.out.println("Counting publications for userId: " + user.getId());

        PublishResponse publishResponse = publishMapper.mapToResponse(savedPublish);
        publishResponse.setNumberOfPublications(numberOfPublications);

        return publishResponse;
    }

    public Integer getNumberOfPublications(Long userId) {
        return publishRepository.countPublicationsByUserId(userId);
    }

    public List<PublishResponse> getAll() {
        return publishRepository.findAll().stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

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
        existingPublish.setImage(publishRequest.getImage());
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

    public List<PublishResponse> myPublishes(Long userId) {
        List<Publish> myPublish = userRepository.getAllPublishByUserId(userId);
        List<PublishResponse> appResponses = new ArrayList<>();
        for (Publish publish : myPublish) {
            appResponses.add(publishMapper.mapToResponse(publish));
        }
        return appResponses;
    }
}