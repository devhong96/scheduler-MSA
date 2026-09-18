# 프로젝트 도메인 명세서 (Domain Specifications)

AI는 코드를 생성할 때 이 문서에 정의된 도메인들을 `architecture.md`에 명시된 격리 구조(Modular Monolith)에 맞춰 구현해야 합니다.

## 1. Course (수강 도메인)
* **역할:** 수강 스케줄 관리, 정원 관리, 강사 할당
* **주요 엔티티:** `CourseSchedule`, `CourseRegistration`
* **공개 API (Port):** 타 도메인에서 특정 강의의 남은 정원을 조회할 수 있는 인터페이스 제공

## 2. Member (회원 도메인)
* **역할:** 학생 및 강사의 정보 관리, 권한 처리
* **주요 엔티티:** `Member`, `Role`
* **공개 API (Port):** 회원 식별자(ID)로 기본 프로필 정보를 제공하는 인터페이스 제공

## 3. Notification (알림 도메인)
* **역할:** 수강 신청 완료, 취소 등 이벤트 발생 시 사용자에게 알림 전송
* **통신 방식:** Member나 Course를 직접 의존하지 않고, Spring Event(ApplicationEventPublisher)를 구독하여 비동기로 처리할 것.