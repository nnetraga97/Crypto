package com.nnetraga.crypto.settlement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettlementConfiguration {
    @Bean
    public ChainSettlementAdapter chainSettlementAdapter() {
        return new FakeChainSettlementAdapter();
    }

    @Bean
    public SettlementRepository settlementRepository() {
        return new InMemorySettlementRepository();
    }

    @Bean
    public SettlementService settlementService(
            ChainSettlementAdapter chainSettlementAdapter,
            SettlementRepository settlementRepository) {
        return new SettlementService(chainSettlementAdapter, settlementRepository);
    }
}