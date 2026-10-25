# Mini-project C — Observability Pack (Stretch)

**Topic:** [15 Microservices](../../../phase-05-distributed/15-microservices.md) · **Phase 5**

---

## Goal

Add JSON structured logs, skim Micrometer metrics, and write a 5-bullet postmortem for one failing cross-service request—so debugging microservices is a habit, not a mystery.

---

## Prerequisites

- Mini-project A (and ideally B) runnable
- Zipkin or equivalent tracing already attempted
- Actuator enabled on services

---

## Learn

| Resource | Why |
|----------|-----|
| [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html) | Health, metrics, info |
| [Micrometer](https://micrometer.io/docs) | Metrics facade |
| Structured logging (Logback JSON encoder / Logstash encoder docs) | Machine-readable logs |
| Your Zipkin UI from Mini-project A | Trace failed spans |

---

## Project layout

```text
observability-pack/
  # overlays on A/B services
  docs/
    postmortem-failing-order.md
    log-fields.md
  README.md
```

---

## Build steps

- [ ] **1. Agree log fields**
  1. Document required fields: `timestamp`, `level`, `service`, `message`, `requestId` / `traceId`, `spanId` (if present).
  2. Write them in `docs/log-fields.md`.
  3. **Done when:** Field list is committed and matches what you will emit.

- [ ] **2. JSON logging**
  1. Add Logback JSON encoder (or equivalent) to catalog + order (+ gateway if easy).
  2. Put `requestId` in MDC; include in pattern/encoder.
  3. **Done when:** One place-order produces JSON lines with `service` + `requestId`.

- [ ] **3. Metrics skim**
  1. Enable `metrics` endpoint (secure appropriately for lab).
  2. `curl` JVM + HTTP metrics; note one Feign/Resilience4j metric name if present.
  3. **Done when:** `NOTES` lists 3 metric names you actually saw.

- [ ] **4. Induce a failing request**
  1. Stop catalog or force 500 from stock-check.
  2. Capture gateway HTTP status + response body.
  3. **Done when:** Failure is reproducible on demand.

- [ ] **5. Trace the failure**
  1. Find Zipkin trace (or log correlation) for the failed call.
  2. Note which span failed and latency.
  3. **Done when:** You can point to the failing hop without guessing.

- [ ] **6. Postmortem (5 bullets)**
  1. Write `docs/postmortem-failing-order.md`: what happened, impact, root cause, detection, fix/prevention.
  2. Keep it to five bullets + optional timeline.
  3. **Done when:** Someone else could follow the postmortem to reproduce and understand.

- [ ] **7. Health honesty**
  1. Confirm `/actuator/health` shows DOWN or degraded when a critical dependency is missing (if you wire indicators).
  2. **Done when:** README mentions what health does/doesn’t cover.

- [ ] **8. Pack README**
  1. How to enable JSON logs, where metrics live, where postmortem is.
  2. **Done when:** Stretch pack is linkable from topic §6.

---

## Definition of Done

- [ ] JSON logs with documented fields
- [ ] Metrics endpoint skimmed; names noted
- [ ] One failing request traced
- [ ] 5-bullet postmortem written
- [ ] README for the pack

---

## Demo

```bash
# Trigger failure, then:
curl -s http://localhost:8082/actuator/metrics | head
curl -s http://localhost:8082/actuator/metrics/http.server.requests
# Open Zipkin; paste traceId into postmortem
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Logging secrets/tokens | Redact Authorization headers |
| Metrics without knowing names | Use `/actuator/metrics` index first |
| Postmortem as blame essay | Stick to cause + detection + fix |
| Only logging in one service | Same fields on every hop |

---

## After this

- Reuse log fields + health mindset in Docker (16), K8s probes (18), and BookStore microservices phase project
