package com.ulutman.controller;

import com.ulutman.model.dto.UserAccountCreateRequest;
import com.ulutman.model.dto.UserAccountUpdateRequest;
import com.ulutman.model.entities.UserAccount;
import com.ulutman.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user-accounts")
@Tag(name = "Profile")
@SecurityRequirement(name = "Authorization")
public class UserAccountController {
    @Autowired
    private ProfileService userAccountService;

    @Operation(summary = "Create a profile")
    @ApiResponse(responseCode = "201", description = "The profile created successfully")
    @PostMapping
    public ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccountCreateRequest request) {
        UserAccount userAccount = userAccountService.createUserAccount(
                request.getUserId(),
                request.getUsername(),
                request.getLastName(),
                request.getPhoneNumber(),
                request.getEmailAddress()
        );
        return new ResponseEntity<>(userAccount, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a profile")
    @ApiResponse(responseCode = "201", description = "The profile updated successfully")
    @PutMapping("/{userId}")
    public ResponseEntity<UserAccount> updateUserAccount(@PathVariable Long userId,
                                                         @RequestBody UserAccountUpdateRequest request) {
        UserAccount userAccount = userAccountService.updateUserAccount(
                userId,
                request.getUsername(),
                request.getLastName(),
                request.getPhoneNumber(),
                request.getEmailAddress()
        );
        return new ResponseEntity<>(userAccount, HttpStatus.OK);
    }
}

