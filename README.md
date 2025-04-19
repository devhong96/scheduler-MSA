# 📅 Scheduler-MSA

**각 교사와 학생의 시간대 별 수강신청 어플리케이션**

---

## 📌 개요

Synology NAS를 기반으로 교사와 학생의 시간대 정보를 기반으로 수업을 예약하고 관리할 수 있는 수강 신청 어플리케이션입니다. 

마이크로서비스 아키텍처(MSA)를 적용하여 각 기능이 독립적으로 배포되고 확장 가능한 구조로 설계되었습니다.

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
- **Messaging**: Kafka(ver.3.8.1), RabbitMQ

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


---


## 📌 Git 저장소 목록
프로젝트의 각 서비스별 GitHub 저장소 링크입니다.

- **Scheduler Discovery Service** (서비스 디스커버리)  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-discovery-service)


- **Scheduler Config Service** (환경 설정 관리)  
  🔒 *이 저장소는 비공개입니다.*


- **Scheduler Config** (환경 설정)  
  🔒 *이 저장소는 비공개입니다.*


- **Scheduler API Gateway Service** (API 게이트웨이)  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-apigateway-service)


- **Scheduler Member Service** (회원 관리)  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-member-service)


- **Scheduler Course Service** (수업 관리)  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-course-service)


- **Scheduler Order Service** (주문 서비스, kakao, naver, nicepay)  
  🔗 [GitHub Repository](https://github.com/devhong96/scheduler-order-service)

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
## 🛠️ 트러블 슈팅

- [MSA로 전환한 계기와 설계 방향, 그리고 기준](https://dev-hong96.tistory.com/134)
- [160만건에 이르는 데이터에 대한 조회 속도 개선](https://dev-hong96.tistory.com/133)
- [[MSA] 각 서비스간의 인증과 보안( 비밀번호 : 4zMDQ4Nj )](https://dev-hong96.tistory.com/135)
- [[MSA] 데이터 조회(테스트 코드 포함)](https://dev-hong96.tistory.com/136)
- [[MSA] 컨테이너 실행 순서](https://dev-hong96.tistory.com/137)
- [[MSA] 데이터 정합성과 멱등성](https://dev-hong96.tistory.com/138)

