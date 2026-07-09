package com.nnetraga.crypto.settlement.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nnetraga.crypto.settlement.domain.ChainSubmissionResult;
import com.nnetraga.crypto.settlement.domain.SettlementIntent;
import com.nnetraga.crypto.settlement.domain.SettlementStatus;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@Import(JpaSettlementRepository.class)
public class JpaSettlementRepositoryTest {
    @Autowired
    private JpaSettlementRepository repository;

    @Test
    public void saveMakesIntentFindableByIdempotencyKey() {
        SettlementIntent submittedIntent = newSubmittedIntent();

        repository.save(submittedIntent);

        SettlementIntent foundIntent =
                repository.findByIdempotencyKey("idempotencyKey123").orElseThrow();
        assertEquals(SettlementStatus.SUBMITTED, foundIntent.status());
        assertEquals("fake-tx-settlement123", foundIntent.chainTransactionHash());
    }

    @Test
    public void saveAllowsMultipleStateUpdatesWithOptimisticVersioning() {
        SettlementIntent submittedIntent = repository.save(newSubmittedIntent());
        SettlementIntent confirmingIntent = repository.save(submittedIntent.markConfirming());

        SettlementIntent settledIntent = repository.save(confirmingIntent.markSettled());

        assertEquals(SettlementStatus.SETTLED, settledIntent.status());
    }

    private static SettlementIntent newSubmittedIntent() {
        SettlementIntent intent = SettlementIntent.create(
                "settlement123",
                "idempotencyKey123",
                new BigDecimal("100.00"),
                "USDC",
                "destination-address");
        return intent.markSubmitted(ChainSubmissionResult.submitted("fake-tx-settlement123"));
    }
}
