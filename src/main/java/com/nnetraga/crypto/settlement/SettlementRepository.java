package com.nnetraga.crypto.settlement;

import java.util.Optional;

public interface SettlementRepository {
    Optional<SettlementIntent> findByIdempotencyKey(String idempotencyKey);

    SettlementIntent save(SettlementIntent intent);
}
