package com.ulutman.service;

import com.ulutman.model.entities.AdVersiting;
import com.ulutman.model.entities.User;
import com.ulutman.repository.AdVersitingRepository;
import com.ulutman.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import okhttp3.*;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@Service
public class AdVersitingService {

    private final AdVersitingRepository adVersitingRepository;
    private final UserRepository userRepository;
    private static final String TELEGRAM_BOT_TOKEN = "7967485487:AAGhVVsiOZ3V2ZFonfZqWXoxCpRpVL0D1nE";
    private static final String ADMIN_CHAT_ID = "1818193495";
    private final MailingService mailingService;

    String baseDirectoryPath = System.getProperty("user.dir") + File.separator + "ads";

    @Autowired
    public AdVersitingService(AdVersitingRepository adVersitingRepository, UserRepository userRepository, MailingService mailingService) throws IOException {
        this.adVersitingRepository = adVersitingRepository;
        this.userRepository = userRepository;
        this.mailingService = mailingService;
    }

    public void createAdvertising(MultipartFile imageFile, String bank, MultipartFile paymentReceiptFile, Principal principal) throws IOException, MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());

        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("Пользователь не найден.");
        }
        User user = userOptional.get();

        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Файл изображения не может быть пустым.");
        }

        BufferedImage img = ImageIO.read(imageFile.getInputStream());
        if (img == null) {
            throw new IllegalArgumentException("Не удалось прочитать изображение.");
        }

        int width = img.getWidth();
        int height = img.getHeight();

        if ((width == 285 && height == 407) || (width == 564 && height == 246)) {
            saveAdvertising(imageFile, bank, paymentReceiptFile, user.getEmail());
            System.out.println("Реклама создана с изображением: " + imageFile.getOriginalFilename());
        } else {
            throw new IllegalArgumentException("Размер изображения должен быть 285x407 или 564x246.");
        }
    }


    public void saveAdvertising(MultipartFile imageFile, String bank, MultipartFile paymentReceiptFile, String userEmail) throws IOException, MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("Пользователь с таким email не найден.");
        }

        User user = userOptional.get();
        String userDirectoryPath = baseDirectoryPath + File.separator + user.getId();
        File userDirectory = new File(userDirectoryPath);

        if (!userDirectory.exists()) {
            boolean created = userDirectory.mkdirs();
            if (!created) {
                throw new IOException("Не удалось создать директорию: " + userDirectoryPath);
            }
        }

        File savedImageFile = new File(userDirectory, imageFile.getOriginalFilename());
        imageFile.transferTo(savedImageFile);

        File savedReceiptFile = new File(userDirectory, paymentReceiptFile.getOriginalFilename());
        paymentReceiptFile.transferTo(savedReceiptFile);

        AdVersiting ad = new AdVersiting(savedImageFile.getAbsolutePath(), true, savedReceiptFile.getAbsolutePath(), bank, user);
        ad.setCreatedAt(LocalDateTime.now());
        ad.setActive(false);
        adVersitingRepository.save(ad);


        if (savedReceiptFile.exists()) {
            sendReceiptAsDocumentToTelegram(savedReceiptFile, bank, ad);
        }
    }

    @Scheduled(fixedRate = 86400000)
    public void scheduleExpiredPublishesRemoval() {
        removeExpiredPublishes();
    }

    @Transactional
    public void removeExpiredPublishes() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.minusDays(30);

        List<AdVersiting> expiredPublishes = adVersitingRepository.findAllByCreatedAtBefore(expirationTime);

        for (AdVersiting publish : expiredPublishes) {
            mailingService.sendMailing1(
                    publish.getUser().getEmail(),
                    "Уведомление о завершении срока действия \n",
                    "Привет, на связи отдел договоров Ulutman.ru! \n" +
                    "Срок действия вашего рекламы по id: " + publish.getId() + " подошел к концу. \n" +
                    " Оно больше не будет отображаться на Ulutman.ru. \n" +
                    " С уважением," +
                    " Команда Ulutman.ru \n");

            adVersitingRepository.delete(publish);
        }
    }

    public void sendReceiptAsDocumentToTelegram(File receiptFile, String bankName, AdVersiting adVersiting) throws MessagingException {
        if (!receiptFile.exists() || !receiptFile.canRead()) {
            throw new RuntimeException("Файл не найден или недоступен для чтения: " + receiptFile.getAbsolutePath());
        }

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/pdf"); // Измените на "image/png" или другой тип в зависимости от файла

        RequestBody fileBody = RequestBody.create(mediaType, receiptFile);

        String messageBody = "Новый чек: \n" +
                             "Имя карты: " + bankName + "\n" +
                             "Реклама ID: " + adVersiting.getId() + "\n" +
                             "Email пользователя: " + (adVersiting.getUser() != null ? adVersiting.getUser().getEmail() : "Не указан");


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("chat_id", ADMIN_CHAT_ID)
                .addFormDataPart("document", receiptFile.getName(), fileBody)
                .addFormDataPart("caption", messageBody); // Добавляем текст сообщения как caption

        Request request = new Request.Builder()
                .url(String.format("https://api.telegram.org/bot%s/sendDocument", TELEGRAM_BOT_TOKEN))
                .post(builder.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response.code() + ": " + responseBody);
            }
            System.out.println("Документ и сообщение успешно отправлены в Telegram. Ответ: " + responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при отправке документа в Telegram", e);
        }
    }


    public List<AdVersiting> getAllActiveAds() {
        return adVersitingRepository.findAllActiveAdvertisements();
    }

    public boolean deleteAd(Long id) {
        Optional<AdVersiting> ad = adVersitingRepository.findById(id);
        if (ad.isPresent()) {
            adVersitingRepository.delete(ad.get());
            return true;
        }
        return false;
    }

    public List<AdVersiting> getAllActiveAdsForUser(Long userId) {
        return adVersitingRepository.findAllActiveAdvertisementsByUserId(userId);
    }

    // Метод для удаления объявления конкретного пользователя
    public boolean deleteAd(Long id, Long userId) {
        Optional<AdVersiting> ad = adVersitingRepository.findById(id);
        if (ad.isPresent() && ad.get().getUser().getId().equals(userId)) {
            adVersitingRepository.delete(ad.get());
            return true;
        }
        return false;
    }
}
