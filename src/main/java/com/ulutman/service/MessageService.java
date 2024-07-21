package com.ulutman.service;


import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.MessageMapper;
import com.ulutman.model.dto.MessageRequest;
import com.ulutman.model.dto.MessageResponse;
import com.ulutman.model.entities.Message;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.repository.MessageRepository;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;


    public MessageResponse addMessage(MessageRequest messageRequest) {
        User user = userRepository.findById(messageRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь по идентификатору " + messageRequest.getUserId() + " не найден"));

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setUser(user);
        message.setModeratorStatus(ModeratorStatus.ОЖИДАЕТ);
        messageRepository.save(message);
        return messageMapper.mapToResponse(message);
    }

    public MessageResponse updateMessageStatus(Long messageId, ModeratorStatus moderatorStatus) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Сообщение по идентификатору " + messageId + " не найдено"));
        message.setModeratorStatus(moderatorStatus);
        messageRepository.save(message);
        return messageMapper.mapToResponse(message);
    }
}
