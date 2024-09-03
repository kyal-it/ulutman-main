package com.ulutman.controller;

import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.dto.UserPublishesResponse;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
import com.ulutman.service.ManageCategoryService;
import com.ulutman.service.PublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/manage/category")
@Tag(name = "Manage Category")
@SecurityRequirement(name = "Authorization")
public class ManageCategoryController {

    private final ManageCategoryService manageCategoryService;
    private final PublishService publishService;

    @Operation(summary = "Update user status")
    @ApiResponse(responseCode = "201", description = "Updated category status successfully")
    @PutMapping("/{id}/categoryStatus")
    public ResponseEntity<PublishResponse> updateCategoryStatus(
            @PathVariable Long id,
            @RequestParam CategoryStatus newStatus) {

        PublishResponse updatedPublish = manageCategoryService.updateCategoryStatus(id, newStatus);
        return ResponseEntity.ok(updatedPublish);
    }

    @Operation(summary = "Get user publications ")
    @ApiResponse(responseCode = "201", description = "Return the list of the user's publications")
    @GetMapping("/{userId}/publications")
    public UserPublishesResponse getUserWithPublications(@PathVariable Long userId) {
        return manageCategoryService.getUserWithPublications(userId);
    }

    @Operation(summary = "Count user publications ")
    @ApiResponse(responseCode = "201", description = "Return count of the user's publications")
    @GetMapping("/count/{userId}")
    public int getNumberOfPublications(@PathVariable Long userId) {
        return publishService.getNumberOfPublications(userId);
    }

    @Operation(summary = "Filter categories ")
    @ApiResponse(responseCode = "201", description = "category  successfully filtered")
    @GetMapping("/filter")
    public UserPublishesResponse getUserWithFilteredPublications(@RequestParam Long userId,
                                                                 @RequestParam(required = false) List<Category> categories,
                                                                 @RequestParam(required = false) List<CategoryStatus> categoryStatuses,
                                                                 @RequestParam(required = false) Integer minPublications) {
        return manageCategoryService.getUserWithFilteredPublications(userId, categories, categoryStatuses, minPublications);
    }

    @Operation(summary = "Reset filters categories")
    @ApiResponse(responseCode = "201", description = "Users filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter() {
        return publishService.getAll();
    }
}
