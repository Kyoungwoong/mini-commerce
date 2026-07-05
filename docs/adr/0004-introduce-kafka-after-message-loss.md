# ADR-0004: Introduce Kafka After Reproducing Message Loss

## Status

Accepted

## Context

Kafka should not be introduced just because it is common in MSA.

The project should first show a real problem:

```text
order-service calls notification-service through REST
notification-service is down
notification request is lost
```

## Decision

Start with REST-based notification.

After reproducing request/message loss, introduce Kafka for asynchronous event delivery.

## Consequences

Positive:

- Kafka has a clear reason to exist in the project.
- The blog can explain the transition from synchronous REST to asynchronous events.
- Idempotency and offset handling can be introduced naturally.

Trade-offs:

- Notification implementation will be changed later.
- There will be an intentionally flawed intermediate state.

## Alternatives Considered

- Use Kafka from the beginning
- Use RabbitMQ
- Use database polling without Kafka
