package com.ulutman.controller;

import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.service.PublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/publishes")
@Tag(name = "Publish")
@SecurityRequirement(name = "Authorization")
public class PublishController {

    private final PublishService publishService;

    @Operation(summary = "Create a publication")
    @ApiResponse(responseCode = "201", description = "The publish created successfully")
    @PostMapping("/create")
    public ResponseEntity<PublishResponse> createPublish(@RequestBody PublishRequest publishRequest) {
        try {
            PublishResponse response = publishService.createPublish(publishRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a publication with details")
    @ApiResponse(responseCode = "201", description = "The publish with details  created successfully")
    @PostMapping("/createDetails")
    public ResponseEntity<PublishResponse> createPublicationDetails(@RequestBody PublishRequest publishRequest) {
        try {
            PublishResponse publishResponse = publishService.createPublishDetails(publishRequest);
            return new ResponseEntity<>(publishResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при создании публикации: ", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

//    @Operation(summary = "Get all publications")
//    @ApiResponse(responseCode = "201", description = "Return list of publishes")
//    @GetMapping("/getAll")
//    public ResponseEntity<List<PublishResponse>> getAllPublishes(Principal principal) {
//        // Вызов сервиса для получения всех публикаций с учётом избранных для текущего пользователя
//        List<PublishResponse> publishes = publishService.getAll(principal);
//
//        // Возвращаем результат в виде HTTP-ответа
//        return new ResponseEntity<>(publishes, HttpStatus.OK);
//    }
//    public ResponseEntity<List<PublishResponse>> getAllPublishes() {
//        List<PublishResponse> publishes = publishService.getAll();
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(publishes);
//    }
    @GetMapping("/getAll")
    public ResponseEntity<List<PublishResponse>> getAllPublishes() {
        List<PublishResponse> publishes = publishService.getAll();
        return ResponseEntity.ok(publishes);
    }

    @Operation(summary = "Get a publication by id")
    @ApiResponse(responseCode = "201", description = "Publication found")
    @GetMapping("find/{id}")
    public PublishResponse findById(@PathVariable Long id) {
        return this.publishService.findById(id);
    }

    @Operation(summary = "Update a publication by id")
    @ApiResponse(responseCode = "201", description = "Updated  the publication  by id successfully")
    @PutMapping("/update/{id}")
    public ResponseEntity<PublishResponse> updatePublish(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
        PublishResponse updatedPublish = publishService.updatePublish(id, publishRequest);
        return ResponseEntity.ok(updatedPublish);
    }

    @Operation(summary = "Delete a publication by id")
    @ApiResponse(responseCode = "201", description = "Deleted the publication  by id successfully")
    @DeleteMapping(("/delete/{id}"))
    public String delete(@PathVariable("id") Long id) {
        this.publishService.deletePublish(id);
        return "Delete publish with id:" + id + " successfully delete";
    }

    @Operation(summary = "My publishes")
    @ApiResponse(responseCode = "201", description = "return list of my publishes successfully")
    @GetMapping("/my")
    public ResponseEntity<List<PublishResponse>> getMyPublishes(@RequestParam Long userId) {
        List<PublishResponse> publishes = publishService.myPublishes(userId);
        return ResponseEntity.ok(publishes);
    }
}