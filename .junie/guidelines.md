# Project Guidelines

이 파일은 JetBrains Junie가 코드 제안 시 참고하는 프로젝트 단위 지침입니다.

## 프로젝트 개요
- **프로젝트명**: MSA E-Commerce Training
- **목적**: JetBrains AI Assistant 활용 교육용 MSA 샘플 프로젝트
- **아키텍처**: Microservices Architecture (Spring Cloud)
- **기술 스택**: Java 17, Spring Boot 3.2, Spring Cloud Gateway, Apache Kafka, H2 Database, Gradle (Kotlin DSL)

## 모듈 구성 및 서비스 포트

| 모듈 | 포트 | 역할 |
|------|------|------|
| `api-gateway` | 8080 | Spring Cloud Gateway, 라우팅 진입점 |
| `user-service` | 8081 | 사용자 등록/조회, UserCreatedEvent 발행 |
| `product-service` | 8082 | 상품/재고 관리, StockUpdatedEvent 발행 |
| `order-service` | 8083 | 주문 생성/조회, OrderCreatedEvent 발행 |
| `common` | - | 공통 DTO(ApiResponse), Kafka 이벤트 클래스 |

## 코딩 컨벤션
- 클래스명: PascalCase, 메서드명: camelCase, 상수: UPPER_SNAKE_CASE
- Controller: `@RestController` + `@RequestMapping`
- Service: `@Service` + `@Transactional`
- 모든 응답은 `ApiResponse<T>` 래퍼 클래스 사용
- 테스트 메서드명: `should_기대결과_when_조건` 형식
- 에러 처리: `@ControllerAdvice` + `GlobalExceptionHandler`
