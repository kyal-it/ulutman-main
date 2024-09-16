package com.ulutman.controller;

import com.ulutman.model.dto.*;
import com.ulutman.model.entities.User;
import com.ulutman.service.CommentService;
import com.ulutman.service.ManageModeratorService;
import com.ulutman.service.MessageService;
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
    private final CommentService commentService;
    private final MessageService messageService;

    @Operation(summary = "Get comments of user")
    @ApiResponse(responseCode = "201", description = "Return list comments of user")
    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<List<ModeratorCommentResponse>> getUserComments(@PathVariable Long userId) {
        List<ModeratorCommentResponse> responses = manageModeratorService.getUserComments(userId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get messages of user")
    @ApiResponse(responseCode = "201", description = "Return list messages of user")
    @GetMapping("/users/{userId}/messages")
    public ResponseEntity<List<ModeratorMessageResponse>> getUserMessages(@PathVariable Long userId) {
        List<ModeratorMessageResponse> responses = manageModeratorService.getUserMessages(userId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get users comments and messages ")
    @ApiResponse(responseCode = "201", description = "Return list  comments and messages of user")
    @GetMapping("/users/{userId}/details")
    public ResponseEntity<UserCommentsMessagesResponse> getUserCommentsAndMessages(@PathVariable Long userId) {
        UserCommentsMessagesResponse response = manageModeratorService.getUserCommentsAndMessages(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all users comments and messages")
    @ApiResponse(responseCode = "201", description = "Return list  users comments and messages")
    @GetMapping("/users/comments-messages")
    public ResponseEntity<List<UserCommentsMessagesResponse>> getAllUsersCommentsAndMessages() {
        try {
            List<UserCommentsMessagesResponse> responses = manageModeratorService.getAllUserCommentsAndMessages();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Update comment status")
    @ApiResponse(responseCode = "201", description = "Updated comment status successfully")
    @PutMapping("/{commentId}/status")
    public ResponseEntity<CommentResponse> updateCommentStatus(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentResponse updatedComment = commentService.updateCommentStatus(commentId, commentRequest);
        return ResponseEntity.ok(updatedComment);
    }

    @Operation(summary = "Update message status")
    @ApiResponse(responseCode = "201", description = "Updated message status successfully")
    @PutMapping("/{messageId}/status")
    public ResponseEntity<MessageResponse> updateMessageStatus(@PathVariable Long messageId, @RequestBody MessageRequest messageRequest) {
        MessageResponse updatedMessage = messageService.updateMessageStatus(messageId, messageRequest);
        return ResponseEntity.ok(updatedMessage);
    }

    @Operation(summary = "Filter comments")
    @ApiResponse(responseCode = "201", description = "Comments successfully filtered")
    @GetMapping("/comments/filter")
    public List<CommentResponse> filterComments(@RequestParam(value = "user", required = false) List<User> users,
                                                @RequestParam(value = "content", required = false) List<String> content,
                                                @RequestParam(value = "createDate", required = false) List<LocalDate> createDates,
                                                @RequestParam(value = "moderatorStatuses", required = false) List<String> moderatorStatuses) {
        return manageModeratorService.getCommentsByFilters(users, content, createDates, moderatorStatuses);
    }

    @Operation(summary = "Filter messages")
    @ApiResponse(responseCode = "201", description = "Messages successfully filtered")
    @GetMapping("/message/filter")
    public List<MessageResponse> filterMessages(@RequestParam(value = "user", required = false) List<User> users,
                                                @RequestParam(value = "content", required = false) List<String> content,
                                                @RequestParam(value = "createDate", required = false) List<LocalDate> createDates,
                                                @RequestParam(value = "moderatorStatuses", required = false) List<String> moderatorStatuses) {
        return manageModeratorService.getMessagesByFilters(users, content, createDates, moderatorStatuses);
    }
}
