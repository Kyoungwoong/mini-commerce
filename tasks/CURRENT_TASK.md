# CURRENT TASK

## Phase

Phase 2-4 - Mini Commerce Domain in Monolith: Fake Payment

## Goal

Add a simple Fake Payment domain to the current modular monolith.

The goal of this task is to connect Order and Payment inside `commerce-monolith`.

This task is important because Payment will later become the starting point for distributed transaction and Saga pattern learning.

This is not a real payment gateway integration task.

This is not a microservice extraction task.

## Context

The project currently has the following modules:

```text
common
commerce-monolith
```

`commerce-monolith` is still the only executable Spring Boot application.

The following domains already exist inside the monolith:

```text
member
product
order
```

Order creation already validates member/product, stores order items, and decreases product stock.

Now we will add a simple Fake Payment domain inside the same monolith.

Current architecture is still:

```text
Client
  ↓
commerce-monolith
  ↓
common
```

It is not MSA yet.

In this task, Payment should update the existing Order status inside the same monolith.

Later, when Order and Payment are separated into different services, this simple in-process transaction will become a distributed transaction problem.

## Scope

AI AGENT may change:

* `commerce-monolith/**`
* `common/**` only if the existing common response or error model needs a small compatible improvement
* Gradle build files only if required for tests or existing dependency alignment

AI AGENT should not change:

* future service directories
* Gateway-related files
* Kafka-related files
* Redis-related files
* Eureka-related files
* gRPC-related files
* unrelated docs

## Requirements

Implement a simple Fake Payment domain inside `commerce-monolith`.

Required package direction:

```text
com.minicommerce.commerce.payment
```

Recommended structure:

```text
payment
 ├── controller
 ├── dto
 ├── application
 ├── domain
 └── infrastructure
```

Implement the following APIs:

```http
POST /api/v1/payments
GET /api/v1/payments/{paymentId}
```

## API Details

### 1. Request Fake Payment

Request example:

```json
{
  "orderId": 1,
  "shouldFail": false
}
```

If `shouldFail` is `false`, payment should be approved.

Expected response:

```json
{
  "success": true,
  "data": {
    "paymentId": 1,
    "orderId": 1,
    "amount": 60000,
    "status": "APPROVED",
    "orderStatus": "PAID"
  }
}
```

If `shouldFail` is `true`, payment should fail.

Expected response:

```json
{
  "success": true,
  "data": {
    "paymentId": 1,
    "orderId": 1,
    "amount": 60000,
    "status": "FAILED",
    "orderStatus": "PAYMENT_FAILED"
  }
}
```

Payment failure is a business result, not necessarily an API error.

So it is acceptable to return `success: true` with payment status `FAILED`.

### 2. Payment Detail

Request:

```http
GET /api/v1/payments/{paymentId}
```

Expected response:

```json
{
  "success": true,
  "data": {
    "paymentId": 1,
    "orderId": 1,
    "amount": 60000,
    "status": "APPROVED"
  }
}
```

## Domain Model

Create a simple `Payment` entity.

Suggested classes:

```text
Payment
PaymentStatus
```

Suggested fields for `Payment`:

```text
id
orderId
amount
status
failureReason
createdAt
```

Suggested `PaymentStatus`:

```kotlin
enum class PaymentStatus {
    APPROVED,
    FAILED
}
```

## Order Status Interaction

Payment should update the existing Order status.

Expected Order status flow:

```text
CREATED
  → PAID
  → PAYMENT_FAILED
```

If the existing `OrderStatus` enum does not have these statuses, add them:

```kotlin
enum class OrderStatus {
    CREATED,
    PAYMENT_REQUESTED,
    PAID,
    PAYMENT_FAILED,
    CANCELLED
}
```

Only update what is necessary.

Do not implement full payment workflow or Saga yet.

## Payment Rules

When payment is requested:

1. Validate order exists.
2. Validate order is payable.
3. Determine amount from the order total price.
4. If `shouldFail` is `false`:

    * create payment with status `APPROVED`
    * update order status to `PAID`
5. If `shouldFail` is `true`:

    * create payment with status `FAILED`
    * update order status to `PAYMENT_FAILED`

Keep the implementation simple.

## Transaction

Use a simple Spring `@Transactional` boundary if appropriate.

The learning point is:

```text
In a monolith, Payment can update Order in the same application and database transaction.
Later, when Order and Payment are separated, this becomes a distributed transaction problem.
```

Do not implement Saga in this task.

## Common Response

Use the existing common response wrapper.

Do not create a payment-only response format.

If the current common response or error model is insufficient, improve it minimally and compatibly.

## Error Handling

Add or reuse simple error handling.

Required cases:

* order not found
* payment not found
* order is not payable
* invalid request

If a global exception handler already exists, reuse or extend it.

Do not create duplicate exception handling structures.

## Tests

Add relevant tests.

Required minimum:

* payment approval succeeds
* payment approval updates order status to `PAID`
* payment failure succeeds as a business result
* payment failure updates order status to `PAYMENT_FAILED`
* payment detail lookup succeeds
* payment request for non-existing order returns an error
* payment request for already paid order returns an error if payable validation is implemented

Choose simple tests that fit the current project setup.

## Do Not

* Do not create separate microservice modules yet.
* Do not extract `payment-service`.
* Do not call an external payment gateway.
* Do not add real PG integration.
* Do not add Toss Payments, KakaoPay, NaverPay, or any external SDK.
* Do not add Notification domain in this task.
* Do not send notification.
* Do not add Kafka.
* Do not add Redis.
* Do not add Redis Sentinel.
* Do not add Eureka.
* Do not add API Gateway.
* Do not add Load Balancer.
* Do not add JWT.
* Do not add Spring Security.
* Do not add CQRS.
* Do not add Event Sourcing.
* Do not add gRPC.
* Do not add Docker Compose unless explicitly requested.
* Do not implement Saga yet.
* Do not implement compensation transaction yet.

## Done When

* Fake payment API works.
* Payment detail API works.
* Approved payment updates order status to `PAID`.
* Failed payment updates order status to `PAYMENT_FAILED`.
* Payment amount is based on order total price.
* Common response format is used.
* Relevant tests pass.
* The project builds successfully.
* No future-phase infrastructure is introduced.
* Changed files are summarized.
* Any assumptions are documented.
