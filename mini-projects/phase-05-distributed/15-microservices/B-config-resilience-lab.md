# Mini-project B — Config & Resilience Lab

**Topic:** [15 Microservices](../../../phase-05-distributed/15-microservices.md) · **Phase 5**

---

## Goal

Stand up Spring Cloud Config Server with a local (or Git) config repo, drive one feature-flag-style property into a client service, and run a chaos drill: stop catalog → place-order fallback → restart → recovery.

---

## Prerequisites

- Mini-project A baseline (Eureka + Gateway + catalog + order) running
- Comfort with `application.yml` profiles
- Optional Git repo for config files (file-system backend OK for learning)

---

## Learn

| Resource | Why |
|----------|-----|
| [Spring Cloud Config](https://docs.spring.io/spring-cloud-config/reference/) | Server + client |
| [Spring Cloud Context refresh](https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/multiuser-configuration.html) | `/actuator/refresh` mindset |
| Resilience4j circuit breaker docs | Fallback + half-open recovery |

---

## Project layout

```text
config-resilience-lab/
  config-repo/                    # or git repo
    application.yml
    catalog-service.yml
    order-service.yml
  config-server/                  # Spring Cloud Config Server
  # reuse A services as clients OR thin client stubs
  scripts/
    chaos-catalog.ps1             # or .sh
  NOTES.md
  README.md
```

---

## Build steps

- [ ] **1. Config repo**
  1. Create `config-repo/application.yml` with shared non-secrets (e.g. `bookstore.banner=hello`).
  2. Add `order-service.yml` with `bookstore.orders.max-lines=5` (example).
  3. **Done when:** Files exist and are readable; no passwords committed.

- [ ] **2. Config Server app**
  1. New Boot app with `spring-cloud-config-server`; `@EnableConfigServer`.
  2. Point `spring.cloud.config.server.native.search-locations` (or Git URI) at `config-repo`.
  3. Run on port `8888`.
  4. **Done when:** `curl http://localhost:8888/order-service/default` returns YAML/JSON with your properties.

- [ ] **3. Wire a client**
  1. Add Config client dependency to `order-service` (or both services).
  2. `spring.config.import=optional:configserver:http://localhost:8888` (Boot 2.4+/3 style) or classic `bootstrap`.
  3. Inject `@Value` or `@ConfigurationProperties` for the flag/limit.
  4. **Done when:** Startup logs show property loaded from Config Server.

- [ ] **4. Feature-flag style change**
  1. Change `bookstore.banner` (or similar) in config-repo.
  2. Either restart client **or** expose refresh and call `POST /actuator/refresh` (if enabled securely for lab).
  3. Hit an endpoint that echoes the banner.
  4. **Done when:** Documented before/after values prove config-driven change.

- [ ] **5. Chaos script — stop catalog**
  1. Script or NOTES steps: kill/stop catalog process.
  2. `POST` place-order via gateway.
  3. Confirm fallback / 503 / problem+json — not a hung thread.
  4. **Done when:** One failing request completes in &lt; few seconds with controlled body.

- [ ] **6. Recovery**
  1. Restart catalog; wait Eureka UP.
  2. Place-order again succeeds.
  3. Note circuit breaker state transitions if metrics/logs show them.
  4. **Done when:** Happy path restored without restarting order-service (ideal) or with documented restart if needed.

- [ ] **7. Resilience checklist in NOTES**
  1. List: timeouts set?, breaker name?, fallback behavior?, recovery wait time.
  2. **Done when:** `NOTES.md` has a short chaos postmortem (5 bullets).

- [ ] **8. README link from A**
  1. Link Mini-project B from A’s README.
  2. Document Config Server start order (Config → Eureka → services → Gateway).
  3. **Done when:** Newcomer can run Config demo from README alone.

---

## Definition of Done

- [ ] Config Server serves at least `application` + one service file
- [ ] Client loads a non-secret property from Config
- [ ] One property change demonstrated (restart or refresh)
- [ ] Chaos: catalog down → controlled failure → restart → success
- [ ] Notes + README complete

---

## Demo

```bash
# Config Server
curl -s http://localhost:8888/order-service/default

# Echo banner / max-lines from order-service (your endpoint)
curl -s http://localhost:8080/api/orders/config-info

# Chaos
# Stop catalog-service process
curl -s -o - -w "\nHTTP %{http_code}\n" -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" -d '{"bookId":1,"quantity":1}'
# Restart catalog; wait; retry POST → 201
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Secrets in config-repo | Only non-secrets; use env for passwords |
| Client can’t find server | Start Config before clients; check URI |
| Refresh without actuator security thought | Lab-only; don’t expose refresh publicly |
| Chaos without timeouts | Hangs look like “breaker failed” |
| Skipping recovery proof | Always restart and succeed once |

---

## After this

- Optional stretch: [C-observability-pack.md](./C-observability-pack.md)
- Carry Config + chaos notes into Docker Compose (topic 16) and phase project
