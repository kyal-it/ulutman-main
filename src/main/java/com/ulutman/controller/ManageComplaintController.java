package com.ulutman.controller;

import com.ulutman.model.dto.ComplaintRequest;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.entities.User;
import com.ulutman.service.ManageComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/complaints")
@Tag(name = "Auth")
@SecurityRequirement(name = "Authorization")
public class ManageComplaintController {

    private final ManageComplaintService complaintService;
    private final ManageComplaintService manageComplaintService;

    @Operation(summary = "Manage complaints: create complaint")
    @ApiResponse(responseCode = "201", description = "Complaint created successfully")
    @PostMapping("/create")
    public ResponseEntity<ComplaintResponse> createComplaint(@RequestBody ComplaintRequest complaintRequest) {
        ComplaintResponse response = complaintService.createComplaint(complaintRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Manage complaints: get all complaints")
    @ApiResponse(responseCode = "201", description = "Return list of complaints")
    @GetMapping("/getAll")
    public List<ComplaintResponse> getAll() {
        return complaintService.getAllComplaints();
    }

    @Operation(summary = "Manage complaints: update complaint status")
    @ApiResponse(responseCode = "201", description = "Updated complaint status successfully")
    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse> updateComplaintStatus(@PathVariable Long id, @RequestBody ComplaintRequest complaintRequest) {
        ComplaintResponse updatedComplaint = complaintService.updateComplaintStatus(id, complaintRequest);
        return ResponseEntity.ok(updatedComplaint);
    }

    @Operation(summary = "Manage complaints: filter complaints")
    @ApiResponse(responseCode = "201", description = "Complaints successfully filtered")
    @GetMapping("/filter")
    public List<ComplaintResponse> filterComplaints(
            @RequestParam(value = "user", required = false) List<User> users,
            @RequestParam(value = "complaintType", required = false) List<String> complaintTypes,
            @RequestParam(value = "createDate", required = false) List<LocalDate> createDates,
            @RequestParam(value = "complaintStatus", required = false) List<String> complaintStatuses) {

        return manageComplaintService.getFilteredComplaints(users, complaintTypes, createDates, complaintStatuses);
    }

    @Operation(summary = "Manage complaints: reset filters complaints")
    @ApiResponse(responseCode = "201", description = "Complaints filters successfully reset")
    @GetMapping("/resetFilter")
    public List<ComplaintResponse> resetFilter() {
        return manageComplaintService.getAllComplaints();
    }
}
