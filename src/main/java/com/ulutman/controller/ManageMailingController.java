package com.ulutman.controller;

import com.ulutman.model.dto.MailingResponse;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.MailingStatus;
import com.ulutman.model.enums.MailingType;
import com.ulutman.service.ManageMailingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/mailing")
@Tag(name = "Manage Mailing")
@SecurityRequirement(name = "Authorization")
public class ManageMailingController {

    private final ManageMailingService manageMailingService;

    @PostMapping("/send-to-all")
    public ResponseEntity<String> sendMailingToAllUsers(@RequestParam("mailingId") Long mailingId) {
        manageMailingService.sendMailingToAllUsers(mailingId);
        return ResponseEntity.ok("Рассылка успешно отправлена всем пользователям.");
    }

    @GetMapping("/users-with-mailings")
    public List<User> getAllUsersWithMailings() {
        return manageMailingService.getAllUsersWithMailings();
    }

    @GetMapping("/all")
    public ResponseEntity<List<MailingResponse>> getAllMailings() {
        List<MailingResponse> mailings = manageMailingService.getAllMailings();
        return ResponseEntity.ok(mailings); // Возвращаем ответ с кодом 200 и списком рассылок
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<MailingResponse> updateMailingStatus(@PathVariable Long id,
                                                               @RequestParam MailingStatus newStatus) {
        MailingResponse response = manageMailingService.updateMailingStatus(id, newStatus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("title/filter") // Эндпоинт для фильтрации
    public ResponseEntity<List<MailingResponse>> filterMailingByTitle(@RequestParam String title) {
        try {
            List<MailingResponse> mailings = manageMailingService.filterMailingByTitle(title);
            return new ResponseEntity<>(mailings, HttpStatus.OK); // Возвращаем список с кодом 200
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Если ошибка, возвращаем 400
        }
    }

    @Operation(summary = "Filter mailings")
    @ApiResponse(responseCode = "201", description = "Mailings successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<MailingResponse>> filterMailings(
            @RequestParam(value = "mailingTypes", required = false) List<MailingType> mailingTypes,
            @RequestParam(value = "mailingStatuses", required = false) List<MailingStatus> mailingStatuses,
            @RequestParam(value = "createDates", required = false) List<LocalDate> createDates
    ) {
        List<MailingResponse> mailingResponses = manageMailingService.filterMailing(mailingTypes, mailingStatuses, createDates);
        return ResponseEntity.ok(mailingResponses);
    }
}
