package com.ulutman.controller;

import com.ulutman.model.dto.CommentRequest;
import com.ulutman.model.dto.CommentResponse;
import com.ulutman.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest request) {
        CommentResponse response = commentService.addComment(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{commentId}/status")
    public ResponseEntity<CommentResponse> updateCommentStatus(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentResponse updatedComment = commentService.updateCommentStatus(commentId, commentRequest);
        return ResponseEntity.ok(updatedComment);
    }
}
