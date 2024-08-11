package com.ulutman.controller;

import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.service.ManagePublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/publishes")
@Tag(name = "Auth")
@SecurityRequirement(name = "Authorization")
public class ManagePublishController {

    private final ManagePublishService managePublicationsService;

    @Operation(summary = "Manage  publishes: get all publishes")
    @ApiResponse(responseCode = "201", description = "Return list of publishes")
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PublishResponse> getAll() {
        return managePublicationsService.getAllPublish();
    }

    @Operation(summary = "Manage  publishes: update publish by Id")
    @ApiResponse(responseCode = "201", description = "Updated publish by id successfully")
    @PutMapping("/updateById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PublishResponse updateById(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
        return managePublicationsService.updatePublish(id, publishRequest);
    }

    @Operation(summary = "Manage  publishes: delete publish by id")
    @ApiResponse(responseCode = "201", description = "Deleted publish by id successfully")
    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteById(@PathVariable Long id) {
        managePublicationsService.deletePublish(id);
        return "Публикация успешно удалена";
    }

    @Operation(summary = "Manage  publishes: update publish status")
    @ApiResponse(responseCode = "201", description = "Updated publish status by id successfully")
    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PublishResponse updateStatus(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
        return managePublicationsService.addPublishStatus(id, publishRequest);
    }

    @Operation(summary = "Manage  publishes: filter publishes")
    @ApiResponse(responseCode = "201", description = "Publishes successfully filtered")
    @GetMapping("/filter")
    public List<PublishResponse> filterPublishes(
            @RequestParam(value = "description", required = false) List<String> descriptions,
            @RequestParam(value = "category", required = false) List<String> categories,
            @RequestParam(value = "createDate", required = false) List<LocalDate> createDate,
            @RequestParam(value = "publishStatus", required = false) List<String> publishStatus) {

        return managePublicationsService.getFilteredPublishes(descriptions, categories, createDate, publishStatus);
    }

    @Operation(summary = "Manage publishes: reset filters publishes")
    @ApiResponse(responseCode = "201", description = "Publishes filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter() {
        return managePublicationsService.getAllPublish();
    }
}
