package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.mapper.UserPublishesMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.dto.UserPublishesResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageCategoryService {

    private final PublishRepository publishRepository;
    private final PublishMapper publishMapper;
    private final UserRepository userRepository;
    private final UserPublishesMapper userPublishesMapper;

    public PublishResponse addCategoryAndSubCategory(PublishRequest publishRequest) {
        Publish publish = new Publish();
        publish.setCategory(publishRequest.getCategory());
        publish.setSubCategory(publishRequest.getSubcategory());
        publishRepository.save(publish);
        return publishMapper.mapToResponse(publish);
    }

    public PublishResponse updateCategoryAndSubCategory(Long id, @RequestBody PublishRequest publishRequest) {
        Publish publish = publishRepository.findById(id).orElseThrow(() -> new NotFoundException("Публикация  по идентификатору " + id + " не найдена "));
        publish.setCategory(publishRequest.getCategory());
        publish.setSubCategory(publishRequest.getSubcategory());
        publishRepository.save(publish);
        return publishMapper.mapToResponse(publish);
    }

    public void deleteCategoryAndSubCategory(Long id) {
        publishRepository.deleteById(id);
        log.info("Публикация по идентификатору " + id + " успешно удалена");
    }

    public UserPublishesResponse getUserWithPublications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь по идентификатору " + userId + " не найден"));
        List<Publish> publishes = user.getPublishes();
        return userPublishesMapper.mapToResponse(user, publishes);
    }
}
