package com.nnetraga.crypto.settlement;

public interface ChainSettlementAdapter {
    ChainSubmissionResult submit(SettlementIntent settlementIntent);
}