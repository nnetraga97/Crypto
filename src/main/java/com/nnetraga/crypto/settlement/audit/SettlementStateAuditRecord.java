package com.nnetraga.crypto.settlement.audit;

import java.time.Instant;

import com.nnetraga.crypto.settlement.domain.SettlementStatus;

public record SettlementStateAuditRecord(
        String settlementId,
        String idempotencyKey,
        SettlementStatus previousStatus,
        SettlementStatus newStatus,
        Instant occurredAt) {
}
