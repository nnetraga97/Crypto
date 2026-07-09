package com.nnetraga.crypto.settlement;

import java.util.Objects;

public class SettlementService {
    private final ChainSettlementAdapter chainSettlementAdapter;

    public SettlementService(ChainSettlementAdapter chainSettlementAdapter) {
        this.chainSettlementAdapter =
                Objects.requireNonNull(chainSettlementAdapter, "chainSettlementAdapter is required");
    }

    public SettlementIntent submit(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        ChainSubmissionResult submissionResult = chainSettlementAdapter.submit(intent);
        return intent.markSubmitted(submissionResult);
    }

    public SettlementIntent markConfirming(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return intent.markConfirming();
    }

    public SettlementIntent markSettled(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return intent.markSettled();
    }

    public SettlementIntent markNeedsReview(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return intent.markNeedsReview();
    }
}
