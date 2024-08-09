package com.ulutman.controller;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.service.UserManagementService;
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
public class UserManagementController {

    private final UserManagementService userManagementService;

    @Operation(summary = "Manage  users: add user")
    @ApiResponse(responseCode = "201", description = "User added successfully")
    @PostMapping("/addUsers")
    public ResponseEntity<AuthResponse> save(@RequestBody AuthRequest authRequest) {
        AuthResponse response = userManagementService.save(authRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Manage  users: get all users")
    @ApiResponse(responseCode = "201", description = "Return list if users")
    @GetMapping("/getAll")
    public List<AuthResponse> getAll() {
        return userManagementService.getAllUsers();
    }

    @Operation(summary = "Manage  users: get user by Id")
    @ApiResponse(responseCode = "201", description = "User found")
    @GetMapping("/getById/{id}")
    public AuthResponse getById(@PathVariable Long id) {
        return userManagementService.getUserById(id);
    }

    @Operation(summary = "Manage  users: update user by Id")
    @ApiResponse(responseCode = "201", description = "Updated user by id successfully")
    @PutMapping("/updateById/{id}")
    public AuthResponse updateById(@PathVariable Long id, @RequestBody AuthRequest authRequest) {
        return userManagementService.updateUser(id, authRequest);
    }

    @Operation(summary = "Manage  users: delete user by Id")
    @ApiResponse(responseCode = "201", description = "Deleted user by id successfully")
    @DeleteMapping("/deleteById{id}")
    public String deleteById(@PathVariable Long id) {
        userManagementService.deleteUserById(id);
        return "Пользователь успешно удален";
    }

    @Operation(summary = "Manage  users: update user status")
    @ApiResponse(responseCode = "201", description = "Updated user status successfully")
    @PutMapping("/role/{id}")
    public AuthResponse updateRoleUser(@PathVariable Long id, @RequestBody AuthRequest authRequest) {
        return userManagementService.updateUserRole(id, authRequest);
    }

    @Operation(summary = "Manage user: filter users")
    @ApiResponse(responseCode = "201", description = "Users successfully filtered")
    @GetMapping("/filter")
    public List<AuthResponse> getFilteredProduct(
            @RequestParam(value = "name", required = false) List<String> names,
            @RequestParam(value = "role", required = false) List<String> roles,
            @RequestParam(value = "createDate", required = false) List<LocalDate> createDate,
            @RequestParam(value = "status", required = false) List<String> status
    ) {
        return userManagementService.getFilteredUser(names, roles, createDate, status);
    }

    @Operation(summary = "Manage users: reset filters users")
    @ApiResponse(responseCode = "201", description = "Users filters successfully reset")
    @GetMapping("/resetFilter")
    public List<AuthResponse> resetFilter() {
        return userManagementService.getAllUsers();
    }
}
