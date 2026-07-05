# ADR-0005: Add CQRS and Event Sourcing in a Late Phase

## Status

Accepted

## Context

CQRS and Event Sourcing are valuable, but they add significant complexity.

The project should first have:

- Basic order flow
- Service separation
- Kafka events
- Saga/Outbox

Only then will the pain points around query complexity and state history be visible.

## Decision

Add CQRS after order query complexity appears.

Add Event Sourcing after order state history and replay become meaningful.

Apply Event Sourcing only to order state changes at first.

## Consequences

Positive:

- CQRS/Event Sourcing will be introduced for a clear reason.
- The project avoids becoming too complex too early.
- Blog readers can compare state-based persistence and event-based persistence.

Trade-offs:

- Order logic will be refactored in a later phase.
- Projection consistency and event replay must be explained carefully.

## Alternatives Considered

- Skip CQRS/Event Sourcing entirely
- Use CQRS from the beginning
- Use Event Sourcing for every domain
