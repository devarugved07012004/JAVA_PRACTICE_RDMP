# Mini-project C — Document Q&A (RAG)

**Topic:** [21 AI Integration](../../../phase-06-architecture-ai/21-ai-integration.md) · **Phase 6**

---

## Goal

Ingest markdown/PDF-text course materials; chunk → embed → vector store; `POST /api/ai/ask` returns answers grounded in retrieved chunks with source snippets; explicit behavior when no chunks found.

---

## Prerequisites

- Mini-project A (auth, client, env key)
- Vector DB choice: e.g. PGVector, Qdrant, or in-process store for learning
- Sample course markdown files

---

## Learn

| Resource | Why |
|----------|-----|
| Chosen stack RAG / embedding docs | Chunk + retrieve |
| Topic 21 RAG pitfalls | Empty hits, citations |

---

## Project layout

```text
document-qa-rag/
  materials/                    # seed markdown
  src/.../rag/
    IngestService.java
    AskController.java
    VectorStoreConfig.java
  scripts/demo-ask.sh           # or .ps1 / .http
  README.md
  eval/questions.md             # Should: 10 questions
```

---

## Build steps

- [ ] **1. Seed materials**
  1. Add 2–3 markdown docs with distinct facts (policies, module summaries).
  2. **Done when:** Files committed under `materials/`.

- [ ] **2. Ingest endpoint or CLI**
  1. `POST /api/ai/ingest` (JWT) or CLI that reads `materials/`.
  2. Store doc metadata (`docId`, optional `courseId`).
  3. **Done when:** Ingest completes without error.

- [ ] **3. Chunking**
  1. Split by headings/size with light overlap.
  2. Persist chunk text + embedding.
  3. **Done when:** Chunk count logged; sane sizes (e.g. few hundred tokens-ish).

- [ ] **4. Vector store**
  1. Wire embeddings + similarity search top-k.
  2. **Done when:** Query for a known sentence retrieves the right chunk.

- [ ] **5. `POST /api/ai/ask`**
  1. Body: `{ "question": "...", "courseId": "optional" }`.
  2. Retrieve → prompt with context → generate.
  3. Return `{ "answer", "sources": [{ "snippet", "docId" }] }`.
  4. **Done when:** Question answered only from materials returns citation.

- [ ] **6. Empty retrieval path**
  1. If no chunks above threshold: refuse or ask clarifying question—**no hallucination of facts**.
  2. **Done when:** Out-of-corpus question demonstrates safe behavior.

- [ ] **7. (Should) Filter by courseId**
  1. Restrict retrieval set.
  2. **Done when:** Cross-course leakage tested.

- [ ] **8. (Should) Re-ingest / delete**
  1. Replace doc set; old vectors gone.
  2. **Done when:** Documented commands exist.

- [ ] **9. Demo + eval**
  1. Script: ingest → ask in-corpus → ask out-of-corpus.
  2. Optional 10-question eval list.
  3. **Done when:** README demo cold-start works.

- [ ] **10. Secrets & health**
  1. Env keys; Actuator UP.
  2. **Done when:** Matches A quality bar.

---

## Definition of Done

- [ ] Ingest + chunk + embed + store
- [ ] `/api/ai/ask` with sources
- [ ] Empty-hit handling explicit
- [ ] Seed docs + demo script
- [ ] JWT-protected ingest/ask

---

## Demo

```bash
curl -s -X POST http://localhost:8080/api/ai/ingest -H "Authorization: Bearer $TOKEN"
curl -s -X POST http://localhost:8080/api/ai/ask -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"question":"What is the refund policy in module 2?","courseId":"JAVA-101"}'
curl -s -X POST http://localhost:8080/api/ai/ask -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"question":"Who won the 1998 World Cup?"}'
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| RAG without empty-hit handling | Refuse / clarify |
| Huge chunks | Tune size + overlap |
| No citations | Always return snippets |
| Docs only in chat history | Persist vectors; reboot must still work |

---

## After this

- Phase project Track B reuses this service
- Capstone AI week expands with S3 materials path
