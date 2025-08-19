package com.fintech.loanportal.service;

import com.fintech.loanportal.entity.AuditLog;
import com.fintech.loanportal.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Override
    public void logEvent(String eventType, String eventDescription, Long userId, HttpServletRequest request) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEventType(eventType);
        auditLog.setEventDescription(eventDescription);
        auditLog.setUserId(userId);
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }
}
