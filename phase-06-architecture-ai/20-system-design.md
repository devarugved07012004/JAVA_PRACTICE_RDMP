# 20 — System Design

**Duration:** ~4 weeks (28 days) · **Cadence:** 1 hour/day  
**Focus:** HLD, LLD, scalability, load balancing, caching, database scaling, distributed systems, CAP theorem, message queues — with Java/Spring examples and practical design exercises for systems you own

---

## 1. Why this topic is important

System design is how you turn “it works on my laptop” into a system that stays understandable as traffic, data, and features grow. High-level design (HLD) decides **what pieces exist and how they talk**; low-level design (LLD) decides **classes, APIs, schemas, and failure handling** inside those pieces.

You already built Spring Boot APIs, microservices, Docker, Kafka, and Kubernetes. This topic teaches you to **choose** those tools deliberately: when a cache helps, when a queue decouples enrollment from email, when one Postgres is enough, and when splitting databases or services reduces pain instead of creating it.

Frame every exercise as architecture skill for **your** BookStore, Learning Hub, and notification flows—not as puzzle drills for someone else’s whiteboard.

---

## 2. Prerequisites

- Phase 4 BookStore API fluency (Boot, JPA, JWT, Postgres)
- Phase 5 distributed basics: multiple services, Gateway, Feign/HTTP, Kafka events, Docker Compose, K8s awareness
- Comfortable reading Spring `@RestController`, services, repositories
- Can draw a simple box diagram of a request path (client → API → DB)

**Environment**

- JDK 17+ · Spring Boot 3.x notes from prior phases
- Local Postgres (and Redis optional for caching labs)
- Draw.io, Excalidraw, Mermaid, or paper for diagrams
- Prior BookStore / microservices repos available to refactor mentally

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Requirements → constraints | Functional needs vs non-functional (latency, availability, cost, simplicity) |
| HLD vs LLD | HLD = components + data flow + tech choices; LLD = APIs, classes, tables, sequences |
| Clients & boundaries | Browser/mobile → API gateway → services → data stores |
| Sync vs async | HTTP request/response vs fire-and-forget events (Kafka) |
| Vertical vs horizontal scale | Bigger machine vs more instances behind a load balancer |
| Stateless services | JWT-friendly Spring Boot; session sticky as a smell for most APIs |
| Single DB designs | When one Postgres schema/service is the right first design |
| Caching intuition | Hot reads (book catalog) vs rare writes; cache invalidation is the hard part |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Load balancing | Round-robin / least-conn; Spring Cloud LoadBalancer + Gateway; health checks |
| Caching layers | Browser/CDN → API response cache → Redis → DB; TTL vs write-through vs cache-aside |
| Database scaling | Indexes first; read replicas; connection pools (`HikariCP`); when sharding is premature |
| Consistency models | Strong vs eventual; outbox pattern for “DB write + Kafka publish” |
| CAP theorem (practical) | Partition happens; you choose availability vs consistency trade-offs per use case |
| Message queues | Kafka topics for enrollment/notification; at-least-once; idempotent consumers |
| Backpressure & retries | Resilience4j retry/circuit breaker on Feign; dead-letter thinking |
| Capacity basics | Rough QPS, payload size, storage growth — order-of-magnitude only |

### Advanced (design depth)

| Concept | What to internalize |
|--------|----------------------|
| Multi-region awareness | Latency, data residency; usually out of scope for local builds but know the words |
| CQRS lite | Separate read models (search/list) from write models when lists get heavy |
| Saga / choreography | Enrollment + notification without distributed 2PC; compensate on failure |
| Observability in design | Logs, metrics, traces as first-class design outputs, not afterthoughts |
| Failure modes | Cascading failure, thundering herd, hot keys, poison messages |
| Security in HLD | AuthN at gateway, AuthZ in services, secrets out of images |
| Evolution paths | Monolith → modular monolith → services; design for the next step, not the final fantasy |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35–40 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 40 min Practice steps · 10 min notes · mark boxes

### Week 1 — Requirements, HLD, LLD foundations (Days 1–7)

