package com.ulutman.controller;

import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.dto.UserPublishesResponse;
import com.ulutman.service.ManageCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/category")
public class ManageCategoryController {

    private final ManageCategoryService categoryAndSubCategoryService;

    @PostMapping("/addCatAndSub")
    public ResponseEntity<PublishResponse> addCategoryAndSubCategory(@RequestBody PublishRequest publishRequest) {
        PublishResponse publishResponse = categoryAndSubCategoryService.addCategoryAndSubCategory(publishRequest);
        return ResponseEntity.ok(publishResponse);
    }

    @PutMapping("/updateById/{id}")
    public PublishResponse updateCategoryAndSubCategory( Long id, @RequestBody PublishRequest publishRequest) {
        return categoryAndSubCategoryService.updateCategoryAndSubCategory(id, publishRequest);
    }

    @PutMapping("/deleteById/{id}")
    public void deleteCategoryAndSubCategory(Long id) {
        categoryAndSubCategoryService.deleteCategoryAndSubCategory(id);
        log.info("Публикация по идентификатору "+ id +" успешна удалена ");
    }

    @GetMapping("/{id}/publications")
    public ResponseEntity<UserPublishesResponse> getUserWithPublications(@PathVariable Long id) {
        UserPublishesResponse response = categoryAndSubCategoryService.getUserWithPublications(id);
        return ResponseEntity.ok(response);
    }
}
