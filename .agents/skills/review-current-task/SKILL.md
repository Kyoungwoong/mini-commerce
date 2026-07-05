---
name: review-current-task
description: Use when reviewing whether the current repository changes satisfy tasks/CURRENT_TASK.md and follow the project roadmap.
---

# Skill: Review Current Task

Use this skill when asked to review whether the current repository state satisfies the active task.

## Inputs

Read these first:

- `AGENTS.md`
- `tasks/CURRENT_TASK.md`
- `docs/ROADMAP.md`
- `docs/CODE_REVIEW_RULES.md`
- relevant ADRs under `docs/adr/`

## Steps

1. Identify the current phase.
2. Identify the requested scope.
3. Inspect changed files.
4. Check for phase violations.
5. Check build/test instructions.
6. Check whether documentation updates are needed.
7. Produce a concise review.

## Phase Violation Examples

- Kafka introduced before the Kafka phase.
- Redis introduced before the auth phase.
- Eureka introduced before the service discovery phase.
- gRPC introduced before REST internal calls exist.
- CQRS/Event Sourcing introduced before the base order flow exists.
- Spring Cloud Gateway used during the custom gateway phase.
- Spring Cloud LoadBalancer used during the custom load-balancer phase.

## Output Format

```text
Review Summary

✅ Good
- ...

⚠️ Needs attention
- ...

❌ Must fix
- ...

Phase compliance
- ...

Verification
- ...

Next suggested task
- ...
```
