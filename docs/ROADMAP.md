# ROADMAP

This roadmap defines the intended learning sequence.

Do not skip phases unless the user explicitly changes the plan.

## Phase 0. Vibe Coding Setup

### Goal

Prepare the repository so coding agent can work consistently.

### Deliverables

- `AGENTS.md`
- `docs/ROADMAP.md`
- `docs/CODE_REVIEW_RULES.md`
- `docs/adr/`
- `tasks/CURRENT_TASK.md`
- `.agents/skills/`
- `.prompts/`

### Do Not

- Do not implement application code yet unless the current task requests it.

---

## Phase 1. Kotlin Spring Boot Modular Monolith

### Goal

Start with a simple modular monolith.

### Modules

```text
common
commerce-monolith
```

### Features

- Health check endpoint
- Common response model
- Common error model
- Basic package structure

### Do Not

- Do not create multiple deployable services yet.
- Do not add Kafka.
- Do not add Redis.
- Do not add Eureka.
- Do not add custom gateway yet.

---

## Phase 2. Mini Commerce Domain in Monolith

### Goal

Implement the first business flow inside the monolith.

### Domains

- Member
- Product
- Order
- Payment
- Notification

### Features

- Member signup/login mock or simple implementation
- Product registration/list/detail
- Order creation
- Fake payment
- Direct notification call

### Learning Point

Experience the convenience and limitation of a monolith.

---

## Phase 3. Service Separation

### Goal

Split the monolith gradually.

### Order

1. `member-service`
2. `product-service`
3. `order-service`
4. `payment-service`
5. `notification-service`

### Rules

- Each service owns its own database later.
- Other services must not access another service's database directly.
- Start with REST for service-to-service calls.

---

## Phase 4. Custom API Gateway

### Goal

Build a learning API Gateway manually.

### Features

- Static route mapping
- Request forwarding
- Header forwarding
- Trace ID generation
- Simple filter chain
- Common error response

### Hard Restriction

Do not use Spring Cloud Gateway in this phase.

---

## Phase 5. Custom Load Balancer

### Goal

Build load-balancing behavior manually.

### Algorithms

1. Round Robin
2. Random
3. Weighted Round Robin
4. Health-check-based exclusion

### Hard Restriction

Do not use Spring Cloud LoadBalancer in this phase.

---

## Phase 6. Service Discovery

### Goal

Stop hardcoding service instance addresses.

### Steps

1. Use static instance list.
2. Build a simple custom service registry.
3. Introduce Netflix Eureka.
4. Keep custom load balancer selection logic visible.

### Learning Point

Eureka provides service instance discovery. The load-balancing decision can still be our own learning implementation.

---

## Phase 7. Authentication with Redis

### Goal

Move authentication checks to the gateway.

### Features

- JWT access token
- Refresh token storage in Redis
- Token blacklist or logout handling
- Rate-limit counter option

### Later

- Redis master/replica
- Redis Sentinel
- Failover test

---

## Phase 8. Message Loss Scenario and Kafka

### Goal

Experience why asynchronous messaging is needed.

### Scenario

```text
order-service
→ REST call
→ notification-service down
→ notification request is lost
```

### Solution

Introduce Kafka:

```text
order-service
→ OrderCreated event
→ Kafka
→ notification-service
```

### Required Concepts

- Producer
- Consumer
- Topic
- Consumer group
- Offset
- Idempotency

---

## Phase 9. Saga and Outbox

### Goal

Handle order-payment workflow across services.

### Features

- Order state machine
- Payment success/failure
- Compensation behavior
- Outbox table
- Outbox publisher

### Learning Point

Database transaction and event publishing must be coordinated.

---

## Phase 10. Resilience

### Goal

Prevent failure propagation.

### Features

- Timeout
- Retry with backoff
- Custom Circuit Breaker for learning
- Fallback response
- Failure metrics

### Later Comparison

Compare custom implementation with Resilience4j.

---

## Phase 11. Observability

### Goal

Make distributed behavior visible.

### Features

- Trace ID propagation
- Structured logs
- Metrics
- OpenTelemetry
- Prometheus
- Grafana

---

## Phase 12. Docker Compose Runtime

### Goal

Run the full local MSA environment with one command.

### Components

- Services
- Databases
- Redis/Sentinel
- Kafka
- Eureka
- Observability tools

---

## Phase 13. CQRS

### Goal

Separate write model and read model when order queries become complex.

### Trigger Problem

Order list/detail pages need data from multiple services.

### Features

- Command model
- Query model
- Projection table
- Kafka-based projection update

---

## Phase 14. Event Sourcing

### Goal

Store order state changes as events.

### Scope

Apply Event Sourcing only to order state changes at first.

### Features

- Event store
- Aggregate replay
- Snapshot as optional later step

---

## Phase 15. gRPC for Selected Internal Calls

### Goal

Replace selected internal REST calls with gRPC when REST limitations become visible.

### Rules

- External API remains REST.
- Gateway remains REST-facing.
- Start with `order-service → product-service`.
- Add `order-service → payment-service` later.

---

## Phase 16. Final Architecture Review

### Goal

Compare the original monolith with the final MSA architecture.

### Deliverables

- Architecture diagram
- Trade-off summary
- Blog series retrospective
- What would be done differently in production
