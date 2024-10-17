package com.ulutman.controller;

import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
import com.ulutman.service.ManageCategoryService;
import com.ulutman.service.PublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @Operation(summary = "Get user publications")
    @ApiResponse(responseCode = "201", description = "Return the list of the user's publications")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PublishResponse>> getAllPublishesByUser(@PathVariable Long userId) {
        List<PublishResponse> publishes = manageCategoryService.getAllPublishesByUser(userId);
        return ResponseEntity.ok(publishes);
    }


    @Operation(summary = "Get all users with publications")
    @ApiResponse(responseCode = "201", description = "Return the list of the user's publications")
    @GetMapping("/with-publishes")
    public ResponseEntity<List<AuthResponse>> getAllUsersWithPublishes() {
        List<AuthResponse> users = manageCategoryService.getAllUsersWithPublishes();


        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }


        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Update user status")
    @ApiResponse(responseCode = "201", description = "Updated category status successfully")
    @PutMapping("/{id}/categoryStatus")
    public ResponseEntity<PublishResponse> updateCategoryStatus(
            @PathVariable Long id,
            @RequestParam CategoryStatus newStatus) {

        PublishResponse updatedPublish = manageCategoryService.updateCategoryStatus(id, newStatus);
        return ResponseEntity.ok(updatedPublish);
    }

    @Operation(summary = "Delete publication by id")
    @ApiResponse(responseCode = "201", description = "Deleted the publication by id successfully")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deletePublish(@PathVariable Long productId) {
        try {
            manageCategoryService.deletePublish(productId); // Вызов сервиса для удаления публикации
            return ResponseEntity.ok("Публикация с идентификатором " + productId + " успешно удалена");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Обработка ошибки, если публикация не найдена
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при удалении публикации: " + e.getMessage()); // Общая ошибка
        }
    }

    @Operation(summary = "Filter  users by name")
    @ApiResponse(responseCode = "201", description = "Users  by name successfully filtered")
    @GetMapping("/name/filter")
    public List<AuthResponse> filterUsers(@RequestParam(required = false) String name) {
        return manageCategoryService.filterUsersByName(name);
    }

    @GetMapping("/filter")
    public List<PublishResponse> filterPublishes(
            @RequestParam(required = false) List<Category> categories,
            @RequestParam(required = false) List<CategoryStatus> categoryStatuses,
            @RequestParam(required = false) List<LocalDate> createDates,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String names) {


        return manageCategoryService.filterPublishes(categories, categoryStatuses, createDates, title, names);
    }

    @Operation(summary = "Reset filters categories")
    @ApiResponse(responseCode = "201", description = "Users filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter() {
        return publishService.getAll();
    }

    @Operation(summary = "Count user publications ")
    @ApiResponse(responseCode = "201", description = "Return count of the user's publications")
    @GetMapping("/count/{userId}")
    public int getNumberOfPublications(@PathVariable Long userId) {
        return publishService.getNumberOfPublications(userId);
    }
}
