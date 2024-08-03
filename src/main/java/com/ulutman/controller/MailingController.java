package com.ulutman.controller;

import com.ulutman.model.dto.MailingRequest;
import com.ulutman.model.dto.MailingResponse;
import com.ulutman.service.MailingService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mailing")
@Slf4j
public class MailingController {

    private final MailingService mailingService;

    @PostMapping("/create")
    public ResponseEntity<MailingResponse> mail(@RequestBody MailingRequest request) {
        log.info("Mailing successfully created");
        MailingResponse response = mailingService.createMailing(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<Void> sendMailing(@PathVariable Long id, @RequestParam String recipientEmail) {
        try {
            mailingService.sendMailing(id, recipientEmail);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MessagingException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
