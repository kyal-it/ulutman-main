package com.ulutman.controller;

import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.dto.UserPublishesResponse;
import com.ulutman.model.entities.Publish;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

//    @Operation(summary = "Get user publications ")
//    @ApiResponse(responseCode = "201", description = "Return the list of the user's publications")
//    @GetMapping("/{userId}/publications")
//    public UserPublishesResponse getUserWithPublications(@PathVariable Long userId) {
//        return manageCategoryService.getUserWithFilteredPublications(userId);
//    }

    @Operation(summary = "Count user publications ")
    @ApiResponse(responseCode = "201", description = "Return count of the user's publications")
    @GetMapping("/count/{userId}")
    public int getNumberOfPublications(@PathVariable Long userId) {
        return publishService.getNumberOfPublications(userId);
    }

    @Operation(summary = "Filter  users by name")
    @ApiResponse(responseCode = "201", description = "Users  by name successfully filtered")
    @GetMapping("/name/filter")
    public List<AuthResponse> filterUsers(@RequestParam(required = false) String name) {
        return manageCategoryService.filterUsersByName(name);
    }

    @GetMapping("/title/filter")
    public List<Publish> getProductsByTitle(@RequestParam(required = false) String title) {
        return manageCategoryService.getProductsByTitle(title);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidTitleException(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @Operation(summary = "Filter categories ")
    @ApiResponse(responseCode = "201", description = "category  successfully filtered")
    @GetMapping("/filter")
    public List<PublishResponse> filterPublicationsByCategoryAndStatus(@RequestParam(required = false) List<Category> categories,
                                                                       @RequestParam(required = false) List<CategoryStatus> categoryStatuses) {
        return manageCategoryService.filterPublicationsByCategoryAndStatus(categories, categoryStatuses);
    }

//    @Operation(summary = "Filter categories by count ")
//    @ApiResponse(responseCode = "201", description = "category  successfully filtered")
//    @GetMapping("/filterByMinCount")
//    public ResponseEntity<?> filterPublicationsByCount(
//            @RequestParam Integer minPublications) {
//
//        try {
//            List<PublishResponse> filteredPublications = manageCategoryService.filterPublicationsByMinCount(minPublications);
//            return ResponseEntity.ok(filteredPublications);
//        } catch (IllegalArgumentException e) {
//            log.info("Ошибка при фильтрации публикаций: {}", e.getMessage());
//
//            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
//        } catch (Exception e) {
//            log.info("Доступ запрещен: {}", e.getMessage());
//
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ошибка: Доступ запрещен.");
//        }
//    }

    @Operation(summary = "Reset filters categories")
    @ApiResponse(responseCode = "201", description = "Users filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter() {
        return publishService.getAll();
    }
}
