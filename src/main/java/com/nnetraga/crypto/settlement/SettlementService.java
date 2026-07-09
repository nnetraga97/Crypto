package com.nnetraga.crypto.settlement;

import java.util.Objects;
import java.util.Optional;

public class SettlementService {
    private final ChainSettlementAdapter chainSettlementAdapter;
    private final SettlementRepository settlementRepository;

    public SettlementService(ChainSettlementAdapter chainSettlementAdapter) {
        this(chainSettlementAdapter, new InMemorySettlementRepository());
    }

    public SettlementService(
            ChainSettlementAdapter chainSettlementAdapter,
            SettlementRepository settlementRepository) {
        this.chainSettlementAdapter =
                Objects.requireNonNull(chainSettlementAdapter, "chainSettlementAdapter is required");
        this.settlementRepository =
                Objects.requireNonNull(settlementRepository, "settlementRepository is required");
    }

    public SettlementIntent submit(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return settlementRepository
                .findByIdempotencyKey(intent.idempotencyKey())
                .orElseGet(() -> submitNew(intent));
    }

    private SettlementIntent submitNew(SettlementIntent intent) {
        ChainSubmissionResult submissionResult = chainSettlementAdapter.submit(intent);
        SettlementIntent submittedIntent = intent.markSubmitted(submissionResult);
        return settlementRepository.save(submittedIntent);
    }

    public SettlementIntent markConfirming(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return settlementRepository.save(intent.markConfirming());
    }

    public SettlementIntent markSettled(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return settlementRepository.save(intent.markSettled());
    }

    public SettlementIntent markNeedsReview(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return settlementRepository.save(intent.markNeedsReview());
    }

    public Optional<SettlementIntent> findByIdempotencyKey(String idempotencyKey) {
        Objects.requireNonNull(idempotencyKey, "idempotencyKey is required");
        return settlementRepository.findByIdempotencyKey(idempotencyKey);
    }
}
