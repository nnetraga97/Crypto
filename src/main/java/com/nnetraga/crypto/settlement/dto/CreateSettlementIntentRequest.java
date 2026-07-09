package com.nnetraga.crypto.settlement.dto;

import java.math.BigDecimal;

public record CreateSettlementIntentRequest(
        String settlementId,
        String idempotencyKey,
        BigDecimal amount,
        String asset,
        String destinationAddress) {
}