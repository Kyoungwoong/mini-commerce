---
name: write-adr
description: Use when recording an architecture decision in docs/adr, including context, decision, consequences, and alternatives.
---

# Skill: Write ADR

Use this skill when an architecture decision should be recorded.

## Location

Create ADR files under:

```text
docs/adr/
```

## Naming

Use this pattern:

```text
0001-short-title.md
0002-short-title.md
```

## ADR Template

```md
# ADR-0000: Title

## Status

Proposed / Accepted / Superseded

## Context

What problem or decision are we facing?

## Decision

What did we decide?

## Consequences

What improves?

What gets worse?

What should we watch later?

## Alternatives Considered

- Alternative 1
- Alternative 2
```

## Rules

- Keep ADRs short.
- Record why, not only what.
- Mention if the decision is for learning purposes and not production-ready.
- Link the decision to roadmap phase if helpful.

## Common ADR Triggers

- Choosing Kotlin/Spring Boot
- Starting with monolith
- Building custom gateway
- Building custom load balancer
- Introducing Kafka
- Introducing Redis Sentinel
- Introducing CQRS/Event Sourcing
- Introducing gRPC
