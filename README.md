# Stacked PR Experiment

This project is a minimal Spring Boot application used to test how stacked PR review flows work for AI-generated changes.

## Project structure

- Controller layer: `task.controller`
- Service layer: `task.service`
- Logic layer: `task.logic`
- Entity layer: `task.entity`
- Repository layer: `task.repository`

## Local quick checks

```bash
docker compose up -d
./mvnw test
./mvnw spring-boot:run
docker compose down
```

The application and tests use PostgreSQL. The default local connection is
`jdbc:postgresql://localhost:5432/prtest` with user and password `prtest`.
These values can be overridden with `SPRING_DATASOURCE_*` environment
variables.

See [docs/testing-strategy.md](docs/testing-strategy.md) for the test
boundaries, local/CI execution policy, and stacked PR review flow.

## GitHub Actions

The CI workflow runs the standard Maven test suite on every push and pull request.

## Experiment idea

Use AI to generate a new feature in a small slice, then review the change with stacked PRs while human reviewers check the layering and integration boundaries.
