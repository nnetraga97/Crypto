package com.nnetraga.crypto.settlement.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.nnetraga.crypto.settlement.domain.ChainSubmissionResult;
import com.nnetraga.crypto.settlement.domain.SettlementIntent;
import com.nnetraga.crypto.settlement.domain.SettlementStatus;
import com.nnetraga.crypto.settlement.infrastructure.chain.FakeChainSettlementAdapter;

public class SettlementServiceTest {
    @Test
    public void constructorRejectsNullAdapter() {
        assertThrows(NullPointerException.class, () -> new SettlementService(null));
    }

    @Test
    public void constructorRejectsNullRepository() {
        assertThrows(
                NullPointerException.class,
                () -> new SettlementService(new FakeChainSettlementAdapter(), null));
    }

    @Test
    public void submitRejectsNullIntent() {
        SettlementService service = new SettlementService(new FakeChainSettlementAdapter());

        assertThrows(NullPointerException.class, () -> service.submit(null));
    }

    @Test
    public void submitReturnsSubmittedIntent() {
        SettlementService service = new SettlementService(new FakeChainSettlementAdapter());

        SettlementIntent submittedIntent = service.submit(newIntent());

        assertEquals(SettlementStatus.SUBMITTED, submittedIntent.status());
    }

    @Test
    public void submitStoresChainTransactionHash() {
        SettlementService service = new SettlementService(new FakeChainSettlementAdapter());

        SettlementIntent submittedIntent = service.submit(newIntent());

        assertEquals("fake-tx-settlement123", submittedIntent.chainTransactionHash());
        assertNotNull(submittedIntent.updatedAt());
    }

    @Test
    public void submitReturnsExistingIntentForRepeatedIdempotencyKey() {
        CountingChainSettlementAdapter adapter = new CountingChainSettlementAdapter();
        SettlementService service = new SettlementService(adapter);

        SettlementIntent firstSubmission = service.submit(newIntent());
        SettlementIntent retrySubmission = service.submit(newIntent());

        assertEquals(firstSubmission, retrySubmission);
        assertEquals(1, adapter.submissionCount());
    }

    @Test
    public void submitRejectsSameIdempotencyKeyWithDifferentPayload() {
        SettlementService service = new SettlementService(new FakeChainSettlementAdapter());

        service.submit(newIntent());

        assertThrows(
                IdempotencyConflictException.class,
                () -> service.submit(SettlementIntent.create(
                        "settlement123",
                        "idempotencyKey123",
                        new BigDecimal("200.00"),
                        "BTC",
                        "destinationAddress123")));
    }

    @Test
    public void markConfirmingReturnsConfirmingIntent() {
        SettlementService service = new SettlementService(new FakeChainSettlementAdapter());
        SettlementIntent submittedIntent = service.submit(newIntent());

        SettlementIntent confirmingIntent = service.markConfirming(submittedIntent);

        assertEquals(SettlementStatus.CONFIRMING, confirmingIntent.status());
        assertEquals(submittedIntent.chainTransactionHash(), confirmingIntent.chainTransactionHash());
    }

    @Test
    public void markSettledReturnsSettledIntent() {
        SettlementService service = new SettlementService(new FakeChainSettlementAdapter());
        SettlementIntent confirmingIntent = service.markConfirming(service.submit(newIntent()));

        SettlementIntent settledIntent = service.markSettled(confirmingIntent);

        assertEquals(SettlementStatus.SETTLED, settledIntent.status());
        assertEquals(confirmingIntent.chainTransactionHash(), settledIntent.chainTransactionHash());
    }

    @Test
    public void markNeedsReviewReturnsNeedsReviewIntent() {
        SettlementService service = new SettlementService(new FakeChainSettlementAdapter());
        SettlementIntent submittedIntent = service.submit(newIntent());

        SettlementIntent reviewIntent = service.markNeedsReview(submittedIntent);

        assertEquals(SettlementStatus.NEEDS_REVIEW, reviewIntent.status());
        assertEquals(submittedIntent.chainTransactionHash(), reviewIntent.chainTransactionHash());
    }

    private static SettlementIntent newIntent() {
        return SettlementIntent.create(
                "settlement123",
                "idempotencyKey123",
                new BigDecimal("100.00"),
                "BTC",
                "destinationAddress123");
    }

    private static final class CountingChainSettlementAdapter implements ChainSettlementAdapter {
        private int submissionCount;

        @Override
        public ChainSubmissionResult submit(SettlementIntent settlementIntent) {
            submissionCount++;
            return ChainSubmissionResult.submitted("counting-tx-" + settlementIntent.settlementId());
        }

        private int submissionCount() {
            return submissionCount;
        }
    }
}
