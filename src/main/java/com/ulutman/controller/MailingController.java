package com.ulutman.controller;

import com.ulutman.exception.PasswordsDoNotMatchException;
import com.ulutman.model.dto.MailingRequest;
import com.ulutman.model.dto.MailingResponse;
import com.ulutman.service.MailingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mailing")
@Slf4j
@Tag(name = "Mailing")
@SecurityRequirement(name = "Authorization")
public class MailingController {

    private final MailingService mailingService;

    @Operation(summary = "Create mailing")
    @ApiResponse(responseCode = "201", description = "Mailing created successfully")
    @PostMapping("/create")
    public ResponseEntity<MailingResponse> mail(@RequestBody MailingRequest request) {
        log.info("Mailing successfully created");
        MailingResponse response = mailingService.createMailing(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Send mailing")
    @ApiResponse(responseCode = "201", description = "Mailing sent  successfully")
    @PostMapping("/{id}/send")
    public ResponseEntity<Void> sendMailing(@PathVariable Long id, @RequestParam String recipientEmail) {
        try {
            mailingService.sendMailing(id, recipientEmail);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MessagingException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sendPasswordResetCode")
    public void sendPasswordResetCode(@RequestParam String email) throws EntityNotFoundException {
        mailingService.sendPasswordResetCode(email);
    }

    @PostMapping("/resetPassword")
    public String resetPassword( @RequestParam String email, @RequestParam int pinCode,
                                 @RequestParam String newPassword, @RequestParam String confirmPassword)
            throws EntityNotFoundException, PasswordsDoNotMatchException {
        return mailingService.resetPassword(email, pinCode, newPassword, confirmPassword);
    }
}
