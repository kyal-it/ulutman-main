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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Filter by title ")
    @ApiResponse(responseCode = "201", description = "title  successfully filtered")
    @GetMapping("/title/filter")
    public ResponseEntity<?> getPublishesByTitle(@RequestParam("title") String title) {
        try {
            List<PublishResponse> publishResponses = manageCategoryService.filterPublishesByTitle(title);
            return ResponseEntity.ok(publishResponses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//Фильтрации по title по нижнему регистру
//    @Operation(summary = "Filter by title ")
//    @ApiResponse(responseCode = "201", description = "title  successfully filtered")
//    @GetMapping("/title/filter")
//    public List<PublishResponse> getProductsByTitle(@RequestParam(required = false) String title) {
//        return manageCategoryService.getProductsByTitle(title);
//    }

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

    @Operation(summary = "Filter by count ")
    @ApiResponse(responseCode = "201", description = "count  successfully filtered")
    @GetMapping("/count/filter")
    public ResponseEntity<List<PublishResponse>> filterByPublicationCount(@RequestParam(required = false) Integer minCount,
                                                                          @RequestParam(required = false) Integer maxCount) {
        try {
            List<PublishResponse> responses = manageCategoryService.getProductsByPublicationCount(minCount, maxCount);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Reset filters categories")
    @ApiResponse(responseCode = "201", description = "Users filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter() {
        return publishService.getAll();
    }
}
