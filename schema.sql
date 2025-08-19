-- Fintech Loan Portal Database Schema
-- PostgreSQL 15+ compatible
-- Designed for high performance, security, and regulatory compliance

-- Enable UUID extension for secure ID generation (optional)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create custom types for better data integrity
CREATE TYPE account_status AS ENUM ('ACTIVE', 'INACTIVE', 'LOCKED', 'EXPIRED', 'SUSPENDED');
CREATE TYPE loan_type AS ENUM ('PERSONAL', 'AUTO', 'MORTGAGE', 'BUSINESS', 'STUDENT', 'HOME_EQUITY');
CREATE TYPE loan_purpose AS ENUM ('DEBT_CONSOLIDATION', 'HOME_IMPROVEMENT', 'MAJOR_PURCHASE', 'MEDICAL_EXPENSES', 'VACATION', 'WEDDING', 'BUSINESS_EXPANSION', 'EDUCATION', 'OTHER');
CREATE TYPE employment_status AS ENUM ('EMPLOYED_FULL_TIME', 'EMPLOYED_PART_TIME', 'SELF_EMPLOYED', 'UNEMPLOYED', 'RETIRED', 'STUDENT');
CREATE TYPE application_status AS ENUM ('DRAFT', 'SUBMITTED', 'UNDER_REVIEW', 'ADDITIONAL_INFO_REQUIRED', 'APPROVED', 'REJECTED', 'WITHDRAWN', 'FUNDED');
CREATE TYPE risk_level AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'VERY_HIGH');

-- =====================================================
-- ROLES TABLE
-- Manages user roles and permissions
-- =====================================================
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_role_name CHECK (name ~ '^[A-Z_]+$')
);

-- Insert default roles
INSERT INTO roles (name, description) VALUES 
('USER', 'Standard user with loan application privileges'),
('ADMIN', 'Administrator with full system access'),
('MANAGER', 'Loan manager with application review privileges'),
('AUDITOR', 'Read-only access for compliance and auditing');

-- =====================================================
-- USERS TABLE
-- Core user information with comprehensive profile data
-- =====================================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    
    -- Basic Information
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL, -- BCrypt hash
    phone_number VARCHAR(20),
    date_of_birth DATE,
    
    -- Address Information
    address VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(50),
    zip_code VARCHAR(20),
    
    -- Employment Information
    employer VARCHAR(100),
    job_title VARCHAR(100),
    annual_income DECIMAL(12,2),
    employment_years INTEGER,
    
    -- Account Security
    account_status account_status NOT NULL DEFAULT 'ACTIVE',
    email_verified BOOLEAN NOT NULL DEFAULT false,
    failed_login_attempts INTEGER NOT NULL DEFAULT 0,
    account_locked_until TIMESTAMP,
    last_login TIMESTAMP,
    password_changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Audit Fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    -- Constraints
    CONSTRAINT chk_user_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_user_phone CHECK (phone_number IS NULL OR phone_number ~ '^\+?[1-9]\d{1,14}$'),
    CONSTRAINT chk_user_income CHECK (annual_income IS NULL OR annual_income >= 0),
    CONSTRAINT chk_user_employment_years CHECK (employment_years IS NULL OR employment_years >= 0),
    CONSTRAINT chk_user_failed_attempts CHECK (failed_login_attempts >= 0),
    CONSTRAINT chk_user_birth_date CHECK (date_of_birth IS NULL OR date_of_birth <= CURRENT_DATE - INTERVAL '18 years')
);

-- User indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(account_status);
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_users_last_login ON users(last_login);

-- =====================================================
-- USER_ROLES TABLE
-- Many-to-many relationship between users and roles
-- =====================================================
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT,
    
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_by) REFERENCES users(id) ON DELETE SET NULL
);

