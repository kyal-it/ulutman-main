package com.ulutman.controller;

import com.ulutman.model.entities.AdVersiting;
import com.ulutman.service.AdVersitingService;
import io.jsonwebtoken.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/advertising")
public class AdvertisingController {

    private final AdVersitingService adVersitingService;

    public AdvertisingController(AdVersitingService adVersitingService) {
        this.adVersitingService = adVersitingService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAdvertising(@RequestParam("imageFile") MultipartFile imageFile) {
        try {
           adVersitingService.createAdvertising(imageFile);
            return ResponseEntity.ok("Реклама успешно создана.");
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<AdVersiting>> getAllAds() {
        List<AdVersiting> ads = adVersitingService.getAllAds();
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAd(@PathVariable Long id) {
        boolean isDeleted = adVersitingService.deleteAd(id);

        if (isDeleted) {
            return ResponseEntity.ok("Реклама успешно удалена.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Реклама не найдена.");
        }
    }
}
