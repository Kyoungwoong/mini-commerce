---
name: add-rest-api
description: Use when adding or modifying a REST API endpoint, including controller, DTO, service logic, tests, and API documentation.
---

# Skill: Add REST API

Use this skill when adding a REST API endpoint.

## Required Context

Read:

- `AGENTS.md`
- `tasks/CURRENT_TASK.md`
- relevant API docs if present

## Steps

1. Confirm the current phase.
2. Identify the target module.
3. Define request DTO.
4. Define response DTO or use common response wrapper if already introduced.
    1. If there's kotlin have a class like java's record, use it.
5. Add controller.
6. Add application/service logic.
7. Add domain logic if needed.
8. Add tests.
9. Update docs if this is a public API.

## API Rules

- Use versioned external paths when appropriate:
  - `/api/v1/...`
- Keep controller thin.
- Do not put business logic in controllers.
- Use clear error codes when common error handling exists.
- Avoid leaking persistence entities directly through APIs.

## Test Rules

Add at least one of:

- Controller test
- Service test
- Integration test

Choose the smallest test type that verifies the behavior.

## Output Checklist

- [ ] Endpoint implemented
- [ ] Request/response shape is clear
- [ ] Tests added or updated
- [ ] API docs updated if needed
- [ ] No unrelated refactor
