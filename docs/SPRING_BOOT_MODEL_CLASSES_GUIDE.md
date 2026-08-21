# Spring Boot Model/Entity Classes - Simple Learning Guide

> **What is this?** Entity classes are Java classes that represent database tables.
> **Why?** You write Java code, Hibernate creates and manages database tables for you.

---

## Table of Contents

1. [Theoretical Concepts](#1-theoretical-concepts)
2. [What is an Entity?](#2-what-is-an-entity)
3. [How ORM Maps Java to Database](#3-how-orm-maps-java-to-database)
4. [Class-Level Annotations](#4-class-level-annotations)
5. [Field-Level Annotations](#5-field-level-annotations)
6. [Lombok - Write Less Code](#6-lombok---write-less-code)
7. [JPA Relationships](#7-jpa-relationships)
8. [Cascade - The Domino Effect](#8-cascade---the-domino-effect)
9. [Lifecycle Callbacks](#9-lifecycle-callbacks)
10. [Enums in Database](#10-enums-in-database)
11. [Validation](#11-validation)
12. [Embeddable - Reusable Fields](#12-embeddable---reusable-fields)
13. [Common Mistakes](#13-common-mistakes)
14. [Interview Cheat Sheet](#14-interview-cheat-sheet)

---

## 1. Theoretical Concepts

### What is a Database Table?

**A table is like a spreadsheet that stores data in rows and columns.**

```
Table: users
+----+-----------+-------------------+------------+
| id | firstName | email             | phone      |
+----+-----------+-------------------+------------+
| 1  | John      | john@email.com    | 1234567890 |
| 2  | Jane      | jane@email.com    | 0987654321 |
| 3  | Bob       | bob@email.com     | 5555555555 |
+----+-----------+-------------------+------------+

Each row = One record (one user)
Each column = One field (name, email, phone)
```

---

### What is a Primary Key?

**A unique identifier for each row. No two rows can have the same key.**

```
+----+-----------+
| id | name      |    id = Primary Key
+----+-----------+
| 1  | John      |    (unique, no duplicate)
| 2  | Jane      |    (unique, no duplicate)
| 3  | Bob       |    (unique, no duplicate)
+----+-----------+

You can always find a specific row using its primary key.
```

---

### What is a Foreign Key?

**A link from one table to another table's primary key.**

```
users                    orders
+----+-----------+      +----+---------+-----------+
| id | name      |      | id | user_id | orderDate |
+----+-----------+      +----+---------+-----------+
| 1  | John      |◄─────| 10 |    1    | 2026-01-15|
| 2  | Jane      |◄─────| 11 |    1    | 2026-02-20|
| 3  | Bob       |      | 12 |    2    | 2026-03-10|
+----+-----------+      +----+---------+-----------+

user_id in orders = Foreign Key
It points to id in users
John (id=1) has two orders (10 and 11)
```

---

### What is an Entity?

**An Entity is a Java class that represents a database table.**

```
Java Class                    Database Table
┌──────────────┐             ┌──────────────────┐
│ class User   │    ORM      │ CREATE TABLE     │
│ {            │   maps to   │ users (          │
│   Long id;   │  ←───────   │   id INT PRIMARY │
│   String name│             │   name VARCHAR   │
│ }            │             │ )                │
└──────────────┘             └──────────────────┘

You write Java class.
Hibernate creates table.
```

---

### What is ORM (Object-Relational Mapping)?

**ORM automatically converts between Java objects and database tables.**

```
Java World                    Database World
┌──────────────┐             ┌──────────────────┐
│ User user    │             │ users table      │
│ = new User() │  ←───────   │ INSERT INTO...   │
│ user.setName │             │ name = 'John'    │
└──────────────┘             └──────────────────┘

ORM translates:
Java: user.setName("John")
  ↓
SQL: INSERT INTO users (name) VALUES ('John')
```

---

### Why Do We Need Entities?

**Without Entities (Old Way):**
```java
// Write SQL manually everywhere
Statement stmt = connection.createStatement();
stmt.executeUpdate("INSERT INTO users (name, email) VALUES ('John', 'john@email.com')");
// tedious and error-prone!
```

**With Entities (New Way):**
```java
// Just use Java objects
User user = new User();
user.setName("John");
user.setEmail("john@email.com");
userRepository.save(user);
// Clean and simple!
```

---

## 2. What is an Entity?

**An Entity is a Java class that maps to a database table.**

```
┌─────────────────────────────────────────────────┐
│  @Entity = "This class is a table"              │
│  @Table(name = "users") = "Table name is users" │
│  @Id = "This field is primary key"              │
│  private String name; = "Column 'name'"         │
└─────────────────────────────────────────────────┘
```

---

### What Happens When You Run the App?

```
1. Hibernate reads your @Entity class
2. Hibernate creates table in database (if not exists)
3. Hibernate manages all database operations
4. You just use Java objects - no SQL needed!
```

---

## 3. How ORM Maps Java to Database

### Basic Mapping

```
Java Class:                    Database Table:
┌─────────────────┐           ┌──────────────────────┐
│ @Entity         │    →      │ CREATE TABLE users (  │
│ public class    │           │   id BIGINT PRIMARY,  │
│   User {        │           │   name VARCHAR(255),  │
│                 │           │   email VARCHAR(255)  │
│   @Id           │           │ )                     │
│   Long id;      │    →      │                       │
│   String name;  │    →      │                       │
│   String email; │    →      │                       │
│ }               │           │                       │
└─────────────────┘           └──────────────────────┘

Java Type → Database Type:
Long      → BIGINT
String    → VARCHAR
Integer   → INT
Boolean   → BOOLEAN
BigDecimal → DECIMAL
LocalDateTime → TIMESTAMP
```

---

### Annotation Mapping

| Java Annotation | Database Equivalent |
|-----------------|---------------------|
| `@Entity` | This class is a table |
| `@Table(name = "users")` | Table name is "users" |
| `@Id` | Primary key |
| `@GeneratedValue` | Auto-increment |
| `@Column` | Column properties |
| `@Enumerated` | Enum storage |

---

## 4. Class-Level Annotations

### @Entity

**Purpose: Marks a class as a database table**

```java
@Entity
public class User { }
```

**Without @Entity:**
- Hibernate ignores the class
- No table created
- No database operations

---

### @Table

**Purpose: Specifies table name**

```java
@Entity
@Table(name = "users")
public class User { }
```

**Why use it?**
```
Without @Table: Table name = "user" (reserved word in SQL!)
With @Table:    Table name = "users" (safe!)
```

**Rules:**
- Use plural form: User → users, Order → orders
- Avoid SQL reserved words
- Use snake_case: user_profile, order_items

---

### @Data (Lombok)

**Purpose: Auto-generates getters, setters, toString, equals, hashCode**

```java
@Data
public class User {
    private String name;
}

// Lombok generates:
// public String getName() { return name; }
// public void setName(String name) { this.name = name; }
// public String toString() { ... }
// public boolean equals(Object o) { ... }
// public int hashCode() { ... }
```

**Without @Data:** You write 50+ lines of boilerplate code
**With @Data:** You write 1 line

---

### @NoArgsConstructor (Lombok)

**Purpose: Creates empty constructor (required by JPA)**

```java
@NoArgsConstructor
public class User { }
// Generates: public User() { }
```

**Why JPA needs it:**
```
Database returns row → Hibernate creates object → Needs empty constructor!

Hibernate does:
User user = new User();  // ← Needs this!
user.setName(resultSet.getString("name"));
```

---

### @Builder (Lombok)

**Purpose: Creates objects using Builder pattern**

```java
@Builder
public class User {
    private String name;
    private String email;
}

// Usage:
User user = User.builder()
    .name("John")
    .email("john@email.com")
    .build();
```

**Why Builder?**
```
Without Builder: new User("John", "Doe", "john@email.com", "1234567890", "Mumbai")
                 (Which is name? Which is city? Confusing!)

With Builder:    User.builder().name("John").email("john@email.com").build()
                 (Clear and readable!)
```

---

## 5. Field-Level Annotations

### @Id

**Purpose: Marks primary key**

```java
@Id
private Long id;
```

**Every entity needs @Id:**
- Database requires primary key
- Used for unique identification
- Required for JPA operations

---

### @GeneratedValue

**Purpose: Auto-generate primary key**

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

**Strategies:**

| Strategy | How It Works | Best For |
|----------|--------------|----------|
| IDENTITY | Database auto-increment | MySQL, SQL Server |
| AUTO | Hibernate decides | Any database |
| SEQUENCE | Database sequence | PostgreSQL, Oracle |

---

### @Column

**Purpose: Configure column properties**

```java
@Column(nullable = false)
private String name;

@Column(unique = true)
private String email;

@Column(length = 500)
private String description;
```

**Properties:**

| Property | What It Does | Example |
|----------|--------------|---------|
| nullable = false | Cannot be NULL | Required field |
| unique = true | No duplicates | Email, phone |
| length = 100 | Max characters | Limit text |
| name = "col_name" | Custom column name | Different from field |

---

### @Enumerated

**Purpose: Store enum in database**

```java
@Enumerated(EnumType.STRING)
private UserRole role;
```

**STRING vs ORDINAL:**

| Type | Stores | Example | Safe? |
|------|--------|---------|-------|
| STRING | Name | "ADMIN", "CUSTOMER" | ✅ Yes |
| ORDINAL | Position | 0, 1, 2 | ❌ No |

**Always use STRING - data stays safe even if enum order changes.**

---

## 6. Lombok - Write Less Code

### The Problem

**Without Lombok, every entity needs 50+ lines:**

```java
public class User {
    private String name;
    private String email;

    public User() { }
    public User(String name, String email) { ... }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String toString() { ... }
    public boolean equals(Object o) { ... }
    public int hashCode() { ... }
}
// 40+ lines just for 2 fields!
```

---

### The Solution

**With Lombok, just add annotations:**

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String name;
    private String email;
}
// 6 lines! Lombok generates everything else.
```

---

### Lombok Annotations Summary

| Annotation | What It Generates |
|------------|-------------------|
| @Data | Getters + Setters + toString + equals + hashCode |
| @NoArgsConstructor | Empty constructor |
| @AllArgsConstructor | Constructor with all fields |
| @Builder | Builder pattern methods |
| @Getter | Only getters |
| @Setter | Only setters |

---

## 7. JPA Relationships

### Why Relationships?

**Real-world data is connected:**

```
One User → Many Orders
One Order → Many Items
One Product → One Category
```

**Database uses foreign keys to connect tables.**

---

### One-to-Many / Many-to-One

**Example: One User has Many Orders**

```
users                    orders
+----+-----------+      +----+---------+
| id | name      |      | id | user_id |
+----+-----------+      +----+---------+
| 1  | John      |◄─────| 10 |    1    |
| 2  | Jane      |◄─────| 11 |    1    |
|     (one user) |      | 12 |    2    |
+----+-----------+      +----+---------+
                        (many orders)
```

**Java Code:**

```java
// User side (Parent)
@OneToMany(mappedBy = "user")
private List<Order> orders;

// Order side (Child)
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
```

---

### One-to-One

**Example: One User has One Cart**

```
users                   carts
+----+-----------+     +----+---------+
| id | name      |     | id | user_id |
+----+-----------+     +----+---------+
| 1  | John      |◄───►| 1  |    1    |
| 2  | Jane      |◄───►| 2  |    2    |
+----+-----------+     +----+---------+
```

**Java Code:**

```java
// User side
@OneToOne
@JoinColumn(name = "cart_id")
private Cart cart;

// Cart side
@OneToOne(mappedBy = "cart")
private User user;
```

---

### Many-to-Many

**Example: Students and Courses**

```
students       enrollments        courses
+----+         +------------+    +----+
| id |         | student_id |    | id |
+----+         | course_id  |    +----+
| 1  |◄────────| 1    | 1   |───►| 1  |
| 1  |◄────────| 1    | 2   |───►| 2  |
| 2  |◄────────| 2    | 1   |───►| 1  |
+----+         +------------+    +----+

Student 1 takes courses 1 and 2
Course 1 has students 1 and 2
```

**Java Code:**

```java
// Student side
@ManyToMany
@JoinTable(
    name = "enrollments",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private Set<Course> courses;

// Course side
@ManyToMany(mappedBy = "courses")
private Set<Student> students;
```

---

### mappedBy Explained

**`mappedBy` means: "The other class owns this relationship"**

```java
@OneToMany(mappedBy = "user")
private List<Order> orders;
```

**Translation:** "The `user` field in `Order` class owns the foreign key"

```
OneToMany(mappedBy = "FIELD_NAME_IN_CHILD")
```

---

### Relationship Summary

| Relationship | Example | Annotation | Foreign Key |
|-------------|---------|------------|-------------|
| One-to-One | User ↔ Cart | @OneToOne | Either table |
| One-to-Many | User → Orders | @OneToMany (parent) | Child table |
| Many-to-One | Order → User | @ManyToOne (child) | Child table |
| Many-to-Many | Student ↔ Course | @ManyToMany | Join table |

---

## 8. Cascade - The Domino Effect

### What is Cascade?

**Operations on parent automatically apply to children.**

```
Delete a folder → All files inside are deleted too
That's cascade!
```

---

### Cascade Types

```java
@OneToMany(cascade = CascadeType.ALL)
private List<OrderItem> items;
```

| Type | What Happens |
|------|--------------|
| PERSIST | Save children when parent saved |
| MERGE | Update children when parent updated |
| REMOVE | Delete children when parent deleted |
| ALL | All of the above |

---

### Orphan Removal

```java
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
private List<OrderItem> items;
```

**What is an orphan?**
- A child without a parent
- Example: Remove item from order's list

```java
order.getItems().remove(0);

// Without orphanRemoval: Item still in database (orphan!)
// With orphanRemoval: Item DELETED from database (no orphan!)
```

---

## 9. Lifecycle Callbacks

### What are Lifecycle Callbacks?

**Methods that automatically run before/after database operations.**

---

### @PrePersist

**Runs BEFORE saving to database**

```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();    // Auto-set creation time
    active = true;                      // Default value
    role = UserRole.CUSTOMER;          // Default value
}
```

**Use cases:**
- Set creation timestamp
- Set default values
- Generate unique codes

---

### @PreUpdate

**Runs BEFORE updating in database**

```java
@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();    // Auto-set update time
}
```

---

### Lifecycle Summary

| Annotation | When It Runs | Common Use |
|------------|--------------|------------|
| @PrePersist | Before INSERT | Set createdAt, defaults |
| @PreUpdate | Before UPDATE | Set updatedAt |
| @PostPersist | After INSERT | Send email |
| @PreRemove | Before DELETE | Cleanup |

---

## 10. Enums in Database

### What is an Enum?

**A fixed set of values that a field can have.**

```java
public enum OrderStatus {
    PENDING,      // Order placed
    CONFIRMED,    // Order confirmed
    SHIPPED,      // Sent to customer
    DELIVERED,    // Received
    CANCELLED     // Cancelled
}
```

---

### Using Enums in Entities

```java
@Enumerated(EnumType.STRING)
private OrderStatus status;
```

**Why @Enumerated?**
- Without it: JPA doesn't know how to store enum
- With STRING: Stores as "PENDING", "SHIPPED"
- With ORDINAL: Stores as 0, 1, 2 (dangerous!)

---

## 11. Validation

### What is Validation?

**Check data before saving to database.**

```java
@NotBlank(message = "Name is required")
private String name;

@Email(message = "Invalid email")
private String email;

@Min(0)
private Integer age;
```

---

### Common Validations

| Annotation | What It Checks |
|------------|----------------|
| @NotNull | Not null |
| @NotBlank | Not null and not empty |
| @Email | Valid email format |
| @Size(min, max) | Length constraints |
| @Min(value) | Minimum value |
| @Max(value) | Maximum value |
| @Positive | Greater than 0 |

---

## 12. Embeddable - Reusable Fields

### What is Embeddable?

**Reuse common fields across multiple entities.**

```java
@Embeddable
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
}
```

**Usage:**

```java
@Entity
public class Order {
    @Embedded
    private Address shippingAddress;

    @Embedded
    private Address billingAddress;
}
```

---

## 13. Common Mistakes

### Mistake 1: Missing @Enumerated

```java
// Wrong - stores as integer
private OrderStatus status;

// Correct - stores as string
@Enumerated(EnumType.STRING)
private OrderStatus status;
```

---

### Mistake 2: Wrong Return Type

```java
// Wrong - Optional but multiple can have same status
Optional<Order> findByStatus(OrderStatus status);

// Correct - Returns list
List<Order> findByStatus(OrderStatus status);
```

---

### Mistake 3: Wrong Field Name

```java
// Wrong - Entity has "name", not "userName"
List<User> findByUserName(String name);

// Correct - Match entity field
List<User> findByName(String name);
```

---

### Mistake 4: Missing Lombok Constructors

```java
// Wrong - JPA can't create entity
@Data
public class User { }

// Correct - Add constructors
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User { }
```

---

## 14. Interview Cheat Sheet

### Key Concepts

| Concept | Definition |
|---------|------------|
| **Entity** | Java class that maps to database table |
| **Primary Key** | Unique identifier for each row |
| **Foreign Key** | Links one table to another |
| **ORM** | Maps Java objects to database tables |
| **@Entity** | Marks class as database table |
| **@Table** | Specifies table name |
| **@Id** | Marks primary key |
| **@GeneratedValue** | Auto-generate ID |
| **@Column** | Column properties |
| **@Enumerated** | Store enum in database |
| **@OneToMany** | One parent, many children |
| **@ManyToOne** | Many children, one parent |
| **@ManyToMany** | Many-to-many relationship |
| **Cascade** | Apply operations to children |
| **@PrePersist** | Before INSERT |
| **@PreUpdate** | Before UPDATE |

---

### Interview Questions & Answers

**Q: What is the difference between @OneToOne and @OneToMany?**
> @OneToOne: One parent has exactly one child (User has one Cart)
> @OneToMany: One parent has many children (User has many Orders)

**Q: Why use @Enumerated(EnumType.STRING) instead of ORDINAL?**
> STRING stores the name ("PENDING"), ORDINAL stores the position (0). If enum order changes, ORDINAL data gets corrupted.

**Q: What is cascade?**
> Operations on parent automatically apply to children. Delete Order → Delete all OrderItems.

**Q: Why do we need @NoArgsConstructor?**
> JPA/Hibernate needs empty constructor to create entities from database results.

**Q: What is the difference between @Column and @JoinColumn?**
> @Column: Configures a regular field
> @JoinColumn: Specifies foreign key column in relationships

---

### Annotation Quick Reference

| Annotation | Purpose |
|------------|---------|
| @Entity | Class is a table |
| @Table | Table name |
| @Id | Primary key |
| @GeneratedValue | Auto-increment ID |
| @Column | Column properties |
| @Enumerated | Enum storage |
| @ManyToOne | Many-to-one relationship |
| @OneToMany | One-to-many relationship |
| @OneToOne | One-to-one relationship |
| @ManyToMany | Many-to-many relationship |
| @JoinColumn | Foreign key column |
| @MappedBy | Relationship owner |
| @Cascade | Apply to children |
| @PrePersist | Before INSERT |
| @PreUpdate | Before UPDATE |
| @Embedded | Reusable fields |
| @NotBlank | Not empty validation |
| @Email | Email validation |

---

**Created for Ecommerce Microservices Project**
**Last Updated: August 2026**
