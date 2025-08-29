# REQ-001: 카카오 로그인 설정 가이드

**날짜**: 2025-01-15  
**버전**: v1.0  
**상태**: done  
**담당자**: DEV

## 카카오 개발자 콘솔 설정

### 1. 카카오 개발자 계정 생성
1. [카카오 개발자 콘솔](https://developers.kakao.com/)에 접속
2. 카카오 계정으로 로그인
3. "내 애플리케이션" → "애플리케이션 추가하기" 클릭

### 2. 애플리케이션 등록
- **앱 이름**: AI Use Case Kakao Login Demo
- **사업자명**: 개인 또는 회사명
- **카테고리**: 기타

### 3. 플랫폼 설정
1. **Web 플랫폼 등록**
   - 사이트 도메인: `http://localhost:8080`
   - JavaScript 키 복사 (나중에 사용)

2. **카카오 로그인 활성화**
   - "카카오 로그인" → "활성화 설정" → "활성화"
   - Redirect URI: `http://localhost:8080/login/oauth2/code/kakao`

### 4. 동의항목 설정
- **필수 동의항목**:
  - 닉네임 (profile_nickname)
  - 이메일 (account_email)

### 5. 보안 설정 (3가지 방법 중 선택)

#### 방법 1: 환경변수 사용 (권장 - 가장 안전)
```bash
# Windows PowerShell
$env:KAKAO_CLIENT_ID="your-kakao-rest-api-key"
$env:KAKAO_CLIENT_SECRET="your-kakao-client-secret"

# Windows Command Prompt
set KAKAO_CLIENT_ID=your-kakao-rest-api-key
set KAKAO_CLIENT_SECRET=your-kakao-client-secret

# Linux/Mac
export KAKAO_CLIENT_ID="your-kakao-rest-api-key"
export KAKAO_CLIENT_SECRET="your-kakao-client-secret"
```

#### 방법 2: 별도 설정 파일 사용 (개발 환경)
1. `src/main/resources/application-oauth-kakao-secret.properties` 파일 생성
2. 실제 키 값 입력:
   ```properties
   kakao.client.id=your-actual-rest-api-key
   kakao.client.secret=your-actual-client-secret
   ```
3. 이 파일은 `.gitignore`에 추가되어 Git에서 제외됨

#### 방법 3: 직접 설정 (권장하지 않음)
`application.yml`에서 직접 설정:
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          kakao:
            client-id: your-kakao-rest-api-key
            client-secret: your-kakao-client-secret
```

## 애플리케이션 실행

### 1. Gradle 빌드
```bash
./gradlew build
```

### 2. 환경별 실행 방법

#### 로컬 환경 실행
```bash
# Windows
scripts/run-local.bat

# 또는 직접 실행
set SPRING_PROFILES_ACTIVE=local
gradlew bootRun

# Linux/Mac
export SPRING_PROFILES_ACTIVE=local
./gradlew bootRun
```

#### 개발 서버 환경 실행
```bash
# Windows
scripts/run-dev.bat

# 또는 직접 실행
set SPRING_PROFILES_ACTIVE=dev
gradlew bootRun

# Linux/Mac
export SPRING_PROFILES_ACTIVE=dev
./gradlew bootRun
```

#### 운영 환경 실행
```bash
# Windows
scripts/run-prod.bat

# 또는 직접 실행
set SPRING_PROFILES_ACTIVE=prod
set DB_USERNAME=produser
set DB_PASSWORD=prodpassword123
gradlew bootRun

# Linux/Mac
export SPRING_PROFILES_ACTIVE=prod
export DB_USERNAME=produser
export DB_PASSWORD=prodpassword123
./gradlew bootRun
```

#### 테스트 환경 실행
```bash
# Windows
set SPRING_PROFILES_ACTIVE=test
gradlew bootRun

# Linux/Mac
export SPRING_PROFILES_ACTIVE=test
./gradlew bootRun
```

### 3. 접속 확인
- **로컬 환경**: http://localhost:8080
- **개발 서버**: https://dev.aiusecase.com
- **운영 서버**: https://aiusecase.com
- **테스트 환경**: http://localhost:8080 (H2 콘솔: http://localhost:8080/h2-console)

## 테스트 시나리오

### 1. 정상 로그인 플로우
1. 메인 페이지에서 "카카오로 로그인" 클릭
2. 카카오 인증 페이지로 리다이렉트
3. 카카오 계정으로 로그인
4. 애플리케이션으로 돌아와 홈 페이지 표시
5. 사용자 정보 확인

### 2. 로그아웃 테스트
1. 홈 페이지에서 "로그아웃" 클릭
2. 메인 페이지로 리다이렉트
3. 보호된 페이지 접근 시 로그인 페이지로 리다이렉트

### 3. 에러 처리 테스트
1. 카카오 인증 취소
2. 네트워크 오류 상황
3. 잘못된 Redirect URI

## 문제 해결

### 일반적인 문제들

1. **"Invalid redirect URI" 에러**
   - 카카오 개발자 콘솔의 Redirect URI와 application.yml의 redirect-uri가 정확히 일치하는지 확인

2. **"Client ID not found" 에러**
   - 환경변수 KAKAO_CLIENT_ID가 올바르게 설정되었는지 확인
   - REST API 키를 사용하고 있는지 확인 (JavaScript 키가 아님)

3. **"Scope not allowed" 에러**
   - 카카오 개발자 콘솔에서 동의항목이 올바르게 설정되었는지 확인

4. **CORS 에러**
   - 개발 환경에서는 localhost:8080으로 설정
   - 프로덕션 환경에서는 실제 도메인으로 변경

### 로그 확인
```bash
# 디버그 로그 활성화
./gradlew bootRun --debug
```

## 보안 고려사항

1. **환경변수 사용**
   - 클라이언트 ID를 소스코드에 하드코딩하지 않음
   - 프로덕션 환경에서는 보안된 환경변수 관리 시스템 사용

2. **HTTPS 사용**
   - 프로덕션 환경에서는 반드시 HTTPS 사용
   - 카카오 개발자 콘솔에서 HTTPS 도메인 등록

3. **세션 관리**
   - 현재 구현은 세션 기반 인증
   - 필요시 JWT 토큰 기반으로 변경 가능

## 다음 단계

1. 데이터베이스 연동 (사용자 정보 저장)
2. JWT 토큰 기반 인증으로 변경
3. 추가 소셜 로그인 (Google, Naver 등) 구현
4. 프로덕션 배포 설정
