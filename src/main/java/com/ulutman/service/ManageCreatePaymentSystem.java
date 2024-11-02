package com.ulutman.service;

import com.ulutman.model.entities.Mailing;
import com.ulutman.model.entities.Publish;
import com.ulutman.repository.PublishRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageCreatePaymentSystem {
//    final PublishRepository publishRepository; // Репозиторий для работы с публикациями
//    final MailingService mailingService;
//    // Метод для получения всех деактивированных публикаций
//    public List<Publish> getAllDeactivatedPublications() {
//        log.info("Получение всех деактивированных публикаций");
//        return publishRepository.findAllByActiveFalse();
//    }
//
//    // Метод для активации публикации
//    public void activatePublication(Long publicationId) {
//        log.info("Активация публикации с ID: {}", publicationId);
//        Publish publication = publishRepository.findById(publicationId)
//                .orElseThrow(() -> new RuntimeException("Публикация не найдена: " + publicationId));
//
//        publication.setActive(true);
//        publishRepository.save(publication);
//        // Отправка уведомления пользователю о том, что его публикация активирована
//        mailingService.sendMailing(publication.get, "Ваша публикация успешно активирована: " + publication.getTitle());
//        log.info("Публикация активирована и уведомление отправлено пользователю: {}", publication.getUserId());
//    }
//
//    // Метод для деактивации публикации
//    public void deactivatePublication(Long publicationId) {
//        log.info("Деактивация публикации с ID: {}", publicationId);
//        Publish publication = publicationRepository.findById(publicationId)
//                .orElseThrow(() -> new RuntimeException("Публикация не найдена: " + publicationId));
//
//        publication.setActive(false);
//        publishRepository.save(publication);
//        log.info("Публикация деактивирована: {}", publication);
//    }
}
