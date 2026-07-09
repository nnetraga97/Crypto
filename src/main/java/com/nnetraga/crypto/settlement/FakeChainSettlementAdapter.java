package com.nnetraga.crypto.settlement;

import java.util.Objects;

public class FakeChainSettlementAdapter implements ChainSettlementAdapter {
    @Override
    public ChainSubmissionResult submit(SettlementIntent settlementIntent) {
        Objects.requireNonNull(settlementIntent, "settlementIntent is required");
        return ChainSubmissionResult.submitted("fake-tx-"+settlementIntent.settlementId());
    }
}
