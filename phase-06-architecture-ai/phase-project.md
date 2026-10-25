# Phase 6 Project — BookStore HLD/LLD + RAG Document Q&A Service

**Duration:** ~2–3 weeks (14–21 days) after topics 20–21 · **Cadence:** 1 hour/day  
**Outcome:** (1) A written architecture pack (HLD + key LLD) for BookStore / Learning Hub foundations, and (2) a working Java RAG document Q&A service you can demo end-to-end

---

## 1. Why this topic is important

Topics 20 and 21 teach design vocabulary and AI integration in isolation. This phase project forces both into one deliverable: **design the system you will build in the Capstone**, then **prove an AI slice works** against real course-like documents.

The HLD/LLD pack becomes the Capstone blueprint (services, data, events, AI placement). The RAG service becomes the Capstone’s “ask the materials” feature in miniature—so Capstone week 1 is evolution, not invention.

---

## 2. Prerequisites

- Topics [20](20-system-design.md) and [21](21-ai-integration.md) mastery checklists ≥90% each
- Phase 4/5 BookStore (or microservices) code available for reference
- Spring Boot 3.x, Postgres, JWT comfort
- Working LLM API access (or documented local OpenAI-compatible endpoint)
- Vector store choice ready (PGVector recommended)

**Suggested stack**

| Piece | Choice |
|-------|--------|
| Design docs | Markdown + Mermaid in `designs/` |
| Runtime | Spring Boot 3.x, JDK 17+ |
| AI | Spring AI **or** LangChain4j (pick one primary) |
| DB | PostgreSQL + PGVector (or equivalent free vector store) |
| Security | Spring Security + JWT on AI endpoints |
| Ops | Actuator health; env-based secrets |
| Build | Maven or Gradle |

---

## 3. Concepts to learn (beginner → intermediate → advanced)

This project **applies** prior topics; new learning is integration under a Capstone-shaped spec.

### Beginner (Project v1 — design + hello RAG)

| Focus | Apply |
|-------|--------|
| HLD | Boxes for Auth, Catalog, Enrollment/Orders, Notification, AI |
| LLD | Key APIs + tables for Catalog + AI ask/ingest |
| RAG hello | Ingest 3 docs; ask 3 questions |

### Intermediate (Project v2 — quality bar)

| Focus | Apply |
|-------|--------|
| Consistency notes | Sync enrollment vs async notification |
| Caching notes | What Catalog may cache |
| RAG quality | Chunking, citations, empty retrieval |
| Security | JWT on ingest/ask; user/tenant metadata |

### Advanced (Project v3 — Capstone-ready)

| Focus | Apply |
|-------|--------|
| Event sketch | `EnrollmentCreated` → notification |
| AI placement | Dedicated `ai-service` vs module inside gateway edge |
| Failure modes | Provider down, vector DB down, Gateway auth failure |
| DoD pass | Demo script + architecture matches code |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links first (10 min), then follow every numbered **Practice** step (35–45 min). Check each box when done. Notes + checklist fill the last 10 min.

**Daily template (60 min):** 10 min Learn · 40 min Practice steps · 10 min notes / mark boxes below

### Week 1 — Architecture pack (Days 1–7)

