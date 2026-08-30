# Database per Service Pattern

## Concept

Each microservice owns its database. No direct cross-service table access. This is a core microservices principle: **decentralized data management**.

## Our Implementation (Logical Separation)

One MySQL instance (`ecommerce-mysql:3306`) with **5 logical databases**:

| Service | JDBC URL (mysql profile) | Database | Tables Owned |
|---------|--------------------------|----------|--------------|
| user-service | `jdbc:mysql://localhost:3306/userdb?createDatabaseIfNotExist=true` | `userdb` | `users` |
| product-service | `jdbc:mysql://localhost:3306/productdb?...` | `productdb` | `products`, `categories` |
| order-service | `jdbc:mysql://localhost:3306/orderdb?...` | `orderdb` | `orders`, `order_items` |
| payment-service | `jdbc:mysql://localhost:3306/paymentdb?...` | `paymentdb` | `payments` |
| cart-service | `jdbc:mysql://localhost:3306/cartdb?...` | `cartdb` | `carts`, `cart_items` |

- `createDatabaseIfNotExist=true` auto-creates each DB on first connection.
- `spring.jpa.hibernate.ddl-auto: update` creates tables inside each DB.

## Physical Separation (Production)

For true isolation, run separate MySQL instances/containers:

```yaml
mysql-user:
  image: mysql:8.0
  environment: { MYSQL_DATABASE: userdb }

mysql-product:
  image: mysql:8.0
  environment: { MYSQL_DATABASE: productdb }
```

Our `docker-compose.yml` uses single instance for dev simplicity; switch to 5 instances by adding services and updating each service's `SPRING_DATASOURCE_URL`.

## Cross-Service Data Access

Forbidden: `cart-service` must NOT query `productdb.products` via local repository.

Allowed patterns:

1. **API composition (sync):** `cart-service` calls `product-service` via RestClient (`http://product-service/api/products/{id}`) — we implement this via `ProductServiceClient` with Resilience4j.
2. **Event-driven (async):** `order-service` publishes `OrderCreatedEvent` via RabbitMQ/Kafka; `payment-service` consumes it.

## Verification

```bash
docker compose up -d
docker exec -it ecommerce-mysql mysql -uroot -proot -e "SHOW DATABASES;"
# Should list: userdb, productdb, orderdb, paymentdb, cartdb
```

## Trade-offs

- Pros: independent scaling, schema evolution per service, failure isolation
- Cons: no cross-service JOINs or ACID transactions; eventual consistency must be handled at application layer (Saga pattern)
