package com.ulutman.controller;

import com.ulutman.model.dto.*;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.service.ManageModeratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "Filter comments by content")
    @ApiResponse(responseCode = "201", description = "Comment by content successfully filtered")
    @GetMapping("/content/filter")
    public ResponseEntity<?> getPublishesByContent(@RequestParam("content") String content) {
        try {
            List<CommentResponse> commentResponses = manageModeratorService.filterPublishesByContent(content);
            return ResponseEntity.ok(commentResponses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
