# ADR-0003: Build Custom Gateway and Load Balancer Before Using Frameworks

## Status

Accepted

## Context

The project aims to understand why API Gateway and Load Balancer patterns exist.

Using Spring Cloud Gateway or Spring Cloud LoadBalancer immediately would hide important behavior such as routing, filtering, server selection, and failure handling.

## Decision

Build learning versions of:

- Custom API Gateway
- Custom Load Balancer

Do not use Spring Cloud Gateway or Spring Cloud LoadBalancer during those phases.

## Consequences

Positive:

- The project will expose core MSA infrastructure behavior.
- Blog posts can explain routing and load balancing from first principles.
- Later comparison with production-grade tools becomes meaningful.

Trade-offs:

- The custom implementation is not production-ready.
- Some functionality will be intentionally simplified.

## Alternatives Considered

- Use Spring Cloud Gateway immediately
- Use Nginx/Envoy immediately
- Use Spring Cloud LoadBalancer immediately
