# Ecommerce Microservices

Production-ready microservices backend with Spring Boot 3.2, Spring Cloud 2023, MySQL/H2, Eureka, Gateway, JWT.

## Services

| Service | Port | Description |
|---------|------|-------------|
| ecommerce-registry | 8761 | Eureka Service Discovery |
| ecommerce-config | 8888 | Config Server |
| ecommerce-gateway | 8080 | API Gateway (routes `/api/**`) |
| user-service | 8081 | Auth, registration, profile (JWT) |
| product-service | 8082 | Products & categories |
| order-service | 8083 | Orders & order items |
| payment-service | 8084 | Payments |
| cart-service | 8085 | Shopping cart (RestClient → product-service) |

## Quick Start

### Dev (H2 in-memory, no Docker)

```bash
.\mvnw clean install -DskipTests
# Run each service: .\mvnw spring-boot:run -pl <service> -am
# Gateway: http://localhost:8080/api/users
```

### Prod (MySQL + Docker)

```bash
docker compose up --build -d   # builds all 9 services + MySQL + phpMyAdmin
# phpMyAdmin: http://localhost:8086
# Services use: --spring.profiles.active=mysql
```

## API Examples

```bash
# Register
POST http://localhost:8080/api/users
{ "firstName":"Satish","lastName":"P","email":"a@b.com","password":"secret","phone":"123" }

# Login -> JWT
POST http://localhost:8080/api/auth/login
{ "email":"a@b.com","password":"secret" }

# Products
GET  http://localhost:8080/api/products?search=iphone
GET  http://localhost:8080/api/categories

# Cart
POST http://localhost:8080/api/carts/1/items  { "productId": 1 }
GET  http://localhost:8080/api/carts/user/1/or-create
GET  http://localhost:8080/api/carts/1/total

# Orders & Payments
POST http://localhost:8080/api/orders
POST http://localhost:8080/api/payments
```

## Profiles

- `h2` (default): `jdbc:h2:mem:<db>` — zero setup for learning
- `mysql`: `jdbc:mysql://localhost:3306/<db>` — set `SPRING_PROFILES_ACTIVE=mysql` or `docker compose up`

## Docs

- `docs/topics/` — per-concept notes (@Transactional, Cart patterns)
- `docs/services/SERVICE_LAYER_COMPLETE_GUIDE.md` — full service layer reference
