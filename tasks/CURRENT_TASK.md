# CURRENT TASK

## Phase

Phase 1 - Kotlin Spring Boot Modular Monolith

## Goal

Create the initial Kotlin Spring Boot project skeleton.

This is the first implementation task after the AI AGENT working documents are added.

## Scope

Create a minimal multi-module Gradle Kotlin DSL project.

Initial modules:

```text
common
commerce-monolith
```

## Requirements

- Use Kotlin.
- Use Spring Boot.
- Use Gradle Kotlin DSL.
- Configure root build files.
- `commerce-monolith` must be executable.
- Add a simple health check endpoint.
- Add minimal common response classes in `common`.
- Add a basic test that verifies the application context or health endpoint.

## Suggested Health API

```http
GET /health
```

Expected response can be simple:

```json
{
  "success": true,
  "data": "OK"
}
```

## Do Not

- Do not create separate microservice modules yet.
- Do not add Kafka.
- Do not add Redis.
- Do not add Eureka.
- Do not add API Gateway.
- Do not add Load Balancer.
- Do not add CQRS.
- Do not add Event Sourcing.
- Do not add gRPC.
- Do not add Docker Compose unless explicitly requested.

## Done When

- The project builds successfully.
- The monolith application can run.
- `/health` returns OK.
- Relevant tests pass.
- Changed files are summarized.
- Any assumptions are documented.
