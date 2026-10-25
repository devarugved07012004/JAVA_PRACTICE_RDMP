# 15 — Microservice Architecture

**Duration:** ~4–5 weeks (28–35 days) · **Cadence:** 1 hour/day  
**Focus:** Microservice Architecture, Eureka, Config Server, API Gateway, Feign Client, Resilience4j, Distributed Tracing, Centralized Logging · Build a multi-service Spring application

---

## 1. Why this topic is important

A working Spring Boot monolith (Phase 4 BookStore) teaches one process, one database, one deployable. Microservices teach **boundaries**: who owns which data, how services talk (sync HTTP vs async events), what happens when a dependency is slow or down, and how config, discovery, and observability keep a fleet operable.

You will learn the Spring Cloud stack that many Java shops still use locally and in production: **service discovery (Eureka)**, **centralized config**, **API Gateway**, **Feign** for typed HTTP clients, **Resilience4j** for timeouts/retries/circuit breakers, plus **tracing and logging** so failures are diagnosable. The goal is practical fluency—not dogma. You will also practice knowing **when not to split**.

---

## 2. Prerequisites

- Phase 4 BookStore API (or equivalent) mastery: Boot, REST, JPA, JWT basics, Actuator
- Topics [11](../phase-04-spring/11-spring-core.md)–[14](../phase-04-spring/14-jpa-hibernate.md) checklists ≥90%
- Comfortable running multiple Spring Boot apps on different ports
- Maven or Gradle; JDK 17+
- Basic Docker awareness helps later (topic 16); not required on Day 1
- Git optional for personal snapshots

**Environment you will set up:**

