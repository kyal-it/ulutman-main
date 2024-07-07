package com.ulutman.service;

import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.*;
import com.ulutman.repository.PublishRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublishService {

    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;

    public PublishResponse createPublish(PublishRequest publishRequest) {
        if (publishRequest.getCategory() == null || publishRequest.getSubcategory() == null) {
            throw new IllegalArgumentException("Необходимо выбрать категорию и подкатегорию");
        }
        if (!Category.getAllSubcategories(publishRequest.getCategory()).contains(publishRequest.getSubcategory())) {
            throw new IllegalArgumentException("Неверная подкатегория для выбранной категории");
        }
        if (!Category.getAllCategories().contains(publishRequest.getCategory())) {
            throw new IllegalArgumentException("Неверная категория");
        }
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
        Publish publish = (Publish)this.publishRepository.findById(id).orElseThrow(() -> {
        return new EntityNotFoundException("Публикация с идентификатором " + id + " не найдено");
    }); return this.publishMapper.mapToResponse(publish);
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
            return new EntityNotFoundException("Публикация  по идентификатору " + productId +  " успешно удалено");
        });
        this.publishRepository.deleteById(productId);
    }



}