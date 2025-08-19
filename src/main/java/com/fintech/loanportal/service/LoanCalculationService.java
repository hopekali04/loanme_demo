package com.fintech.loanportal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fintech.loanportal.dto.AmortizationScheduleEntry;
import com.fintech.loanportal.dto.LoanCalculationRequest;
import com.fintech.loanportal.dto.LoanCalculationResult;

/**
 * Service for loan calculations including monthly payments and amortization schedules.
 * 
 * Features:
 * - Monthly payment calculation using standard loan formulas
 * - Complete amortization schedule generation
 * - Interest rate validation and formatting
 * - Caching for performance optimization
 * - Support for various loan types and terms
 * - Precision handling for financial calculations
 * 
 * Mathematical formulas used:
 * - Monthly Payment: P * [r(1+r)^n] / [(1+r)^n - 1]
 *   where P = principal, r = monthly rate, n = number of payments
 * 
 * All calculations use BigDecimal for precision to avoid floating point errors
 * which are critical in financial applications.
 */
@Service
public class LoanCalculationService {

    private static final int DECIMAL_PLACES = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    // Interest rate bounds for validation
    private static final BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.01");
    private static final BigDecimal MAX_INTEREST_RATE = new BigDecimal("30.00");
    
    // Loan amount bounds
    private static final BigDecimal MIN_LOAN_AMOUNT = new BigDecimal("1000.00");
    private static final BigDecimal MAX_LOAN_AMOUNT = new BigDecimal("10000000.00");

