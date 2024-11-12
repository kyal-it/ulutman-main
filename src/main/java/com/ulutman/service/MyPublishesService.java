package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.exception.UnauthorizedException;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.repository.FavoriteRepository;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPublishesService {
    private final UserRepository userRepository;
    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;
    private final FavoriteRepository favoriteRepository;


    public List<PublishResponse> myActivePublishes(Long userId) {
        List<Publish> myActivePublish = userRepository.getAllPublishByUserId(userId);

        return myActivePublish.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
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

    public Integer getFavoritesCount(Long userId, Long publishId) {
        Publish publish = publishRepository.findById(publishId)
                .orElseThrow(() -> new NotFoundException("Публикация не найдена"));

        if (!publish.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Недостаточно прав для просмотра статистики этой публикации");
        }

        Long count = favoriteRepository.countByPublishId(publishId);

        return count.intValue();
    }

    public List<PublishResponse> myActivePublications(Long userId) {
        List<Publish> myActivePublish = userRepository.getAllPublishByUserId(userId);

        myActivePublish.forEach(this::boostIfNeeded);

        return myActivePublish.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    private void boostIfNeeded(Publish publish) {
        LocalDateTime now = LocalDateTime.now();
        if (publish.getLastBoostedAt() == null || ChronoUnit.HOURS.between(publish.getLastBoostedAt(), now) >= 24) {
            publish.setLastBoostedAt(now);
            publishRepository.save(publish); // Используем правильный репозиторий
        }
    }
}
