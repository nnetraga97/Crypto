package com.nnetraga.crypto.settlement.application;

import com.nnetraga.crypto.settlement.domain.ChainSubmissionResult;
import com.nnetraga.crypto.settlement.domain.SettlementIntent;

public interface ChainSettlementAdapter {
    ChainSubmissionResult submit(SettlementIntent settlementIntent);
}
