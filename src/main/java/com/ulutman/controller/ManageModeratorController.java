package com.ulutman.controller;

import com.ulutman.model.dto.*;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.service.ManageModeratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/manage/moderator")
@Tag(name = "Manage Moderator")
@SecurityRequirement(name = "Authorization")
public class ManageModeratorController {

    private final ManageModeratorService manageModeratorService;

    @Operation(summary = "Get comments of user")
    @ApiResponse(responseCode = "201", description = "Return list comments of user")
    @GetMapping("/comments/{userId}")
    public ResponseEntity<UserCommentsResponse> getUserWithComments(@PathVariable Long userId) {
        UserCommentsResponse userWithComments = manageModeratorService.getUserWithComments(userId);
        return ResponseEntity.ok(userWithComments);
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

    @Operation(summary = "Filter comments by criteria")
    @ApiResponse(responseCode = "201", description = "Comments successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<FilteredCommentResponse>> filterComments(
            @RequestParam(required = false) List<LocalDate> createDates,
            @RequestParam(required = false) List<ModeratorStatus> moderatorStatuses,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String names) {

        List<FilteredCommentResponse> comments = manageModeratorService.filterComments(createDates, moderatorStatuses, content, names);

        return ResponseEntity.ok(comments);
    }

//    @Operation(summary = "Delete comment by Id")
//    @ApiResponse(responseCode = "201", description = "Comments successfully deleted")
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
//        manageModeratorService.deleteComment(id);
//        log.info("Комментарий с идентификатором " + id + " успешно удален");
//
//        return ResponseEntity.status(HttpStatus.NO_CONTENT)
//                .body("Комментарий с идентификатором " + id + " успешно удален");
//    }
}