    /**
     * Calculate loan payment details and amortization schedule.
     * Results are cached based on input parameters for performance.
     */
    @Cacheable(value = "loanCalculations", key = "#request.loanAmount + '_' + #request.interestRate + '_' + #request.termMonths")
    public LoanCalculationResult calculateLoan(LoanCalculationRequest request) {
        validateLoanRequest(request);
        
        BigDecimal loanAmount = request.getLoanAmount();
        BigDecimal annualRate = request.getInterestRate();
        int termMonths = request.getLoanTermMonths();
        
        // Convert annual rate to monthly decimal rate
        BigDecimal monthlyRate = annualRate
            .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)  // Convert percentage to decimal
            .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP);   // Convert annual to monthly
        
        // Calculate monthly payment using loan payment formula
        BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, monthlyRate, termMonths);
        
        // Generate complete amortization schedule
        List<AmortizationScheduleEntry> schedule = generateAmortizationSchedule(
            loanAmount, monthlyRate, monthlyPayment, termMonths, request.getStartDate()
        );
        
        // Calculate totals
        BigDecimal totalPayments = monthlyPayment.multiply(BigDecimal.valueOf(termMonths));
        BigDecimal totalInterest = totalPayments.subtract(loanAmount);
        
        return LoanCalculationResult.builder()
            .loanAmount(loanAmount)
            .interestRate(annualRate)
            .termMonths(termMonths)
            .monthlyPayment(monthlyPayment)
            .totalPayments(totalPayments)
            .totalInterest(totalInterest)
            .amortizationSchedule(schedule)
            .calculatedAt(java.time.LocalDateTime.now())
            .build();
    }

    /**
     * Calculate monthly payment using the standard loan payment formula.
     * 
     * Formula: P * [r(1+r)^n] / [(1+r)^n - 1]
     * where P = principal, r = monthly rate, n = number of payments
     * 
     * Special case: If interest rate is 0, monthly payment = principal / term
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            // No interest loan - simple division
            return principal.divide(BigDecimal.valueOf(termMonths), DECIMAL_PLACES, ROUNDING_MODE);
        }
        
        // Calculate (1 + r)^n
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRatePowerN = onePlusRate.pow(termMonths);
        
        // Calculate numerator: r * (1+r)^n
        BigDecimal numerator = monthlyRate.multiply(onePlusRatePowerN);
        
        // Calculate denominator: (1+r)^n - 1
        BigDecimal denominator = onePlusRatePowerN.subtract(BigDecimal.ONE);
        
        // Calculate monthly payment
        BigDecimal monthlyPayment = principal.multiply(numerator.divide(denominator, 8, RoundingMode.HALF_UP));
        
        return monthlyPayment.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }

    /**
     * Generate complete amortization schedule showing payment breakdown for each month.
     * 
     * Each entry includes:
     * - Payment number and date
     * - Interest payment (calculated on remaining balance)
     * - Principal payment (monthly payment - interest payment)
     * - Remaining balance after payment
     * - Cumulative interest paid
     */
    private List<AmortizationScheduleEntry> generateAmortizationSchedule(
            BigDecimal loanAmount, BigDecimal monthlyRate, BigDecimal monthlyPayment, 
            int termMonths, LocalDate startDate) {
        
        List<AmortizationScheduleEntry> schedule = new ArrayList<>();
        BigDecimal remainingBalance = loanAmount;
        BigDecimal cumulativeInterest = BigDecimal.ZERO;
        LocalDate paymentDate = startDate != null ? startDate : LocalDate.now();
        
        for (int paymentNumber = 1; paymentNumber <= termMonths; paymentNumber++) {
            // Calculate interest payment for this month
            BigDecimal interestPayment = remainingBalance.multiply(monthlyRate)
                .setScale(DECIMAL_PLACES, ROUNDING_MODE);
            
            // Calculate principal payment
            BigDecimal principalPayment = monthlyPayment.subtract(interestPayment);
            
            // Adjust for final payment to ensure exact payoff
            if (paymentNumber == termMonths && principalPayment.compareTo(remainingBalance) != 0) {
                principalPayment = remainingBalance;
                monthlyPayment = principalPayment.add(interestPayment);
            }
            
            // Update remaining balance
            remainingBalance = remainingBalance.subtract(principalPayment);
            cumulativeInterest = cumulativeInterest.add(interestPayment);
            
            // Create schedule entry
            AmortizationScheduleEntry entry = AmortizationScheduleEntry.builder()
                .paymentNumber(paymentNumber)
                .paymentDate(paymentDate.plusMonths(paymentNumber - 1))
                .paymentAmount(monthlyPayment)
                .principalPayment(principalPayment)
                .interestPayment(interestPayment)
                .remainingBalance(remainingBalance.max(BigDecimal.ZERO)) // Ensure never negative
                .cumulativeInterest(cumulativeInterest)
                .build();
            
            schedule.add(entry);
        }
        
        return schedule;
    }

    /**
     * Calculate simple interest for short-term loans or interest-only payments.
     */
    public BigDecimal calculateSimpleInterest(BigDecimal principal, BigDecimal annualRate, int days) {
        validateAmount(principal, "Principal");
        validateInterestRate(annualRate);
        
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be positive");
        }
        
        BigDecimal dailyRate = annualRate
            .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)
            .divide(BigDecimal.valueOf(365), 8, RoundingMode.HALF_UP);
        
        return principal.multiply(dailyRate).multiply(BigDecimal.valueOf(days))
            .setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }

    /**
     * Calculate debt-to-income ratio for loan qualification assessment.
     */
    public BigDecimal calculateDebtToIncomeRatio(BigDecimal monthlyPayment, BigDecimal monthlyIncome, 
                                                BigDecimal existingDebt) {
        if (monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monthly income must be positive");
        }
        
        BigDecimal totalDebt = monthlyPayment;
        if (existingDebt != null && existingDebt.compareTo(BigDecimal.ZERO) > 0) {
            totalDebt = totalDebt.add(existingDebt);
        }
        
        return totalDebt.divide(monthlyIncome, 4, RoundingMode.HALF_UP);
    }

    /**
     * Calculate loan affordability based on income and existing obligations.
     */
    public BigDecimal calculateMaxAffordableLoan(BigDecimal monthlyIncome, BigDecimal existingDebt, 
                                                BigDecimal interestRate, int termMonths, 
                                                BigDecimal maxDebtToIncomeRatio) {
        validateAmount(monthlyIncome, "Monthly income");
        validateInterestRate(interestRate);
        
        if (maxDebtToIncomeRatio == null) {
            maxDebtToIncomeRatio = new BigDecimal("0.43"); // Standard 43% DTI limit
        }
        
        // Calculate maximum affordable monthly payment
        BigDecimal maxMonthlyPayment = monthlyIncome.multiply(maxDebtToIncomeRatio);
        if (existingDebt != null && existingDebt.compareTo(BigDecimal.ZERO) > 0) {
            maxMonthlyPayment = maxMonthlyPayment.subtract(existingDebt);
        }
        
        if (maxMonthlyPayment.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO; // Cannot afford any loan
        }
        
        // Calculate maximum loan amount that results in this monthly payment
        BigDecimal monthlyRate = interestRate
            .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)
            .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP);
        
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return maxMonthlyPayment.multiply(BigDecimal.valueOf(termMonths));
        }
        
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRatePowerN = onePlusRate.pow(termMonths);
        BigDecimal numerator = onePlusRatePowerN.subtract(BigDecimal.ONE);
        BigDecimal denominator = monthlyRate.multiply(onePlusRatePowerN);
        
        return maxMonthlyPayment.multiply(numerator.divide(denominator, 8, RoundingMode.HALF_UP))
            .setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }

    /**
     * Get current interest rates for different loan types.
     * In a real system, this would fetch from external rate providers.
     */
    public java.util.Map<String, BigDecimal> getCurrentInterestRates() {
        return java.util.Map.of(
            "PERSONAL", new BigDecimal("8.99"),
            "AUTO", new BigDecimal("4.50"),
            "MORTGAGE", new BigDecimal("6.75"),
            "BUSINESS", new BigDecimal("9.25"),
            "STUDENT", new BigDecimal("5.25"),
            "HOME_EQUITY", new BigDecimal("7.50")
        );
    }

    // Validation Methods
    
    private void validateLoanRequest(LoanCalculationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Loan calculation request cannot be null");
        }
        
        validateAmount(request.getLoanAmount(), "Loan amount");
        validateInterestRate(request.getInterestRate());
        validateLoanTerm(request.getLoanTermMonths());
    }

    private void validateAmount(BigDecimal amount, String fieldName) {
        if (amount == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
        
        if (amount.compareTo(MIN_LOAN_AMOUNT) < 0) {
            throw new IllegalArgumentException(fieldName + " must be at least $" + MIN_LOAN_AMOUNT);
        }
        
        if (amount.compareTo(MAX_LOAN_AMOUNT) > 0) {
            throw new IllegalArgumentException(fieldName + " cannot exceed $" + MAX_LOAN_AMOUNT);
        }
    }

    private void validateInterestRate(BigDecimal interestRate) {
        if (interestRate == null) {
            throw new IllegalArgumentException("Interest rate cannot be null");
        }
        
        if (interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        
        if (interestRate.compareTo(MAX_INTEREST_RATE) > 0) {
            throw new IllegalArgumentException("Interest rate cannot exceed " + MAX_INTEREST_RATE + "%");
        }
    }

    private void validateLoanTerm(Integer termMonths) {
        if (termMonths == null) {
            throw new IllegalArgumentException("Loan term cannot be null");
        }
        
        if (termMonths <= 0) {
            throw new IllegalArgumentException("Loan term must be positive");
        }
        
        if (termMonths > 480) { // 40 years maximum
            throw new IllegalArgumentException("Loan term cannot exceed 480 months (40 years)");
        }
    }
}