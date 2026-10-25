# Mini-project C — Notification System Design Pack

**Topic:** [20 System Design](../../../phase-06-architecture-ai/20-system-design.md) · **Phase 6**

---

## Goal

Design a notification system with email stub + in-app table; enqueue API; worker consumes Kafka `notification.requested`; retries, backoff, DLQ naming; idempotency `(userId, templateId, dedupeKey)`. Optional Spring producer/consumer logging spike.

---

## Prerequisites

- Mini-project B bookstore async path
- Kafka consumer group + DLT awareness from topic 19
- Topic 20 reliability concepts

---

## Learn

| Resource | Why |
|----------|-----|
| Spring Kafka error handlers / DLT patterns | Retries & DLQ |
| Idempotent consumer design | Dedupe keys |

---

## Project layout

```text
designs/
  notification-system.md
  diagrams/
    notification-flow.mmd
  optional-spike/           # producer + consumer logging only
```

---

## Build steps

- [ ] **1. Channels**
  1. In-app notification rows; email stub (log only).
  2. **Done when:** Channel list + priority notes written.

- [ ] **2. APIs**
  1. `POST /api/notifications/enqueue` (internal/admin).
  2. `GET /api/notifications` for user; mark-read Should.
  3. **Done when:** Endpoints + auth notes listed.

- [ ] **3. Kafka topics**
  1. `notification.requested`; DLQ `notification.requested.dlq`.
  2. Payload fields including `dedupeKey`.
  3. **Done when:** Topic contract in doc.

- [ ] **4. Worker design**
  1. Consumer → persist in-app → call email stub.
  2. Retry with backoff; after N failures → DLQ.
  3. **Done when:** State machine / bullet flow written.

- [ ] **5. Idempotency**
  1. Unique constraint or upsert on `(userId, templateId, dedupeKey)`.
  2. **Done when:** Duplicate event → single row explained.

- [ ] **6. Sequence Mermaid**
  1. Enrollment/Order service → Kafka → notification-service → DB/email.
  2. **Done when:** Diagram renders.

- [ ] **7. Failure table**
  1. Broker down, poison JSON, email provider down, consumer lag.
  2. **Done when:** User-visible vs internal behaviors clear.

- [ ] **8. Optional spike**
  1. Minimal Boot producer + consumer that only logs.
  2. **Done when:** Spike README or “skipped” note.

- [ ] **9. Deliverable**
  1. `designs/notification-system.md` complete.
  2. **Done when:** Linkable from topic §6.

---

## Definition of Done

- [ ] Channels + enqueue API + Kafka worker
- [ ] Retries, backoff, DLQ naming
- [ ] Idempotency key design
- [ ] Deliverable path set

---

## Demo

Walk enqueue → Kafka → consumer → in-app row; explain what happens on duplicate `dedupeKey`.

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| No DLQ story | Name DLQ topic; define poison handling |
| Non-idempotent inserts | Dedupe constraint |
| Blocking API on email send | Async via Kafka |
| Mixing user-facing API with unbounded internal flood | Auth + rate limits notes |

---

## After this

- Implement notification service shape in Capstone Week 4
- Phase project Track A can reuse this pack
