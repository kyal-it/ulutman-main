package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.repository.PublishRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final PublishService publishService;

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

//    @PostMapping("/create/{userId}")
//    public ResponseEntity<Publish> createPublish(@PathVariable Long userId, @RequestBody Publish publish) {
//        Publish createdPublish = publishService.createPublish(userId, publish);
//        return ResponseEntity.ok(createdPublish);
//    }

    // Метод для получения всех публикаций пользователя
    public List<PublishResponse> getAllPublishesByUser(Long userId) {
        List<Publish> publishes = publishRepository.findAllByUserId(userId);
        return publishes.stream()
                .map(publishMapper::mapToResponse)  // Маппинг публикации в DTO
                .collect(Collectors.toList());
    }

    public void deletePublish(Long productId) {
        this.publishRepository.findById(productId).orElseThrow(() -> {
            return new EntityNotFoundException("Публикация  по идентификатору " + productId + " успешно удалена");
        });
        this.publishRepository.deleteById(productId);
        log.info("Публикация  по идентификатору" + productId + " успешна удалена");
    }

    public List<PublishResponse> filterPublish(List<String> descriptions,
                                               List<String> categories,
                                               List<LocalDate> createDate,
                                               List<String> publishStatuses) {
        List<Publish> publishes = publishRepository.publishFilter(descriptions, categories, createDate, publishStatuses);
        return publishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
