# OAuth2 Authorization Server

## Overview

New microservice `ecommerce-auth` (port 9000) is an **OAuth2 Authorization Server** (Spring Authorization Server, Spring Boot 3.2). It issues JWT access tokens; gateway + services act as **Resource Servers**.

## Components

| Component | Location | Description |
|-----------|----------|-------------|
| `ecommerce-auth` | `:9000` | Authorization Server: `/oauth2/token`, `/oauth2/jwks`, `/oauth2/authorize` |
| `JwtUtil` | `ecommerce-common` | Shared JWT generation/parsing (HMAC, now also RSA JWK for OAuth2) |
| Gateway `JwtAuthenticationGlobalFilter` | `ecommerce-gateway` | Validates Bearer token, forwards `X-User-*` |
| Service `SecurityConfig` | each service | Permits actuator/h2, trusts gateway headers |

## Registered Client (AuthorizationServerConfig)

```java
RegisteredClient.withId(...)
  .clientId("ecommerce-gateway")
  .clientSecret("{noop}gateway-secret")
  .authorizationGrantType(CLIENT_CREDENTIALS)
  .authorizationGrantType(PASSWORD)      // for login flow
  .authorizationGrantType(REFRESH_TOKEN)
  .scope("read").scope("write")
  .tokenSettings(accessTokenTTL=1h, SELF_CONTAINED)
```

## RSA JWK

Server generates 2048-bit RSA key pair on startup; exposes public JWK at `http://localhost:9000/oauth2/jwks`. Resource servers fetch it to verify signatures.

## Flows

### 1. Client Credentials (service-to-service)

```bash
curl -X POST http://localhost:9000/oauth2/token \
  -u ecommerce-gateway:gateway-secret \
  -d grant_type=client_credentials -d scope=read
```

### 2. Password Grant (user login via gateway — legacy)

```bash
curl -X POST http://localhost:9000/oauth2/token \
  -u ecommerce-gateway:gateway-secret \
  -d grant_type=password -d username=user@example.com -d password=secret -d scope=read
```

### Current User Login (via user-service)

For simplicity, `POST /api/auth/login` in `user-service` still issues HMAC JWT via `JwtUtil.generateToken()`. To switch to OAuth2, point it to `http://ecommerce-auth:9000/oauth2/token` or replace with Authorization Server's token endpoint.

## Resource Server Configuration (future)

Add to each service `application.yml`:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://ecommerce-auth:9000
          jwk-set-uri: http://ecommerce-auth:9000/oauth2/jwks
```

And dependency `spring-boot-starter-oauth2-resource-server` (transitively via `ecommerce-common` if centralized).

## Running

```bash
docker compose up ecommerce-auth -d
curl http://localhost:9000/.well-known/oauth-authorization-server
```

## Security Hardening

- Gateway `JwtAuthenticationGlobalFilter` validates token before routing; open paths: `/api/auth/**`, `POST /api/users`, `/actuator/**`, `/h2-console/**`
- Services keep `SecurityConfig` permissive for dev; enable `authenticated()` for `/api/**` in prod and enforce `X-User-Id` presence
- Next: add RBAC checks (`@PreAuthorize("hasRole('ADMIN')")`) using `role` claim from JWT

## Common Module

`ecommerce-common` now holds `JwtUtil` + `CorrelationIdFilter` + security deps (`spring-security`, `jjwt`). All 5 services inherit them transitively — no duplication.
