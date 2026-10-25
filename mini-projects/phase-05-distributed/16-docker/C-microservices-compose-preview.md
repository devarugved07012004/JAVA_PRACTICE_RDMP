# Mini-project C — Microservices Compose Preview (Stretch)

**Topic:** [16 Docker](../../../phase-05-distributed/16-docker.md) · **Phase 5**

---

## Goal

Sketch a Compose file for the topic-15 style stack (Eureka, Gateway, catalog, order, DB)—even with stubs—so service names, ports, and healthchecks are practiced before the full phase project.

---

## Prerequisites

- Mini-projects A + B
- Familiarity with topic 15 service names
- Optional: reuse real images from A/B or stub `nginx`/`wiremock` placeholders

---

## Learn

| Resource | Why |
|----------|-----|
| Compose `profiles` (optional) | Run subsets |
| Healthchecks + `depends_on` | Control plane vs data plane start order |
| Topic 15 port map | Consistency |

---

## Project layout

```text
microservices-compose-preview/
  compose.yaml
  stubs/                 # optional Dockerfiles that sleep / return 200
  README.md              # service name ↔ port table
  NOTES.md               # what is real vs stub
```

---

## Build steps

- [ ] **1. Service inventory table**
  1. List: `eureka`, `gateway`, `catalog-service`, `order-service`, `postgres` (and optional `zipkin`, `config-server`).
  2. Assign container names and host ports.
  3. **Done when:** Table exists in README.

- [ ] **2. Compose skeleton**
  1. Create `compose.yaml` with all services declared (images or build contexts).
  2. Shared network `ms-net`.
  3. **Done when:** `docker compose config` validates without error.

- [ ] **3. Postgres + volume**
  1. Real Postgres service with healthcheck.
  2. **Done when:** DB healthy alone via `docker compose up postgres -d`.

- [ ] **4. Stub or real apps**
  1. Prefer real JARs from topic 15 if available; else stub containers that expose `/actuator/health` or static 200.
  2. Env placeholders for JDBC / Eureka URLs using **service names**.
  3. **Done when:** At least gateway + one backend container starts.

- [ ] **5. Start-order comments**
  1. Comment in compose or README: Config (opt) → Eureka → DB → services → Gateway.
  2. Wire `depends_on` for Eureka/DB where meaningful.
  3. **Done when:** Order is written, not tribal knowledge.

- [ ] **6. Port conflict check**
  1. `docker compose up -d` (as far as stubs allow).
  2. Fix host port clashes.
  3. **Done when:** `docker compose ps` shows expected listening ports.

- [ ] **7. DNS smoke**
  1. From gateway container, resolve `catalog-service` / `eureka`.
  2. **Done when:** NOTES show `getent hosts catalog-service` or equivalent.

- [ ] **8. Honesty section**
  1. Label each service Real / Stub in README.
  2. Point to phase project for full wiring.
  3. **Done when:** No claim that stubs are production-ready microservices.

---

## Definition of Done

- [ ] Compose skeleton for gateway + catalog + order + eureka + db
- [ ] Service DNS names practiced
- [ ] Ports documented
- [ ] Real vs stub labeled
- [ ] `docker compose config` clean

---

## Demo

```bash
docker compose config
docker compose up -d postgres eureka
docker compose ps
docker compose up -d
docker compose logs gateway --tail 40
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Claiming full phase project done | This is a preview skeleton |
| Mixing localhost URLs in env | Use Compose DNS |
| One giant Postgres for all “services” without schemas | Prefer separate DBs/schemas when real |
| Undocumented ports | Always publish the table |

---

## After this

- Expand into [phase-project/bookstore-microservices.md](../phase-project/bookstore-microservices.md)
