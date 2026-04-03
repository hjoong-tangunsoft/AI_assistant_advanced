# MSA E-Commerce Training

> JetBrains AI Assistant 활용 교육을 위한 **MSA(Microservices Architecture) 기반 E-Commerce 샘플 프로젝트**입니다.

## 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 3.2, Spring Cloud Gateway
- **Messaging**: Apache Kafka
- **Database**: H2 (In-Memory)
- **Build Tool**: Gradle (Kotlin DSL)
- **Containerization**: Docker Compose

## 모듈 구성 및 서비스 포트

| 모듈 | 포트 | 역할 |
|------|------|------|
| `api-gateway` | 8080 | Spring Cloud Gateway, 라우팅 진입점 |
| `user-service` | 8081 | 사용자 등록/조회, UserCreatedEvent 발행 |
| `product-service` | 8082 | 상품/재고 관리, StockUpdatedEvent 발행 |
| `order-service` | 8083 | 주문 생성/조회, OrderCreatedEvent 발행 |
| `common` | - | 공통 DTO(ApiResponse), Kafka 이벤트 클래스 |

## Kafka 이벤트 흐름

| 이벤트 클래스 | 토픽 | 발행 서비스 | 구독 서비스 |
|--------------|------|------------|------------|
| `UserCreatedEvent` | `user-created` | user-service | order-service |
| `StockUpdatedEvent` | `stock-updated` | product-service | order-service |
| `OrderCreatedEvent` | `order-created` | order-service | product-service (재고 차감) |

## 아키텍처 다이어그램

```
[Client] → [API Gateway :8080]
               ├── /users/**  → [User Service :8081]    → Kafka(user-created)
               ├── /products/** → [Product Service :8082] ← Kafka(order-created)
               └── /orders/**  → [Order Service :8083]   ← Kafka(user-created, stock-updated)
```

## 패키지 구조 (각 서비스 공통)

```
com.example.<servicename>/
├── controller/     # REST API 엔드포인트 (@RestController)
├── service/        # 비즈니스 로직 (@Service, @Transactional)
├── repository/     # 데이터 접근 계층 (JpaRepository)
├── entity/         # JPA 엔티티 (@Entity)
├── dto/            # 요청/응답 DTO
├── kafka/          # Kafka Producer / Consumer
└── config/         # 설정 클래스 (@Configuration)
```

## 실행 방법

```bash
# 전체 빌드
./gradlew build

# Docker Compose로 전체 실행 (Kafka 포함)
docker-compose up --build

# 개별 서비스 실행
./gradlew :user-service:bootRun
./gradlew :product-service:bootRun
./gradlew :order-service:bootRun
./gradlew :api-gateway:bootRun
```

## 주요 특징

- 모든 API 응답은 `ApiResponse<T>` 공통 래퍼 클래스 사용
- 전역 예외 처리: `@ControllerAdvice` + `GlobalExceptionHandler`
- 비밀번호 BCrypt 암호화
- H2 인메모리 DB 사용 (서비스 재시작 시 데이터 초기화)
- Kafka 기반 서비스 간 비동기 이벤트 통신
