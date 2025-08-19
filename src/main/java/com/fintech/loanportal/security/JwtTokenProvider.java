package com.fintech.loanportal.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.file.attribute.UserPrincipal;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT Token Provider for generating and validating JWT tokens.
 * 
 * Features:
 * - Secure token generation with HS512 algorithm
 * - Token expiration management
 * - User roles embedded in token claims
 * - Comprehensive token validation
 * - Secure key management
 * 
 * Security considerations:
 * - Uses cryptographically secure keys
 * - Tokens are stateless and self-contained
 * - Short expiration times to minimize risk
 * - Comprehensive error handling and logging
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey jwtSecret;
    
    @Value("${app.jwt.expiration:86400}")  // 24 hours default
    private int jwtExpirationInSeconds;

    @Value("${app.jwt.refresh-expiration:604800}")  // 7 days default
    private int jwtRefreshExpirationInSeconds;

    /**
     * Constructor initializes JWT secret key.
     * In production, this should be loaded from secure configuration.
     */
    public JwtTokenProvider(@Value("${app.jwt.secret:}") String jwtSecretString) {
        if (jwtSecretString.isEmpty()) {
            // Generate secure key if not provided (development only)
            this.jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            logger.warn("Using generated JWT secret key. Configure app.jwt.secret for production!");
        } else {
            // Use provided secret key
            this.jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes());
        }
    }

    /**
     * Generate JWT access token for authenticated user.
     * 
     * Token contains:
     * - User email as subject
     * - User roles as claims
     * - Expiration time
     * - Issued at timestamp
     */
    public String generateAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + (jwtExpirationInSeconds * 1000L));

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .claim("sub", userPrincipal.getName())
                .claim("userId", userPrincipal.getName()) // Use getName() if getId() is not available
                .claim("roles", roles)
                .claim("tokenType", "ACCESS")
                .issuedAt(java.time.Instant.now())
                .setExpiration(expiryDate)
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Generate JWT refresh token for token renewal.
     * Refresh tokens have longer expiration times but limited scope.
     */
    public String generateRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + (jwtRefreshExpirationInSeconds * 1000L));

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("userId", userPrincipal.getId())
                .claim("tokenType", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extract user email from JWT token.
     */
    public String getUserEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Extract user ID from JWT token.
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    /**
     * Extract user roles from JWT token.
     */
    public String getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", String.class);
    }

    /**
     * Get token expiration date.
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    /**
     * Check if token is expired.
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.warn("Could not determine token expiration: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Validate JWT token with comprehensive error handling.
     * 
     * Validation includes:
     * - Signature verification
     * - Expiration check
     * - Token structure validation
     * - Token type verification (for access tokens)
     */
    public boolean validateToken(String authToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(authToken)
                    .getBody();

            // Verify this is an access token
            String tokenType = claims.get("tokenType", String.class);
            if (!"ACCESS".equals(tokenType)) {
                logger.warn("Invalid token type: {}", tokenType);
                return false;
            }

            return true;

        } catch (SecurityException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        } catch (Exception ex) {
            logger.error("JWT token validation error: {}", ex.getMessage());
        }

        return false;
    }

    /**
     * Validate refresh token with specific checks for refresh token type.
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // Verify this is a refresh token
            String tokenType = claims.get("tokenType", String.class);
            if (!"REFRESH".equals(tokenType)) {
                logger.warn("Invalid refresh token type: {}", tokenType);
                return false;
            }

            return true;

        } catch (Exception ex) {
            logger.error("Refresh token validation error: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Get remaining time until token expiration in seconds.
     */
    public long getTokenRemainingTime(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            long remainingTime = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            return Math.max(0, remainingTime);
        } catch (Exception e) {
            logger.warn("Could not determine token remaining time: {}", e.getMessage());
            return 0;
        }
    }
}