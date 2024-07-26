package com.ulutman.controller;

import com.ulutman.model.dto.FavoriteResponse;
import com.ulutman.model.dto.FavoriteResponseList;
import com.ulutman.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FavoriteController {

    private final FavoriteService favoriteService;
    @PostMapping("/addToFavorites/{id}")
    public FavoriteResponse addToFavorites(@PathVariable("id") Long publishId, Principal principal){
        return favoriteService.addToFavorites(publishId, principal);
    }

    @GetMapping("/getAllFavorites")
    public FavoriteResponseList getAllFavorites(Principal principal) {
        return favoriteService.getAllFavorites(principal);
    }

    @DeleteMapping("/deleteFromFavorites/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long publishId, Principal principal){
        favoriteService.deleteFromFavorites(publishId, principal);
        return ResponseEntity.ok("Публикация, успешно удаленная из избранного");
    }

    @PostMapping("/deleteAllFavorites")
    public ResponseEntity<String> deleteAll(Principal principal) {
        favoriteService.deleteAllFavorites(principal);
        return ResponseEntity.ok("Ваш список избранного был успешно очищен");
    }
}
