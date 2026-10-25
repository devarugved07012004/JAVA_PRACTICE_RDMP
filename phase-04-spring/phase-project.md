# Phase 4 Project — BookStore API

**Duration:** ~2–3 weeks (14–21 days) after topics 11–14 · **Cadence:** 1 hour/day  
**Outcome:** A production-shaped Spring Boot API with JPA, JWT security, validation, Actuator, and PostgreSQL — books, users, and orders end-to-end

---

## 1. Why this topic is important

Isolated topics teach pieces; this phase project forces them to work as one system. The BookStore API is the first time you ship **Boot + JPA + JWT + validation + Actuator + Postgres** together: real auth, real persistence, real HTTP contracts, and an ops health endpoint.

Finishing it proves Phase 4 fluency and creates a codebase you will split or extend in Phase 5 (microservices, Docker, messaging). Same domain, clearer service boundaries later—catalog, orders, and auth are natural cut lines.

---

## 2. Prerequisites

- Topics [11](11-spring-core.md) → [14](14-jpa-hibernate.md) mastery checklists ≥90% each
- Comfortable with: DI mental model, Boot layering, JWT filter chain, JPA relationships & transactions
- PostgreSQL running locally (or Docker Postgres)
- Git optional but recommended for personal snapshots (learning only)
- Phase 3 REST discipline: status codes, JSON errors, idempotent thinking

**Suggested stack**

| Piece | Choice |
|-------|--------|
| Runtime | Spring Boot 3.x, JDK 17+ |
| API | `spring-boot-starter-web` |
| Persistence | Spring Data JPA + PostgreSQL driver |
| Security | Spring Security + JWT |
| Validation | `spring-boot-starter-validation` |
| Ops | `spring-boot-starter-actuator` |
| Build | Maven or Gradle |
| Config | `application.yml` + `dev` / `prod` profiles |

---

## 3. Concepts to learn (beginner → intermediate → advanced)

This project **applies** prior topics; new learning is integration and API design under constraints.

### Beginner (Project v1 — vertical slice)

| Focus | Apply |
|-------|--------|
| Project skeleton | Packages, Boot main, Postgres connection |
| Auth | Register/login JWT |
| Catalog | Book entity CRUD (admin write, user read) |
| Validation | DTO constraints + 400 errors |
| Health | Actuator `/actuator/health` |

### Intermediate (Project v2 — domain depth)

| Focus | Apply |
|-------|--------|
| Relationships | User, Book, Order, OrderItem |
| Transactions | Place order: stock check + line items |
| Authorization | Roles `USER` / `ADMIN`; ownership rules |
| Pagination | Book list + order history |
| Profiles | Dev (verbose SQL) vs prod-like (tight logging, env secrets) |

### Advanced (Project v3 — harden & Phase 5 prep)

| Focus | Apply |
|-------|--------|
| Auditing | Created/updated on orders/books |
| Error model | Stable problem JSON across modules |
| Observability lite | Health + info; readiness notes |
| Boundary notes | What becomes Catalog / Order / Auth services later |
| Optional Flyway | Schema as code for the main tables |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35–40 min) → tick boxes when done.

**Daily template (60 min):** 5 min plan · 10 min Learn · 35–40 min Practice steps · 5–10 min notes/checklist

### Week 1 — Skeleton, auth, catalog (Days 1–7)

