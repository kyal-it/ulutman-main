package com.ulutman.controller;

import com.ulutman.model.dto.ModeratorCommentResponse;
import com.ulutman.model.dto.ModeratorMessageResponse;
import com.ulutman.model.dto.UserCommentsMessagesResponse;
import com.ulutman.service.ManageModeratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/moderator")
public class ManageModeratorController {

    private final ManageModeratorService manageModeratorService;

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
}
