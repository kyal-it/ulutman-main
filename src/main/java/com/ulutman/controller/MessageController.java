package com.ulutman.controller;

import com.ulutman.model.dto.MessageRequest;
import com.ulutman.model.dto.MessageResponse;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addMessage(@RequestBody MessageRequest messageRequest) {
     MessageResponse messageResponse = messageService.addMessage(messageRequest);
     return ResponseEntity.ok(messageResponse);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<MessageResponse> updateMessageStatus(@PathVariable Long id, @RequestParam ModeratorStatus moderatorStatus) {
        MessageResponse response = messageService.updateMessageStatus(id, moderatorStatus);
        return ResponseEntity.ok(response);
    }
}
