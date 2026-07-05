# CURRENT TASK

## Phase

Phase 2-3 - Mini Commerce Domain in Monolith: Order

## Goal

Add the Order domain to the current modular monolith.

The goal of this task is to implement a simple order flow inside `commerce-monolith`.

This task is important because Order will later become the center of many MSA problems:

* service-to-service communication
* distributed transaction
* payment workflow
* notification event
* Saga pattern
* CQRS / Event Sourcing

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
```

Now we will add the Order domain inside the same monolith.

Current architecture is still:

```text
Client
  ↓
commerce-monolith
  ↓
common
```

It is not MSA yet.

In this task, Order should use existing Member and Product data inside the monolith.

Later, when services are separated, this simple in-process call will become service-to-service communication.

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

Implement a simple Order domain inside `commerce-monolith`.

Required package direction:

```text
com.minicommerce.commerce.order
```

Recommended structure:

```text
order
 ├── controller
 ├── dto
 ├── application
 ├── domain
 └── infrastructure
```

Implement the following APIs:

```http
POST /api/v1/orders
GET /api/v1/orders/{orderId}
GET /api/v1/orders?memberId={memberId}
```

## API Details

### 1. Create Order

Request example:

```json
{
  "memberId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

Response example:

```json
{
  "success": true,
  "data": {
    "orderId": 1,
    "memberId": 1,
    "status": "CREATED",
    "totalPrice": 60000,
    "items": [
      {
        "productId": 1,
        "productName": "Keyboard",
        "orderPrice": 30000,
        "quantity": 2
      }
    ]
  }
}
```

### 2. Order Detail

Request:

```http
GET /api/v1/orders/{orderId}
```

Response example:

```json
{
  "success": true,
  "data": {
    "orderId": 1,
    "memberId": 1,
    "status": "CREATED",
    "totalPrice": 60000,
    "items": [
      {
        "productId": 1,
        "productName": "Keyboard",
        "orderPrice": 30000,
        "quantity": 2
      }
    ]
  }
}
```

### 3. Member Orders

Request:

```http
GET /api/v1/orders?memberId=1
```

Response example:

```json
{
  "success": true,
  "data": [
    {
      "orderId": 1,
      "memberId": 1,
      "status": "CREATED",
      "totalPrice": 60000
    }
  ]
}
```

## Domain Rules

Create a simple `Order` aggregate.

Suggested entities:

```text
Order
OrderItem
```

Suggested fields for `Order`:

```text
id
memberId
status
totalPrice
createdAt
items
```

Suggested fields for `OrderItem`:

```text
id
productId
productName
orderPrice
quantity
```

Important:

* Store `productName` and `orderPrice` inside `OrderItem`.
* Do not only store `productId`.

Reason:

The order should preserve the product name and price at the time of ordering.

If the product price changes later, the past order price should not change.

## Order Status

Add a simple order status enum.

Initial status can be:

```text
CREATED
```

You may also prepare future statuses if it does not complicate the implementation:

```text
CREATED
PAYMENT_REQUESTED
PAID
PAYMENT_FAILED
CANCELLED
```

Do not implement payment behavior in this task.

## Product Stock

When an order is created:

1. Validate member exists.
2. Validate product exists.
3. Validate requested quantity is greater than 0.
4. Validate product stock is enough.
5. Decrease product stock.
6. Create order.

This is intentionally simple.

Do not implement stock reservation yet.

Do not implement concurrency control yet.

Concurrency and distributed stock consistency will be discussed later.

## Persistence

Use the existing H2 and Spring Data JPA setup.

Create JPA entities and repositories for Order and OrderItem.

Keep the implementation simple.

## Cross-domain Interaction

Order may use existing Member and Product logic inside the monolith.

Prefer using application services or small reader/facade methods if they already exist.

Avoid duplicating Member/Product lookup logic unnecessarily.

However, do not over-engineer ports/adapters in this task.

The learning point is:

```text
In a monolith, Order can easily call Member/Product logic in the same process.
Later, when services are separated, this becomes service-to-service communication.
```

## Common Response

Use the existing common response wrapper.

Do not create an order-only response format.

If the current common response or error model is insufficient, improve it minimally and compatibly.

## Error Handling

Add or reuse simple error handling.

Required cases:

* member not found
* product not found
* order not found
* invalid order quantity
* insufficient product stock
* invalid request

If a global exception handler already exists, reuse or extend it.

Do not create duplicate exception handling structures.

## Tests

Add relevant tests.

Required minimum:

* order creation succeeds
* order detail lookup succeeds
* member orders lookup succeeds
* creating order with non-existing member returns an error
* creating order with non-existing product returns an error
* creating order with insufficient stock returns an error
* creating order decreases product stock

Choose simple tests that fit the current project setup.

## Do Not

* Do not create separate microservice modules yet.
* Do not extract `order-service`.
* Do not add Payment domain yet.
* Do not add Notification domain yet.
* Do not implement payment behavior.
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
* Do not introduce complex concurrency control yet.

## Done When

* Order creation API works.
* Order detail API works.
* Member orders lookup API works.
* Order stores product name and order price at the time of ordering.
* Product stock decreases when an order is created.
* Common response format is used.
* Relevant tests pass.
* The project builds successfully.
* No future-phase infrastructure is introduced.
* Changed files are summarized.
* Any assumptions are documented.
