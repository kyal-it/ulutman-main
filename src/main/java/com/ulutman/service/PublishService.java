package com.ulutman.service;

import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.*;
import com.ulutman.repository.PublishRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublishService {
    private final Map<Category, Class<? extends Enum>> categoryToSubCategoryMap = new HashMap<>();

    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;

    public PublishService(PublishMapper publishMapper, PublishRepository publishRepository) {
        this.publishMapper = publishMapper;
        this.publishRepository = publishRepository;
    }

    public PublishResponse createPublish(PublishRequest publishRequest) {

        validateCategoryAndSubCategory(
                publishRequest.getCategory(),
                publishRequest.getSubCategory().getName());
                Publish publish = this.publishMapper.mapToEntity(publishRequest);
        publishRepository.save(publish);
        return this.publishMapper.mapToResponse(publish);
    }

    public List<PublishResponse> getAll() {
        return publishRepository.findAll().stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public PublishResponse findById(Long id) {
        Publish publish = publishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publish not found with id: " + id));
        return publishMapper.mapToResponse(publish);
    }

    public PublishResponse updatePublish(Long id, PublishRequest publishRequest) {
        Publish existingPublish = publishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publish not found with id: " + id));
        publishRepository.save(existingPublish);
        return publishMapper.mapToResponse(existingPublish);
    }

    public void deletePublish(Long id) {
        if (!publishRepository.existsById(id)) {
            throw new EntityNotFoundException("Publish not found with id: " + id);
        }
        publishRepository.deleteById(id);
    }

    @PostConstruct
    public void initCategoryMap() {
        categoryToSubCategoryMap.put(Category.AUTO, CAR_SUB_CATEGORY.class);
        categoryToSubCategoryMap.put(Category.RENT, RENT_SUB_CATEGORY.class);
        categoryToSubCategoryMap.put(Category.SERVICES, SERVICE_SUB_CATEGORY.class);
        categoryToSubCategoryMap.put(Category.HOTEL, HOTEL_SUB_CATEGORY.class);
        categoryToSubCategoryMap.put(Category.WORK, JOB_SUB_CATEGORY.class);
        categoryToSubCategoryMap.put(Category.SELL, SELL_SUB_CATEGORY.class);
        categoryToSubCategoryMap.put(Category.REAL_ESTATE, REALTY_SUB_CATEGORY.class);
    }

    private void validateCategoryAndSubCategory(Category category, String subCategoryName) {
        Class<? extends Enum> subCategoryClass = categoryToSubCategoryMap.get(category);
        if (subCategoryClass == null) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }

        try {
            Enum.valueOf(subCategoryClass, subCategoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid subcategory '" + subCategoryName + "' for category " + category);
        }
    }

}