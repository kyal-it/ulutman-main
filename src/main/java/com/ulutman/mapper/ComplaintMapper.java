package com.ulutman.mapper;

import com.ulutman.model.dto.ComplaintRequest;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.entities.Complaint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComplaintMapper {

    public Complaint mapToEntity(ComplaintRequest complaintRequest) {
        Complaint complaint = new Complaint();
        complaint.setComplaintType(complaintRequest.getComplaintType());
        complaint.setComplaintContent(complaintRequest.getComplaintContent());
        complaint.setUser(complaint.getUser());
        return complaint;
    }

    public ComplaintResponse mapToResponse(Complaint complaint) {
        return ComplaintResponse.builder()
                .complaintType(complaint.getComplaintType())
                .complaintContent(complaint.getComplaintContent())
                .complaintStatus(complaint.getComplaintStatus())
                .createDate(complaint.getCreateDate())
                .build();
    }
}
