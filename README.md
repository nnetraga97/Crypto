# Crypto Interview Prep

This repository tracks a focused 5-day preparation plan for staff-level backend and crypto/payments engineering interviews.

## Current Guide

- [Staff Crypto Payments 5-Day Interview Guide](staff-crypto-payments-5-day-interview-guide.md)

## Day 1 Materials

- [Crypto Payments Primer](docs/crypto-primer.md)
- [Crypto Settlement Security Notes](docs/security-notes.md)
- [Day 1 Crypto Checklist](docs/day-1-crypto-checklist.md)
- [Day 1 Coding Guide](docs/day-1-coding-guide.md)
- [Day 2 Settlement Core Notes](docs/day-2-settlement-core.md)

The guide covers:

- Likely interview format and rounds for staff-level backend roles
- Publicly reported question patterns
- Staff-level backend and system-design signals
- A project recommendation: Stablecoin Settlement Orchestration Platform
- A 5-day build, study, and mock-interview schedule

## Project Shape

The current implementation is a Java/Spring Boot settlement orchestration service:

- `domain`: settlement intent, idempotency request, statuses, and state transitions.
- `application`: service orchestration, request hashing, idempotency conflict behavior, and state-change events.
- `infrastructure`: fake chain adapter plus JPA-backed settlement and idempotency repositories.
- `web`: REST controller, validation, exception mapping, and automatic API audit interceptor.
- `audit`: audit event model, repository, service, and state-change listener.

## Local Run

The default profile uses H2 in memory:

```bash
mvn spring-boot:run
```

For local PostgreSQL:

```bash
docker compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

The Docker profile exposes Postgres on host port `15432` to avoid clashing with an existing local database on `5432`.

## API Examples

Create a settlement:

```bash
curl -X POST http://localhost:8080/settlement-intents \
  -H 'Content-Type: application/json' \
  -d '{"settlementId":"settlement-123","idempotencyKey":"idem-123","amount":100.00,"asset":"USDC","destinationAddress":"destination-address"}'
```

Retry with the same key and same payload returns the existing settlement. Reusing the same key with a different payload returns `409 Conflict`.

Move through lifecycle states:

```bash
curl -X POST http://localhost:8080/settlement-intents/idempotency/idem-123/confirming
curl -X POST http://localhost:8080/settlement-intents/idempotency/idem-123/settled
```

## Status

Day 1 crypto basics and Day 2 Spring settlement core are implemented through DB-backed idempotency, request hashing, validation, JPA persistence, Flyway migrations, and automatic audit logging. The service-level transaction boundary around idempotency + settlement creation is intentionally left as the next hands-on exercise.
