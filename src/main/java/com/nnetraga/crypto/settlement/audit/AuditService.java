package com.nnetraga.crypto.settlement.audit;

public interface AuditService {
    void recordApiRequest(ApiAuditRecord record);

    void recordSettlementStateChange(SettlementStateAuditRecord record);
}
