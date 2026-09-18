### 📄 1. `architecture.md` (범용 아키텍처 & 설계 원칙)

```markdown
# 프로젝트 아키텍처 및 설계 원칙

## 1. System Context & Technology Stack
본 프로젝트는 **모듈러 모놀리식(Modular Monolithic)** 아키텍처를 채택합니다. 단일 애플리케이션으로 배포되지만, 내부적으로는 각 비즈니스 도메인이 독립적인 모듈로 철저히 격리되어 추후 MSA로의 추출(Extraction)이 원활하도록 설계합니다.

* **Language:** Java 21 (Record 적극 활용)
* **Framework:** Spring Boot 3.x
* **Data Access:** Spring Data JPA, QueryDSL
* **Database:** MySQL / H2 (Development)
* **Security**: JWT 토큰, Spring Security
* **문서화**: SpringDoc OpenAPI
* **Cache & Infrastructure:** Redis, RabbitMQ (비동기 처리용)

## 2. Project Structure
물리적으로는 하나의 프로젝트지만, 논리적으로는 완벽히 분리된 구조를 가집니다. 다른 도메인의 내부 로직(Service, Repository)을 직접 호출하는 것은 엄격히 금지됩니다.

```text
com.company.projectname
 ├── common/               # [공통 영역] 시스템 전반 공유 자산
 │    ├── domain/          # BaseEntity (Auditing)
 │    └── exception/       # GlobalExceptionHandler, BaseException, ErrorCode Enum
 │
 ├── {domain-a}/           # [비즈니스 도메인 A] (예: user, order 등)
 │    ├── controller/      # HTTP 요청 수신 (프론트엔드 API)
 │    ├── domain/          # 핵심 엔티티 및 도메인 서비스
 │    ├── dto/             # 계층 간 데이터 전송용 Record 객체
 │    ├── repository/      # 데이터 접근 인터페이스 및 QueryDSL
 │    ├── service/         # 애플리케이션 서비스
 │    └── api/             # (Port) 타 도메인에서 본 도메인을 호출할 때 사용하는 공개 인터페이스
 │
 ├── {domain-b}/           # [비즈니스 도메인 B]
 │    └── ... (동일한 구조)
 │
 └── infra/                # [기술 인프라] 외부 기술 구현체 및 설정
      ├── config/          # Async, QueryDSL, Redis 설정
      └── cache/           # Redis Cache 구현체

```

## 3. Architecture Rules & Design Principles

### 3.1. Cross-Domain Communication (모듈 간 통신 원칙)

현재는 모놀리식 환경이므로 S2S(Server-to-Server) HTTP 통신을 위한 `@FeignClient`나 내부용 Controller는 사용하지 않습니다. 대신 아래의 원칙을 따릅니다.

* **동기 통신 (조회 등):** 상대 도메인의 `api` 패키지에 정의된 **공개 인터페이스(Facade/Port)**를 주입받아 메서드로 호출합니다. 상대방의 Repository나 Service를 직접 의존하는 것은 절대 금지됩니다.
* **비동기 통신 (상태 변경 후 전파):** 결합도를 끊기 위해 Spring의 `ApplicationEventPublisher` (또는 RabbitMQ)를 사용하여 이벤트를 발행하고 구독합니다.

### 3.2. Layered Constraints

* **Domain Layer:** JPA Entity는 웹 계층의 DTO를 알지 못해야 합니다(DTO 역참조 금지).
* **Service Layer:** 조회 로직은 `@Transactional(readOnly = true)`를 기본으로 사용하고, Controller로 Entity를 직접 반환하지 않습니다.

