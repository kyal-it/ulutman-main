package com.ulutman.controller;

import com.ulutman.model.dto.ComplaintRequest;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping("/create")
    public ResponseEntity<ComplaintResponse> createComplaint(@RequestBody ComplaintRequest complaintRequest) {
        ComplaintResponse response = complaintService.createComplaint(complaintRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ComplaintResponse>> getAllComplaints() {
        List<ComplaintResponse> responses = complaintService.getAllComplaints();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<ComplaintResponse> updateComplaintStatus(@PathVariable Long id, @RequestParam ComplaintStatus complaintStatus) {
        ComplaintResponse response = complaintService.updateComplaintStatus(id, complaintStatus);
        return ResponseEntity.ok(response);
    }
}
