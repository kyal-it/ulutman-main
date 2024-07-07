package com.ulutman.controller;

import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.service.ManagePublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage")
public class ManagePublishController {

    private final ManagePublishService managePublicationsService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PublishResponse> getAll() {
        return managePublicationsService.getAllPublish();
    }

    @PutMapping("/updateById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PublishResponse updateById(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
        return managePublicationsService.updatePublish(id, publishRequest);
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteById(@PathVariable Long id) {
        managePublicationsService.deletePublish(id);
        return "Публикация успешно удалена";
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PublishResponse updateStatus(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
        return managePublicationsService.addPublishStatus(id, publishRequest);
    }

    @GetMapping("/filter")
    public List<PublishResponse> filterPublishes(
            @RequestParam(value = "description", required = false) List<String> descriptions,
            @RequestParam(value = "category", required = false) List<String> categories,
            @RequestParam(value = "createDate", required = false) List<LocalDate> createDate,
            @RequestParam(value = "publishStatus", required = false) List<String> publishStatus) {

        return managePublicationsService.getFilteredPublishes(descriptions, categories, createDate, publishStatus);
    }

    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter() {
        return managePublicationsService.getAllPublish();
    }
}
