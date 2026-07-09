package com.nnetraga.crypto.settlement;
import java.time.Instant;

public record ChainSubmissionResult(String transactionHash, Instant submittedAt) {
    public ChainSubmissionResult {
        if (transactionHash == null || transactionHash.isBlank()) {
            throw new IllegalArgumentException("transactionHash is required");
        }
        if (submittedAt == null) {
            throw new IllegalArgumentException("submittedAt is required");
        }
    }
    
    public static ChainSubmissionResult submitted(String transactionHash) {
        return new ChainSubmissionResult(transactionHash, Instant.now());
    }
}
