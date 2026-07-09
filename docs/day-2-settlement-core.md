# Day 2 Settlement Core Notes

## What This Slice Proves

This slice turns the project from an in-memory API into a persistence-backed settlement service. The main interview point is reliability under retries:

```text
same idempotency key + same payload = return existing result
same idempotency key + different payload = 409 Conflict
```

## Package Layout

- `domain`: pure Java records and enums. State rules stay here.
- `application`: orchestration, request hashing, ports, exceptions, and state-change events.
- `infrastructure`: adapters for chain submission and database persistence.
- `web`: REST API, validation, request auditing, and error mapping.
- `audit`: audit entities, repository, service, and event listener.

## Idempotency Design

`idempotency_requests.idempotency_key` is the primary key. That database constraint is the final guard against duplicate request processing across multiple API instances.

`request_hash` is computed from the business payload:

- `settlementId`
- `amount`
- `asset`
- `destinationAddress`

The idempotency key is not part of the hash. It is the lookup key used to find the prior request.

## Audit Design

API requests are recorded by a Spring MVC `HandlerInterceptor`, so controller methods do not need to remember to write request audit rows.

Settlement state changes are recorded by publishing `SettlementStateChangedEvent` from the application service and handling it with a Spring `@EventListener`.

## Intentional Gap

The service-level transaction boundary is not implemented yet. That means the next exercise is to wrap idempotency creation, settlement persistence, and idempotency completion in one atomic boundary and explain rollback behavior.
