### 📄 2. `geminirules.md` (범용 코딩 컨벤션)

```markdown
### 🛠️ geminirules (AI 코드 생성 강제 지침)

# Gemini AI Code Generation Rules

너는 이 프로젝트의 코드를 작성할 때 다음 규칙을 100% 엄격하게 준수해야 한다. 이유 불문하고 무조건 지켜라.

## 1. Domain & Entity Rules (가장 중요)
* **DTO 역참조 절대 금지:** JPA Entity 내부에서 웹 계층의 `Request`, `Response` 등 DTO 클래스를 절대 `import` 하거나 매개변수로 받지 마라.
* **상태 변경:** 엔티티에 `@Setter` 사용을 엄격히 금지한다. `updateStatus()`, `changeOwner()` 등 비즈니스 의미가 명확한 메서드를 통해서만 상태를 변경해라.
* **OS 종속성 배제:** 엔티티 내부에서 `Locale.getDefault()` 같은 시스템 종속적인 코드를 사용하지 마라.
* **BaseEntity (Auditing):** 모든 엔티티는 `common/domain/BaseEntity`를 상속받는다. Spring Data JPA 표준인 `@CreatedDate`, `@LastModifiedDate`를 사용하며, 엔티티 클래스 상단에 반드시 `@EntityListeners(AuditingEntityListener.class)`를 부착해라.

## 2. Service Layer Rules
* **트랜잭션 최적화:** Service 클래스 상단에 `@Transactional(readOnly = true)`를 기본으로 적용하고, 데이터 변경(CUD)이 일어나는 메서드에만 `@Transactional`을 명시적으로 붙여라.
* **반환 객체:** Service 메서드는 절대로 `Entity`를 Controller로 직접 반환하지 마라. 반드시 도메인 객체나 Service 전용 DTO로 변환하여 반환해라.

## 3. 의존성 주입 (Dependency Injection)
* 스프링 빈 주입 시 `@Autowired`는 절대 사용하지 않는다.
* 모든 의존성은 `final` 키워드와 롬복(Lombok)의 `@RequiredArgsConstructor`를 통한 생성자 주입만 사용해라.

## 4. DTO & 불변성
* 클라이언트와 통신하는 모든 DTO(Request, Response)와 메시지 큐 객체는 Java 21의 `record`를 사용하여 불변 객체로 생성해라.

## 5. Exception Handling (중복 생성 방지)
* 새로운 Custom Exception 클래스를 무단으로 만들지 마라.
* 비즈니스 예외가 필요할 경우, `common/exception/ErrorCode` Enum에 에러 코드를 추가하고 `throw new BusinessException(ErrorCode.XXX)` 형태로 발생시켜라.

## 6. Cross-Domain 호출 규칙 (Modular Monolith 환경)
* 타 모듈(도메인)의 데이터를 조회하거나 조작할 때는 타 모듈의 `api` 패키지에 있는 인터페이스만 의존성 주입(`@RequiredArgsConstructor`)을 받아 사용해라.
* 현재 단계에서는 `@FeignClient`를 생성하거나 사용하지 않는다.

```

