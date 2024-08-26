package com.ulutman.controller;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
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
@Tag(name = "Auth")
@SecurityRequirement(name = "Authorization")
public class ManageUserController {

    private final ManageUserService manageUserService;


    @Operation(summary = "Manage  users: add user")
    @ApiResponse(responseCode = "201", description = "User added successfully")
    @PostMapping("/addUsers")
    public ResponseEntity<AuthResponse> save(@RequestBody AuthRequest authRequest) {
        AuthResponse response = manageUserService.save(authRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Manage  users: get all users")
    @ApiResponse(responseCode = "201", description = "Return list if users")
    @GetMapping("/getAll")
    public List<AuthResponse> getAll() {
        return manageUserService.getAllUsers();
    }

    @Operation(summary = "Manage  users: get user by Id")
    @ApiResponse(responseCode = "201", description = "User found")
    @GetMapping("/getById/{id}")
    public AuthResponse getById(@PathVariable Long id) {
        return manageUserService.getUserById(id);
    }


    @Operation(summary = "Manage  users: delete user by Id")
    @ApiResponse(responseCode = "201", description = "Deleted user by id successfully")
    @DeleteMapping("/deleteById{id}")
    public String deleteById(@PathVariable Long id) {
        manageUserService.deleteUserById(id);
        return "Пользователь успешно удален";
    }

    @Operation(summary = "Manage  users: update user status")
    @ApiResponse(responseCode = "201", description = "Updated user status successfully")
    @PutMapping("/{id}/status")
    public ResponseEntity<AuthResponse> updateUserStatus(@PathVariable Long id,
                                                         @RequestParam Status newStatus) {
        AuthResponse authResponse = manageUserService.updateUserStatus(id, newStatus);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Manage user: filter by name")
    @ApiResponse(responseCode = "201", description = "Users  by name successfully filtered")
    @GetMapping("/name/filter")
    public List<AuthResponse> filterUsers(@RequestParam(required = false) String name) {
        return manageUserService.filterUsersByName(name);
    }

    @Operation(summary = "Manage user: filter users")
    @ApiResponse(responseCode = "201", description = "Users successfully filtered")
    @GetMapping("/filter")
    public List<AuthResponse> filterUsers(@RequestParam(required = false) List<Role> roles,
                                          @RequestParam(required = false) List<LocalDate> createDate,
                                          @RequestParam(required = false) List<Status> statuses) {
        return manageUserService.filterUsers(roles, createDate, statuses);
    }

    @Operation(summary = "Manage users: reset filters users")
    @ApiResponse(responseCode = "201", description = "Users filters successfully reset")
    @GetMapping("/resetFilter")
    public List<AuthResponse> resetFilter() {
        return manageUserService.getAllUsers();
    }
}
