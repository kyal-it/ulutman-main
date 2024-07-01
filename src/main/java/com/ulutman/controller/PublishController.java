package com.ulutman.controller;

import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.repository.PublishRepository;
import com.ulutman.service.PublishService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishes")
public class PublishController {

       private final PublishMapper publishMapper;
       private final PublishRepository publishRepository;
        private final PublishService publishService;

        public PublishController(PublishMapper publishMapper, PublishRepository publishRepository, PublishService publishService) {
            this.publishMapper = publishMapper;
            this.publishRepository = publishRepository;
            this.publishService = publishService;
        }


       @PostMapping
       @ResponseStatus(HttpStatus.CREATED)
       public PublishResponse createPublish(@RequestBody PublishRequest publishRequest) {
            return publishService.createPublish(publishRequest);
       }


        @GetMapping
        public ResponseEntity<List<PublishResponse>> getAllPublishes() {
            List<PublishResponse> publishes = publishService.getAll();
            return ResponseEntity.ok(publishes);
        }

    @GetMapping("/{id}")
    public  PublishResponse findById(@PathVariable Long id) {
            return  this.publishService.findById(id);
    }

/*    @GetMapping("/{id}")
    public PublishResponse findById(@PathVariable Long id) {
        Publish publish = this.publishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Publish with id " + id + " not found"));
        return this.publishMapper.mapToResponse(publish);
    }*/

        @PutMapping("/{id}")
        public ResponseEntity<PublishResponse> updatePublish(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
            PublishResponse updatedPublish = publishService.updatePublish(id, publishRequest);
            return ResponseEntity.ok(updatedPublish);
        }

/*        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePublish(@PathVariable Long id) {
            publishService.deletePublish(id);
            return ResponseEntity.noContent().build();
        }*/

    @DeleteMapping(("/{id}"))
    public String delete(@PathVariable("id") Long id) {
        this.publishService.deletePublish(id);
        return "Delete publish with id:" + id + " successfully delete";
    }
    }