- [ ] **Day 1 — Repo layout**
  - [ ] **Learn:** [20-system-design.md](20-system-design.md) §6 · [Mermaid flowcharts](https://mermaid.js.org/syntax/flowchart.html)
  - [ ] **Practice:** Create `phase6-project/` with `designs/` and `ai-qa-service/`
    1. Create `phase6-project/designs/` and `phase6-project/ai-qa-service/` (empty dirs OK).
    2. Add `phase6-project/README.md`: Track A = design pack under `designs/`; Track B = RAG under `ai-qa-service/`.
    3. Add `phase6-project/.gitignore` covering `.env`, `target/`, `build/`, `.idea/`, `*.iml`.
    4. Stub `designs/README.md` listing planned files: `hld.md`, `seq-sync.md`, `seq-async.md`, `lld-catalog-auth.md`, `lld-ai.md`, `events.md`, `failures.md`, `capstone-handoff.md`.
    5. Under `ai-qa-service/`, note base package `com.bookstore.aiqa` (Boot init can wait until Day 8).
    6. **Done when:** Tree shows `designs/` + `ai-qa-service/` + root README; planned design file list exists.

- [ ] **Day 2 — Requirements**
  - [ ] **Learn:** [AWS Well-Architected](https://docs.aws.amazon.com/wellarchitected/latest/framework/welcome.html) (skim) · topic [20](20-system-design.md) §1–§3
  - [ ] **Practice:** Requirements doc: users, must-have flows (browse, enroll/order, notify, ask docs)
    1. Write `designs/requirements.md` with actors: student, instructor/admin.
    2. Document four must-have flows: browse catalog, enroll/order, notify, ask docs (RAG).
    3. Add out-of-scope: payments, native mobile, multi-region, agent tools.
    4. Add Phase 6 success metrics (e.g. 3 grounded asks + 1 empty-hit in demo script).
    5. Link `designs/requirements.md` from root `README.md`.
    6. **Done when:** Requirements name all four flows and an explicit out-of-scope list.

- [ ] **Day 3 — HLD component diagram**
  - [ ] **Learn:** [Mermaid live](https://mermaid.live/) · [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/)
  - [ ] **Practice:** Mermaid HLD: services + Postgres + Kafka + AI + Gateway
    1. Create `designs/hld.md` with a `flowchart TB` (or C4-style boxes) Mermaid diagram.
    2. Include nodes: `Gateway`, `Auth`, `Catalog`, `Enrollment`, `Notification`, `AI` (`ai-qa-service`), Postgres per service, `Kafka`.
    3. Label edges: sync HTTP (Client→Gateway→services) vs async Kafka (`EnrollmentCreated` → Notification).
    4. Paste source into `designs/hld.md`; render on [mermaid.live](https://mermaid.live/) and fix syntax until clean.
    5. AI box must list `POST /api/ai/ingest` and `POST /api/ai/ask` (no vague “AI brain”).
    6. **Done when:** `designs/hld.md` Mermaid renders with Gateway, Kafka, AI, and Postgres.

- [ ] **Day 4 — Sync & async sequences**
  - [ ] **Learn:** [Mermaid sequence](https://mermaid.js.org/syntax/sequenceDiagram.html) · [Kafka intro](https://kafka.apache.org/intro)
  - [ ] **Practice:** Sequence for enroll/order sync; sequence for notify async
    1. Write `designs/seq-sync.md`: `sequenceDiagram` Client→Gateway→Enrollment→Catalog (stock/seat check)→HTTP 201.
    2. Write `designs/seq-async.md`: Enrollment publishes `EnrollmentCreated` → Kafka → Notification consumer → email/log.
    3. On async diagram, show Gateway returning to Client **before** Notification finishes.
    4. Render both on mermaid.live; keep final Mermaid source in those two files.
    5. Add a short contrast paragraph at the bottom of `designs/seq-async.md` (sync consistency vs async decoupling).
    6. **Done when:** Both Mermaid sequence files render and the contrast note exists.

- [ ] **Day 5 — Catalog & Auth LLD**
  - [ ] **Learn:** [OpenAPI](https://swagger.io/specification/) · [Spring Security](https://docs.spring.io/spring-security/reference/) · [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/)
  - [ ] **Practice:** Catalog APIs + tables; Auth JWT issuance/validation notes
    1. Create `designs/lld-catalog-auth.md` with tables: `course`/`book`, `user`, ownership note (no cross-service joins).
    2. Sketch OpenAPI-style ops: `GET /api/catalog/courses`, `GET /api/catalog/courses/{id}`, `POST /api/auth/register`, `POST /api/auth/login`.
    3. Document JWT: Auth **issues** (`sub`, `roles`); Gateway + `ai-qa-service` **validate** with shared secret/`JWT_SECRET`.
    4. Note status codes: 401 missing/invalid JWT, 403 forbidden role, 404 unknown course.
    5. Align Capstone names (Auth, Catalog, Enrollment) with [enterprise-platform](../capstone/enterprise-platform.md) wording.
    6. **Done when:** `designs/lld-catalog-auth.md` has schema, API stubs, and JWT issue/validate split.

- [ ] **Day 6 — AI LLD**
  - [ ] **Learn:** [Spring AI RAG](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html) · [LangChain4j](https://docs.langchain4j.info/) · [PGVector](https://github.com/pgvector/pgvector)
  - [ ] **Practice:** Ingest/ask APIs + document/chunk tables or collections
    1. Create `designs/lld-ai.md` documenting `POST /api/ai/ingest` and `POST /api/ai/ask` request/response JSON.
    2. Design PGVector-backed storage: `document` (id, courseId, title), `chunk` (id, document_id, content, embedding `vector(N)`).
    3. Specify ask response fields: `answer`, `sources[]` (docId, snippet), empty-hit behavior when no chunks above threshold.
    4. Add Mermaid mini-flows in the same file: ingest→chunk→embed→PGVector; ask→similarity search→grounded prompt→LLM.
    5. Note planned packages: `com.bookstore.aiqa.ingest`, `.rag`, `.web`, `.security`.
    6. **Done when:** `designs/lld-ai.md` has APIs, PGVector schema, and retrieve Mermaid.

- [ ] **Day 7 — Failures & security**
  - [ ] **Learn:** [OWASP LLM Top 10](https://owasp.org/www-project-top-10-for-large-language-model-applications/) · [Spring Security](https://docs.spring.io/spring-security/reference/)
  - [ ] **Practice:** Failure & security sections; kill vague boxes
    1. Create `designs/failures.md` table: LLM provider down → 502/503 JSON; PGVector down → 503; Kafka down → enroll succeeds, notify delayed; Gateway auth fail → 401.
    2. Add Security section to `designs/hld.md`: JWT required on `/api/ai/**`; secrets via env (`OPENAI_API_KEY`, `JWT_SECRET`, `DB_*`); no PII in prompts/logs.
    3. Re-read `designs/hld.md`; rename/delete any vague “AI magic” / “misc service” boxes.
    4. Add a 3–5 row OWASP LLM risk table (mitigate vs accept) in `designs/failures.md`.
    5. Create `designs/events.md` listing at least `EnrollmentCreated` (+ optional `OrderPlaced`); update `designs/README.md` as the pack index.
    6. **Done when:** Failures + security + events docs exist and HLD has no vague boxes.

### Week 2 — RAG service build (Days 8–14)

- [ ] **Day 8 — Boot skeleton**
  - [ ] **Learn:** [Spring Boot](https://docs.spring.io/spring-boot/reference/) · [Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html) · [Spring Security](https://docs.spring.io/spring-security/reference/)
  - [ ] **Practice:** Boot skeleton + security + health; Postgres connection
    1. Generate Spring Boot 3.x under `ai-qa-service/` with base package `com.bookstore.aiqa`; deps: Web, Security, Actuator, JDBC/JPA, Postgres.
    2. Configure Datasource to local Postgres; enable Actuator; confirm `GET /actuator/health` → `{"status":"UP"}`.
    3. Add `com.bookstore.aiqa.security` JWT filter/resource-server so `/api/ai/**` requires `Authorization: Bearer <jwt>`.
    4. Prove: `curl -i http://localhost:8080/actuator/health` → 200; `curl -i http://localhost:8080/api/ai/ask` → 401 without token.
    5. Add `ai-qa-service/.env.example` with `JWT_SECRET`, `SPRING_DATASOURCE_URL`, `OPENAI_API_KEY` (never commit real `.env`).
    6. **Done when:** Secured app boots, health is UP, and AI routes return 401 without JWT.

- [ ] **Day 9 — Ingest + chunker**
  - [ ] **Learn:** [Spring AI RAG](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html) · [LangChain4j](https://docs.langchain4j.info/)
  - [ ] **Practice:** Document ingest (multipart or path-based) + chunker
    1. Implement `com.bookstore.aiqa.web.IngestController` → `POST /api/ai/ingest` (multipart file **or** server path + `courseId`).
    2. Add `com.bookstore.aiqa.ingest.Chunker` (size + overlap); persist `document` + `chunk` rows (embedding column nullable until Day 10).
    3. With JWT: `curl -H "Authorization: Bearer $TOKEN" -F "file=@docs/sample-materials/refund-policy.md" -F "courseId=course-101" http://localhost:8080/api/ai/ingest`.
    4. Reject empty files with 400; log chunk count after ingest.
    5. Verify rows with SQL (`SELECT count(*) FROM chunk WHERE document_id = …`).
    6. **Done when:** One markdown file ingests into document + chunk rows behind JWT.

- [ ] **Day 10 — Embed & store**
  - [ ] **Learn:** [OpenAI embeddings](https://platform.openai.com/docs/guides/embeddings) · [Spring AI vector DBs](https://docs.spring.io/spring-ai/reference/api/vectordbs.html) · [PGVector](https://github.com/pgvector/pgvector)
  - [ ] **Practice:** Embeddings + vector store write
    1. Enable PGVector (`CREATE EXTENSION IF NOT EXISTS vector;`); add `vector(N)` column/index on chunks (or Spring AI `PgVectorStore`).
    2. Wire embedding model in `application.yml` / env (`OPENAI_API_KEY`); place code in `com.bookstore.aiqa.rag`.
    3. Re-ingest or backfill so every Day 9 chunk has a non-null embedding.
    4. Prove similarity works (SQL `<=>` / store similarity search API) for a known chunk.
    5. Document embedding model name + dimension in `ai-qa-service/README.md`.
    6. **Done when:** Chunks have PGVector embeddings and a similarity query returns neighbors.

- [ ] **Day 11 — Ask endpoint**
  - [ ] **Learn:** [Spring AI RAG](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html)
  - [ ] **Practice:** Retrieve + ask with grounded prompt
    1. Implement `com.bookstore.aiqa.web.AskController` → `POST /api/ai/ask` with body `{ "courseId", "question" }`: retrieve → grounded system prompt → generate.
    2. Curl: `curl -s -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d "{\"courseId\":\"course-101\",\"question\":\"What is the refund policy?\"}" http://localhost:8080/api/ai/ask`.
    3. Log retrieved chunk ids (DEBUG) for bad-answer debugging.
    4. Return at least `{ "answer": "..." }` today (add `sources` on Day 12).
    5. Force a bad `OPENAI_API_KEY` briefly; confirm controlled JSON error (not stack dump).
    6. **Done when:** Authenticated ask returns a grounded answer from ingested corpus.

- [ ] **Day 12 — Citations + JWT**
  - [ ] **Learn:** [Spring Security](https://docs.spring.io/spring-security/reference/) · [OWASP LLM Top 10](https://owasp.org/www-project-top-10-for-large-language-model-applications/)
  - [ ] **Practice:** Citations + empty-hit behavior; JWT required on routes
    1. Extend ask JSON with `sources: [{ "documentId", "snippet" }]`.
    2. Empty retrieval → stable message (e.g. “No relevant materials found”) and **no** invented facts.
    3. Confirm JWT required on both ingest and ask (and chat if present); `/actuator/health` stays open.
    4. Three curls: in-corpus (answer + sources); out-of-corpus (empty-hit); no `Authorization` → 401.
    5. Paste the three curls into `ai-qa-service/README.md`.
    6. **Done when:** Citations, empty-hit, and JWT 401 checks all pass.

- [ ] **Day 13 — Seed + demo script**
  - [ ] **Learn:** [21-ai-integration.md](21-ai-integration.md) §6C
  - [ ] **Practice:** Seed 5–10 course/bookstore markdown docs; curl demo script
    1. Add 5–10 `.md` files under `ai-qa-service/docs/sample-materials/` (policies, module notes, bookstore FAQs).
    2. Ingest all with JWT + `courseId`; record document/chunk counts in notes.
    3. Write `ai-qa-service/scripts/demo-ask.sh` **or** `demo-ask.ps1`: health → 2 in-corpus asks → 1 out-of-corpus ask.
    4. Run the script end-to-end; document expected behaviors in README.
    5. If retrieval flaky, tune chunk size / top-k and re-run until stable.
    6. **Done when:** Demo script completes with 2 hits + 1 empty-hit without manual edits mid-run.

- [ ] **Day 14 — Align design with code**
  - [ ] **Learn:** Your `designs/hld.md` + [Mermaid](https://mermaid.live/)
  - [ ] **Practice:** README; align design doc “AI” box with what you built
    1. Update `designs/hld.md` AI box: real port, `/api/ai/ingest`, `/api/ai/ask`, PGVector dependency.
    2. Diff `designs/lld-ai.md` vs actual schema/packages; fix drift (`com.bookstore.aiqa.*`).
    3. Root `README.md`: how to run `ai-qa-service` + links to every file under `designs/`.
    4. Remove aspirational HLD features not built (agents, multi-tenant SaaS, etc.).
    5. Skim Week 2 DoD items in §12; punch-list only (fixes tomorrow if needed).
    6. **Done when:** Design docs match running endpoints, packages, and PGVector schema.

### Week 3 — Harden & Capstone handoff (Days 15–21)

- [ ] **Day 15 — Tenant filters**
  - [ ] **Learn:** [Spring AI vector DBs](https://docs.spring.io/spring-ai/reference/api/vectordbs.html) · [PGVector](https://github.com/pgvector/pgvector)
  - [ ] **Practice:** Filter by `courseId`/`docSetId`; negative test (wrong course)
    1. Enforce metadata filter on PGVector retrieve (`courseId` or `docSetId` required on ask).
    2. Seed `course-101` and `course-202` materials; ask `course-101` a question only answered in `course-202` → empty-hit.
    3. Ask `course-101` about its own material → hit + sources.
    4. Document the filter field in `designs/lld-ai.md` OpenAPI notes.
    5. Add both curls to `scripts/demo-ask.sh` / `.ps1`.
    6. **Done when:** Wrong-course ask cannot leak the other course’s chunks.

- [ ] **Day 16 — Provider errors**
  - [ ] **Learn:** [Resilience4j](https://resilience4j.readme.io/) · [Spring AI](https://docs.spring.io/spring-ai/reference/)
  - [ ] **Practice:** Provider timeout/error handling polish
    1. Set invalid `OPENAI_API_KEY` (or block network); `POST /api/ai/ask` returns JSON 502/503 (not HTML/stack).
    2. Configure client timeout so the request fails in seconds, not hangs.
    3. Distinguish empty-RAG vs provider-error in response `code`/`message` if practical.
    4. Add/update the provider-down row in `designs/failures.md`.
    5. Restore key; re-run one in-corpus ask successfully.
    6. **Done when:** Provider failure is clean JSON and happy path works again.

- [ ] **Day 17 — Optional chatbot**
  - [ ] **Learn:** [LangChain4j](https://docs.langchain4j.info/) · [Spring AI](https://docs.spring.io/spring-ai/reference/)
  - [ ] **Practice:** Thin chatbot session endpoint **or** skip if timeboxed
    1. If building: `com.bookstore.aiqa.web.ChatController` → `POST /api/ai/chat` with `{ sessionId, message }`, JWT, short memory.
    2. Demo 2-turn curl (second turn references first); document in README.
    3. If skipping: write `ai-qa-service/docs/SKIPPED-chatbot.md` with reason + Capstone follow-up plan.
    4. Do not break ingest/ask while experimenting.
    5. Update `designs/hld.md` AI box **only** if chat shipped.
    6. **Done when:** Chat works with JWT **or** an explicit skip note is filed.

- [ ] **Day 18 — Capstone mapping**
  - [ ] **Learn:** [Capstone enterprise-platform](../capstone/enterprise-platform.md) §3–§4 (skim)
  - [ ] **Practice:** Which Capstone weeks reuse which diagrams (`designs/capstone-handoff.md`)
    1. Create `designs/capstone-handoff.md` with a table: Capstone week → reuse `hld.md` / `seq-*.md` / `lld-*.md` / `events.md`.
    2. State AI placement decision: dedicated `ai-qa-service` vs module (pick one for Capstone v1).
    3. List packages to copy: `com.bookstore.aiqa.web`, `.rag`, `.ingest`, `.security`.
    4. List Capstone gaps: Enrollment, Notification consumer, Gateway routes `/api/ai/**`, K8s.
    5. Link `designs/capstone-handoff.md` from root README and from Capstone notes if you keep any.
    6. **Done when:** `designs/capstone-handoff.md` maps diagrams + packages to Capstone weeks.

- [ ] **Day 19 — Definition of Done**
  - [ ] **Learn:** §12 in this file
  - [ ] **Practice:** Full DoD pass; fix gaps
    1. Walk every §12 Must checkbox against live `ai-qa-service` + `designs/`.
    2. Fix must-haves only: JWT, citations, empty-hit, HLD names, PGVector persistence across reboot.
    3. Re-run `scripts/demo-ask.sh` / `.ps1` after fixes.
    4. Record pass/fail next to each DoD item in notes (or a short `designs/dod-status.md`).
    5. No new features today.
    6. **Done when:** All Must DoD items in §12 pass.

- [ ] **Day 20 — Demo script run**
  - [ ] **Learn:** §6 sample demo flow in this file
  - [ ] **Practice:** Record/write demo run (questions + expected behavior)
    1. Execute §6 flow: obtain JWT → ingest (if needed) → ask hit → ask miss → `GET /actuator/health`.
    2. Write `ai-qa-service/docs/demo-run.md` with exact questions and expected answer/empty-hit behavior.
    3. Time the rehearsal (target ≤10 minutes cold start excluded).
    4. Fix script flakiness (token export, ports, courseIds).
    5. Store redacted sample JSON outputs beside the script notes (no secrets).
    6. **Done when:** `docs/demo-run.md` matches a successful timed rehearsal.

- [ ] **Day 21 — Freeze for Capstone**
  - [ ] **Learn:** Recap [20](20-system-design.md) · [21](21-ai-integration.md) checklists
  - [ ] **Practice:** Retrospective + sign-off; freeze design pack for Capstone
    1. Write a short keep/stop/start retrospective in `designs/retro.md` (or root notes).
    2. Sign off completed Phase 6 / topics 20–21 checklist items you actually finished.
    3. Freeze snapshot: tag or zip `designs/` + `ai-qa-service/` as `phase-6-done`.
    4. Re-read `designs/capstone-handoff.md`; fix anything stale before Capstone week 1.
    5. List non-blocking debts (streaming, agents, chatbot if skipped) explicitly as Capstone-optional.
    6. **Done when:** Sign-off exists and the design/RAG freeze is ready for Capstone.

*On a 2-week pace: finish architecture by Day 5; RAG DoD by Day 14; fold Week 3 into a single harden/demo day.*

---

## 5. Hands-on exercises

Use these as spikes before or during the project:

1. Redraw Phase 5 BookStore services into Capstone-named boxes (Auth, Catalog, Enrollment, Notification, AI).
2. Write OpenAPI-style stubs for `POST /api/ai/ingest` and `POST /api/ai/ask`.
3. Prove JWT rejection on AI routes (401 without token).
4. Ingest one file; SQL/vector query shows rows/embeddings exist.
5. Ask an in-corpus question; confirm answer uses a cited snippet.
6. Ask an out-of-corpus question; confirm non-hallucinated refusal/fallback.
7. Kill API key / wrong base URL; confirm controlled 502/503-style API error.
8. Duplicate ingest of same doc id — define overwrite vs reject behavior.
9. Load test lightly with 5 sequential asks; note latency for README “known limits.”
10. Cross-check HLD Kafka topics against a short topic list in `designs/events.md`.

---

## 6. Mini-projects

> **Build guide:** [Bookstore HLD + RAG](../mini-projects/phase-06-architecture-ai/phase-project/bookstore-hld-rag.md) · [Index](../mini-projects/README.md)

The phase project **is** the deliverable, shipped as two tracks:

### Track A — Architecture specification (Must)

**BookStore / Learning Hub foundations**

- [Must] HLD component diagram (Mermaid or equivalent)
- [Must] Sequence diagrams: sync write path + async notification path
- [Must] Service list with responsibilities (Auth, Catalog, Enrollment/Orders, Notification, AI, Gateway)
- [Must] Data ownership: which DB/schema per service (no cross-service joins)
- [Must] Kafka topic list (at least enrollment/order + notification)
- [Must] Caching decision notes for Catalog
- [Must] Security: JWT at Gateway; service-to-service notes
- [Must] Failure modes table (dependency → user-visible behavior)
- [Should] Capacity assumptions labeled as “learning deploy”
- [Should] LLD class/package sketch for AI service and Catalog
- [Could] C4-style containers diagram

### Track B — RAG Document Q&A service (Must)

- [Must] Spring Boot app with JWT-protected ingest + ask
- [Must] Chunk → embed → vector store → retrieve → generate
- [Must] Returns answer + source snippets
- [Must] Empty retrieval handled explicitly
- [Must] Seed documents + demo script in README
- [Must] Secrets via env; Actuator health
- [Should] `docSetId` / `courseId` filter
- [Should] Basic eval list (10 questions)
- [Could] Thin chatbot memory endpoint

### Suggested layout

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
    scripts/demo-ask.sh   # or .http / PowerShell
```

### Sample demo flow

```text
1. Obtain JWT (reuse Auth from prior phase or a stub issuer for local lab)
2. POST /api/ai/ingest  (course materials)
3. POST /api/ai/ask     “What is the refund policy in module 2?” → answer + sources
4. POST /api/ai/ask     “Who won the 1998 World Cup?” → out-of-corpus behavior
5. GET  /actuator/health → UP
```

---

## 7. Industry use cases

- **Architecture packs before large builds:** Teams write HLD/LLD to align services, events, and ownership before coding for months
- **Doc Q&A over product manuals:** Same RAG shape as internal help centers
- **Learning products:** Students ask questions against course PDFs/markdown
- **Bookstore content ops:** Ask against shipping policies / author guidelines stored as docs
- **Capstone acceleration:** Reusing a real design + AI slice beats greenfield guessing

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Beautiful HLD that ignores Capstone services | Name Capstone services explicitly now |
| RAG demo with docs only in chat history | Persist vectors; reboot must still answer |
| No citations | Always return snippets for trust/debug |
| Design doc not updated after code changes | Day 14 alignment pass is mandatory |
| AI endpoints left `permitAll` | JWT from day one of the service |
| Vague “AI brain” box | Ingest, retrieve, generate, tools — separate concerns |
| Skipping failure modes | Capstone will hit provider and network failures |
| Overbuilding agents in phase project | One RAG ask path is enough; agents can wait for Capstone |
| Mixing Catalog DB tables into AI service | Own document/chunk store; call Catalog via API if needed |
| Undocumented env vars | README table: `OPENAI_API_KEY`, DB URL, JWT secret |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [20-system-design.md](20-system-design.md) | HLD/LLD patterns and design packs |
| [21-ai-integration.md](21-ai-integration.md) | RAG, Spring AI / LangChain4j |
| [Spring AI reference](https://docs.spring.io/spring-ai/reference/) | Vector stores, chat models |
| [LangChain4j docs](https://docs.langchain4j.info/) | RAG / AiServices |
| [PGVector](https://github.com/pgvector/pgvector) | Postgres vectors |
| [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/) | Edge placement notes |
| [Apache Kafka docs](https://kafka.apache.org/documentation/) | Event list realism |
| [Mermaid live](https://mermaid.live/) | Diagrams in markdown |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Spring AI: https://docs.spring.io/spring-ai/reference/
- Spring AI RAG: https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html
- LangChain4j: https://docs.langchain4j.info/
- PGVector: https://github.com/pgvector/pgvector
- Spring Security: https://docs.spring.io/spring-security/reference/
- Spring Boot Actuator: https://docs.spring.io/spring-boot/reference/actuator/endpoints.html
- Spring Data JPA: https://docs.spring.io/spring-data/jpa/reference/
- Kafka: https://kafka.apache.org/documentation/
- Spring Kafka: https://docs.spring.io/spring-kafka/reference/
- Spring Cloud Gateway: https://docs.spring.io/spring-cloud-gateway/reference/
- Mermaid flowcharts: https://mermaid.js.org/syntax/flowchart.html
- Mermaid sequence: https://mermaid.js.org/syntax/sequenceDiagram.html
- OpenAPI: https://swagger.io/specification/
- OWASP LLM Top 10: https://owasp.org/www-project-top-10-for-large-language-model-applications/

---

## 11. Practice roadmap

### Evolution path

```text
v1 HLD + hello RAG
  → v2 Citations, JWT, failure notes, Catalog LLD
    → v3 Capstone mapping + DoD + demo script freeze
```

### Week plan (summary)

| Week | Deliverable |
|------|-------------|
| 1 | Architecture pack (HLD/LLD/events/failures) |
| 2 | Working JWT-secured RAG Q&A with seed docs |
| 3 | Harden, demo script, Capstone handoff notes |

### Definition of architecture notes for Capstone (Day 18)

Capture in `designs/capstone-handoff.md`:

1. Which Capstone week implements which HLD box?
2. Does AI run as its own service or a module first?
3. Which Kafka events are Must vs Should?
4. What is explicitly out of scope for Capstone v1?

### Definition of Done (summary)

See §12. Short version: **diagrams match Capstone names**, **RAG demo works offline-reproducibly with README**, **failure modes written**.

---

## 12. Mastery checklist

### Definition of Done (project)

- [ ] `designs/hld.md` includes component diagram with Auth, Catalog, Enrollment/Orders, Notification, AI, Gateway, Postgres, Kafka
- [ ] Sync + async sequence diagrams exist and are readable
- [ ] Data ownership per service documented (no shared write DB across services)
- [ ] Kafka topics listed for enrollment/order and notification
- [ ] Caching and consistency decisions written for Catalog vs stock/seats
- [ ] Failure modes table covers AI provider, vector DB, notification consumer, Gateway auth
- [ ] `ai-qa-service` runs with documented env vars
- [ ] Ingest + ask work with JWT
- [ ] Answers include source snippets; empty retrieval is safe
- [ ] Seed materials + demo script produce a repeatable demo
- [ ] Actuator health works
- [ ] Capstone handoff notes map designs → build weeks
- [ ] README explains how to run design review + RAG demo

### Personal mastery

- [ ] Can walk through HLD box-by-box without notes
- [ ] Can explain RAG pipeline stages and where they live in code
- [ ] Can debug a bad answer by inspecting retrieved chunks
- [ ] Topics 20–21 checklists remain complete
- [ ] Ready to start Capstone — [../capstone/enterprise-platform.md](../capstone/enterprise-platform.md)

---

## Quick reference — success criteria

You are done with Phase 6 when a reviewer can (1) understand your Capstone-shaped architecture from the design pack alone, and (2) run the RAG service and get grounded answers from your sample materials—with clear behavior when the model or retrieval fails. Do not start Capstone until both tracks meet Definition of Done.
