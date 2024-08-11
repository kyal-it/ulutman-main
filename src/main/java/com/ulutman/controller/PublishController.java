package com.ulutman.controller;

import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.service.PublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishes")
@Tag(name = "Auth")
@SecurityRequirement(name = "Authorization")
public class PublishController {

    private final PublishService publishService;

    @Operation(summary = "Create publish")
    @ApiResponse(responseCode = "201", description = "Publish created successfully")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PublishResponse createPublish(@RequestBody PublishRequest publishRequest) {
        return publishService.createPublish(publishRequest);
    }

    @Operation(summary = "Publishes: get all publishes")
    @ApiResponse(responseCode = "201", description = "Return list of publishes")
    @GetMapping("/getAll")
    public List<PublishResponse> getAll() {
        return publishService.getAll();
    }

    @GetMapping
    public ResponseEntity<List<PublishResponse>> getAllPublishes() {
        List<PublishResponse> publishes = publishService.getAll();
        return ResponseEntity.ok(publishes);
    }

    @Operation(summary = "Publishes: get publish by Id")
    @ApiResponse(responseCode = "201", description = "Publish found")
    @GetMapping("find/{id}")
    public PublishResponse findById(@PathVariable Long id) {
        return this.publishService.findById(id);
    }

    @Operation(summary = "Publishes: update publish by Id")
    @ApiResponse(responseCode = "201", description = "Updated publish by id successfully")
    @PutMapping("/update/{id}")
    public ResponseEntity<PublishResponse> updatePublish(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
        PublishResponse updatedPublish = publishService.updatePublish(id, publishRequest);
        return ResponseEntity.ok(updatedPublish);
    }

    @Operation(summary = "Manage  publishes: delete publish by id")
    @ApiResponse(responseCode = "201", description = "Deleted publish by id successfully")
    @DeleteMapping(("/delete/{id}"))
    public String delete(@PathVariable("id") Long id) {
        this.publishService.deletePublish(id);
        return "Delete publish with id:" + id + " successfully delete";
    }
}