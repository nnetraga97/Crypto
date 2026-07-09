# Day 2 Idempotency And Audit Plan

Goal: complete the Day 2 Spring/API slice while leaving the service-level transaction boundary for the user to implement.

Steps:

1. Restructure packages into domain, application, infrastructure, web, and audit.
2. Add JPA, Flyway, validation, H2, PostgreSQL driver, and local Docker Postgres config.
3. Persist settlement intents and idempotency requests with database uniqueness.
4. Add request hash conflict behavior for reused idempotency keys.
5. Add automatic audit logging for API requests and settlement state changes.
6. Add API validation, error mapping, README examples, and tests.

Boundary intentionally left open:

- Do not add a service-level `@Transactional` boundary around idempotency + settlement creation.
