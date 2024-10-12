package com.ulutman.controller;

import com.ulutman.service.S3Service;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/S3")
@Tag(name = "Amazon S3")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class S3Controller {

    private S3Service s3Service;

    @Autowired
    public void FileUploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Operation(summary = "Загрузить файл в AWS")
    @ApiResponse(responseCode = "201", description = "Uploads the file to AWS and returns the url of the downloaded file")
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, Path> filesToUpload = new HashMap<>();

        for (MultipartFile file : files) {
            try {
                // Сохраняем файл на временную директорию
                Path tempFilePath = Files.createTempFile(file.getOriginalFilename(), null);
                file.transferTo(tempFilePath.toFile()); // Перемещаем файл во временное место
                filesToUpload.put(file.getOriginalFilename(), tempFilePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(List.of("Ошибка при сохранении файла: " + e.getMessage()));
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> fileUrls = s3Service.uploadFiles(filesToUpload);

        return ResponseEntity.ok(fileUrls);
    }
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
//        String fileName = file.getOriginalFilename();
//        if (fileName == null || file.isEmpty()) {
//            return ResponseEntity.badRequest().body("Файл не найден");
//        }
//
//        try {
//            Path filePath = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
//            Files.write(filePath, file.getBytes());
//
//            // Загружаем файл в S3 и получаем URL
//            String fileUrl = s3Service.uploadFile(fileName, filePath);
//            return ResponseEntity.ok(fileUrl);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Ошибка при загрузке файла: " + e.getMessage());
//        } catch (java.io.IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
