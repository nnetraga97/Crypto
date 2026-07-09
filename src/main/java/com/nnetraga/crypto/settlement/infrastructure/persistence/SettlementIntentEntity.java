package com.nnetraga.crypto.settlement.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.Instant;

import com.nnetraga.crypto.settlement.domain.SettlementIntent;
import com.nnetraga.crypto.settlement.domain.SettlementStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "settlement_intents")
public class SettlementIntentEntity {
    @Id
    @Column(name = "settlement_id", nullable = false, length = 80)
    private String settlementId;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 120)
    private String idempotencyKey;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "asset", nullable = false, length = 20)
    private String asset;

    @Column(name = "destination_address", nullable = false, length = 200)
    private String destinationAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 40)
    private SettlementStatus status;

    @Column(name = "chain_transaction_hash", length = 160)
    private String chainTransactionHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    protected SettlementIntentEntity() {
    }

    private SettlementIntentEntity(SettlementIntent intent) {
        this.settlementId = intent.settlementId();
        this.idempotencyKey = intent.idempotencyKey();
        this.amount = intent.amount();
        this.asset = intent.asset();
        this.destinationAddress = intent.destinationAddress();
        this.status = intent.status();
        this.chainTransactionHash = intent.chainTransactionHash();
        this.createdAt = intent.createdAt();
        this.updatedAt = intent.updatedAt();
    }

    public static SettlementIntentEntity fromDomain(SettlementIntent intent) {
        return new SettlementIntentEntity(intent);
    }

    public void apply(SettlementIntent intent) {
        this.idempotencyKey = intent.idempotencyKey();
        this.amount = intent.amount();
        this.asset = intent.asset();
        this.destinationAddress = intent.destinationAddress();
        this.status = intent.status();
        this.chainTransactionHash = intent.chainTransactionHash();
        this.createdAt = intent.createdAt();
        this.updatedAt = intent.updatedAt();
    }

    public SettlementIntent toDomain() {
        return new SettlementIntent(
                settlementId,
                idempotencyKey,
                amount,
                asset,
                destinationAddress,
                status,
                chainTransactionHash,
                createdAt,
                updatedAt);
    }
}