- [ ] **Day 1 — Skeleton & Postgres**
  - [ ] **Learn:** [start.spring.io](https://start.spring.io/) · [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot/) · [Spring Boot SQL / JPA](https://docs.spring.io/spring-boot/reference/data/sql.html)
  - [ ] **Practice:** Initializr BookStore + Postgres DB
    1. [start.spring.io](https://start.spring.io/): Maven · Java · Boot 3.x · Java 17+ · **Group** `com.learn` · **Artifact** `bookstore` · **Package** `com.learn.bookstore`.
    2. Dependencies: **Web**, **Security**, **Data JPA**, **Validation**, **Actuator**, **PostgreSQL Driver**.
    3. Create DB: `createdb bookstore` (or Docker: Postgres 16, DB/user/pass `bookstore`/`bookstore`/`bookstore`).
    4. `application.yml`: `spring.datasource.url: jdbc:postgresql://localhost:5432/bookstore`, username/password, `spring.jpa.hibernate.ddl-auto: update`, `server.port: 8080`.
    5. Packages: `com.learn.bookstore.{auth,book,order,config,common}`; README skeleton with run command.
    6. **Done when:** App starts, connects to Postgres, and README has project name + how to run.

- [ ] **Day 2 — User + JWT auth**
  - [ ] **Learn:** [Password Storage](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html) · [Baeldung — JWT with Spring Security](https://www.baeldung.com/spring-security-oauth-jwt) · [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
  - [ ] **Practice:** `User` entity + register/login JWT
    1. `@Entity User`: `id`, unique `username`/`email`, `passwordHash`, `role` (`USER`/`ADMIN`).
    2. Reuse topic 13: `PasswordEncoder` (BCrypt), `JwtService`, `AuthController`.
    3. `POST /api/auth/register` → **201**; store only BCrypt hash.
    4. `POST /api/auth/login` → **200** `{"accessToken":"…","tokenType":"Bearer"}`.
    5. Bad password → **401**; never return hash in JSON.
    6. **Done when:** Register + login work against Postgres and passwords are BCrypt-only.

- [ ] **Day 3 — Security filter chain**
  - [ ] **Learn:** [SecurityFilterChain / Java config](https://docs.spring.io/spring-security/reference/servlet/configuration/java.html) · [Authorize HttpRequests](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html) · [Actuator endpoints](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Public auth/health; protect `/api/**`
    1. `SecurityFilterChain`: CSRF off; STATELESS; JWT filter registered.
    2. `permitAll`: `/api/auth/**`, `/actuator/health`; `anyRequest().authenticated()` for `/api/**`.
    3. YAML: `management.endpoints.web.exposure.include: health`.
    4. `curl -i http://localhost:8080/api/books` → **401** (no Bearer).
    5. `curl -i http://localhost:8080/actuator/health` → **200** UP (with Postgres).
    6. **Done when:** Auth routes public; API 401 without token; health 200.

- [ ] **Day 4 — Book catalog**
  - [ ] **Learn:** [Spring Data JPA Repositories](https://docs.spring.io/spring-data/jpa/reference/jpa/repositories.html) · [Authorize HttpRequests](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html) · [REST service guide](https://spring.io/guides/gs/rest-service/)
  - [ ] **Practice:** Book CRUD with unique ISBN + roles
    1. `@Entity Book`: title, author, unique `isbn`, `stock`, `price`.
    2. `GET /api/books`, `GET /api/books/{id}` — authenticated (or public if you choose; document).
    3. `POST`/`PUT` books — `hasRole("ADMIN")` → admin **201/200**; user → **403**.
    4. Duplicate ISBN → **409**.
    5. Curl: login as admin → create book → list shows it.
    6. **Done when:** Admin write + unique ISBN enforced; list/get work with Bearer.

- [ ] **Day 5 — Validation & errors**
  - [ ] **Learn:** [Validation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-validation.html) · [Exceptions](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-rest-exceptions.html) · [Baeldung — `@ControllerAdvice`](https://www.baeldung.com/exception-handling-for-rest-with-spring)
  - [ ] **Practice:** DTO constraints + unified error JSON
    1. `BookRequest`: `@NotBlank` title/author/isbn, `@Min(0)` stock/price.
    2. `@RestControllerAdvice` maps validation → **400**, not-found → **404**, conflict → **409**.
    3. Error body: `timestamp`, `status`, `error`, `message`, `fieldErrors` (optional).
    4. Curl invalid POST (blank title) → **400** with field errors.
    5. Curl missing id → **404** same envelope.
    6. **Done when:** 400/404/409 share one JSON shape.

- [ ] **Day 6 — Seed data**
  - [ ] **Learn:** [Database initialization](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.initialization) · [Baeldung — `data.sql`](https://www.baeldung.com/spring-boot-data-sql-and-schema-sql)
  - [ ] **Practice:** Seed books + admin/user; document logins
    1. `ApplicationRunner` (preferred) or SQL: create admin + user with BCrypt hashes.
    2. Seed ≥3 books with stock > 0.
    3. Idempotent seed (skip if admin exists).
    4. README draft: demo logins e.g. `admin`/`admin123`, `user`/`user123` (dev only).
    5. Restart on DB with data → no duplicate key errors.
    6. **Done when:** Fresh-ish start yields demo users + books; logins documented.

- [ ] **Day 7 — Auth + catalog script**
  - [ ] **Learn:** Re-skim [OWASP Authentication](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html) (threat notes)
  - [ ] **Practice:** Curl script register → login → catalog
    1. Create `scripts/smoke-auth-catalog.sh` or `http/bookstore.http`.
    2. Steps: register (or use seed) → login → save `TOKEN` → `GET /api/books` with Bearer → admin `POST /api/books`.
    3. Assert statuses: login **200**, list **200**, user create book **403**, admin create **201**.
    4. Fix any 401/403/validation gaps found.
    5. Paste script into README.
    6. **Done when:** Script passes end-to-end without manual guessing.

### Week 2 — Orders, rules, Actuator (Days 8–14)

- [ ] **Day 8 — Order mappings**
  - [ ] **Learn:** [Hibernate associations](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#associations) · [Baeldung — JPA relationships](https://www.baeldung.com/jpa-hibernate-associations)
  - [ ] **Practice:** `Order` + `OrderItem` + user FK
    1. `Order`: ManyToOne `User`, OneToMany `OrderItem`, status/total optional.
    2. `OrderItem`: ManyToOne `Order`, ManyToOne `Book`, `quantity`, `unitPrice`.
    3. Helpers: `order.addItem(item)` keep both sides in sync.
    4. Persist a sample order in a temp service method; verify FKs in Postgres.
    5. No place-order business rules yet — mappings only.
    6. **Done when:** Order + items save with user and book FKs correct.

- [ ] **Day 9 — Place-order transaction**
  - [ ] **Learn:** [Declarative transactions](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative.html) · [Baeldung — `@Transactional`](https://www.baeldung.com/transaction-configuration-with-jpa-spring)
  - [ ] **Practice:** `POST /api/orders` with stock decrement
    1. Request DTO: list of `{ bookId, quantity }`.
    2. `@Transactional` service: for each line check stock; decrement; create order+items.
    3. Success → **201** + order DTO; stock reduced in DB.
    4. Insufficient stock → **400/409**; no order row; stock unchanged.
    5. `curl -X POST http://localhost:8080/api/orders -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"items":[{"bookId":1,"quantity":1}]}'`.
    6. **Done when:** Happy path 201 and stock-failure rollback both proven.

- [ ] **Day 10 — List orders by role**
  - [ ] **Learn:** [Method Security](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html) · [Authorize HttpRequests](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html)
  - [ ] **Practice:** My orders vs admin all; no leakage
    1. `GET /api/orders/me` — current user only → **200**.
    2. `GET /api/orders` — `ADMIN` only → **200** all; `USER` → **403**.
    3. Place orders as alice and bob; alice `/me` never shows bob’s ids.
    4. Optional: `GET /api/orders/{id}` ownership check → **403/404** if not owner (non-admin).
    5. Document designed behavior (403 vs empty) in README.
    6. **Done when:** Cross-user order visibility is blocked as designed.

- [ ] **Day 11 — Pagination & search**
  - [ ] **Learn:** [Pagination and sorting](https://docs.spring.io/spring-data/commons/reference/repositories/core-concepts.html) · [Query methods](https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods.html) · [Baeldung — Pagination](https://www.baeldung.com/spring-data-jpa-pagination-sorting)
  - [ ] **Practice:** Paginated books + optional filter; DTOs only
    1. `GET /api/books?page=0&size=2&sort=title,asc` → Page DTO.
    2. Optional `?q=` title/author contains ignore-case (derived or Spec).
    3. Controllers return DTOs only (no entities).
    4. Curl `size=2` → 2 content items + correct `totalElements`.
    5. Seed enough books if needed.
    6. **Done when:** Pagination + filter verified; responses are DTOs.

- [ ] **Day 12 — Actuator lockdown**
  - [ ] **Learn:** [Actuator endpoints](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html) · [Health](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.health)
  - [ ] **Practice:** Health UP; tight exposure
    1. Confirm `management.endpoints.web.exposure.include: health` (add `info` only if needed).
    2. `curl -i http://localhost:8080/actuator/health` → **200** `"status":"UP"` with Postgres up.
    3. Optional: stop Postgres briefly → health DOWN/503 — note behavior.
    4. Ensure `/actuator/env` is **not** exposed (404 or disabled).
    5. Document exposure in README.
    6. **Done when:** Health reflects DB and exposure is minimal by design.

- [ ] **Day 13 — Profiles & secrets**
  - [ ] **Learn:** [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html) · [Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html)
  - [ ] **Practice:** Env-based JWT + datasource for prod
    1. `application-dev.yml`: verbose SQL, local datasource defaults.
    2. `application-prod.yml`: `JWT_SECRET` / `SPRING_DATASOURCE_URL` via `${…}`; quiet logging.
    3. Fail fast in prod if JWT secret missing/short.
    4. `.gitignore` `application-local.yml`; no real secrets in git.
    5. README env var table: `JWT_SECRET`, DB URL, username, password.
    6. **Done when:** Prod profile docs use env overrides and repo has no secrets.

- [ ] **Day 14 — E2E test matrix**
  - [ ] **Learn:** Skim your HTTP script + [OWASP Authorization](https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html)
  - [ ] **Practice:** Full matrix + bug-fix
    1. Matrix rows: auth, catalog admin/user, place order, stock fail, order list roles, health.
    2. Expected status per row; execute all; mark pass/fail.
    3. Fix every Must failure.
    4. Re-run smoke script green.
    5. Note remaining Should/Could gaps.
    6. **Done when:** Auth + catalog + orders + stock-failure matrix all pass.

### Week 3 — Definition of Done & microservices prep (Days 15–21)

- [ ] **Day 15 — Auditing & version**
  - [ ] **Learn:** [Spring Data JPA Auditing](https://docs.spring.io/spring-data/jpa/reference/auditing.html) · [Optimistic locking](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#locking-optimistic) · [Baeldung — Auditing](https://www.baeldung.com/spring-data-jpa-auditing)
  - [ ] **Practice:** Audit fields + optional `@Version` on Book
    1. `@EnableJpaAuditing`; `createdAt`/`updatedAt` on Book and/or Order.
    2. Create + update book; timestamps populate in DTO/DB.
    3. Optional: `@Version` on Book; conflict on concurrent stock update → **409**.
    4. If skipping version: note why in README.
    5. Keep place-order transactional with version if enabled.
    6. **Done when:** Audit fields work; version either proven or deliberately skipped.

- [ ] **Day 16 — README complete**
  - [ ] **Learn:** Revisit [Spring Boot Reference](https://docs.spring.io/spring-boot/reference/) (packaging/config bookmarks)
  - [ ] **Practice:** Architecture + flows + env + HTTP script
    1. README sections: overview, packages, roles (`USER`/`ADMIN`), run steps.
    2. Sample flows: register/login, admin catalog, place order, list my orders.
    3. Env vars + profiles table.
    4. Full HTTP/curl script block.
    5. Actuator health URL + exposure note.
    6. **Done when:** README alone can onboard a peer to the full demo.

- [ ] **Day 17 — Optional Flyway**
  - [ ] **Learn:** [Flyway docs](https://documentation.red-gate.com/fd) · [Spring Boot Flyway](https://docs.spring.io/spring-boot/reference/howto/data-initialization.html#howto.data-initialization.migration-tool.flyway)
  - [ ] **Practice:** Flyway baseline **or** documented skip
    1. **Do:** Add Flyway; `V1__bookstore_init.sql`; profile with `ddl-auto: validate`.
    2. Fresh DB starts; migrations apply; app healthy.
    3. **Or skip:** Write `NOTES-flyway-skip.md` with reason and when you would add it.
    4. Do not leave prod-like profile on `create-drop`.
    5. Update README either way.
    6. **Done when:** Validate+Flyway works **or** skip is explicit and justified.

- [ ] **Day 18 — Phase 5 prep notes**
  - [ ] **Learn:** Skim topics [11](11-spring-core.md)–[14](14-jpa-hibernate.md) package boundaries · [Spring Boot Reference](https://docs.spring.io/spring-boot/reference/)
  - [ ] **Practice:** Write `NOTES-phase5-prep.md`
    1. Propose service boundaries: Auth, Catalog, Orders.
    2. List shared-DB smells if split naively (FK across services).
    3. Sync-call failure cases: place-order needs stock — what breaks if Catalog is down.
    4. What stays in monolith for Phase 5 v1 vs extract first.
    5. Optional sequence diagram (text/mermaid) of place-order today.
    6. **Done when:** `NOTES-phase5-prep.md` answers §11-style boundary prompts.

- [ ] **Day 19 — Executable JAR**
  - [ ] **Learn:** [Packaging](https://docs.spring.io/spring-boot/reference/packaging.html) · [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html)
  - [ ] **Practice:** `java -jar` against local Postgres
    1. `./mvnw -DskipTests package` (or Gradle `bootJar`).
    2. Run: `java -jar target/bookstore-*.jar --spring.profiles.active=prod` with env vars set.
    3. Curl health + login + list books → expected statuses.
    4. Add JAR run command to README.
    5. Confirm no DevTools required at runtime.
    6. **Done when:** Packaged JAR serves API with prod-like profile locally.

- [ ] **Day 20 — Definition of Done pass**
  - [ ] **Learn:** Re-read §12 Definition of Done + [Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Full DoD checklist; fix Must gaps
    1. Walk every Must in §12 / project DoD.
    2. Fix gaps (auth, catalog, orders, validation, health, README).
    3. Re-run E2E matrix from Day 14.
    4. Tick Should items you completed; list deferred Coulds.
    5. Snapshot/tag the repo state.
    6. **Done when:** All Must DoD items pass.

- [ ] **Day 21 — Retrospective & sign-off**
  - [ ] **Learn:** [Spring Security](https://docs.spring.io/spring-security/reference/) · [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/) · [Hibernate docs](https://hibernate.org/orm/documentation/) (bookmark for Phase 5)
  - [ ] **Practice:** Split-first note + mastery sign-off
    1. Write 5–10 lines: what you would extract first for microservices and why.
    2. Sign off Phase 4 topic mastery checklists (11–14) + this project DoD.
    3. List top 3 bugs you fixed this project.
    4. Bookmark Security / Data JPA / Hibernate for Phase 5.
    5. Celebrate: BookStore API is the Phase 4 gate.
    6. **Done when:** Split-first note exists and checklists are honestly signed off.

*On a 2-week pace: merge Week 3 into Days 12–14 (README + DoD + short Phase 5 notes; skip Flyway if needed).*

---

## 5. Hands-on exercises

Use these as spikes before or during integration:

1. Prove JWT → `SecurityContext` → `@AuthenticationPrincipal` on a `/me` endpoint.
2. Transaction rollback: force failure after stock decrement; confirm book stock unchanged.
3. N+1 check when listing orders with items—fix fetch if logs explode.
4. 409 on duplicate ISBN or email.
5. Admin-only book delete vs user 403.
6. Page books with `size=2` and verify `totalElements`.
7. Actuator health returns UP with Postgres running.
8. Invalid JWT and expired JWT both rejected cleanly.

---

## 6. Mini-projects

The phase project **is** the deliverable, shipped as progressive versions.

**Detailed multi-step guide:** [bookstore-api.md](../mini-projects/phase-04-spring/phase-project/bookstore-api.md) (start.spring.io settings, package layout, v1–v3 build steps, curl + expected statuses, DoD, demo script).

### Feature specification (build all marked Must)

**Auth**

- [Must] Register user (email/username + password); passwords BCrypt-hashed
- [Must] Login returns JWT
- [Must] Roles: at least `USER` and `ADMIN`
- [Must] `/api/auth/me` (or equivalent) for current user
- [Should] Configurable JWT secret/TTL via env

**Catalog (Books)**

- [Must] Fields: id, title, author, isbn (unique), price, stock, optional description
- [Must] List (paginated) + get by id
- [Must] Create/update (and optionally delete) restricted to `ADMIN`
- [Should] Search by title/author
- [Could] Soft delete or active flag

**Orders**

- [Must] Authenticated user places order: one or more book lines with quantities
- [Must] Transactional stock decrement; reject if insufficient stock
- [Must] User lists own orders; cannot see others’ orders
- [Must] Admin can list all orders
- [Should] Order status enum (`NEW`, `PAID`, `CANCELLED`—even if only NEW used)
- [Could] Cancel order restores stock (transactional)

**Platform quality**

- [Must] Validation on write DTOs
- [Must] Consistent JSON error body (400/401/403/404/409 as applicable)
- [Must] PostgreSQL as the datastore
- [Must] Actuator health enabled with conscious exposure
- [Must] Layered packages: controller / service / repository / security / dto
- [Must] README with run steps, env vars, and sample HTTP script
- [Should] `dev` / `prod` profiles
- [Should] Auditing timestamps
- [Could] Flyway migrations

### Suggested package layout

```text
com.yourname.bookstore/
  BookStoreApplication.java
  config/          # Security, JPA auditing, OpenAPI optional
  security/        # JWT filter, JwtService, UserDetailsService
  user/            # entity, repo, service, auth controller
  book/            # entity, repo, service, controller
  order/           # entity, repo, service, controller
  common/          # errors, dto mappers, constants
```

### Sample HTTP flow (manual test script)

```text
1. POST /api/auth/register   → 201
2. POST /api/auth/login      → 200 + token
3. GET  /api/books?page=0&size=10  (Bearer) → 200
4. POST /api/books           (ADMIN Bearer) → 201
5. POST /api/orders          (USER Bearer)  → 201
6. GET  /api/orders/mine     → 200
7. GET  /actuator/health     → 200 UP
8. Repeat 5 with stock=0     → 400/409 business error
```

---

## 7. Industry use cases

- **Online bookstore / digital catalog:** Inventory + checkout rules
- **Any authenticated CRUD SaaS:** Users, roles, owned resources
- **Order management backends:** Transactional consistency across lines and stock
- **Platform baseline:** Health checks for deploy pipelines and container orchestrators
- **Microservice precursor:** Monolith module boundaries that later become services

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| All logic in controllers | Services own transactions and rules |
| Returning entities with lazy bags as JSON | Map to DTOs inside the transaction |
| `permitAll` leftover from debugging | Re-lock routes; re-run security script |
| Stock updates without `@Transactional` | One service method for place-order |
| Hardcoded JWT secret in git | Env var; sample `.env.example` without real values |
| H2 “for now” forever | Postgres is part of the Definition of Done |
| Admin and user sharing one password in README only | Seed both roles; document both logins |
| No seed data | Always ship a demo catalog + admin |
| Skipping Actuator because “not features” | Health is a Must for this phase |
| Big-bang rewrite | Evolve v1 auth/catalog → v2 orders → v3 harden |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| Topic files 11–14 in this folder | Pattern refresh while integrating |
| [Spring Boot Reference](https://docs.spring.io/spring-boot/reference/) | Config, Actuator, Data |
| [Spring Security Reference](https://docs.spring.io/spring-security/reference/) | Filter chain, JWT wiring context |
| [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/) | Repositories, pagination |
| [Hibernate User Guide](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html) | Fetching, relationships |
| [spring.io Guides](https://spring.io/guides) | REST, JPA, Security starters |
| [Baeldung — Spring Boot / Security / JPA (free)](https://www.baeldung.com/spring-boot) | Integration how-tos |
| [PostgreSQL docs](https://www.postgresql.org/docs/current/) | Constraints, indexes |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Spring Boot: https://docs.spring.io/spring-boot/reference/
- Actuator endpoints: https://docs.spring.io/spring-boot/reference/actuator/endpoints.html
- Externalized configuration: https://docs.spring.io/spring-boot/reference/features/external-config.html
- Spring Security: https://docs.spring.io/spring-security/reference/
- authorizeHttpRequests: https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
- Spring Data JPA: https://docs.spring.io/spring-data/jpa/reference/
- Accessing Data with JPA guide: https://spring.io/guides/gs/accessing-data-jpa/
- Securing a Web Application guide: https://spring.io/guides/gs/securing-web/
- Building a RESTful Web Service: https://spring.io/guides/gs/rest-service/
- Hibernate docs: https://hibernate.org/orm/documentation/
- PostgreSQL: https://www.postgresql.org/docs/current/
- start.spring.io: https://start.spring.io/
- JWT RFC 7519: https://datatracker.ietf.org/doc/html/rfc7519
- Baeldung Spring index: https://www.baeldung.com/spring-boot

---

## 11. Practice roadmap

### Evolution path

```text
v1 Auth + Books + Health
    → v2 Orders + transactions + role rules + pagination
        → v3 Auditing, profiles, JAR, Phase 5 boundary notes
```

### Week plan (summary)

| Week | Deliverable |
|------|-------------|
| 1 | JWT auth + book catalog on Postgres + validation + seed |
| 2 | Orders with stock rules, pagination, Actuator, test matrix |
| 3 | Harden, README, DoD, microservices prep notes |

### How this prepares for Phase 5 (Microservices)

| BookStore monolith piece | Phase 5 direction |
|--------------------------|-------------------|
| `user` + security package | Auth service or shared identity concerns |
| `book` package | Catalog service with its own DB |
| `order` package | Order service; calls catalog for stock (sync) then later events |
| Single Postgres schema | Per-service databases; no cross-service joins |
| JWT filter in-process | Gateway validation + service trust boundaries |
| Actuator on one app | Health per service + compose/k8s probes |
| Modular packages | Extract modules → separate Boot apps |

**Phase 5 gate hint:** Keep packages cohesive and services free of cross-entity shortcuts (e.g., avoid order controller talking straight to book repositories outside an application service). Clear module edges make extraction realistic.

### Definition of architecture notes to write (Day 18)

Capture in `NOTES-phase5-prep.md` (personal learning notes):

1. Which endpoints belong together?
2. What data does Orders need from Catalog without sharing tables?
3. What fails if Catalog is down during place-order?
4. What would you dockerize first?

---

## 12. Mastery checklist

### Definition of Done (project)

- [ ] App runs against **PostgreSQL** with documented commands
- [ ] Register/login JWT works; passwords are encoded
- [ ] Roles enforced: admin catalog writes; users place/list own orders
- [ ] Book CRUD (admin writes) + paginated list works
- [ ] Place-order is transactional and respects stock
- [ ] Validation and security errors return consistent JSON
- [ ] Actuator health is reachable with intentional exposure settings
- [ ] Seed data enables a demo without manual SQL
- [ ] `dev`/`prod` (or equivalent) profiles documented; secrets via env
- [ ] README covers features, packages, env vars, and sample HTTP script
- [ ] Layered packages; no business rules trapped in controllers
- [ ] Personal Phase 5 prep notes list service boundaries and failure cases

### Personal mastery

- [ ] Can explain AuthN vs AuthZ using BookStore routes
- [ ] Can point to the transaction boundary for place-order
- [ ] Can debug a 401 vs 403 vs 400 from logs + config in under 15 minutes
- [ ] Can add a new authenticated endpoint following existing patterns
- [ ] Topics 11–14 checklists remain complete
- [ ] Ready to start Phase 5 — Microservices (when available)

---

## Quick reference — success criteria

You are done with Phase 4 when the BookStore API meets the Definition of Done above and the stack (Boot + JPA + JWT + validation + Actuator + Postgres) feels like one coherent system—not four tutorials glued together. Do not rush into microservices until this monolith’s module boundaries are boringly clear.
