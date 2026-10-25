# Phase project — BookStore / Learning Hub HLD + RAG Q&A

**Topic:** [Phase 6 project](../../../phase-06-architecture-ai/phase-project.md) · **Phase 6**

---

## Goal

Ship two tracks: **(A)** architecture specification for BookStore/Learning Hub foundations, and **(B)** a JWT-protected RAG Document Q&A service with citations and empty-hit handling—ready to accelerate the Capstone.

---

## Prerequisites

- Topic 20 design packs A–C (or equivalent depth)
- Topic 21 mini-projects A–C
- Phase 5 service names familiarity

---

## Learn

Phase-project.md §6 Must lists; Spring AI / LangChain4j RAG docs; Mermaid for HLD/LLD.

---

## Project layout

```text
phase6-project/
  designs/
    hld.md
    lld-catalog-ai.md
    events.md
    failures.md
  ai-qa-service/
    src/...
    README.md
    docs/sample-materials/
    scripts/demo-ask.sh
  README.md
```

---

## Build steps

### Track A — Architecture

- [ ] **1. HLD component diagram**
  1. Auth, Catalog, Enrollment/Orders, Notification, AI, Gateway + Kafka.
  2. **Done when:** Mermaid in `designs/hld.md`.

- [ ] **2. Sequence diagrams**
  1. Sync write path (enroll/order).
  2. Async notification path.
  3. **Done when:** Both in `designs/` or `hld.md`.

- [ ] **3. Data ownership**
  1. Which DB per service; no cross-service joins.
  2. **Done when:** Table committed.

- [ ] **4. Kafka topics**
  1. At least enrollment/order + notification topics named.
  2. **Done when:** `events.md` lists contracts.

- [ ] **5. Caching + security**
  1. Catalog cache decisions; JWT at Gateway; service-to-service notes.
  2. **Done when:** Written explicitly.

- [ ] **6. Failure modes**
  1. Dependency → user-visible behavior table.
  2. **Done when:** `failures.md` complete.

- [ ] **7. (Should) LLD sketch**
  1. Packages/classes for AI + Catalog.
  2. **Done when:** `lld-catalog-ai.md` exists.

### Track B — RAG service

- [ ] **8. Service skeleton**
  1. Boot app; JWT on ingest + ask; env secrets; Actuator.
  2. **Done when:** Health UP; unauthorized rejected.

- [ ] **9. Ingest pipeline**
  1. Sample materials under `docs/sample-materials/`.
  2. Chunk → embed → vector store.
  3. **Done when:** Reboot still answers (persisted store).

- [ ] **10. Ask + citations**
  1. Answer + source snippets; empty retrieval explicit.
  2. **Done when:** In-corpus + out-of-corpus demos pass.

- [ ] **11. (Should) courseId / docSetId filter + eval list**
  1. Ten questions in `eval/`.
  2. **Done when:** Filter tested or deferred honestly.

- [ ] **12. Root README + demo script**
  1. Mirror phase-project sample demo flow.
  2. **Done when:** Clean clone demo ≤ documented steps.

---

## Definition of Done

- [ ] Track A Must items from phase-project.md
- [ ] Track B Must items (ingest, ask, citations, empty-hit, seed, secrets, health)
- [ ] Layout matches suggested structure
- [ ] Known limits listed for Capstone

---

## Demo

```text
1. Obtain JWT
2. POST /api/ai/ingest
3. POST /api/ai/ask  → grounded answer + sources
4. POST /api/ai/ask  → out-of-corpus behavior
5. GET  /actuator/health → UP
6. Walk designs/hld.md against Capstone service names
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Beautiful HLD ignoring Capstone services | Name Capstone services now |
| RAG only in chat memory | Persist vectors |
| No citations | Always return snippets |
| Scope explosion into full Capstone | Stop at phase DoD |

---

## After this

- Capstone: [enterprise-learning-hub-steps.md](../../capstone/enterprise-learning-hub-steps.md)
- Spec: [enterprise-platform.md](../../../capstone/enterprise-platform.md)
