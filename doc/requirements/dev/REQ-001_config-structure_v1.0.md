# REQ-001: 설정 파일 구조 정리

**날짜**: 2025-01-15  
**버전**: v1.0  
**상태**: done  
**담당자**: DEV

## 설정 파일 구조

```
src/main/resources/
├── application.yml                    # 기본 설정 (공통)
├── application.properties            # 공통 설정 (데이터베이스, JPA)
├── application-local.properties      # 로컬 개발 환경
├── application-dev.properties        # 개발 서버 환경
├── application-prod.properties       # 운영 환경
├── application-test.properties       # 테스트 환경
└── application-oauth-kakao.properties # 카카오 OAuth2 설정
```

## 설정 파일 역할 분담

### 1. application.yml (기본 설정)
- **역할**: 애플리케이션 기본 정보 및 공통 설정
- **내용**:
  - 서버 포트 설정
  - 애플리케이션 이름
  - 프로파일 활성화 설정
  - 카카오 OAuth2 기본 설정
  - 기본 로깅 설정

### 2. application.properties (공통 설정)
- **역할**: 데이터베이스 및 JPA 공통 설정
- **내용**:
  - 데이터베이스 연결 설정 (환경변수 참조)
  - JPA 기본 설정
  - 프로파일별로 오버라이드됨

### 3. application-{profile}.properties (환경별 설정)
- **역할**: 각 환경에 특화된 설정
- **내용**:
  - 환경별 데이터베이스 설정
  - 환경별 로깅 설정
  - 환경별 보안 설정
  - 환경별 카카오 OAuth2 설정

## 설정 우선순위

1. **환경변수** (가장 높은 우선순위)
2. **프로파일별 설정 파일** (`application-{profile}.properties`)
3. **공통 설정 파일** (`application.properties`)
4. **기본 설정 파일** (`application.yml`)

## 중복 제거된 설정 항목

### 제거된 중복 설정
- `server.port`: application.yml에서만 관리
- `spring.application.name`: application.yml에서만 관리
- `spring.datasource.driver-class-name`: application.properties에서만 관리
- `spring.jpa.show-sql`: application.properties에서 기본값 설정
- `spring.jpa.properties.hibernate.format_sql`: application.properties에서 기본값 설정
- `spring.jpa.properties.hibernate.dialect`: application.properties에서 기본값 설정
- `logging.level.root`: application.yml에서 기본값 설정

### 환경별로만 다른 설정
- `spring.datasource.url`: 각 환경별 데이터베이스 URL
- `spring.datasource.username`: 각 환경별 사용자명
- `spring.datasource.password`: 각 환경별 비밀번호
- `spring.jpa.hibernate.ddl-auto`: 각 환경별 DDL 모드
- `spring.security.oauth2.client.registration.kakao.redirect-uri`: 각 환경별 리다이렉트 URI
- `logging.level.*`: 각 환경별 로깅 레벨

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

## 설정 파일 예시

### application.yml (기본 설정)
```yaml
server:
  port: 8080

spring:
  application:
    name: kakao-login-demo
  profiles:
    active: local
  security:
    oauth2:
      client:
        registration:
          kakao:
            client-id: ${KAKAO_CLIENT_ID:your-kakao-rest-api-key}
            client-secret: ${KAKAO_CLIENT_SECRET:}
            redirect-uri: ${KAKAO_REDIRECT_URI:http://localhost:8080/login/oauth2/code/kakao}
            # ... 기타 설정

logging:
  level:
    root: INFO
    org.springframework.security: DEBUG
    com.aiusecase: DEBUG
```

### application.properties (공통 설정)
```properties
# 데이터베이스 공통 설정
spring.datasource.driver-class-name=${DB_DRIVER:com.mysql.cj.jdbc.Driver}
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# JPA 공통 설정
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### application-local.properties (로컬 환경)
```properties
# 데이터베이스 설정 (로컬 MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/kakao_login?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=1234

# JPA 설정 (로컬 개발용)
spring.jpa.hibernate.ddl-auto=create-drop

# 카카오 OAuth2 설정 (로컬)
spring.security.oauth2.client.registration.kakao.redirect-uri=http://localhost:8080/login/oauth2/code/kakao

# 로깅 설정 (로컬 개발용 - 상세)
logging.level.com.aiusecase=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

## 장점

1. **중복 제거**: 동일한 설정이 여러 파일에 중복되지 않음
2. **명확한 역할 분담**: 각 파일의 역할이 명확히 구분됨
3. **유지보수성 향상**: 공통 설정 변경 시 한 곳에서만 수정
4. **환경별 설정 용이**: 프로파일별로 필요한 설정만 오버라이드
5. **가독성 향상**: 설정 파일이 간결하고 이해하기 쉬움

## 주의사항

1. **설정 우선순위 이해**: 환경변수 > 프로파일별 설정 > 공통 설정 > 기본 설정
2. **환경변수 사용**: 민감한 정보는 환경변수로 관리
3. **프로파일 활성화**: 실행 시 올바른 프로파일이 활성화되었는지 확인
4. **설정 충돌 방지**: 동일한 설정이 여러 파일에 정의되지 않도록 주의
