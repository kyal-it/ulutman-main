package com.ulutman.controller;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.UserResponse;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import com.ulutman.service.ManageUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/manage/users")
@Slf4j
@Tag(name = "Manage User")
@SecurityRequirement(name = "Authorization")
public class ManageUserController {

    private final ManageUserService manageUserService;


    @Operation(summary = "Add a user")
    @ApiResponse(responseCode = "201", description = "User added successfully")
    @PostMapping("/addUsers")
    public ResponseEntity<AuthResponse> save(@RequestBody AuthRequest authRequest) {
        AuthResponse response = manageUserService.save(authRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "201", description = "Return list if users")
    @GetMapping("/getAll")
    public List<AuthResponse> getAll() {
        return manageUserService.getAllUsers();
    }

    @Operation(summary = "Get user by id")
    @ApiResponse(responseCode = "201", description = "User found")
    @GetMapping("/getById/{id}")
    public AuthResponse getById(@PathVariable Long id) {
        return manageUserService.getUserById(id);
    }


    @Operation(summary = "Delete user by id")
    @ApiResponse(responseCode = "201", description = "Deleted user by id successfully")
    @DeleteMapping("/deleteById{id}")
    public String deleteById(@PathVariable Long id) {
        manageUserService.deleteUserById(id);
        return "Пользователь успешно удален";
    }

    @Operation(summary = "Update user status")
    @ApiResponse(responseCode = "201", description = "Updated user status successfully")
    @PutMapping("/{id}/status")
    public ResponseEntity<AuthResponse> updateUserStatus(@PathVariable Long id,
                                                         @RequestParam Status newStatus) {
        AuthResponse authResponse = manageUserService.updateUserStatus(id, newStatus);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Filter users by criteria")
    @ApiResponse(responseCode = "201", description = "Users successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<UserResponse>> filterUsers(
            @RequestParam(required = false) List<Role> roles,
            @RequestParam(required = false) List<Status> statuses,
            @RequestParam(required = false) List<LocalDate> createDates,
            @RequestParam(required = false) String names) {

        List<UserResponse> filteredUsers = manageUserService.filterUsers(roles, createDates, statuses, names);

        return ResponseEntity.ok(filteredUsers);
    }

    @Operation(summary = "Reset filters users")
    @ApiResponse(responseCode = "201", description = "Users filters successfully reset")
    @GetMapping("/resetFilter")
    public List<AuthResponse> resetFilter() {
        return manageUserService.getAllUsers();
    }
}
