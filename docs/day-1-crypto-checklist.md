# Day 1 Crypto Checklist

## Goal

Get from zero crypto knowledge to a defensible first explanation of stablecoin settlement and the project you are building.

## 90-Minute Session

### 0-15 Minutes: Mental Model

Read `docs/crypto-primer.md` through "Payment UX Versus Settlement."

Answer out loud:

- What is a blockchain in backend engineering terms?
- What is a stablecoin?
- Why is settlement different from checkout/payment UX?

### 15-35 Minutes: Risk Model

Read `docs/security-notes.md`.

Answer out loud:

- Why are private keys dangerous?
- Why should a backend service not directly hold production signing keys?
- What is replay protection?
- Why does confirmation policy matter?

### 35-60 Minutes: Project Mapping

Sketch this flow in notes:

1. Client calls `POST /settlement-intents`.
2. API stores an idempotency key and settlement intent.
3. Worker validates and submits to `ChainSettlementAdapter`.
4. Adapter returns a transaction hash or simulated reference.
5. Reconciliation checks status and marks the intent settled.

Explain:

- where duplicate prevention happens
- where retries happen
- where human review happens
- where observability happens

### 60-75 Minutes: Interview Drill

Practice these answers:

- Explain stablecoin settlement to a backend engineer.
- Explain why 24/7 settlement still needs reconciliation.
- Explain how you would prevent double settlement.
- Explain what you built versus what is simulated.

### 75-90 Minutes: Write A Tiny Project Pitch

Use this structure:

```text
I am building a learning project that models stablecoin settlement as a backend orchestration problem.
The important parts are idempotent APIs, a ledger-backed state machine, a chain adapter, reconciliation, and observability.
I am starting with a mock chain adapter so I can first get the reliability model right before adding a smart contract.
```

## Definition Of Done

- You can define stablecoin, wallet, private key, smart contract, EVM, confirmation, reorg, and reconciliation.
- You can explain payment UX versus settlement.
- You can explain why idempotency matters for settlement.
- You can clearly say you are building a learning project, not claiming production crypto experience.

## Self-Score

Score each from 1-5:

- Vocabulary comfort:
- Settlement mental model:
- Security risk awareness:
- Interview explanation:
- Confidence level:
