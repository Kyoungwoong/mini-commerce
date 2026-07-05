# Skill: Create Kotlin Spring Module

Use this skill when creating a new Kotlin Spring Boot module.

## Required Context

Read:

- `AGENTS.md`
- `docs/ROADMAP.md`
- `tasks/CURRENT_TASK.md`

## Rules

- Create only the module requested by the current task.
- Use Gradle Kotlin DSL.
- Keep dependencies minimal.
- Do not add infrastructure that belongs to a later phase.
- Add a basic package structure.
- Add a minimal test when the module is executable.

## Suggested Structure

For an executable service:

```text
<module>/
 ├── build.gradle.kts
 └── src/
     ├── main/kotlin/
     │   └── ...
     └── test/kotlin/
         └── ...
```

For a library module:

```text
<module>/
 ├── build.gradle.kts
 └── src/
     ├── main/kotlin/
     └── test/kotlin/
```

## Checklist

- [ ] Module is included in `settings.gradle.kts`
- [ ] Module dependencies are minimal
- [ ] Package names are consistent
- [ ] Build succeeds
- [ ] Test succeeds
- [ ] No future-phase infrastructure added