-- =====================================================
-- LOAN_APPLICATIONS TABLE
-- Comprehensive loan application data
-- =====================================================
CREATE TABLE loan_applications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    
    -- Loan Details
    loan_amount DECIMAL(10,2) NOT NULL,
    interest_rate DECIMAL(6,4) NOT NULL,
    loan_term_months INTEGER NOT NULL,
    loan_type loan_type NOT NULL,
    loan_purpose loan_purpose NOT NULL,
    
    -- Calculated Fields
    monthly_payment DECIMAL(10,2),
    total_interest DECIMAL(10,2),
    total_amount DECIMAL(10,2),
    
    -- Applicant Financial Information
    annual_income DECIMAL(12,2) NOT NULL,
    monthly_expenses DECIMAL(10,2) NOT NULL,
    credit_score INTEGER,
    employment_status employment_status NOT NULL,
    employment_years INTEGER,
    employer_name VARCHAR(100),
    existing_debt DECIMAL(10,2),
    
    -- Collateral Information
    has_collateral BOOLEAN NOT NULL DEFAULT false,
    collateral_description TEXT,
    
    -- Additional Information
    additional_notes TEXT,
    
    -- Application Status
    status application_status NOT NULL DEFAULT 'SUBMITTED',
    submitted_at TIMESTAMP,
    reviewed_at TIMESTAMP,
    approved_at TIMESTAMP,
    rejected_at TIMESTAMP,
    review_notes TEXT,
    rejection_reason TEXT,
    
    -- Risk Assessment
    debt_to_income_ratio DECIMAL(5,4),
    risk_level risk_level,
    risk_score DECIMAL(5,2),
    
    -- Processing Information
    assigned_to_user_id BIGINT,
    processing_priority INTEGER DEFAULT 0,
    requires_manual_review BOOLEAN NOT NULL DEFAULT false,
    
    -- Audit Fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    
    -- Foreign Keys
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (assigned_to_user_id) REFERENCES users(id) ON DELETE SET NULL,
    
    -- Constraints
    CONSTRAINT chk_loan_amount CHECK (loan_amount >= 1000.00 AND loan_amount <= 10000000.00),
    CONSTRAINT chk_interest_rate CHECK (interest_rate >= 0.0000 AND interest_rate <= 30.0000),
    CONSTRAINT chk_loan_term CHECK (loan_term_months >= 6 AND loan_term_months <= 480),
    CONSTRAINT chk_annual_income CHECK (annual_income >= 10000.00),
    CONSTRAINT chk_monthly_expenses CHECK (monthly_expenses >= 0.00),
    CONSTRAINT chk_credit_score CHECK (credit_score IS NULL OR (credit_score >= 300 AND credit_score <= 850)),
    CONSTRAINT chk_employment_years CHECK (employment_years IS NULL OR employment_years >= 0),
    CONSTRAINT chk_existing_debt CHECK (existing_debt IS NULL OR existing_debt >= 0),
    CONSTRAINT chk_debt_to_income CHECK (debt_to_income_ratio IS NULL OR debt_to_income_ratio >= 0),
    CONSTRAINT chk_risk_score CHECK (risk_score IS NULL OR risk_score >= 0),
    CONSTRAINT chk_processing_priority CHECK (processing_priority >= 0)
);

-- Loan application indexes for performance
CREATE INDEX idx_loan_app_user ON loan_applications(user_id);
CREATE INDEX idx_loan_app_status ON loan_applications(status);
CREATE INDEX idx_loan_app_created ON loan_applications(created_at);
CREATE INDEX idx_loan_app_risk ON loan_applications(risk_level);
CREATE INDEX idx_loan_app_assigned ON loan_applications(assigned_to_user_id);
CREATE INDEX idx_loan_app_type_status ON loan_applications(loan_type, status);
CREATE INDEX idx_loan_app_amount ON loan_applications(loan_amount);
CREATE INDEX idx_loan_app_submitted ON loan_applications(submitted_at);

-- =====================================================
-- AUDIT_LOGS TABLE
-- Comprehensive audit trail for compliance
-- =====================================================
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    
    -- Event Information
    event_type VARCHAR(50) NOT NULL,
    event_description TEXT NOT NULL,
    table_name VARCHAR(50),
    record_id BIGINT,
    
    -- User and Session Information
    user_id BIGINT,
    user_email VARCHAR(100),
    session_id VARCHAR(100),
    
    -- Request Information
    ip_address INET,
    user_agent TEXT,
    request_method VARCHAR(10),
    request_url TEXT,
    
    -- Data Changes (for data modification events)
    old_values JSONB,
    new_values JSONB,
    
    -- Status and Results
    success BOOLEAN NOT NULL DEFAULT true,
    error_message TEXT,
    
    -- Timing
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duration_ms INTEGER,
    
    -- Foreign Keys
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    
    -- Constraints
    CONSTRAINT chk_audit_event_type CHECK (event_type ~ '^[A-Z_]+$'),
    CONSTRAINT chk_audit_duration CHECK (duration_ms IS NULL OR duration_ms >= 0)
);