- Spring Boot 3.x + Spring Cloud (compatible BOM from [start.spring.io](https://start.spring.io/))
- At least three Boot apps: discovery, gateway, and two domain services (e.g. catalog + order)
- Optional: Zipkin or Tempo for tracing demos; a log aggregator later (or file-based first)
- PostgreSQL (one DB per service preferred for learning) or H2 per service for early weeks

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Why microservices | Independent deploy, team ownership, scale per service—and the cost (ops, consistency, latency) |
| Service boundaries | Bounded context; data ownership; no shared tables across services |
| Sync vs async | HTTP request/response now; events later (Kafka in topic 19) |
| Spring Cloud overview | BOM, version alignment with Boot |
| Eureka Server / Client | Registry of instances; heartbeat; lookup by name |
| Config Server | Externalized config in Git/filesystem; `@RefreshScope` idea |
| Multiple ports | Local multi-app workflow; `server.port` per service |
| Actuator health | Each service exposes health for later probes/gateway checks |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| API Gateway (Spring Cloud Gateway) | Single edge entry; routes; filters; auth at the edge (pattern) |
| Service discovery in clients | Call `http://service-name/...` via load-balanced client |
| OpenFeign | Declarative HTTP clients; fallbacks; timeouts |
| Client-side load balancing | Multiple instances of one service; round-robin intuition |
| Resilience4j | Timeout, retry, circuit breaker, bulkhead, rate limiter concepts |
| Failure modes | Cascading failure; timeouts beat infinite waits |
| Distributed config | Profiles per service; secrets not in Git (env / vault later) |
| Correlation IDs | Pass a request ID across services for log stitching |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| Distributed tracing | Trace/span model; Micrometer Tracing + Zipkin/OTLP |
| Centralized logging | Structured JSON logs; aggregation pattern (ELK/OpenSearch/Loki concepts) |
| Saga / eventual consistency (intro) | Stock reservation vs sync call trade-offs; prep for Kafka |
| Idempotency | Retries create duplicates unless designed for |
| Gateway security patterns | JWT validation at gateway vs per service |
| Observability triad | Logs + metrics + traces working together |
| Anti-patterns | Distributed monolith; chatty APIs; shared DB; premature split |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Boundaries & Eureka (Days 1–7)

- [ ] **Day 1 — Service boundaries**
  - [ ] **Learn:** [Spring Cloud overview](https://docs.spring.io/spring-cloud/reference/) · [Microsoft — Microservices architecture](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/)
  - [ ] **Practice:** Sketch BookStore service boundaries and data owners
    1. Draw three boxes: `user-service`, `catalog-service`, `order-service`.
    2. Under each, list 3–6 REST endpoints you expect.
    3. List tables/entities each owns; mark **no shared tables** across services.
    4. Draw sync calls (order → catalog) vs future async lines.
    5. Write one paragraph: why a shared `bookstore` DB is the wrong default.
    6. **Done when:** Diagram + endpoint/data-owner lists exist with no shared-table plan.

- [ ] **Day 2 — Eureka Server**
  - [ ] **Learn:** [Spring Cloud Netflix (Eureka)](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html) · [start.spring.io](https://start.spring.io/)
  - [ ] **Practice:** Create and run Eureka Server
    1. Generate a Boot app on start.spring.io with Eureka Server dependency.
    2. Add `@EnableEurekaServer`; set `server.port=8761`; disable self-preservation noise if needed for learning.
    3. Run the app; open `http://localhost:8761`.
    4. Confirm dashboard loads with an empty/nearly empty registry.
    5. Note Eureka URL in a personal README.
    6. **Done when:** Dashboard opens on 8761 with no real app instances yet.

- [ ] **Day 3 — Catalog registers**
  - [ ] **Learn:** [Eureka Client](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html) · [Spring Boot externalized config](https://docs.spring.io/spring-boot/reference/features/external-config.html)
  - [ ] **Practice:** Register `catalog-service` with Eureka
    1. Create `catalog-service` Boot app with Eureka Client (+ Web).
    2. Set `spring.application.name=catalog-service` and `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`.
    3. Add a simple `GET /api/books/ping` (or health stub).
    4. Start Eureka then catalog; refresh dashboard until instance appears.
    5. Click through / note instance id and port.
    6. **Done when:** `catalog-service` is visible on the Eureka dashboard.

- [ ] **Day 4 — Order service + multi-instance**
  - [ ] **Learn:** Re-skim [Eureka](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html) (client registration)
  - [ ] **Practice:** Register `order-service` and optional second catalog
    1. Create `order-service` with Eureka Client; set application name `order-service`.
    2. Register and confirm both catalog and order appear on Eureka.
    3. If time: run a second catalog on another port (`server.port=8082`) with same app name.
    4. Confirm Eureka shows two catalog instances (or note how to later).
    5. Document ports in README (8761, catalog, order).
    6. **Done when:** `order-service` is registered (and second catalog attempted or scheduled).

- [ ] **Day 5 — Actuator health**
  - [ ] **Learn:** [Spring Boot Actuator endpoints](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Expose health on catalog and order
    1. Add Actuator to catalog and order; expose `health` (and optionally `info`).
    2. Curl `http://localhost:<catalog-port>/actuator/health` and same for order.
    3. Confirm both return UP (or show clear DOWN reason).
    4. Add ports + Eureka URL table to personal README.
    5. Optional: show health in Eureka metadata if easy.
    6. **Done when:** Both services return health and README lists ports/Eureka.

- [ ] **Day 6 — Externalized properties**
  - [ ] **Learn:** [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html)
  - [ ] **Practice:** Clean `application.yml` without secrets
    1. Move ports, app names, and Eureka URLs into `application.yml` per service.
    2. Remove any passwords/tokens from yaml (use env placeholders if needed).
    3. Restart services; confirm registration still works.
    4. Add `application-local.yml` example for overrides (gitignored if sensitive).
    5. Document required env vars in README.
    6. **Done when:** Services start from clean yaml and secrets are not committed.

- [ ] **Day 7 — Discovery review**
  - [ ] **Learn:** [Spring Cloud reference hub](https://docs.spring.io/spring-cloud/reference/) (quick recap)
  - [ ] **Practice:** Draw and explain Eureka discovery
    1. Sequence diagram: client → Eureka → service instance.
    2. Label register, heartbeat, fetch registry.
    3. Explain aloud without notes for 60 seconds.
    4. Write what breaks if Eureka is down (client cache awareness).
    5. Fix any fuzzy wording in your README.
    6. **Done when:** Diagram exists and you can explain Eureka without looking it up.

### Week 2 — Config Server & Gateway (Days 8–14)

- [ ] **Day 8 — Config Server**
  - [ ] **Learn:** [Spring Cloud Config](https://docs.spring.io/spring-cloud-config/reference/)
  - [ ] **Practice:** Serve `catalog-service.yml` from Config Server
    1. Create Config Server app; `@EnableConfigServer`; point to native filesystem or local Git repo.
    2. Add `catalog-service.yml` with a custom property (e.g. `bookstore.catalog.message=hello-config`).
    3. Start Config Server (e.g. port 8888).
    4. Curl `http://localhost:8888/catalog-service/default` and confirm property appears.
    5. Document Config Server URL in README.
    6. **Done when:** Config Server returns `catalog-service` properties over HTTP.

- [ ] **Day 9 — Config clients**
  - [ ] **Learn:** [Config Client / refresh](https://docs.spring.io/spring-cloud-config/reference/)
  - [ ] **Practice:** Prove a remote property change in catalog/order
    1. Add Config Client to catalog (and order if ready); set `spring.cloud.config.uri`.
    2. Inject/`@Value` the custom property; expose on a ping endpoint or log at startup.
    3. Change the property in the config repo/file.
    4. Restart client **or** use `/actuator/refresh` if configured.
    5. Confirm the new value is visible.
    6. **Done when:** Property change from Config Server is observed in the client.

- [ ] **Day 10 — Gateway static route**
  - [ ] **Learn:** [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Route to catalog by static URL through Gateway
    1. Create Gateway Boot app; set a free port (e.g. 8080).
    2. Configure a route: `Path=/api/catalog/**` → `http://localhost:<catalog-port>` (static URI).
    3. Start Gateway + catalog; curl through Gateway only.
    4. Confirm response matches direct catalog call.
    5. Note Gateway port as the only public entry in README.
    6. **Done when:** Curl via Gateway reaches catalog without calling catalog port from the client.

- [ ] **Day 11 — Discovery routes**
  - [ ] **Learn:** [Gateway + service discovery](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Switch to `lb://catalog-service`
    1. Register Gateway with Eureka (or enable discovery locator).
    2. Change route URI to `lb://catalog-service` with appropriate predicates/filters.
    3. Restart; curl Gateway → catalog.
    4. Stop calling catalog by hardcoded host from the client.
    5. Confirm Eureka still shows catalog + gateway (if registered).
    6. **Done when:** Gateway uses `lb://catalog-service` successfully.

- [ ] **Day 12 — Order route + request id**
  - [ ] **Learn:** [Gateway filters](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Add order route and `X-Request-Id` filter
    1. Add Gateway route for `order-service` (`lb://order-service` or static first).
    2. Add a global filter that sets `X-Request-Id` (generate UUID if missing).
    3. Log the header in order or catalog controller/filter.
    4. Curl through Gateway; confirm header appears in service logs.
    5. Document routes table in README.
    6. **Done when:** Both routes work and `X-Request-Id` is visible downstream.

- [ ] **Day 13 — Edge auth notes**
  - [ ] **Learn:** [Spring Security + JWT (skim)](https://docs.spring.io/spring-security/reference/) · [Gateway filters](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Decide JWT at Gateway vs services
    1. Sketch trust boundaries: public → Gateway → services.
    2. Either add a minimal JWT validation filter at Gateway **or** write a half-page note on where auth will live later.
    3. List which paths stay public (health, login) vs protected.
    4. Note risks of trusting unsigned headers from Gateway.
    5. Keep implementation light—clarity over completeness.
    6. **Done when:** Auth placement is documented (and optional filter works if you built it).

- [ ] **Day 14 — E2E gateway path**
  - [ ] **Learn:** Revisit [Gateway](https://docs.spring.io/spring-cloud-gateway/reference/) + [Eureka](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html)
  - [ ] **Practice:** Stabilize gateway → discovery → catalog GET
    1. Start order: Eureka → Config (if used) → services → Gateway.
    2. Curl Gateway catalog GET until stable (fix registration timing if 503).
    3. Capture one failing startup and the fix in notes.
    4. Write start-order checklist in README.
    5. Optional: add retry/wait script for local demos.
    6. **Done when:** Cold start + successful Gateway catalog GET is reproducible.

### Week 3 — Feign & Resilience4j (Days 15–21)

- [ ] **Day 15 — OpenFeign client**
  - [ ] **Learn:** [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/)
  - [ ] **Practice:** Define `CatalogClient` in order-service
    1. Add OpenFeign to `order-service`; `@EnableFeignClients`.
    2. Create `CatalogClient` with `GET` book-by-id and a stock stub method.
    3. Point Feign at `catalog-service` via discovery name.
    4. Call the client from a temporary test endpoint; log the response.
    5. Confirm Eureka name matches Feign client name.
    6. **Done when:** Order service successfully Feign-calls catalog.

- [ ] **Day 16 — Place-order happy path**
  - [ ] **Learn:** [OpenFeign reference](https://docs.spring.io/spring-cloud-openfeign/reference/)
  - [ ] **Practice:** Implement sync place-order via Feign
    1. Add `POST /api/orders` that loads book(s) via `CatalogClient`.
    2. Persist a simple order entity in order’s own DB/in-memory store.
    3. Happy path only: assume stock OK; return 201 with order id.
    4. Curl through Gateway if routes exist.
    5. Verify catalog was called (logs).
    6. **Done when:** Place-order happy path works end-to-end over Feign.

- [ ] **Day 17 — Timeouts & retries**
  - [ ] **Learn:** [Resilience4j Getting started](https://resilience4j.readme.io/docs/getting-started) · [Retry](https://resilience4j.readme.io/docs/retry)
  - [ ] **Practice:** Demo timeout/retry against a slow catalog
    1. Add a catalog endpoint that `Thread.sleep(3000)` (delay demo).
    2. Configure Feign/Resilience4j connect/read timeouts shorter than the sleep.
    3. Add a small retry (2–3 attempts) on the Feign client.
    4. Call place-order; observe timeout/retry logs.
    5. Remove or gate the delay endpoint behind a profile.
    6. **Done when:** Logs show timeout/retry behavior against the slow endpoint.

- [ ] **Day 18 — Circuit breaker**
  - [ ] **Learn:** [Resilience4j CircuitBreaker](https://resilience4j.readme.io/docs/circuitbreaker) · [Spring Boot integration](https://resilience4j.readme.io/docs/getting-started-3)
  - [ ] **Practice:** Open the circuit and return a clear fallback
    1. Wrap catalog Feign calls with CircuitBreaker + fallback.
    2. Stop catalog or force failures until the circuit opens.
    3. Confirm fallback returns 503 or clear business error JSON (not a hang).
    4. Restart catalog; wait for half-open/closed; confirm recovery.
    5. Note thresholds used in README.
    6. **Done when:** Open circuit produces a clear error and later recovers.

- [ ] **Day 19 — Load balance under failure**
  - [ ] **Learn:** [Spring Cloud LoadBalancer](https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html)
  - [ ] **Practice:** Two catalogs — kill one and observe behavior
    1. Run two `catalog-service` instances on different ports; both in Eureka.
    2. Place several orders; confirm traffic spreads (instance logs).
    3. Kill one catalog; continue calling.
    4. Observe LoadBalancer skipping dead instance and/or breaker behavior.
    5. Record observations in notes.
    6. **Done when:** Notes show LB + failure behavior with two instances.

- [ ] **Day 20 — Error mapping**
  - [ ] **Learn:** [OpenFeign error handling](https://docs.spring.io/spring-cloud-openfeign/reference/)
  - [ ] **Practice:** Map Feign failures to consistent problem JSON
    1. Standardize error JSON fields (`timestamp`, `message`, `path`, `status`).
    2. Add `@ControllerAdvice` (or handler) on order for Feign exceptions.
    3. Trigger 404 from catalog and a connection failure; compare responses.
    4. Align Gateway error body if easy.
    5. Document status code mapping table.
    6. **Done when:** Feign failures return consistent problem responses.

- [ ] **Day 21 — Failure matrix**
  - [ ] **Learn:** [Resilience4j docs hub](https://resilience4j.readme.io/) (quick recap)
  - [ ] **Practice:** Write the week’s failure matrix
    1. Table rows: catalog down, catalog slow, one of two instances down, Config Server down.
    2. Columns: user-visible behavior, logs/trace, mitigation.
    3. Fill from experiments Days 17–20.
    4. Week review: top 5 learnings.
    5. List gaps for Week 4 tracing.
    6. **Done when:** Failure matrix file/notes is complete and concrete.

### Week 4 — Tracing, logging, multi-service app (Days 22–28)

- [ ] **Day 22 — Micrometer Tracing**
  - [ ] **Learn:** [Micrometer Tracing](https://docs.micrometer.io/tracing/reference/) · [Zipkin Quickstart](https://zipkin.io/pages/quickstart.html)
  - [ ] **Practice:** Enable tracing export to Zipkin
    1. Run Zipkin: `docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin` (or local jar).
    2. Add Micrometer Tracing + Zipkin/Brave (or OTel) deps to Gateway, order, catalog.
    3. Set `management.tracing.sampling.probability=1.0` for learning.
    4. Restart services; generate one request.
    5. Open `http://localhost:9411` and confirm traces appear (even partial).
    6. **Done when:** Zipkin UI shows at least one span from your apps.

- [ ] **Day 23 — Cross-service trace**
  - [ ] **Learn:** [Micrometer Tracing](https://docs.micrometer.io/tracing/reference/) · [OpenZipkin](https://zipkin.io/)
  - [ ] **Practice:** Capture gateway → order → catalog in one trace
    1. Place an order through Gateway so order Feign-calls catalog.
    2. In Zipkin, find the trace by service name or time.
    3. Confirm spans for Gateway, order, and catalog share one `traceId`.
    4. Screenshot or paste span list into notes.
    5. Fix missing spans (deps, sampling, Feign instrumentation) if needed.
    6. **Done when:** One Zipkin trace shows all three hops.

- [ ] **Day 24 — Structured logs**
  - [ ] **Learn:** [Spring Boot logging](https://docs.spring.io/spring-boot/reference/features/logging.html)
  - [ ] **Practice:** Log `traceId` / `X-Request-Id` on each service
    1. Configure logging pattern (or JSON encoder) to include `traceId`/`spanId` if available.
    2. Log `X-Request-Id` on Gateway filter and downstream services.
    3. Make one request; grep logs across three services for the same ids.
    4. Prefer consistent field names in all services.
    5. Document the log pattern in README.
    6. **Done when:** One request’s ids are correlatable across Gateway, order, catalog logs.

- [ ] **Day 25 — Centralized logging pattern**
  - [ ] **Learn:** [OpenTelemetry overview (optional)](https://opentelemetry.io/docs/) · [Micrometer Tracing](https://docs.micrometer.io/tracing/reference/)
  - [ ] **Practice:** Document ELK/Loki aggregation (light hands-on)
    1. Write how logs would ship to ELK or Loki (agents → aggregator → UI).
    2. Optionally redirect Boot logs to a `./logs/` folder per service for local “aggregation.”
    3. Show how `traceId` would be the search key in a central UI.
    4. List 3 queries you’d run during an outage.
    5. Keep scope to notes unless you already know the stack.
    6. **Done when:** Aggregation pattern notes exist with concrete search keys.

- [ ] **Day 26 — Domain APIs for demo**
  - [ ] **Learn:** [spring.io Guides](https://spring.io/guides)
  - [ ] **Practice:** Flesh catalog + order enough for place-order demo
    1. Catalog: list books, get by id, stock field (even if simplistic).
    2. Order: place-order using Feign; return order id + lines.
    3. Seed 2–3 books for the demo.
    4. Curl script: list → place → get order (if you have get).
    5. Remove dead stub endpoints that confuse the demo.
    6. **Done when:** Demo place-order flow works with real-looking APIs.

- [ ] **Day 27 — Auth trust boundaries**
  - [ ] **Learn:** [Spring Security reference](https://docs.spring.io/spring-security/reference/)
  - [ ] **Practice:** Stub JWT issuer or document trust boundaries
    1. Draw who issues JWT and who validates it (Gateway vs services).
    2. Optional: minimal auth stub issuing HS256 JWT for learning.
    3. Protect one order endpoint; prove 401 without token.
    4. Document public vs authenticated paths.
    5. Note what Capstone/user-service will replace.
    6. **Done when:** Trust-boundary doc exists (and optional JWT stub works).

- [ ] **Day 28 — Demo & checklist**
  - [ ] **Learn:** Recap [Spring Cloud hub](https://docs.spring.io/spring-cloud/reference/)
  - [ ] **Practice:** Demo script, architecture diagram, checklist dry-run
    1. Draw architecture: Eureka, Config, Gateway, catalog, order, Zipkin.
    2. Write numbered demo script with curl commands.
    3. Run the demo once from the script; fix gaps.
    4. Dry-run mastery checklist; mark honest status.
    5. List Week 5 mini-project focus.
    6. **Done when:** Demo script + diagram exist and checklist is marked.

### Week 5 — Consolidation & mini-projects (Days 29–35)

- [ ] **Day 29 — Mini-project A start**
  - [ ] **Learn:** Skim §6A + [Gateway](https://docs.spring.io/spring-cloud-gateway/reference/) + [Eureka](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html)
  - [ ] **Practice:** Stabilize Eureka + Gateway + two services for Mini-project A
    1. Read §6A acceptance criteria; create a checklist.
    2. Ensure Eureka + Gateway + two services boot in a known order.
    3. Fix port conflicts; update README start order.
    4. Smoke: Gateway ping to both services.
    5. Tag git/`NOTES` as mini-project-A baseline.
    6. **Done when:** Stable cold start for Eureka + Gateway + two services.

- [ ] **Day 30 — Feign + resilience paths**
  - [ ] **Learn:** [OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/) · [CircuitBreaker](https://resilience4j.readme.io/docs/circuitbreaker)
  - [ ] **Practice:** Complete Feign + Resilience4j for Mini-project A
    1. Ensure place-order (or project flow) uses Feign.
    2. Confirm timeout, retry, and circuit breaker configs are intentional.
    3. Demo catalog-down fallback once; capture response body.
    4. Add these steps to the demo script.
    5. Remove leftover experimental delay endpoints or hide behind profile.
    6. **Done when:** Happy path + catalog-down path both demo cleanly.

- [ ] **Day 31 — Tracing end-to-end**
  - [ ] **Learn:** [Micrometer Tracing](https://docs.micrometer.io/tracing/reference/)
  - [ ] **Practice:** End-to-end tracing + request ids for the mini-project
    1. Verify `X-Request-Id` and Zipkin `traceId` on the mini-project flow.
    2. Capture one Zipkin screenshot or span dump in `docs/`.
    3. Ensure sampling stays on for demos.
    4. Add Zipkin URL to README.
    5. Fix any service missing instrumentation.
    6. **Done when:** Docs include one full-trace artifact for the demo path.

- [ ] **Day 32 — Mini-project B config**
  - [ ] **Learn:** [Spring Cloud Config](https://docs.spring.io/spring-cloud-config/reference/)
  - [ ] **Practice:** Config Server + refresh story (Mini-project B)
    1. Ensure Config Server serves at least one client property used in a response/log.
    2. Change property; refresh or restart to prove pickup.
    3. Document native/Git backend choice and folder layout.
    4. Keep scope tight—refresh story over many files.
    5. Link Mini-project B notes from README.
    6. **Done when:** Config change is demonstrable via Config Server.

- [ ] **Day 33 — Chaos drills**
  - [ ] **Learn:** [Resilience4j](https://resilience4j.readme.io/) (failure modes)
  - [ ] **Practice:** Kill Eureka/catalog and induce latency; record outcomes
    1. Kill catalog; call place-order; record response time and body.
    2. Induce latency on catalog; record timeout/retry behavior.
    3. Stop Eureka briefly; observe client behavior (cached registry vs failures).
    4. Fill a chaos results table in notes.
    5. Restore all processes; confirm healthy demo.
    6. **Done when:** Chaos table has outcomes for Eureka down, catalog down, latency.

- [ ] **Day 34 — README runbook**
  - [ ] **Learn:** Your notes + [Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** README start order, ports, limitations
    1. Write start order: Eureka → Config → services → Gateway → Zipkin.
    2. Ports table for every process.
    3. Health curl commands per service.
    4. Known limitations (no Compose yet, in-memory bits, etc.).
    5. Have a classmate/future-you test by following README only (you simulate).
    6. **Done when:** README alone is enough to boot and smoke-test.

- [ ] **Day 35 — Mastery gate**
  - [ ] **Learn:** Recap [Spring Cloud](https://docs.spring.io/spring-cloud/reference/) · [Resilience4j](https://resilience4j.readme.io/)
  - [ ] **Practice:** Checklist ≥90% and Phase project preview notes
    1. Tick §12 checklist; aim ≥90% honest completion.
    2. Fix any quick gaps (missing health, missing route).
    3. Note what Phase project adds: Compose, per-service DBs, Kafka.
    4. List reuse candidates (Gateway routes, Feign client, Zipkin config).
    5. Short retrospective (keep/stop/start).
    6. **Done when:** Checklist ≥90% and Phase project preview notes exist.

*On a 4-week pace: merge Week 5 into Days 25–28 (ship Mini-project A + tracing; Config Server refresh can be lighter).*

---

## 5. Hands-on exercises

1. Register three fake instances of one service name; call through gateway 20 times; count hits per instance (logs).
2. Break Eureka URL in a client; observe startup/retry behavior; fix and re-register.
3. Config Server: change `custom.message`; prove service reads new value after restart (and refresh if enabled).
4. Gateway path rewrite: external `/api/books/**` → catalog `/books/**`.
5. Feign client with wrong host: confirm failure mode before discovery wiring.
6. Resilience4j: force 5 failures → open circuit → verify fallback; wait for half-open success.
7. Timeout: catalog sleeps 5s; client timeout 1s; ensure thread pool does not hang forever.
8. Propagate `X-Request-Id` from gateway through Feign interceptor; grep logs for one ID across apps.
9. Zipkin: identify which span is the Feign call; note duration.
10. Health aggregation notes: list what “UP” means per service vs whole system.
11. Shared-DB smell demo (optional): show why joining order+book tables across services is wrong—use API instead.
12. Write a one-page “when I would stay modular monolith” decision note from your failure drills.

---

## 6. Mini-projects

> **Build guides:** [A Book Catalog + Orders](../mini-projects/phase-05-distributed/15-microservices/A-book-catalog-orders.md) · [B Config & Resilience Lab](../mini-projects/phase-05-distributed/15-microservices/B-config-resilience-lab.md) · [C Observability Pack](../mini-projects/phase-05-distributed/15-microservices/C-observability-pack.md) · [Index](../mini-projects/README.md)

### A. Multi-Service Book Catalog + Orders (core)

**Services:**

| Service | Responsibility |
|---------|----------------|
| `eureka-server` | Registry |
| `api-gateway` | Edge routes to catalog & order |
| `catalog-service` | Books CRUD (simplify auth if needed) |
| `order-service` | Place order via Feign → catalog stock check |

**Must:**

- Discovery registration for catalog and order
- Gateway is the only public entry for HTTP demos
- Place-order calls catalog; Resilience4j timeout + circuit breaker
- Correlation ID in logs; at least one Zipkin trace captured (screenshot/notes OK)
- Personal README with start order and ports

**Should:** Config Server for shared non-secret settings  
**Could:** Third `user-service` for register/login JWT

### B. Config & Resilience Lab

- Dedicated Config Server repo/folder with `application.yml` + per-service files
- Demonstrate one breaking change rolled via config (feature flag style property)
- Chaos script (manual): stop catalog → place-order fallback → restart → recovery

### C. (Stretch) Observability Pack

- JSON logs + documented log fields
- Micrometer metrics endpoint skim
- Trace one failing request and write a 5-bullet postmortem

---

## 7. Industry use cases

- **E-commerce checkout:** Order service orchestrates catalog, pricing, inventory over APIs or events
- **Edge API platforms:** API Gateway for routing, TLS termination, auth, rate limits
- **Multi-team backends:** Each team owns a service and deploy cadence
- **SaaS multi-tenant cores:** Isolation of billing vs identity vs product modules
- **Resilient integrations:** Circuit breakers around payment or partner HTTP APIs
- **Ops / SRE:** Traces and centralized logs to debug cross-service latency

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Splitting before module boundaries are clear | Finish Phase 4 monolith clarity first |
| Shared database across “services” | One database (or schema) per service for learning |
| No timeouts on Feign | Always set connect/read timeouts + Resilience4j |
| Retrying non-idempotent POSTs blindly | Idempotency keys or “at most once” awareness |
| Calling services by localhost ports from gateway in “discovery mode” | Use `lb://service-id` consistently |
| Starting apps in random order forever | Document start order; later use Compose healthchecks (topic 16) |
| Eureka + Config as unexplained magic | Draw the control-plane vs data-plane diagram |
| Ignoring clock/registry lag | Retry registration; wait for instances BEFORE demos |
| Logging only inside one service | Correlation ID + trace across the hop |
| Building a distributed monolith | Prefer fewer, clearer services over many chatty ones |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Spring Cloud docs](https://docs.spring.io/spring-cloud/reference/) | Official Cloud reference hub |
| [Spring Cloud Netflix (Eureka)](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html) | Eureka server/client |
| [Spring Cloud Config](https://docs.spring.io/spring-cloud-config/reference/) | Config Server / client |
| [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/) | Routes, filters, discovery |
| [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/) | Declarative clients |
| [Resilience4j docs](https://resilience4j.readme.io/) | Circuit breaker, retry, etc. |
| [Micrometer Tracing](https://docs.micrometer.io/tracing/reference/) | Tracing concepts |
| [OpenZipkin](https://zipkin.io/) | Local trace UI |
| [spring.io Guides](https://spring.io/guides) | Boot + Cloud starter patterns |
| [Microsoft — Microservices architecture (free ebook/docs)](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/) | Architecture intuition (language-agnostic ideas) |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Spring Cloud reference: https://docs.spring.io/spring-cloud/reference/
- Spring Cloud release train / BOM notes: https://github.com/spring-cloud/spring-cloud-release/wiki
- Eureka: https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html
- Spring Cloud Config: https://docs.spring.io/spring-cloud-config/reference/
- Spring Cloud Gateway: https://docs.spring.io/spring-cloud-gateway/reference/
- Gateway discovery locator: https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-mvc/filters.html (also check WebFlux Gateway routing docs on same site)
- OpenFeign: https://docs.spring.io/spring-cloud-openfeign/reference/
- Spring Boot Actuator: https://docs.spring.io/spring-boot/reference/actuator/endpoints.html
- Resilience4j Getting started: https://resilience4j.readme.io/docs/getting-started
- Resilience4j CircuitBreaker: https://resilience4j.readme.io/docs/circuitbreaker
- Resilience4j with Spring Boot: https://resilience4j.readme.io/docs/getting-started-3
- Micrometer Tracing: https://docs.micrometer.io/tracing/reference/
- Zipkin: https://zipkin.io/pages/quickstart.html
- OpenTelemetry (optional deepening): https://opentelemetry.io/docs/
- start.spring.io: https://start.spring.io/
- Spring Boot externalized config: https://docs.spring.io/spring-boot/reference/features/external-config.html

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Boundaries | Clear service/data ownership sketch | Written diagram for BookStore split |
| 2. Discovery | Eureka + 2 services | Instances visible; health UP |
| 3. Edge | Gateway routes via discovery | All demos through gateway |
| 4. Config | Config Server feeding ≥1 service | Property change proven |
| 5. Sync calls | Feign order → catalog | Happy path place-order |
| 6. Resilience | Timeout + circuit breaker | Failure matrix exercised |
| 7. Observability | Trace + correlation logs | One cross-service trace explained |
| 8. Gate | Mini-project A done | Checklist ≥90%; ready for Docker |

**Weekly rhythm:** Mon–Fri build; Sat failure drills; Sun diagram + README cleanup.

**Suggested local port map (example):**

| App | Port |
|-----|------|
| Eureka | 8761 |
| Config Server | 8888 |
| Gateway | 8080 |
| Catalog | 8081 |
| Order | 8082 |
| Zipkin | 9411 |

---

## 12. Mastery checklist

- [ ] Can explain monolith vs microservices trade-offs in plain language
- [ ] Service boundaries and **data ownership** reasoned out for a sample domain
- [ ] Eureka Server runs; ≥2 services register and show in the dashboard
- [ ] Config Server supplies at least one external property to a service
- [ ] API Gateway routes to services using discovery (`lb://...`)
- [ ] Sync inter-service call works (Feign or equivalent)
- [ ] Resilience4j timeout and circuit breaker demonstrated under failure
- [ ] Failure/timeout thinking captured in personal notes (failure matrix)
- [ ] Correlation ID (or traceId) visible across ≥2 services’ logs
- [ ] At least one distributed trace inspected in Zipkin (or equivalent)
- [ ] Centralized logging approach understood (even if aggregator is minimal)
- [ ] Know when **not** to split further (written decision note)
- [ ] Mini-project A (multi-service app) completed
- [ ] Ready to containerize the stack in [16-docker.md](16-docker.md)
