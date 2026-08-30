# @Transactional Annotation Guide

## 1. What is @Transactional?

`@Transactional` ensures that **all database operations inside a method succeed together or fail together** (atomicity).

### Real-World Analogy: Bank Transfer

```
Transfer Rs.5000: Account A -> Account B

Step 1: Deduct Rs.5000 from A   OK
Step 2: Add Rs.5000 to B        FAILED (power cut!)

WITHOUT transaction : Rs.5000 vanished!
WITH @Transactional : Step 1 auto-undone. Money safe.
```

---

## 2. The Problem It Solves

Every repository call = a separate DB operation.

```java
// WITHOUT @Transactional - DANGEROUS
item.setQuantity(item.getQuantity() + 1);  // only Java memory
cartItemRepository.save(item);             // DB write #1
cartRepository.save(cart);                 // DB write #2 <- fails here!
// Result: quantity updated but cart total stale = corrupted data
```

```java
// WITH @Transactional - SAFE
@Transactional
public void addToCart(...) {
    cartItemRepository.save(item);   // \
    cartRepository.save(cart);       //  > ONE unit: all commit OR all rollback
}
```

---

## 3. How It Works

```
@Transactional method
+---------------------------+
| save(item)     ---+       |
| save(cart)         +--> ONE transaction
| delete(old)    ---+       |
+---------------------------+

All succeed  -> COMMIT  (changes saved permanently)
Any failure  -> ROLLBACK (everything undone automatically)
```

---

## 4. When To Use

| Method Type | @Transactional? | Why |
|-------------|-----------------|-----|
| Multiple writes (save/save/delete) | YES | Must commit together |
| Find-then-write (getOrCreate) | YES | Read + write in one flow |
| Single read (findById, findBy...) | NO | Nothing to roll back |
| Single write only | Optional | Already atomic by itself |

**Rule of thumb:** Multiple writes or find-then-write -> add `@Transactional`.

---

## 5. Where We Used It (Cart Service)

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

@Override
@Transactional
public CartItem addProductToCart(Long cartId, Long productId) {
    // update item quantity + recalculate subtotal in ONE unit
}
```

---

## 6. Import Statement

```java
import org.springframework.transaction.annotation.Transactional;
```

> Note: Use the Spring annotation, NOT `jakarta.transaction.Transactional`
> (Spring's version has extra features like readOnly and rollback rules).

---

## 7. Interview One-Liner

> "@Transactional ensures atomicity — multiple database operations execute
> as one unit; if any operation fails, all changes are rolled back,
> preventing data inconsistency."
