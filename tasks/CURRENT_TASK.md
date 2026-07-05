# CURRENT TASK

## Phase

Phase 2-2 - Mini Commerce Domain in Monolith: Product

## Goal

Add the Product domain to the current modular monolith.

The goal of this task is to implement simple product management inside `commerce-monolith`.

This is not a microservice extraction task.

## Context

The project currently has the following modules:

```text
common
commerce-monolith
```

`commerce-monolith` is still the only executable Spring Boot application.

The Member domain has already been added inside the monolith.

In this task, we add the Product domain inside the same monolith so that a later Order domain can use products for order creation.

Current architecture is still:

```text
Client
  ↓
commerce-monolith
  ↓
common
```

It is not MSA yet.

## Scope

AI AGENT may change:

* `commerce-monolith/**`
* `common/**` only if the existing common response or error model needs a small compatible improvement
* `build.gradle.kts` files only if required for tests or existing dependency alignment
* `docs/**` only if a short note is needed

AI AGENT should not change:

* existing Member domain behavior unless required to fix a build issue
* future service directories
* Gateway-related files
* Kafka-related files
* Redis-related files
* Eureka-related files
* gRPC-related files

## Requirements

Implement a simple Product domain inside `commerce-monolith`.

Required package direction:

```text
com.minicommerce.commerce.product
```

Recommended structure:

```text
product
 ├── controller
 ├── dto
 ├── application
 ├── domain
 └── infrastructure
```

Implement the following APIs:

```http
POST /api/v1/products
GET /api/v1/products
GET /api/v1/products/{productId}
```

### 1. Product registration

Request example:

```json
{
  "name": "Keyboard",
  "price": 30000,
  "stockQuantity": 100
}
```

Response example:

```json
{
  "success": true,
  "data": {
    "productId": 1,
    "name": "Keyboard",
    "price": 30000,
    "stockQuantity": 100
  }
}
```

### 2. Product list

Request:

```http
GET /api/v1/products
```

Response example:

```json
{
  "success": true,
  "data": [
    {
      "productId": 1,
      "name": "Keyboard",
      "price": 30000,
      "stockQuantity": 100
    }
  ]
}
```

### 3. Product detail

Request:

```http
GET /api/v1/products/{productId}
```

Response example:

```json
{
  "success": true,
  "data": {
    "productId": 1,
    "name": "Keyboard",
    "price": 30000,
    "stockQuantity": 100
  }
}
```

## Persistence

Use the existing H2 and Spring Data JPA setup.

Create a simple `Product` entity.

Suggested fields:

```text
id
name
price
stockQuantity
createdAt
```

Validation rules:

* name must not be blank
* price must be greater than 0
* stockQuantity must be 0 or greater

Do not implement stock decrease/reservation yet.

Stock decrease will be handled later when the Order domain is introduced.

## Common Response

Use the existing common response wrapper.

Do not create a separate product-only response format.

If the current common response or error model is insufficient, improve it minimally and compatibly.

Do not over-engineer the common module.

## Error Handling

Add simple error handling for this task.

At minimum:

* product not found
* invalid product request

If a global exception handler already exists from the Member task, reuse or extend it.

Do not create duplicate exception handling structures if a common pattern already exists.

## Tests

Add relevant tests.

Required minimum:

* product registration succeeds
* product list returns registered products
* product detail lookup succeeds
* product not found returns an error
* invalid product request returns an error

Choose simple tests that fit the current project setup.

## Do Not

* Do not create separate microservice modules yet.
* Do not extract `product-service`.
* Do not add Order domain yet.
* Do not add Payment domain yet.
* Do not add Notification domain yet.
* Do not implement stock decrease or stock reservation yet.
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

## Done When

* Product registration API works.
* Product list API works.
* Product detail API works.
* Common response format is used.
* Relevant tests pass.
* The project builds successfully.
* No future-phase infrastructure is introduced.
* Changed files are summarized.
* Any assumptions are documented.
