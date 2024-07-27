package com.ulutman.controller;

import com.ulutman.model.dto.ComplaintRequest;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.model.enums.ComplaintType;
import com.ulutman.service.ManageComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/complaints")
public class ManageComplaintController {

    private final ManageComplaintService complaintService;
    private final ManageComplaintService manageComplaintService;

    @PostMapping("/create")
    public ResponseEntity<ComplaintResponse> createComplaint(@RequestBody ComplaintRequest complaintRequest) {
        ComplaintResponse response = complaintService.createComplaint(complaintRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public List<ComplaintResponse> getAll() {
        return complaintService.getAllComplaints();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse> updateComplaintStatus(@PathVariable Long id, @RequestBody ComplaintRequest complaintRequest) {
        ComplaintResponse updatedComplaint = complaintService.updateComplaintStatus(id, complaintRequest);
        return ResponseEntity.ok(updatedComplaint);
    }

    @GetMapping("/filter")
    public List<ComplaintResponse> filterComplaints(
            @RequestParam(value = "user", required = false) List<User> users,
            @RequestParam(value = "complaintType", required = false) List<String> complaintTypes,
            @RequestParam(value = "createDate", required = false) List<LocalDate> createDates,
            @RequestParam(value = "complaintStatus", required = false) List<String> complaintStatuses) {

        return manageComplaintService.getFilteredComplaints(users, complaintTypes, createDates, complaintStatuses);
    }

    @GetMapping("/resetFilter")
    public List<ComplaintResponse> resetFilter() {
        return manageComplaintService.getAllComplaints();
    }
}
