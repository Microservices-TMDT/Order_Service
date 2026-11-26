# Order Service

Microservice quản lý đơn hàng trong hệ thống thương mại điện tử. Chịu trách nhiệm tạo đơn hàng, điều phối luồng với product-service qua Kafka và cập nhật trạng thái đơn hàng theo kết quả khóa tồn kho.

---

## Tổng quan kiến trúc

```
┌──────────────────────────────────────────────────────────────────────┐
│                         Order Service                                │
│                                                                      │
│  REST API  ──►  OrderController                                      │
│                       │                                              │
│                 OrderServiceImpl                                      │
│                 ├── ProductClient (WebClient → product-service)      │
│                 └── KafkaTemplate  ──► topic: order_created          │
│                                                                      │
│  Kafka Consumer ──► ProductLockedConsumer                            │
│                     (topic: product_locked → status NEW → PREPARED)  │
│                                                                      │
│  Repository ──► OrderRepository / OrderItemRepository (MySQL)        │
└──────────────────────────────────────────────────────────────────────┘
```

### Luồng Saga tạo đơn hàng

```
Client
  │
  ▼  POST /api/v1/orders
OrderService.createOrder()
  │  1. Lưu Order (status=NEW) + OrderItems vào MySQL
  │  2. Publish Kafka event ──► topic: order_created
  │
  ▼
product-service (OrderCreatedConsumer)
  │  Khóa tồn kho (Redisson RLock + PESSIMISTIC_WRITE)
  │  Publish Kafka event ──► topic: product_locked
  │
  ▼
OrderService (ProductLockedConsumer)
     Nhận product_locked → cập nhật Order.status = PREPARED
```

---

## Stack công nghệ

| Thành phần       | Công nghệ                                            |
|------------------|------------------------------------------------------|
| Framework        | Spring Boot 3.4.5, Java 21                           |
| ORM              | Spring Data JPA + Hibernate                          |
| Database         | MySQL 8                                              |
| Messaging        | Apache Kafka                                         |
| HTTP Client      | Spring WebFlux `WebClient` (gọi product-service)     |
| Service Registry | Netflix Eureka Client                                |
| Mapping          | MapStruct 1.6.3                                      |
| Utilities        | Lombok                                               |

---

## API Endpoints

### Orders `/api/v1/orders`

| Method | Path              | Mô tả                                             |
|--------|-------------------|---------------------------------------------------|
| `POST` | `/api/v1/orders`  | Tạo đơn hàng mới, kích hoạt Saga tồn kho          |

---

## Trạng thái đơn hàng (Order Status)

```
       POST /api/v1/orders
              │
              ▼
           [NEW]
              │
              │  Kafka: product_locked
              ▼
         [PREPARED]
              │
              │  (các bước tiếp theo - chưa implement)
              ▼
      [PENDING_PAYMENT] ──► [PAID] ──► [SHIPPED] ──► [DELIVERED]
                                 └──► [CANCELLED]
```

| Status            | Ý nghĩa                                              |
|-------------------|------------------------------------------------------|
| `NEW`             | Đơn hàng vừa được tạo, đang chờ lock tồn kho        |
| `PREPARED`        | Tồn kho đã được khóa thành công                      |
| `PENDING_PAYMENT` | Mặc định trong entity (chưa được dùng trong flow)    |

---

## Kafka Topics

| Topic           | Producer      | Consumer                   | Mô tả                                       |
|-----------------|---------------|----------------------------|---------------------------------------------|
| `order_created` | order-service | product-service            | Kích hoạt khóa tồn kho                      |
| `product_locked`| product-service | `ProductLockedConsumer`  | Báo hiệu lock thành công → chuyển sang PREPARED |

---

## Khởi chạy

### Yêu cầu
- Java 21+
- Maven 3.9+
- MySQL 8 đang chạy tại `localhost:3306`
- Kafka đang chạy tại `localhost:9092`
- Eureka Server tại `localhost:8761`
- product-service đang chạy (để Kafka hoạt động)

### Chạy local
```bash
# Build
mvn clean install -DskipTests

# Chạy
mvn spring-boot:run
# hoặc
java -jar target/order_service-0.0.1-SNAPSHOT.jar
```

Service khởi động tại **http://localhost:8082**

### Cấu hình môi trường (`application.yaml`)

| Property                         | Default                           | Mô tả               |
|----------------------------------|-----------------------------------|---------------------|
| `server.port`                    | `8082`                            | Port HTTP           |
| `spring.datasource.url`          | `jdbc:mysql://localhost:3306/...` | MySQL URL           |
| `spring.kafka.bootstrap-servers` | `localhost:9092`                  | Kafka brokers       |
| `eureka.client.service-url`      | `http://localhost:8761/eureka/`   | Eureka server       |

---

