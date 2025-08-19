package com.fintech.loanportal.dto;

import java.math.BigDecimal;
import java.util.List;

public class LoanCalculationResult {

    private BigDecimal monthlyPayment;
    private BigDecimal totalInterest;
    private BigDecimal totalAmount;
    private List<AmortizationScheduleEntry> amortizationSchedule;
        private BigDecimal loanAmount;
        private BigDecimal interestRate;
        private Integer termMonths;
        private BigDecimal totalPayments;
        private java.time.LocalDateTime calculatedAt;

    public LoanCalculationResult(BigDecimal monthlyPayment, BigDecimal totalInterest, BigDecimal totalAmount, List<AmortizationScheduleEntry> amortizationSchedule) {
        this.monthlyPayment = monthlyPayment;
        this.totalInterest = totalInterest;
        this.totalAmount = totalAmount;
        this.amortizationSchedule = amortizationSchedule;
    }
        public LoanCalculationResult() {}

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<AmortizationScheduleEntry> getAmortizationSchedule() {
        return amortizationSchedule;
    }

    public void setAmortizationSchedule(List<AmortizationScheduleEntry> amortizationSchedule) {
        this.amortizationSchedule = amortizationSchedule;
    }
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
        public Integer getTermMonths() {
            return termMonths;
        }
        public void setTermMonths(Integer termMonths) {
            this.termMonths = termMonths;
        }
        public BigDecimal getTotalPayments() {
            return totalPayments;
        }
        public void setTotalPayments(BigDecimal totalPayments) {
            this.totalPayments = totalPayments;
        }
        public java.time.LocalDateTime getCalculatedAt() {
            return calculatedAt;
        }
        public void setCalculatedAt(java.time.LocalDateTime calculatedAt) {
            this.calculatedAt = calculatedAt;
        }
        public static Builder builder() {
            return new Builder();
        }
        public static class Builder {
            private final LoanCalculationResult result = new LoanCalculationResult();
            public Builder loanAmount(BigDecimal loanAmount) {
                result.setLoanAmount(loanAmount);
                return this;
            }
            public Builder interestRate(BigDecimal interestRate) {
                result.setInterestRate(interestRate);
                return this;
            }
            public Builder termMonths(Integer termMonths) {
                result.setTermMonths(termMonths);
                return this;
            }
            public Builder monthlyPayment(BigDecimal monthlyPayment) {
                result.setMonthlyPayment(monthlyPayment);
                return this;
            }
            public Builder totalPayments(BigDecimal totalPayments) {
                result.setTotalPayments(totalPayments);
                return this;
            }
            public Builder totalInterest(BigDecimal totalInterest) {
                result.setTotalInterest(totalInterest);
                return this;
            }
            public Builder amortizationSchedule(List<AmortizationScheduleEntry> amortizationSchedule) {
                result.setAmortizationSchedule(amortizationSchedule);
                return this;
            }
            public Builder calculatedAt(java.time.LocalDateTime calculatedAt) {
                result.setCalculatedAt(calculatedAt);
                return this;
            }
            public LoanCalculationResult build() {
                return result;
            }
        }
}
