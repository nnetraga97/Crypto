package com.nnetraga.crypto.settlement;

import java.math.BigDecimal;
import java.time.Instant;

public record SettlementIntent(
        String settlementId,
        String idempotencyKey,
        BigDecimal amount,
        String asset,
        String destinationAddress,
        SettlementStatus status,
        String chainTransactionHash,
        Instant createdAt,
        Instant updatedAt) {
    public SettlementIntent {
        if (isBlank(settlementId)) {
            throw new IllegalArgumentException("settlementId is required");
        }
        if (isBlank(idempotencyKey)) {
            throw new IllegalArgumentException("idempotencyKey is required");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (isBlank(asset)) {
            throw new IllegalArgumentException("asset is required");
        }
        if (isBlank(destinationAddress)) {
            throw new IllegalArgumentException("destinationAddress is required");
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

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static SettlementIntent create(
            String settlementId,
            String idempotencyKey,
            BigDecimal amount,
            String asset,
            String destinationAddress) {
        Instant now = Instant.now();
        return new SettlementIntent(
                settlementId,
                idempotencyKey,
                amount,
                asset,
                destinationAddress,
                SettlementStatus.CREATED,
                null,
                now,
                now);
    }

    public SettlementIntent markSubmitted(ChainSubmissionResult submissionResult) {
        if (submissionResult == null) {
            throw new IllegalArgumentException("submissionResult is required");
        }
        if (status != SettlementStatus.CREATED && status != SettlementStatus.VALIDATED) {
            throw new IllegalStateException(
                    "SettlementIntent must be CREATED or VALIDATED to submit. Current status: " + status);
        }
        return new SettlementIntent(
                settlementId,
                idempotencyKey,
                amount,
                asset,
                destinationAddress,
                SettlementStatus.SUBMITTED,
                submissionResult.transactionHash(),
                createdAt,
                submissionResult.submittedAt());
    }
}
