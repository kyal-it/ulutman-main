package com.ulutman.service;

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
    final PublishRepository publishRepository;
    final MailingService mailingService;

    public List<Publish> getAllDeactivatedPublications() {
        log.info("Получение всех деактивированных публикаций");
        return publishRepository.findAllByActiveFalse();
    }

    public void activatePublication(Long publicationId) {
        log.info("Активация публикации с ID: {}", publicationId);
        Publish publication = publishRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Публикация не найдена: " + publicationId));

        publication.setActive(true);
        publishRepository.save(publication);
        mailingService.sendMailing1(
                publication.getUser().getEmail(),
                "Ваша публикация активирована!",
                "Мы рады сообщить вам, что ваша публикация «" + publication + "» успешно активирована на сайте ULUTMAN.ru \n" +
                "Если у вас есть вопросы, пишите на: ulutman@gmail.comnn \n" +
                "С уважением, " +
                "Команда ULUTMAN"
        );
        log.info("Публикация активирована и уведомление отправлено пользователю: {}", publication.getUser().getId());
    }

    public void deactivatePublication(Long publicationId) {
        log.info("Деактивация публикации с ID: {}", publicationId);
        Publish publication = publishRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Публикация не найдена: " + publicationId));

        publication.setActive(false);
        publishRepository.save(publication);
        mailingService.sendMailing1(
                publication.getUser().getEmail(),
                "Уведомление о том, что ваша публикация не активирована",
                "Привет, на связи отдел договоров Ulutman.ru!\n" +
                "Ваша публикация не активирована: {" + publication + "}. Возможно, причины связаны с оплатой или с условиями Ulutman.ru.\n" +
                "Она не будет отображаться на Ulutman.ru.\nСвяжитесь с поддержкой сайта для получения дополнительной информации." +
                "\nС уважением," +
                "\nКоманда Ulutman.ru"
        );
        log.info("Публикация деактивирована: {}", publication);
    }
}
