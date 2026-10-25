# Phase 5 Project — BookStore Microservices

**Duration:** ~3–4 weeks (21–28 days) after topics 15–19 foundations · **Cadence:** 1 hour/day  
**Outcome:** Split BookStore into **user**, **catalog**, and **order** services; run with **Docker Compose**; publish/consume **Kafka order events**

---

## 1. Why this topic is important

Topics 15–19 teach pieces of a distributed stack. This project forces them into one runnable system: separate deployables, separate data ownership, an edge entry (gateway or documented Compose ports), containerized dependencies, and an asynchronous **OrderPlaced** path on Kafka.

Finishing it proves Phase 5 fluency and creates the codebase you will reason about in Phase 6 (system design, resilience patterns, optional AI features) and the final capstone. Same bookstore domain as Phase 4—clearer service boundaries, real failure modes, and an event-driven checkout slice.

---

## 2. Prerequisites

- Phase 4 BookStore monolith (or equivalent) completed—clear modules to extract
- Topic checklists ≥80–90% for [15](15-microservices.md)–[19](19-kafka.md) (you may finish Kafka Streams lightly if the event path works)
- Docker Compose working; Kafka via Compose
- JDK 17+; Maven/Gradle multi-module **or** separate repos/folders
- PostgreSQL: prefer **one database per service** (three schemas/DBs)

**Suggested stack**

| Piece | Choice |
|-------|--------|
| Services | `user-service`, `catalog-service`, `order-service` |
| Edge | Spring Cloud Gateway (recommended) or nginx in Compose |
| Discovery | Eureka **or** Compose DNS only (document choice) |
| Sync calls | OpenFeign order → catalog (stock check) where still needed |
| Async | Kafka topic `bookstore.orders` / `OrderPlaced` |
| Data | Postgres ×3 (or one server, three databases) |
| Containers | Dockerfile per service + `compose.yaml` |
| Observability | Actuator health; correlation id; optional Zipkin |

---

## 3. Concepts to learn (beginner → intermediate → advanced)

This project **applies** prior topics; new learning is integration under constraints.

### Beginner (Project v1 — split & run)

| Focus | Apply |
|-------|--------|
| Extract services | User/auth, catalog/books, orders |
| Per-service DB | No cross-service joins |
| Compose | Build/run all + Postgres + Kafka |
| Health | Each service `/actuator/health` |

### Intermediate (Project v2 — sync + edge)

| Focus | Apply |
|-------|--------|
| Gateway routes | `/api/users`, `/api/books`, `/api/orders` |
| Feign | Order validates/reserves stock via catalog |
| JWT | Issue in user-service; validate at gateway and/or services |
| Failure | Timeout/circuit breaker when catalog is down |

### Advanced (Project v3 — events + Phase 6 prep)

| Focus | Apply |
|-------|--------|
| Kafka `OrderPlaced` | Order service produces after successful place |
| Consumers | Catalog adjusts projection **or** notification logger; optional email stub |
| Idempotency | Event id / order id unique processing |
| Docs | Architecture diagram, failure matrix, Compose runbook |
| Phase 6 hooks | Scalability, consistency, observability notes for system design |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35–45 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 40 min Practice steps · 10 min notes / mark boxes

### Week 1 — Split & databases (Days 1–7)

