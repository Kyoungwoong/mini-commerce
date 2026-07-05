# ADR-0001: Use Kotlin and Spring Boot

## Status

Accepted

## Context

The project is intended to teach Kotlin while building a realistic MSA-style Mini Commerce system.

Spring Boot provides a familiar backend foundation, while Kotlin allows the project to practice modern JVM development.

## Decision

Use Kotlin and Spring Boot as the primary application stack.

Use Gradle Kotlin DSL for builds.

## Consequences

Positive:

- Kotlin can be learned through practical backend code.
- Spring Boot makes it easier to focus on architecture instead of low-level server setup.
- Gradle Kotlin DSL keeps the build language consistent with the application language.

Trade-offs:

- Kotlin-specific build and plugin issues may appear.
- Some Spring examples online are Java-first, so adaptation may be needed.

## Alternatives Considered

- Java with Spring Boot
- Kotlin with Ktor
- Node.js/NestJS
