package com.ulutman.controller;

import com.ulutman.model.entities.Publish;
import com.ulutman.service.ManageCreatePaymentSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class ManageCreatePaymentSystemController {
    private final ManageCreatePaymentSystem manageCreatePaymentSystem;


    @GetMapping("/deactivated")
    public ResponseEntity<List<Publish>> getAllDeactivatedPublications() {
        List<Publish> deactivatedPublications = manageCreatePaymentSystem.getAllDeactivatedPublications();
        return ResponseEntity.ok(deactivatedPublications);
    }

    @PostMapping("/activate/{publicationId}")
    public ResponseEntity<String> activatePublication(@PathVariable Long publicationId) {
        manageCreatePaymentSystem.activatePublication(publicationId);
        return ResponseEntity.ok("Публикация активирована успешно.");
    }

    @PostMapping("/deactivate/{publicationId}")
    public ResponseEntity<String> deactivatePublication(@PathVariable Long publicationId) {
        manageCreatePaymentSystem.deactivatePublication(publicationId);
        return ResponseEntity.ok("Публикация деактивирована успешно.");
    }
}