- [ ] **Day 1 — Design checklist**
  - [ ] **Learn:** [AWS Well-Architected Framework](https://docs.aws.amazon.com/wellarchitected/latest/framework/welcome.html) (skim pillars)
  - [ ] **Practice:** One-page “how to design” checklist
    1. Create `designs/CHECKLIST.md` with sections: users, flows, scale assumptions, failure expectations, NFRs.
    2. Fill each section with 3–5 bullet prompts you will always ask.
    3. Add “draw HLD before coding APIs” and “sequence before caching” rules.
    4. Map Well-Architected pillars you care about (reliability, security, cost) in one line each.
    5. Keep the page ≤1 printed page.
    6. **Done when:** `CHECKLIST.md` exists and you can run a design session from it alone.

- [ ] **Day 2 — HLD vs LLD**
  - [ ] **Learn:** [Mermaid sequence diagrams](https://mermaid.js.org/syntax/sequenceDiagram.html) · [OpenAPI Specification](https://swagger.io/specification/)
  - [ ] **Practice:** Redraw BookStore HLD and list LLD artifacts
    1. In `designs/bookstore-hld.md`, draw Mermaid flowchart boxes: Client, Gateway, User, Catalog, Order, DBs, Kafka.
    2. List LLD artifacts under a second heading: controllers, services, entities, tables.
    3. Mark which boxes are HLD-only (no class names inside).
    4. Render in [mermaid.live](https://mermaid.live/) and fix syntax errors.
    5. Write 3 sentences: HLD answers “what talks to what”; LLD answers “how inside a service.”
    6. **Done when:** Mermaid HLD renders and LLD artifact list is attached.

- [ ] **Day 3 — Non-functional requirements**
  - [ ] **Learn:** [AWS Well-Architected](https://aws.amazon.com/architecture/well-architected/) (reliability/performance vocabulary)
  - [ ] **Practice:** Pick realistic learning-deploy NFRs for BookStore
    1. Write targets: p95 latency for browse/list, availability goal for learning (e.g. best-effort), monthly cost ceiling.
    2. State assumed users (e.g. ≤100 concurrent) — learning scale, not FAANG fantasy.
    3. List what you will **not** optimize yet.
    4. Add NFRs section to `bookstore-hld.md`.
    5. Note one trade-off (cost vs multi-AZ).
    6. **Done when:** NFR section has concrete numbers/assumptions for a learning deploy.

- [ ] **Day 4 — Sync sequence diagram**
  - [ ] **Learn:** [Mermaid live editor](https://mermaid.live/) · [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Mermaid sequence for sync place-order/enroll
    1. Create `designs/seq-place-order-sync.md` with `sequenceDiagram`.
    2. Include Client → Gateway → Order → Catalog → DB (order) actors.
    3. Show success response path and one stock-reject alt box.
    4. Render in mermaid.live; export/paste into the md file.
    5. Annotate sync boundary (what must complete before HTTP 201).
    6. **Done when:** Sync sequence Mermaid renders with happy + reject paths.

- [ ] **Day 5 — Async sequence diagram**
  - [ ] **Learn:** [Kafka intro](https://kafka.apache.org/intro) · [Mermaid sequence](https://mermaid.js.org/syntax/sequenceDiagram.html)
  - [ ] **Practice:** Same flow with Kafka notification (contrast Day 4)
    1. Create `designs/seq-place-order-async.md` sequence: Order publishes → Kafka → Notification consumer.
    2. Show HTTP returns before notification completes.
    3. Add a note on at-least-once / duplicate risk.
    4. Write a short contrast paragraph vs Day 4 sync diagram.
    5. Render both diagrams side by side in notes.
    6. **Done when:** Async Mermaid exists and contrast notes vs Day 4 are written.

- [ ] **Day 6 — API contracts**
  - [ ] **Learn:** [OpenAPI Specification](https://swagger.io/specification/)
  - [ ] **Practice:** OpenAPI-style endpoints for catalog + auth
    1. Create `designs/openapi-catalog-auth.md` (YAML or markdown tables OK).
    2. Define paths: register, login, list books, get book — methods, request bodies, status codes.
    3. Include 401/404/409 where relevant.
    4. Mark which fields are required.
    5. Keep it consistent with your Phase 5 names if possible.
    6. **Done when:** Contract doc covers catalog + auth with status codes.

- [ ] **Day 7 — Polish HLD/LLD**
  - [ ] **Learn:** Recap [Mermaid](https://mermaid.live/)
  - [ ] **Practice:** One polished HLD + one LLD class sketch
    1. Remove vague boxes (“magic service”, “engine”, “AI brain” without API).
    2. Polish HLD Mermaid: named services only, arrows labeled sync/async.
    3. LLD: class sketch for OrderService + CatalogClient + Order entity (Mermaid classDiagram or boxes).
    4. Peer-review yourself: every arrow has a protocol (HTTP/Kafka).
    5. Update `designs/README.md` index of diagrams.
    6. **Done when:** Polished HLD + LLD sketch have no vague boxes.

### Week 2 — Scale, load balancing, caching (Days 8–14)

- [ ] **Day 8 — Vertical vs horizontal**
  - [ ] **Learn:** [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/) · [Actuator endpoints](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Document 2 Boot instances behind Gateway
    1. Run catalog (or bookstore) on ports 8081 and 8082 with same app name if Eureka, or two Gateway routes/weights for learning.
    2. Hit through Gateway 10 times; log which instance answered.
    3. Write vertical vs horizontal scaling definitions in notes with your demo as horizontal.
    4. Add Mermaid: Gateway → two instances.
    5. Note session stickiness: avoid needing it (JWT stateless).
    6. **Done when:** Notes + diagram show two instances reached via Gateway.

- [ ] **Day 9 — Load balancing**
  - [ ] **Learn:** [Spring Cloud LoadBalancer](https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html) · [Gateway](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Use health as LB signal in notes/demo
    1. Skim LoadBalancer docs; note how unhealthy instances are skipped.
    2. Stop one instance; confirm traffic shifts / errors change.
    3. Point Actuator health as the readiness signal in notes (and K8s later).
    4. Write 5 bullets: LB checklist for your stack.
    5. Capture before/after instance hit counts.
    6. **Done when:** Demo notes show health-aware routing behavior.

- [ ] **Day 10 — Cache-aside**
  - [ ] **Learn:** [Spring Cache Abstraction](https://docs.spring.io/spring-framework/reference/integration/cache.html) · [Spring Data Redis](https://docs.spring.io/spring-data/redis/reference/) · [Caffeine](https://github.com/ben-manes/caffeine/wiki)
  - [ ] **Practice:** Cache book-by-id; invalidate on update
    1. Add Caffeine **or** Redis cache for `getBook(id)`.
    2. Prove second GET is faster / doesn’t hit DB (SQL log off or counter).
    3. On update/PUT, evict or update cache entry.
    4. Document cache key design (`book:{id}`).
    5. Draw cache-aside Mermaid (Client→Service→Cache→DB).
    6. **Done when:** Read cache works and update invalidates/refreshes the entry.

- [ ] **Day 11 — Stale cache lab**
  - [ ] **Learn:** [Redis docs](https://redis.io/docs/) · [Spring Cache](https://docs.spring.io/spring-framework/reference/integration/cache.html)
  - [ ] **Practice:** Reproduce stale price; fix with TTL or eviction
    1. Intentionally update price **without** eviction; show stale GET.
    2. Fix with `@CacheEvict` on update **or** TTL.
    3. Re-test: GET reflects new price.
    4. Write when TTL alone is insufficient (admin must see instant updates).
    5. Add “stale cache” to failure notes.
    6. **Done when:** Stale bug reproduced and fixed; notes explain the fix.

- [ ] **Day 12 — CDN vs API cache**
  - [ ] **Learn:** [Spring Cache](https://docs.spring.io/spring-framework/reference/integration/cache.html)
  - [ ] **Practice:** Decide CDN/static vs API cache for Learning Hub
    1. List landing-page assets that belong on CDN/static hosting.
    2. List API responses that belong in app cache (book-by-id) vs never cache (auth, cart).
    3. Table in `designs/caching-strategy.md`.
    4. One Mermaid: browser → CDN → static; browser → API → cache → DB.
    5. Note auth headers prevent shared CDN caching of private APIs.
    6. **Done when:** Caching strategy table + Mermaid exist.

- [ ] **Day 13 — Pools & indexes**
  - [ ] **Learn:** [HikariCP config](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby) · [PostgreSQL indexes](https://www.postgresql.org/docs/current/indexes.html) · [EXPLAIN](https://www.postgresql.org/docs/current/using-explain.html)
  - [ ] **Practice:** SQL logging + index note for a hot list endpoint
    1. Enable SQL logging briefly; hit list books/courses.
    2. Identify a filter/sort column used often.
    3. Write an index DDL note (`CREATE INDEX ...`) and reason.
    4. Optional: `EXPLAIN` before/after if you apply the index.
    5. Note Hikari pool size vs instance count (don’t multiply blindly).
    6. **Done when:** Hot-list index note exists from observed SQL.

- [ ] **Day 14 — 10× reads HLD**
  - [ ] **Learn:** Recap [Gateway](https://docs.spring.io/spring-cloud-gateway/reference/) · [Redis](https://redis.io/docs/)
  - [ ] **Practice:** HLD for BookStore at 10× read traffic
    1. New Mermaid HLD: extra catalog replicas + cache + Gateway.
    2. Label what scales horizontally vs what stays single (learning honesty).
    3. Call out DB as bottleneck without replicas/cache.
    4. List three measurements you’d watch (p95, hit ratio, CPU).
    5. Save as `designs/bookstore-10x-reads.md`.
    6. **Done when:** 10×-reads HLD Mermaid + metrics list are saved.

### Week 3 — Databases, CAP, distributed systems (Days 15–21)

- [ ] **Day 15 — Schema & pagination rules**
  - [ ] **Learn:** [PostgreSQL performance tips](https://www.postgresql.org/docs/current/performance-tips.html) · [Indexes](https://www.postgresql.org/docs/current/indexes.html)
  - [ ] **Practice:** Indexes, pagination, no cross-service joins
    1. For each service DB, list primary keys + 1–2 secondary indexes.
    2. Mandate pagination on list endpoints (`page`,`size`) in the design doc.
    3. Write rule: **never join across services** — fetch by id via API.
    4. Example anti-pattern: order service SQL join to books table (forbidden).
    5. Save as `designs/data-rules.md`.
    6. **Done when:** Data rules doc covers indexes, pagination, no cross-service joins.

- [ ] **Day 16 — Read replicas**
  - [ ] **Learn:** [PostgreSQL docs](https://www.postgresql.org/docs/current/) (replication awareness skim)
  - [ ] **Practice:** Note when lists could read from a replica
    1. Mark endpoints as read-heavy vs write-heavy in a table.
    2. Propose list/get on replica; place-order on primary.
    3. Note replication lag risk (stale list after admin update).
    4. If you cannot run a replica, simulate with a diagram + config sketch only.
    5. Add to HLD as optional dashed box.
    6. **Done when:** Replica read policy notes exist (diagram OK if not deployed).

- [ ] **Day 17 — CAP trade-offs**
  - [ ] **Learn:** [CAP theorem (overview)](https://en.wikipedia.org/wiki/CAP_theorem)
  - [ ] **Practice:** Map stock/order vs notification preferences
    1. Table: stock decrement / place-order → prefer consistency.
    2. Notification delivered → prefer availability / eventual consistency.
    3. Write what “partition” means in your Kafka+HTTP world (practical).
    4. One paragraph: why you won’t claim CP for the whole system.
    5. Save under `designs/cap-notes.md`.
    6. **Done when:** CAP mapping table for stock vs notification is written.

- [ ] **Day 18 — Partial failure & idempotency**
  - [ ] **Learn:** [Resilience4j CircuitBreaker](https://resilience4j.readme.io/docs/circuitbreaker) · [Kafka intro](https://kafka.apache.org/intro)
  - [ ] **Practice:** Document retries → idempotency keys
    1. List flows that retry (Feign, client refresh, Kafka redelivery).
    2. Define idempotency keys: `eventId`, `Idempotency-Key` on place-order if useful.
    3. Sequence snippet showing duplicate request safe outcome.
    4. Tie to Phase 5 consumer dedupe.
    5. Add to checklist for Capstone.
    6. **Done when:** Idempotency approach documented for HTTP and events.

- [ ] **Day 19 — Outbox / transactional messaging**
  - [ ] **Learn:** [Spring Kafka](https://docs.spring.io/spring-kafka/reference/) · [Kafka docs](https://kafka.apache.org/documentation/)
  - [ ] **Practice:** Outbox notes for order created → notify
    1. Write problem: DB commit succeeds, Kafka publish fails (or vice versa).
    2. Sketch outbox table columns + poller/publisher.
    3. Mermaid: Order tx writes order + outbox → publisher → Kafka.
    4. Mark as target pattern for Capstone if not implemented.
    5. Contrast with “publish after commit and hope.”
    6. **Done when:** Outbox sketch Mermaid + problem statement exist.

- [ ] **Day 20 — URL shortener HLD**
  - [ ] **Learn:** §6A + [Mermaid](https://mermaid.live/) · [Redis](https://redis.io/docs/)
  - [ ] **Practice:** Start URL shortener HLD (§6A)
    1. Read §6A requirements; create `designs/url-shortener-hld.md`.
    2. Mermaid components: API, DB, cache, optional analytics queue.
    3. State create vs redirect paths.
    4. Note scale assumption (learning).
    5. List open questions.
    6. **Done when:** URL shortener HLD Mermaid covers create + redirect.

- [ ] **Day 21 — URL shortener LLD**
  - [ ] **Learn:** [OpenAPI](https://swagger.io/specification/) · [PostgreSQL indexes](https://www.postgresql.org/docs/current/indexes.html)
  - [ ] **Practice:** Tables, APIs, cache keys, collisions
    1. Table design: `code` PK/unique, `long_url`, `created_at`, `owner_id` optional.
    2. APIs: `POST /shorten`, `GET /{code}` → 302.
    3. Cache key `short:{code}` → long URL; TTL policy.
    4. Collision handling: regenerate code / reject.
    5. Save LLD section under the same design file.
    6. **Done when:** LLD covers schema, APIs, cache keys, collisions.

### Week 4 — Queues + full design exercises (Days 22–28)

- [ ] **Day 22 — Message queues roles**
  - [ ] **Learn:** [Kafka documentation](https://kafka.apache.org/documentation/) · [Kafka intro](https://kafka.apache.org/intro)
  - [ ] **Practice:** Topic design for enrollment events
    1. Write roles: buffer, fan-out, decoupling — one example each.
    2. Design topics: e.g. `enrollment.created` (partitions ≥3), key=`userId` or `enrollmentId`.
    3. Mermaid: Enrollment service → topic → Notification (+ optional analytics).
    4. Note consumer group names.
    5. Save `designs/enrollment-topics.md`.
    6. **Done when:** Topic design + Mermaid fan-out exist.

- [ ] **Day 23 — Idempotent consumers**
  - [ ] **Learn:** [Spring for Apache Kafka](https://docs.spring.io/spring-kafka/reference/)
  - [ ] **Practice:** Sketch Spring Kafka consumer with processed IDs
    1. Pseudocode/`@KafkaListener` sketch storing processed event IDs.
    2. Unique constraint on `event_id`.
    3. Sequence: deliver twice → one side effect.
    4. Link to Phase 5 implementation experience.
    5. Add DLQ note for poison messages.
    6. **Done when:** Consumer sketch + uniqueness strategy documented.

- [ ] **Day 24 — Bookstore design pack**
  - [ ] **Learn:** §6B + [Gateway](https://docs.spring.io/spring-cloud-gateway/reference/) · [Resilience4j](https://resilience4j.readme.io/)
  - [ ] **Practice:** Refresh bookstore HLD/LLD with cache + queue
    1. Update bookstore HLD: services + cache + `bookstore.orders` queue.
    2. Update LLD notes for place-order + event publish.
    3. Include breaker on catalog call in the sequence.
    4. Ensure diagrams match Phase 5 reality where possible.
    5. Index file lists all bookstore diagrams.
    6. **Done when:** Bookstore pack includes services, cache, and queue coherently.

- [ ] **Day 25 — Notification design pack**
  - [ ] **Learn:** §6C + [Spring Kafka](https://docs.spring.io/spring-kafka/reference/)
  - [ ] **Practice:** Notification HLD with retries and DLQ
    1. Create `designs/notification-hld.md` Mermaid: topic → notification-service → DB/email stub.
    2. Document retry policy + DLQ topic name.
    3. APIs: list notifications, mark read.
    4. Failure modes: email provider down, duplicate events.
    5. Align with Capstone notification-service naming.
    6. **Done when:** Notification design includes retries/DLQ and APIs.

- [ ] **Day 26 — Security & observability**
  - [ ] **Learn:** [Spring Security](https://docs.spring.io/spring-security/reference/) · [Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Add security & observability to every HLD this week
    1. For URL shortener, bookstore, notification: add Security section (authn/z, secrets).
    2. Add Observability section (health, logs, metrics, traces).
    3. Ensure Gateway is the edge on diagrams that need it.
    4. Note PII in logs to avoid.
    5. Quick consistency pass across the three packs.
    6. **Done when:** All three HLDs have security + observability sections.

- [ ] **Day 27 — Shared patterns**
  - [ ] **Learn:** Your three design docs + [Well-Architected](https://docs.aws.amazon.com/wellarchitected/latest/framework/welcome.html)
  - [ ] **Practice:** Compare three designs — shared patterns list
    1. Table: pattern → where used (Gateway, cache-aside, outbox, idempotent consumer, LB).
    2. List patterns unique to one system.
    3. Write “interview story” using bookstore as primary example.
    4. Save `designs/shared-patterns.md`.
    5. Star the top 5 patterns to memorize.
    6. **Done when:** Shared patterns list covers all three design packs.

- [ ] **Day 28 — Mastery gate**
  - [ ] **Learn:** Recap §9/§10 indexes · [Mermaid](https://mermaid.js.org/syntax/sequenceDiagram.html)
  - [ ] **Practice:** Checklist dry-run; rewrite one HLD for clarity
    1. Dry-run mastery checklist; mark honest gaps.
    2. Pick the weakest HLD; rewrite Mermaid for “current you” clarity.
    3. Delete remaining vague boxes.
    4. Ensure `designs/` folder has an index README.
    5. Note what Capstone will reuse.
    6. **Done when:** Checklist reviewed and one HLD rewritten cleanly.

*If compressing to 3 weeks: merge Week 2 cache labs into Week 1 evenings and keep Week 4 design exercises intact.*

## 5. Hands-on exercises

### Design drills (paper or Mermaid)

1. Write functional + non-functional requirements for a **URL shortener** you would run for personal projects.
2. Draw HLD: client → Spring Boot → Postgres → Redis; mark sync paths.
3. LLD: `ShortUrl` entity fields; `POST /api/links`, `GET /{code}` redirect flow.
4. Estimate rough storage for 10M links (order of magnitude).
5. Add rate limiting notes at Gateway for create-link.

### Spring / Java spikes

6. Enable Actuator health/liveness; hit it as if you were a load balancer.
7. Add Caffeine or Redis cache on a catalog `findById`; measure second-hit latency subjectively.
8. Evict cache on `updateBook`; prove stale data is gone.
9. Simulate two instances (two Boot processes); call via Gateway route.
10. Publish a domain event DTO to Kafka after a successful write; consume and log.
11. Make the consumer idempotent (duplicate message → no duplicate side effect).
12. Add Resilience4j circuit breaker on a Feign call; force failure and observe open state.

### Consistency & failure

13. Document what happens if Notification service is down during enrollment (user still enrolled?).
14. Sketch compensating action if stock decrement succeeds but order row fails (transaction boundary).
15. Write a “poison message” handling note for bad Kafka payloads.

---

## 6. Mini-projects

> **Build guides:** [A URL Shortener Design](../mini-projects/phase-06-architecture-ai/20-system-design/A-url-shortener-design.md) · [B Bookstore Design](../mini-projects/phase-06-architecture-ai/20-system-design/B-bookstore-design.md) · [C Notification System Design](../mini-projects/phase-06-architecture-ai/20-system-design/C-notification-system-design.md) · [Index](../mini-projects/README.md)

### A. Design pack — URL Shortener (architecture only + optional spike)

**Goal:** Complete HLD + LLD docs you could implement later.

- Requirements: create short link, redirect, optional expiry, basic analytics count
- HLD: Spring Boot API, Postgres, Redis for hot redirects, optional async analytics via Kafka
- LLD: schema (`code` PK, `long_url`, `created_at`, `hit_count`), services, collision strategy
- Java note: `RestController` for create; redirect controller returns `302`
- Deliverable: `designs/url-shortener.md` with Mermaid diagrams

### B. Design pack — Bookstore (your domain)

- Evolve Phase 4/5 BookStore into a deliberate architecture doc
- Include: Auth, Catalog, Orders, Notification
- Show sync place-order vs async “OrderPlaced” event
- Caching: catalog reads; what you refuse to cache (stock) without careful invalidation
- Deliverable: `designs/bookstore.md`

### C. Design pack — Notification System

- Channels: email stub + in-app notification table
- API: enqueue notification; worker consumes Kafka `notification.requested`
- Retries, backoff, DLQ topic naming
- Idempotency: `(userId, templateId, dedupeKey)`
- Optional spike: Spring Boot producer + consumer logging only
- Deliverable: `designs/notification-system.md`

---

## 7. Industry use cases

- **Catalog-heavy apps:** Aggressive read caching (books, courses, product pages) with careful invalidation on admin updates
- **Checkout / enrollment:** Prefer strong consistency on inventory or seat counts; use transactions and clear ownership
- **Notifications & digests:** Queues absorb spikes; retries and DLQs keep the main API fast
- **Multi-service platforms:** Gateway for cross-cutting auth; per-service databases; events for cross-domain facts
- **Internal tools:** Often stay modular monolith + good indexes — design skill includes knowing when *not* to distribute
- **Observability-driven ops:** Health checks for load balancers; structured logs for incident replay

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Drawing fancy microservices for 10 users | Start with clear modules; split when pain is real |
| Caching mutable stock/balances casually | Cache stable reads; keep critical writes consistent |
| Ignoring invalidation | Pair every cache-put with an eviction/TTL story |
| “Exactly-once” assumptions with Kafka | Design idempotent consumers; accept at-least-once |
| Cross-service DB joins | APIs or events; duplicate read models if needed |
| CAP as trivia, not decisions | Per use case: what do you sacrifice during a partition? |
| Load balancer without health checks | Wire Actuator/probes; remove unhealthy instances |
| Giant LLD class diagrams with no APIs | Lead with endpoints + tables + sequence flows |
| No failure section in HLD | Always answer: what if X is down? |
| Premature sharding | Index, cache, replica, then reconsider |
| Hardcoding scale numbers you cannot justify | Use ranges and “learning deploy” assumptions openly |
| Treating Gateway as only a reverse proxy | Use it for routing, auth edge, rate limits, TLS termination notes |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Spring Cloud Gateway docs](https://docs.spring.io/spring-cloud-gateway/reference/) | Routing, filters, LB integration |
| [Spring Data Redis](https://docs.spring.io/spring-data/redis/reference/) | Cache-aside patterns in Spring |
| [Caffeine (local cache)](https://github.com/ben-manes/caffeine) | In-process cache before Redis |
| [HikariCP](https://github.com/brettwooldridge/HikariCP) | Pool sizing intuition |
| [Apache Kafka docs](https://kafka.apache.org/documentation/) | Topics, consumers, delivery semantics |
| [Spring for Apache Kafka](https://docs.spring.io/spring-kafka/reference/) | Listener containers, error handlers |
| [Resilience4j](https://resilience4j.readme.io/) | Circuit breaker, retry, bulkhead |
| [PostgreSQL docs — performance](https://www.postgresql.org/docs/current/performance-tips.html) | Indexes, EXPLAIN mindset |
| [AWS Well-Architected (free whitepapers)](https://aws.amazon.com/architecture/well-architected/) | Reliability/performance mental models |
| [Mermaid live editor](https://mermaid.live/) | Sequence and flowchart diagrams |
| Martin Kleppmann talks / *Designing Data-Intensive Applications* (book — buy/borrow; not required free) | Deep consistency mental models (optional) |

Prefer official Spring/Kafka/Postgres docs as source of truth; use architecture whitepapers for vocabulary, not cargo-cult patterns.

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Spring Cloud Gateway: https://docs.spring.io/spring-cloud-gateway/reference/
- Spring Cloud LoadBalancer: https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html
- Spring Cache Abstraction: https://docs.spring.io/spring-framework/reference/integration/cache.html
- Spring Data Redis: https://docs.spring.io/spring-data/redis/reference/
- Redis documentation: https://redis.io/docs/
- Caffeine: https://github.com/ben-manes/caffeine/wiki
- Spring Boot Actuator: https://docs.spring.io/spring-boot/reference/actuator/endpoints.html
- HikariCP: https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby
- PostgreSQL indexes: https://www.postgresql.org/docs/current/indexes.html
- PostgreSQL EXPLAIN: https://www.postgresql.org/docs/current/using-explain.html
- Apache Kafka intro: https://kafka.apache.org/intro
- Kafka docs: https://kafka.apache.org/documentation/
- Spring Kafka: https://docs.spring.io/spring-kafka/reference/
- Resilience4j CircuitBreaker: https://resilience4j.readme.io/docs/circuitbreaker
- Spring Cloud Circuit Breaker: https://docs.spring.io/spring-cloud-circuitbreaker/reference/
- CAP theorem background (Wikipedia overview): https://en.wikipedia.org/wiki/CAP_theorem
- AWS Well-Architected Framework: https://docs.aws.amazon.com/wellarchitected/latest/framework/welcome.html
- Mermaid sequence diagrams: https://mermaid.js.org/syntax/sequenceDiagram.html
- OpenAPI Specification: https://swagger.io/specification/

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Vocabulary | HLD/LLD, sync/async, scale types | Explain each in your own words |
| 2. Diagrams | Sequence + component diagrams for BookStore | No vague “processing” boxes |
| 3. Caching | One working cache-aside spike | Stale-data bug fixed once |
| 4. LB & health | Multi-instance mental model + health | Documented LB check path |
| 5. Data & CAP | Consistency choices per flow | Written trade-off notes |
| 6. Queues | Idempotent consumer sketch | Duplicate-safe notification path |
| 7. Design packs | URL shortener + Bookstore + Notifications | Three design docs complete |
| 8. Gate | Ready for AI integration topic | Mastery checklist ≥90% |

**Weekly rhythm:** Mon–Fri concepts + spikes; Sat design doc polish; Sun compare patterns across designs.

### Suggested personal folder

```text
designs/
  url-shortener.md
  bookstore.md
  notification-system.md
  patterns.md          # shared: cache-aside, outbox, idempotent consumer
```

---

## 12. Mastery checklist

- [ ] Can write clear functional + non-functional requirements for a small system
- [ ] Distinguishes HLD (components/flows) from LLD (APIs/classes/schemas)
- [ ] Draws sequence diagrams for sync and async variants of the same feature
- [ ] Explains vertical vs horizontal scaling with a Spring Boot example
- [ ] Places a load balancer/Gateway and health checks in an HLD correctly
- [ ] Implements or sketches cache-aside with invalidation/TTL
- [ ] Knows what not to cache without a consistency plan (e.g., stock)
- [ ] Applies indexes/pagination before “scale the database” fantasies
- [ ] Explains CAP trade-offs with two concrete BookStore/Learning Hub flows
- [ ] Designs a Kafka topic + idempotent consumer for notifications
- [ ] Documents failure modes (dependency down, duplicate events, stale cache)
- [ ] Completed design packs for URL shortener, bookstore, and notifications
- [ ] Uses Java/Spring examples (Gateway, cache, Kafka, Resilience4j) in notes
- [ ] Architecture docs include security and observability sections
- [ ] Ready to start [21-ai-integration.md](21-ai-integration.md)

---

## Quick reference — success criteria

You are done with topic 20 when you can design a system you intend to build: requirements → HLD → key LLD → failure/cache/queue decisions — and justify each Spring/infra choice in plain language. Fancy diagrams without failure modes do not count.
