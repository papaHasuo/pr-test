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
./mvnw test
./mvnw spring-boot:run
```

## GitHub Actions

The CI workflow runs the standard Maven test suite on every push and pull request.

## Experiment idea

Use AI to generate a new feature in a small slice, then review the change with stacked PRs while human reviewers check the layering and integration boundaries.
