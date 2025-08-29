# REQ-001: 환경별 설정 가이드

**날짜**: 2025-01-15  
**버전**: v1.0  
**상태**: done  
**담당자**: DEV

## 환경별 설정 파일 구조

```
src/main/resources/
├── application.properties              # 기본 설정 (공통)
├── application-local.properties       # 로컬 개발 환경
├── application-dev.properties         # 개발 서버 환경
├── application-prod.properties        # 운영 환경
├── application-test.properties        # 테스트 환경
└── application-oauth-kakao.properties # 카카오 OAuth2 설정
```

## 환경별 데이터베이스 설정

### 1. 로컬 환경 (local)
- **데이터베이스**: MySQL (localhost)
- **URL**: `jdbc:mysql://localhost:3306/kakao_login`
- **사용자**: `root`
- **비밀번호**: `1234`
- **DDL 모드**: `create-drop` (개발용)
- **SQL 로그**: 상세 출력

### 2. 개발 서버 환경 (dev)
- **데이터베이스**: MySQL (dev-server)
- **URL**: `jdbc:mysql://dev-server:3306/kakao_login_dev`
- **사용자**: `devuser`
- **비밀번호**: `devpassword123`
- **DDL 모드**: `update`
- **SQL 로그**: 기본 출력

### 3. 운영 환경 (prod)
- **데이터베이스**: MySQL (prod-server)
- **URL**: `jdbc:mysql://prod-server:3306/kakao_login_prod`
- **사용자**: 환경변수 `DB_USERNAME`
- **비밀번호**: 환경변수 `DB_PASSWORD`
- **DDL 모드**: `validate` (스키마 검증만)
- **SQL 로그**: 최소 출력
- **보안**: SSL 활성화

### 4. 테스트 환경 (test)
- **데이터베이스**: H2 (인메모리)
- **URL**: `jdbc:h2:mem:testdb`
- **사용자**: `sa`
- **비밀번호**: (없음)
- **DDL 모드**: `create-drop`
- **SQL 로그**: 상세 출력

## 환경별 카카오 OAuth2 설정

### Redirect URI 설정
- **로컬**: `http://localhost:8080/login/oauth2/code/kakao`
- **개발**: `https://dev.aiusecase.com/login/oauth2/code/kakao`
- **운영**: `https://aiusecase.com/login/oauth2/code/kakao`
- **테스트**: `http://localhost:8080/login/oauth2/code/kakao`

## 환경별 로깅 설정

### 로컬 환경
```properties
logging.level.root=INFO
logging.level.com.aiusecase=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### 개발 서버 환경
```properties
logging.level.root=INFO
logging.level.com.aiusecase=INFO
logging.level.org.springframework.security=INFO
logging.level.org.hibernate.SQL=INFO
```

### 운영 환경
```properties
logging.level.root=WARN
logging.level.com.aiusecase=INFO
logging.level.org.springframework.security=WARN
logging.level.org.hibernate.SQL=WARN
```

### 테스트 환경
```properties
logging.level.root=INFO
logging.level.com.aiusecase=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

## 환경별 보안 설정

### 로컬/개발 환경
- CSRF 비활성화
- HTTP 사용
- 기본 세션 설정

### 운영 환경
- CSRF 활성화
- HTTPS 강제
- 보안 쿠키 설정
- 세션 타임아웃 60분

## 실행 방법

### 1. 환경변수로 프로파일 설정
```bash
# Windows
set SPRING_PROFILES_ACTIVE=local

# Linux/Mac
export SPRING_PROFILES_ACTIVE=local
```

### 2. JVM 옵션으로 프로파일 설정
```bash
java -jar -Dspring.profiles.active=local app.jar
```

### 3. 스크립트 파일 사용
```bash
# Windows
scripts/run-local.bat
scripts/run-dev.bat
scripts/run-prod.bat

# Linux/Mac
./scripts/run-local.sh
./scripts/run-dev.sh
./scripts/run-prod.sh
```

## 데이터베이스 연결 확인

### MySQL 연결 테스트
```bash
# 로컬 환경
mysql -h localhost -u root -p1234 -D kakao_login

# 개발 서버
mysql -h dev-server -u devuser -pdevpassword123 -D kakao_login_dev

# 운영 서버
mysql -h prod-server -u $DB_USERNAME -p$DB_PASSWORD -D kakao_login_prod
```

### H2 콘솔 (테스트 환경)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- 사용자명: `sa`
- 비밀번호: (비어있음)

## 주의사항

1. **운영 환경 설정 파일은 Git에서 제외됨**
   - `application-prod.properties`는 `.gitignore`에 포함
   - 실제 운영 환경에서는 환경변수 사용 권장

2. **데이터베이스 접속 정보 보안**
   - 운영 환경에서는 반드시 환경변수 사용
   - 소스코드에 하드코딩 금지

3. **프로파일 활성화 확인**
   - 애플리케이션 시작 시 로그에서 활성 프로파일 확인
   - `The following profiles are active: local`

4. **환경별 설정 우선순위**
   - 환경변수 > 프로파일별 설정 파일 > 기본 설정 파일
