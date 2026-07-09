package com.nnetraga.crypto.settlement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nnetraga.crypto.settlement.application.ChainSettlementAdapter;
import com.nnetraga.crypto.settlement.infrastructure.chain.FakeChainSettlementAdapter;

@Configuration
public class SettlementConfiguration {
    @Bean
    public ChainSettlementAdapter chainSettlementAdapter() {
        return new FakeChainSettlementAdapter();
    }
}
