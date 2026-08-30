# Service Layer Complete Guide (User + Cart)

All service layer patterns and implementations for the ecommerce microservices.

---

## 1. Architecture Overview

```
Controller  ->  DTO  <->  Service  ->  Repository  ->  Entity  ->  DB
                  Mapper          @Transactional
```

---

## 2. User Service

### Entity: User

- Fields: id, firstName, lastName, email (unique), password, phone, userRole, active, resetToken, tokenExpiry
- `@PrePersist` sets active=true, role=CUSTOMER, timestamps

### Repository: UserRepository

| Method | Return Type | Purpose |
|--------|-------------|---------|
| findByEmail | Optional<User> | View profile |
| existsByEmail | boolean | Duplicate check |
| findByUserRoleAndActive | List<User> | Filter users |
| findByResetToken | Optional<User> | Password reset |
| findByFirstNameContainingIgnoreCase | List<User> | Search |

### Service: UserService + UserServiceImp

| Method | Logic |
|--------|-------|
| createUser | existsByEmail check -> encode password -> save |
| getUserById / getUserByEmail | orElseThrow |
| getUsersByUserRoleAndActive | direct delegation |
| updateUser | find -> set allowed fields -> save |
| deactivateUser | self-check with .equals() -> set active=false |
| forgotPassword | findByEmail -> UUID token -> set expiry 1h -> save |
| resetPassword | findByResetToken -> check expiry -> encode -> clear token |

Security: `PasswordConfig` provides `BCryptPasswordEncoder` bean via `spring-security-crypto`.

---

## 3. Cart Service

### Entities: Cart, CartItem

- User 1--1 Cart, Cart 1--N CartItem, CartItem N--1 Product
- Cart: totalAmount, totalItems, @OneToMany(cascade=ALL) items
- CartItem: quantity, price (snapshot), subtotal (qty * price)

### Repositories

**CartRepository**

| Method | Purpose |
|--------|---------|
| findByUserId | Get user's cart (traverses user.id) |
| existsByUserId | Check cart existence |
| findByUpdatedAtBefore | Abandoned carts |
| countNonEmptyCarts | @Query SIZE(items)>0 |

**CartItemRepository**

| Method | Purpose |
|--------|---------|
| findByCartId | All items in cart (Q3) |
| findByCartIdAndProductId | Duplicate check (Q4) |
| deleteByCartId | Clear cart (Q6) |
| @Query quantity/revenue | Analytics |

**ProductRepository (temporary)**

- Local copy in cart-service for learning phase
- Replaced by RestClient after controller phase (database-per-service)

### Service: CartService (cart-level)

| Method | Logic |
|--------|-------|
| getOrCreateCart | findByUserId.orElseGet(save new cart) |
| getCartByUserId | orElseThrow |
| existsByUserId | existsByUserId delegation |
| countActiveCarts | countNonEmptyCarts query |
| findAbandonedCarts | findByUpdatedAtBefore(now - days) |
| emptyCart | find cart -> deleteByCartId -> reset totals |

### Service: CartItemService (item-level)

| Method | Logic |
|--------|-------|
| getCartItemsByCartId | findByCartId |
| getCartItemByCartIdAndProductId | findByCartIdAndProductId |
| addProductToCart | Q4: check existing -> inc qty OR fetch product -> validate active/stock -> build -> save |
| removeProductFromCart | find -> delete |
| calculateCartTotal | Q5: fetch items -> loop sum quantity + subtotal -> CartTotalResponse |

---

## 4. Key Patterns

### Get-or-Create

```java
cartRepository.findByUserId(userId)
    .orElseGet(() -> cartRepository.save(Cart.builder().user(User.builder().id(userId).build()).build()));
```

### Add-or-Increase

- Found -> quantity++, recalc subtotal
- Not found -> fetch product, validate, create new item

### Java-side Totals (vs DB SUM)

- Cart page already loads items for display -> free to sum in Java
- DB aggregation only for analytics on millions of rows

### DTO Pattern

- Entity never leaves service boundary
- UserResponse excludes password
- CartTotalResponse is immutable snapshot
- Mapper: manual static methods (MapStruct later if desired)

### @Transactional

- Multiple writes -> atomic (addProductToCart, emptyCart, getOrCreateCart)
- Single reads -> not needed
- Derived deletes (deleteByCartId) REQUIRE it

### Database-per-Service

- Now: ProductRepository local (learning)
- Later: ProductServiceClient via RestClient @LoadBalanced -> http://product-service

### Spring Profiles

```yaml
spring.profiles.active: h2   # default: jdbc:h2:mem
# switch: --spring.profiles.active=mysql  -> jdbc:mysql://localhost:3306
```

### Validation + Exception Handling

- DTOs: @NotBlank, @Email, @Min with @Valid in controller
- GlobalExceptionHandler: IllegalArgumentException -> 404, MethodArgumentNotValidException -> 400

---

## 5. Controller Layer

**UserController**: POST /api/users, GET /{id}, GET /email/{email}, GET ?role&active, PUT /{id}, PATCH /{id}/deactivate, POST /forgot-password, POST /reset-password

**CartController**: GET /user/{userId}, GET /user/{userId}/or-create, GET /{cartId}/items, POST /{cartId}/items, DELETE /{cartId}/items/{productId}, DELETE /{cartId}/clear, GET /{cartId}/total, GET /abandoned?days, GET /active-count

---

## 6. Interview Design Answers

- **OneToOne User-Cart?** Each user has exactly one cart (not many).
- **CascadeType.ALL?** Yes on Cart.items — cart lifecycle owns items. Risk: deleting cart deletes all items (desired). Without it, orphan items remain.
- **Updating totalAmount?** Recalculate from items after each add/remove, or via CartItemService.calculateCartTotal called after mutations. Keep in DB for fast display, recalc on write.
