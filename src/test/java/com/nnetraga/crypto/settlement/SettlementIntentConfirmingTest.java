package com.nnetraga.crypto.settlement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class SettlementIntentConfirmingTest {
    @Test
    public void submittedIntentCanBeMarkedConfirming() {
        String settlementId = "settlement123";
        String idempotencyKey = "idempotencyKey123";
        BigDecimal amount = new BigDecimal("100.00");
        String asset = "BTC";
        String destinationAddress = "destinationAddress123";
        SettlementIntent intent = SettlementIntent.create(settlementId, idempotencyKey, amount, asset,
                destinationAddress);
        String transactionHash = "transactionHash123";
        ChainSubmissionResult submissionResult = ChainSubmissionResult.submitted(transactionHash);
        SettlementIntent confirmingIntent = intent.markSubmitted(submissionResult);
        SettlementIntent confirmedIntent = confirmingIntent.markConfirming();
        assertEquals(SettlementStatus.CONFIRMING, confirmedIntent.status());
    }

    @Test
    public void confirmingIntentKeepsTransactionHash() {
        String settlementId = "settlement123";
        String idempotencyKey = "idempotencyKey123";
        BigDecimal amount = new BigDecimal("100.00");
        String asset = "BTC";
        String destinationAddress = "destinationAddress123";
        SettlementIntent intent = SettlementIntent.create(settlementId, idempotencyKey, amount, asset,
                destinationAddress);
        String transactionHash = "transactionHash123";
        ChainSubmissionResult submissionResult = ChainSubmissionResult.submitted(transactionHash);
        SettlementIntent confirmingIntent = intent.markSubmitted(submissionResult);
        SettlementIntent confirmedIntent = confirmingIntent.markConfirming();
        assertEquals(transactionHash, confirmedIntent.chainTransactionHash());
    }

    @Test
    public void confirmingIntentKeepsOriginalCreatedAt() {
        String settlementId = "settlement123";
        String idempotencyKey = "idempotencyKey123";
        BigDecimal amount = new BigDecimal("100.00");
        String asset = "BTC";
        String destinationAddress = "destinationAddress123";
        SettlementIntent intent = SettlementIntent.create(settlementId, idempotencyKey, amount, asset,
                destinationAddress);
        String transactionHash = "transactionHash123";
        ChainSubmissionResult submissionResult = ChainSubmissionResult.submitted(transactionHash);
        SettlementIntent confirmingIntent = intent.markSubmitted(submissionResult);
        SettlementIntent confirmedIntent = confirmingIntent.markConfirming();
        assertEquals(intent.createdAt(), confirmedIntent.createdAt());
    }

    @Test
    public void createdIntentCannotBeMarkedConfirming() {
        String settlementId = "settlement123";
        String idempotencyKey = "idempotencyKey123";
        BigDecimal amount = new BigDecimal("100.00");
        String asset = "BTC";
        String destinationAddress = "destinationAddress123";
        SettlementIntent intent = SettlementIntent.create(settlementId, idempotencyKey, amount, asset,
                destinationAddress);
        assertThrows(IllegalStateException.class, () -> intent.markConfirming());
    }
}