-- Audit log indexes for performance and compliance queries
CREATE INDEX idx_audit_event_type ON audit_logs(event_type);
CREATE INDEX idx_audit_user ON audit_logs(user_id);
CREATE INDEX idx_audit_created_at ON audit_logs(created_at);
CREATE INDEX idx_audit_table_record ON audit_logs(table_name, record_id);
CREATE INDEX idx_audit_ip ON audit_logs(ip_address);
CREATE INDEX idx_audit_success ON audit_logs(success);

-- =====================================================
-- INTEREST_RATES TABLE
-- Current interest rates by loan type
-- =====================================================
CREATE TABLE interest_rates (
    id BIGSERIAL PRIMARY KEY,
    loan_type loan_type NOT NULL,
    base_rate DECIMAL(6,4) NOT NULL,
    min_rate DECIMAL(6,4) NOT NULL,
    max_rate DECIMAL(6,4) NOT NULL,
    effective_date DATE NOT NULL,
    expiry_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    
    CONSTRAINT chk_rates_base CHECK (base_rate >= 0.0000 AND base_rate <= 30.0000),
    CONSTRAINT chk_rates_min CHECK (min_rate >= 0.0000 AND min_rate <= base_rate),
    CONSTRAINT chk_rates_max CHECK (max_rate >= base_rate AND max_rate <= 30.0000),
    CONSTRAINT chk_rates_dates CHECK (expiry_date IS NULL OR expiry_date > effective_date)
);

-- Interest rates indexes
CREATE INDEX idx_interest_rates_type ON interest_rates(loan_type);
CREATE INDEX idx_interest_rates_effective ON interest_rates(effective_date);
CREATE INDEX idx_interest_rates_active ON interest_rates(is_active);

