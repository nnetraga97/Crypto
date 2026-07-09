package com.nnetraga.crypto.settlement.application;

import java.util.Optional;

import com.nnetraga.crypto.settlement.domain.IdempotencyRequest;

public interface IdempotencyRequestRepository {
    Optional<IdempotencyRequest> findByIdempotencyKey(String idempotencyKey);

    IdempotencyRequest create(IdempotencyRequest request);

    IdempotencyRequest save(IdempotencyRequest request);
}
