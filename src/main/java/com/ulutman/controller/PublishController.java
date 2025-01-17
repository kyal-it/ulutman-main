package com.ulutman.controller;

import ch.qos.logback.classic.Logger;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.enums.*;
import com.ulutman.repository.BankCardRepository;
import com.ulutman.service.PublishService;
import com.ulutman.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    private final S3Service s3Service;

    @Operation(summary = "Create a publication")
    @ApiResponse(responseCode = "201", description = "The publish created successfully")
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<PublishResponse> createPublish(
            @Parameter(description = "The title of the publication")
            @RequestParam("title") String title,

            @Parameter(description = "The description of the publication")
            @RequestParam("description") String description,

            @Parameter(description = "The metro area of the publication")
            @RequestParam("metro") Metro metro,

            @Parameter(description = "The address of the publication")
            @RequestParam("address") String address,

            @Parameter(description = "The phone number associated with the publication")
            @RequestParam("phoneNumber") String phoneNumber,

            @Parameter(description = "The list of images for the publication")
            @RequestPart("images") List<MultipartFile> images, // Используем @RequestPart для файлов

            @Parameter(description = "The price of the publication")
            @RequestParam("price") double price,

            @Parameter(description = "The category of the publication")
            @RequestParam("category") Category category,

            @Parameter(description = "The subcategory of the publication")
            @RequestParam("subcategory") Subcategory subcategory,

            @Parameter(description = "The bank associated with the publication (optional)")
            @RequestParam(value = "bank", required = false) String bank, // Опционально

            @Parameter(description = "The payment receipt file (optional)")
            @RequestPart(value = "paymentReceiptFile", required = false) MultipartFile paymentReceiptFile, // Используем @RequestPart для файла

            @Parameter(description = "The user ID associated with the publication")
            @RequestParam("userId") Long userId) {

        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setTitle(title);
        publishRequest.setDescription(description);
        publishRequest.setMetro(metro);
        publishRequest.setAddress(address);
        publishRequest.setPhoneNumber(phoneNumber);

        String tempDir = System.getProperty("java.io.tmpdir");

        try {
            // Обработка изображений
            Map<String, Path> filesMap = new HashMap<>();
            for (MultipartFile file : images) {
                Path tempFile = Paths.get(tempDir, file.getOriginalFilename());
                Files.write(tempFile, file.getBytes());
                filesMap.put(file.getOriginalFilename(), tempFile);
            }

            List<String> imageUrls = s3Service.uploadFiles(filesMap);
            publishRequest.setImages(imageUrls);

            for (Path tempFile : filesMap.values()) {
                Files.deleteIfExists(tempFile);
            }

            // Обработка файла с квитанцией (если есть)
            if (paymentReceiptFile != null && !paymentReceiptFile.isEmpty()) {
                Path tempFile = Paths.get(tempDir, paymentReceiptFile.getOriginalFilename());
                Files.write(tempFile, paymentReceiptFile.getBytes());
                Map<String, Path> receiptMap = Map.of(paymentReceiptFile.getOriginalFilename(), tempFile);
                List<String> receiptUrls = s3Service.uploadFiles(receiptMap);
                publishRequest.setPaymentReceiptFile(Optional.of(new File(receiptUrls.get(0))));
                Files.deleteIfExists(tempFile);
            } else {
                publishRequest.setPaymentReceiptFile(Optional.empty());
            }
        } catch (Exception e) {
            logger.error("Ошибка загрузки файлов в S3: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        publishRequest.setPrice(price);
        publishRequest.setCategory(category);
        publishRequest.setSubcategory(subcategory);
        publishRequest.setBank(Optional.ofNullable(bank));
        publishRequest.setUserId(userId);

        try {
            PublishResponse response = publishService.createPublish(publishRequest, images.get(0)); // Используем первый файл изображения
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка в аргументах: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            logger.error("Ошибка выполнения: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


//        List<String> imagePaths = new ArrayList<>();
//        for (MultipartFile file : images) {
//            String path = saveFile(file); // Правильный вызов метода
//            imagePaths.add(path);
//        }
//        publishRequest.setImages(imagePaths);
//        publishRequest.setPrice(price);
//        publishRequest.setCategory(category);
//        publishRequest.setSubcategory(subcategory);
//        publishRequest.setBank(Optional.ofNullable(bank));
//        publishRequest.setUserId(userId);
//        if (paymentReceiptFile != null && !paymentReceiptFile.isEmpty()) {
//            File tempFile = null;
//            try {
//                tempFile = File.createTempFile("paymentReceipt", ".tmp");
//                paymentReceiptFile.transferTo(tempFile); // Сохраняем файл во временное место
//
//                publishRequest.setPaymentReceiptFile(Optional.of(tempFile));
//            } catch (IOException e) {
//                logger.error("Ошибка при обработке файла: {}", e.getMessage());
//                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//            } finally {
//                if (tempFile != null && tempFile.exists()) {
//                    tempFile.deleteOnExit();
//                }
//            }
//        } else {
//            publishRequest.setPaymentReceiptFile(Optional.empty());
//        }
//
//        try {
//            PublishResponse response = publishService.createPublish(publishRequest);
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            logger.error("Ошибка в аргументах: {}", e.getMessage());
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        } catch (RuntimeException e) {
//            logger.error("Ошибка выполнения: {}", e.getMessage());
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//
//    }

    private String saveFile(MultipartFile file) {
        String directoryPath = "";

        // Создайте директорию, если она не существует
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Создает папки, если их нет
        }

        // Генерируйте уникальное имя файла
        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Полный путь к сохранению файла
        String fullPath = directoryPath + uniqueFileName;

        try {
            // Сохраните файл
            file.transferTo(new File(fullPath));
            return fullPath; // Верните полный путь для дальнейшего использования
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла: " + e.getMessage());
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