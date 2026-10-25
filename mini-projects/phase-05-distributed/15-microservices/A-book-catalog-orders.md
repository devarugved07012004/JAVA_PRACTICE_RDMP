# Mini-project A — Multi-Service Book Catalog + Orders

**Topic:** [15 Microservices](../../../phase-05-distributed/15-microservices.md) · **Phase 5**

---

## Goal

Run a four-piece Spring Cloud stack: Eureka, API Gateway, `catalog-service`, and `order-service`. Place an order through the gateway; order checks stock via Feign with Resilience4j; prove discovery, correlation IDs, and at least one Zipkin trace.

---

## Prerequisites

- Phase 4 BookStore / Spring Boot + JPA comfort
- JDK 17+, Maven or Gradle
- Topic 15 Weeks 1–4 concepts (Eureka, Gateway, Feign, Resilience4j)
- Optional: Zipkin via Docker (`docker run -d -p 9411:9411 openzipkin/zipkin`)

---

## Learn

| Resource | Why |
|----------|-----|
| [Spring Cloud Netflix Eureka](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html) | Registry |
| [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/) | Edge routes + `lb://` |
| [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/) | Declarative catalog client |
| [Resilience4j](https://resilience4j.readme.io/) | Timeout + circuit breaker |
| [Micrometer Tracing / Zipkin](https://docs.spring.io/spring-boot/reference/actuator/tracing.html) | Cross-service traces |

---

## Project layout

```text
book-catalog-orders/
  eureka-server/
  api-gateway/
  catalog-service/          # Books CRUD; stock check endpoint
  order-service/            # Place order → Feign → catalog
  NOTES.md                  # ports, start order, screenshots
  README.md
```

**Suggested ports:** Eureka `8761` · Gateway `8080` · Catalog `8081` · Order `8082` · Zipkin `9411`

---

## Build steps

- [ ] **1. Scaffold modules**
  1. Create four Spring Boot apps (same Spring Cloud BOM).
  2. Add parent README listing modules and ports.
  3. **Done when:** Each module builds with `mvn -q -DskipTests package` (or Gradle equivalent).

- [ ] **2. Eureka server**
  1. `@EnableEurekaServer`; disable self-registration in `application.yml`.
  2. Start and open `http://localhost:8761`.
  3. **Done when:** Dashboard loads empty (no clients yet).

- [ ] **3. Catalog service**
  1. `@EnableDiscoveryClient`; `spring.application.name=catalog-service`.
  2. In-memory or H2/Postgres books; `GET /books`, `GET /books/{id}`, `POST /books/{id}/stock-check` (or reserve).
  3. Register with Eureka; wait until instance shows UP.
  4. **Done when:** Direct `curl http://localhost:8081/books` works and Eureka lists the service.

- [ ] **4. Order service + Feign**
  1. `@EnableFeignClients`; Feign interface to `catalog-service` using service id (not hard-coded host in discovery mode).
  2. `POST /orders` creates order after successful stock check.
  3. Register with Eureka.
  4. **Done when:** Calling order with catalog up returns `201` and decrements/validates stock.

- [ ] **5. Resilience4j on Feign**
  1. Configure connect/read timeouts and circuit breaker around catalog calls.
  2. Fallback or controlled error body when catalog is down.
  3. **Done when:** Stop catalog → `POST /orders` fails fast with documented response (no hang).

- [ ] **6. API Gateway**
  1. Routes: `/api/catalog/**` → `lb://catalog-service`, `/api/orders/**` → `lb://order-service` (rewrite as needed).
  2. Demo **only** through gateway port `8080`.
  3. **Done when:** `curl http://localhost:8080/api/catalog/books` and place-order via gateway succeed.

- [ ] **7. Correlation ID**
  1. Gateway or filter sets/propagates `X-Request-Id` (generate if missing).
  2. Both services log the id on place-order path.
  3. **Done when:** One request id appears in gateway + catalog + order logs for the same call.

- [ ] **8. Zipkin trace**
  1. Add Micrometer tracing + Zipkin reporter (or Brave) to gateway + services.
  2. Place one order; open Zipkin UI; find the trace spanning gateway → order → catalog.
  3. Save screenshot or paste `traceId` in `NOTES.md`.
  4. **Done when:** At least one multi-span trace is captured and noted.

- [ ] **9. README runbook**
  1. Document start order: Eureka → Catalog → Order → Gateway (Zipkin optional first).
  2. Sample curls for list books + place order.
  3. **Done when:** A clean terminal session following README reproduces the happy path.

- [ ] **10. (Should) Config Server hook**
  1. Optional shared non-secret property (e.g. `bookstore.feature.banner`).
  2. Note “full Config lab is Mini-project B.”
  3. **Done when:** Either wired lightly or explicitly deferred to B in README.

---

## Definition of Done

- [ ] Eureka shows catalog + order (+ gateway if registered)
- [ ] Gateway is the only public HTTP entry used in demos
- [ ] Place-order → Feign → catalog with Resilience4j timeout + breaker
- [ ] Correlation ID in logs across the hop
- [ ] Zipkin (or equivalent) trace captured in notes
- [ ] Personal README with ports and start order

---

## Demo

```bash
# Terminals: eureka, catalog, order, gateway (Zipkin if used)
curl -s http://localhost:8080/api/catalog/books | jq .
curl -s -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"quantity":1}' | jq .
# Stop catalog; repeat POST — expect controlled failure
# Zipkin: http://localhost:9411 — search recent traces
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Gateway calls `localhost:8081` | Use `lb://catalog-service` with discovery |
| Feign without timeouts | Always set connect/read + Resilience4j |
| Demo before Eureka registration settles | Wait for instances UP; retry |
| Shared DB for “services” | Separate schemas/stores for learning |
| No request id | Filter + MDC; log on every hop |

---

## After this

- Complete [B-config-resilience-lab.md](./B-config-resilience-lab.md)
- Stretch: [C-observability-pack.md](./C-observability-pack.md)
- Later: Dockerize this stack in topic 16 Compose preview
