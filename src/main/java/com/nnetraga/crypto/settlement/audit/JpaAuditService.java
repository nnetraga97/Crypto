package com.nnetraga.crypto.settlement.audit;

import org.springframework.stereotype.Service;

@Service
public class JpaAuditService implements AuditService {
    private final AuditEventRepository auditEventRepository;

    public JpaAuditService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    @Override
    public void recordApiRequest(ApiAuditRecord record) {
        auditEventRepository.save(AuditEventEntity.apiRequest(record));
    }

    @Override
    public void recordSettlementStateChange(SettlementStateAuditRecord record) {
        auditEventRepository.save(AuditEventEntity.settlementStateChange(record));
    }
}
