package com.ulutman.controller;

import com.ulutman.model.entities.Admin;
import com.ulutman.model.enums.ServiceRole;
import com.ulutman.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
@Tag(name = "Auth")
@SecurityRequirement(name = "Authorization")
public class AdminController {

    private final AdminService adminService;


    @Operation(summary = "Create a new Admin")
    @ApiResponse(responseCode = "201", description = "Admin created successfully")
    @PostMapping("/create")
    public ResponseEntity<String> createAdmin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Set<ServiceRole> roles) {
        Admin admin = adminService.createAdmin(username, password, roles);
        return ResponseEntity.ok("Админ " + admin.getUsername() + " созданный с помощью ролей: " + roles);
    }

    @Operation(summary = "Get Admin by username")
    @ApiResponse(responseCode = "201", description = "Admin received successfully")
    @GetMapping("/{username}")
    public ResponseEntity<Admin> getAdminByUsername(@PathVariable String username) {
        Optional<Admin> adminOpt = adminService.findAdminByUsername(username);
        if (adminOpt.isPresent()) {
            return ResponseEntity.ok(adminOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update Admin roles")
    @ApiResponse(responseCode = "201", description = "Updated admin roles successfully")
    @PutMapping("/updateRoles")
    public ResponseEntity<String> updateAdminRoles(
            @RequestParam String username,
            @RequestParam Set<ServiceRole> roles) {
        adminService.updateAdminRoles(username, roles);
        return ResponseEntity.ok("Roles updated for admin " + username + ": " + roles);
    }
}
