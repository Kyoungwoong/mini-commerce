# CODE REVIEW RULES

Use this file to review AI Agent output.

The goal is not only "does it work?" but also "is this implementation appropriate for the current learning phase?"

## 1. Phase Guard

Check:

- Does the change match `tasks/CURRENT_TASK.md`?
- Does the change respect `docs/ROADMAP.md`?
- Did it introduce future-phase infrastructure too early?

Reject or revise if:

- Kafka appears before the Kafka phase.
- Eureka appears before the discovery phase.
- gRPC appears before REST internal calls exist.
- CQRS/Event Sourcing appears before the basic order workflow exists.
- Spring Cloud Gateway or Spring Cloud LoadBalancer is used during custom implementation phases.

## 2. Scope Control

Check:

- Are changes limited to the requested files/modules?
- Are unrelated refactors avoided?
- Did the implementation create unnecessary abstractions?

Good:

- Small, focused changes.
- Clear names.
- Easy-to-review diff.

Bad:

- Giant refactor mixed with feature work.
- Generic framework introduced without need.
- Hidden behavior that is hard to explain.

## 3. Kotlin Quality

Check:

- Kotlin null-safety is used clearly.
- Data classes are used for DTOs when appropriate.
- Domain code is readable.
- Extension functions are not overused.
- Scope functions are used only when they improve clarity.

Prefer:

- Explicit names.
- Simple control flow.
- Clear domain language.

Avoid:

- Clever one-liners.
- Too much magic.
- Overly nested lambdas.

## 4. Spring Boot Structure

Check:

- Controller code does not contain domain logic.
- Service/application code does not depend unnecessarily on web details.
- Repository code is isolated.
- Configuration code is explicit.

Expected layering:

```text
controller
dto
application/service
domain
infrastructure/repository
configuration
```

This can be simplified in early phases.

## 5. API Design

Check:

- API paths are versioned when public-facing.
- Response format follows common response rules if already introduced.
- Error format includes code and message.
- Trace ID is included after tracing is introduced.

Example:

```json
{
  "success": false,
  "code": "PRODUCT_NOT_FOUND",
  "message": "Product not found",
  "traceId": "..."
}
```

## 6. Test Review

Check:

- Business logic changes have tests.
- Tests are focused and readable.
- Test names describe behavior.
- Integration tests are not introduced unnecessarily early.

If tests were not added, require an explanation.

## 7. Documentation Review

Check:

- ADR added for architecture decisions.
- Roadmap updated if phase changes.
- Current task status updated when appropriate.
- Blog notes added if the change is part of a blog episode.

## 8. Security Review

Check:

- No real secrets.
- No hardcoded real credentials.
- `.env.example` uses fake values.
- JWT secret examples are fake.
- Redis/Kafka/DB passwords are not committed as real secrets.

## 9. Failure Behavior

For MSA features, check:

- What happens if the target service is down?
- Is there a timeout?
- Is an error response clear?
- Is retry safe?
- Is duplicate message handling considered?

## 10. Review Summary Template

Use this when reviewing a task:

```text
Review Summary

✅ Good
- ...

⚠️ Needs attention
- ...

❌ Must fix
- ...

Phase compliance
- ...

Test/verification
- ...

Documentation
- ...

Decision
- Approve / Request changes
```
