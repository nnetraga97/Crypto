package com.nnetraga.crypto.settlement.application;

import java.util.Optional;

import com.nnetraga.crypto.settlement.domain.SettlementIntent;

public interface SettlementRepository {
    Optional<SettlementIntent> findByIdempotencyKey(String idempotencyKey);

    SettlementIntent save(SettlementIntent intent);
}
