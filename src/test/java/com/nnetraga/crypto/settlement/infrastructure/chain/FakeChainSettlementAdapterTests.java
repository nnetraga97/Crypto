package com.nnetraga.crypto.settlement.infrastructure.chain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.nnetraga.crypto.settlement.domain.ChainSubmissionResult;
import com.nnetraga.crypto.settlement.domain.SettlementIntent;

public class FakeChainSettlementAdapterTests {

    @Test
    public void submitReturnsTransactionHashWithSettlementId() {
        FakeChainSettlementAdapter adapter = new FakeChainSettlementAdapter();
        SettlementIntent intent = SettlementIntent.create(
                "settlement123",
                "idempotencyKey123",
                new BigDecimal("100.00"),
                "asset123",
                "destinationAddress123");

        ChainSubmissionResult result = adapter.submit(intent);

        assertEquals("fake-tx-settlement123", result.transactionHash());
        assertNotNull(result.submittedAt());
    }

    @Test
    public void nullIntentThrowsException() {
        FakeChainSettlementAdapter adapter = new FakeChainSettlementAdapter();
        assertThrows(NullPointerException.class, () -> adapter.submit(null));
    }

}
