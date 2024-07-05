package com.ulutman.service;

import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.*;
import com.ulutman.repository.PublishRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublishService {

    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;

    public PublishService(PublishMapper publishMapper, PublishRepository publishRepository) {
        this.publishMapper = publishMapper;
        this.publishRepository = publishRepository;
    }

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
        publishRepository.save(existingPublish);
        return publishMapper.mapToResponse(existingPublish);
    }

  /*  public void deletePublish(Long id) {*/
/*        if (!publishRepository.existsById(id)) {
            throw new EntityNotFoundException("Публикация не найдена с идентификатором: " + id);
        }
        publishRepository.deleteById(id);
    }*/

    public void deletePublish(Long productId) {
        this.publishRepository.findById(productId).orElseThrow(() -> {
            return new EntityNotFoundException("Publish with id" + productId +  "not found");
        });
        this.publishRepository.deleteById(productId);
    }



}