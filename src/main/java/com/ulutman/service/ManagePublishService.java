package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;;
import com.ulutman.repository.PublishRepository;
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

    public void deletePublish(Long productId) {
        this.publishRepository.findById(productId).orElseThrow(() -> {
            return new EntityNotFoundException("Публикация  по идентификатору " + productId + " успешно удалена");
        });
        this.publishRepository.deleteById(productId);
        log.info("Публикация  по идентификатору" + productId + " успешна удалена");
    }

    public PublishResponse addPublishStatus(Long id, @RequestBody PublishRequest publishRequest) {
        Publish publish = publishRepository.findById(id).orElseThrow(() -> new NotFoundException("Публикация по идентификатору " + id + " не найдена"));
        publish.setPublishStatus(publish.getPublishStatus());
        publishRepository.save(publish);
        return publishMapper.mapToResponse(publish);
    }

    public List<PublishResponse> getFilteredPublishes(
            List<String> descriptions,
            List<String> categories,
            List<LocalDate> createDates,
            List<String> publishStatuses) {
        List<Publish> publishList = publishRepository.publishFilter(descriptions, categories, createDates, publishStatuses);
        return publishList.stream().map(publishMapper::mapToResponse).collect(Collectors.toList());
    }
}
