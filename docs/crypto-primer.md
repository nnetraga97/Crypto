# Crypto Payments Primer

This is the Day 1 mental model. The goal is not to become a protocol engineer in one day. The goal is to explain how crypto rails can be used in a payment-settlement system, what risks they add, and how a backend engineer would design around those risks.

## The Core Idea

Traditional payment systems and blockchain systems solve different parts of money movement.

Traditional payment networks are good at:

- consumer authorization
- fraud checks
- merchant acceptance
- bank relationships
- dispute and compliance workflows
- operational trust

Blockchains are good at:

- shared transaction history
- programmable asset movement
- 24/7 transfer availability
- public verification of transaction state
- asset settlement without waiting for a business-day batch window

A crypto payments system usually does not mean the shopper pays directly onchain at checkout. A common enterprise pattern is more boring and more useful: keep the customer experience familiar, but use stablecoins or blockchain rails behind the scenes for settlement, treasury, liquidity, or cross-border movement.

## Vocabulary

### Blockchain

A blockchain is a shared ledger. Instead of one private database owned by one company, many computers agree on the order and validity of transactions.

For interviews, the important phrase is:

> A blockchain is a replicated transaction log with consensus rules.

That makes it easier to connect blockchain ideas to backend engineering concepts you already know: append-only logs, replication, consistency, ordering, and failure handling.

### Crypto Asset

A crypto asset is a digital asset represented on a blockchain. Some assets fluctuate wildly in price. That volatility is bad for payments.

### Stablecoin

A stablecoin is a crypto asset designed to keep a stable value, usually pegged to a fiat currency like the U.S. dollar.

In payment systems, stablecoins are interesting because they can move on blockchain rails while representing a predictable unit of value.

Interview-safe wording:

> Stablecoins are useful for settlement because they combine programmable transfer with a relatively stable unit of account, but they still require controls around reserves, compliance, reconciliation, and operational risk.

### Wallet

A wallet controls private keys. It does not literally store coins like a leather wallet. The assets are recorded onchain; the wallet proves the right to move them.

### Private Key

A private key is the secret that authorizes transactions. If it is leaked, funds can be moved. If it is lost, funds may be unrecoverable.

For backend interviews, private keys should trigger security architecture thoughts:

- never log secrets
- use dedicated custody or key-management systems
- separate duties
- audit all signing events
- rotate keys where possible
- design for least privilege

### Smart Contract

A smart contract is code deployed on a blockchain. It can enforce rules for asset movement or record settlement instructions.

For this project, a smart contract can be intentionally small:

- accept a settlement instruction ID
- store a hash of the instruction
- reject duplicates
- emit an event that offchain services can reconcile

### EVM

EVM means Ethereum Virtual Machine. It is the runtime used by Ethereum and many compatible chains. Solidity is the most common language for writing EVM smart contracts.

### ERC Standards And EIPs

EIPs are Ethereum Improvement Proposals. ERCs are a category of EIPs focused on application-level standards.

The one to know first is ERC-20: a standard interface for fungible tokens. Many stablecoins follow ERC-20-like behavior.

### Confirmation

A confirmation means a transaction has been included in a block and more blocks have been added after it. More confirmations usually mean lower reorg risk.

### Reorg

A reorg happens when the chain's recent history changes because the network settles on a different valid branch. Backend systems must avoid treating a very fresh transaction as final too early.

### Consensus

Consensus is how nodes agree on transaction ordering and validity. You do not need deep protocol math for this project. You should be able to say:

> Consensus is the mechanism that lets independent nodes agree on one ledger history. For application design, I care about finality, latency, transaction cost, and failure/reorg behavior.

## Payment UX Versus Settlement

Do not mix these up.

Payment UX:

- customer taps a card or clicks checkout
- merchant wants immediate approval or decline
- fraud, authorization, and consumer experience matter

Settlement:

- money is moved between financial institutions or partners after transactions are authorized
- reconciliation, liquidity, timing, fees, and operational resilience matter

Crypto rails are often more relevant to settlement than checkout UX. In an interview, this distinction makes you sound much more grounded.

## Why 24/7 Settlement Still Needs Reconciliation

Even if assets can move 24/7, companies still need to answer:

- Did the expected transfer happen?
- Was it for the correct amount?
- Did it settle on the expected chain?
- Did the offchain ledger update exactly once?
- Was the transaction reversed, failed, delayed, or reorged?
- Are internal ledger balances consistent with onchain events?

The backend engineering work is not "send token and done." It is designing a reliable state machine around uncertain external infrastructure.

## A Simple Settlement State Machine

Use this for the project:

1. `CREATED`: API accepted a settlement intent.
2. `VALIDATED`: business rules and balances passed.
3. `SUBMITTED`: transaction was submitted to a chain adapter.
4. `CONFIRMING`: transaction is waiting for enough confirmations.
5. `SETTLED`: transaction is confirmed and reconciled.
6. `FAILED`: terminal failure that requires operator visibility.
7. `NEEDS_REVIEW`: ambiguous state that should not be auto-retried forever.

Good staff-level signal:

> I would avoid a binary success/failure model because external settlement has ambiguous states. The state machine needs explicit review and reconciliation paths.

## The Interview Pitch

Short version:

> I built a learning project that models stablecoin settlement as a backend orchestration problem. The core is not speculation or trading; it is idempotent APIs, a ledger-backed state machine, a chain adapter, reconciliation, and observability.

Defensive version:

> I do not claim production crypto experience. I built this to learn the domain and to show how I would reason about reliable payment settlement, external network risk, and secure integration boundaries.

## What To Learn First

Learn these in order:

1. Stablecoin
2. Wallet and private key
3. Transaction
4. Smart contract
5. EVM and ERC-20
6. Confirmation and reorg
7. Reconciliation
8. Idempotency and double-settlement prevention

That is enough to start the project without drowning in crypto jargon.
