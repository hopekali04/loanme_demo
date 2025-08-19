package com.fintech.loanportal.repository;

import com.fintech.loanportal.entity.LoanApplication;
import com.fintech.loanportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByUser(User user);
}
