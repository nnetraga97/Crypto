package com.nnetraga.crypto.settlement.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.nnetraga.crypto.settlement.domain.SettlementIntent;

public class InMemorySettlementRepositoryTest {
    @Test
    public void saveMakesIntentFindableByIdempotencyKey() {
        InMemorySettlementRepository repository = new InMemorySettlementRepository();
        SettlementIntent intent = newIntent("idempotencyKey123");

        repository.save(intent);

        assertEquals(intent, repository.findByIdempotencyKey("idempotencyKey123").orElseThrow());
    }

    @Test
    public void findReturnsEmptyForUnknownIdempotencyKey() {
        InMemorySettlementRepository repository = new InMemorySettlementRepository();

        assertTrue(repository.findByIdempotencyKey("missing").isEmpty());
    }

    @Test
    public void findRejectsNullIdempotencyKey() {
        InMemorySettlementRepository repository = new InMemorySettlementRepository();

        assertThrows(NullPointerException.class, () -> repository.findByIdempotencyKey(null));
    }

    @Test
    public void saveRejectsNullIntent() {
        InMemorySettlementRepository repository = new InMemorySettlementRepository();

        assertThrows(NullPointerException.class, () -> repository.save(null));
    }

    private static SettlementIntent newIntent(String idempotencyKey) {
        return SettlementIntent.create(
                "settlement123",
                idempotencyKey,
                new BigDecimal("100.00"),
                "BTC",
                "destinationAddress123");
    }
}
