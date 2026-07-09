package com.nnetraga.crypto.settlement.domain;

public enum SettlementStatus {
    CREATED,
    VALIDATED,
    SUBMITTED,
    CONFIRMING,
    SETTLED,
    FAILED,
    NEEDS_REVIEW
}
