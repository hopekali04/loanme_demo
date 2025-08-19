package com.fintech.loanportal.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public class LoanCalculationRequest {

    @DecimalMin("1.0")
    private BigDecimal loanAmount;

    @DecimalMin("0.0")
    private BigDecimal interestRate;

    @Min(1)
    private int loanTermMonths;
        private java.time.LocalDate startDate;

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getLoanTermMonths() {
        return loanTermMonths;
    }

    public void setLoanTermMonths(int loanTermMonths) {
        this.loanTermMonths = loanTermMonths;
    }
    public java.time.LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(java.time.LocalDate startDate) {
        this.startDate = startDate;
    }
}
