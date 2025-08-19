package com.fintech.loanportal.controller;

import com.fintech.loanportal.entity.LoanApplication;
import com.fintech.loanportal.entity.User;
import com.fintech.loanportal.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Endpoints for administrative tasks")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/loans")
    public ResponseEntity<List<LoanApplication>> getAllLoanApplications() {
        List<LoanApplication> loanApplications = adminService.getAllLoanApplications();
        return ResponseEntity.ok(loanApplications);
    }

    @PostMapping("/loans/{id}/approve")
    public ResponseEntity<LoanApplication> approveLoanApplication(@PathVariable Long id) {
        LoanApplication loanApplication = adminService.approveLoanApplication(id);
        return ResponseEntity.ok(loanApplication);
    }

    @PostMapping("/loans/{id}/reject")
    public ResponseEntity<LoanApplication> rejectLoanApplication(@PathVariable Long id) {
        LoanApplication loanApplication = adminService.rejectLoanApplication(id);
        return ResponseEntity.ok(loanApplication);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
