package com.fintech.loanportal.controller;

import com.fintech.loanportal.dto.LoanCalculationRequest;
import com.fintech.loanportal.dto.LoanCalculationResult;
import com.fintech.loanportal.dto.LoanApplicationRequest;
import com.fintech.loanportal.entity.LoanApplication;
import com.fintech.loanportal.entity.User;
import com.fintech.loanportal.security.CurrentUser;
import com.fintech.loanportal.security.UserPrincipal;
import com.fintech.loanportal.service.LoanCalculationService;
import com.fintech.loanportal.service.LoanApplicationService;
import com.fintech.loanportal.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loan Management", description = "Endpoints for loan calculation and applications")
public class LoanController {

    @Autowired
    private LoanCalculationService loanCalculationService;

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    private UserService userService;

    @PostMapping("/calculate")
    public ResponseEntity<LoanCalculationResult> calculateLoan(@Valid @RequestBody LoanCalculationRequest request) {
        LoanCalculationResult result = loanCalculationService.calculateLoan(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LoanApplication> applyForLoan(@Valid @RequestBody LoanApplicationRequest request, @CurrentUser UserPrincipal currentUser) {
        User user = userService.getUserByEmail(currentUser.getUserEmail());
        LoanApplication loanApplication = loanApplicationService.applyForLoan(request, user);
        return ResponseEntity.ok(loanApplication);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LoanApplication>> getLoanApplications(@CurrentUser UserPrincipal currentUser) {
        User user = userService.getUserByEmail(currentUser.getUserEmail());
        List<LoanApplication> loanApplications = loanApplicationService.getLoanApplicationsForUser(user);
        return ResponseEntity.ok(loanApplications);
    }
}
