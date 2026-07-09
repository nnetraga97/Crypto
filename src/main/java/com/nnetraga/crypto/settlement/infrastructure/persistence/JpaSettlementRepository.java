package com.nnetraga.crypto.settlement.infrastructure.persistence;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.nnetraga.crypto.settlement.application.SettlementRepository;
import com.nnetraga.crypto.settlement.domain.SettlementIntent;

@Repository
public class JpaSettlementRepository implements SettlementRepository {
    private final JpaSettlementIntentRepository repository;

    public JpaSettlementRepository(JpaSettlementIntentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<SettlementIntent> findByIdempotencyKey(String idempotencyKey) {
        Objects.requireNonNull(idempotencyKey, "idempotencyKey is required");
        return repository.findByIdempotencyKey(idempotencyKey)
                .map(SettlementIntentEntity::toDomain);
    }

    @Override
    public SettlementIntent save(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        SettlementIntentEntity entity = repository.findById(intent.settlementId())
                .map(existingEntity -> {
                    existingEntity.apply(intent);
                    return existingEntity;
                })
                .orElseGet(() -> SettlementIntentEntity.fromDomain(intent));
        return repository.save(entity).toDomain();
    }
}
