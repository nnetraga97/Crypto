# Day 1 Crypto Basics Plan

Goal: make the first crypto session approachable for someone starting from zero while still mapping to staff-level backend interview signals.

Steps:

1. Add a beginner crypto/payments primer.
2. Add security notes that frame the risks without overclaiming production crypto experience.
3. Add a Day 1 checklist with explain/defend/drill prompts.
4. Keep all wording company-neutral.

Open questions:

- Whether to implement a Solidity contract today or first keep the adapter mocked until the mental model is solid.

## API lifecycle completion

Goal: fill in the repetitive REST lifecycle routes after the create/read endpoint is understood.

Steps:

1. Add command-style endpoints for confirming, settled, and needs-review transitions.
2. Map invalid state transitions to HTTP 409 Conflict.
3. Add focused API tests for the happy path, missing keys, and invalid transitions.

## Idempotency request model

Goal: model the durable idempotency record before adding database-backed storage.

Steps:

1. Add tests for required idempotency key and request hash.
2. Represent idempotency state with `IdempotencyStatus`, not a raw string.
3. Add completed and failed transitions for request processing.
