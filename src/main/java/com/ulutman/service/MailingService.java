package com.ulutman.service;

import com.ulutman.exception.MailSendingException;
import com.ulutman.exception.PasswordsDoNotMatchException;
import com.ulutman.mapper.MailingMapper;
import com.ulutman.model.dto.MailingRequest;
import com.ulutman.model.dto.MailingResponse;
import com.ulutman.model.entities.Mailing;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.MailingStatus;
import com.ulutman.repository.MailingRepository;
import com.ulutman.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailingService {

    private final MailingRepository mailingRepository;
    private final MailingMapper mailingMapper;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MailingResponse createMailing(MailingRequest mailingRequest) {
        Mailing mailing = mailingMapper.mapToEntity(mailingRequest);
        mailing.setTitle(mailingRequest.getTitle());
        mailing.setMessage(mailingRequest.getMessage());
        mailing.setMailingType(mailingRequest.getMailingType());
        mailing.setMailingStatus(mailingRequest.getMailingStatus());
        mailing.setPromotionStartDate(mailingRequest.getPromotionStartDate());
        mailing.setPromotionEndDate(mailingRequest.getPromotionEndDate());
        mailing.setCreateDate(LocalDate.now());

        List<User> users = userRepository.findAll();
        mailing.setRecipients(users);

        for (User user : users) {
            user.getMailings().add(mailing); // Обновляем связь у пользователя
        }
        mailingRepository.save(mailing);
        userRepository.saveAll(users);

        return mailingMapper.mapToResponse(mailing);
    }

    public void sendMailing1(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
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

    public void sendPublicationRejectionNotification(String recipientEmail, String publicationTitle) throws MessagingException {
        log.info("Отправка уведомления на email: {}", recipientEmail);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject("Ваша публикация отклонена");

        String body = "<html><body>"
                      + "<h2>Уважаемый пользователь,</h2>"
                      + "<p>Ваша публикация с заголовком <strong>" + publicationTitle + "</strong> была отклонена и удалена, "
                      + "так как она не соответствует нашим требованиям.</p>"
                      + "<p>Создайте публикацию, которая соответствует установленным требованиям. Если у вас есть вопросы, пожалуйста, свяжитесь с поддержкой.</p>"
                      + "</body></html>";

        helper.setText(body, true);

        try {
            javaMailSender.send(message);
            log.info("Уведомление успешно отправлено на email: {}", recipientEmail);
        } catch (MailException e) {
            log.error("Ошибка при отправке уведомления на email: {}", recipientEmail, e);
            throw e;
        }
    }

    public void sendCommentRejectionNotification(String recipientEmail, String commentContent) throws MessagingException {
        // Создание сообщения и отправка его, аналогично методу для публикаций
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject("Ваш комментарий отклонен");

        // Форматирование тела сообщения
        String body = "<html><body>"
                      + "<p>Ваш комментарий был отклонен:</p>"
                      + "<p><strong>Содержимое:</strong> " + commentContent + "</p>"
                      + "<p>Если у вас есть вопросы, пожалуйста, свяжитесь с поддержкой.</p>"
                      + "</body></html>";

        helper.setText(body, true);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new MessagingException("Ошибка отправки уведомления", e);
        }
    }

    public void sendComplaintRejectionNotification(String recipientEmail, String complaintDetails) throws MessagingException {
        // Логика для отправки уведомления пользователю о том, что его жалоба отклонена
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject("Ваша жалоба отклонена");

        // Форматирование тела сообщения
        String body = "<html><body>"
                      + "<p>К сожалению, ваша жалоба была отклонена.</p>"
                      + "<p><strong>Детали жалобы:</strong> " + complaintDetails + "</p>"
                      + "<p>Если у вас есть вопросы, пожалуйста, свяжитесь с поддержкой.</p>"
                      + "</body></html>";

        helper.setText(body, true);
        javaMailSender.send(message);
    }


    public void sendPasswordResetCode(String email) throws EntityNotFoundException {
        int pinCode = generatePinCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("ajzirektoktosunova853@gmail.com");
        message.setSubject("Password reset");
        message.setText(String.valueOf(pinCode));
        try {
            javaMailSender.send(message);
            updateUserPinCode(email, pinCode);
        } catch (MailException e) {
            throw new MailSendingException("Не удалось отправить код для сброса пароля", e);
        }
    }

    public String resetPassword(String email, int pinCode, String newPassword, String confirmPassword)
            throws EntityNotFoundException, PasswordsDoNotMatchException {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordsDoNotMatchException("Пароли не совпадают");
        } else {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
            if (user.getEmail().equals(email)) {
                if (pinCode == user.getPinCode()) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    return "Сброс пароля прошел успешно";
                }
            }
            return "Неверный PIN-код";
        }
    }

    private int generatePinCode() {
        Random random = new Random();
        return random.nextInt(100000, 1000000);
    }

    private void updateUserPinCode(String email, int pinCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        user.setPinCode(pinCode);
        userRepository.save(user);
    }
}

