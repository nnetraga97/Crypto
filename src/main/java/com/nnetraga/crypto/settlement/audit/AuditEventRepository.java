package com.nnetraga.crypto.settlement.audit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEventRepository extends JpaRepository<AuditEventEntity, Long> {
    long countByEventType(AuditEventType eventType);
}
