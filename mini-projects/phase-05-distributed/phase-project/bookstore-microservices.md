# Phase project — BookStore Microservices

**Topic:** [Phase 5 project](../../../phase-05-distributed/phase-project.md) · **Phase 5**

---

## Goal

Ship a multi-service BookStore: **user**, **catalog**, **order**, **API Gateway**, per-service Postgres, **Docker Compose**, **Kafka `OrderPlaced`**, JWT auth, and documented resilience—building on mini-projects 15–19.

---

## Prerequisites

- Topics 15–19 mastery ≥90% (or parallel completion per learning path)
- Mini-projects: microservices A, Docker A, Kafka A at minimum
- Phase 4 BookStore monolith as domain reference

---

## Learn

Reuse official docs from topics 15–19 (Gateway, Eureka optional, Compose, Kafka, Actuator). Prefer the phase-project file’s feature specification as source of truth.

---

## Project layout

```text
bookstore-microservices/
  platform/
    gateway/
    eureka-server/            # Could
    config-server/            # Could
  services/
    user-service/
    catalog-service/
    order-service/
    notification-logger/      # optional Kafka consumer
  infra/
    compose.yaml
    kafka/                    # if split
  docs/
    architecture.md
    demo.md
  README.md
  .env.example
```

---

## Build steps

- [ ] **1. Monorepo skeleton**
  1. Create folders above; root README + `.env.example` + `.gitignore`.
  2. **Done when:** Tree matches layout; empty modules build or placeholders exist.

- [ ] **2. user-service + DB**
  1. Postgres `postgres-user`; register/login JWT; roles `USER`/`ADMIN`; `/me`.
  2. **Done when:** Direct curls register/login work.

- [ ] **3. catalog-service + DB**
  1. Books CRUD; admin writes; stock source of truth; paginated list.
  2. Stock change/reserve API for orders.
  3. **Done when:** Seed books; get/list work.

- [ ] **4. order-service + DB**
  1. Place order with line items; Feign/WebClient to catalog stock; own DB only.
  2. List mine; ownership checks.
  3. **Done when:** Happy path order with catalog up.

- [ ] **5. Resilience**
  1. Timeouts + Resilience4j on catalog calls; controlled failure when catalog down.
  2. **Done when:** Chaos curl documented.

- [ ] **6. Gateway + JWT**
  1. Routes `/api/users/**`, `/api/books/**`, `/api/orders/**`.
  2. JWT validation at gateway and/or services.
  3. **Done when:** Full flow only via gateway base URL.

- [ ] **7. Kafka OrderPlaced**
  1. Compose Kafka; after successful order, publish event (outbox Should).
  2. Consumer logs or notification stub.
  3. **Done when:** Place order → consumer sees event within seconds.

- [ ] **8. Docker Compose stack**
  1. All services + 3 Postgres + Kafka (+ Zipkin Could).
  2. Healthchecks; DNS names; `docker compose up --build`.
  3. **Done when:** Clean machine smoke: register → login → list books → order → event.

- [ ] **9. Observability basics**
  1. Actuator health per service; correlation/request id Should.
  2. **Done when:** `curl` health on each or via gateway notes.

- [ ] **10. (Could) Eureka/Config**
  1. Retain discovery from topic 15 in Compose.
  2. **Done when:** Either wired or explicitly out of scope in README.

- [ ] **11. Demo script + architecture**
  1. `docs/demo.md` with exact curls; Mermaid component diagram.
  2. **Done when:** 10-minute demo rehearsed.

- [ ] **12. Phase gate self-check**
  1. Tick Must items from phase-project.md §6.
  2. **Done when:** ≥ Must complete; known limits listed.

---

## Definition of Done

- [ ] Must features from phase-project specification
- [ ] Compose brings up stack
- [ ] JWT + gateway path
- [ ] Kafka `OrderPlaced` observed
- [ ] No shared business tables across services
- [ ] README: ports, env, demo, destroy/`compose down`

---

## Demo

```bash
docker compose up --build -d
curl -s -X POST http://localhost:8080/api/users/register -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"Secret123!"}'
TOKEN=$(curl -s -X POST http://localhost:8080/api/users/login -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"Secret123!"}' | jq -r .token)
curl -s http://localhost:8080/api/books -H "Authorization: Bearer $TOKEN"
curl -s -X POST http://localhost:8080/api/orders -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" -d '{"items":[{"bookId":1,"quantity":1}]}'
docker compose logs order-service notification-logger --tail 40
docker compose down
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Shared DB for all services | Separate Postgres services/DBs |
| Business logic in gateway | Route + auth edge only |
| Kafka send without considering dual-write | Outbox spike / accept documented risk |
| `localhost` inside Compose | Service DNS names |
| Skipping clean `compose up` test | Gate requires it |

---

## After this

- Phase 6 design packs evolve this architecture
- Capstone reuses Gateway/JWT/Kafka/Compose patterns for Learning Hub
