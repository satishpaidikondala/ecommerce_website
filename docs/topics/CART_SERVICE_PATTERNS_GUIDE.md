# Cart Service Patterns Guide

Patterns used in `cart-service` service layer.

---

## 1. Get-or-Create Pattern

**Problem:** New customer has no cart yet. Amazon never says "Cart not found" — it shows an empty cart.

```java
@Override
@Transactional
public Cart getOrCreateCart(Long userId) {
    return cartRepository.findByUserId(userId)
            .orElseGet(() -> cartRepository.save(
                    Cart.builder()
                            .user(User.builder().id(userId).build())
                            .build()));
}
```

### orElseThrow vs orElseGet

| Method | Behavior | When |
|--------|----------|------|
| `.orElseThrow(() -> new ...)` | Fail if missing | Strict lookups (user profile) |
| `.orElseGet(() -> ...)` | Create fallback if missing | Carts, sessions, drafts |

### Why `User.builder().id(userId).build()` (only ID)?

- We only need the **foreign key** (`user_id`) for the INSERT
- Fetching the full user = extra wasted query
- JPA inserts just the ID column, never touches the users table

**Memory trick:** *"Find it, or FABRICATE it (and save!)"*

---

## 2. Add-or-Increase Pattern (Add Product to Cart)

**Problem:** Adding "iPhone" twice should NOT create duplicate rows — quantity increases instead.

```java
@Override
@Transactional
public CartItem addProductToCart(Long cartId, Long productId) {

    Optional<CartItem> existing =
            cartItemRepository.findByCartIdAndProductId(cartId, productId);

    if (existing.isPresent()) {
        // Already in cart -> increase quantity + recalculate subtotal
        CartItem item = existing.get();
        item.setQuantity(item.getQuantity() + 1);
        item.setSubtotal(item.getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())));
        return cartItemRepository.save(item);
    }

    // Not in cart -> fetch REAL product data and create item
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Product not found with id: " + productId));

    // Real-world business rules
    if (!product.isActive()) {
        throw new IllegalArgumentException("Product not available");
    }
    if (product.getUnitsInStock() < 1) {
        throw new IllegalArgumentException("Product out of stock");
    }

    CartItem newItem = CartItem.builder()
            .cart(Cart.builder().id(cartId).build())
            .product(product)
            .quantity(1)
            .price(product.getPrice())
            .subtotal(product.getPrice())
            .build();

    return cartItemRepository.save(newItem);
}
```

---

## 3. Return Type Cheat Sheet

| Situation | Return Type |
|-----------|-------------|
| Might not exist | `Optional<T>` / throw via `orElseThrow` |
| Multiple results | `List<T>` |
| Exists check only | `boolean` |
| After save() | Always `T` (never Optional!) |

---

## 4. Constructor Injection Rule

Every field must be assigned in the constructor:

```java
private final CartItemRepository cartItemRepository;
private final ProductRepository productRepository;

// BOTH injected - missing one = NullPointerException at runtime!
public CartItemServiceImpl(CartItemRepository cartItemRepository,
                           ProductRepository productRepository) {
    this.cartItemRepository = cartItemRepository;
    this.productRepository = productRepository;
}
```

---

## 5. Microservices Note: Cross-Service Data

Each service has its OWN database. cart-service cannot query product-service's tables.

```
cart-service (H2/MySQL: carts)     product-service (H2/MySQL: products)
        |                                    |
        |   REST call (future step)          |
        |----------------------------------->|
        |   GET /api/products/{id}           |
        |<-- { id, name, price } ------------|
```

- NOW (learning): temporary local `ProductRepository` in cart-service
- LATER: replace with RestClient/OpenFeign call to product-service
