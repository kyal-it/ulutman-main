package com.ulutman.service;

import com.ulutman.exception.IncorrectCodeException;
import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.FavoriteMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.FavoriteResponse;
import com.ulutman.model.dto.FavoriteResponseList;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.repository.FavoriteRepository;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PublishRepository publishRepository;
    private final FavoriteMapper favoriteMapper;
    private final PublishMapper publishMapper;
    private  PublishResponse publishResponse;

    public FavoriteResponse addToFavorites(Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Publish publish = publishRepository.findById(productId).
                orElseThrow(() -> new NotFoundException("Товар не найден"));

        Favorite favorites = favoriteRepository.getFavoritesByUserId(user.getId());
        if (favorites == null) {
            favorites = new Favorite();
            favorites.setUserId(user.getId());
            favorites.setPublishes(new ArrayList<>());
        }
        List<Publish> publishes = favorites.getPublishes();
        publishes.add(publish);

        if (favorites.getPublishes().contains(publishes)) {
            throw new IncorrectCodeException("уже в избранном");
        }

        favorites.setPublishes(publishes);
        favoriteRepository.save(favorites);
        return favoriteMapper.mapToResponse(favorites, publish);
    }
    public FavoriteResponseList getAllFavorites(Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found with " + principal));
        Favorite favorites = favoriteRepository.getFavoritesByUserId(user.getId());
        if (favorites != null) {
            List<Publish> products = favoriteRepository.findProductsInFavorites(favorites.getId());
            FavoriteResponseList productResponse = new FavoriteResponseList();
            productResponse.setId(user.getId()); // Устанавливаем значение id на основе userId
            productResponse.setPublishResponseList(favoriteMapper.mapListToResponseList(products));
            return productResponse;
        } else {
            // Обработка ситуации, когда избранные элементы не найдены
            return new FavoriteResponseList(); // Возвращаем пустой список
        }
    }

    public void deleteFromFavorites(Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new NotFoundException("User not found"));

        Publish publish = publishRepository.findById(productId).
                orElseThrow(() -> new NotFoundException("Product not found"));
        Favorite favorite = user.getFavorite();
        List<Publish> publishes = favorite.getPublishes();
        publishes.remove(publish);
        favorite.setPublishes(publishes);
        publishRepository.save(publish);
        favoriteRepository.save(favorite);
    }

    public void deleteAllFavorites(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found with this username " + principal));

        Favorite favorites = user.getFavorite();
        if (favorites != null) {
            favorites.getPublishes().clear();
            favoriteRepository.save(favorites);
        } else {
            throw new RuntimeException("Your favorites list is empty");
        }
    }
}

