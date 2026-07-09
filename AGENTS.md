# Repository Guidelines

## Project Structure & Module Organization

This is a Java/Maven learning project for backend settlement orchestration.

- `src/main/java/com/nnetraga/crypto/settlement/`: core domain model, state transitions, and chain adapter interfaces.
- `src/test/java/com/nnetraga/crypto/settlement/`: JUnit tests for domain behavior and adapter boundaries.
- `docs/`: learning notes, Day 1 checklist, coding guide, and security/domain primers.
- `scratchpad/`: short planning notes for contributor/agent work.
- `target/`: generated Maven output; do not commit.

Keep business rules in the domain layer first. Spring or API delivery code should wrap the domain model rather than absorb settlement-state rules.

## Build, Test, and Development Commands

Use Maven from the repository root:

```bash
mvn test
```

Runs the JUnit test suite.

```bash
mvn verify
```

Runs tests and builds the jar. This is the default verification command before committing.

```bash
git status --short --branch
```

Check for unexpected local changes before editing or committing.

## Coding Style & Naming Conventions

Use Java 21 language level as configured in `pom.xml`. Prefer small, explicit domain methods such as `markSubmitted()`, `markConfirming()`, and `markNeedsReview()`. Use 4-space indentation in Java source. Keep class names descriptive and domain-focused: `SettlementIntent`, `ChainSubmissionResult`, `FakeChainSettlementAdapter`.

Avoid adding static-analysis plugins unless they are stable with the local JDK. Keep generated files, IDE settings, build output, and personal documents out of commits.

## Testing Guidelines

Tests use JUnit 5. Name test classes after the behavior under test, for example `SettlementIntentSettledTest`. Test state transitions directly: allowed transition, rejected transition, preserved fields, and updated timestamps where relevant.

Run `mvn verify` before pushing. New state transitions or adapter behavior should include focused tests.

## Commit & Pull Request Guidelines

Use concise imperative commit messages matching the current history:

- `Add settlement intent domain model`
- `Add needs-review settlement transition`
- `Add fake chain settlement adapter`

Pull requests should describe the behavior added, list verification commands, and call out any domain tradeoffs. Keep PRs small enough to review one concept at a time.

## Security & Public Repository Notes

This is a public repository. Keep content company-neutral. Do not commit resumes, private keys, credentials, target-company names, requisition IDs, or direct job links.
