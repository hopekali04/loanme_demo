package com.fintech.loanportal.dto;

import java.math.BigDecimal;

public class AmortizationScheduleEntry {

    private int month;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal remainingBalance;
        private int paymentNumber;
        private java.time.LocalDate paymentDate;
        private BigDecimal paymentAmount;
        private BigDecimal cumulativeInterest;

    public AmortizationScheduleEntry(int month, BigDecimal principal, BigDecimal interest, BigDecimal remainingBalance) {
        this.month = month;
        this.principal = principal;
        this.interest = interest;
        this.remainingBalance = remainingBalance;
    }
        public AmortizationScheduleEntry() {}

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
        public int getPaymentNumber() {
            return paymentNumber;
        }
        public void setPaymentNumber(int paymentNumber) {
            this.paymentNumber = paymentNumber;
        }
        public java.time.LocalDate getPaymentDate() {
            return paymentDate;
        }
        public void setPaymentDate(java.time.LocalDate paymentDate) {
            this.paymentDate = paymentDate;
        }
        public BigDecimal getPaymentAmount() {
            return paymentAmount;
        }
        public void setPaymentAmount(BigDecimal paymentAmount) {
            this.paymentAmount = paymentAmount;
        }
        public BigDecimal getCumulativeInterest() {
            return cumulativeInterest;
        }
        public void setCumulativeInterest(BigDecimal cumulativeInterest) {
            this.cumulativeInterest = cumulativeInterest;
        }
        public static Builder builder() {
            return new Builder();
        }
        public static class Builder {
            private final AmortizationScheduleEntry entry = new AmortizationScheduleEntry();
            public Builder paymentNumber(int paymentNumber) {
                entry.setPaymentNumber(paymentNumber);
                return this;
            }
            public Builder paymentDate(java.time.LocalDate paymentDate) {
                entry.setPaymentDate(paymentDate);
                return this;
            }
            public Builder paymentAmount(BigDecimal paymentAmount) {
                entry.setPaymentAmount(paymentAmount);
                return this;
            }
            public Builder principalPayment(BigDecimal principalPayment) {
                entry.setPrincipal(principalPayment);
                return this;
            }
            public Builder interestPayment(BigDecimal interestPayment) {
                entry.setInterest(interestPayment);
                return this;
            }
            public Builder remainingBalance(BigDecimal remainingBalance) {
                entry.setRemainingBalance(remainingBalance);
                return this;
            }
            public Builder cumulativeInterest(BigDecimal cumulativeInterest) {
                entry.setCumulativeInterest(cumulativeInterest);
                return this;
            }
            public AmortizationScheduleEntry build() {
                return entry;
            }
        }
}
