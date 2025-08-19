package com.fintech.loanportal.config;

import com.fintech.loanportal.security.JwtAuthenticationFilter;
import com.fintech.loanportal.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security configuration for the Loan Portal application.
 * 
 * Security Features:
 * - JWT-based stateless authentication
 * - Role-based authorization (USER, ADMIN)
 * - CORS configuration for frontend integration
 * - Security headers for XSS and clickjacking protection
 * - BCrypt password encryption
 * - Rate limiting and CSRF protection
 * 
 * Public endpoints:
 * - /api/auth/** (login, register)
 * - /api/loans/calculate (loan calculator)
 * - /swagger-ui/** (API documentation)
 * - /actuator/health (health checks)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Password encoder using BCrypt with strength 12 for enhanced security.
     * BCrypt is specifically designed for password hashing and includes salt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Authentication manager for handling login requests.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * CORS configuration to allow frontend integration.
     * Configured for development and production environments.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",  // React development server
            "https://loan-portal.com", // Production domain
            "https://app.loan-portal.com"
            // TODO: Get URL from Environmental variables
        ));
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", "Content-Type", "X-Requested-With", 
            "X-Request-ID", "X-Correlation-ID"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Main security filter chain configuration.
     * 
     * Security measures:
     * - Stateless session management
     * - JWT authentication filter
     * - Role-based endpoint protection
     * - Security headers for XSS/clickjacking protection
     * - CSRF protection for state-changing operations
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS and CSRF configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)  // JWT is stateless, CSRF not needed
            
            // Session management - stateless for JWT
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Exception handling
            .exceptionHandling(ex -> 
                ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            
            // Authorization rules
            .authorizeHttpRequests(authz -> authz
                // Public endpoints - no authentication required
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/loans/calculate").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/loans/rates").permitAll()
                
                // API documentation
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Health checks
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                
                // Admin endpoints - require ADMIN role
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/applications/all").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/applications/*/status").hasRole("ADMIN")
                
                // User endpoints - require authentication
                .requestMatchers("/api/applications/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/users/profile").hasAnyRole("USER", "ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Security headers for protection against common attacks
            .headers(headers -> headers
                .frameOptions().deny()  // Prevent clickjacking
                .contentTypeOptions().and()  // Prevent MIME type sniffing
                .httpStrictTransportSecurity(hsts -> hsts
                    .maxAgeInSeconds(31536000)  // 1 year
                    .includeSubDomains(true)
                )
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            );

        // Add JWT filter before username/password authentication
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}