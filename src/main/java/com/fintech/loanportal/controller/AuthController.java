package com.fintech.loanportal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintech.loanportal.dto.JwtAuthenticationResponse;
import com.fintech.loanportal.dto.LoginRequest;
import com.fintech.loanportal.dto.RegisterRequest;
import com.fintech.loanportal.entity.User;
import com.fintech.loanportal.security.JwtTokenProvider;
import com.fintech.loanportal.service.AuditService;
import com.fintech.loanportal.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuditService auditService;

    @Operation(summary = "User Login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateAccessToken(authentication);
            
            User user = userService.getUserByEmail(loginRequest.getEmail());
            auditService.logEvent("USER_LOGIN_SUCCESS", "User logged in successfully: " + user.getEmail(), user.getId(), request);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (Exception e) {
            auditService.logEvent("USER_LOGIN_FAILURE", "Failed login attempt for email: " + loginRequest.getEmail(), null, request);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @Operation(summary = "User Registration", description = "Registers a new user.")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "User already exists")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email address already in use!", HttpStatus.BAD_REQUEST);
        }

        User user = userService.registerUser(registerRequest);
        auditService.logEvent("USER_REGISTRATION_SUCCESS", "New user registered: " + user.getEmail(), user.getId(), request);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}