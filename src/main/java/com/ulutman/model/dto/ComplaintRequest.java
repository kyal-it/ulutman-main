package com.ulutman.model.dto;

import com.ulutman.model.enums.ComplaintType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintRequest {

    ComplaintType complaintType;
    String complaintContent;
    Long userId;
}
