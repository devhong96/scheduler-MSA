# 📅 Scheduler-MSA (1인 개발)

Synology NAS를 기반으로 교사와 학생의 수업을 예약하고 관리할 수 있는 수강 신청 어플리케이션입니다. 

  이 프로젝트는 1년전, **모놀리식**에서 시작하여 **마이크로서비스**(MSA)로 조금씩 전환되는 과정을 기록하고 있습니다. 각 기능은 독립적으로 배포되고 확장 가능한 구조로 재설계 되었으며, 주문 서비스를 제외한 각 서비스 API에 대하여 테스트 케이스가 작성되어 있습니다.

**🔗 모놀리식 버전 GitHub 저장소**: https://github.com/devhong96/scheduler

---
## 🚀 서비스 엔드포인트
- **Eureka 서버 (서비스 디스커버리)**  
  🔗 [Eureka Dashboard](https://seho0218.synology.me:8762/)<br><br>

- **Swagger 문서 (API 문서화)**  
  🔗 [Swagger UI](https://seho0218.synology.me:8087/swagger-ui/index.html)

---
### 🛠️ 기술 스택 (Tech Stack)

| 구분             | 기술                                                                     |
| :------------- | :--------------------------------------------------------------------- |
| **Backend**    | Java 17, Spring Boot 3, JPA, QueryDSL, Spring Security 6, JWT          |
| **MQ**         | Kafka (3.8.x), RabbitMQ                                                |
| **Cloud**      | Spring Cloud 4.x (Eureka, Config, Gateway, BootStrap, CircuitBreaker)  |
| **Database**   | MySQL 8.0, Redis                                                       |
| **DevOps**     | Docker, Docker-Compose, GitHub Actions, Docker Hub, Synology NAS (DSM) |
| **Monitoring** | Prometheus, Grafana, Spring Boot Actuator                              |
### 🖥️ Infrastructure
- **Synology NAS** (개인 서버 기반 MSA 환경 구축)
    -  Hardware : Synology 920+
        - CPU: intel CeleronJ4125 (4C 4T, Base 2.0GHz, Boost 2.70 GHz)
        - RAM : 20GB(4 + 16)
    - 네트워크 : 가정용 네트워크 500Mbps (공유기 : iptime a5004ns)

---

## 🏗️ 설계 특징

#### **사용자 관리 (Member Management)**

- **회원 가입 및 인증**: 이메일 인증을 통해 안전한 회원가입 절차와 비밀번호 찾기 기능을 제공

- **교사 등록 승인**: 관리자가 교사의 회원 가입 요청을 검토하고 승인

- **학생 등록 승인**: 교사는 학생의 회원가입을 승인

#### **수업 및 스케줄 관리 (Course & Schedule Management)**

- **스케줄 설정**: **교사**가 수업을 진행할 수 있는 날짜와 시간을 자유롭게 등록하고 관리

- **수강 신청**: **학생**이 교사가 등록한 스케줄을 확인하고 실시간으로 수강을 신청

#### **결제 및 시스템 (Payment & System)**

- **온라인 결제**: 카카오, 네이버, 나이스페이 등 다양한 PG사와 연동하여 편리한 결제 기능을 제공

- **안정적인 예약 처리**: **Redisson 분산 락**을 통해 동시성 문제 해결

- **신뢰성 있는 데이터 처리**: Kafka와 RabbitMQ 기반의 비동기 이벤트를 통해 결제, 예약 등의 데이터를 안정적으로 처리

---
### 기본 기능

#### 공통 기능
- 모든 입력 조회 폼에서 입력해야하는 칸에 아무것도 입력하지 않은 경우 오류 메시지가 발생한다.
- 교사와 관리자는 이메일을 통해 아이디 찾기와 비밀번호 찾기를 할 수 있으며 이메일 변경도 가능하다.
- 기본적으로 선착순으로 처리되며 학생이 1시에 신청을 했을 경우,다른 학생은 1시를 선택할 수 없다.
- 학생 명부에 등록된 학생만 수강 신청과 시간표 변경을 할 수 있다.
- Spring security 와 관리자 계정을 통해 교사 아이디에 승인 절차를 부여할 수 있도록 되어있다.
- 관리자 계정은 최고 권한을 갖기 때문에 회원가입을 통해 계정이 생성되지 않고   
  어플리케이션 실행시 하나의 계정만 생성되며 생성되어 있을 경우 추가 생성되지는 않는다.

#### 관리자
- 공지사항을 작성 및 수정 삭제할 수 있다.
- 교사의 권한과 역할을 포함한다.
- 교사 회원 가입시 승인 절차 없이 바로 스케쥴을 관리할 수 있으면 문제가 발생할 수 있기에  
  회원가입한 교사에게 회원 가입한 교사의 권한 인가와 회수를 할 수 있다.
- 학생의 담임 교사를 변경할 수 있다.  
  '갑'선생님에서 '을'선생님으로 담임교사를 변경할 때, 시간이 겹칠 경우 오류메시지를 보여준다.
- 교사 계정을 삭제할 수 있으며 삭제할시, 시간표도 같이 삭제된다.
- 어플리케이션 실행시 자동으로 1개의 계정만 생성된다.

#### 교사
- 회원 가입한 후, 관리자의 권한 인가를 받은 교사만 역할을 수행 할 수 있다.
- 학생을 등록, 삭제하고 시간표를 변경할 수 있다.
- 학생을 삭제할 경우, 학생이 등록한 시간표가 같이 사라진다.
- 교사별 스케쥴이 따로 있어 '갑'선생님이 월요일 1시에 수업이 있어도 '을'선생님이 월요일 1시에 수업을 할 수 있다.

#### 학생
- 담당 교사가 등록해줬을 경우, 수업을 조회하거나 신청, 변경을 할 수 있다.

- ### 인증 및 보안
- 교사 회원가입을 통해 회원가입을 할 경우, BCrypt를 통해 비밀번호가 암호화 되고 관리자의 인가를 받아서 학생의 수업을 삭제할 수 있다.
- 누구나 관리자 아이디에 접근이 가능하도록 되어있기 때문에 주기적(1시간) 단위로 비밀번호가 README의 로그인 정보에 있는 비밀번호로 갱신되며 실제로 서비스할 경우, 이 기능은 탑재되지 않는다.
- 입력한 이메일과 아이디를 바탕으로 아이디 찾기 및 비밀번호 찾기가 가능하다.
- 아이디는 가입시 입력한 이메일로 gmail을 통해 전송되며 비밀번호도 마찬가지로 가입시 입력한 gmail을 통해 발송된 인증번호를 바탕으로 비밀번호를 재설정할 수 있다.

---

## ⚙️ Scheduler-MSA Architecture Diagram

![scheduler.png](scheduler.png)


---
## 📌 Git 저장소 목록

### 🛠️ Infra

- **Discovery Service** (서비스 디스커버리)<br>🔗 [GitHub Repository](https://github.com/devhong96/scheduler-discovery-service)

- **Config Service** (환경 설정 관리)  
  🔒 *이 저장소는 비공개입니다.*

- **Config** (환경 설정)  
  🔒 *이 저장소는 비공개입니다.*

---

### 🚀 Application Services Repository

- **API Gateway Service**<span style="color: #888;"> Spring Cloud Gateway 기반</span><br>🔗 [GitHub Repository](https://github.com/devhong96/scheduler-apigateway-service)
 

- **Member Service**<span style="color: #888;"> 사용자 계정, 인증 및 권한 관리</span><br>🔗 [GitHub Repository](https://github.com/devhong96/scheduler-member-service)


- **Course Service**<span style="color: #888;"> 수업 일정 생성 및 관리</span><br>🔗 [GitHub Repository](https://github.com/devhong96/scheduler-course-service)


- **Article Service**(개발 중)<span style="color: #888;"> 문의사항 및 게시판 관리.</span><br>🔗 [GitHub Repository](https://github.com/devhong96/scheduler-article-service)


- **Order Service**<span style="color: #888;"> Kakao, Naver, NicePay와 연동된 결제 처리</span><br>🔗 [GitHub Repository](https://github.com/devhong96/scheduler-order-service)

---
## 📚 참고한 강의

🔗 [김영한의 스프링 로드맵](https://www.inflearn.com/roadmaps/373)

🔗 [김영한의 스프링 부트와 JPA 실무 완전 정복 로드맵](https://www.inflearn.com/roadmaps/149)

🔗 [Spring Cloud로 개발하는 마이크로서비스 애플리케이션(MSA)](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%EC%84%9C%EB%B9%84%EC%8A%A4)

🔗 [스프링부트로 직접 만들면서 배우는 대규모 시스템 설계 - 게시판](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8%EB%A1%9C-%EB%8C%80%EA%B7%9C%EB%AA%A8-%EC%8B%9C%EC%8A%A4%ED%85%9C%EC%84%A4%EA%B3%84-%EA%B2%8C%EC%8B%9C%ED%8C%90)

🔗 [대규모 트래픽 처리를 위한 부하테스트 입문/실전](https://www.inflearn.com/course/%EB%8C%80%EA%B7%9C%EB%AA%A8%ED%8A%B8%EB%9E%98%ED%94%BD-%EB%B6%80%ED%95%98%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%9E%85%EB%AC%B8-%EC%8B%A4%EC%A0%84)


