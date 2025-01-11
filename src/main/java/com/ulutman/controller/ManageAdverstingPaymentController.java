package com.ulutman.controller;

import com.ulutman.model.entities.AdVersiting;
import com.ulutman.service.ManagePaymentAdversting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/adversting")
public class ManageAdverstingPaymentController {

    private final ManagePaymentAdversting managePaymentAdversting;

    @GetMapping("/deactivated")
    @Operation(summary = "Create a getAllDeactivatedPublications")
    @ApiResponse(responseCode = "201", description = "getAllDeactivatedPublications created successfully")
    public ResponseEntity<List<AdVersiting>> getAllDeactivatedPublications() {
        List<AdVersiting> deactivatedPublications = managePaymentAdversting.getAllDeactivatedPublications();
        return ResponseEntity.ok(deactivatedPublications);
    }


    @Operation(summary = "Create a activatePublication")
    @ApiResponse(responseCode = "201", description = "activatePublication created successfully")
    @PostMapping("/activate/{publicationId}")
    public ResponseEntity<String> activatePublication(@PathVariable Long publicationId) {
        managePaymentAdversting.activatePublication(publicationId);
        return ResponseEntity.ok("Публикация активирована успешно.");
    }

    @Operation(summary = "Create a deactivatePublication")
    @ApiResponse(responseCode = "201", description = "deactivatePublication created successfully")
    @PostMapping("/deactivate/{publicationId}")
    public ResponseEntity<String> deactivatePublication(@PathVariable Long publicationId) {
        managePaymentAdversting.deactivatePublication(publicationId);
        return ResponseEntity.ok("Публикация деактивирована успешно.");
    }
}


