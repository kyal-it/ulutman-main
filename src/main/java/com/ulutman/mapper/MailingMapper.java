package com.ulutman.mapper;

import com.ulutman.model.dto.MailingRequest;
import com.ulutman.model.dto.MailingResponse;
import com.ulutman.model.entities.Mailing;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MailingMapper {

    public Mailing mapToEntity(MailingRequest mailingRequest) {
        Mailing mailing = new Mailing();
        mailing.setTitle(mailingRequest.getTitle());
        mailing.setMessage(mailingRequest.getMessage());
        mailing.setMailingType(mailingRequest.getMailingType());
        mailing.setMailingStatus(mailingRequest.getMailingStatus());
        mailing.setImage(mailingRequest.getImage());
        mailing.setPromotionStartDate(mailingRequest.getPromotionStartDate());
        mailing.setPromotionEndDate(mailingRequest.getPromotionEndDate());
        mailing.setCreateDate(LocalDate.now());
        return mailing;
    }

    public MailingResponse mapToResponse(Mailing mailing) {
        return MailingResponse.builder()
                .id(mailing.getId())
                .title(mailing.getTitle())
                .title(mailing.getTitle())
                .message(mailing.getMessage())
                .mailingType(mailing.getMailingType())
                .mailingStatus(mailing.getMailingStatus())
                .promotionStartDate(mailing.getPromotionStartDate())
                .promotionEndDate(mailing.getPromotionEndDate())
                .createDate(mailing.getCreateDate())
                .build();
    }
}
