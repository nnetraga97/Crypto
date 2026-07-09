package com.nnetraga.crypto.settlement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class SettlementIntentNeedsReviewTest {
    @Test
    public void submittedIntentCanBeMarkedNeedsReview() {
        SettlementIntent submittedIntent = submittedIntent();

        SettlementIntent reviewIntent = submittedIntent.markNeedsReview();

        assertEquals(SettlementStatus.NEEDS_REVIEW, reviewIntent.status());
    }

    @Test
    public void confirmingIntentCanBeMarkedNeedsReview() {
        SettlementIntent confirmingIntent = submittedIntent().markConfirming();

        SettlementIntent reviewIntent = confirmingIntent.markNeedsReview();

        assertEquals(SettlementStatus.NEEDS_REVIEW, reviewIntent.status());
    }

    @Test
    public void needsReviewIntentKeepsTransactionHash() {
        SettlementIntent confirmingIntent = submittedIntent().markConfirming();

        SettlementIntent reviewIntent = confirmingIntent.markNeedsReview();

        assertEquals(confirmingIntent.chainTransactionHash(), reviewIntent.chainTransactionHash());
    }

    @Test
    public void needsReviewIntentKeepsOriginalCreatedAt() {
        SettlementIntent confirmingIntent = submittedIntent().markConfirming();

        SettlementIntent reviewIntent = confirmingIntent.markNeedsReview();

        assertEquals(confirmingIntent.createdAt(), reviewIntent.createdAt());
    }

    @Test
    public void createdIntentCannotBeMarkedNeedsReview() {
        SettlementIntent intent = newIntent();

        assertThrows(IllegalStateException.class, intent::markNeedsReview);
    }

    @Test
    public void settledIntentCannotBeMarkedNeedsReview() {
        SettlementIntent settledIntent = submittedIntent().markConfirming().markSettled();

        assertThrows(IllegalStateException.class, settledIntent::markNeedsReview);
    }

    private static SettlementIntent submittedIntent() {
        SettlementIntent intent = newIntent();
        ChainSubmissionResult submissionResult = ChainSubmissionResult.submitted("transactionHash123");
        return intent.markSubmitted(submissionResult);
    }

    private static SettlementIntent newIntent() {
        return SettlementIntent.create(
                "settlement123",
                "idempotencyKey123",
                new BigDecimal("100.00"),
                "BTC",
                "destinationAddress123");
    }
}
