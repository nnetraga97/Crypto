package com.nnetraga.crypto.settlement.application;

import java.time.Instant;

import com.nnetraga.crypto.settlement.domain.SettlementStatus;

public record SettlementStateChangedEvent(
        String settlementId,
        String idempotencyKey,
        SettlementStatus previousStatus,
        SettlementStatus newStatus,
        Instant occurredAt) {
}
