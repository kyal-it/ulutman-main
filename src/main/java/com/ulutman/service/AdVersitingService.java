package com.ulutman.service;

import com.ulutman.model.entities.AdVersiting;
import com.ulutman.repository.AdVersitingRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdVersitingService {

    private final AdVersitingRepository adVersitingRepository;

    String baseDirectoryPath = System.getProperty("user.dir") + File.separator + "ads";


    public void createAdvertising(MultipartFile imageFile) throws IOException, java.io.IOException {
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Файл изображения не может быть пустым.");
        }

        BufferedImage img = ImageIO.read(imageFile.getInputStream());
        int width = img.getWidth();
        int height = img.getHeight();

        if ((width == 285 && height == 407) || (width == 564 && height == 246)) {
            saveAdvertising(imageFile);
            System.out.println("Реклама создана с изображением: " + imageFile.getOriginalFilename());
        } else {
            throw new IllegalArgumentException("Размер изображения должен быть 285x407 или 564x246.");
        }
    }

    public void saveAdvertising(MultipartFile imageFile) throws IOException, java.io.IOException {
        String userDirectoryPath = baseDirectoryPath + File.separator ;
        File userDirectory = new File(userDirectoryPath);

        if (!userDirectory.exists()) {
            boolean created = userDirectory.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directory: " + userDirectoryPath);
            }
        }

        File savedFile = new File(userDirectory, imageFile.getOriginalFilename());
        imageFile.transferTo(savedFile);

        AdVersiting ad = new AdVersiting();
        ad.setImagePath(savedFile.getAbsolutePath());

        adVersitingRepository.save(ad);
    }

    public List<AdVersiting> getAllAds() {
        return adVersitingRepository.findAll();
    }

    public boolean deleteAd(Long id) {
        Optional<AdVersiting> ad = adVersitingRepository.findById(id);
        if (ad.isPresent()) {
            adVersitingRepository.delete(ad.get());
            return true;
        }
        return false;
    }
}
