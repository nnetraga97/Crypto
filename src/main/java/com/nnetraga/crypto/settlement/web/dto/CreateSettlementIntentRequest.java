package com.nnetraga.crypto.settlement.web.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateSettlementIntentRequest(
        @NotBlank
        String settlementId,
        @NotBlank
        String idempotencyKey,
        @NotNull
        @Positive
        BigDecimal amount,
        @NotBlank
        String asset,
        @NotBlank
        String destinationAddress) {
}
