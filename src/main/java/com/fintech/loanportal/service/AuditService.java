package com.fintech.loanportal.service;

import jakarta.servlet.http.HttpServletRequest;

public interface AuditService {
    void logEvent(String eventType, String eventDescription, Long userId, HttpServletRequest request);
}
