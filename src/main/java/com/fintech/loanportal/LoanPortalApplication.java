package com.fintech.loanportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Boot application class for the Fintech Loan Portal.
 * 
 * This application provides:
 * - Loan calculation services
 * - User authentication and authorization
 * - Loan application management
 * - Admin dashboard for application review
 * - Comprehensive audit logging
 * 
 * Security features:
 * - JWT-based authentication
 * - Role-based access control
 * - OWASP compliance
 * - Comprehensive input validation
 * 
 * @author Fintech Development Team
 * @version 1.0
 */
@SpringBootApplication
@EnableCaching      // Enable Spring Cache abstraction
@EnableAsync        // Enable asynchronous processing
@EnableMethodSecurity(prePostEnabled = true)  // Enable method-level security
public class LoanPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoanPortalApplication.class, args);
    }
}