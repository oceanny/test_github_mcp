# Test GitHub MCP Repository

이 레포지토리는 GitHub MCP(Model Context Protocol)의 기능 테스트 및 검증을 위한 테스트 레포지토리입니다.

## 📋 프로젝트 개요

- **목적**: GitHub MCP 도구 검증 및 마이크로서비스 아키텍처 학습
- **기술 스택**: Java 17, Spring Boot 3.2, Apache Kafka, Docker
- **아키텍처**: 마이크로서비스 아키텍처 (MSA)

## 🎯 주요 기능

### 1. 이슈 관리
- 기능 요청 (`Feature Request`)
- 버그 리포트 (`Bug Report`)
- 문서화 (`Documentation`)
- 인프라 (`Infrastructure`)
- 리팩토링 (`Refactoring`)

### 2. 마이크로서비스
- **API Gateway**: 모든 요청의 진입점
- **User Service**: 사용자 관리
- **Product Service**: 상품 정보 관리
- **Order Service**: 주문 처리

### 3. 메시지 큐
- **Apache Kafka**: 서비스 간 비동기 통신

## 📁 프로젝트 구조

```
.
├── README.md                      # 프로젝트 설명 (이 파일)
├── docker-compose.yml             # Docker Compose 설정
├── build.gradle.kts              # 빌드 설정 (Kotlin DSL)
├── settings.gradle.kts           # 멀티 프로젝트 설정
├── api-gateway/                  # API Gateway 서비스
├── user-service/                 # User Service
├── product-service/              # Product Service
├── order-service/                # Order Service
├── common/                        # 공통 모듈
├── docs/                          # 프로젝트 문서
└── docker-images/                # Docker 이미지 설정
```

## 🚀 빠른 시작

### 사전 요구사항
- Java 17+
- Docker & Docker Compose
- Gradle 8.0+

### 설치 및 실행

```bash
# 1. 레포지토리 클론
git clone https://github.com/oceanny/test_github_mcp.git
cd test_github_mcp

# 2. 프로젝트 빌드
./gradlew build

# 3. Docker Compose로 서비스 시작
docker-compose up -d

# 4. 서비스 상태 확인
docker-compose ps
```

## 📚 주요 엔드포인트

### API Gateway (Port: 8080)
```
POST   /api/v1/users              # 사용자 생성
GET    /api/v1/users/{userId}     # 사용자 조회
POST   /api/v1/products           # 상품 생성
GET    /api/v1/products/{productId} # 상품 조회
POST   /api/v1/orders             # 주문 생성
GET    /api/v1/orders/{orderId}   # 주문 조회
```

## 🧪 테스트

### 단위 테스트 실행
```bash
./gradlew test
```

### 통합 테스트 실행
```bash
./gradlew integrationTest
```

### 테스트 결과 리포트
```bash
./gradlew testReport
```

## 📖 문서

자세한 문서는 [docs/](./docs/) 폴더에서 확인할 수 있습니다.

- [커리큘럼 요약](./docs/00-curriculum-summary.md)
- [문제 정의](./docs/01-problem-definition.md)
- [개발 지침](./docs/02-instruction-guide.md)
- [개발 사양](./docs/03-development-spec.md)
- [코딩 가이드](./docs/04-prompt-guide.md)
- [코드 읽기 가이드](./docs/05-code-reading-guide.md)
- [코드 리뷰 가이드](./docs/06-code-review-guide.md)
- [테스트 가이드](./docs/07-testing-guide.md)
- [리팩토링 가이드](./docs/08-refactoring-guide.md)

## 🔌 API 문서

API 상세 문서는 다음을 참고하세요:
- OpenAPI/Swagger: `http://localhost:8080/swagger-ui.html`
- API 스키마: `http://localhost:8080/v3/api-docs`

## 🐛 이슈 보고

버그나 기능 요청이 있으시면 [Issues](https://github.com/oceanny/test_github_mcp/issues) 탭에서 이슈를 생성해주세요.

### 이슈 작성 가이드
1. **중복 확인**: 기존 이슈와 중복되지 않는지 확인
2. **템플릿 선택**: 해당하는 템플릿 선택 (Feature, Bug, Documentation 등)
3. **상세 작성**: 최대한 상세하고 명확하게 작성
4. **라벨 지정**: 적절한 라벨 추가

## 💬 토론 및 질문

버그나 기능 요청이 아닌 일반적인 질문이나 토론은 [Discussions](https://github.com/oceanny/test_github_mcp/discussions) 탭을 이용해주세요.

## 🤝 기여

이 프로젝트에 기여하고 싶으신 분은 다음 단계를 따라주세요:

1. Fork 진행
2. Feature Branch 생성 (`git checkout -b feature/AmazingFeature`)
3. 변경사항 Commit (`git commit -m 'Add some AmazingFeature'`)
4. Branch Push (`git push origin feature/AmazingFeature`)
5. Pull Request 생성

### 코드 컨벤션
- **Java 코드 스타일**: Google Java Style Guide
- **패키지명**: lowercase (예: `com.example.userservice`)
- **클래스명**: PascalCase (예: `UserService`)
- **메서드명**: camelCase (예: `createUser`)
- **상수명**: UPPER_SNAKE_CASE (예: `MAX_RETRY_COUNT`)

## 📝 라이센스

이 프로젝트는 MIT 라이센스를 따릅니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참고하세요.

## 👥 팀

- **주관**: oceanny
- **프로젝트 타입**: 마이크로서비스 아키텍처 학습 프로젝트

## 📞 연락처

질문이나 문의사항이 있으시면:
- [GitHub Issues](https://github.com/oceanny/test_github_mcp/issues)
- [GitHub Discussions](https://github.com/oceanny/test_github_mcp/discussions)

---

**마지막 업데이트**: 2026-06-25
