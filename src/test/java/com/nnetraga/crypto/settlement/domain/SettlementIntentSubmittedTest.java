package com.nnetraga.crypto.settlement.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class SettlementIntentSubmittedTest {

    @Test
    public void intentCanBeMarkedAsSubmitted() {
        String settlementId = "settlement123";
        String idempotencyKey = "idempotencyKey123";
        BigDecimal amount = new BigDecimal("100.00");
        String asset = "BTC";
        String destinationAddress = "destinationAddress123";
        SettlementIntent intent = SettlementIntent.create(settlementId, idempotencyKey, amount, asset, destinationAddress);
        String transactionHash = "transactionHash123";
        ChainSubmissionResult submissionResult = ChainSubmissionResult.submitted(transactionHash);
        SettlementIntent submittedIntent = intent.markSubmitted(submissionResult);
        assertEquals(SettlementStatus.SUBMITTED, submittedIntent.status());
        assertNotNull(submittedIntent.chainTransactionHash());
        assertEquals(intent.createdAt(), submittedIntent.createdAt());
        assertNotEquals(intent.updatedAt(), submittedIntent.updatedAt());
        assertThrows(IllegalStateException.class, () -> submittedIntent.markSubmitted(submissionResult));
        assertEquals(SettlementStatus.SUBMITTED, submittedIntent.status());
    }

}