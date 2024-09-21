package com.ulutman.service;

import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishResponse;
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
public class MainPageService {

    private final PublishRepository publishRepository;
    private final PublishMapper publishMapper;

    public List<PublishResponse> findPublishByCategoryWork() {
        List<Publish> work = publishRepository.findByCategoryWork();
        return work.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryRent() {
        List<Publish> rent = publishRepository.findByCategoryRent();
        return rent.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategorySell() {
        List<Publish> sell = publishRepository.findByCategorySell();
        return sell.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryHotel() {
        List<Publish> hotel = publishRepository.findByCategoryHotel();
        return hotel.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryServices() {
        List<Publish> services = publishRepository.findByCategoryServices();
        return services.stream().map(publishMapper::mapToResponse).toList();
    }
    public List<PublishResponse> findPublishByCategoryRealEstate() {
        List<Publish> realEstate = publishRepository.findByCategoryRealEstate();
        return realEstate.stream().map(publishMapper::mapToResponse).toList();
    }
}
