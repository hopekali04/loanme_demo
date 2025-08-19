package com.fintech.loanportal.service;

import com.fintech.loanportal.entity.LoanApplication;
import com.fintech.loanportal.entity.User;

import java.util.List;

public interface AdminService {
    List<LoanApplication> getAllLoanApplications();
    LoanApplication approveLoanApplication(Long applicationId);
    LoanApplication rejectLoanApplication(Long applicationId);
    List<User> getAllUsers();
}
