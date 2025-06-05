# 📅 Scheduler-MSA

**각 교사와 학생의 시간대 별 수강신청 어플리케이션**

---

## 📌 개요

Synology NAS를 기반으로 교사와 학생의 시간대 정보를 기반으로 수업을 예약하고 관리할 수 있는 수강 신청 어플리케이션입니다. 

마이크로서비스 아키텍처(MSA)를 적용하여 각 기능이 독립적으로 배포되고 확장 가능한 구조로 설계되었으며, 주문 서비스를 제외한 각 서비스의 api에 대하여 테스트 케이스가 작성되어 있습니다.

---
## 🏗️ 설계 특징

### 이벤트 기반 아키텍처 

Kafka를 활용한 비동기 처리로 서비스 간 결합도를 낮추고 멱등성을 보장.
RabbitMQ와 아웃박스 이벤트를 이용하여 실패한 이벤트 관리하며 특정 서비스에서 문제가 발생하였을 경우, 보상 트랜잭션 작동.

### 동시성 제어

Redisson 분산 락으로 동일 시간대 예약 충돌 방지.

### 성능 최적화

Redis 캐싱을 통해 빈번한 스케줄 조회 속도 개선.

### 장애 복원력

@CircuitBreaker로 외부 서비스 장애 시 안정성 유지.

---

## 🛠️ 사용 기술

### 🧱 Framework
- **Java** : 17
- **Spring**: Spring Framework, Spring Data JPA, QueryDSL
- **Security**: Spring Security, JWT

### ☁️ Cloud & Messaging
- **Spring Cloud**: Config Server, Eureka, BootStrap, CircuitBreaker
- **Messaging**: Kafka 3.9, RabbitMQ

### 🗄️ Database & Storage
- **RDBMS**: MySQL
- **NoSQL**: Redis

### 🔍 Monitoring
- **Monitoring**: Actuator, Prometheus, Grafana

### 🐳 Containerization & CI/CD
- **Containerization**: Docker, Docker Compose
- **CI/CD**: GitHub Actions

### 🖥️ Infrastructure
- **Synology NAS** (개인 서버 기반 MSA 환경 구축)
  -  제원 : Synology 920+ 
     - CPU: 인텔 Celeron J4125 (4코어, 2.0GHz)
     - RAM : 20GB(4 + 16)
  - 네트워크 : 가정용 네트워크 500Mbps (공유기 : Iptime a5004ns + Iptime a6004mx 메쉬 와이파이 구성)


---


## 📌 Git 저장소 목록
프로젝트의 각 서비스별 GitHub 저장소 링크입니다.

### 🛠️ Infra

- **Discovery Service** (서비스 디스커버리)  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-discovery-service)


- **Config Service** (환경 설정 관리)  
  🔒 *이 저장소는 비공개입니다.*


- **Config** (환경 설정)  
  🔒 *이 저장소는 비공개입니다.*

---


### 🚀 Application Services

- **API Gateway Service**  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-apigateway-service)
  <span style="color: #888;">Spring Cloud Gateway 기반</span>


- **Member Service**  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-member-service)
  <span style="color: #888;">사용자 계정, 인증 및 권한 관리</span>


- **Course Service**  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-course-service)
  <span style="color: #888;">수업 일정 생성 및 관리</span>


- **Article Service**(개발 중)  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-article-service)
  <span style="color: #888;">문의사항 및 게시판 관리.</span>


- **Order Service**   
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-order-service)
  <span style="color: #888;">Kakao, Naver, NicePay와 연동된 결제 처리</span>

---

## 🚀 서비스 엔드포인트
- **Eureka 서버 (서비스 디스커버리)**  
  🔗 [Eureka Dashboard](https://seho0218.synology.me:8762/)


- **Swagger 문서 (API 문서화)**  
  🔗 [Swagger UI](https://seho0218.synology.me:8087/swagger-ui/index.html)

---
## ⚙️ Scheduler-MSA Architecture Diagram
\
![scheduler.png](scheduler.png)

---
## 📚 참고한 강의 리스트

🔗 [김영한의 스프링 로드맵](https://www.inflearn.com/roadmaps/373)

🔗 [김영한의 스프링 부트와 JPA 실무 완전 정복 로드맵](https://www.inflearn.com/roadmaps/149)

🔗 [Spring Cloud로 개발하는 마이크로서비스 애플리케이션(MSA)](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%EC%84%9C%EB%B9%84%EC%8A%A4)

🔗 [스프링부트로 직접 만들면서 배우는 대규모 시스템 설계 - 게시판](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8%EB%A1%9C-%EB%8C%80%EA%B7%9C%EB%AA%A8-%EC%8B%9C%EC%8A%A4%ED%85%9C%EC%84%A4%EA%B3%84-%EA%B2%8C%EC%8B%9C%ED%8C%90)

🔗 [대규모 트래픽 처리를 위한 부하테스트 입문/실전](https://www.inflearn.com/course/%EB%8C%80%EA%B7%9C%EB%AA%A8%ED%8A%B8%EB%9E%98%ED%94%BD-%EB%B6%80%ED%95%98%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%9E%85%EB%AC%B8-%EC%8B%A4%EC%A0%84)


---
## 🛠️ 트러블 슈팅

- [MSA로 전환한 계기와 설계 방향, 그리고 기준](https://dev-hong96.tistory.com/134)
- [160만건에 이르는 데이터에 대한 조회 속도 개선](https://dev-hong96.tistory.com/133)
- [[MSA] 각 서비스간의 인증과 보안( 비밀번호 : 4zMDQ4Nj )](https://dev-hong96.tistory.com/135)
- [[MSA] 데이터 조회(테스트 코드 포함)](https://dev-hong96.tistory.com/136)
- [[MSA] 컨테이너 실행 순서](https://dev-hong96.tistory.com/137)
- [[MSA] 데이터 정합성과 멱등성](https://dev-hong96.tistory.com/138)

