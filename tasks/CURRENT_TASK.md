# CURRENT TASK

## Phase

Phase 2-5 - Mini Commerce Domain in Monolith: Notification (Synchronous Baseline)

## Goal

Add a minimal Notification domain to `commerce-monolith` and observe the consequences of a synchronous, in-process notification call after a successful Fake Payment.

**Learning order:** implement a simple baseline -> reproduce coupling/latency in tests -> document observations -> introduce a solution only in a later task when justified.

This task is **not** microservice extraction and **not** Kafka integration.

## Context / Prerequisites

- Existing Gradle modules: `common`, `commerce-monolith`.
- Existing domains: Member, Product, Order, Fake Payment.
- Only `commerce-monolith` is executable; domains currently share one application and database.
- Fake Payment may already have been refactored to hide simulated approval/failure behind a server-side fake gateway. **Inspect current code first and preserve that design.** Do not reintroduce `shouldFail` as a public request field.
- If the Order/Payment changes are not on the target branch or do not build, report the blocker before implementing.
- Before coding, read `AGENTS.md`, `docs/ROADMAP.md`, `tasks/CURRENT_TASK.md`, `docs/CODE_REVIEW_RULES.md`, and the existing Order/Payment sources and tests.

## Scope

Allowed changes:

- `commerce-monolith/src/main/**` for the Notification domain and a minimal Payment integration point.
- `commerce-monolith/src/test/**` for happy-path, error, rollback, and latency experiments.
- `common/**` and Gradle files only for small, justified, backward-compatible changes.

Keep existing Member/Product/Order behavior unchanged except for a strictly necessary minimal integration adjustment. Do not create new Gradle modules.

## Domain Responsibilities

- Payment decides **when** a notification is required: only after a Fake Payment is **approved**.
- Notification records a payment-approved notification **synchronously** in the existing database.
- A stored record is **not** proof that a real SMS, email, push, or network notification was delivered. Use a truthful name/status (e.g., `RECORDED` or no delivery status); do not claim `SENT`.
- Notification must not contain PG credentials, personal contact information, or real sending behavior.

Suggested package (adapt to the repository's actual conventions):

```text
com.minicommerce.commerce.notification
  controller/
  dto/
  application/
  domain/
  infrastructure/
```

Suggested minimal persisted fields:

```text
id
memberId
orderId
type = PAYMENT_APPROVED
message
createdAt
```

## API

Do not create a public API for triggering notification creation. Creation is invoked only from Payment business logic.

For inspection, expose one read-only API:

```http
GET /api/v1/notifications?memberId=1
```

Example response (IDs and time are illustrative; use the existing common response type):

```json
{
  "success": true,
  "data": [
    {
      "notificationId": 1,
      "memberId": 1,
      "orderId": 1,
      "type": "PAYMENT_APPROVED",
      "message": "주문 1의 결제가 승인되었습니다."
    }
  ]
}
```

Use appropriate DTOs; do not return JPA entities directly. Follow existing API validation/error conventions. No pagination required for this small learning phase; document the limitation.

## Functional Flow

```text
POST /api/v1/payments
  -> existing fake gateway returns approval
  -> persist Payment APPROVED
  -> change Order to PAID
  -> call NotificationService.recordPaymentApproved(...) directly
  -> persist Notification record
  -> return payment response
```

When fake payment is rejected, preserve the existing Payment/Order failure handling and **do not** record a payment-approved notification.

## Transaction and Failure Experiment

For this phase, **intentionally use the existing synchronous in-process call inside the payment transaction boundary** where the current code permits it. Do not introduce async or `afterCommit` processing yet.

Document and demonstrate the current coupling, not an imaginary distributed message loss:

1. **Happy path:** approval updates Payment/Order and creates one Notification record.
2. **Business rejection:** Fake Payment failure updates its business state according to current design, creates no approved notification, and must not be treated as a notification system error.
3. **Notification exception:** simulate an unchecked exception thrown by the notification collaborator in an **automated test**. For a shared default transaction, this should roll back the new Payment approval and Order status change. Assert database state (Order remains payable, no newly approved Payment). If the current code behaves differently, report the actual transaction boundary and reason; do not silently weaken assertions.
4. **Notification latency:** simulate a slow notification collaborator using test-only stubs/mocks or test configuration. Demonstrate that synchronous notification completion delays the payment request; avoid production delay code and flaky tight timing thresholds.

**Important distinctions:**

- The Order-creation transaction was committed in an earlier HTTP request. A failure in a later Payment request must not be described as rolling back the original order creation or stock reduction.
- Persisting a notification row in the same DB is not external delivery, and testing a thrown exception here is not equivalent to Kafka message loss.
- Do not swallow notification exceptions in this baseline. We want to observe the coupling and capture evidence before redesigning it.

## Tests (minimum)

- Approved payment -> Payment APPROVED, Order PAID, one notification for the right member/order.
- Failed/rejected fake payment -> no approval notification; existing failed-payment behavior preserved.
- Notification read endpoint -> registered record is returned using common response wrapper.
- Notification throws unchecked exception -> rollback of approval changes within the actual payment transaction; no success response.
- Existing Order/Payment/Member/Product tests still pass.
- One clear experiment or test showing synchronous latency impact; report method and observation.

Use current project test conventions (JUnit, MockK/Mockito, integration tests, etc.). Avoid adding a new test library merely for convenience.

## Do Not

- No `notification-service` or new Gradle module.
- No REST/gRPC service-to-service calls for Notification yet.
- No email, SMS, push provider or production credentials.
- No Kafka, RabbitMQ, Outbox, Saga, CQRS, Event Sourcing, Redis, Eureka, Gateway, Load Balancer, Docker Compose, or new infrastructure.
- No `@Async`, coroutines, Spring events, scheduled retries, circuit breakers, or `REQUIRES_NEW`/`afterCommit` as a premature solution.
- No publicly accessible `shouldFail`, `delayMs`, `simulateError`, or debugging endpoints for injecting notification failures.
- No sweeping changes to Payment, Order, or the fake PG abstraction.

## Verification

Run:

```bash
./gradlew test
./gradlew build
```

If the project supports running the app, verify:

1. Create member and product (or use existing test fixtures).
2. Create order.
3. Approve Fake Payment via the **current** supported API.
4. Query `GET /api/v1/notifications?memberId={memberId}`.
5. Confirm the notification record matches the paid order.

For failure and latency experiments, prefer tests rather than publicly exposed switches.

## Done When

- Direct in-process Notification call happens after payment approval.
- Notification is persisted and queryable; its status does not falsely claim external delivery.
- Failed business payment does not create an approval notification.
- A test reproduces and documents notification-failure transaction coupling.
- Latency effect is demonstrated without production fault-injection endpoints.
- Existing functionality remains intact, tests and build pass.
- No infrastructure/future phase is introduced.
- The report distinguishes observed results from unverified assumptions and captures problems to revisit after service separation.

## Report Format

```text
Summary
Changed files
Verification (commands and results)
Observed coupling / failure / latency
Not done / risks
Next suggested task
```
