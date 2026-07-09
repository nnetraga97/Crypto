package com.nnetraga.crypto.settlement.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class SettlementIntentTest {

    @Test
    public void createStartsIntentInCreatedState() {
        String settlementId = "settlement123";
        String idempotencyKey = "idempotencyKey123";
        BigDecimal amount = new BigDecimal("100.00");
        String asset = "BTC";
        String destinationAddress = "destinationAddress123";
        SettlementIntent intent = SettlementIntent.create(settlementId, idempotencyKey, amount, asset, destinationAddress);
        assertEquals(SettlementStatus.CREATED, intent.status());
        assertNotNull(intent.createdAt());
        assertNotNull(intent.updatedAt());
        assertNull(intent.chainTransactionHash());
    }

    @Test
    public void rejectsNegativeAmount(){
        String settlementId = "settlement123";
        String idempotencyKey = "idempotencyKey123";
        BigDecimal amount = new BigDecimal("-1.00");
        String asset = "BTC";
        String destinationAddress = "destinationAddress123";
        assertThrows(IllegalArgumentException.class, () -> SettlementIntent.create(settlementId, idempotencyKey, amount, asset, destinationAddress));
    }

    @Test
    public void rejectsBlankIdempotencyKey(){
        String settlementId = "settlement123";
        String idempotencyKey = " ";
        BigDecimal amount = new BigDecimal("100.00");
        String asset = "BTC";
        String destinationAddress = "destinationAddress123";
        assertThrows(IllegalArgumentException.class, () -> SettlementIntent.create(settlementId, idempotencyKey, amount, asset, destinationAddress));
    }

    @Test
    public void rejectsBlankDestinationAddress(){
        String settlementId = "settlement123";
        String idempotencyKey = "idempotencyKey123";
        BigDecimal amount = new BigDecimal("100.00");
        String asset = "BTC";
        String destinationAddress = " ";
        assertThrows(IllegalArgumentException.class, () -> SettlementIntent.create(settlementId, idempotencyKey, amount, asset, destinationAddress));
    }
}