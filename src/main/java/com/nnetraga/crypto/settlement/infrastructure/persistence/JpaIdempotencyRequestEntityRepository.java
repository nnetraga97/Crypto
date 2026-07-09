package com.nnetraga.crypto.settlement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaIdempotencyRequestEntityRepository
        extends JpaRepository<IdempotencyRequestEntity, String> {
}
