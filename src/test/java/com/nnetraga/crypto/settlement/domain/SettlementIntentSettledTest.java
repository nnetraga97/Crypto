package com.nnetraga.crypto.settlement.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class SettlementIntentSettledTest {
    @Test
    public void confirmingIntentCanBeMarkedSettled() {
        SettlementIntent confirmingIntent = confirmingIntent();

        SettlementIntent settledIntent = confirmingIntent.markSettled();

        assertEquals(SettlementStatus.SETTLED, settledIntent.status());
    }

    @Test
    public void settledIntentKeepsTransactionHash() {
        SettlementIntent confirmingIntent = confirmingIntent();

        SettlementIntent settledIntent = confirmingIntent.markSettled();

        assertEquals(confirmingIntent.chainTransactionHash(), settledIntent.chainTransactionHash());
    }

    @Test
    public void settledIntentKeepsOriginalCreatedAt() {
        SettlementIntent confirmingIntent = confirmingIntent();

        SettlementIntent settledIntent = confirmingIntent.markSettled();

        assertEquals(confirmingIntent.createdAt(), settledIntent.createdAt());
    }

    @Test
    public void submittedIntentCannotBeMarkedSettled() {
        SettlementIntent submittedIntent = submittedIntent();

        assertThrows(IllegalStateException.class, submittedIntent::markSettled);
    }

    @Test
    public void createdIntentCannotBeMarkedSettled() {
        SettlementIntent intent = SettlementIntent.create(
                "settlement123",
                "idempotencyKey123",
                new BigDecimal("100.00"),
                "BTC",
                "destinationAddress123");

        assertThrows(IllegalStateException.class, intent::markSettled);
    }

    private static SettlementIntent confirmingIntent() {
        return submittedIntent().markConfirming();
    }

    private static SettlementIntent submittedIntent() {
        SettlementIntent intent = SettlementIntent.create(
                "settlement123",
                "idempotencyKey123",
                new BigDecimal("100.00"),
                "BTC",
                "destinationAddress123");
        ChainSubmissionResult submissionResult = ChainSubmissionResult.submitted("transactionHash123");
        return intent.markSubmitted(submissionResult);
    }
}
