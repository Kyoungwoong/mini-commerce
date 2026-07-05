# BACKLOG

This backlog contains future work. Do not implement these unless the current task explicitly asks for them.

## Phase 1

- Initialize Gradle Kotlin DSL project
- Add `common`
- Add `commerce-monolith`
- Add health endpoint
- Add common response/error types

## Phase 2

- Add member domain
- Add product domain
- Add order domain
- Add fake payment flow
- Add direct notification call

## Phase 3

- Extract member-service
- Extract product-service
- Extract order-service
- Add service-to-service REST calls

## Phase 4

- Create custom API Gateway
- Add static routing
- Add filter chain
- Add trace ID filter
- Add auth filter placeholder

## Phase 5

- Implement Round Robin load balancer
- Implement Random load balancer
- Implement Weighted Round Robin
- Add health-check-based exclusion

## Phase 6

- Create custom service registry
- Add heartbeat
- Add registry client
- Introduce Eureka
- Compare custom registry and Eureka

## Phase 7

- Add JWT login flow
- Store refresh token in Redis
- Add Redis failure scenario
- Add Redis Sentinel

## Phase 8

- Reproduce notification request loss
- Introduce Kafka
- Add idempotent consumer

## Phase 9

- Add Saga flow
- Add Outbox table
- Add Outbox publisher

## Phase 10

- Add timeout
- Add retry
- Add custom circuit breaker

## Phase 11

- Add trace propagation
- Add OpenTelemetry
- Add Prometheus/Grafana

## Phase 13+

- Add CQRS
- Add Event Sourcing
- Add gRPC