-- =====================================================
-- LOAN_DOCUMENTS TABLE
-- Document management for loan applications
-- =====================================================
CREATE TABLE loan_documents (
    id BIGSERIAL PRIMARY KEY,
    loan_application_id BIGINT NOT NULL,
    
    -- Document Information
    document_type VARCHAR(50) NOT NULL,
    document_name VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path TEXT NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    
    -- Document Status
    is_required BOOLEAN NOT NULL DEFAULT false,
    is_verified BOOLEAN NOT NULL DEFAULT false,
    verified_by BIGINT,
    verified_at TIMESTAMP,
    
    -- Upload Information
    uploaded_by BIGINT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- File Security
    file_hash VARCHAR(64), -- SHA-256 hash for integrity
    encryption_key_id VARCHAR(100), -- For encrypted storage
    
    FOREIGN KEY (loan_application_id) REFERENCES loan_applications(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (verified_by) REFERENCES users(id) ON DELETE SET NULL,
    
    CONSTRAINT chk_file_size CHECK (file_size > 0 AND file_size <= 10485760), -- 10MB max
    CONSTRAINT chk_document_type CHECK (document_type ~ '^[A-Z_]+)
);

-- Document indexes
CREATE INDEX idx_loan_docs_application ON loan_documents(loan_application_id);
CREATE INDEX idx_loan_docs_type ON loan_documents(document_type);
CREATE INDEX idx_loan_docs_uploaded ON loan_documents(uploaded_at);
CREATE INDEX idx_loan_docs_verified ON loan_documents(is_verified);

-- =====================================================
-- APPLICATION_STATUS_HISTORY TABLE
-- Track status changes for audit and workflow
-- =====================================================
CREATE TABLE application_status_history (
    id BIGSERIAL PRIMARY KEY,
    loan_application_id BIGINT NOT NULL,
    
    -- Status Change Information
    previous_status application_status,
    new_status application_status NOT NULL,
    change_reason TEXT,
    notes TEXT,
    
    -- Change Metadata
    changed_by BIGINT NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    automated BOOLEAN NOT NULL DEFAULT false,
    
    FOREIGN KEY (loan_application_id) REFERENCES loan_applications(id) ON DELETE CASCADE,
    FOREIGN KEY (changed_by) REFERENCES users(id) ON DELETE RESTRICT
);

-- Status history indexes
CREATE INDEX idx_status_history_application ON application_status_history(loan_application_id);
CREATE INDEX idx_status_history_status ON application_status_history(new_status);
CREATE INDEX idx_status_history_changed ON application_status_history(changed_at);
CREATE INDEX idx_status_history_user ON application_status_history(changed_by);

-- =====================================================
-- SYSTEM_SETTINGS TABLE
-- Configuration settings for the application
-- =====================================================
CREATE TABLE system_settings (
    id BIGSERIAL PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT NOT NULL,
    data_type VARCHAR(20) NOT NULL DEFAULT 'STRING',
    description TEXT,
    is_encrypted BOOLEAN NOT NULL DEFAULT false,
    is_public BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL,
    
    CONSTRAINT chk_data_type CHECK (data_type IN ('STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'JSON'))
);

-- System settings indexes
CREATE INDEX idx_system_settings_key ON system_settings(setting_key);
CREATE INDEX idx_system_settings_public ON system_settings(is_public);

-- =====================================================
-- SESSION_TOKENS TABLE (Optional - for token blacklisting)
-- Track active JWT tokens for security
-- =====================================================
CREATE TABLE session_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL, -- SHA-256 hash of token
    token_type VARCHAR(20) NOT NULL DEFAULT 'ACCESS',
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    revoked_at TIMESTAMP,
    revoked_by BIGINT,
    ip_address INET,
    user_agent TEXT,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (revoked_by) REFERENCES users(id) ON DELETE SET NULL,
    
    CONSTRAINT chk_token_type CHECK (token_type IN ('ACCESS', 'REFRESH'))
);

-- Session token indexes
CREATE INDEX idx_session_tokens_user ON session_tokens(user_id);
CREATE INDEX idx_session_tokens_hash ON session_tokens(token_hash);
CREATE INDEX idx_session_tokens_expires ON session_tokens(expires_at);
CREATE INDEX idx_session_tokens_revoked ON session_tokens(revoked_at);

-- =====================================================
-- VIEWS FOR COMMON QUERIES
-- =====================================================

-- User summary view with role information
CREATE VIEW user_summary AS
SELECT 
    u.id,
    u.first_name,
    u.last_name,
    u.email,
    u.phone_number,
    u.account_status,
    u.email_verified,
    u.last_login,
    u.created_at,
    STRING_AGG(r.name, ', ') AS roles,
    COUNT(la.id) AS total_applications,
    COUNT(CASE WHEN la.status = 'APPROVED' THEN 1 END) AS approved_applications
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
LEFT JOIN loan_applications la ON u.id = la.user_id
GROUP BY u.id, u.first_name, u.last_name, u.email, u.phone_number, 
         u.account_status, u.email_verified, u.last_login, u.created_at;

-- Loan application dashboard view
CREATE VIEW loan_application_dashboard AS
SELECT 
    la.id,
    la.user_id,
    CONCAT(u.first_name, ' ', u.last_name) AS applicant_name,
    u.email AS applicant_email,
    la.loan_amount,
    la.loan_type,
    la.status,
    la.risk_level,
    la.submitted_at,
    la.requires_manual_review,
    CASE 
        WHEN la.submitted_at IS NOT NULL THEN 
            EXTRACT(DAYS FROM (CURRENT_TIMESTAMP - la.submitted_at))
        ELSE 0
    END AS days_pending,
    CONCAT(reviewer.first_name, ' ', reviewer.last_name) AS assigned_reviewer
FROM loan_applications la
JOIN users u ON la.user_id = u.id
LEFT JOIN users reviewer ON la.assigned_to_user_id = reviewer.id;

-- =====================================================
-- FUNCTIONS AND TRIGGERS
-- =====================================================

-- Function to update timestamp on record changes
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$ LANGUAGE plpgsql;

-- Triggers for automatic timestamp updates
CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_loan_applications_updated_at 
    BEFORE UPDATE ON loan_applications 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Function to automatically log application status changes
CREATE OR REPLACE FUNCTION log_application_status_change()
RETURNS TRIGGER AS $
BEGIN
    IF OLD.status IS DISTINCT FROM NEW.status THEN
        INSERT INTO application_status_history (
            loan_application_id,
            previous_status,
            new_status,
            changed_by,
            automated
        ) VALUES (
            NEW.id,
            OLD.status,
            NEW.status,
            COALESCE(NEW.assigned_to_user_id, 1), -- Default to system user
            true
        );
    END IF;
    RETURN NEW;
END;
$ LANGUAGE plpgsql;

-- Trigger for automatic status change logging
CREATE TRIGGER log_loan_status_changes
    AFTER UPDATE ON loan_applications
    FOR EACH ROW EXECUTE FUNCTION log_application_status_change();

-- Function to clean up expired tokens
CREATE OR REPLACE FUNCTION cleanup_expired_tokens()
RETURNS void AS $
BEGIN
    DELETE FROM session_tokens 
    WHERE expires_at < CURRENT_TIMESTAMP 
    AND revoked_at IS NULL;
END;
$ LANGUAGE plpgsql;

-- =====================================================
-- SECURITY POLICIES (Row Level Security)
-- Enable RLS for sensitive tables
-- =====================================================

-- Enable RLS on users table
ALTER TABLE users ENABLE ROW LEVEL SECURITY;

-- Policy: Users can only see their own data
CREATE POLICY users_own_data ON users
    FOR ALL TO authenticated_user
    USING (id = current_setting('app.current_user_id')::BIGINT);

-- Policy: Admins can see all users
CREATE POLICY users_admin_access ON users
    FOR ALL TO admin_role
    USING (true);

-- Enable RLS on loan applications
ALTER TABLE loan_applications ENABLE ROW LEVEL SECURITY;

-- Policy: Users can only see their own applications
CREATE POLICY applications_own_data ON loan_applications
    FOR ALL TO authenticated_user
    USING (user_id = current_setting('app.current_user_id')::BIGINT);

-- Policy: Loan officers can see assigned applications
CREATE POLICY applications_assigned_access ON loan_applications
    FOR ALL TO loan_officer_role
    USING (assigned_to_user_id = current_setting('app.current_user_id')::BIGINT);

-- =====================================================
-- INITIAL DATA SETUP
-- =====================================================

-- Insert default interest rates
INSERT INTO interest_rates (loan_type, base_rate, min_rate, max_rate, effective_date) VALUES 
('PERSONAL', 8.9900, 5.9900, 24.9900, CURRENT_DATE),
('AUTO', 4.5000, 3.0000, 15.0000, CURRENT_DATE),
('MORTGAGE', 6.7500, 6.0000, 8.5000, CURRENT_DATE),
('BUSINESS', 9.2500, 7.0000, 18.0000, CURRENT_DATE),
('STUDENT', 5.2500, 4.0000, 12.0000, CURRENT_DATE),
('HOME_EQUITY', 7.5000, 6.5000, 12.0000, CURRENT_DATE);

-- Insert system settings
INSERT INTO system_settings (setting_key, setting_value, data_type, description, is_public) VALUES 
('max_loan_amount', '500000.00', 'DECIMAL', 'Maximum loan amount allowed', true),
('min_loan_amount', '1000.00', 'DECIMAL', 'Minimum loan amount allowed', true),
('max_loan_term_months', '360', 'INTEGER', 'Maximum loan term in months', true),
('min_credit_score', '600', 'INTEGER', 'Minimum credit score for approval', false),
('max_debt_to_income_ratio', '0.43', 'DECIMAL', 'Maximum debt-to-income ratio', false),
('auto_approval_enabled', 'true', 'BOOLEAN', 'Enable automatic loan approvals', false),
('require_document_verification', 'true', 'BOOLEAN', 'Require document verification', false);

-- =====================================================
-- PERFORMANCE OPTIMIZATIONS
-- =====================================================

-- Partial indexes for better performance
CREATE INDEX idx_users_active_email ON users(email) WHERE account_status = 'ACTIVE';
CREATE INDEX idx_applications_pending ON loan_applications(created_at, risk_level) 
    WHERE status IN ('SUBMITTED', 'UNDER_REVIEW');
CREATE INDEX idx_applications_high_priority ON loan_applications(submitted_at) 
    WHERE processing_priority > 0 AND status = 'SUBMITTED';

-- Analyze tables for query optimizer
ANALYZE users;
ANALYZE loan_applications;
ANALYZE audit_logs;
ANALYZE roles;
ANALYZE user_roles;

-- =====================================================
-- BACKUP AND MAINTENANCE RECOMMENDATIONS
-- =====================================================

-- Create maintenance procedures
CREATE OR REPLACE FUNCTION maintain_audit_logs(retention_days INTEGER DEFAULT 2555) -- 7 years
RETURNS void AS $
BEGIN
    -- Archive old audit logs (optional - move to archive table)
    -- DELETE FROM audit_logs WHERE created_at < CURRENT_DATE - INTERVAL '1 day' * retention_days;
    
    -- For now, just log the maintenance
    INSERT INTO audit_logs (event_type, event_description, success) 
    VALUES ('MAINTENANCE', 'Audit log maintenance completed', true);
END;
$ LANGUAGE plpgsql;

-- Comment: Run this monthly via cron job
-- SELECT maintain_audit_logs();

-- Comment: Regular backup recommendations
-- 1. Daily full database backups
-- 2. Continuous WAL archiving for point-in-time recovery
-- 3. Test restore procedures monthly
-- 4. Monitor disk space and performance metrics
-- 5. Regular VACUUM and ANALYZE operations

-- End of schema definition