package com.fintech.loanportal.service;

import com.fintech.loanportal.dto.LoanApplicationRequest;
import com.fintech.loanportal.entity.LoanApplication;
import com.fintech.loanportal.entity.User;
import com.fintech.loanportal.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Override
    public LoanApplication applyForLoan(LoanApplicationRequest loanApplicationRequest, User user) {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setUser(user);
        loanApplication.setLoanAmount(loanApplicationRequest.getLoanAmount());
        loanApplication.setLoanTermMonths(loanApplicationRequest.getLoanTermMonths());
        loanApplication.setInterestRate(loanApplicationRequest.getInterestRate());
        return loanApplicationRepository.save(loanApplication);
    }

    @Override
    public List<LoanApplication> getLoanApplicationsForUser(User user) {
        return loanApplicationRepository.findByUser(user);
    }
}
