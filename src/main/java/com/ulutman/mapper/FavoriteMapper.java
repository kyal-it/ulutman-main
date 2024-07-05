package com.ulutman.mapper;

import com.ulutman.model.dto.FavoriteResponse;
import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.Publish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoriteMapper {
    private final PublishMapper publishMapper;
    public FavoriteResponse mapToResponse(Favorite favorites,Publish publish) {
        return FavoriteResponse.builder()
                .id(favorites.getId())
                .publishResponse(publishMapper.mapToResponse(publish))
                .build();
    }
    }

