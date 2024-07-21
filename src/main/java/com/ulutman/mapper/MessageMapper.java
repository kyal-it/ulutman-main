package com.ulutman.mapper;


import com.ulutman.model.dto.MessageRequest;
import com.ulutman.model.dto.MessageResponse;
import com.ulutman.model.entities.Message;
import com.ulutman.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final MessageRepository messageRepository;

    public Message mapToEntity(MessageRequest messageRequest) {
        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setUser(message.getUser());
        message.setModeratorStatus(message.getModeratorStatus());
        return message;
    }

    public MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .username(message.getUser().getUsername())
                .moderatorStatus(message.getModeratorStatus())
                .build();
    }
}
