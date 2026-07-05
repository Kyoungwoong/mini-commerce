# AGENTS.md

This file is the project constitution for coding agents.

## Project Identity

This repository is a learning-oriented Kotlin MSA project.

We are building a Mini Commerce system step by step:

1. Start with a Kotlin Spring Boot modular monolith.
2. Split services gradually.
3. Build selected MSA infrastructure by hand for learning.
4. Find the problem in the structure in each current step.
5. Introduce productive tools only after problems arise.

The goal isn't just the code that works. 
The goal is the code that allows me to study Kotlin and clearly explain the concept of MSA in a blog post.

## Primary Goals

- Learn Kotlin through a real project.
- Learn MSA by evolving a Mini Commerce system.
- Keep each phase small, reviewable, and blog-friendly.
- Prefer simple implementations first, then refactor intentionally.
- Record important architecture decisions.
- When you proceed with the refactoring at each stage, make a note of what your current problem is, how to solve it, and what skills (design patterns, etc.) you use.

## Tech Stack

Default stack:

- Language: Kotlin
- Framework: Spring Boot
- Build: Gradle Kotlin DSL
- Database: H2, PostgreSQL
- Persistence: Spring Data JPA
- API Docs: OpenAPI / Swagger
- Auth: JWT
- Cache/Auth store: Redis, later Redis Sentinel
- Messaging: Kafka
- Service Discovery: custom registry first, then Netflix Eureka
- Observability: Trace ID first, then OpenTelemetry / Prometheus / Grafana
- Local runtime: Docker Compose
- Internal RPC later: gRPC
- etc: SAGA Pattern, OutBox Pattern
- Validation: Jakarta Bean Validation
- Test: JUnit 5, MockK, Spring Boot Test
- Integration Test later: Testcontainers
- HTTP Client: Spring RestClient / WebClient

## Tech Stack Rule

Do not introduce all technologies at the beginning.

Use technologies only when the current roadmap phase requires them.

For example:

- H2 is used only for the initial monolith and simple tests.
- PostgreSQL or MySQL is introduced when service databases become meaningful.
- Redis is introduced during the authentication phase.
- Redis Sentinel is introduced only after Redis becomes a single point of failure.
- Kafka is introduced only after the REST-based notification message-loss scenario is reproduced.
- gRPC is introduced only after REST-based internal calls exist.
- CQRS/Event Sourcing is introduced only after the basic order flow and query complexity exist.

## Roadmap Discipline

Do not jump ahead.

Follow the project roadmap in `docs/ROADMAP.md`.
You always must have to follow the step.

If the current task is in an early phase, do not introduce later-phase infrastructure.

Examples:

- Do not add Kafka before the message-loss scenario is implemented.
- Do not add Eureka before custom registry or static routing is implemented.
- Do not add Redis Sentinel before single Redis auth behavior is implemented.
- Do not add gRPC before REST-based internal calls exist.
- Do not add CQRS/Event Sourcing before the basic order flow exists.
- Do not add Kubernetes unless explicitly requested.

## Hard Restrictions

During custom gateway and load balancer phases:

- Do not use Spring Cloud Gateway.
- Do not use Spring Cloud LoadBalancer.
- Do not hide routing or load-balancing behavior inside a framework.
- Implement the learning version explicitly in Kotlin.

Production-grade alternatives may be mentioned in documentation, but should not replace the learning implementation unless the current task explicitly asks for that comparison.
n addition, when introducing a certain technology, I will introduce it by presenting an explanation and a reason to me except for kafka, docker compose

## Coding Principles

- Prefer readable Kotlin over clever Kotlin.
- Use Kotlin idioms where they improve clarity.
- Avoid unnecessary abstraction.
- Avoid premature generic frameworks.
- Keep domain logic separate from web/controller code.
- Keep infrastructure code separate from domain logic.
- Favor small functions and explicit names.
- Make behavior easy to explain in a blog post.

## Package and Module Guidelines

Early project shape:

```text
common
commerce-monolith
```

Later project shape:

```text
common
member-service
product-service
order-service
payment-service
notification-service
custom-api-gateway
custom-loadbalancer
service-discovery
etc...
```

Do not create all final modules unless the current phase asks for them.

## Documentation Rules

Update documentation when a task changes architecture, behavior, or project direction.

Important docs:

- `docs/ROADMAP.md`
- `docs/CODE_REVIEW_RULES.md`
- `docs/adr/`
- `tasks/CURRENT_TASK.md`

When an architecture decision is made, add or update an ADR.

When the implementation creates a useful blog story, add short blog notes to the task result or related prompt log.

## Testing Rules

When business logic changes:

- Add or update unit tests.
- Add integration tests only when infrastructure is involved.
- Keep tests understandable for blog readers.
- Prefer focused tests over broad fragile tests.

If tests cannot be run, clearly explain why.

## Build and Verification

Use repository-provided commands when available.

Expected commands may include:

```bash
./gradlew build
./gradlew test
```

## Task Workflow

For every task:

1. Read `tasks/CURRENT_TASK.md`.
2. Read relevant docs and ADRs.
3. Identify the current phase.
4. Implement only the requested scope.
5. Avoid unrelated refactoring.
6. Run relevant build/tests.
7. Summarize changed files.
8. Report anything not completed.
9. Suggest the next small task if appropriate.

## Completion Criteria

A task is complete only when:

- The requested scope is implemented.
- The code compiles, or the reason it cannot compile is clearly explained.
- Relevant tests are added or updated.
- No unrelated infrastructure is introduced.
- Documentation is updated when needed.
- The implementation can be explained in a blog post.

## Communication Style for Task Reports

Use this format:

```text
Summary
- ...

Changed files
- ...

Verification
- ...

Not done / risks
- ...

Next suggested task
- ...
```

## Security and Secrets

- Do not commit real secrets.
- Use `.env.example` for sample values.
- Use obviously fake values in documentation.
- Do not hardcode real credentials in source code.

## Blog-Aware Development

This project is also a blog series.

When implementing features, prefer a sequence that creates a clear learning narrative:

```text
problem appears
→ limitation is observed
→ small solution is implemented
→ trade-off is documented
→ next problem becomes visible
```

Do not produce a final perfect architecture too early.
