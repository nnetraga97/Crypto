package com.nnetraga.crypto.settlement.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.nnetraga.crypto.settlement.domain.SettlementIntent;

public class Sha256SettlementRequestHasherTest {
    private final Sha256SettlementRequestHasher hasher = new Sha256SettlementRequestHasher();

    @Test
    public void hashIsStableForSameBusinessPayload() {
        SettlementIntent first = intent("idempotencyKey1", new BigDecimal("100.00"));
        SettlementIntent retry = intent("idempotencyKey2", new BigDecimal("100.0"));

        assertEquals(hasher.hash(first), hasher.hash(retry));
    }

    @Test
    public void hashChangesWhenAmountChanges() {
        SettlementIntent first = intent("idempotencyKey1", new BigDecimal("100.00"));
        SettlementIntent changedAmount = intent("idempotencyKey1", new BigDecimal("200.00"));

        assertNotEquals(hasher.hash(first), hasher.hash(changedAmount));
    }

    private static SettlementIntent intent(String idempotencyKey, BigDecimal amount) {
        return SettlementIntent.create(
                "settlement123",
                idempotencyKey,
                amount,
                "USDC",
                "destination-address");
    }
}
