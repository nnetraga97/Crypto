package com.nnetraga.crypto.settlement.infrastructure.persistence;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.nnetraga.crypto.settlement.application.IdempotencyKeyAlreadyExistsException;
import com.nnetraga.crypto.settlement.application.IdempotencyRequestRepository;
import com.nnetraga.crypto.settlement.domain.IdempotencyRequest;

public class InMemoryIdempotencyRequestRepository implements IdempotencyRequestRepository {
    private final ConcurrentMap<String, IdempotencyRequest> requestsByIdempotencyKey =
            new ConcurrentHashMap<>();

    @Override
    public Optional<IdempotencyRequest> findByIdempotencyKey(String idempotencyKey) {
        Objects.requireNonNull(idempotencyKey, "idempotencyKey is required");
        return Optional.ofNullable(requestsByIdempotencyKey.get(idempotencyKey));
    }

    @Override
    public IdempotencyRequest create(IdempotencyRequest request) {
        Objects.requireNonNull(request, "request is required");
        IdempotencyRequest existing =
                requestsByIdempotencyKey.putIfAbsent(request.idempotencyKey(), request);
        if (existing != null) {
            throw new IdempotencyKeyAlreadyExistsException("Idempotency key already exists");
        }
        return request;
    }

    @Override
    public IdempotencyRequest save(IdempotencyRequest request) {
        Objects.requireNonNull(request, "request is required");
        requestsByIdempotencyKey.put(request.idempotencyKey(), request);
        return request;
    }
}
