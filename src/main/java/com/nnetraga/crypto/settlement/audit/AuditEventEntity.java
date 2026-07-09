package com.nnetraga.crypto.settlement.audit;

import java.time.Instant;

import com.nnetraga.crypto.settlement.domain.SettlementStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_events")
public class AuditEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_event_id")
    private Long auditEventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 60)
    private AuditEventType eventType;

    @Column(name = "subject_type", nullable = false, length = 80)
    private String subjectType;

    @Column(name = "subject_id", length = 120)
    private String subjectId;

    @Column(name = "idempotency_key", length = 120)
    private String idempotencyKey;

    @Column(name = "http_method", length = 20)
    private String httpMethod;

    @Column(name = "request_path", length = 400)
    private String requestPath;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 40)
    private SettlementStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", length = 40)
    private SettlementStatus newStatus;

    @Column(name = "message", length = 500)
    private String message;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    protected AuditEventEntity() {
    }

    private AuditEventEntity(
            AuditEventType eventType,
            String subjectType,
            String subjectId,
            String idempotencyKey,
            String httpMethod,
            String requestPath,
            Integer responseStatus,
            SettlementStatus previousStatus,
            SettlementStatus newStatus,
            String message,
            Instant occurredAt) {
        this.eventType = eventType;
        this.subjectType = subjectType;
        this.subjectId = subjectId;
        this.idempotencyKey = idempotencyKey;
        this.httpMethod = httpMethod;
        this.requestPath = requestPath;
        this.responseStatus = responseStatus;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.message = message;
        this.occurredAt = occurredAt;
    }

    public static AuditEventEntity apiRequest(ApiAuditRecord record) {
        return new AuditEventEntity(
                AuditEventType.API_REQUEST,
                "API",
                null,
                null,
                record.httpMethod(),
                record.requestPath(),
                record.responseStatus(),
                null,
                null,
                record.message(),
                record.occurredAt());
    }

    public static AuditEventEntity settlementStateChange(SettlementStateAuditRecord record) {
        return new AuditEventEntity(
                AuditEventType.SETTLEMENT_STATE_CHANGED,
                "SETTLEMENT_INTENT",
                record.settlementId(),
                record.idempotencyKey(),
                null,
                null,
                null,
                record.previousStatus(),
                record.newStatus(),
                "Settlement status changed",
                record.occurredAt());
    }
}
