# Kotlin MSA Commerce

Kotlin과 Spring Boot로 Mini Commerce 시스템을 만들고, 이를 단계적으로 MSA로 전환하는 학습 프로젝트입니다.

이 프로젝트는 처음부터 완성된 MSA를 만드는 것이 아니라, 문제를 하나씩 경험하면서 구조를 확장합니다.

## Learning Flow

```text
Modular Monolith
→ Service Separation
→ Custom API Gateway
→ Custom Load Balancer
→ Service Discovery
→ Redis Auth
→ Kafka
→ Saga / Outbox
→ Resilience
→ Observability
→ CQRS / Event Sourcing
→ gRPC
```

## Main Rule

기술을 먼저 넣지 않습니다.

먼저 문제를 만들고, 그 문제가 왜 불편한지 확인한 뒤에 해결책을 도입합니다.

## Key Documents

- `AGENTS.md`: AI AGENT 작업 규칙
- `docs/ROADMAP.md`: 전체 프로젝트 여정
- `docs/CODE_REVIEW_RULES.md`: 코드 검토 기준
- `docs/adr/`: 아키텍처 의사결정 기록
- `tasks/CURRENT_TASK.md`: 현재 작업 범위
- `.agents/skills/`: 반복 작업용 AI AGENT skills
- `.prompts/`: AI AGENT에게 줄 작업 지시서
