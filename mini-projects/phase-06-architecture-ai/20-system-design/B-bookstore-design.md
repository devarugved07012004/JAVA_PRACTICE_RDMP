# Mini-project B — Bookstore Design Pack

**Topic:** [20 System Design](../../../phase-06-architecture-ai/20-system-design.md) · **Phase 6**

---

## Goal

Evolve Phase 4/5 BookStore into a deliberate architecture document: Auth, Catalog, Orders, Notification; sync place-order vs async `OrderPlaced`; caching rules for catalog vs stock.

---

## Prerequisites

- Phase 5 BookStore microservices experience (or monolith + event notes)
- Mini-project A design habit
- Topic 20 consistency/caching sections

---

## Learn

| Resource | Why |
|----------|-----|
| Phase 5 phase-project architecture | Ground truth services |
| Kafka at-least-once + idempotency | Async path |
| Cache-aside patterns | Catalog reads |

---

## Project layout

```text
designs/
  bookstore.md
  diagrams/
    bookstore-hld.mmd
    place-order-sync.mmd
    order-placed-async.mmd
```

---

## Build steps

- [ ] **1. Context & goals**
  1. Actors: shopper, admin; learning-scale assumptions.
  2. **Done when:** Scope and non-goals listed (no payments processor, etc.).

- [ ] **2. Service list**
  1. Auth/User, Catalog, Orders, Notification (+ Gateway).
  2. Data ownership per service (no cross-service joins).
  3. **Done when:** Ownership table complete.

- [ ] **3. HLD Mermaid**
  1. Components + Kafka topic `order-placed`.
  2. **Done when:** Diagram matches service list.

- [ ] **4. Sync place-order sequence**
  1. Gateway → Order → Catalog stock check → Order DB commit → response.
  2. Failure if insufficient stock.
  3. **Done when:** Sequence diagram done.

- [ ] **5. Async notification path**
  1. After commit → `OrderPlaced` → Notification consumer → in-app/email stub.
  2. Idempotency key notes.
  3. **Done when:** Async Mermaid + delivery semantics called out (at-least-once).

- [ ] **6. Caching decisions**
  1. Cache: book metadata list/detail.
  2. Refuse casual cache of live stock without invalidation design.
  3. **Done when:** “Cache / don’t cache” table written.

- [ ] **7. Security**
  1. JWT at Gateway; admin catalog writes; order ownership.
  2. **Done when:** Security section maps to endpoints.

- [ ] **8. Failure modes**
  1. Catalog down, Kafka down, notification lag, DB down per service.
  2. **Done when:** Table complete.

- [ ] **9. Capacity honesty**
  1. Label numbers as “learning deploy” if used.
  2. **Done when:** No fake FAANG-scale claims.

- [ ] **10. Deliverable polish**
  1. `designs/bookstore.md` links all diagrams; points to Capstone evolution.
  2. **Done when:** Pack is reviewable standalone.

---

## Definition of Done

- [ ] Auth, Catalog, Orders, Notification covered
- [ ] Sync vs async paths diagrammed
- [ ] Caching decisions explicit
- [ ] Deliverable: `designs/bookstore.md`

---

## Demo

Narrate place-order sync path, then how Notification learns without Order calling it over HTTP.

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Caching stock casually | Strong consistency on inventory writes |
| Shared DB in the design | Per-service data ownership |
| Exactly-once assumptions | Idempotent consumers |
| Diagram drift from Phase 5 code | Align names with what you built |

---

## After this

- [C-notification-system-design.md](./C-notification-system-design.md)
- Feed into [bookstore-hld-rag.md](../phase-project/bookstore-hld-rag.md)
