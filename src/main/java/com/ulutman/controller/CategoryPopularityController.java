package com.ulutman.controller;

import com.ulutman.model.enums.Category;
import com.ulutman.service.CategoryPopularityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/categories")
@RequiredArgsConstructor
public class CategoryPopularityController {

    private final CategoryPopularityService popularityService;

    @PostMapping("/view/{category}")  //просмотр категорий
    public ResponseEntity<String> viewCategory(@PathVariable Category category) {
        popularityService.incrementViews(category);
        return ResponseEntity.ok("Просмотр увеличен для каждой категории: " + category);
    }

    @GetMapping("/popularity/views")
    public ResponseEntity<Map<Category, Integer>> getPopularityByViews() {
        Map<Category, Integer> popularity = popularityService.getCategoryPopularity(true);
        return ResponseEntity.ok(popularity);
    }

    @GetMapping("/popularity/publications")
    public ResponseEntity<Map<Category, Integer>> getPopularityByPublications() {
        Map<Category, Integer> popularity = popularityService.getCategoryPopularity(false);
        return ResponseEntity.ok(popularity);
    }
}
