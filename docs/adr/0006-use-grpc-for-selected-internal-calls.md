# ADR-0006: Use gRPC for Selected Internal Calls

## Status

Accepted

## Context

The project should start with REST because REST is easier to understand and fits the external API style.

However, internal service-to-service calls may later benefit from stricter contracts and more efficient RPC-style communication.

## Decision

Start with REST for internal calls.

Later, replace selected internal calls with gRPC.

Initial gRPC candidates:

- `order-service → product-service`
- `order-service → payment-service`

External client communication remains REST through the API Gateway.

## Consequences

Positive:

- REST limitations are experienced before gRPC is introduced.
- gRPC has a clear use case.
- External API remains simple for users and blog readers.

Trade-offs:

- The project must support both REST and gRPC for some time.
- Proto contracts and generated code add build complexity.

## Alternatives Considered

- Use gRPC from the beginning
- Use REST only
- Use Kafka for all service-to-service communication
