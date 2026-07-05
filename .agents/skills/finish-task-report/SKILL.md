---
name: finish-task-report
description: Use at the end of an implementation task to summarize changes, verification results, risks, and the next suggested task.
---

# Skill: Finish Task Report

Use this skill at the end of an implementation task.

## Steps

1. Summarize what was implemented.
2. List changed files.
3. Report verification commands and results.
4. Mention unverified items.
5. Mention risks or assumptions.
6. Suggest the next small task.

## Output Format

```text
Summary
- ...

Changed files
- ...

Verification
- ...

Not done / risks
- ...

Next suggested task
- ...
```

## Rules

- Be honest about failed or skipped verification.
- Do not hide build/test failures.
- Do not claim production readiness.
- Keep the summary focused on the current task.
