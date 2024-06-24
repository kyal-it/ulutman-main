package com.ulutman.controller;

import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.service.PublishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishes")
public class PublishController {


        private final PublishService publishService;

        public PublishController(PublishService publishService) {
            this.publishService = publishService;
        }

        @PostMapping
        public ResponseEntity<PublishResponse> createPublish(@RequestBody PublishRequest publishRequest) {
            PublishResponse createdPublish = publishService.createPublish(publishRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPublish);
        }

        @GetMapping
        public ResponseEntity<List<PublishResponse>> getAllPublishes() {
            List<PublishResponse> publishes = publishService.getAll();
            return ResponseEntity.ok(publishes);
        }

        @GetMapping("/{id}")
        public ResponseEntity<PublishResponse> getPublishById(@PathVariable Long id) {
            PublishResponse publishResponse = publishService.findById(id);
            return ResponseEntity.ok(publishResponse);
        }

        @PutMapping("/{id}")
        public ResponseEntity<PublishResponse> updatePublish(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
            PublishResponse updatedPublish = publishService.updatePublish(id, publishRequest);
            return ResponseEntity.ok(updatedPublish);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePublish(@PathVariable Long id) {
            publishService.deletePublish(id);
            return ResponseEntity.noContent().build();
        }
    }