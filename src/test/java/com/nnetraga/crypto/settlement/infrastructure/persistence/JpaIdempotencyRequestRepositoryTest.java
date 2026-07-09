package com.nnetraga.crypto.settlement.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nnetraga.crypto.settlement.application.IdempotencyKeyAlreadyExistsException;
import com.nnetraga.crypto.settlement.domain.IdempotencyRequest;
import com.nnetraga.crypto.settlement.domain.IdempotencyStatus;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@Import(JpaIdempotencyRequestRepository.class)
public class JpaIdempotencyRequestRepositoryTest {
    @Autowired
    private JpaIdempotencyRequestRepository repository;

    @Test
    public void createMakesRequestFindableByIdempotencyKey() {
        IdempotencyRequest request = newRequest("idempotencyKey123");

        repository.create(request);

        IdempotencyRequest foundRequest =
                repository.findByIdempotencyKey("idempotencyKey123").orElseThrow();
        assertEquals(request.idempotencyKey(), foundRequest.idempotencyKey());
        assertEquals(request.requestHash(), foundRequest.requestHash());
        assertEquals(IdempotencyStatus.IN_PROGRESS, foundRequest.status());
    }

    @Test
    public void findReturnsEmptyForUnknownIdempotencyKey() {
        assertTrue(repository.findByIdempotencyKey("missing").isEmpty());
    }

    @Test
    public void createRejectsDuplicateIdempotencyKey() {
        repository.create(newRequest("duplicate-key"));

        assertThrows(
                IdempotencyKeyAlreadyExistsException.class,
                () -> repository.create(newRequest("duplicate-key")));
    }

    private static IdempotencyRequest newRequest(String idempotencyKey) {
        return IdempotencyRequest.create(
                idempotencyKey,
                "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef",
                "settlement123");
    }
}
