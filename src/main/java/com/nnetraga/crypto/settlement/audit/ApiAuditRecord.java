package com.nnetraga.crypto.settlement.audit;

import java.time.Instant;

public record ApiAuditRecord(
        String httpMethod,
        String requestPath,
        int responseStatus,
        String message,
        Instant occurredAt) {
}
