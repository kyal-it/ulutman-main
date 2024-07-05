package com.ulutman.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class FavoriteResponseList {
    Long id;
    private List<PublishResponse> publishResponseList;
}
