package com.fintech.loanportal.controller;

import com.fintech.loanportal.entity.User;
import com.fintech.loanportal.security.CurrentUser;
import com.fintech.loanportal.security.UserPrincipal;
import com.fintech.loanportal.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management", description = "Endpoints for user information")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        User user = userService.getUserByEmail(currentUser.getUserEmail());
        return ResponseEntity.ok(user);
    }
}
