# CURRENT TASK

## Phase

Phase 2-1 - Mini Commerce Domain in Monolith: Member

## Goal

Add the first domain feature to the modular monolith.

The goal of this task is to implement a simple Member domain inside `commerce-monolith` while keeping the project as a monolith.

This is not a microservice extraction task.

## Context

The project currently has the following modules:

```text
common
commerce-monolith
```

`common` provides shared response classes such as `ApiResponse` and `ErrorResponse`.

`commerce-monolith` is the only executable Spring Boot application.

In this phase, we start adding business domains inside the monolith so that we can later experience why service separation becomes useful.

## Scope

AI AGENT may change:

* `commerce-monolith/**`
* `common/**` only if the existing common response or error model needs a small compatible improvement
* `build.gradle.kts` files only if required for JPA/H2/test dependencies
* `docs/**` only if a short note is needed

AI AGENT should not change:

* `docs/ROADMAP.md` unless the current task requires a roadmap correction
* existing ADR files unless the task requires a new architecture decision
* unrelated modules or future service directories

## Requirements

Implement a simple Member domain inside `commerce-monolith`.

Required package direction:

```text
com.minicommerce.commerce.member
```

Recommended structure:

```text
member
 ├── controller
 ├── dto
 ├── application
 ├── domain
 └── infrastructure
```

Implement the following APIs:

```http
POST /api/v1/members
GET /api/v1/members/{memberId}
POST /api/v1/members/login
```

### 1. Member signup

Request example:

```json
{
  "email": "hiro@example.com",
  "name": "hiro"
}
```

Response example:

```json
{
  "success": true,
  "data": {
    "memberId": 1,
    "email": "hiro@example.com",
    "name": "hiro"
  }
}
```

### 2. Member lookup

Request:

```http
GET /api/v1/members/{memberId}
```

Response example:

```json
{
  "success": true,
  "data": {
    "memberId": 1,
    "email": "hiro@example.com",
    "name": "hiro"
  }
}
```

### 3. Mock login

This is not real authentication.

Request example:

```json
{
  "email": "hiro@example.com"
}
```

Response example:

```json
{
  "success": true,
  "data": {
    "memberId": 1,
    "mockAccessToken": "mock-token-1"
  }
}
```

The mock login endpoint should only exist to support later order flows.

Do not implement JWT in this task.

## Persistence

Use H2 and Spring Data JPA for this monolith phase if they are not already configured.

Create a simple `Member` entity.

Suggested fields:

```text
id
email
name
createdAt
```

Email should be unique.

If duplicate email is requested, return a clear error response.

## Common Response

Use the existing common response wrapper.

If the current common response structure is insufficient, improve it minimally.

Do not over-engineer the common module.

## Validation

Add simple validation where appropriate.

Examples:

* email must not be blank
* name must not be blank

Use Jakarta Bean Validation if it fits the current project setup.

## Error Handling

Add simple error handling for this task.

At minimum:

* member not found
* duplicate email
* invalid request

If a global exception handler does not exist yet, add a simple one inside `commerce-monolith`.

Keep it minimal.

## Tests

Add relevant tests.

Required minimum:

* member signup succeeds
* member lookup succeeds
* duplicate email returns an error
* mock login succeeds for an existing member

Choose simple tests that fit the current project setup.

## Do Not

* Do not create separate microservice modules yet.
* Do not extract `member-service`.
* Do not add Kafka.
* Do not add Redis.
* Do not add Redis Sentinel.
* Do not add Eureka.
* Do not add API Gateway.
* Do not add Load Balancer.
* Do not add JWT.
* Do not add Spring Security unless absolutely required, and if added, explain why.
* Do not add CQRS.
* Do not add Event Sourcing.
* Do not add gRPC.
* Do not add Docker Compose unless explicitly requested.

## Done When

* Member signup API works.
* Member lookup API works.
* Mock login API works.
* Common response format is used.
* Relevant tests pass.
* The project builds successfully.
* No future-phase infrastructure is introduced.
* Changed files are summarized.
* Any assumptions are documented.
