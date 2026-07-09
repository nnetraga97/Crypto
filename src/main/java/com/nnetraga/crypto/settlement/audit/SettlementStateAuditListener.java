package com.nnetraga.crypto.settlement.audit;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.nnetraga.crypto.settlement.application.SettlementStateChangedEvent;

@Component
public class SettlementStateAuditListener {
    private final AuditService auditService;

    public SettlementStateAuditListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @EventListener
    public void recordStateChange(SettlementStateChangedEvent event) {
        auditService.recordSettlementStateChange(new SettlementStateAuditRecord(
                event.settlementId(),
                event.idempotencyKey(),
                event.previousStatus(),
                event.newStatus(),
                event.occurredAt()));
    }
}
