package com.fintech.loanportal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fintech.loanportal.dto.AmortizationScheduleEntry;
import com.fintech.loanportal.dto.LoanCalculationRequest;
import com.fintech.loanportal.dto.LoanCalculationResult;

@Service
public class LoanCalculationServiceImpl extends LoanCalculationService {

    @Override
    public LoanCalculationResult calculateLoan(LoanCalculationRequest request) {
        BigDecimal principal = request.getLoanAmount();
        BigDecimal monthlyInterestRate = request.getInterestRate().divide(new BigDecimal("100")).divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP);
        int termInMonths = request.getLoanTermMonths();

        BigDecimal monthlyPayment = calculateMonthlyPayment(principal, monthlyInterestRate, termInMonths);
        List<AmortizationScheduleEntry> schedule = generateAmortizationSchedule(principal, monthlyInterestRate, termInMonths, monthlyPayment);

        BigDecimal totalInterest = schedule.stream().map(AmortizationScheduleEntry::getInterest).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmount = principal.add(totalInterest);

        return new LoanCalculationResult(monthlyPayment, totalInterest, totalAmount, schedule);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal monthlyInterestRate, int termInMonths) {
        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(termInMonths), 2, RoundingMode.HALF_UP);
        }
        BigDecimal ratePlusOne = monthlyInterestRate.add(BigDecimal.ONE);
        BigDecimal numerator = principal.multiply(monthlyInterestRate).multiply(ratePlusOne.pow(termInMonths));
        BigDecimal denominator = ratePlusOne.pow(termInMonths).subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    private List<AmortizationScheduleEntry> generateAmortizationSchedule(BigDecimal principal, BigDecimal monthlyInterestRate, int termInMonths, BigDecimal monthlyPayment) {
        List<AmortizationScheduleEntry> schedule = new ArrayList<>();
        BigDecimal remainingBalance = principal;

        for (int month = 1; month <= termInMonths; month++) {
            BigDecimal interest = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPaid = monthlyPayment.subtract(interest).setScale(2, RoundingMode.HALF_UP);
            remainingBalance = remainingBalance.subtract(principalPaid);

            if (month == termInMonths && remainingBalance.compareTo(BigDecimal.ZERO) != 0) {
                principalPaid = principalPaid.add(remainingBalance);
                remainingBalance = BigDecimal.ZERO;
            }

            schedule.add(new AmortizationScheduleEntry(month, principalPaid, interest, remainingBalance));
        }
        return schedule;
    }
}
