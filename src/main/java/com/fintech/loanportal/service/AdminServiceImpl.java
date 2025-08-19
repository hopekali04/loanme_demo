package com.fintech.loanportal.service;

import com.fintech.loanportal.entity.LoanApplication;
import com.fintech.loanportal.entity.User;
import com.fintech.loanportal.repository.LoanApplicationRepository;
import com.fintech.loanportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }

    @Override
    public LoanApplication approveLoanApplication(Long applicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Loan application not found"));
        // Add logic to approve the loan
        return loanApplicationRepository.save(loanApplication);
    }

    @Override
    public LoanApplication rejectLoanApplication(Long applicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Loan application not found"));
        // Add logic to reject the loan
        return loanApplicationRepository.save(loanApplication);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
