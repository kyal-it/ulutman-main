package com.ulutman.controller;

import com.ulutman.model.dto.*;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.service.CommentService;
import com.ulutman.service.ManageModeratorService;
import com.ulutman.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/moderator")
public class ManageModeratorController {

    private final ManageModeratorService manageModeratorService;
    private final CommentService commentService;
    private final MessageService messageService;

    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<List<ModeratorCommentResponse>> getUserComments(@PathVariable Long userId) {
        List<ModeratorCommentResponse> responses = manageModeratorService.getUserComments(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/users/{userId}/messages")
    public ResponseEntity<List<ModeratorMessageResponse>> getUserMessages(@PathVariable Long userId) {
        List<ModeratorMessageResponse> responses = manageModeratorService.getUserMessages(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/users/{userId}/details")
    public ResponseEntity<UserCommentsMessagesResponse> getUserCommentsAndMessages(@PathVariable Long userId) {
        UserCommentsMessagesResponse response = manageModeratorService.getUserCommentsAndMessages(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{commentId}/status")
    public ResponseEntity<CommentResponse> updateCommentStatus(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentResponse updatedComment = commentService.updateCommentStatus(commentId, commentRequest);
        return ResponseEntity.ok(updatedComment);
    }

    @PutMapping("/{messageId}/status")
    public ResponseEntity<MessageResponse> updateMessageStatus(@PathVariable Long messageId, @RequestBody MessageRequest messageRequest) {
        MessageResponse updatedMessage = messageService.updateMessageStatus(messageId, messageRequest);
        return ResponseEntity.ok(updatedMessage);
    }
}
