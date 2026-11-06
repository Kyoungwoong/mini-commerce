# Prompt 006 - Add Synchronous Notification in Monolith

## Current Phase

Phase 2-5 - Mini Commerce Domain in Monolith: Notification

## Goal

Implement a **minimal synchronous Notification domain** inside `commerce-monolith` and demonstrate its coupling to an approved Fake Payment using automated experiments.

This is an educational baseline. **Do not fix the coupling with Kafka or async processing in this task.**

## Context

This Mini Commerce project deliberately evolves from a modular monolith toward MSA. Existing domains: Member, Product, Order, and Fake Payment. In-process method calls and the current shared database are the starting point.

Before changing code, read and respect:

- `AGENTS.md`
- `docs/ROADMAP.md`
- `tasks/CURRENT_TASK.md`
- `docs/CODE_REVIEW_RULES.md`
- existing Order, Payment, common response, exception handler, and test sources
- relevant `.agents/skills/` instructions as needed

**Important:** inspect the actual Payment API and fake gateway. We previously discussed removing a client-provided `shouldFail` input and putting fake approval/rejection on the server side. Preserve whichever current server-side fake gateway implementation exists; do not recreate client-controlled payment success/failure. If payment is missing, incomplete, or cannot build, describe this before implementation.

## Scope

May change:

- Notification code/tests under `commerce-monolith/**`.
- The smallest required Payment application-service integration point and related tests.
- Backward-compatible common/Gradle changes only if truly necessary.

Must not change unrelated domains or create new service modules.

## Requirements

### A. Minimal Notification model

Implement a persisted record containing at least:

- notification ID
- member ID
- order ID
- notification type, starting with `PAYMENT_APPROVED`
- explanatory message
- created timestamp

Keep delivery semantics accurate: database recording != external sending. Do not call the state `SENT` if nothing is actually delivered.

Use the repository's existing package patterns. Suggested package root: `com.minicommerce.commerce.notification`.

### B. Direct, synchronous integration

After the current Fake Payment approves an order and updates its state to `PAID`, invoke the Notification application service **directly** to record one approved-payment notification.

This call must be a normal synchronous method invocation within the existing monolith, NOT an HTTP request, event bus, async coroutine, or separate thread.

Failed/rejected payments must never create a `PAYMENT_APPROVED` notification. Preserve payment validation and status transitions as they exist.

Avoid a cyclic Order -> Payment -> Notification -> Order service dependency. Pass only the necessary IDs/context to the Notification service.

### C. Read-only inspection endpoint

Add:

```http
GET /api/v1/notifications?memberId={memberId}
```

Return the project's common `ApiResponse` wrapper and DTOs. Keep the endpoint minimal and consistent with current API conventions.

Example (illustrative):

```json
{
  "success": true,
  "data": [
    {
      "notificationId": 1,
      "orderId": 1,
      "memberId": 1,
      "type": "PAYMENT_APPROVED",
      "message": "주문 1의 결제가 승인되었습니다."
    }
  ]
}
```

### D. Transactions and controlled failures

**First implement the intentionally coupled baseline.** If the Payment application service currently owns the transaction that writes Payment/Order state, call Notification inside that same transactional flow. Do not add `try/catch` that masks notification failures or premature async/compensating logic.

Write automated tests that simulate a Notification collaborator throwing an unchecked exception and verify what happened to the DB transaction. Under a common default Spring transaction, approval changes should be rolled back. Make sure the order-creation transaction (already committed in a separate request) is not incorrectly expected to roll back.

Simulate notification latency only in tests using an injected stub, mocked dependency, or controlled test configuration. Show that the payment request waits for the synchronous collaborator. Prefer coordination-based or comfortably tolerant assertions over brittle timing thresholds. No production `Thread.sleep` or failure flag exposed to clients.

### E. Tests

Required:

1. Successful payment creates exactly one notification for the order/member.
2. Failed Fake Payment creates no approved notification.
3. Read endpoint returns the newly created notification.
4. Unchecked notification failure demonstrates transaction coupling/rollback, with DB assertions.
5. Existing tests pass and Payment API remains backward compatible.
6. Latency demonstration recorded as a focused test or reproducible test observation.

Use existing JUnit/MockK/Mockito conventions. Reuse current exception handling; do not create duplicate global handlers.

## Do Not

- Do not create `notification-service` or additional Gradle modules.
- Do not add a REST call to Notification; this remains an in-process method call.
- Do not integrate actual mail, SMS, push, or external providers.
- Do not add Kafka, RabbitMQ, Redis, Eureka, API Gateway, Load Balancer, gRPC, CQRS, Event Sourcing, Saga, Outbox, or Docker Compose.
- Do not use `@Async`, `afterCommit`, `REQUIRES_NEW`, retry, circuit breaker, or new asynchronous mechanisms in this baseline.
- Do not expose `shouldFail`, `simulateFailure`, or `delay` as a public request field/endpoint.
- Do not refactor existing Member/Product/Order/Payment implementation beyond minimal necessary changes.

## Verification

Run:

```bash
./gradlew test
./gradlew build
```

Show the exact tests executed and outcomes. If a test fails, fix within scope or report the precise blocker.

## Done When

- Happy-path payment approval records a Notification synchronously.
- Business payment failure creates no payment-approved record.
- Notification records are queryable.
- Failure/latency experiments show the actual effect of synchronous coupling, without production test switches.
- Existing tests/build pass, and no future infrastructure is introduced.

## Report Format

```text
Summary
Changed files
Verification
Observed coupling / failure / latency
Not done / risks
Next suggested task
```

Respond in Korean. Clearly separate (1) observed facts from tests and (2) future design ideas. Do not implement future ideas now.
