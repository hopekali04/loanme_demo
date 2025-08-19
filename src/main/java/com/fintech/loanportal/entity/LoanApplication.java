package com.fintech.loanportal.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Loan Application entity representing a user's loan request.
 * 
 * Features:
 * - Comprehensive application data capture
 * - Status tracking with audit trail
 * - Risk assessment fields
 * - Integration with loan calculation service
 * - Support for different loan types
 * - Automated workflow processing
 * 
 * Business Rules:
 * - Applications must be submitted by authenticated users
 * - Status changes are tracked and audited
 * - Credit score affects approval decisions
 * - Employment verification required for approval
 * - Loan amounts have minimum and maximum limits
 */
@Entity
@Table(name = "loan_applications",
       indexes = {
           @Index(name = "idx_application_user", columnList = "user_id"),
           @Index(name = "idx_application_status", columnList = "status"),
           @Index(name = "idx_application_created", columnList = "created_at"),
           @Index(name = "idx_application_type_status", columnList = "loan_type, status")
       })
@EntityListeners(AuditingEntityListener.class)
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship to User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_loan_application_user"))
    private User user;

    // Loan Details
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "1000.00", message = "Minimum loan amount is $1,000")
    @DecimalMax(value = "500000.00", message = "Maximum loan amount is $500,000")
    @Digits(integer = 8, fraction = 2, message = "Invalid loan amount format")
    @Column(name = "loan_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal loanAmount;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.01", message = "Interest rate must be positive")
    @DecimalMax(value = "30.00", message = "Interest rate cannot exceed 30%")
    @Digits(integer = 2, fraction = 4, message = "Invalid interest rate format")
    @Column(name = "interest_rate", nullable = false, precision = 6, scale = 4)
    private BigDecimal interestRate;

    @NotNull(message = "Loan term is required")
    @Min(value = 6, message = "Minimum loan term is 6 months")
    @Max(value = 360, message = "Maximum loan term is 360 months")
    @Column(name = "loan_term_months", nullable = false)
    private Integer loanTermMonths;

    @NotNull(message = "Loan type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false, length = 20)
    private LoanType loanType;

    @NotNull(message = "Loan purpose is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_purpose", nullable = false, length = 30)
    private LoanPurpose loanPurpose;

    // Calculated Fields (from loan calculator)
    @Column(name = "monthly_payment", precision = 10, scale = 2)
    private BigDecimal monthlyPayment;

    @Column(name = "total_interest", precision = 10, scale = 2)
    private BigDecimal totalInterest;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Applicant Financial Information
    @NotNull(message = "Annual income is required")
    @DecimalMin(value = "10000.00", message = "Minimum annual income is $10,000")
    @DecimalMax(value = "10000000.00", message = "Annual income seems unrealistic")
    @Digits(integer = 10, fraction = 2, message = "Invalid income format")
    @Column(name = "annual_income", nullable = false, precision = 12, scale = 2)
    private BigDecimal annualIncome;

    @NotNull(message = "Monthly expenses are required")
    @DecimalMin(value = "0.00", message = "Monthly expenses cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid monthly expenses format")
    @Column(name = "monthly_expenses", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyExpenses;

    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score cannot exceed 850")
    @Column(name = "credit_score")
    private Integer creditScore;

    @NotNull(message = "Employment status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", nullable = false, length = 20)
    private EmploymentStatus employmentStatus;

    @Min(value = 0, message = "Employment years cannot be negative")
    @Max(value = 50, message = "Employment years seems unrealistic")
    @Column(name = "employment_years")
    private Integer employmentYears;

    // Additional Information
    @Size(max = 100, message = "Employer name must not exceed 100 characters")
    @Column(name = "employer_name", length = 100)
    private String employerName;

    @Column(name = "existing_debt", precision = 10, scale = 2)
    private BigDecimal existingDebt;

    @Column(name = "has_collateral", nullable = false)
    private Boolean hasCollateral = false;

    @Size(max = 500, message = "Collateral description must not exceed 500 characters")
    @Column(name = "collateral_description", length = 500)
    private String collateralDescription;

    @Size(max = 1000, message = "Additional notes must not exceed 1000 characters")
    @Column(name = "additional_notes", length = 1000)
    private String additionalNotes;

    // Application Status and Processing
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Size(max = 500, message = "Review notes must not exceed 500 characters")
    @Column(name = "review_notes", length = 500)
    private String reviewNotes;

    @Size(max = 500, message = "Rejection reason must not exceed 500 characters")
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    // Risk Assessment
    @Column(name = "debt_to_income_ratio", precision = 5, scale = 4)
    private BigDecimal debtToIncomeRatio;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", length = 10)
    private RiskLevel riskLevel;

    @Column(name = "risk_score", precision = 5, scale = 2)
    private BigDecimal riskScore;

    // Processing Information
    @Column(name = "assigned_to_user_id")
    private Long assignedToUserId;

    @Column(name = "processing_priority")
    private Integer processingPriority = 0;

    @Column(name = "requires_manual_review", nullable = false)
    private Boolean requiresManualReview = false;

    // Audit Fields
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    // Enums
    public enum LoanType {
        PERSONAL,
        AUTO,
        MORTGAGE,
        BUSINESS,
        STUDENT,
        HOME_EQUITY
    }

    public enum LoanPurpose {
        DEBT_CONSOLIDATION,
        HOME_IMPROVEMENT,
        MAJOR_PURCHASE,
        MEDICAL_EXPENSES,
        VACATION,
        WEDDING,
        BUSINESS_EXPANSION,
        EDUCATION,
        OTHER
    }

    public enum EmploymentStatus {
        EMPLOYED_FULL_TIME,
        EMPLOYED_PART_TIME,
        SELF_EMPLOYED,
        UNEMPLOYED,
        RETIRED,
        STUDENT
    }

    public enum ApplicationStatus {
        DRAFT,
        SUBMITTED,
        UNDER_REVIEW,
        ADDITIONAL_INFO_REQUIRED,
        APPROVED,
        REJECTED,
        WITHDRAWN,
        FUNDED
    }

    public enum RiskLevel {
        LOW,
        MEDIUM,
        HIGH,
        VERY_HIGH
    }

    // Constructors
    public LoanApplication() {}

    public LoanApplication(User user, BigDecimal loanAmount, BigDecimal interestRate, 
                          Integer loanTermMonths, LoanType loanType) {
        this.user = user;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.loanTermMonths = loanTermMonths;
        this.loanType = loanType;
        this.submittedAt = LocalDateTime.now();
    }

    // Business Logic Methods
    
    /**
     * Calculate debt-to-income ratio based on monthly payment and income.
     */
    public void calculateDebtToIncomeRatio() {
        if (monthlyPayment != null && annualIncome != null && annualIncome.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal monthlyIncome = annualIncome.divide(BigDecimal.valueOf(12), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal totalMonthlyDebt = monthlyPayment;
            
            if (monthlyExpenses != null) {
                totalMonthlyDebt = totalMonthlyDebt.add(monthlyExpenses);
            }
            
            this.debtToIncomeRatio = totalMonthlyDebt.divide(monthlyIncome, 4, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * Assess risk level based on various factors.
     */
    public void assessRiskLevel() {
        int riskPoints = 0;
        
        // Credit score risk
        if (creditScore != null) {
            if (creditScore < 600) riskPoints += 30;
            else if (creditScore < 650) riskPoints += 20;
            else if (creditScore < 700) riskPoints += 10;
        } else {
            riskPoints += 25; // No credit score is risky
        }
        
        // Debt-to-income ratio risk
        if (debtToIncomeRatio != null) {
            if (debtToIncomeRatio.compareTo(BigDecimal.valueOf(0.43)) > 0) riskPoints += 25;
            else if (debtToIncomeRatio.compareTo(BigDecimal.valueOf(0.36)) > 0) riskPoints += 15;
            else if (debtToIncomeRatio.compareTo(BigDecimal.valueOf(0.28)) > 0) riskPoints += 5;
        }
        
        // Employment status risk
        switch (employmentStatus) {
            case UNEMPLOYED -> riskPoints += 40;
            case EMPLOYED_PART_TIME -> riskPoints += 15;
            case SELF_EMPLOYED -> riskPoints += 10;
            case EMPLOYED_FULL_TIME -> riskPoints += 0;
            case RETIRED -> riskPoints += 5;
            case STUDENT -> riskPoints += 20;
        }
        
        // Employment years risk
        if (employmentYears != null && employmentYears < 2) {
            riskPoints += 10;
        }
        
        // Loan amount relative to income
        if (annualIncome != null && loanAmount != null) {
            BigDecimal loanToIncomeRatio = loanAmount.divide(annualIncome, 4, BigDecimal.ROUND_HALF_UP);
            if (loanToIncomeRatio.compareTo(BigDecimal.valueOf(0.5)) > 0) riskPoints += 15;
            else if (loanToIncomeRatio.compareTo(BigDecimal.valueOf(0.3)) > 0) riskPoints += 8;
        }
        
        // Set risk level and score
        this.riskScore = BigDecimal.valueOf(riskPoints);
        
        if (riskPoints >= 60) {
            this.riskLevel = RiskLevel.VERY_HIGH;
            this.requiresManualReview = true;
        } else if (riskPoints >= 40) {
            this.riskLevel = RiskLevel.HIGH;
            this.requiresManualReview = true;
        } else if (riskPoints >= 25) {
            this.riskLevel = RiskLevel.MEDIUM;
        } else {
            this.riskLevel = RiskLevel.LOW;
        }
    }

    /**
     * Submit the application for review.
     */
    public void submit() {
        if (this.status == ApplicationStatus.DRAFT) {
            this.status = ApplicationStatus.SUBMITTED;
            this.submittedAt = LocalDateTime.now();
            calculateDebtToIncomeRatio();
            assessRiskLevel();
        }
    }

    /**
     * Approve the loan application.
     */
    public void approve(String reviewNotes) {
        this.status = ApplicationStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
        this.reviewedAt = LocalDateTime.now();
        this.reviewNotes = reviewNotes;
    }

    /**
     * Reject the loan application.
     */
    public void reject(String rejectionReason) {
        this.status = ApplicationStatus.REJECTED;
        this.rejectedAt = LocalDateTime.now();
        this.reviewedAt = LocalDateTime.now();
        this.rejectionReason = rejectionReason;
    }

    /**
     * Check if application can be auto-approved based on risk assessment.
     */
    public boolean isEligibleForAutoApproval() {
        return riskLevel == RiskLevel.LOW && 
               creditScore != null && creditScore >= 750 &&
               debtToIncomeRatio != null && debtToIncomeRatio.compareTo(BigDecimal.valueOf(0.28)) <= 0 &&
               employmentStatus == EmploymentStatus.EMPLOYED_FULL_TIME &&
               employmentYears != null && employmentYears >= 2;
    }

    /**
     * Get application age in days.
     */
    public long getApplicationAgeInDays() {
        if (submittedAt != null) {
            return java.time.Duration.between(submittedAt, LocalDateTime.now()).toDays();
        }
        return 0;
    }

    /**
     * Check if application is overdue for review.
     */
    public boolean isOverdueForReview() {
        return getApplicationAgeInDays() > 3 && 
               (status == ApplicationStatus.SUBMITTED || status == ApplicationStatus.UNDER_REVIEW);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public Integer getLoanTermMonths() { return loanTermMonths; }
    public void setLoanTermMonths(Integer loanTermMonths) { this.loanTermMonths = loanTermMonths; }

    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }

    public LoanPurpose getLoanPurpose() { return loanPurpose; }
    public void setLoanPurpose(LoanPurpose loanPurpose) { this.loanPurpose = loanPurpose; }

    public BigDecimal getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(BigDecimal monthlyPayment) { this.monthlyPayment = monthlyPayment; }

    public BigDecimal getTotalInterest() { return totalInterest; }
    public void setTotalInterest(BigDecimal totalInterest) { this.totalInterest = totalInterest; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(BigDecimal annualIncome) { this.annualIncome = annualIncome; }

    public BigDecimal getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public EmploymentStatus getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(EmploymentStatus employmentStatus) { this.employmentStatus = employmentStatus; }

    public Integer getEmploymentYears() { return employmentYears; }
    public void setEmploymentYears(Integer employmentYears) { this.employmentYears = employmentYears; }

    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }

    public BigDecimal getExistingDebt() { return existingDebt; }
    public void setExistingDebt(BigDecimal existingDebt) { this.existingDebt = existingDebt; }

    public Boolean getHasCollateral() { return hasCollateral; }
    public void setHasCollateral(Boolean hasCollateral) { this.hasCollateral = hasCollateral; }

    public String getCollateralDescription() { return collateralDescription; }
    public void setCollateralDescription(String collateralDescription) { this.collateralDescription = collateralDescription; }

    public String getAdditionalNotes() { return additionalNotes; }
    public void setAdditionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public LocalDateTime getRejectedAt() { return rejectedAt; }
    public void setRejectedAt(LocalDateTime rejectedAt) { this.rejectedAt = rejectedAt; }

    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public BigDecimal getDebtToIncomeRatio() { return debtToIncomeRatio; }
    public void setDebtToIncomeRatio(BigDecimal debtToIncomeRatio) { this.debtToIncomeRatio = debtToIncomeRatio; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public BigDecimal getRiskScore() { return riskScore; }
    public void setRiskScore(BigDecimal riskScore) { this.riskScore = riskScore; }

    public Long getAssignedToUserId() { return assignedToUserId; }
    public void setAssignedToUserId(Long assignedToUserId) { this.assignedToUserId = assignedToUserId; }

    public Integer getProcessingPriority() { return processingPriority; }
    public void setProcessingPriority(Integer processingPriority) { this.processingPriority = processingPriority; }

    public Boolean getRequiresManualReview() { return requiresManualReview; }
    public void setRequiresManualReview(Boolean requiresManualReview) { this.requiresManualReview = requiresManualReview; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanApplication that = (LoanApplication) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
                "id=" + id +
                ", loanAmount=" + loanAmount +
                ", loanType=" + loanType +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}