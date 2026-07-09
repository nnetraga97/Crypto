package com.nnetraga.crypto.settlement;

import java.time.Instant;

public record IdempotencyRequest(
        String idempotencyKey,
        String requestHash,
        String settlementId,
        IdempotencyStatus status,
        Instant createdAt,
        Instant updatedAt) {
    public IdempotencyRequest {
        if (isBlank(idempotencyKey)) {
            throw new IllegalArgumentException("idempotencyKey is required");
        }
        if (isBlank(requestHash)) {
            throw new IllegalArgumentException("requestHash is required");
        }
        if (isBlank(settlementId)) {
            throw new IllegalArgumentException("settlementId is required");
        }
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("createdAt is required");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("updatedAt is required");
        }
    }

    public static IdempotencyRequest create(
            String idempotencyKey,
            String requestHash,
            String settlementId) {
        Instant now = Instant.now();
        return new IdempotencyRequest(
                idempotencyKey,
                requestHash,
                settlementId,
                IdempotencyStatus.IN_PROGRESS,
                now,
                now);
    }

    public IdempotencyRequest markCompleted() {
        return new IdempotencyRequest(
                idempotencyKey,
                requestHash,
                settlementId,
                IdempotencyStatus.COMPLETED,
                createdAt,
                Instant.now());
    }

    public IdempotencyRequest markFailed() {
        return new IdempotencyRequest(
                idempotencyKey,
                requestHash,
                settlementId,
                IdempotencyStatus.FAILED,
                createdAt,
                Instant.now());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
