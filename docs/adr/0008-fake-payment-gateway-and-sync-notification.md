# ADR-0008: Server-Side Fake Payment Gateway and Synchronous Notification Baseline

## Status

Accepted

## Context

Phase 2-5 adds a Notification domain to the monolith. Two issues surfaced first:

- The Payment API accepted `shouldFail` from the client, so the client decided whether a payment succeeded. A real PG decides this on the server side.
- Notification must start as the simplest possible call so that its coupling with Payment can be observed before any redesign.

## Decision

- Remove `shouldFail` from `RequestPaymentRequest`; the request body is now `{ "orderId": ... }`. Unknown fields such as a legacy `shouldFail` are ignored.
- Add `FakePaymentGateway`, a server-side simulation component in `payment/infrastructure`. It approves every payment in normal runs and makes no external call. Tests replace it with `@MockitoBean` to produce APPROVED or FAILED.
- No `PaymentGateway` interface yet. One implementation exists, and tests replace the class directly.
- After an approved payment, `PaymentService` calls `NotificationService.recordPaymentApproved(memberId, orderId)` directly, inside the same `@Transactional` method. Rejected payments record nothing.
- A notification is only a DB record. It has no `SENT` status because nothing is delivered.
- `GET /api/v1/notifications?memberId=` exposes records read-only, with no pagination.

## Consequences

Positive:

- The client can no longer choose the payment result.
- Payment, Order and Notification are consistent: all commit or all roll back together.
- The code is easy to follow, which suits a blog explanation.

Negative / trade-offs (observed in `PaymentNotificationCouplingTest`):

- A notification failure rolls back an otherwise valid payment. A side effect breaks the core flow.
- The payment response waits for the notification call. A slow notification makes payment slow, and the payment transaction stays open meanwhile.
- `PaymentService` depends directly on `NotificationService`.
- An unexpected exception has no generic handler, so the client gets a framework 500 instead of `ApiResponse`.

Watch later:

- Phase 3: when `notification-service` is split out, this call becomes a REST call, and a down service loses the notification (Phase 8 message-loss scenario → Kafka).
- Phase 10: timeout and fallback for slow collaborators.

## Alternatives Considered

- Keep `shouldFail`: rejected, because the client should not decide the payment result.
- Catch and log notification failures: hides the coupling we want to observe in this phase.
- Spring events, `@Async`, `afterCommit`, `REQUIRES_NEW`: premature for the Phase 2 baseline.
