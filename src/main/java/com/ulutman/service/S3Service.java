//package com.ulutman.service;
//
//import io.jsonwebtoken.io.IOException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.S3Exception;
//
//import java.nio.file.Path;
//
//@Service
//public class S3Service {
//
//    private final S3Client s3Client;
//
//    @Value("${aws.s3.bucket-name}")
//    private String bucketName;
//
//    @Value("${aws.s3.region}")
//    private String awsRegion;
//
//    public S3Service(@Value("${aws.s3.access-key-id}") String accessKeyId,
//                     @Value("${aws.s3.secret-access-key}") String secretAccessKey,
//                     @Value("${aws.s3.region}") String region) {
//
//        // Настройка AWS Credentials и инициализация клиента S3
//        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
//        s3Client = S3Client.builder()
//                .region(Region.of(region))
//                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
//                .build();
//    }
//
//    public String uploadFile(String fileName, Path filePath) {
//        try {
//
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(fileName)
//                    .build();
//
//            s3Client.putObject(putObjectRequest, filePath);
//
//            return getFileUrl(fileName);
//        } catch (S3Exception | IOException e) {
//            throw new RuntimeException("Ошибка при загрузке файла: " + e.getMessage());
//        }
//    }
//
//    public String getFileUrl(String fileName) {
//        return "https://" + bucketName + ".s3." + awsRegion + ".amazonaws.com/" + fileName;
//    }
//}
