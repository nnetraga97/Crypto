# Crypto Settlement Security Notes

These notes frame the risk model for the learning project. They are intentionally conservative.

## What Must Never Happen

- The same settlement instruction is submitted twice.
- A private key is logged, committed, or exposed to application code unnecessarily.
- An unconfirmed onchain transaction is treated as final.
- The internal ledger says "settled" when the chain transaction failed.
- A webhook/event is processed multiple times and creates duplicate ledger entries.
- A retry loop keeps moving money instead of retrying the status check.

## Key Handling

For a learning project, use a mock signer or local test key only.

For a production design, discuss:

- managed custody or HSM-backed signing
- strict separation between API service and signing service
- audit logs for every signing request
- approval policies for high-value movement
- secret rotation and emergency revocation

Interview wording:

> I would not let a general API service directly hold production private keys. I would isolate signing behind a dedicated service or custody provider with auditability and policy controls.

## Replay Protection

Replay means a valid instruction or transaction is reused when it should only be accepted once.

Controls:

- unique settlement instruction ID
- idempotency key at the API boundary
- unique database constraint on settlement instruction ID
- chain ID included in signed payloads
- contract-level duplicate rejection when using a smart contract
- reconciliation job that checks internal ledger versus onchain events

## Confirmation Policy

Do not mark a transaction as final just because it was submitted.

Possible states:

- submitted
- seen onchain
- included in block
- enough confirmations
- reconciled

The number of confirmations is a business and risk decision. Higher confirmation counts reduce reorg risk but increase settlement latency.

## Smart Contract Testing

Minimum tests:

- accepts a new instruction
- rejects duplicate instruction ID
- emits an event with the instruction ID and hash
- rejects invalid caller if access control exists
- handles boundary values

## Operational Controls

Add alerts for:

- high failure rate
- stuck `CONFIRMING` transactions
- mismatch between internal ledger and chain events
- duplicate idempotency attempts above normal baseline
- signing service unavailable

## Honest Resume/Interview Boundary

Say:

> Built a learning project that models stablecoin settlement integration patterns.

Do not say:

> Built a production crypto settlement system.

The truth is strong enough. The interview value is in how clearly you reason about reliability, security, and tradeoffs.
