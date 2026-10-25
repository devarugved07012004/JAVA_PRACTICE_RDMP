# Mini-project A — Catalog + Orders (JPA)

**Topic:** [14 — JPA & Hibernate](../../../phase-04-spring/14-jpa-hibernate.md)

---

## Goal

Persist a catalog and orders with JPA: Product/Book, Category or Author, Order, OrderLine. Correct relationships, transactional place-order (stock check + lines), pagination, at least one JPQL `@Query`, SQL logging to verify fetch behavior. Prefer DTO responses.

---

## Prerequisites

- Topic 14 Weeks 1–2 (entities, repos, transactions, associations, fetch)
- PostgreSQL local (or Docker); H2 only for early spikes — DoD wants Postgres
- Boot Web + Validation comfort from topic 12

---

## Learn links

| Resource | Why |
|----------|-----|
| [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/) | Official starter |
| [Spring Data JPA repos](https://docs.spring.io/spring-data/jpa/reference/jpa/repositories.html) | CRUD + queries |
| [Pagination](https://docs.spring.io/spring-data/commons/reference/repositories/core-concepts.html) | Pageable |
| [Hibernate fetching](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching) | LAZY / N+1 |
| [Declarative transactions](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative.html) | `@Transactional` |

### start.spring.io settings

| Field | Value |
|-------|--------|
| Group | `com.learn` |
| Artifact | `catalog-orders` |
| Package name | `com.learn.catalogorders` |
| Spring Boot | 3.x · Java 17+ |
| Dependencies | **Spring Web**, **Spring Data JPA**, **PostgreSQL Driver**, **Validation**, **H2** (optional test profile only) |

---

## Project layout

```text
com.learn.catalogorders/
  CatalogOrdersApplication.java
  product/
    Product.java                # stock, price, @ManyToOne Category
    Category.java
    ProductRepository.java
    ProductController.java
    dto/…
  order/
    OrderEntity.java            # avoid Order keyword clash
    OrderLine.java
    OrderRepository.java
    OrderService.java           # @Transactional placeOrder
    OrderController.java
  common/
    GlobalExceptionHandler.java
src/main/resources/
  application.yml               # Postgres + show-sql
  data.sql / ApplicationRunner  # seed
README.md
```

---

## Build steps

- [ ] **1. Initializr + Postgres**
  1. Generate with settings above.
  2. DB: `createdb catalog_orders` (user/pass as you like).
  3. YAML: JDBC URL, `ddl-auto: update`, `show-sql: true`, `format_sql: true`.
  4. **Done when:** App starts connected to Postgres.

- [ ] **2. Category + Product entities**
  1. `Category` id + name; `Product` title/sku unique, price, stock, `@ManyToOne` category (LAZY).
  2. Repositories; seed 1 category + 3 products via `ApplicationRunner`.
  3. **Done when:** `psql` shows rows after startup.

- [ ] **3. Product CRUD + pagination**
  1. `GET /api/products?page=0&size=2` → **200** page JSON (`content`, `totalElements`).
  2. `GET /api/products/{id}` → **200** / **404**.
  3. `POST /api/products` → **201** (admin later; open for now or leave open for learning).
  4. DTOs only in responses.
  5. **Done when:** Page size respected; SQL log shows LIMIT/OFFSET.

- [ ] **4. JPQL `@Query`**
  1. e.g. `@Query("select p from Product p where lower(p.title) like lower(concat('%', :q, '%'))")`.
  2. `GET /api/products/search?q=…` → **200**.
  3. **Done when:** Search uses `@Query`, not in-memory filter.

- [ ] **5. Order + OrderLine mapping**
  1. `OrderEntity`: id, createdAt, `@OneToMany` lines (`CascadeType.ALL`, orphanRemoval as intentional).
  2. `OrderLine`: `@ManyToOne` product, quantity, unitPrice snapshot.
  3. **Done when:** Schema shows FK columns; owning side clear in notes.

- [ ] **6. Place-order transactional service**
  1. Request: list of `{productId, quantity}`.
  2. `@Transactional`: for each line lock/load product, if stock < qty → throw business exception → **409** or **400**.
  3. Decrement stock; create order + lines; save.
  4. Prove rollback: force failure mid-method → stock unchanged.
  5. `POST /api/orders` → **201** on success.
  6. **Done when:** Success persists order; insufficient stock fails with no partial writes.

- [ ] **7. List orders + fetch check**
  1. `GET /api/orders` → **200** DTO list (or page).
  2. Watch SQL: fix N+1 with join fetch / `@EntityGraph` if listing lines.
  3. **Done when:** Notes show before/after SQL count for listing.

- [ ] **8. Auditing (Should)**
  1. `@EnableJpaAuditing`; `createdAt`/`updatedAt` on Order.
  2. **Done when:** Timestamps populated automatically.

- [ ] **9. Optimistic locking (Could)**
  1. `@Version` on Product; concurrent update demo optional.
  2. **Done when:** Version column exists (demo conflict optional).

- [ ] **10. README**
  1. DB setup, seed, curl create product / page / place order / fail stock.
  2. **Done when:** Cold demo works from README.

---

## Definition of Done

- [ ] Entities: Product/Book, Category or Author, Order, OrderLine
- [ ] Relationships mapped; CRUD via repositories
- [ ] Place-order `@Transactional` with stock check
- [ ] Pagination on catalog listing
- [ ] ≥1 JPQL `@Query`
- [ ] SQL logging used to verify fetch behavior
- [ ] Should: auditing on Order; DTO responses only
- [ ] Could: `@Version` on Product stock
- [ ] Postgres + README + seed data

---

## Demo script

```bash
# seed already loaded on startup

curl -i "http://localhost:8080/api/products?page=0&size=2"
# → 200 paginated

curl -i "http://localhost:8080/api/products/search?q=dune"
# → 200 filtered

curl -i -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d "{\"items\":[{\"productId\":1,\"quantity\":1}]}"
# → 201

curl -i -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d "{\"items\":[{\"productId\":1,\"quantity\":99999}]}"
# → 400 or 409 ; stock unchanged in DB

curl -i http://localhost:8080/api/orders
# → 200
```

Verify with:

```bash
psql -d catalog_orders -c "SELECT id, stock FROM product;"
psql -d catalog_orders -c "SELECT * FROM order_line;"
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| `EAGER` everywhere | LAZY + fetch per use case |
| Place-order without `@Transactional` | One service method |
| Exposing entity graphs as JSON | DTOs inside transaction |
| Ignoring N+1 | Read SQL logs; join fetch |
| `ddl-auto=create` on shared DB | `update` for lab; migrations later |

---

## After this

[B — Blog/Notes with Tags](B-blog-notes-tags.md) for many-to-many practice; [C — Migration starter](C-migration-starter.md) before production habits. Then [BookStore phase project](../phase-project/bookstore-api.md).
