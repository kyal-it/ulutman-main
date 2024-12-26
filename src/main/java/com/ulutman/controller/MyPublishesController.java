package com.ulutman.controller;

import com.ulutman.exception.NotFoundException;
import com.ulutman.exception.UnauthorizedException;
import com.ulutman.model.dto.AdVersitingResponse;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.AdVersiting;
import com.ulutman.model.entities.User;
import com.ulutman.service.AdVersitingService;
import com.ulutman.service.MyPublishesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/my-publishes")
@Tag(name = "my-publishes")
public class MyPublishesController {
    private final MyPublishesService publishService;
    private  final AdVersitingService adVersitingService;
    private final MyPublishesService myPublishesService;

    //Возвращает список активных публикаций пользователя
    @Operation(summary = "Get all active publications for a user")
    @ApiResponse(responseCode = "201", description = "successfully returned a list of active user publications")
    @GetMapping("/{userId}")
    public ResponseEntity<List<PublishResponse>> myPublishes(@PathVariable Long userId) {
        List<PublishResponse> publishes = publishService.myActivePublishes(userId);
        return ResponseEntity.ok(publishes);
    }

    //Возвращает список активных публикаций пользователя
    @Operation(summary = "Get all active adversting for a user")
    @ApiResponse(responseCode = "201", description = "successfully returned a list of active user publications")
    @GetMapping("/ads/{userId}")
    public ResponseEntity<List<AdVersitingResponse>> myAdverstings(@PathVariable Long userId) {
        List<AdVersitingResponse> publishes = publishService.myActiveAdVerstings(userId);
        return ResponseEntity.ok(publishes);
    }

    //Возвращает список активных рекламы пользователя
    @Operation(summary = "Returns a list of the user's active advertisements")
    @ApiResponse(responseCode = "201", description = "successfully returns a list of the user's active advertisements")
    @GetMapping("/my-ads")
    public List<AdVersiting> getMyAds(@AuthenticationPrincipal User user) {
        Long userId = user.getId();
        return adVersitingService.getAllActiveAdsForUser(userId);
    }

    //Удаляет рекламы по id пользователя
    @Operation(summary = "Removes advertisements by user ID")
    @ApiResponse(responseCode = "201", description = "successfully removes advertisements by user ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAd(@PathVariable Long id, @AuthenticationPrincipal User userDetails) {
        Long userId = userDetails.getId();
        boolean deleted = adVersitingService.deleteAd(id, userId);
        if (deleted) {
            return ResponseEntity.ok("Объявление успешно удалено");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Вы не можете удалить это объявление");
        }
    }
//
////    //Деактивирует публикацию
////    @Operation(summary = "Deactivates publication")
////    @ApiResponse(responseCode = "201", description = "publication successfully deactivated")
////    @PutMapping("/deactivate/{userId}/{publishId}")
////    public ResponseEntity<PublishResponse> deactivatePublish(@PathVariable Long userId, @PathVariable Long publishId) throws UnauthorizedException {
////        PublishResponse deactivatedPublish = publishService.deactivatePublish(userId, publishId);
////        return ResponseEntity.ok(deactivatedPublish);
////    }
//
////    //Активирует публикацию
////    @Operation(summary = "Activates publication")
////    @ApiResponse(responseCode = "201", description = "publication successfully activated")
////    @PutMapping("/activate/{userId}/{publishId}")
////    public ResponseEntity<PublishResponse> activatePublish(@PathVariable Long userId, @PathVariable Long publishId) throws UnauthorizedException {
////        PublishResponse activatedPublish = publishService.activatePublish(userId, publishId);
////        return ResponseEntity.ok(activatedPublish);
////    }

    //Удаляет публикации пользователя
    @Operation(summary = "Deletes a user's publishes")
    @ApiResponse(responseCode = "201", description = "Posts successfully deleted")
    @DeleteMapping("/delete-by-user/{userId}")
    public ResponseEntity<String> deletePublishesByUser(
            @PathVariable Long userId,
            @RequestBody Set<Long> publishIds) {
        try {
            publishService.deletePublishesByUser(userId, publishIds);
            return ResponseEntity.ok("Публикации успешно удалены");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка при удалении публикаций: " + e.getMessage());
        }
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
//
////    // Поднять свои публикации на первый ряд в методе getAll
////    @Operation(summary = "Raise your publications to the front row")
////    @ApiResponse(responseCode = "201", description = "All your publications have successfully risen to the first row")
////    @GetMapping("/raising-the-publication")
////    public ResponseEntity<List<PublishResponse>> getMyActivePublishes(@RequestParam Long userId) {
////        List<PublishResponse> myActivePublishes =publishService.myActivePublications(userId);
////        return ResponseEntity.ok(myActivePublishes);
////    }
//   // Поднять свои рекламы на первый ряд в методе getAllActiveAds
////    @Operation(summary = "Raise your publications to the front row")
////    @ApiResponse(responseCode = "201", description = "All your publications have successfully risen to the first row")
////    @GetMapping("/raising-the-adVersting")
////    public ResponseEntity<List<AdVersiting>> getAllActiveAds() {
////        List<AdVersiting> activeAds = myPublishesService.getAllActiveAds();
////        return ResponseEntity.ok(activeAds);
////    }
//
    @PutMapping("/boost/{publishId}")
    public ResponseEntity<PublishResponse> boostPublication(@PathVariable Long publishId, @RequestParam Long userId) {
        try {
            PublishResponse response = myPublishesService.boostPublication(userId, publishId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); //или более специфичный ответ об ошибке
        }
    }

    @PutMapping("/boostAdversting/{adId}")
    public ResponseEntity<AdVersitingResponse> boostAdversting(@PathVariable Long adId, @RequestParam Long userId) {
        try {
            AdVersitingResponse response = myPublishesService.boostAdversting(userId, adId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); //или более специфичный ответ об ошибке
        }
    }
}