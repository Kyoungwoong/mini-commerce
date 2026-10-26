# CLAUDE.md

Follow `AGENTS.md` (project constitution), `docs/ROADMAP.md`, and `tasks/CURRENT_TASK.md`.

## Dev sub-agents

Project sub-agents live in `.claude/agents/`. The main session is the **orchestrator**: sub-agents cannot call each other, so delegate step by step and pass each result to the next agent.

| Agent | Role | Writes to |
|---|---|---|
| `architect` | phase check, scope, design, ADRs, blog notes | `docs/`, `tasks/` only |
| `backend` | Kotlin/Spring implementation | `src/main` |
| `frontend` | UI (dormant until an ADR/task requires it) | `frontend/` only |
| `tester` | tests + `./gradlew test` | `src/test` only |
| `git` | branch, commit, PR drafts | git state only |

## Workflow

The order is always fixed:

```text
1. architect : phase, scope, design/ADR
2. backend   : implement
3. frontend  : implement (dormant: skipped unless a task/ADR activates it)
4. review    : tester writes/runs tests (failures go back to backend),
               then check .agents/skills/review-current-task + docs/CODE_REVIEW_RULES.md
5. git       : commit locally; push/PR only after the user confirms
-> report    : AGENTS.md format (Summary / Changed files / Verification / Not done / Risks / Next)
```

## Rules

- Never reorder the steps above. Run agents sequentially; never let two agents edit the same files at once. Only read-only research runs in parallel.
- Phase guard applies to every agent: no future-phase infrastructure.
- Check which branch holds the latest work before starting (`git fetch`, then compare with `origin/main`).
- Ask the user before push, PR, merge, or introducing a new technology.
