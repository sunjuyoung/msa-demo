# 🌞 Sun Board - 마이크로서비스 기반 게시판 시스템

## 📋 프로젝트 개요

**Sun Board**는 Spring Boot 3.4.2와 Spring Cloud 2024.0.2를 기반으로 구축된 마이크로서비스 아키텍처의 데모 시스템입니다.



## 🚀 주요 서비스

### 1. **API Gateway** (`service/apigateway`)

- 모든 클라이언트 요청의 진입점
- JWT 인증 필터링
- 서비스 라우팅 및 로드 밸런싱

### 2. **Eureka Server** (`service/eureka`)

- 서비스 디스커버리 및 등록
- 마이크로서비스 간 통신을 위한 중앙 레지스트리

### 3. **Member Service** (`service/member`)

- 사용자 회원가입, 로그인, 인증
- JPA를 사용한 사용자 정보 관리
- Role 기반 권한 관리

### 4. **Article Service** (`service/article`)

- 게시글 CRUD 작업
- 게시판별 게시글 관리
- 작성자 정보 연동

### 5. **Comment Service** (`service/comment`)

- 댓글 작성, 수정, 삭제
- 게시글과 댓글의 계층 구조 관리

### 6. **View Service** (`service/view`)

- 게시글 조회수 추적
- 사용자별 조회 이력 관리

### 7. **Like Service** (`service/like`)

- 게시글 좋아요 기능
- 사용자별 좋아요 상태 관리

### 8. **Hot Article Service** (`service/hot-article`)

- 인기 게시글 선정 알고리즘
- 이벤트 기반 점수 계산
- 일별 인기글 랭킹 제공

### 9. **Article Read Service** (`service/article-read`)

- 게시글 읽기 전용 서비스
- 캐시를 통한 성능 최적화
- 이벤트 소비를 통한 데이터 동기화


### 10. **Product Service** (`service/product`)

- 상품 정보 관리
- 상품 목록 및 상세 조회
- 재고 관리

### 11 **Ordering Service** (`service/ordering`)

- 주문 생성 및 관리
- 주문 상태 추적
- 주문 이력 관리



### 12. **Payment Service** (`service/payment`)

- 결제 처리 및 관리
- 결제 이력 추적

## 🔧 공통 라이브러리 (Common)

### 1. **Event System** (`common/event`)

- 서비스 간 이벤트 통신
- JSON 직렬화/역직렬화 지원
- 이벤트 타입별 페이로드 관리

### 2. **Outbox Message Relay** (`common/outbox-message-relay`)

- 메시지 전송의 신뢰성 보장
- 샤딩 기반 메시지 분산 처리
- 장애 복구 및 재시도 메커니즘

### 3. **Snowflake ID Generator** (`common/snowflake`)

- 분산 환경에서의 고유 ID 생성
- 64비트 ID 구조 (타임스탬프 + 노드ID + 시퀀스)
- UUID 대비 성능 최적화

### 4. **Data Serializer** (`common/data-serializer`)

- 객체 직렬화/역직렬화 유틸리티
- JSON 변환 지원

## 🛠️ 기술 스택

### Backend

- **Java 21** - 최신 LTS 버전
- **Spring Boot 3.4.2** - 메인 프레임워크
- **Spring Cloud 2024.0.2** - 마이크로서비스 지원
- **Spring Data JPA** - 데이터 접근 계층
- **Spring Security** - 인증 및 권한 관리

### Database & Cache

- **JPA/Hibernate** - ORM
- **Redis** - 캐싱 및 세션 저장소

### Message Queue

- **Kafka** - 이벤트 스트리밍 및 메시지 브로커

### Service Discovery

- **Netflix Eureka** - 서비스 디스커버리

### Build Tool

- **Gradle** - 의존성 관리 및 빌드

### Development Tools

- **Lombok** - 보일러플레이트 코드 제거
- **Logback** - 로깅 프레임워크

## 🚀 시작하기

### Prerequisites

- Java 21
- Gradle 8.0+
- Docker (선택사항)
- Kafka
- Redis
- Database (MySQL/PostgreSQL)





## 🔄 이벤트 기반 통신

시스템은 이벤트 기반 아키텍처를 사용하여 서비스 간 느슨한 결합을 구현합니다:

### 주요 이벤트 타입

- `ARTICLE_CREATED` - 게시글 생성
- `ARTICLE_UPDATED` - 게시글 수정
- `ARTICLE_DELETED` - 게시글 삭제
- `COMMENT_CREATED` - 댓글 생성
- `LIKE_ADDED` - 좋아요 추가
- `VIEW_COUNTED` - 조회수 증가

### 이벤트 처리 흐름

1. **이벤트 발생**: 서비스에서 도메인 이벤트 생성
2. **Outbox 저장**: 이벤트를 로컬 데이터베이스에 저장
3. **메시지 릴레이**: 백그라운드에서 Kafka로 전송
4. **이벤트 소비**: 구독 서비스에서 이벤트 처리
5. **상태 동기화**: 서비스 간 데이터 일관성 유지

## 🏗️ 데이터베이스 설계

### 주요 엔티티

- **Member**: 사용자 정보 및 권한
- **Article**: 게시글 내용 및 메타데이터
- **Comment**: 댓글 및 계층 구조
- **View**: 조회 이력 및 통계
- **Like**: 좋아요 상태
- **HotArticle**: 인기글 랭킹

## 🔒 보안

- **JWT 기반 인증**: API Gateway에서 토큰 검증
- **Role 기반 권한 관리**: 사용자, 관리자 권한 분리
- **API 보안**: Spring Security를 통한 엔드포인트 보호

## 📈 성능 최적화

- **캐싱**: Redis를 통한 자주 조회되는 데이터 캐싱
- **비동기 처리**: 이벤트 기반 비동기 메시지 처리
- **데이터베이스 최적화**: JPA 인덱싱 및 쿼리 최적화

