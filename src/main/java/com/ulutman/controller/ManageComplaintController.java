package com.ulutman.controller;

import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.ComplaintRequest;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.model.enums.ComplaintType;
import com.ulutman.repository.UserRepository;
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
@Tag(name = "Manage Complaint")
@SecurityRequirement(name = "Authorization")
public class ManageComplaintController {

    private final ManageComplaintService complaintService;
    private final ManageComplaintService manageComplaintService;
    private final UserRepository userRepository;

    @Operation(summary = "Create a  complaint")
    @ApiResponse(responseCode = "201", description = "Complaint created successfully")
    @PostMapping("/create")
    public ResponseEntity<ComplaintResponse> createComplaint(@RequestBody ComplaintRequest complaintRequest) {
        ComplaintResponse response = complaintService.createComplaint(complaintRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all complaints")
    @ApiResponse(responseCode = "201", description = "Return list of complaints")
    @GetMapping("/getAll")
    public ResponseEntity<List<ComplaintResponse>> getAll() {
        List<ComplaintResponse> complaints = manageComplaintService.getAllComplaints();

        if (complaints.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращает 204 No Content, если список пуст
        }

        return ResponseEntity.ok(complaints); // Возвращает 200 OK с телом ответа
    }

    @Operation(summary = "Update complaint status")
    @ApiResponse(responseCode = "201", description = "Updated complaint status successfully")
    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse> updateComplaintStatus(@PathVariable Long id, @RequestBody ComplaintRequest complaintRequest) {
        ComplaintResponse updatedComplaint = complaintService.updateComplaintStatus(id, complaintRequest);
        return ResponseEntity.ok(updatedComplaint);
    }

    @Operation(summary = "Filter  users by name")
    @ApiResponse(responseCode = "201", description = "Users  by name successfully filtered")
    @GetMapping("/name/filter")
    public List<AuthResponse> filterUsers(@RequestParam(required = false) String name) {
        return manageComplaintService.filterUsersByName(name);
    }

    @Operation(summary = "Filter complaints")
    @ApiResponse(responseCode = "201", description = "Complaints successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<ComplaintResponse>> filterComplaints(
            @RequestParam(required = false) List<ComplaintType> complaintTypes,
            @RequestParam(required = false)  List<LocalDate> createDates,
            @RequestParam(required = false) List<ComplaintStatus> complaintStatuses) {
        List<ComplaintResponse> filteredComplaints =manageComplaintService.filterComplaints(complaintTypes,createDates,complaintStatuses);

        return ResponseEntity.ok(filteredComplaints);
    }

    @Operation(summary = "Reset filters complaints")
    @ApiResponse(responseCode = "201", description = "Complaints filters successfully reset")
    @GetMapping("/resetFilter")
    public List<ComplaintResponse> resetFilter() {
        return manageComplaintService.getAllComplaints();
    }
}
