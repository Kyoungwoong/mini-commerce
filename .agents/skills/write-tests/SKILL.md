# Skill: Write Tests

Use this skill when adding or improving tests.

## Test Philosophy

Tests should be readable enough to support the blog series.

Prefer behavior-focused names.

Good:

```text
creates order when product has enough stock
returns error when product does not exist
```

Bad:

```text
test1
orderTest
```

## Test Selection

Use the smallest effective test.

| Situation | Preferred Test |
|---|---|
| Pure domain logic | Unit test |
| Application service logic | Unit test with mocks/fakes |
| Controller request/response | Controller test |
| Repository query | Repository slice test |
| DB/Kafka/Redis behavior | Integration test |

## Rules

- Do not introduce Testcontainers until infrastructure is part of the current phase.
- Do not over-mock simple domain objects.
- Keep test data explicit.
- Avoid brittle time/order assumptions unless the behavior requires it.

## Checklist

- [ ] Tests cover the requested behavior
- [ ] Tests fail without the implementation
- [ ] Tests are readable
- [ ] Tests are in the correct module
- [ ] Tests do not require future-phase infrastructure
