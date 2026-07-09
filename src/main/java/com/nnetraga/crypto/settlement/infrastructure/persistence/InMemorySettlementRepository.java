package com.nnetraga.crypto.settlement.infrastructure.persistence;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.nnetraga.crypto.settlement.application.SettlementRepository;
import com.nnetraga.crypto.settlement.domain.SettlementIntent;

public class InMemorySettlementRepository implements SettlementRepository {
    private final ConcurrentMap<String, SettlementIntent> settlementsByIdempotencyKey =
            new ConcurrentHashMap<>();

    @Override
    public Optional<SettlementIntent> findByIdempotencyKey(String idempotencyKey) {
        Objects.requireNonNull(idempotencyKey, "idempotencyKey is required");
        return Optional.ofNullable(settlementsByIdempotencyKey.get(idempotencyKey));
    }

    @Override
    public SettlementIntent save(SettlementIntent intent) {
        Objects.requireNonNull(intent, "intent is required");
        settlementsByIdempotencyKey.put(intent.idempotencyKey(), intent);
        return intent;
    }
}
