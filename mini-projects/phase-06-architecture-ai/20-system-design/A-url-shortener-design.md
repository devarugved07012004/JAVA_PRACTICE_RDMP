# Mini-project A — URL Shortener Design Pack

**Topic:** [20 System Design](../../../phase-06-architecture-ai/20-system-design.md) · **Phase 6**

---

## Goal

Produce an architecture-only design pack (HLD + LLD) for a URL shortener you could implement later: create short link, redirect, optional expiry, basic analytics—plus Mermaid diagrams and a Java/Spring note for controllers.

---

## Prerequisites

- Topics through Phase 5 familiarity (API, Postgres, cache, optional Kafka)
- Mermaid or draw.io
- Topic 20 HLD/LLD vocabulary

---

## Learn

| Resource | Why |
|----------|-----|
| Topic 20 §3 concepts | CAP, caching, consistency |
| [Mermaid live](https://mermaid.live/) | Diagrams |
| Spring redirect (`302`) awareness | LLD realism |

---

## Project layout

```text
designs/
  url-shortener.md          # primary deliverable
  diagrams/
    hld.mmd
    redirect-sequence.mmd
```

---

## Build steps

- [ ] **1. Requirements**
  1. Must: create short URL, redirect to long URL, unique code.
  2. Should: expiry, hit count analytics.
  3. Non-goals: custom vanity domains at scale, multi-region active-active (unless noted).
  4. **Done when:** Requirements section is crisp and testable.

- [ ] **2. HLD components**
  1. Spring Boot API, Postgres, Redis for hot redirects, optional Kafka for async analytics.
  2. Mermaid component diagram.
  3. **Done when:** `hld.mmd` renders and matches prose.

- [ ] **3. API sketch**
  1. `POST /api/urls` → `{ code, shortUrl }`; `GET /{code}` → `302`.
  2. Auth notes (public create vs authenticated).
  3. **Done when:** Status codes and bodies listed.

- [ ] **4. Data model (LLD)**
  1. Table: `code` PK, `long_url`, `created_at`, `expires_at`, `hit_count`.
  2. Collision strategy (retry generate / hash truncate).
  3. **Done when:** Schema + collision section written.

- [ ] **5. Redirect path**
  1. Sequence: client → API → Redis miss → Postgres → set cache → 302.
  2. Cache TTL vs expiry field rules.
  3. **Done when:** Sequence Mermaid done.

- [ ] **6. Analytics**
  1. Sync increment vs async Kafka `url.hit` event.
  2. Trade-offs: lost counts vs latency.
  3. **Done when:** Decision recorded with rationale.

- [ ] **7. Failure modes**
  1. Redis down, DB down, duplicate code, expired link.
  2. **Done when:** Table: dependency → user-visible behavior.

- [ ] **8. Java note**
  1. `RestController` create; redirect controller returns `ResponseEntity` with `HttpStatus.FOUND`.
  2. **Done when:** Short code sketch (even pseudocode) in doc.

- [ ] **9. Optional spike**
  1. Tiny Boot app with in-memory map only—optional.
  2. **Done when:** Spike linked or explicitly skipped.

- [ ] **10. Package deliverable**
  1. Single `designs/url-shortener.md` linking diagrams.
  2. **Done when:** Ready to show as architecture pack.

---

## Definition of Done

- [ ] Requirements + HLD + LLD schema
- [ ] Mermaid diagrams
- [ ] Caching + failure section
- [ ] Java controller notes
- [ ] Deliverable path: `designs/url-shortener.md`

---

## Demo

Walk the Mermaid HLD and redirect sequence in ≤5 minutes; answer: “What if Redis is down?”

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Fancy multi-region before single-region clarity | Start simple |
| Caching without TTL/expiry story | Pair every cache with invalidation/TTL |
| No failure section | Always include |
| Diagrams without APIs | Lead with endpoints |

---

## After this

- [B-bookstore-design.md](./B-bookstore-design.md)
- Capstone-friendly patterns for caching & redirects
