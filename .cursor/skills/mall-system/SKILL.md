---
name: mall-system
description: >-
  Guide agent to develop a Java Spring Boot multi-module mini-program mall backend.
  Enforces MVP scope, module boundaries, coding conventions, and implementation
  priority. Use when writing or modifying any Java code in this repository, adding
  new features, creating controllers/services/mappers, or asking about project
  architecture. Trigger terms: mall, order, cart, product, user, pay, WeChat login,
  Spring Boot module, DTO, VO, Entity.
---

# mall-system Agent Instructions

## Project Overview

Java Spring Boot multi-module backend for a WeChat mini-program mall.
Phase: **MVP only** — not a full e-commerce platform.

## MVP Scope

Build only:
- WeChat login
- User profile + address management
- Category / product / SKU browsing
- Cart
- Submit order
- Order list / detail / cancel
- Payment create / callback / result
- MQ for timeout close and async post-payment handling

### Out of Scope

Do NOT add any of the following:
Spring Cloud, coupons, seckill, distribution, multi-merchant, refund / after-sales, complex admin UI.

## Module Architecture

```
mall-system (parent, pom packaging)
├── mall-common    — shared Result / enums / exceptions
├── mall-infra     — Redis / JWT / MyBatis config / interceptors
├── mall-user      — user & address
├── mall-product   — category / product / SKU
├── mall-cart      — shopping cart
├── mall-order     — order lifecycle
├── mall-pay       — payment integration
├── mall-mq        — async messaging (timeout close, post-payment)
└── mall-app       — sole Spring Boot application entry (depends on all above)
```

`mall-app` is the **only** boot application. All other modules are plain libraries.

## Implementation Priority

Follow this order strictly:
1. mall-common
2. mall-infra
3. mall-user
4. mall-product
5. mall-cart
6. mall-order
7. mall-pay
8. mall-mq

## Coding Conventions

### Layering Rules

| Layer | Responsibility |
|-------|---------------|
| Controller | Request/response mapping only |
| Service | Business orchestration, transactions |
| Mapper | Persistence only |

- `@Transactional` belongs in Service layer only.
- Never put business logic in Controller or Mapper.

### Data Object Separation

| Type | Purpose | Package suffix |
|------|---------|---------------|
| Entity | DB table mapping | `.entity` |
| DTO | Request/input | `.dto` |
| VO | Response/output | `.vo` |

### Money & Stock Safety

- Use `BigDecimal` for all monetary values — never `double` or `float`.
- Never trust frontend-submitted amounts; always recalculate on the server.
- Use conditional SQL updates (optimistic locking) for stock changes:

```sql
UPDATE product_sku SET stock = stock - #{qty}
WHERE id = #{skuId} AND stock >= #{qty}
```

### Status Management

- Use Java enums for all statuses (order status, pay status, etc.).
- Map enum values to integer codes stored in DB.

### Payment Callback

- Payment callbacks **must** be idempotent.
- Check order status before processing; skip if already handled.

## Delivery Rules

When implementing a feature:

1. **Explain** which files will be touched before writing code.
2. **Output complete code** — never use placeholders like `// TODO` or `...`.
3. **Include all imports** — do not skip any import statement.
4. **No scope creep** — do not invent requirements beyond what was asked.
5. **Minimal changes** — keep modifications module-scoped; do not refactor unrelated code.

The SQL file under sql/init.sql is the source of truth for database schema.
All entities, mapper XML, and SQL must follow that schema exactly.
