package com.nnetraga.crypto.settlement.infrastructure.chain;

import java.util.Objects;

import com.nnetraga.crypto.settlement.application.ChainSettlementAdapter;
import com.nnetraga.crypto.settlement.domain.ChainSubmissionResult;
import com.nnetraga.crypto.settlement.domain.SettlementIntent;

public class FakeChainSettlementAdapter implements ChainSettlementAdapter {
    @Override
    public ChainSubmissionResult submit(SettlementIntent settlementIntent) {
        Objects.requireNonNull(settlementIntent, "settlementIntent is required");
        return ChainSubmissionResult.submitted("fake-tx-" + settlementIntent.settlementId());
    }
}
