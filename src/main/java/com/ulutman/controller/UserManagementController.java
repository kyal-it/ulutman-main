package com.ulutman.controller;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@Slf4j
public class UserManagementController {

    private final UserManagementService userManagementService;

    @PostMapping("/addUsers")
    public ResponseEntity<AuthResponse> save(@RequestBody AuthRequest authRequest) {
        AuthResponse response = userManagementService.save(authRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuthResponse> getAll() {
        return userManagementService.getAllUsers();
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AuthResponse getById(@PathVariable Long id) {
        return userManagementService.getUserById(id);
    }

    @PutMapping("/updateById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AuthResponse updateById(@PathVariable Long id, @RequestBody AuthRequest authRequest) {
        return userManagementService.updateUser(id, authRequest);
    }

    @DeleteMapping("/deleteById{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteById(@PathVariable Long id) {
        userManagementService.deleteUserById(id);
        return "Пользователь успешно удален";
    }

    @PutMapping("/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AuthResponse updateRoleUser(@PathVariable Long id, @RequestBody AuthRequest authRequest) {
        return userManagementService.updateUserRole(id, authRequest);
    }

    @GetMapping("/filter")
    public List<AuthResponse> getFilteredProduct(
            @RequestParam(value = "name", required = false) List<String> names,
            @RequestParam(value = "role", required = false) List<String> roles,
            @RequestParam(value = "createDate", required = false) List<LocalDate> createDate,
            @RequestParam(value = "status", required = false) List<String> status
    ) {
        return userManagementService.getFilteredUser(names, roles, createDate, status);
    }

    @GetMapping("/resetFilter")
    public List<AuthResponse> resetFilter() {
        return userManagementService.getAllUsers();
    }
}