- [ ] **Day 1 — Architecture sketch**
  - [ ] **Learn:** [Spring Cloud overview](https://docs.spring.io/spring-cloud/reference/) · [Microservices architecture (Microsoft)](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/)
  - [ ] **Practice:** Sketch endpoints, ownership, sync vs async
    1. Create `phase5-project/docs/architecture.md` with three services: user, catalog, order.
    2. List endpoints per service (CRUD + place-order + auth).
    3. Assign DB ownership (`user_db`, `catalog_db`, `order_db`) — no shared tables.
    4. Draw sync lines (order→catalog) and future async (order→Kafka).
    5. Note ports you’ll use locally before Compose.
    6. **Done when:** Architecture doc has endpoints, DBs, and sync/async lines.

- [ ] **Day 2 — Extract catalog**
  - [ ] **Learn:** [Spring Boot reference](https://docs.spring.io/spring-boot/reference/) · [PostgreSQL docs](https://www.postgresql.org/docs/current/)
  - [ ] **Practice:** Extract `catalog-service` with its own Postgres
    1. Create `catalog-service` Boot app; connect to Postgres database `catalog_db`.
    2. Move/implement books CRUD (entity, repo, controller).
    3. Prove with curl that list/create/get work against `catalog_db` only.
    4. Confirm no `orders`/`users` tables in this DB (`\dt`).
    5. Add Actuator health.
    6. **Done when:** Catalog CRUD runs on its own Postgres DB.

- [ ] **Day 3 — Extract user/auth**
  - [ ] **Learn:** [Spring Security](https://docs.spring.io/spring-security/reference/)
  - [ ] **Practice:** Extract `user-service` with JWT register/login
    1. Create `user-service` + Postgres `user_db`.
    2. Implement register/login with BCrypt + JWT issuance.
    3. Seed admin and normal user (Flyway/sql/data.sql).
    4. Curl register + login; decode/inspect JWT claims (sub/roles) in notes.
    5. Protect a sample endpoint; prove 401 without token.
    6. **Done when:** Login returns JWT and seeded users exist in `user_db`.

- [ ] **Day 4 — Extract order skeleton**
  - [ ] **Learn:** [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/)
  - [ ] **Practice:** Extract `order-service` without book joins
    1. Create `order-service` + Postgres `order_db`.
    2. Model Order/OrderLine storing `bookId` as reference id (no JPA join to Book).
    3. Create schema; verify `\dt` has no `books` table.
    4. Stub `POST /api/orders` that persists without calling catalog yet.
    5. Add health endpoint.
    6. **Done when:** Order entities persist in `order_db` with no book joins.

- [ ] **Day 5 — Catalog Feign client**
  - [ ] **Learn:** [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/)
  - [ ] **Practice:** Feign/WebClient for get book + stock
    1. Add OpenFeign (or WebClient) to order-service.
    2. Define client methods: get book by id, decrement/check stock.
    3. Point URL/discovery at running catalog-service.
    4. Unit/manual test: fetch a seeded book from order-service.
    5. Log request/response for debugging.
    6. **Done when:** Order service successfully reads book/stock from catalog.

- [ ] **Day 6 — Sync place-order**
  - [ ] **Learn:** [OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/)
  - [ ] **Practice:** Happy-path place-order + reject insufficient stock
    1. Implement transactional place-order: validate stock via catalog → save order.
    2. On insufficient stock, return 409/400 with clear JSON (no order row).
    3. Curl happy path; verify order row + stock change.
    4. Curl failure path; verify no order created.
    5. Document both curls in a scratch script.
    6. **Done when:** Happy path and insufficient-stock rejection both work.

- [ ] **Day 7 — Multi-terminal runbook**
  - [ ] **Learn:** [Actuator endpoints](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Manual multi-terminal run and start-order notes
    1. Start three DBs (or one Postgres with three DBs) and three services in separate terminals.
    2. Fix port conflicts; record final ports in `docs/runbook.md`.
    3. Curl health for each service.
    4. Run register→login→list→place-order manually once.
    5. Write start order: DBs → user → catalog → order.
    6. **Done when:** Runbook boots the stack and the manual path succeeds.

### Week 2 — Gateway, Compose, resilience (Days 8–14)

- [ ] **Day 8 — API Gateway**
  - [ ] **Learn:** [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Route all demos through Gateway only
    1. Add Spring Cloud Gateway; configure routes to user, catalog, order.
    2. Prefer `lb://` if Eureka is in play; otherwise static URLs for now.
    3. Curl register/login/list/place-order **only** via Gateway port.
    4. Remove “direct service port” from demo scripts.
    5. Document route table in README.
    6. **Done when:** Full demo path works exclusively through Gateway.

- [ ] **Day 9 — Dockerfiles**
  - [ ] **Learn:** [Dockerfile reference](https://docs.docker.com/reference/dockerfile/) · [Multi-stage builds](https://docs.docker.com/build/building/multi-stage/) · [Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker/)
  - [ ] **Practice:** Multi-stage images for each service
    1. Add multi-stage `Dockerfile` to user, catalog, order, gateway (and Eureka if used).
    2. Build each: `docker build -t user-service:dev ./user-service` (etc.).
    3. Run one image briefly with `-p` to verify it starts.
    4. Add `.dockerignore` per module.
    5. Note image sizes in notes.
    6. **Done when:** All core services build to local tagged images.

- [ ] **Day 10 — Compose stack**
  - [ ] **Learn:** [Docker Compose](https://docs.docker.com/compose/) · [Compose file](https://docs.docker.com/compose/compose-file/)
  - [ ] **Practice:** Compose services + databases + network
    1. Write `compose.yaml` with app services + three DB services **or** one Postgres with three databases/init scripts.
    2. Put all on one user-defined network.
    3. Map Gateway (and optionally Eureka) ports to host.
    4. `docker compose config` to validate YAML.
    5. First `docker compose up --build` attempt; note failures for Day 11–12.
    6. **Done when:** Compose file defines services + DBs on one network.

- [ ] **Day 11 — Compose DNS datasources**
  - [ ] **Learn:** [Docker networking](https://docs.docker.com/engine/network/) · [Externalized config](https://docs.spring.io/spring-boot/reference/features/external-config.html)
  - [ ] **Practice:** JDBC URLs use Compose DNS hostnames
    1. Set `SPRING_DATASOURCE_URL` to hosts like `catalog-db`, `user-db`, `order-db` (or one `postgres` with db names).
    2. Pass credentials via Compose `environment` / `.env` (not baked into images).
    3. Recreate app containers; check logs for successful pool init.
    4. Intentionally break one hostname; read error; fix.
    5. Document env vars in `.env.example`.
    6. **Done when:** All three apps connect via Compose DNS (no `localhost` JDBC inside containers).

- [ ] **Day 12 — Healthchecks & up**
  - [ ] **Learn:** [Compose depends_on](https://docs.docker.com/compose/compose-file/05-services/#depends_on) · [Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Healthy `compose up --build` demo
    1. Add Postgres `healthcheck` (`pg_isready`) and `depends_on: condition: service_healthy`.
    2. Optionally healthcheck apps on `/actuator/health`.
    3. `docker compose down` then `docker compose up --build`.
    4. Wait until Gateway health is UP; curl through Gateway.
    5. Paste working commands into README.
    6. **Done when:** Clean `compose up --build` yields a working Gateway demo.

- [ ] **Day 13 — Resilience chaos**
  - [ ] **Learn:** [Resilience4j CircuitBreaker](https://resilience4j.readme.io/docs/circuitbreaker) · [Getting started](https://resilience4j.readme.io/docs/getting-started)
  - [ ] **Practice:** Stop catalog container; observe breaker
    1. Confirm Resilience4j on catalog Feign in order-service.
    2. `docker compose stop catalog-service` (name may vary).
    3. Place order via Gateway; capture fallback/503 JSON and latency.
    4. `docker compose start catalog-service`; retry until success.
    5. Record chaos results in `docs/failure-matrix.md`.
    6. **Done when:** Catalog-down produces clear failure (not hang) and recovery works.

- [ ] **Day 14 — E2E gateway script**
  - [ ] **Learn:** Recap [Gateway](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Script register → login → list → place order
    1. Write `scripts/e2e.sh` or `.ps1` with the four steps through Gateway.
    2. Capture JWT from login into a variable for later calls.
    3. Assert HTTP statuses (200/201) in the script or manually.
    4. Run script against Compose stack twice.
    5. Link script from README.
    6. **Done when:** Script completes the full Gateway path successfully.

### Week 3 — Kafka order events (Days 15–21)

- [ ] **Day 15 — Kafka in Compose**
  - [ ] **Learn:** [Kafka Quickstart](https://kafka.apache.org/quickstart) · [Kafka docs](https://kafka.apache.org/documentation/) · [Compose](https://docs.docker.com/compose/)
  - [ ] **Practice:** Add Kafka and create `bookstore.orders`
    1. Add Kafka (KRaft) service to `compose.yaml`; expose `9092`.
    2. `docker compose up -d kafka` and wait until healthy/ready.
    3. Create topic: `kafka-topics.sh --bootstrap-server localhost:9092 --create --topic bookstore.orders --partitions 3 --replication-factor 1`.
    4. Describe topic; confirm partitions ≥ 3.
    5. Document bootstrap servers for apps (`kafka:9092` in-network).
    6. **Done when:** `bookstore.orders` exists with ≥3 partitions in Compose Kafka.

- [ ] **Day 16 — Event contract**
  - [ ] **Learn:** [Kafka intro](https://kafka.apache.org/intro)
  - [ ] **Practice:** Define `OrderPlacedEvent` JSON contract
    1. Create shared DTO/record: `eventId`, `orderId`, `userId`, `lines`, `total`, `occurredAt`.
    2. Write example JSON in `docs/events.md`.
    3. Decide keying strategy (e.g. `orderId` or `userId` as Kafka key).
    4. Version note (`eventType=OrderPlaced`, optional `schemaVersion`).
    5. Align field names across producer/consumer modules.
    6. **Done when:** `docs/events.md` + Java type match the contract.

- [ ] **Day 17 — Produce after commit**
  - [ ] **Learn:** [Spring Kafka](https://docs.spring.io/spring-kafka/reference/) · [Boot Kafka](https://docs.spring.io/spring-boot/reference/messaging/kafka.html)
  - [ ] **Practice:** Publish `OrderPlacedEvent` after successful place-order
    1. Add `spring-kafka` to order-service; set bootstrap servers for Compose.
    2. After DB commit of place-order, `KafkaTemplate.send("bookstore.orders", key, event)`.
    3. Prefer publish-after-commit (or document outbox TODO).
    4. Console-consume `bookstore.orders` while placing an order.
    5. Confirm event JSON matches contract.
    6. **Done when:** Successful place-order produces a visible Kafka message.

- [ ] **Day 18 — Consumer side effect**
  - [ ] **Learn:** [Spring Kafka listeners](https://docs.spring.io/spring-kafka/reference/)
  - [ ] **Practice:** Consume with a visible side effect
    1. Add `@KafkaListener` in catalog (audit/projection) **or** a notification stub service.
    2. On message: log + write audit row / in-app notification / counter.
    3. Place an order; confirm side effect appears.
    4. Set a dedicated `group-id` (e.g. `catalog-order-audit`).
    5. Document consumer responsibility in `docs/events.md`.
    6. **Done when:** Place-order yields a consumer-visible side effect.

- [ ] **Day 19 — Idempotent consumer**
  - [ ] **Learn:** [Kafka consumer docs](https://kafka.apache.org/documentation/#consumerapi)
  - [ ] **Practice:** Deduplicate by `eventId`
    1. Store processed `eventId` (DB unique constraint preferred).
    2. Skip duplicates in the listener.
    3. Republish/re-send the same `eventId` (or restart before commit carefully).
    4. Confirm side effect applied once.
    5. Note at-least-once + idempotent handler in failure matrix.
    6. **Done when:** Duplicate event does not double-apply the side effect.

- [ ] **Day 20 — Correlation across hops**
  - [ ] **Learn:** [Micrometer Tracing](https://docs.micrometer.io/tracing/reference/) · [Gateway filters](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Correlation id through Gateway → services → Kafka
    1. Ensure Gateway sets `X-Request-Id` (or trace id).
    2. Log the id in order-service on place-order.
    3. If practical, copy id into Kafka headers on publish; log on consume.
    4. Grep one request id across Gateway, order, consumer logs.
    5. Document header name in README.
    6. **Done when:** One id correlates HTTP hops (and Kafka if implemented).

- [ ] **Day 21 — Order event demo**
  - [ ] **Learn:** Recap [Spring Kafka](https://docs.spring.io/spring-kafka/reference/) · [Kafka](https://kafka.apache.org/documentation/)
  - [ ] **Practice:** Full place-order → Kafka → consumer demo
    1. `compose up` full stack including Kafka.
    2. Run e2e script to place order.
    3. Show message on `bookstore.orders` (console consumer) and consumer side effect.
    4. Record commands/expected outputs in README demo section.
    5. Fix any race (consumer not ready) with restart/order notes.
    6. **Done when:** Demo shows HTTP → Kafka message → consumer effect.

### Week 4 — Definition of Done, Phase 6 & capstone prep (Days 22–28)

- [ ] **Day 22 — README runbook**
  - [ ] **Learn:** [Compose overview](https://docs.docker.com/compose/) · [Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Diagram + Compose runbook + HTTP script
    1. Mermaid or drawn architecture: Gateway, 3 services, 3 DBs, Kafka.
    2. README: `docker compose up --build`, ports, health checks.
    3. Link `scripts/e2e` sample HTTP script.
    4. Document topic `bookstore.orders`.
    5. Peer-test by following README only (simulate).
    6. **Done when:** README has diagram, compose runbook, and sample script.

- [ ] **Day 23 — Failure matrix**
  - [ ] **Learn:** [Resilience4j](https://resilience4j.readme.io/) · [Kafka intro](https://kafka.apache.org/intro)
  - [ ] **Practice:** Document catalog down, Kafka down, partial consume
    1. Fill matrix: catalog down, Kafka down, consumer lag/partial fail.
    2. Re-run catalog-stop drill; paste actual response.
    3. Stop Kafka; note place-order behavior (fail vs succeed without event — document truth).
    4. Add mitigations (breaker, outbox, alerts).
    5. Keep `docs/failure-matrix.md` updated.
    6. **Done when:** Matrix covers catalog/Kafka/partial consume with observed behavior.

- [ ] **Day 24 — Observability polish**
  - [ ] **Learn:** [Micrometer Tracing](https://docs.micrometer.io/tracing/reference/) · [Zipkin Quickstart](https://zipkin.io/pages/quickstart.html)
  - [ ] **Practice:** Optional Zipkin or structured logs polish
    1. Add Zipkin to Compose **or** improve JSON/structured logs with request id.
    2. Capture one multi-service trace or correlated log set.
    3. Link observability section in README.
    4. Don’t block DoD if Zipkin is incomplete—logs must be correlatable.
    5. Note Capstone follow-ups.
    6. **Done when:** You can correlate one place-order across services via trace or request id.

- [ ] **Day 25 — Definition of Done**
  - [ ] **Learn:** §12 checklist in this file
  - [ ] **Practice:** DoD pass and fix gaps
    1. Walk §12 checklist item by item against the running Compose stack.
    2. File bugs/gaps as a punch list.
    3. Fix must-have gaps (broken route, missing topic, no idempotency).
    4. Re-run e2e script.
    5. Mark DoD status honestly in notes.
    6. **Done when:** Must-have DoD items pass on Compose path.

- [ ] **Day 26 — Phase 6 prep notes**
  - [ ] **Learn:** §11 Phase 6 prep questions in this file
  - [ ] **Practice:** Write design questions from *your* system
    1. Create `NOTES-phase6-prep.md`.
    2. Write 8–12 system-design questions grounded in this bookstore (scale, cache, CAP, queues).
    3. Sketch short answers you would give in an interview.
    4. List diagrams you’ll redraw in Phase 6.
    5. Link the file from README.
    6. **Done when:** `NOTES-phase6-prep.md` exists with concrete questions from your system.

- [ ] **Day 27 — Capstone backlog**
  - [ ] **Learn:** [Capstone overview](../capstone/enterprise-platform.md) (skim §1–§3)
  - [ ] **Practice:** Idea list for Capstone reuse/hardening
    1. Skim Capstone §1–§3; map your services to Auth/Catalog/Enrollment/Notification/AI.
    2. Backlog ideas: harden, split further, search, payments stub, AI later.
    3. Mark which Phase 5 pieces are reusable as-is.
    4. Keep list short (≤10 items); no new builds today.
    5. Save as `docs/capstone-backlog.md`.
    6. **Done when:** Capstone backlog file exists with mapped reuse notes.

- [ ] **Day 28 — Sign-off**
  - [ ] **Learn:** Recap topics [15](15-microservices.md)–[19](19-kafka.md) checklists
  - [ ] **Practice:** Retrospective, checklist sign-off, snapshot tag
    1. Retrospective: keep / stop / start (5 bullets total).
    2. Sign off Phase 5 checklists you completed.
    3. Tag or zip snapshot labeled `phase-5-done` (git tag if repo exists).
    4. Confirm Compose demo still runs.
    5. Note open debts carried to Phase 6/Capstone.
    6. **Done when:** Sign-off notes + `phase-5-done` snapshot exist.

*On a 3-week pace: merge Week 4 into Days 19–21 (README + DoD + short Phase 6 notes; Zipkin optional).*

---

## 5. Hands-on exercises

Use these as spikes before or during integration:

1. Prove catalog DB has no `orders` table and order DB has no `books` table.
2. Place order with catalog stopped → circuit breaker / clear error (not hang).
3. Gateway path rewrite correctness for all three services.
4. Kafka: consume `OrderPlaced` with console consumer while app runs.
5. Publish duplicate event; consumer does not double-apply side effects.
6. Scale `catalog-service` to 2 Compose replicas; hit via gateway/load balancing if discovery enabled.
7. `compose down` vs `down -v`—document data loss risk.
8. JWT expired/invalid at gateway or service—consistent 401.
9. Order for another user’s id—403/ownership rules.
10. Lag drill: pause consumer; place orders; resume; catch up.

---

## 6. Mini-projects / feature specification

> **Build guide:** [BookStore Microservices](../mini-projects/phase-05-distributed/phase-project/bookstore-microservices.md) · [Index](../mini-projects/README.md)

The phase project **is** the deliverable. Build all **Must** items.

### Service responsibilities

| Service | Owns | Must expose |
|---------|------|-------------|
| `user-service` | Users, credentials, roles | Register, login (JWT), `/me` |
| `catalog-service` | Books, stock source of truth | List/get books; admin write; stock reserve/decrement API for orders |
| `order-service` | Orders, line items | Place order; list mine; admin list all |
| `api-gateway` | Edge routing | Routes to the three APIs |
| Kafka | Events | `OrderPlaced` (and optional follow-ons) |

### Auth

- [Must] Register + login JWT from user-service
- [Must] Roles `USER` / `ADMIN`
- [Must] Orders require authenticated user; catalog writes admin-only
- [Should] Gateway validates JWT; services trust gateway **or** also validate
- [Could] Refresh tokens (out of scope unless easy)

### Catalog

- [Must] Book fields: id, title, author, isbn, price, stock, optional description
- [Must] Paginated list + get by id
- [Must] Stock change API used by order-service (clear contract)
- [Should] Search by title/author

### Orders

- [Must] Place order with line items; validate stock via catalog sync call
- [Must] Persist order in order DB only
- [Must] Publish `OrderPlaced` to Kafka after success
- [Must] User lists own orders only
- [Should] Status enum (`NEW`, …)
- [Could] Cancel + compensating event

### Platform

- [Must] Docker Compose brings up stack (services + DBs + Kafka)
- [Must] Documented ports, env vars, start commands
- [Must] Actuator health per service
- [Must] No shared business tables across services
- [Should] Resilience4j on catalog calls
- [Should] Correlation / request id
- [Could] Eureka + Config Server from topic 15 retained in Compose
- [Could] Zipkin

### Sample HTTP flow

```text
1. POST /api/users/register     → 201
2. POST /api/users/login        → 200 + JWT
3. GET  /api/books              → 200
4. POST /api/orders             → 201
5. GET  /api/orders/mine        → 200
6. Observe Kafka OrderPlaced    → consumer log/side effect
7. GET  /actuator/health        → UP (per service or gateway)
8. Stop catalog; POST order     → controlled failure (not infinite wait)
```

### Suggested Compose shape

```text
services:
  gateway
  user-service
  catalog-service
  order-service
  postgres-user
  postgres-catalog
  postgres-order
  kafka
  (optional) eureka, zipkin, kafka-ui
```

### Event contract (example fields)

```text
OrderPlacedEvent
  eventId       (UUID, idempotency)
  orderId
  userId
  lines[] { bookId, quantity, unitPrice }
  totalAmount
  occurredAt
```

---

## 7. Industry use cases

- **Marketplace backends:** Catalog, identity, and orders as separate deployables
- **Event-driven checkout:** Downstream fulfillment, email, analytics subscribe to OrderPlaced
- **Containerized local/prod parity:** Compose locally → cloud/K8s later with same images
- **Team ownership:** Different squads can own catalog vs orders
- **Gradual extraction:** Modular monolith → services without rewriting the domain

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Shared database “for convenience” | Separate DBs; accept sync APIs/events |
| Calling other service’s DB | Never; only public APIs/events |
| Publishing Kafka event before DB commit | Commit order first; then publish (note outbox for later) |
| No idempotency on consumers | Store processed `eventId` |
| Giant synchronous chain | Prefer events for non-critical side effects |
| Undocumented Compose order | Healthchecks + README |
| JWT secret diverges per service | Shared verification config via env |
| Rebuilding monolith inside “order-service” | Keep catalog data out of order schema |
| Skipping failure drills | Chaos day is part of learning |
| Scope creep (payments, search, K8s prod) | Park for Phase 6 / capstone |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| Topics 15–19 in this folder | Pattern refresh while integrating |
| [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/) | Edge routes |
| [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/) | Sync calls |
| [Spring Kafka](https://docs.spring.io/spring-kafka/reference/) | OrderPlaced |
| [Docker Compose](https://docs.docker.com/compose/) | Stack orchestration |
| [Apache Kafka docs](https://kafka.apache.org/documentation/) | Topics, consumers |
| [Resilience4j](https://resilience4j.readme.io/) | Timeouts / circuit breakers |
| Phase 4 [phase-project.md](../phase-04-spring/phase-project.md) | Domain rules to preserve |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Spring Cloud: https://docs.spring.io/spring-cloud/reference/
- Spring Cloud Gateway: https://docs.spring.io/spring-cloud-gateway/reference/
- OpenFeign: https://docs.spring.io/spring-cloud-openfeign/reference/
- Spring Kafka: https://docs.spring.io/spring-kafka/reference/
- Spring Boot Kafka: https://docs.spring.io/spring-boot/reference/messaging/kafka.html
- Resilience4j: https://resilience4j.readme.io/docs/getting-started
- Docker Compose: https://docs.docker.com/compose/
- Compose file reference: https://docs.docker.com/compose/compose-file/
- Dockerfile: https://docs.docker.com/reference/dockerfile/
- Kafka documentation: https://kafka.apache.org/documentation/
- Kafka intro: https://kafka.apache.org/intro
- Micrometer Tracing: https://docs.micrometer.io/tracing/reference/
- Spring Boot Actuator: https://docs.spring.io/spring-boot/reference/actuator/endpoints.html
- PostgreSQL: https://www.postgresql.org/docs/current/
- start.spring.io: https://start.spring.io/

---

## 11. Practice roadmap

### Evolution path

```text
v1 Split services + per-service DB + place-order sync
    → v2 Gateway + Docker Compose + resilience
        → v3 Kafka OrderPlaced + idempotent consumer + DoD
            → Phase 6 design notes + capstone backlog
```

### Week plan (summary)

| Week | Deliverable |
|------|-------------|
| 1 | Three services extracted; sync place-order works on host |
| 2 | Gateway + Compose stack; chaos on catalog |
| 3 | Kafka OrderPlaced producer + consumer path |
| 4 | README, DoD, Phase 6 & capstone prep notes |

### How this prepares for Phase 6 (Architecture & AI)

| Phase 5 artifact | Phase 6 use |
|------------------|-------------|
| Service boundary diagram | System design HLD input |
| Failure matrix | Reliability / trade-off discussions |
| Sync + async mix | Consistency models (strong vs eventual) |
| Compose topology | Scale units, bottlenecks, caching ideas |
| Event contract | Event versioning & schema evolution topics |
| Observability gaps list | What you’d add (SLOs, metrics, tracing) in design docs |

Capture in `NOTES-phase6-prep.md`:

1. What is the consistency story for stock after OrderPlaced?
2. Where are single points of failure (gateway, Kafka, each DB)?
3. What would you scale first under load (catalog reads? order writes?).
4. What belongs in a future API design doc vs code?

### Capstone prep (park ideas, do not build all now)

- Payment stub + compensating transactions / saga sketch
- Search service consuming catalog events
- Deploy images to AWS or Kubernetes using Phase 5 artifacts
- Read-model / CQRS for “my orders” timeline
- Optional AI: recommendation consumer on purchase events (Phase 6)

### Gate criteria (aligns with master guide)

- [ ] ≥2 services (target 3: user, catalog, order) + Docker + Kafka event path
- [ ] `docker compose up` (or documented equivalent) brings stack up
- [ ] Phase 5 personal mastery across topics 15–19 substantially complete

---

## 12. Mastery checklist

### Definition of Done (project)

- [ ] **user**, **catalog**, and **order** services run as separate Spring Boot apps
- [ ] Each service has its **own** database (no cross-service joins)
- [ ] API Gateway (or documented edge) routes to all three
- [ ] Register/login JWT works; roles enforced on catalog writes and orders
- [ ] Place-order uses catalog API for stock; persists order locally
- [ ] **Kafka** `OrderPlaced` (or equivalent) published on successful order
- [ ] At least one **consumer** reacts to the event (log/projection/notification stub)
- [ ] Consumer handles duplicates safely (idempotency)
- [ ] **Docker Compose** builds/runs the stack with DBs + Kafka
- [ ] Actuator health OK for services; README with ports, env, HTTP script
- [ ] Failure behavior documented when catalog or Kafka is unavailable
- [ ] Personal Phase 6 prep notes + capstone idea list written

### Personal mastery

- [ ] Can explain data ownership per service without notes
- [ ] Can draw sync vs async paths for place-order
- [ ] Can bring the stack up from a clean machine using only the README
- [ ] Can debug a failed Feign call and a failed Kafka consume in under 20 minutes
- [ ] Topics 15–19 checklists remain largely complete
- [ ] Ready to start Phase 6 — Architecture & AI

---

## Quick reference — success criteria

You are done with Phase 5 when BookStore runs as **containerized microservices** with a real **Kafka order event path**, clear **DB boundaries**, and a README that another-you could follow cold. Do not rush Phase 6 until the failure matrix and Compose runbook feel boringly clear.
