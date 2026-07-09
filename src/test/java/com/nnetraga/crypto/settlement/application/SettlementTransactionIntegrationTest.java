package com.nnetraga.crypto.settlement.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.nnetraga.crypto.settlement.domain.ChainSubmissionResult;
import com.nnetraga.crypto.settlement.domain.SettlementIntent;

@SpringBootTest
public class SettlementTransactionIntegrationTest {
    @Autowired
    private SettlementService settlementService;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private IdempotencyRequestRepository idempotencyRequestRepository;

    @Test
    public void submitRollsBackIdempotencyAndSettlementWritesWhenChainSubmissionFails() {
        SettlementIntent intent = SettlementIntent.create(
                "settlement-rollback-123",
                "idem-rollback-123",
                new BigDecimal("100.00"),
                "USDC",
                "destination-address");

        assertThrows(IllegalStateException.class, () -> settlementService.submit(intent));

        assertTrue(idempotencyRequestRepository.findByIdempotencyKey("idem-rollback-123").isEmpty());
        assertTrue(settlementRepository.findByIdempotencyKey("idem-rollback-123").isEmpty());
    }

    @TestConfiguration
    static class FailingChainConfiguration {
        @Bean
        @Primary
        ChainSettlementAdapter failingChainSettlementAdapter() {
            return new ChainSettlementAdapter() {
                @Override
                public ChainSubmissionResult submit(SettlementIntent settlementIntent) {
                    throw new IllegalStateException("chain unavailable");
                }
            };
        }
    }
}
