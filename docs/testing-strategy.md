# Testing strategy

## Purpose

This project uses PostgreSQL in the application, local development, and CI.
The goal is to catch both business-rule regressions and integration problems
while keeping each stacked PR small enough to review.

## Test layers

| Test | What it protects | Current state | Where to run |
| --- | --- | --- | --- |
| Logic unit test | Pure state and input rules | No dedicated Logic test exists yet | Local, every PR |
| Service unit test | Use-case orchestration and repository interactions | `TaskServiceTest` covers part of creation behavior; repository-backed cases should be expanded | Local, every PR |
| Controller test | Request mapping, redirect, and model contract | `TaskControllerTest` covers the list view contract | Local, every PR |
| Spring context/integration test | Wiring, JPA mappings, and PostgreSQL compatibility | `PrTestApplicationTests` starts the full application context | Local with PostgreSQL, every PR |
| Browser or full flow test | User-visible task flow | Not added yet; add only when UI behavior becomes the experiment target | CI initially, local when debugging |

Tests should verify behavior at the narrowest layer that can express it.
The full context test is not a substitute for unit tests, and unit tests are
not a substitute for the PostgreSQL-backed context test.

## Local workflow

Start the same database type used by the application and CI:

```bash
docker compose up -d
./mvnw test
./mvnw spring-boot:run
docker compose down
```

The default connection is `prtest` / `prtest` on `localhost:5432`. Override it
with `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and
`SPRING_DATASOURCE_PASSWORD` when needed.

For fast feedback, run a focused test while iterating:

```bash
./mvnw -Dtest=TaskServiceTest test
```

Before opening or updating a PR, run the complete `./mvnw test` suite.

## CI workflow

GitHub Actions starts PostgreSQL 16 as a service container, waits for its
health check, and runs the same Maven test command as local development.
This makes a green CI result meaningful for JPA mappings and database
behavior, not just Java compilation.

CI should remain deterministic and should not depend on a developer's local
database or uncommitted schema changes.

## Stacked PR workflow

Each PR should keep its own tests green:

1. Domain/Logic PR: add transition rules and unit tests.
2. Service PR: add persistence orchestration and repository-backed tests.
3. Controller PR: add endpoint and controller tests.
4. UI PR: add the user-visible operation and a flow-level test if needed.

The PR body should state its base branch, parent PR, test command, and any
known limitation. Reviewers should merge from the bottom of the stack upward.
If a lower PR changes a contract, rebase the descendants, update their tests,
and rerun the full suite before asking for review again.

## Recommended CI evolution

Start with one required `mvnw test` job. Split jobs only when runtime or
failure ownership becomes a problem. Add a separate browser test job after
the first UI flow exists; adding a browser framework before that point would
increase maintenance without improving this experiment.
