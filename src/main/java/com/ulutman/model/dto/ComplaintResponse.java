package com.ulutman.model.dto;

import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.model.enums.ComplaintType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintResponse {

    Long id;
    ComplaintType complaintType;
    String complaintContent;
    AuthResponse authResponse;
    LocalDate createDate;
    ComplaintStatus complaintStatus;
}
