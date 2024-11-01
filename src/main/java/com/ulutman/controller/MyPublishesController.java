package com.ulutman.controller;

import com.ulutman.exception.NotFoundException;
import com.ulutman.exception.UnauthorizedException;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.service.MyPublishesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/my-publishes")
@Tag(name = "my-publishes")
public class MyPublishesController {
    private final MyPublishesService publishService;

    //Возвращает список активных публикаций пользователя
    @Operation(summary = "Returns a list of the user's active posts")
    @ApiResponse(responseCode = "201", description = "successfully returned a list of active user publications")
    @GetMapping("/{userId}")
    public ResponseEntity<List<PublishResponse>> myPublishes(@PathVariable Long userId) {
        List<PublishResponse> publishes = publishService.myActivePublishes(userId);
        return ResponseEntity.ok(publishes);
    }

    //Деактивирует публикацию
    @Operation(summary = "Deactivates publication")
    @ApiResponse(responseCode = "201", description = "publication successfully deactivated")
    @PutMapping("/deactivate/{userId}/{publishId}")
    public ResponseEntity<PublishResponse> deactivatePublish(@PathVariable Long userId, @PathVariable Long publishId) throws UnauthorizedException {
        PublishResponse deactivatedPublish = publishService.deactivatePublish(userId, publishId);
        return ResponseEntity.ok(deactivatedPublish);
    }

    //Активирует публикацию
    @Operation(summary = "Activates publication")
    @ApiResponse(responseCode = "201", description = "publication successfully activated")
    @PutMapping("/activate/{userId}/{publishId}")
    public ResponseEntity<PublishResponse> activatePublish(@PathVariable Long userId, @PathVariable Long publishId) throws UnauthorizedException {
        PublishResponse activatedPublish = publishService.activatePublish(userId, publishId);
        return ResponseEntity.ok(activatedPublish);
    }

    //Удаляет публикации пользователя
    @Operation(summary = "Deletes a user's publishes")
    @ApiResponse(responseCode = "201", description = "Posts successfully deleted")
    @DeleteMapping("/delete-by-user/{userId}")
    public ResponseEntity<Void> deletePublishesByUser(@PathVariable Long userId, @RequestBody Set<Long> publishIds) {
        publishService.deletePublishesByUser(userId, publishIds);
        return ResponseEntity.noContent().build();
    }

    //Удаляет все публикации пользователя
    @Operation(summary = "Deletes all user posts")
    @ApiResponse(responseCode = "201", description = "successfully all publications were deleted")
    @DeleteMapping("/delete-all/{userId}")
    public ResponseEntity<String> deleteAllUserPublishes(@PathVariable Long userId) {
        publishService.deleteAllUserPublishes(userId);
        return ResponseEntity.ok("Все публикации успешно удалены");
    }

    //Возвращает список неактивных публикаций пользователя
    @Operation(summary = "Returns a list of inactive user posts")
    @ApiResponse(responseCode = "201", description = "successfully returned a list of inactive user publications")
    @GetMapping("/inactive-publishes/{userId}")
    public ResponseEntity<List<PublishResponse>> getInactivePublishes(@PathVariable Long userId) {
        List<PublishResponse> inactivePublishes = publishService.getInactivePublishes(userId);
        return ResponseEntity.ok(inactivePublishes);
    }

    //Обновляет информацию о публикации
    @Operation(summary = "Edits publication information")
    @ApiResponse(responseCode = "201", description = "successfully edited information about the publication")
    @PutMapping("/update/{userId}/{publishId}")
    public ResponseEntity<PublishResponse> updatePublish(@PathVariable Long userId, @PathVariable Long publishId, @Valid @RequestBody PublishRequest publishRequest) throws UnauthorizedException, NotFoundException {
        PublishResponse updatedPublish = publishService.updatePublish(userId, publishId, publishRequest);
        return ResponseEntity.ok(updatedPublish);
    }

    //Возвращает список отклоненных публикаций пользователя
    @Operation(summary = "Returns a list of user's rejected posts")
    @ApiResponse(responseCode = "201", description = "The list of rejected user publications was successfully restored")
    @GetMapping("/rejected-publishes/{userId}")
    public ResponseEntity<List<PublishResponse>> getRejectedPublishes(@PathVariable Long userId) {
        List<PublishResponse> rejectedPublishes = publishService.getRejectedPublishes(userId);
        return ResponseEntity.ok(rejectedPublishes);
    }


    @Operation(summary = "returns to the client the number of favorites for a specific publication")
    @ApiResponse(responseCode = "201", description = "The number of favorites for a specific publication was successfully returned to the client")
    @GetMapping("/count")
    public ResponseEntity<Integer> getFavoritesCount(
            @RequestParam Long userId,
            @RequestParam Long publishId) {
        try {
            Integer count = publishService.getFavoritesCount(userId, publishId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (NotFoundException | UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}