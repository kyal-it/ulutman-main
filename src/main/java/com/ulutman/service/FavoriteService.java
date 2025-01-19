package com.ulutman.service;

import com.ulutman.exception.IncorrectCodeException;
import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.FavoriteMapper;
import com.ulutman.model.dto.FavoriteResponse;
import com.ulutman.model.dto.FavoriteResponseList;
import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.repository.FavoriteRepository;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PublishRepository publishRepository;
    private final FavoriteMapper favoriteMapper;

    public FavoriteResponse addToFavorites(Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Publish publish = publishRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Публикация не найдена"));

        // Проверка, уже ли публикация в избранном
        Favorite favorites = favoriteRepository.getFavoritesByUserId(user.getId());

        if (favorites != null && favorites.getPublishes().contains(publish)) {
            throw new IncorrectCodeException("Уже в избранном");
        }

        if (favorites == null) {
            // Если у пользователя еще нет избранного, создаем его
            favorites = new Favorite();
            favorites.setUser(user);
            favorites.setPublishes(new HashSet<>());
        }

        favorites.getPublishes().add(publish);

        publish.setFavoriteCount((publish.getFavoriteCount() == null ? 0L : publish.getFavoriteCount()) + 1);


        favoriteRepository.save(favorites);

        publish.setDetailFavorite(true);
        publishRepository.save(publish);


        log.info("Added to favorites");
        return favoriteMapper.mapToResponse(favorites, publish);
    }

//    public FavoriteResponse addToFavorites(Long productId, Principal principal) {
//        User user = userRepository.findByEmail(principal.getName())
//                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
//
//        Publish publish = publishRepository.findById(productId)
//                .orElseThrow(() -> new NotFoundException("Публикация не найдена"));
//
//        // Проверка, уже ли публикация в избранном
//        Favorite favorites = favoriteRepository.getFavoritesByUserId(user.getId());
//
//        if (favorites != null && favorites.getPublishes().contains(publish)) {
//            throw new IncorrectCodeException("Уже в избранном");
//        }
//
//        if (favorites == null) {
//            // Если у пользователя еще нет избранного, создаем его
//            favorites = new Favorite();
//            favorites.setUser(user);
//            favorites.setPublishes(new HashSet<>());
//        }
//
//        favorites.getPublishes().add(publish);
//
//        favoriteRepository.save(favorites);
//
//        publish.setDetailFavorite(true);
//        publishRepository.save(publish);
//
//
//        log.info("Added to favorites");
//        return favoriteMapper.mapToResponse(favorites, publish);
//    }


    public FavoriteResponseList getAllFavorites(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + principal.getName()));

        Favorite favorites = favoriteRepository.getFavoritesByUserId(user.getId());

        FavoriteResponseList favoriteResponseList = new FavoriteResponseList();
        favoriteResponseList.setId(user.getId()); // Устанавливаем id пользователя

        if (favorites != null && favorites.getPublishes() != null) {

            Set<Publish> products = favorites.getPublishes();

            favoriteResponseList.setPublishResponseList(favoriteMapper.mapListToResponseList(new ArrayList<>(products)));
        } else {
            favoriteResponseList.setPublishResponseList(new ArrayList<>());
        }

        return favoriteResponseList;
    }


    public void deleteFromFavorites(Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Publish publish = publishRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Публикация не найдена"));

        Favorite favorite = favoriteRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Избранный не найден"));

        Set<Publish> publishes = new LinkedHashSet<>(favorite.getPublishes());

        if (publishes.contains(publish)) {
            publishes.remove(publish);
            publish.setDetailFavorite(false);
        } else {
            // Если публикации нет в избранном, добавляем её и ставим статус true (красный)
            publishes.add(publish);
            publish.setDetailFavorite(true);
        }

        favorite.setPublishes(publishes);
        favoriteRepository.save(favorite);

        publishRepository.save(publish);
    }

    public void deleteAllFavorites(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("Пользователь с таким именем не найден " + principal));

        Set<Favorite> favorites = user.getFavorites();

        if (favorites != null && !favorites.isEmpty()) {
            for (Favorite favorite : favorites) {
                Set<Publish> publishes = favorite.getPublishes();

                if (publishes != null && !publishes.isEmpty()) {
                    for (Publish publish : publishes) {
                        publish.setDetailFavorite(false);
                        publishRepository.save(publish);
                    }
                    publishes.clear();
                }

                favoriteRepository.delete(favorite);
            }

            favorites.clear();
            userRepository.save(user);
        } else {
            throw new RuntimeException("Ваш список избранного пуст");
        }
    }

    public boolean isPublishInFavorites(Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Favorite favorites = favoriteRepository.getFavoritesByUserId(user.getId());

        if (favorites == null || favorites.getPublishes() == null || favorites.getPublishes().isEmpty()) {
            return false;
        }

        return favorites.getPublishes().stream()
                .anyMatch(publish -> publish.getId().equals(productId));
    }
}

