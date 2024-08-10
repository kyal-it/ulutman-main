package com.ulutman.service;

import com.ulutman.mapper.MailingMapper;
import com.ulutman.model.dto.MailingRequest;
import com.ulutman.model.dto.MailingResponse;
import com.ulutman.model.entities.Mailing;
import com.ulutman.model.enums.MailingStatus;
import com.ulutman.repository.MailingRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailingService {

    private final MailingRepository mailingRepository;
    private final MailingMapper mailingMapper;
    private final JavaMailSender javaMailSender;

    public MailingResponse createMailing(MailingRequest mailingRequest) {

        Mailing mailing = mailingMapper.mapToEntity(mailingRequest);
        mailing.setTitle(mailingRequest.getTitle());
        mailing.setMessage(mailingRequest.getMessage());
        mailing.setMailingType(mailingRequest.getMailingType());
        mailing.setMailingStatus(mailingRequest.getMailingStatus());
        mailing.setPromotionStartDate(mailingRequest.getPromotionStartDate());
        mailing.setPromotionEndDate(mailingRequest.getPromotionEndDate());
        mailing.setCreateDate(LocalDate.now());
        mailingRepository.save(mailing);
        return mailingMapper.mapToResponse(mailing);
    }

    public void sendMailing(Long mailingId, String recipientEmail) throws MessagingException {
        Mailing mailing = mailingRepository.findById(mailingId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный почтовый идентификатор"));

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject(mailing.getTitle());

        // Форматирование тела сообщения
        String body = "<html><body>"
                      + "<h1>" + mailing.getTitle() + "</h1>"
                      + "<p>" + mailing.getMessage() + "</p>"
                      + "<p><strong>Тип рассылки:</strong> " + mailing.getMailingType() + "</p>"
                      + "<p><strong>Начало акции:</strong> " + mailing.getPromotionStartDate() + "</p>"
                      + "<p><strong>Конец акции:</strong> " + mailing.getPromotionEndDate() + "</p>"
                      + "</body></html>";

        helper.setText(body, true);

        // Если вы хотите прикрепить файл, вы можете использовать следующий код
        // FileSystemResource file = new FileSystemResource(new File(mailing.getPhotoPath()));
        // helper.addAttachment("Фотография", file);

        try {
            javaMailSender.send(message);
            mailing.setMailingStatus(MailingStatus.ОТПРАВЛЕНО);
        } catch (MailException e) {
            mailing.setMailingStatus(MailingStatus.ОШИБКА);
            throw e;
        }

        mailingRepository.save(mailing);
    }
}

