package com.nnetraga.crypto.settlement.web;

import java.time.Instant;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.nnetraga.crypto.settlement.audit.ApiAuditRecord;
import com.nnetraga.crypto.settlement.audit.AuditService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiAuditInterceptor implements HandlerInterceptor {
    private final AuditService auditService;

    public ApiAuditInterceptor(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {
        String message = exception == null ? "completed" : exception.getClass().getSimpleName();
        auditService.recordApiRequest(new ApiAuditRecord(
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                message,
                Instant.now()));
    }
}
