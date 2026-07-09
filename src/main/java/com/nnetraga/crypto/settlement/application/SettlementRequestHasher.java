package com.nnetraga.crypto.settlement.application;

import com.nnetraga.crypto.settlement.domain.SettlementIntent;

public interface SettlementRequestHasher {
    String hash(SettlementIntent intent);
}
