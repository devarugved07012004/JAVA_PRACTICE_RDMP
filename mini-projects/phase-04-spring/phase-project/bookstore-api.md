# Phase project — BookStore API

**Source:** [phase-04-spring/phase-project.md](../../../phase-04-spring/phase-project.md) · **Integrates:** Boot + Security/JWT + JPA + Validation + Actuator + PostgreSQL

---

## Goal

Ship a production-shaped BookStore REST API: JWT auth with `USER`/`ADMIN`, paginated book catalog (admin writes), transactional orders with stock rules, consistent JSON errors, Actuator health, seed data, and a README demo script. Evolve v1 → v2 → v3; keep packages ready to split in Phase 5.

---

## Prerequisites

- Topics [11](../../../phase-04-spring/11-spring-core.md)–[14](../../../phase-04-spring/14-jpa-hibernate.md) mastery ≥90%
- Mini-projects: Boot CRUD, Secure Login, Catalog+Orders JPA (or equivalent skills)
- PostgreSQL local or Docker
- curl + JDK 17+

---

## Learn links

| Resource | Why |
|----------|-----|
| [start.spring.io](https://start.spring.io/) | Skeleton |
| [Spring Boot Reference](https://docs.spring.io/spring-boot/reference/) | Config, Actuator, Data |
| [Spring Security](https://docs.spring.io/spring-security/reference/) | Filter chain |
| [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/) | Repos, pagination |
| [Password Storage](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html) | BCrypt |
| [Baeldung JWT](https://www.baeldung.com/spring-security-oauth-jwt) | Adapt carefully to Boot 3 |

### start.spring.io settings

| Field | Value |
|-------|--------|
| Project | Maven (or Gradle) |
| Language | Java |
| Spring Boot | 3.x |
| Group | `com.learn` |
| Artifact | `bookstore` |
| Package name | `com.learn.bookstore` |
| Packaging | Jar |
| Java | 17+ |
| Dependencies | **Spring Web**, **Spring Security**, **Spring Data JPA**, **Validation**, **Actuator**, **PostgreSQL Driver** |
| Optional later | **Flyway Migration**, DevTools |

JWT library: add `jjwt` (api/impl/jackson) or Nimbus manually in the build file.

---

## Project layout

```text
com.learn.bookstore/
  BookStoreApplication.java
  config/           # SecurityConfig, JpaAuditing, OpenAPI optional
  security/         # JwtService, JwtAuthFilter, handlers, UserDetailsService
  user/             # User entity, repo, AuthController, AuthService
  book/             # Book entity, repo, BookService, BookController
  order/            # Order, OrderItem, OrderService, OrderController
  common/           # ApiError, GlobalExceptionHandler, constants
src/main/resources/
  application.yml
  application-dev.yml
  application-prod.yml
  db/migration/     # optional Flyway
README.md
.env.example        # JWT secret placeholders — no real secrets
NOTES-phase5-prep.md
```

---

## Build steps

### v1 — Skeleton, auth, catalog

- [ ] **1. Initializr + Postgres**
  1. Generate with settings above; package `com.learn.bookstore`.
  2. Create DB: `createdb bookstore` (or Docker Postgres 16, db/user/pass `bookstore`/`bookstore`/`bookstore`).
  3. `application.yml`: datasource URL `jdbc:postgresql://localhost:5432/bookstore`, `jpa.hibernate.ddl-auto: update`, `server.port: 8080`.
  4. Packages: `auth`/`user`, `book`, `order`, `config`, `security`, `common`.
  5. **Done when:** App starts, connects to Postgres, README has run command.

- [ ] **2. User entity + BCrypt**
  1. `@Entity User`: id, unique username (and/or email), passwordHash, role enum `USER`/`ADMIN`.
  2. `PasswordEncoder` bean (BCrypt); never store plaintext.
  3. **Done when:** Saving a user persists a BCrypt hash visible in `psql`.

- [ ] **3. Register + Login JWT**
  1. `POST /api/auth/register` → **201**; duplicate → **409**.
  2. `POST /api/auth/login` → **200** `{"accessToken","tokenType":"Bearer"}`; bad password → **401**.
  3. `JwtService` with secret/TTL from env (`BOOKSTORE_JWT_SECRET`, `BOOKSTORE_JWT_TTL_MINUTES`).
  4. **Done when:** Register/login curls match statuses; token decodes with expected claims.

- [ ] **4. SecurityFilterChain + JWT filter**
  1. Stateless; CSRF disabled for API; permit `/api/auth/**`, `/actuator/health`.
  2. Authenticate everything else; JWT filter sets `SecurityContext`.
  3. JSON **401** / **403** handlers.
  4. **Done when:** No token on `/api/books` → **401**; valid token → allowed per later rules.

- [ ] **5. Book entity + admin writes**
  1. Fields: id, title, author, isbn (unique), price, stock, optional description.
  2. `GET /api/books` paginated → **200** (authenticated).
  3. `GET /api/books/{id}` → **200** / **404**.
  4. `POST/PUT/(DELETE)` → `hasRole("ADMIN")` → **201**/**200**/**204**; USER → **403**.
  5. Validation on write DTOs → **400** field errors.
  6. **Done when:** Admin can create books; user can list/get only.

- [ ] **6. Seed data + `/api/auth/me`**
  1. Seed admin + user + 3–5 books on startup (`ApplicationRunner`, passwords BCrypt).
  2. `GET /api/auth/me` → **200** username/roles (no secrets).
  3. **Done when:** Demo works without manual SQL.

- [ ] **7. Actuator health**
  1. `management.endpoints.web.exposure.include: health` (add `info` only if intentional).
  2. `GET /actuator/health` → **200** UP (permitAll).
  3. **Done when:** Health documented in README.

### v2 — Orders, stock, authorization depth

- [ ] **8. Order model**
  1. `Order`: id, user, status enum (`NEW`, `PAID`, `CANCELLED`), createdAt, `@OneToMany` items.
  2. `OrderItem`: book, quantity, unitPrice snapshot.
  3. **Done when:** Schema/FKs correct; LAZY associations default.

- [ ] **9. Place order transactional**
  1. `POST /api/orders` body lines `[{bookId, quantity}]` — authenticated USER (or ADMIN acting as self).
  2. `@Transactional`: stock check → decrement → save order+items; insufficient → **409** or **400**, no partial write.
  3. Success → **201**.
  4. **Done when:** Stock decreases in DB; oversell rejected cleanly.

- [ ] **10. List orders by role**
  1. `GET /api/orders/mine` → **200** only caller’s orders.
  2. `GET /api/orders` admin → **200** all; USER → **403**.
  3. User cannot `GET` another user’s order by id → **403** or **404** (document policy).
  4. **Done when:** Ownership proven with two users.

- [ ] **11. Search + pagination polish**
  1. Books: `q` title/author search + `page`/`size`/`sort` → **200**.
  2. Orders mine paginated (Should).
  3. **Done when:** Page metadata stable; SQL logs sane (no accidental N+1 on demos).

- [ ] **12. Error model consistency**
  1. One JSON shape for 400/401/403/404/409.
  2. Re-run security + order failure curls.
  3. **Done when:** Clients can parse errors uniformly.

### v3 — Harden & Phase 5 prep

- [ ] **13. Profiles `dev` / `prod`**
  1. Dev: verbose SQL, local datasource defaults.
  2. Prod-like: WARN logging, secrets only via env, stricter exposure.
  3. **Done when:** README shows both activations.

- [ ] **14. Auditing (Should)**
  1. `@EnableJpaAuditing` on books/orders timestamps.
  2. **Done when:** Fields auto-populate.

- [ ] **15. Optional Flyway (Could)**
  1. Baseline migration; profile `ddl-auto: validate`.
  2. **Done when:** Fresh DB boots via Flyway **or** skip documented.

- [ ] **16. Cancel order (Could)**
  1. `POST /api/orders/{id}/cancel` transactional stock restore; only owner or admin.
  2. Wrong state → **409**.
  3. **Done when:** Demo cancel restores stock **or** Could deferred in README.

- [ ] **17. JAR + regression script**
  1. Package executable JAR; run with env secret.
  2. Paste full HTTP script into README (below).
  3. Optional: reuse topic-13 regression style asserts.
  4. **Done when:** JAR-only demo passes script.

- [ ] **18. Phase 5 prep notes**
  1. Write `NOTES-phase5-prep.md`: endpoint groups, Orders↔Catalog data needs, catalog-down failure, dockerize-first choice.
  2. **Done when:** Four questions answered in your words.

---

## Definition of Done

- [ ] Runs against **PostgreSQL** with documented commands
- [ ] Register/login JWT; passwords encoded
- [ ] Roles: admin catalog writes; users place/list own orders
- [ ] Book CRUD (admin writes) + paginated list
- [ ] Place-order transactional; respects stock
- [ ] Validation + security errors → consistent JSON
- [ ] Actuator health reachable; intentional exposure
- [ ] Seed data enables demo without manual SQL
- [ ] `dev`/`prod` (or equivalent) documented; secrets via env
- [ ] README: features, packages, env vars, sample HTTP script
- [ ] Layered packages; no business rules trapped in controllers
- [ ] Phase 5 prep notes list service boundaries and failure cases

---

## Demo script

```bash
BASE=http://localhost:8080

# 1. Register (or skip if using seed user)
curl -i -X POST $BASE/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"reader\",\"password\":\"password123\"}"
# → 201

# 2. Login user
curl -i -X POST $BASE/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"reader\",\"password\":\"password123\"}"
# → 200 + accessToken
# export USER_TOKEN=…

# 3. Login admin (seed)
curl -i -X POST $BASE/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"<seed-password>\"}"
# → 200
# export ADMIN_TOKEN=…

# 4. List books
curl -i "$BASE/api/books?page=0&size=10" -H "Authorization: Bearer $USER_TOKEN"
# → 200

# 5. Admin create book
curl -i -X POST $BASE/api/books \
  -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d "{\"title\":\"Dune\",\"author\":\"Herbert\",\"isbn\":\"9780441172719\",\"price\":12.99,\"stock\":5}"
# → 201

# 6. User forbidden create
curl -i -X POST $BASE/api/books \
  -H "Authorization: Bearer $USER_TOKEN" -H "Content-Type: application/json" \
  -d "{\"title\":\"X\",\"author\":\"Y\",\"isbn\":\"1\",\"price\":1,\"stock\":1}"
# → 403

# 7. Place order
curl -i -X POST $BASE/api/orders \
  -H "Authorization: Bearer $USER_TOKEN" -H "Content-Type: application/json" \
  -d "{\"items\":[{\"bookId\":1,\"quantity\":1}]}"
# → 201

# 8. List mine
curl -i $BASE/api/orders/mine -H "Authorization: Bearer $USER_TOKEN"
# → 200

# 9. Health
curl -i $BASE/actuator/health
# → 200 UP

# 10. Oversell
curl -i -X POST $BASE/api/orders \
  -H "Authorization: Bearer $USER_TOKEN" -H "Content-Type: application/json" \
  -d "{\"items\":[{\"bookId\":1,\"quantity\":99999}]}"
# → 400 or 409

# 11. Me
curl -i $BASE/api/auth/me -H "Authorization: Bearer $USER_TOKEN"
# → 200
```

| Call | Expected status |
|------|-----------------|
| POST register | **201** (or **409** duplicate) |
| POST login | **200** |
| POST login bad password | **401** |
| GET books no token | **401** |
| GET books + Bearer | **200** |
| POST books as USER | **403** |
| POST books as ADMIN | **201** |
| POST orders OK | **201** |
| POST orders oversell | **400**/**409** |
| GET orders/mine | **200** |
| GET orders as USER | **403** |
| GET actuator/health | **200** |

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Logic in controllers | Services own transactions and rules |
| Entities + lazy bags as JSON | Map DTOs inside the transaction |
| `permitAll` left from debugging | Re-lock; re-run demo/regression script |
| Stock updates without `@Transactional` | Single place-order method |
| Hardcoded JWT secret in git | Env var; `.env.example` placeholders |
| H2 forever | Postgres is DoD |
| No seed data | Ship admin + catalog |
| Skipping Actuator | Health is Must |
| Big-bang rewrite | Evolve v1 → v2 → v3 |

---

## After this

Phase 4 complete when DoD passes and the monolith’s module edges feel boringly clear. Start Phase 5 (Microservices) with `NOTES-phase5-prep.md` as the extraction map — catalog, orders, and auth are the natural cut lines. Do not rush into microservices until this API’s boundaries are obvious.
