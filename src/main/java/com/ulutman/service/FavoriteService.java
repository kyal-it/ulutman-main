package com.ulutman.service;

import com.ulutman.exception.IncorrectCodeException;
import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.FavoriteMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.FavoriteResponse;
import com.ulutman.model.dto.FavoriteResponseList;
import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.repository.FavoriteRepository;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private UserRepository userRepository;
    private PublishRepository publishRepository;
    private FavoriteMapper favoriteMapper;
    private PublishMapper publishMapper;

    public FavoriteResponse addToFavorites(Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Publish product = publishRepository.findById(productId).
                orElseThrow(() -> new NotFoundException("Товар не найден"));

        Favorite favorites = favoriteRepository.getFavoritesByUserId(user.getId());
        List<Publish> products = favorites.getPublish();
        products.add(product);
        if (favorites.getPublish().contains(products)) {
            throw new IncorrectCodeException("уже в избранном");
        }
        favorites.setPublish(products);
        favoriteRepository.save(favorites);
        return favoriteMapper.mapToResponse(favorites, product);
    }

    public FavoriteResponseList getAllFavorites(Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found with " + principal));

        Favorite favorites = favoriteRepository.getFavoritesByUserId(user.getId());
        List<Publish> products = favoriteRepository.findProductsInFavorites(favorites.getId());
        FavoriteResponseList productResponse = new FavoriteResponseList();
        productResponse.setId(favorites.getId());
        productResponse.setPublishResponseList(products.stream().map(publishMapper::mapToResponse).toList());
        return productResponse;
    }

    public void deleteFromFavorites(Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new NotFoundException("User not found"));

        Publish product = publishRepository.findById(productId).
                orElseThrow(() -> new NotFoundException("Product not found"));
        Favorite favorites = user.getFavorite();
        List<Publish> products = favorites.getPublish();
        products.remove(product);
        favorites.setPublish(products);
        publishRepository.save(product);
        favoriteRepository.save(favorites);
    }

    public void deleteAllFavorites(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found with this username " + principal));

        Favorite favorites = user.getFavorite();
        if (favorites != null) {
            favorites.getPublish().clear();
            favoriteRepository.save(favorites);
        } else {
            throw new RuntimeException("Your favorites list is empty");
        }
    }
}
