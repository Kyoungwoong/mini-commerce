# ADR-0002: Start with a Modular Monolith

## Status

Accepted

## Context

The final goal is MSA, but starting with many services immediately can hide why MSA patterns are needed.

A modular monolith lets us first understand the domain and then experience pain points before splitting services.

## Decision

Start with a Kotlin Spring Boot modular monolith.

Initial modules:

```text
common
commerce-monolith
```

Inside the monolith, keep domain packages separated:

```text
member
product
order
payment
notification
```

## Consequences

Positive:

- Easier initial development.
- Easier debugging.
- Clearer blog narrative.
- Service boundaries can be discovered before physical separation.

Trade-offs:

- The first version is not MSA.
- Some code may need to move later during service extraction.

## Alternatives Considered

- Start directly with microservices
- Start with separate repositories
- Start with a single package without modular boundaries
