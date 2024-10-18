package com.ulutman.controller;

import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishDetailsResponse;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.service.ManagePublishService;
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
@RequestMapping("/api/manage/publishes")
@Tag(name = "Manage Publishes")
@SecurityRequirement(name = "Authorization")
public class ManagePublishController {

    private final ManagePublishService managePublicationsService;

    @Operation(summary = "Get all publications")
    @ApiResponse(responseCode = "201", description = "Return list of publishes")
    @GetMapping("/getAll")
    public List<PublishResponse> getAll() {
        return managePublicationsService.getAllPublish();
    }

    @Operation(summary = "Update a publications by id")
    @ApiResponse(responseCode = "201", description = "Updated the publication  by id successfully")
    @PutMapping("/{id}/updateById")
    public PublishResponse updateById(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
        return managePublicationsService.updatePublish(id, publishRequest);
    }

    @Operation(summary = "Update a publication status")
    @ApiResponse(responseCode = "201", description = "Updated the publication status by id successfully")
    @PutMapping("/{id}/status")
    public ResponseEntity<PublishResponse> updateUserStatus(@PathVariable Long id,
                                                            @RequestParam PublishStatus newStatus) {
        PublishResponse publishResponse = managePublicationsService.updatePublishStatus(id, newStatus);
        return ResponseEntity.ok(publishResponse);
    }

    @Operation(summary = "Get user publications")
    @ApiResponse(responseCode = "201", description = "Return the list of the user's publications")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PublishResponse>> getPublishesByUserId(@PathVariable Long userId) {
        List<PublishResponse> publishes = managePublicationsService.getAllPublishesByUser(userId);
        return ResponseEntity.ok(publishes);
    }

    @Operation(summary = "Delete publication by id")
    @ApiResponse(responseCode = "201", description = "Deleted the publication by id successfully")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deletePublish(@PathVariable Long productId) {
        try {
            managePublicationsService.deletePublish(productId); // Вызов сервиса для удаления публикации
            return ResponseEntity.ok("Публикация с идентификатором " + productId + " успешно удалена");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Обработка ошибки, если публикация не найдена
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при удалении публикации: " + e.getMessage()); // Общая ошибка
        }
    }

//    @Operation(summary = "Delete publication by userId")
//    @ApiResponse(responseCode = "201", description = "Deleted the publication by userId successfully")
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deletePublish(@PathVariable Long id) {
//       managePublicationsService.deletePublish(id);
//        log.info("Публикация с идентификатором " + id + " успешно удалена");
//        return ResponseEntity.noContent().build();
//    }

    @Operation(summary = "Filter  users by name")
    @ApiResponse(responseCode = "201", description = "Users  by name successfully filtered")
    @GetMapping("/name/filter")
    public List<AuthResponse> filterUsers(@RequestParam(required = false) String name) {
        return managePublicationsService.filterUsersByName(name);
    }

    @Operation(summary = "Filter publishes by criteria")
    @ApiResponse(responseCode = "201", description = "Publishes successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<PublishDetailsResponse>> filterPublishes(
            @RequestParam(required = false) List<Category> categories,
            @RequestParam(required = false) List<PublishStatus> publishStatuses,
            @RequestParam(required = false) List<LocalDate> createDates,
            @RequestParam(required = false) String names) {

        List<PublishDetailsResponse> filteredPublishes = managePublicationsService.filterPublishes(categories, publishStatuses, createDates, names);

        return ResponseEntity.ok(filteredPublishes);
    }

    @Operation(summary = "Reset filters publications")
    @ApiResponse(responseCode = "201", description = "Publishes filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter() {
        return managePublicationsService.getAllPublish();
    }
}
