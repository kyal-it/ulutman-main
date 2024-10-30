package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.exception.UnauthorizedException;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPublishesService {
    private final UserRepository userRepository;
    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;

    public List<PublishResponse> myPublishes(Long userId) {
        List<Publish> myPublish = userRepository.getAllPublishByUserId(userId);
        List<PublishResponse> appResponses = new ArrayList<>();

        for (Publish publish : myPublish) {
            if (publish.isActive()) {
                appResponses.add(publishMapper.mapToResponse(publish));
            }
        }

        return appResponses;
    }

    @Transactional
    public PublishResponse deactivatePublish(Long userId, Long publishId) throws UnauthorizedException {
    Publish publish = publishRepository.findById(publishId).orElseThrow(() -> new NotFoundException("Публикация не найдена"));
    if (!publish.getUser().getId().equals(userId)) {
        throw new UnauthorizedException("Недостаточно прав для деактивации этой публикации");
    }
    publish.setActive(false);
    Publish deactivatedPublish = publishRepository.save(publish);
    return publishMapper.mapToResponse(deactivatedPublish);
}

    @Transactional
    public PublishResponse activatePublish(Long userId, Long publishId) throws UnauthorizedException {
    Publish publish = publishRepository.findById(publishId).orElseThrow(() -> new NotFoundException("Публикация не найдена"));
    if (!publish.getUser().getId().equals(userId)) {
        throw new UnauthorizedException("Недостаточно прав для активации этой публикации");
    }
    publish.setActive(true);
    Publish activatedPublish = publishRepository.save(publish);
    return publishMapper.mapToResponse(activatedPublish);
}
    @Transactional
    public void deletePublishesByUser(Long userId, Set<Long> publishIds) {
        List<Publish> userPublishes = userRepository.getAllPublishByUserId(userId);

        userPublishes.removeIf(publish -> !publishIds.contains(publish.getId()));

        publishRepository.deleteAll(userPublishes);
    }

    @Transactional
    public void deleteAllUserPublishes(Long userId) {
        List<Publish> userPublishes = userRepository.getAllPublishByUserId(userId);
        publishRepository.deleteAll(userPublishes);
    }

    public List<PublishResponse> getInactivePublishes(Long userId) {
        List<Publish> inactivePublishes = publishRepository.findByUserIdAndActiveFalse(userId);
        return inactivePublishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public PublishResponse updatePublish(Long userId, Long publishId, PublishRequest publishRequest) throws UnauthorizedException, NotFoundException {
        Publish existingPublish = publishRepository.findById(publishId)
                .orElseThrow(() -> new NotFoundException("Публикация не найдена"));

        if (!existingPublish.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Недостаточно прав для редактирования этой публикации");
        }

        existingPublish.setTitle(publishRequest.getTitle());
        existingPublish.setDescription(publishRequest.getDescription());
        existingPublish.setPrice(publishRequest.getPrice());
        existingPublish.setCategory(publishRequest.getCategory());
        existingPublish.setSubCategory(publishRequest.getSubcategory());
        existingPublish.setMetro(publishRequest.getMetro());
        existingPublish.setAddress(publishRequest.getAddress());
        existingPublish.setImages(publishRequest.getImages());
        existingPublish.setPublishStatus(publishRequest.getPublishStatus());
        Publish updatedPublish = publishRepository.save(existingPublish);
        return publishMapper.mapToResponse(updatedPublish);
    }

    public List<PublishResponse> getRejectedPublishes(Long userId) {
        List<Publish> rejectedPublishes = publishRepository.findByUserIdAndStatus(userId, PublishStatus.ОТКЛОНЕН);

        return rejectedPublishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
