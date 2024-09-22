package com.ulutman.controller;

import com.ulutman.model.entities.UserAccount;
import com.ulutman.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-accounts")
public class UserAccountController {
    @Autowired
    private  ProfileService userAccountService;

    @PostMapping
    public ResponseEntity<UserAccount> createUserAccount(@RequestParam Long userId,
                                                         @RequestParam String username,
                                                         @RequestParam String lastName,
                                                         @RequestParam String phoneNumber,
                                                         @RequestParam String emailAddress) {
        UserAccount userAccount = userAccountService.createUserAccount(userId, username, lastName, phoneNumber, emailAddress);
        return new ResponseEntity<>(userAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserAccount> updateUserAccount(@PathVariable Long userId,
                                                         @RequestParam String username,
                                                         @RequestParam String lastName,
                                                         @RequestParam String phoneNumber,
                                                         @RequestParam String emailAddress) {
        UserAccount userAccount = userAccountService.updateUserAccount(userId, username, lastName, phoneNumber, emailAddress);
        return new ResponseEntity<>(userAccount, HttpStatus.OK);
    }
}
