package com.ulutman.mapper;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PublishMapper {

    public Publish mapToEntity(PublishRequest publishRequest) {
        Publish publish = new Publish();
        publish.setDescription(publishRequest.getDescription());
        publish.setMetro(publishRequest.getMetro());
        publish.setAddress(publishRequest.getAddress());
        publish.setPhone(publishRequest.getPhoneNumber());
        publish.setImage(publishRequest.getImage());
        publish.setCategory(publishRequest.getCategory());
        publish.setSubCategory(publishRequest.getSubcategory());
        publish.setBank(publishRequest.getBank());
        publish.setPublishStatus(publishRequest.getPublishStatus());
        publish.setUser(publish.getUser());
        publish.setPublishStatus(publishRequest.getPublishStatus());
        publish.setCategory(publishRequest.getCategory());
        return publish;
    }

    public PublishResponse mapToResponse(Publish publish) {
        return PublishResponse.builder()
                .id(publish.getId())
                .description(publish.getDescription())
                .metro(publish.getMetro())
                .category(publish.getCategory())
                .subcategory(publish.getSubCategory())
                .address(publish.getAddress())
                .phoneNumber(publish.getPhone())
                .image(publish.getImage())
                .bank(publish.getBank())
                .publishStatus(publish.getPublishStatus())
                .createDate(LocalDate.now())
                .categoryStatus(publish.getCategoryStatus())
                .build();
    }
}
