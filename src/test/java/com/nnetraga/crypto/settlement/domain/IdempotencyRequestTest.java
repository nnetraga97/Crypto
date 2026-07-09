package com.nnetraga.crypto.settlement.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class IdempotencyRequestTest {
    @Test
    public void createStartsRequestInProgress() {
        IdempotencyRequest request = newRequest();

        assertEquals(IdempotencyStatus.IN_PROGRESS, request.status());
        assertNotNull(request.createdAt());
        assertNotNull(request.updatedAt());
    }

    @Test
    public void createRejectsBlankIdempotencyKey() {
        assertThrows(
                IllegalArgumentException.class,
                () -> IdempotencyRequest.create(" ", "request-hash-123", "settlement123"));
    }

    @Test
    public void createRejectsBlankRequestHash() {
        assertThrows(
                IllegalArgumentException.class,
                () -> IdempotencyRequest.create("idempotencyKey123", " ", "settlement123"));
    }

    @Test
    public void createRejectsBlankSettlementId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> IdempotencyRequest.create("idempotencyKey123", "request-hash-123", " "));
    }

    @Test
    public void markCompletedReturnsCompletedRequest() {
        IdempotencyRequest request = newRequest();

        IdempotencyRequest completedRequest = request.markCompleted();

        assertEquals(IdempotencyStatus.COMPLETED, completedRequest.status());
        assertEquals(request.idempotencyKey(), completedRequest.idempotencyKey());
        assertEquals(request.requestHash(), completedRequest.requestHash());
        assertEquals(request.settlementId(), completedRequest.settlementId());
    }

    @Test
    public void markFailedReturnsFailedRequest() {
        IdempotencyRequest request = newRequest();

        IdempotencyRequest failedRequest = request.markFailed();

        assertEquals(IdempotencyStatus.FAILED, failedRequest.status());
        assertEquals(request.idempotencyKey(), failedRequest.idempotencyKey());
        assertEquals(request.requestHash(), failedRequest.requestHash());
        assertEquals(request.settlementId(), failedRequest.settlementId());
    }

    private static IdempotencyRequest newRequest() {
        return IdempotencyRequest.create(
                "idempotencyKey123",
                "request-hash-123",
                "settlement123");
    }
}
