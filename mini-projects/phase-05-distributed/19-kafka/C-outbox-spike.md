# Mini-project C — Outbox Spike (Stretch)

**Topic:** [19 Kafka](../../../phase-05-distributed/19-kafka.md) · **Phase 5**

---

## Goal

Implement the transactional **outbox** pattern in miniature: save `Order` + `Outbox` row in **one DB transaction**; a separate publisher polls the outbox and sends to Kafka—foundation for reliable events in the phase project.

---

## Prerequisites

- Mini-project A Kafka + Spring Boot
- JPA/JDBC + Postgres (Compose)
- Understanding: dual-write problem (DB commit vs Kafka send can diverge)

---

## Learn

| Resource | Why |
|----------|-----|
| Transactional outbox pattern (search reputable architecture notes) | Why dual-write fails |
| Spring `@Transactional` | Single DB commit |
| Kafka producer from poller | Relay to broker |

---

## Project layout

```text
outbox-spike/
  compose.yaml              # postgres + kafka
  order-service/
    ... Order entity, OutboxEntity, OrderService, OutboxPublisher
  README.md
  NOTES.md                  # dual-write failure mode you avoided
```

**Outbox row (example):** `id`, `event_id`, `topic`, `payload`, `created_at`, `published_at` (null until sent)

---

## Build steps

- [ ] **1. Schema**
  1. Tables `orders` and `outbox` in Postgres.
  2. **Done when:** Flyway/Liquibase/SQL script creates both.

- [ ] **2. Transactional write**
  1. `placeOrder`: insert order + insert outbox payload in `@Transactional` method.
  2. **Do not** call Kafka inside that transaction for this spike.
  3. **Done when:** Killing the process after DB commit still leaves an unpublished outbox row (test deliberately).

- [ ] **3. Publisher poller**
  1. Scheduled job / loop: `SELECT ... WHERE published_at IS NULL FOR UPDATE SKIP LOCKED` (or simple select for lab).
  2. `KafkaTemplate.send`; on success set `published_at`.
  3. **Done when:** Outbox rows clear to published after poller runs.

- [ ] **4. End-to-end**
  1. HTTP place order → row in orders + outbox → Kafka message → consumer log.
  2. **Done when:** Full path works with Compose.

- [ ] **5. Failure drill**
  1. Stop Kafka; place order → DB succeeds, outbox unpublished.
  2. Start Kafka; poller drains outbox.
  3. **Done when:** NOTES contrast this with “send Kafka then DB” failure mode.

- [ ] **6. Idempotency**
  1. Publisher and/or consumer use `event_id` to avoid dup processing.
  2. **Done when:** Re-publish attempt doesn’t break consumer (or is no-op).

- [ ] **7. README**
  1. Why outbox; how to run; limits (polling lag, at-least-once).
  2. **Done when:** Phase project can link here as reliability spike.

---

## Definition of Done

- [ ] Order + outbox in one DB transaction
- [ ] Separate publisher → Kafka
- [ ] Kafka-down drill recovers via outbox
- [ ] NOTES + README

---

## Demo

```bash
docker compose up -d
# Stop kafka container
docker compose stop kafka
curl -s -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d '{"bookId":1,"quantity":1}'
# Check outbox unpublished rows in psql
docker compose start kafka
# Wait for poller; console-consume topic
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Sending Kafka inside DB transaction and assuming atomicity | Outbox relay |
| Deleting outbox before broker ack | Mark published after send success |
| No `event_id` | Idempotent consumers still needed |
| Busy-polling without backoff | Sleep/schedule interval |

---

## After this

- Reuse outbox idea in [bookstore-microservices.md](../phase-project/bookstore-microservices.md) for `OrderPlaced`
