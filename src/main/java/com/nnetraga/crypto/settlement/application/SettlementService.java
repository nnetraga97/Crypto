package com.nnetraga.crypto.settlement.application;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnetraga.crypto.settlement.domain.ChainSubmissionResult;
import com.nnetraga.crypto.settlement.domain.IdempotencyRequest;
import com.nnetraga.crypto.settlement.domain.SettlementIntent;
import com.nnetraga.crypto.settlement.domain.SettlementStatus;
import com.nnetraga.crypto.settlement.infrastructure.persistence.InMemoryIdempotencyRequestRepository;
import com.nnetraga.crypto.settlement.infrastructure.persistence.InMemorySettlementRepository;

@Service
public class SettlementService {
    private final ChainSettlementAdapter chainSettlementAdapter;
    private final SettlementRepository settlementRepository;
    private final IdempotencyRequestRepository idempotencyRequestRepository;
    private final SettlementRequestHasher settlementRequestHasher;
    private final SettlementEventPublisher settlementEventPublisher;

    public SettlementService(ChainSettlementAdapter chainSettlementAdapter) {
        this(
                chainSettlementAdapter,
                new InMemorySettlementRepository(),
                new InMemoryIdempotencyRequestRepository(),
                new Sha256SettlementRequestHasher(),
                event -> {
                });
    }

    public SettlementService(
            ChainSettlementAdapter chainSettlementAdapter,
            SettlementRepository settlementRepository) {
        this(
                chainSettlementAdapter,
                settlementRepository,
                new InMemoryIdempotencyRequestRepository(),
                new Sha256SettlementRequestHasher(),
                event -> {
                });
    }

    @Autowired
    public SettlementService(
            ChainSettlementAdapter chainSettlementAdapter,
            SettlementRepository settlementRepository,
            IdempotencyRequestRepository idempotencyRequestRepository,
            SettlementRequestHasher settlementRequestHasher,
            SettlementEventPublisher settlementEventPublisher) {
        this.chainSettlementAdapter =
                Objects.requireNonNull(chainSettlementAdapter, "chainSettlementAdapter is required");
        this.settlementRepository =
                Objects.requireNonNull(settlementRepository, "settlementRepository is required");
        this.idempotencyRequestRepository =
                Objects.requireNonNull(idempotencyRequestRepository, "idempotencyRequestRepository is required");
        this.settlementRequestHasher =
                Objects.requireNonNull(settlementRequestHasher, "settlementRequestHasher is required");
        this.settlementEventPublisher =
                Objects.requireNonNull(settlementEventPublisher, "settlementEventPublisher is required");
    }

    public SettlementIntent submit(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        String requestHash = settlementRequestHasher.hash(intent);
        Optional<IdempotencyRequest> existingRequest =
                idempotencyRequestRepository.findByIdempotencyKey(intent.idempotencyKey());
        if (existingRequest.isPresent()) {
            return handleExistingIdempotencyRequest(intent, requestHash, existingRequest.orElseThrow());
        }
        return submitWithNewIdempotencyRequest(intent, requestHash);
    }

    private SettlementIntent handleExistingIdempotencyRequest(
            SettlementIntent intent,
            String requestHash,
            IdempotencyRequest existingRequest) {
        if (!existingRequest.requestHash().equals(requestHash)) {
            throw new IdempotencyConflictException(
                    "Idempotency key reused with a different request payload");
        }
        return settlementRepository.findByIdempotencyKey(intent.idempotencyKey())
                .orElseThrow(() -> new IdempotencyRequestInProgressException(
                        "Idempotency request is still in progress"));
    }

    private SettlementIntent submitWithNewIdempotencyRequest(SettlementIntent intent, String requestHash) {
        IdempotencyRequest idempotencyRequest = IdempotencyRequest.create(
                intent.idempotencyKey(),
                requestHash,
                intent.settlementId());
        try {
            idempotencyRequestRepository.create(idempotencyRequest);
        } catch (IdempotencyKeyAlreadyExistsException exception) {
            return handleExistingIdempotencyRequest(
                    intent,
                    requestHash,
                    idempotencyRequestRepository.findByIdempotencyKey(intent.idempotencyKey()).orElseThrow());
        }

        try {
            ChainSubmissionResult submissionResult = chainSettlementAdapter.submit(intent);
            SettlementIntent submittedIntent = intent.markSubmitted(submissionResult);
            SettlementIntent savedIntent = settlementRepository.save(submittedIntent);
            idempotencyRequestRepository.save(idempotencyRequest.markCompleted());
            publishStateChange(intent, savedIntent);
            return savedIntent;
        } catch (RuntimeException exception) {
            idempotencyRequestRepository.save(idempotencyRequest.markFailed());
            throw exception;
        }
    }

    public SettlementIntent markConfirming(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return saveStateChange(intent, intent.markConfirming());
    }

    public SettlementIntent markSettled(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return saveStateChange(intent, intent.markSettled());
    }

    public SettlementIntent markNeedsReview(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        return saveStateChange(intent, intent.markNeedsReview());
    }

    public Optional<SettlementIntent> findByIdempotencyKey(String idempotencyKey) {
        Objects.requireNonNull(idempotencyKey, "idempotencyKey is required");
        return settlementRepository.findByIdempotencyKey(idempotencyKey);
    }

    private SettlementIntent saveStateChange(SettlementIntent previousIntent, SettlementIntent updatedIntent) {
        SettlementIntent savedIntent = settlementRepository.save(updatedIntent);
        publishStateChange(previousIntent, savedIntent);
        return savedIntent;
    }

    private void publishStateChange(SettlementIntent previousIntent, SettlementIntent savedIntent) {
        SettlementStatus previousStatus = previousIntent.status();
        SettlementStatus newStatus = savedIntent.status();
        if (previousStatus != newStatus) {
            settlementEventPublisher.publish(new SettlementStateChangedEvent(
                    savedIntent.settlementId(),
                    savedIntent.idempotencyKey(),
                    previousStatus,
                    newStatus,
                    Instant.now()));
        }
    }
}
