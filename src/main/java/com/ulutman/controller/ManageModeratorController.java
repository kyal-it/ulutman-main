package com.ulutman.controller;

import com.ulutman.model.dto.*;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.service.ManageModeratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/moderator")
@Tag(name = "Manage Moderator")
@SecurityRequirement(name = "Authorization")
public class ManageModeratorController {

    private final ManageModeratorService manageModeratorService;

    @Operation(summary = "Get comments of user")
    @ApiResponse(responseCode = "201", description = "Return list comments of user")
    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<List<ModeratorCommentResponse>> getUserComments(@PathVariable Long userId) {
        List<ModeratorCommentResponse> responses = manageModeratorService.getUserComments(userId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get all user comments")
    @ApiResponse(responseCode = "200", description = "Retrieved all user comments successfully")
    @GetMapping("/all")
    public ResponseEntity<List<UserCommentsResponse>> getAllUserComments() {
        List<UserCommentsResponse> userComments = manageModeratorService.getAllUserComments();
        return ResponseEntity.ok(userComments);
    }

    @Operation(summary = "Update comment status")
    @ApiResponse(responseCode = "201", description = "Updated comment status successfully")
    @PutMapping("/{commentId}/status")
    public ResponseEntity<CommentResponse> updateCommentStatus(@PathVariable("commentId") Long commentId,
                                                               @RequestParam("newStatus") ModeratorStatus newStatus) {
        CommentResponse commentResponse = manageModeratorService.updateCommentStatus(commentId, newStatus);
        return ResponseEntity.ok(commentResponse);
    }

    @Operation(summary = "Filter by name")
    @ApiResponse(responseCode = "201", description = "Users  by name successfully filtered")
    @GetMapping("/name/filter")
    public List<AuthResponse> filterUsers(@RequestParam(required = false) String name) {
        return manageModeratorService.filterUsersByName(name);
    }

    @Operation(summary = "Filter mailings by title")
    @ApiResponse(responseCode = "201", description = "Mailings by title successfully filtered")
    @GetMapping("content/filter")
    public ResponseEntity<List<CommentResponse>> filterMailingByContent(@RequestParam String content) {
        try {
            List<CommentResponse> comments = manageModeratorService.filterCommentsByContent(content);
            return new ResponseEntity<>(comments, HttpStatus.OK); // Возвращаем список с кодом 200
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Если ошибка, возвращаем 400
        }
    }

    @Operation(summary = "Filter comments")
    @ApiResponse(responseCode = "201", description = "Comments successfully filtered")
    @GetMapping("/filter")
    public List<CommentResponse> filterComments(
            @RequestParam(value = "createDate", required = false) List<LocalDate> createDates,
            @RequestParam(value = "moderatorStatuses", required = false) List<ModeratorStatus> moderatorStatuses) {
        return manageModeratorService.getCommentsByFilters(createDates, moderatorStatuses);
    }
}
