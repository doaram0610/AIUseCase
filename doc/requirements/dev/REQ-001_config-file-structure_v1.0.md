# REQ-001: YAML 통합 설정 파일 구조

**날짜**: 2025-01-15  
**버전**: v2.0  
**상태**: done  
**담당자**: DEV

## 설정 파일 구조 (YAML 통합)

```
src/main/resources/
├── application.yml                    # 통합 설정 (모든 환경)
├── static/                           # 정적 리소스
└── templates/                        # Thymeleaf 템플릿
```

## YAML 통합 설정의 장점

### 1. 단일 파일 관리
- **모든 환경별 설정이 `application.yml` 하나에 통합**
- 설정 파일 관리가 간편함
- 설정 변경 시 한 곳에서만 작업

### 2. 명확한 구조
- **`---` 구분자로 환경별 설정을 명확히 구분**
- 계층 구조가 시각적으로 명확
- 가독성이 뛰어남

### 3. 유지보수성 향상
- 중복 설정 제거
- 공통 설정과 환경별 설정의 명확한 분리
- 설정 우선순위 이해가 쉬움

## 설정 파일 구조

### application.yml (통합 설정)
```yaml
# 기본 설정 (공통)
server:
  port: 8080

spring:
  application:
    name: kakao-login-demo
  
  # 데이터베이스 공통 설정
  datasource:
    driver-class-name: ${DB_DRIVER:com.mysql.cj.jdbc.Driver}
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  
  # JPA 공통 설정
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect
  
  # 카카오 OAuth2 기본 설정
  security:
    oauth2:
      client:
        registration:
          kakao:
            client-id: ${KAKAO_CLIENT_ID:your-kakao-rest-api-key}
            client-secret: ${KAKAO_CLIENT_SECRET:}
            redirect-uri: ${KAKAO_REDIRECT_URI:http://localhost:8080/login/oauth2/code/kakao}
            # ... 기타 설정

# 기본 로깅 설정
logging:
  level:
    root: INFO
    org.springframework.security: DEBUG
    com.aiusecase: DEBUG

---
# 로컬 개발 환경 설정
spring:
  config:
    activate:
      on-profile: local
  
  datasource:
    url: jdbc:mysql://localhost:3306/kakao_login?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
    username: root
    password: "1234"
  
  jpa:
    hibernate:
      ddl-auto: create-drop
  
  # ... 기타 로컬 환경 설정

---
# 개발 서버 환경 설정
spring:
  config:
    activate:
      on-profile: dev
  
  datasource:
    url: jdbc:mysql://dev-server:3306/kakao_login_dev?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
    username: devuser
    password: devpassword123
  
  # ... 기타 개발 환경 설정

---
# 운영 환경 설정
spring:
  config:
    activate:
      on-profile: prod
  
  datasource:
    url: jdbc:mysql://prod-server:3306/kakao_login_prod?useSSL=true&serverTimezone=UTC&characterEncoding=UTF-8
    username: ${DB_USERNAME:produser}
    password: ${DB_PASSWORD:prodpassword123}
  
  # ... 기타 운영 환경 설정

---
# 테스트 환경 설정
spring:
  config:
    activate:
      on-profile: test
  
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: ""
    driver-class-name: org.h2.Driver
  
  # ... 기타 테스트 환경 설정
```

## 환경별 설정 특징

### 1. 로컬 환경 (local)
- **데이터베이스**: MySQL (localhost:3306/kakao_login)
- **JPA DDL**: create-drop (개발용)
- **로깅**: DEBUG 레벨 (상세)
- **개발 도구**: DevTools 활성화

### 2. 개발 서버 환경 (dev)
- **데이터베이스**: MySQL (dev-server:3306/kakao_login_dev)
- **JPA DDL**: update (개발 서버용)
- **로깅**: INFO 레벨
- **캐시**: Simple Cache

### 3. 운영 환경 (prod)
- **데이터베이스**: MySQL (prod-server:3306/kakao_login_prod)
- **JPA DDL**: validate (운영용)
- **로깅**: WARN 레벨 (최소)
- **보안**: SSL, 보안 쿠키, Caffeine Cache
- **커넥션 풀**: HikariCP 최적화

### 4. 테스트 환경 (test)
- **데이터베이스**: H2 인메모리
- **JPA DDL**: create-drop (테스트용)
- **로깅**: DEBUG 레벨
- **H2 콘솔**: 활성화

## 설정 우선순위

1. **환경변수** (가장 높은 우선순위)
2. **프로파일별 설정** (YAML 내 `---` 구분자로 구분)
3. **기본 설정** (YAML 파일 상단)

## 실행 방법

### 1. 스크립트 사용 (권장)
```bash
# 로컬 환경
scripts/run-local.bat

# 개발 서버 환경
scripts/run-dev.bat

# 운영 환경
scripts/run-prod.bat

# 테스트 환경
scripts/run-test.bat
```

### 2. 직접 실행
```bash
# 로컬 환경
set SPRING_PROFILES_ACTIVE=local
gradlew bootRun

# 개발 서버 환경
set SPRING_PROFILES_ACTIVE=dev
gradlew bootRun

# 운영 환경
set SPRING_PROFILES_ACTIVE=prod
gradlew bootRun

# 테스트 환경
set SPRING_PROFILES_ACTIVE=test
gradlew bootRun
```

## 환경변수 설정

### 공통 환경변수
```bash
# 카카오 OAuth2
KAKAO_CLIENT_ID=your-kakao-rest-api-key
KAKAO_CLIENT_SECRET=your-kakao-client-secret
KAKAO_REDIRECT_URI=http://localhost:8080/login/oauth2/code/kakao

# 데이터베이스 (기본값)
DB_DRIVER=com.mysql.cj.jdbc.Driver
DB_URL=jdbc:mysql://localhost:3306/kakao_login
DB_USER=root
DB_PASSWORD=1234
```

### 운영 환경 전용 환경변수
```bash
# 운영 환경 데이터베이스
DB_USERNAME=produser
DB_PASSWORD=prodpassword123
```

## 장점

1. **단일 파일 관리**: 모든 설정이 `application.yml` 하나에
2. **명확한 구조**: `---` 구분자로 환경별 구분이 명확
3. **가독성**: 계층 구조가 시각적으로 명확
4. **유지보수성**: 설정 변경 시 한 파일에서만 작업
5. **중복 제거**: 동일한 설정이 여러 파일에 중복되지 않음
6. **환경별 설정 용이**: 프로파일별로 필요한 설정만 오버라이드

## 주의사항

1. **설정 우선순위 이해**: 환경변수 > 프로파일별 설정 > 기본 설정
2. **환경변수 사용**: 민감한 정보는 환경변수로 관리
3. **프로파일 활성화**: 실행 시 올바른 프로파일이 활성화되었는지 확인
4. **YAML 문법**: 들여쓰기와 구문에 주의
5. **환경별 설정**: 각 환경에 맞는 설정만 오버라이드

## 변경 이력

- **v1.0**: Properties 파일 기반 분리 설정
- **v2.0**: YAML 통합 설정으로 변경 (현재 버전)
