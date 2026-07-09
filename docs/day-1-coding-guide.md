# Day 1 Coding Guide

You are driving the implementation. This scaffold only gives you the Maven project and package structure.

## Package

Use:

```text
com.nnetraga.crypto.settlement
```

## First Exercise

Create:

```text
src/main/java/com/nnetraga/crypto/settlement/SettlementStatus.java
src/main/java/com/nnetraga/crypto/settlement/SettlementIntent.java
```

`SettlementStatus` should include:

```text
CREATED
VALIDATED
SUBMITTED
CONFIRMING
SETTLED
FAILED
NEEDS_REVIEW
```

`SettlementIntent` should include:

```text
settlementId
idempotencyKey
amount
asset
destinationAddress
status
chainTransactionHash
createdAt
updatedAt
```

Suggested types:

- `String` for IDs, asset, address, and transaction hash
- `BigDecimal` for amount
- `Instant` for timestamps
- `SettlementStatus` for status

## Rules To Enforce

- New intents start in `CREATED`.
- Amount must be positive.
- ID, idempotency key, asset, and destination address cannot be blank.
- Chain transaction hash can be empty at creation time.

## First Tests

Create:

```text
src/test/java/com/nnetraga/crypto/settlement/SettlementIntentTest.java
```

Test:

- new intent starts as `CREATED`
- negative amount is rejected
- blank destination is rejected
- blank idempotency key is rejected

## Run

```bash
mvn test
```
