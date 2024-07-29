package com.ulutman.controller;

import com.ulutman.model.dto.MessageRequest;
import com.ulutman.model.dto.MessageResponse;
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

    @PutMapping("/{messageId}/status")
    public ResponseEntity<MessageResponse> updateMessageStatus(@PathVariable Long messageId, @RequestBody MessageRequest messageRequest) {
        MessageResponse updatedMessage = messageService.updateMessageStatus(messageId, messageRequest);
        return ResponseEntity.ok(updatedMessage);
    }
}
