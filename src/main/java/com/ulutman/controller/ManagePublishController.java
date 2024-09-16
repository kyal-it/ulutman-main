package com.ulutman.controller;

import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.service.ManagePublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
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
    @DeleteMapping("/deleteById/{id}")
    public String deleteById(@PathVariable Long id) {
        managePublicationsService.deletePublish(id);
        return "Публикация успешно удалена";
    }

    @Operation(summary = "Delete publication by userId")
    @ApiResponse(responseCode = "201", description = "Deleted the publication by userId successfully")
    @DeleteMapping("/delete/user/{userId}")
    public String deletePublicationsByUserId(@PathVariable Long userId) {
        managePublicationsService.deletePublicationsByUserId(userId);
        return ("Все публикации пользователя с идентификатором " + userId + " успешно удалены");
    }

    @Operation(summary = "Filter  users by name")
    @ApiResponse(responseCode = "201", description = "Users  by name successfully filtered")
    @GetMapping("/name/filter")
    public List<AuthResponse> filterUsers(@RequestParam(required = false) String name) {
        return managePublicationsService.filterUsersByName(name);
    }

    @Operation(summary = "Filter publications")
    @ApiResponse(responseCode = "201", description = "Publications  successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<PublishResponse>> filterPublishes(
            @RequestParam(value = "categories", required = false) List<Category> categories,
            @RequestParam(value = "publishStatuses", required = false) List<PublishStatus> publishStatuses,
            @RequestParam(value = "createDate", required = false) List<LocalDate> createDates
    ) {
        List<PublishResponse> publishResponses = managePublicationsService.filterPublishes(categories, publishStatuses, createDates);
        return ResponseEntity.ok(publishResponses);
    }

    @Operation(summary = "Reset filters publications")
    @ApiResponse(responseCode = "201", description = "Publishes filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter() {
        return managePublicationsService.getAllPublish();
    }
}
