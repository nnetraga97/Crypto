package com.nnetraga.crypto.settlement.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSettlementIntentRepository extends JpaRepository<SettlementIntentEntity, String> {
    Optional<SettlementIntentEntity> findByIdempotencyKey(String idempotencyKey);
}
