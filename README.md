# AIUseCases

이 레포지토리는 다양한 인공지능(AI) 활용 사례를 정리하고 공유하기 위한 공간입니다.  
각 사례별로 적용된 AI 기술, 사용 목적, 구현 방법, 기대 효과 등을 문서화하여  
AI 도입을 고려하는 분들에게 실질적인 가이드와 참고 자료를 제공합니다.

## 주요 내용
- 해당 프로젝트 하위에 doc/requirements 에 요구사항을 Markdown 형식으로 올려주세요.


## 프로젝트 구조 가이드
프로젝트별로 동일하게 적용 가능한 최소 디렉터리 템플릿입니다. `doc/requirements` 하위에 요구사항을 분리해 관리합니다.

### 구조 시각화(텍스트 트리)
```text
AIUseCase  # 프로젝트명
├─ README.md
├─ src
├─ tests
└─ doc
   ├─ requirements
   │  ├─ 2025-01-15_REQ-101_onboarding-flow_v1.0.md      # PM 요구사항 문서
   │  ├─ 2025-02-02_REQ-101_onboarding-flow_v1.1.md      # PM 요구사항 문서
   ├─ requirements-result
   │  ├─ REQ-101_solution-auth_v1.0.md                   # 개발자 결과/해결 내역 문서
   │  └─ REQ-101_test-report_v1.0.md                     # 개발자 결과/해결 내역 문서
   └─ changelog.md
```

### 요구사항(PM) 파일명 규칙
- 디렉터리: `doc/requirements/pm/`
- 파일명 포맷: `YYYY-MM-DD_REQ-<ID>_<kebab-title>_vMAJOR.MINOR.md`
  - 예: `2025-02-02_REQ-101_onboarding-flow_v1.1.md`
  - MAJOR: 요구 내용의 본질적 변경(스코프/수용기준 대폭 변경)
  - MINOR: 문구 정정, 상세 보강 등 비본질 변경

### 개발자 결과 문서(DEV) 폴더 및 규칙
- 디렉터리: `doc/requirements/dev/`
- 파일명 포맷(매핑 중심): `REQ-<ID>_<artifact>[-<module>]_vMAJOR.MINOR.md`
  - `<artifact>` 예: `solution`, `design`, `test-report`, `api-spec`, `deployment`
  - `<module>`은 선택(예: `auth`, `search`)
  - 예: `REQ-101_solution-auth_v1.0.md`, `REQ-101_test-report_v1.1.md`
- 상태 표기 권장: 문서 상단에 `Status: draft | in-progress | done`
- 추적성: 본문에 관련 이슈/PR/테스트 케이스 링크와 커밋 해시를 명시

### 공통 운영 수칙(요약)
- PM 문서와 DEV 문서는 동일한 `REQ-<ID>`로 상호 참조합니다.
- 변경 시 PR에 변경 사유를 요약하고, 필요 시 `doc/changelog.md`에 누적 기록합니다.
- 파일명은 `kebab-case`를 사용하고, 날짜는 `YYYY-MM-DD`로 통일합니다.
