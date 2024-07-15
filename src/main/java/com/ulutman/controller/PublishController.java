package com.ulutman.controller;

import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.service.PublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishes")
public class PublishController {

    private final PublishService publishService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PublishResponse createPublish(@RequestBody PublishRequest publishRequest) {
        return publishService.createPublish(publishRequest);
    }

    @GetMapping("/getAll")
    public List<PublishResponse> getAll() {
        return publishService.getAll();
    }

    @GetMapping
    public ResponseEntity<List<PublishResponse>> getAllPublishes() {
        List<PublishResponse> publishes = publishService.getAll();
        return ResponseEntity.ok(publishes);
    }

    @GetMapping("find/{id}")
    public PublishResponse findById(@PathVariable Long id) {
        return this.publishService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PublishResponse> updatePublish(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
        PublishResponse updatedPublish = publishService.updatePublish(id, publishRequest);
        return ResponseEntity.ok(updatedPublish);
    }

    @DeleteMapping(("/delete/{id}"))
    public String delete(@PathVariable("id") Long id) {
        this.publishService.deletePublish(id);
        return "Delete publish with id:" + id + " successfully delete";
    }
}