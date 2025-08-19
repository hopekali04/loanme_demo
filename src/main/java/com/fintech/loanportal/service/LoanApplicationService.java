package com.fintech.loanportal.service;

import com.fintech.loanportal.dto.LoanApplicationRequest;
import com.fintech.loanportal.entity.LoanApplication;
import com.fintech.loanportal.entity.User;

import java.util.List;

public interface LoanApplicationService {
    LoanApplication applyForLoan(LoanApplicationRequest loanApplicationRequest, User user);
    List<LoanApplication> getLoanApplicationsForUser(User user);
}
