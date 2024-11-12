package com.ulutman.controller;

import ch.qos.logback.classic.Logger;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.BankCard;
import com.ulutman.model.enums.*;
import com.ulutman.repository.BankCardRepository;
import com.ulutman.service.PublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/publishes")
@Tag(name = "Publish")
@SecurityRequirement(name = "Authorization")
public class PublishController {
    private final PublishService publishService;
    private final BankCardRepository bankCardRepository;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(PublishController.class);


    @Operation(summary = "Create a publication")
    @ApiResponse(responseCode = "201", description = "The publish created successfully")
    @PostMapping("/create")
    public ResponseEntity<PublishResponse> createPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("metro") Metro metro,
            @RequestParam("address") String address,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("images") List<String> images,
            @RequestParam("price") double price,
            @RequestParam("category") Category category,
            @RequestParam("subcategory") Subcategory subcategory,
            @RequestParam(value = "bank", required = false) String bank, // Опционально
            @RequestParam(value = "paymentReceiptFile", required = false) MultipartFile paymentReceiptFile, // Файл
            @RequestParam("userId") Long userId) {

        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setTitle(title);
        publishRequest.setDescription(description);
        publishRequest.setMetro(metro);
        publishRequest.setAddress(address);
        publishRequest.setPhoneNumber(phoneNumber);
        publishRequest.setImages(images);
        publishRequest.setPrice(price);
        publishRequest.setCategory(category);
        publishRequest.setSubcategory(subcategory);
        publishRequest.setBank(Optional.ofNullable(bank));
        publishRequest.setUserId(userId);


        if (paymentReceiptFile != null && !paymentReceiptFile.isEmpty()) {
            File tempFile = null;
            try {
                tempFile = File.createTempFile("paymentReceipt", ".tmp");
                paymentReceiptFile.transferTo(tempFile); // Сохраняем файл во временное место

                publishRequest.setPaymentReceiptFile(Optional.of(tempFile));
            } catch (IOException e) {
                logger.error("Ошибка при обработке файла: {}", e.getMessage());
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.deleteOnExit();
                }
            }
        } else {
            publishRequest.setPaymentReceiptFile(Optional.empty());
        }

        try {
            PublishResponse response = publishService.createPublish(publishRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка в аргументах: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            logger.error("Ошибка выполнения: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "Create a publication with details")
    @ApiResponse(responseCode = "201", description = "The publish with details  created successfully")
    @PostMapping("/createDetails")
    public ResponseEntity<PublishResponse> createPublicationDetails(@RequestBody PublishRequest publishRequest) {
        try {
            PublishResponse publishResponse = publishService.createPublishDetails(publishRequest);
            return new ResponseEntity<>(publishResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при создании публикации: ", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

//    @Operation(summary = "Get all publications")
//    @ApiResponse(responseCode = "201", description = "Return list of publishes")
//    @GetMapping("/getAll")
//    public ResponseEntity<List<PublishResponse>> getAllPublishes(Principal principal) {
//        // Вызов сервиса для получения всех публикаций с учётом избранных для текущего пользователя
//        List<PublishResponse> publishes = publishService.getAll(principal);
//
//        // Возвращаем результат в виде HTTP-ответа
//        return new ResponseEntity<>(publishes, HttpStatus.OK);
//    }
//    public ResponseEntity<List<PublishResponse>> getAllPublishes() {
//        List<PublishResponse> publishes = publishService.getAll();
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(publishes);
//    }
    @GetMapping("/getAll")
    public ResponseEntity<List<PublishResponse>> getAllPublishes() {
        List<PublishResponse> publishes = publishService.getAll();
        return ResponseEntity.ok(publishes);
    }

    @Operation(summary = "Get a publication by id")
    @ApiResponse(responseCode = "201", description = "Publication found")
    @GetMapping("find/{id}")
    public PublishResponse findById(@PathVariable Long id) {
        return this.publishService.findById(id);
    }

    @Operation(summary = "Update a publication by id")
    @ApiResponse(responseCode = "201", description = "Updated  the publication  by id successfully")
    @PutMapping("/update/{id}")
    public ResponseEntity<PublishResponse> updatePublish(@PathVariable Long id, @RequestBody PublishRequest publishRequest) {
        PublishResponse updatedPublish = publishService.updatePublish(id, publishRequest);
        return ResponseEntity.ok(updatedPublish);
    }

    @Operation(summary = "Delete a publication by id")
    @ApiResponse(responseCode = "201", description = "Deleted the publication  by id successfully")
    @DeleteMapping(("/delete/{id}"))
    public String delete(@PathVariable("id") Long id) {
        this.publishService.deletePublish(id);
        return "Delete publish with id:" + id + " successfully delete";
    }

    @Operation(summary = "Filter publishes by criteria")
    @ApiResponse(responseCode = "201", description = "Publishes successfully filtered")
    @GetMapping("/filter")
    public List<PublishResponse> filterPublishes(
            @RequestParam(required = false) Double minTotalArea,
            @RequestParam(required = false) Double maxTotalArea,
            @RequestParam(required = false) Double minKitchenArea,
            @RequestParam(required = false) Double maxKitchenArea,
            @RequestParam(required = false) Double minLivingArea,
            @RequestParam(required = false) Double maxLivingArea,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) TransportType transportType,
            @RequestParam(required = false) Double walkingDistance,
            @RequestParam(required = false) Double transportDistance
    ) {
        return publishService.filterPublishes(
                minTotalArea, maxTotalArea, minKitchenArea, maxKitchenArea,
                minLivingArea, maxLivingArea, minYear, maxYear,
                transportType, walkingDistance, transportDistance
        );
    }

    @Operation(summary = "Reset filters publications")
    @ApiResponse(responseCode = "201", description = "Publishes filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter() {
        return publishService.getAll();
    }
}