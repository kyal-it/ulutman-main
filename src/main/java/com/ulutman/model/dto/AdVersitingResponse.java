package com.ulutman.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdVersitingResponse {

    Long id;

    String imageFile;

    private boolean active;

    private String paymentReceipt;

    private String bank;

    private LocalDateTime nextBoostTime;

    private String timeToNextBoost; // Добавляем поле для строки с временем до следующего бустинга


}
