package com.nnetraga.crypto.settlement.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ChainSubmissionResultTest {

    @Test
    public void submittedCreatesResultWithCurrentTimestamp() {
        String transactionHash = "transactionHash123";
        ChainSubmissionResult result = ChainSubmissionResult.submitted(transactionHash);
        assertEquals(transactionHash, result.transactionHash());
        assertNotNull(result.submittedAt());
    }

    @Test
    public void rejectsBlankTransactionHash() {
        String transactionHash = " ";
        assertThrows(IllegalArgumentException.class, () -> ChainSubmissionResult.submitted(transactionHash));   
    }

    @Test
    public void rejectsNullTransactionHash() {
        String transactionHash = null;
        assertThrows(IllegalArgumentException.class, () -> ChainSubmissionResult.submitted(transactionHash));
    }
}