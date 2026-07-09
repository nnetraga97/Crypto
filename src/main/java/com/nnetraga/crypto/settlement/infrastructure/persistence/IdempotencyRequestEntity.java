package com.nnetraga.crypto.settlement.infrastructure.persistence;

import java.time.Instant;

import com.nnetraga.crypto.settlement.domain.IdempotencyRequest;
import com.nnetraga.crypto.settlement.domain.IdempotencyStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "idempotency_requests")
public class IdempotencyRequestEntity {
    @Id
    @Column(name = "idempotency_key", nullable = false, length = 120)
    private String idempotencyKey;

    @Column(name = "request_hash", nullable = false, length = 64)
    private String requestHash;

    @Column(name = "settlement_id", nullable = false, length = 80)
    private String settlementId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 40)
    private IdempotencyStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected IdempotencyRequestEntity() {
    }

    private IdempotencyRequestEntity(IdempotencyRequest request) {
        this.idempotencyKey = request.idempotencyKey();
        this.requestHash = request.requestHash();
        this.settlementId = request.settlementId();
        this.status = request.status();
        this.createdAt = request.createdAt();
        this.updatedAt = request.updatedAt();
    }

    public static IdempotencyRequestEntity fromDomain(IdempotencyRequest request) {
        return new IdempotencyRequestEntity(request);
    }

    public IdempotencyRequest toDomain() {
        return new IdempotencyRequest(
                idempotencyKey,
                requestHash,
                settlementId,
                status,
                createdAt,
                updatedAt);
    }
}
