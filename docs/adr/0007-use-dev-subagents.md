# ADR-0007: Use Role-Based Sub-Agents for Development

## Status

Accepted

## Context

AGENTS.md, the roadmap, skills and review rules already guide a single coding agent. As the project grows, one agent handling design, implementation, testing and git in one context mixes concerns: it can drift beyond the current phase, edit tests to make them pass, or commit unreviewed work.

## Decision

Define five Claude Code sub-agents in `.claude/agents/`: `architect`, `backend`, `frontend`, `tester`, `git`. The main session orchestrates them in a fixed order (see `CLAUDE.md`):

1. `architect`: phase check, scope, design, ADR
2. `backend`: implementation
3. `frontend`: implementation (dormant, skipped unless a task or ADR activates it)
4. review: `tester` writes and runs tests (failures go back to `backend`), then the code is checked against `docs/CODE_REVIEW_RULES.md`
5. `git`: local commit; push and PR only after user confirmation

The order is never rearranged. Each agent has restricted tools and a write boundary, reads AGENTS.md first, and reuses the existing `.agents/skills/`. The `frontend` agent is dormant because the roadmap has no UI phase; it activates only through a task or ADR.

## Consequences

Positive:

- Separation of duties: the tester cannot change production code, the architect cannot change source code.
- Each agent has a small, focused context; reports follow the AGENTS.md format.
- The setup itself is a good blog story.

Negative / trade-offs:

- Sub-agents cannot call each other, so the orchestrator carries the workflow and hand-offs.
- Boundaries such as "only write under docs/" are enforced by prompt, not by tooling.
- More files to maintain; prompts can drift from AGENTS.md.

Watch later:

- Whether `architect` and `backend` hand-offs lose context.
- Splitting `backend` per service when Phase 3 starts.
- Adding hooks if prompt-only boundaries are violated.

## Alternatives Considered

- Single agent with skills only: simpler, but no role isolation.
- Agent Teams / workflow scripts: heavier, unnecessary at this scale.
