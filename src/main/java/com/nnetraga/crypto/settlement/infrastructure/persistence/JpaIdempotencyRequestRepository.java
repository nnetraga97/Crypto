package com.nnetraga.crypto.settlement.infrastructure.persistence;

import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nnetraga.crypto.settlement.application.IdempotencyKeyAlreadyExistsException;
import com.nnetraga.crypto.settlement.application.IdempotencyRequestRepository;
import com.nnetraga.crypto.settlement.domain.IdempotencyRequest;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class JpaIdempotencyRequestRepository implements IdempotencyRequestRepository {
    private final JpaIdempotencyRequestEntityRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public JpaIdempotencyRequestRepository(JpaIdempotencyRequestEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<IdempotencyRequest> findByIdempotencyKey(String idempotencyKey) {
        Objects.requireNonNull(idempotencyKey, "idempotencyKey is required");
        return repository.findById(idempotencyKey)
                .map(IdempotencyRequestEntity::toDomain);
    }

    @Override
    @Transactional
    public IdempotencyRequest create(IdempotencyRequest request) {
        Objects.requireNonNull(request, "request is required");
        try {
            entityManager.persist(IdempotencyRequestEntity.fromDomain(request));
            entityManager.flush();
            return request;
        } catch (DataIntegrityViolationException | EntityExistsException exception) {
            throw new IdempotencyKeyAlreadyExistsException("Idempotency key already exists");
        }
    }

    @Override
    public IdempotencyRequest save(IdempotencyRequest request) {
        Objects.requireNonNull(request, "request is required");
        return repository.save(IdempotencyRequestEntity.fromDomain(request)).toDomain();
    }
}
