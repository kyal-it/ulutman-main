package com.ulutman.controller;

import com.ulutman.model.entities.AdVersiting;
import com.ulutman.service.AdVersitingService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/advertising")
@Tag(name = "AdVersting")
@SecurityRequirement(name = "Authorization")
public class AdvertisingController {

    private final AdVersitingService adVersitingService;

    public AdvertisingController(AdVersitingService adVersitingService) {
        this.adVersitingService = adVersitingService;
    }

    @PostMapping
    public ResponseEntity<String> createAdvertising(@RequestParam("imageFile") MultipartFile imageFile,
                                                    @RequestParam("bank") String bank,
                                                    @RequestParam("paymentReceiptFile") MultipartFile paymentReceiptFile,
                                                    Principal principal) {
        System.out.println("Received bank: " + bank);
        System.out.println("Received imageFile: " + (imageFile != null ? imageFile.getOriginalFilename() : "null"));
        System.out.println("Received paymentReceiptFile: " + (paymentReceiptFile != null ? paymentReceiptFile.getOriginalFilename() : "null"));
        try {
            adVersitingService.createAdvertising(imageFile, bank, paymentReceiptFile, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body("Реклама успешно создана");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файлов: " + e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при отправке уведомления: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Неизвестная ошибка: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AdVersiting>> getAllAds() {
        List<AdVersiting> ads = adVersitingService.getAllActiveAds();
        return ResponseEntity.ok(ads);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        boolean deleted =adVersitingService.deleteAd(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
