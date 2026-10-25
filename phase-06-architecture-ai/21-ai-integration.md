# 21 — AI Integration with Java

**Duration:** ~4 weeks (28 days) · **Cadence:** 1 hour/day  
**Focus:** OpenAI-compatible APIs, Spring AI, LangChain4j, prompt engineering, RAG, embeddings, vector databases, chatbots, assistants, document search, agents, and enterprise AI features — build Spring Boot AI apps you can demo

---

## 1. Why this topic is important

AI features in enterprise apps are rarely “call a model and hope.” You need **safe prompts**, **timeouts**, **retrieval** over your own documents, and **clear UX** when the model is wrong or the API is down. Java/Spring is where your Auth, Catalog, and Enrollment already live—so integrating AI there means assistants that respect the same security and data boundaries as the rest of the platform.

This topic trains practical integration: embeddings → vector store → retrieve → generate (RAG), chat endpoints with conversation memory, and lightweight “agent” tool calling for lookup tasks. Everything is framed as product features for a Learning Hub / BookStore—not as research ML.

---

## 2. Prerequisites

- Topic [20](20-system-design.md) in progress or complete (you can start AI spikes in parallel with Week 4 design polish if needed, but prefer finishing 20’s design packs first)
- Spring Boot 3.x comfort: controllers, config, `application.yml`, Actuator
- HTTP client literacy; JWT-secured APIs from Phase 4/5
- Postgres available; willingness to run a local vector store (e.g. PGVector) or an embedded option for learning
- API key for an LLM provider that offers a free/low-cost tier (OpenAI, or OpenAI-compatible local/runtime alternatives you configure yourself)

**Environment**

- JDK 17+ · Maven or Gradle
- Spring Boot 3.x project (new `ai-lab` or module in Learning Hub)
- Optional: Docker for Postgres + PGVector
- Never commit API keys; use env vars / `.env` ignored by Git

**Cost & safety note:** Use small models and short prompts while learning. Cap max tokens. Do not send secrets or personal data of others into prompts.

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| LLM as an API | Request messages in → completion out; latency, tokens, rate limits |
| Chat vs completion | System / user / assistant roles |
| OpenAI API shape | Chat Completions (or Responses) HTTP contract; SDKs wrap the same ideas |
| Spring AI overview | Starters, `ChatClient` / chat model beans, portable abstractions |
| LangChain4j overview | AiServices, chat memory, embeddings, RAG helpers in Java |
| Prompt engineering basics | Clear instructions, constraints, output format, few-shot examples |
| Temperature & tokens | Creativity vs determinism; max tokens as cost/latency control |
| Failures | Timeouts, 429s, empty responses — design user-visible fallbacks |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Embeddings | Vectors representing meaning; similarity search ≠ keyword search alone |
| Vector databases | Store embeddings + metadata; top-k retrieval (PGVector, other free options) |
| RAG pipeline | Ingest → chunk → embed → store → retrieve → prompt with context → answer |
| Chunking | Size/overlap trade-offs; headings as boundaries for course docs |
| Chatbots | Session memory; system prompt as product policy |
| Document Q&A | Cite retrieved chunks; refuse when context is empty |
| Grounding | “Answer only from context” reduces hallucinations for internal docs |
| Observability | Log prompt version, latency, retrieval hit count (not full secrets) |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| Hybrid search | Keyword + vector when course codes/IDs matter |
| AI assistants | Tools/functions: “look up enrollment status” via your Spring services |
| AI agents (lightweight) | Plan → tool call → observe → answer; keep tool surface small |
| Evaluation lite | Golden questions set; manual score for faithfulness |
| Enterprise concerns | PII, tenancy (per-user docs), auth on `/ai/**`, audit trails |
| Multi-model routing | Cheap model for classify; stronger model for final answer (optional) |
| Streaming | Token streaming to UI for better perceived latency |
| Guardrails | Max input size, allowlists, moderation hooks where available |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35–40 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 40 min Practice steps · 10 min notes · mark boxes

### Week 1 — LLM APIs, Spring AI / LangChain4j, prompts (Days 1–7)

- [ ] **Day 1 — First chat completion**
  - [ ] **Learn:** [OpenAI API docs](https://platform.openai.com/docs/) · [Spring AI getting started](https://docs.spring.io/spring-ai/reference/getting-started.html) · [Externalized config](https://docs.spring.io/spring-boot/reference/features/external-config.html)
  - [ ] **Practice:** Wire `ai-lab` and hit a chat completion
    1. Create Spring Boot app `ai-lab` with Spring AI (or LangChain4j) starter for your provider.
    2. Set API key via env only: e.g. `OPENAI_API_KEY` — never commit keys.
    3. Call chat completion from a `CommandLineRunner` or test method with prompt “Say hello in one sentence.”
    4. Print the model response to the console.
    5. Confirm failure mode when env var is missing (clear error).
    6. **Done when:** A real completion prints and the key is env-only.

- [ ] **Day 2 — Pick a stack**
  - [ ] **Learn:** [Spring AI reference](https://docs.spring.io/spring-ai/reference/) · [Spring AI project](https://spring.io/projects/spring-ai) · [LangChain4j docs](https://docs.langchain4j.info/)
  - [ ] **Practice:** Compare stacks and pick one primary
    1. Table: Spring AI vs LangChain4j — docs fit, Boot integration, RAG helpers, your comfort.
    2. Pick **one** primary stack for the next 3 weeks; note secondary as optional.
    3. Rewrite Day 1 hello using only the chosen stack if needed.
    4. Record dependency coordinates in README.
    5. Stick to the choice unless blocked.
    6. **Done when:** README states the chosen stack and hello still works.

- [ ] **Day 3 — System + user prompts**
  - [ ] **Learn:** [Prompt engineering (OpenAI)](https://platform.openai.com/docs/guides/prompt-engineering) · [Spring AI](https://docs.spring.io/spring-ai/reference/)
  - [ ] **Practice:** `POST /api/ai/complete` with teaching-assistant policy
    1. Add `POST /api/ai/complete` accepting `{ "userMessage": "..." }`.
    2. Set system prompt: concise teaching assistant for a learning platform.
    3. Curl three different user messages; save sample responses in notes.
    4. Return JSON `{ "answer": "..." }` (not raw provider dump).
    5. Log latency roughly (start/end timestamps).
    6. **Done when:** Curl to `/api/ai/complete` returns policy-shaped answers.

- [ ] **Day 4 — Prompt drills**
  - [ ] **Learn:** [Prompt engineering guide](https://platform.openai.com/docs/guides/prompt-engineering)
  - [ ] **Practice:** JSON-only, bullets, and “I don’t know” policy
    1. Add a mode or prompt variant that requires JSON-only output; validate parse.
    2. Prompt for bullet summaries of a short paragraph you provide.
    3. Ask something outside knowledge; require “I don’t know” / unsure phrasing.
    4. Save three prompt templates in `prompts/`.
    5. Note failure when model ignores JSON — how you’ll handle it.
    6. **Done when:** Three prompt drills work and templates are saved.

- [ ] **Day 5 — Timeouts & errors**
  - [ ] **Learn:** [Resilience4j](https://resilience4j.readme.io/) · [OWASP LLM Top 10](https://owasp.org/www-project-top-10-for-large-language-model-applications/)
  - [ ] **Practice:** Timeouts, careful retries, error JSON
    1. Configure client timeout (e.g. 10–20s) on the chat client.
    2. On provider failure, return stable error JSON (`status`,`message`) — not a stack trace to clients.
    3. Retries: at most 1–2 on idempotent GETs/completions if safe; avoid hammering.
    4. Simulate bad API key or unreachable base URL; confirm error path.
    5. Write OWASP note: unbounded retries / prompt injection awareness (short).
    6. **Done when:** Forced provider failure returns clean error JSON within timeout.

- [ ] **Day 6 — Tokens & cost**
  - [ ] **Learn:** [OpenAI docs](https://platform.openai.com/docs/) (usage/limits skim)
  - [ ] **Practice:** Cap max tokens and log usage
    1. Configure max output tokens on the request.
    2. If the API returns usage, log prompt/completion tokens.
    3. Otherwise estimate/log character length as a proxy and note the limitation.
    4. Add a README warning about cost of anonymous abuse.
    5. Try one oversized prompt; observe truncation/error.
    6. **Done when:** Max tokens set and usage/proxy logged for a request.

- [ ] **Day 7 — Secure the endpoint**
  - [ ] **Learn:** [Spring Security](https://docs.spring.io/spring-security/reference/)
  - [ ] **Practice:** JWT on `/api/ai/**`
    1. Add Spring Security; require JWT (or HTTP Bearer test token) for `/api/ai/**`.
    2. Curl without token → 401.
    3. Curl with valid token → 200 + completion.
    4. Keep `/actuator/health` public if you expose it.
    5. Document how to obtain a token in README.
    6. **Done when:** Anonymous calls to `/api/ai/**` are rejected; authorized calls work.

### Week 2 — Embeddings, vectors, RAG foundations (Days 8–14)

- [ ] **Day 8 — Embeddings**
  - [ ] **Learn:** [OpenAI embeddings](https://platform.openai.com/docs/guides/embeddings) · [Spring AI](https://docs.spring.io/spring-ai/reference/) · [LangChain4j](https://docs.langchain4j.info/)
  - [ ] **Practice:** Embed sentences and compare cosine similarity
    1. Embed 3 sentences (two similar, one different) via the embedding API/helper.
    2. Compute cosine similarity between pairs (hand helper OK).
    3. Print a small similarity matrix in the console.
    4. Note embedding dimension in notes.
    5. Save a tiny utility method for reuse in RAG.
    6. **Done when:** Similar sentences score higher than the unrelated one.

- [ ] **Day 9 — Vector store**
  - [ ] **Learn:** [PGVector](https://github.com/pgvector/pgvector) · [Spring AI vector databases](https://docs.spring.io/spring-ai/reference/api/vectordbs.html)
  - [ ] **Practice:** Stand up PGVector (or equivalent)
    1. Run Postgres with `pgvector` (Compose image that includes the extension).
    2. `CREATE EXTENSION IF NOT EXISTS vector;`
    3. Wire Spring AI / LangChain4j vector store to this DB **or** create a learning table for embeddings.
    4. Insert one test vector row; query nearest neighbor.
    5. Document connection URL in `.env.example`.
    6. **Done when:** A nearest-neighbor query returns the inserted test row.

- [ ] **Day 10 — Ingest & chunk**
  - [ ] **Learn:** [Spring AI RAG](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html) · [LangChain4j](https://docs.langchain4j.info/)
  - [ ] **Practice:** Ingest 3–5 markdown notes with overlapping chunks
    1. Prepare 3–5 course markdown files under `docs/corpus/`.
    2. Implement ingest: load → chunk (~500 tokens/chars with overlap) → embed → store.
    3. Expose `POST /api/ai/ingest` (multipart or path) protected by JWT.
    4. Curl ingest for all files; count stored chunks (SQL or API).
    5. Log chunk counts per document.
    6. **Done when:** ≥3 docs are chunked and stored with embeddings.

- [ ] **Day 11 — Retrieve top-k**
  - [ ] **Learn:** [Spring AI RAG](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html) · [PGVector SQL](https://github.com/pgvector/pgvector#sql)
  - [ ] **Practice:** Retrieve top-k chunks before generation
    1. Implement retrieve(question, k=3) returning text snippets + scores/ids.
    2. Add temporary endpoint or log-only path that prints chunks **without** calling the LLM.
    3. Ask a question clearly answered in the corpus; confirm relevant chunks.
    4. Ask an off-corpus question; note weak scores/empty.
    5. Tune k if needed; document default k.
    6. **Done when:** Top-k retrieval prints sensible chunks for an in-corpus question.

- [ ] **Day 12 — Full RAG**
  - [ ] **Learn:** [Spring AI RAG](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html) · [LangChain4j RAG tutorials](https://docs.langchain4j.info/)
  - [ ] **Practice:** Retrieve → grounded prompt → generate
    1. Build prompt: system rules + retrieved context + user question.
    2. Instruct model to answer only from context.
    3. `POST /api/ai/ask` returns `{ answer, sources? }`.
    4. Curl an in-corpus question; verify answer uses facts from chunks.
    5. Keep JWT required.
    6. **Done when:** `/api/ai/ask` returns a grounded answer for an in-corpus question.

- [ ] **Day 13 — Empty retrieval**
  - [ ] **Learn:** [OWASP LLM Top 10](https://owasp.org/www-project-top-10-for-large-language-model-applications/)
  - [ ] **Practice:** Clear empty-hit behavior (no hallucination path)
    1. Detect empty/low-score retrieval.
    2. Return a fixed message like “No relevant course material found” **without** calling the LLM (preferred) or with a hard refusal prompt.
    3. Curl an out-of-corpus question; confirm no invented syllabus facts.
    4. Add test notes with the question used.
    5. Mention this in README failure modes.
    6. **Done when:** Empty retrieval yields a clear non-hallucinated response.

- [ ] **Day 14 — RAG README**
  - [ ] **Learn:** Recap [Spring AI](https://docs.spring.io/spring-ai/reference/) or [LangChain4j](https://docs.langchain4j.info/)
  - [ ] **Practice:** Document ingest + ask in README
    1. README: env vars, Compose/Postgres, JWT how-to.
    2. Exact curl for ingest and ask (with placeholders).
    3. Expected empty-hit behavior.
    4. Cost warning.
    5. Run the README curls once; fix gaps.
    6. **Done when:** README alone is enough to ingest and ask successfully.

### Week 3 — Chatbot, Document Q&A, enterprise features (Days 15–21)

- [ ] **Day 15 — Chat memory**
  - [ ] **Learn:** [LangChain4j](https://docs.langchain4j.info/) · [Spring AI](https://docs.spring.io/spring-ai/reference/) (chat memory / advisors)
  - [ ] **Practice:** Chatbot API with last-N turns window
    1. Add `POST /api/ai/chat` with `sessionId` + `message`.
    2. Keep last N turns in memory (N=6–10) for the session.
    3. Curl two follow-ups that require prior context; confirm coherence.
    4. System policy still applies.
    5. JWT required.
    6. **Done when:** Multi-turn chat uses prior turns within the window.

- [ ] **Day 16 — Persist sessions**
  - [ ] **Learn:** [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/) · [Spring Security](https://docs.spring.io/spring-security/reference/)
  - [ ] **Practice:** Persist chat sessions in Postgres
    1. Tables/entities: session id, user id, messages (role, content, timestamp).
    2. Save each turn after successful completion.
    3. Reload session by id; continue chat after app restart.
    4. Ensure users cannot read others’ sessions (owner check).
    5. Index by `user_id` + `session_id`.
    6. **Done when:** Chat survives restart and is user-scoped in Postgres.

- [ ] **Day 17 — Document Q&A API**
  - [ ] **Learn:** [Spring AI RAG](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html)
  - [ ] **Practice:** `POST /api/ai/ask` with sources
    1. Request body includes `documentSetId` (or `courseId`) + `question`.
    2. Response includes `answer` + `sources` (snippet text and/or doc ids).
    3. Curl in-corpus ask; confirm sources non-empty.
    4. Align field names with Capstone AI service plans.
    5. Add example to README.
    6. **Done when:** Ask returns answer **and** source snippets for an in-corpus hit.

- [ ] **Day 18 — Metadata filters**
  - [ ] **Learn:** [Spring AI vector DBs](https://docs.spring.io/spring-ai/reference/api/vectordbs.html) · [PGVector](https://github.com/pgvector/pgvector)
  - [ ] **Practice:** Filter retrieval by `courseId`
    1. Store `courseId` (or tenant) metadata on each chunk.
    2. Ask with `courseId=101`; ensure chunks only from 101.
    3. Negative test: ask with wrong `courseId` → empty-hit behavior.
    4. Log filtered vs unfiltered counts once for confidence.
    5. Document metadata fields in README.
    6. **Done when:** Wrong-course ask does not leak other course chunks.

- [ ] **Day 19 — Quotas & size limits**
  - [ ] **Learn:** [OWASP LLM Top 10](https://owasp.org/www-project-top-10-for-large-language-model-applications/)
  - [ ] **Practice:** Rate limit notes + reject oversized uploads
    1. Write per-user quota policy in notes (e.g. N asks/hour) — implement simple counter or document Bucket4j plan.
    2. Reject ingest files over a size limit (e.g. 1–2 MB) with 413/400.
    3. Curl oversized file; confirm rejection.
    4. Optional: simple in-memory rate limit on `/ask`.
    5. Add abuse section to README.
    6. **Done when:** Oversized ingest is rejected and quota policy is documented (optionally enforced).

- [ ] **Day 20 — Streaming spike**
  - [ ] **Learn:** [Spring AI reference](https://docs.spring.io/spring-ai/reference/) · [LangChain4j](https://docs.langchain4j.info/)
  - [ ] **Practice:** Streaming spike or chunked HTTP notes
    1. Attempt streaming completion if the stack supports it (SSE/`flux`).
    2. If blocked, document how you’d expose chunked responses and skip implementation.
    3. Time-box to one session — don’t derail RAG solidity.
    4. Note client curl limitations for SSE.
    5. Keep non-streaming ask as the supported path.
    6. **Done when:** Either a working stream demo **or** clear notes on why deferred.

- [ ] **Day 21 — Eval set**
  - [ ] **Learn:** Your RAG README + prompt guide
  - [ ] **Practice:** Score 10 questions manually
    1. Write 10 questions with “must mention” facts from your corpus.
    2. Run `/api/ai/ask` for each; score pass/fail.
    3. Record results in `evals/rag-scorecard.md`.
    4. Fix the worst miss (chunking, k, prompt) if quick.
    5. Note overall pass rate.
    6. **Done when:** Scorecard has 10 scored questions with pass rate noted.

### Week 4 — Assistants, agents, polish, projects (Days 22–28)

- [ ] **Day 22 — Tool calling**
  - [ ] **Learn:** [LangChain4j tools/AiServices](https://docs.langchain4j.info/) · [Spring AI](https://docs.spring.io/spring-ai/reference/)
  - [ ] **Practice:** Expose `getCourseById` as a tool
    1. Implement a tool/function `getCourseById(id)` returning stub/real course JSON.
    2. Register it with the model/agent API for your stack.
    3. Ask a question that requires the tool; log the tool invocation.
    4. Ensure final user-visible answer is natural language.
    5. JWT still required on the HTTP entrypoint.
    6. **Done when:** Logs show tool call + final NL answer for a tool-needed question.

- [ ] **Day 23 — Bound the agent**
  - [ ] **Learn:** [OWASP LLM Top 10](https://owasp.org/www-project-top-10-for-large-language-model-applications/) · [LangChain4j](https://docs.langchain4j.info/)
  - [ ] **Practice:** Max tool rounds 2–3; always end with NL answer
    1. Configure max tool rounds (2–3).
    2. Force a scenario that would loop; confirm it stops.
    3. Disallow tools that mutate data for now (read-only).
    4. Document agent bounds in README.
    5. Add OWASP note: excessive agency risk.
    6. **Done when:** Tool loop is capped and answers still end in natural language.

- [ ] **Day 24 — Mini-project A**
  - [ ] **Learn:** §6A + [Spring AI getting started](https://docs.spring.io/spring-ai/reference/getting-started.html)
  - [ ] **Practice:** Secured AI complete vertical slice
    1. Align code with §6A: secured `POST /api/ai/complete`.
    2. System policy + error JSON + JWT.
    3. Curl tests: 401, 200, provider-down error.
    4. README section for Mini-project A.
    5. Keep scope tight (no RAG required today).
    6. **Done when:** §6A complete endpoint passes the three curl tests.

- [ ] **Day 25 — Mini-project A polish**
  - [ ] **Learn:** [Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Health + README polish for Mini-project A
    1. Expose `/actuator/health`.
    2. Add dependency/version notes and env template.
    3. Re-run Day 24 curls from README.
    4. Fix any flaky timeout settings.
    5. Tag notes `mini-a-done`.
    6. **Done when:** Health is UP and README curls succeed cleanly.

- [ ] **Day 26 — Mini-project B chatbot**
  - [ ] **Learn:** §6B + [LangChain4j](https://docs.langchain4j.info/) or [Spring AI](https://docs.spring.io/spring-ai/reference/)
  - [ ] **Practice:** Chatbot with memory + policy prompt
    1. Meet §6B: chat endpoint + memory window + policy.
    2. Persist or clearly document in-memory limitation.
    3. Demo script: 3-turn conversation.
    4. JWT on chat routes.
    5. Link from root README.
    6. **Done when:** 3-turn demo works under policy with memory.

- [ ] **Day 27 — Mini-project C RAG**
  - [ ] **Learn:** §6C + [Spring AI RAG](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html)
  - [ ] **Practice:** Document Q&A with citations
    1. Meet §6C: ingest corpus + ask with citations + empty-hit.
    2. Run eval subset (5 questions) and record results.
    3. Ensure course/docSet filter still works.
    4. Demo script in README.
    5. Note Capstone reuse points.
    6. **Done when:** RAG demo with citations and empty-hit passes README script.

- [ ] **Day 28 — Mastery gate**
  - [ ] **Learn:** Recap [Spring AI](https://docs.spring.io/spring-ai/reference/) · [LangChain4j](https://docs.langchain4j.info/) · [OWASP LLM](https://owasp.org/www-project-top-10-for-large-language-model-applications/)
  - [ ] **Practice:** Checklist + failure notes
    1. Tick mastery checklist honestly.
    2. Write failure notes: API down, empty RAG, rate limit — with expected HTTP behavior.
    3. Re-run one complete, one ask, one chat curl.
    4. List Capstone handoff items (PGVector, prompts, JWT).
    5. Retrospective keep/stop/start.
    6. **Done when:** Checklist + failure notes exist and smoke curls pass.

*If using only free local models: keep the same API shapes; document your local endpoint in README.*

## 5. Hands-on exercises

### Prompt & API

1. Write three system prompts for: tutor, strict fact-checker, JSON extractor — compare outputs on the same user question.
2. Force structured output (JSON schema or “JSON only”) and parse it in Java.
3. Handle provider `429` / timeout with a friendly API error body.
4. Add request size limit (reject prompts > N characters).

### Embeddings & RAG

5. Embed “enrollment deadline” vs “course syllabus” sentences; confirm similar texts score higher.
6. Chunk a long markdown file; inspect chunk boundaries for broken sentences.
7. Retrieve top-3 chunks; manually verify relevance before generation.
8. Ablation: answer **with** vs **without** retrieved context; note hallucination differences.
9. Add citation field: return `{ answer, sources: [{ title, snippet }] }`.

### Product features

10. Chat memory: ask a follow-up that requires the previous turn (“make it shorter”).
11. Clear session endpoint; prove memory is gone.
12. JWT: user A cannot read user B’s chat session.
13. Tool call: model asks for `courseId=5`; your service returns JSON; model summarizes.
14. Kill the LLM URL in config; confirm API still returns controlled error (no stack trace leak).

### Stretch

15. Hybrid: if question contains a course code pattern, fetch that row from JPA first, then RAG.
16. Simple eval CSV: question, expected_substring, pass/fail.

---

## 6. Mini-projects

> **Build guides:** [A AI Spring Boot App](../mini-projects/phase-06-architecture-ai/21-ai-integration/A-ai-spring-boot-app.md) · [B Chatbot](../mini-projects/phase-06-architecture-ai/21-ai-integration/B-chatbot.md) · [C Document Q&A RAG](../mini-projects/phase-06-architecture-ai/21-ai-integration/C-document-qa-rag.md) · [Index](../mini-projects/README.md)

### A. AI-powered Spring Boot app

**Must**

- Spring Boot service with secured `POST /api/ai/complete`
- System prompt for a Learning Hub teaching assistant
- Env-based API key; README with run steps
- Timeouts + consistent error JSON
- Actuator health (app up even if AI provider is down — report AI dependency separately if easy)

**Should**

- Usage logging (user id, latency ms, token estimate)
- Profile `dev` with verbose AI logs redacting secrets

### B. Chatbot

**Must**

- `POST /api/ai/chat` with `sessionId` + message
- In-memory or DB-backed last-N message memory
- System policy: helpful, no invented enrollment facts without tools/RAG

**Should**

- List sessions for current user
- Title auto-generated from first message

### C. Document Q&A System (RAG)

**Must**

- Ingest endpoint or CLI for markdown/PDF-text course materials
- Chunk + embed + store in vector DB
- `POST /api/ai/ask` returns answer grounded in retrieved chunks
- Explicit behavior when no chunks found
- Return source snippets for transparency

**Should**

- Filter by `courseId`
- Re-ingest / delete document set

**Could**

- Streaming answers
- Simple eval suite of 10 questions

---

## 7. Industry use cases

- **Internal knowledge assistants:** Policies, runbooks, onboarding docs via RAG
- **Customer support copilots:** Draft replies grounded in product docs (human still approves)
- **Learning platforms:** Course material Q&A, study guides, quiz generation from chapters
- **Developer productivity:** Explain logs/errors with optional codebase RAG (careful with secrets)
- **Enterprise search upgrade:** Semantic search over manuals alongside keyword search
- **Workflow assistants:** Tool calling into existing Spring services (orders, enrollments, tickets)
- **Compliance-aware apps:** Audit prompts/responses metadata; tenant isolation on vector collections

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Sending API keys in Git | Env / secret manager; rotate if leaked |
| Unbounded prompts from users | Max length; auth; rate limits |
| RAG without empty-hit handling | Refuse or ask clarifying question |
| Huge chunks / no overlap | Tune chunk size; keep headings |
| Trusting the model for DB facts | Use tools/SQL/services for authoritative data |
| No timeouts | Configure client timeouts; fail fast |
| Logging full prompts with PII | Redact; log hashes/ids |
| Agent with unlimited tools/loops | Cap iterations; allowlist tools |
| One giant “AI microservice” with no product API | Expose clear use-case endpoints (ask, chat, ingest) |
| Skipping citations | Return sources so you can debug wrong answers |
| Temperature high for factual Q&A | Use low temperature for grounded answers |
| Blocking the whole app if AI is down | Isolate failures; degrade gracefully |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [OpenAI API docs](https://platform.openai.com/docs/) | Chat, embeddings, API shapes (use current product names in docs) |
| [Spring AI reference](https://docs.spring.io/spring-ai/reference/) | ChatClient, advisors, vector stores, RAG |
| [Spring AI project](https://spring.io/projects/spring-ai) | Overview & samples |
| [LangChain4j docs](https://docs.langchain4j.info/) | AiServices, embeddings, RAG, tools |
| [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) | Examples and module list |
| [PGVector](https://github.com/pgvector/pgvector) | Vectors in Postgres |
| [PostgreSQL full text search](https://www.postgresql.org/docs/current/textsearch.html) | Hybrid keyword side |
| [OWASP LLM Top 10](https://owasp.org/www-project-top-10-for-large-language-model-applications/) | Prompt injection & security awareness |
| [Prompt engineering guide (OpenAI)](https://platform.openai.com/docs/guides/prompt-engineering) | Practical prompting |
| Baeldung Spring AI tutorials (free articles) | Extra Spring-oriented walkthroughs |

Official Spring AI and LangChain4j docs win when tutorials disagree.

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- OpenAI platform docs: https://platform.openai.com/docs/
- OpenAI embeddings guide: https://platform.openai.com/docs/guides/embeddings
- OpenAI prompt engineering: https://platform.openai.com/docs/guides/prompt-engineering
- Spring AI reference: https://docs.spring.io/spring-ai/reference/
- Spring AI getting started: https://docs.spring.io/spring-ai/reference/getting-started.html
- Spring AI vector databases: https://docs.spring.io/spring-ai/reference/api/vectordbs.html
- Spring AI advisors / RAG patterns: https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html
- LangChain4j documentation: https://docs.langchain4j.info/
- LangChain4j GitHub: https://github.com/langchain4j/langchain4j
- LangChain4j Spring Boot integration (see docs modules): https://docs.langchain4j.info/tutorials/spring-boot-integration/
- PGVector: https://github.com/pgvector/pgvector
- PGVector SQL examples: https://github.com/pgvector/pgvector#sql
- OWASP Top 10 for LLM Apps: https://owasp.org/www-project-top-10-for-large-language-model-applications/
- Spring Boot externalized config: https://docs.spring.io/spring-boot/reference/features/external-config.html
- Resilience4j (timeouts/retry mindset): https://resilience4j.readme.io/
- JWT / Spring Security (protect AI endpoints): https://docs.spring.io/spring-security/reference/

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. First completion | Secured chat/complete endpoint | Works with env key |
| 2. Prompt craft | Policies + structured output | Reliable JSON parse demo |
| 3. Embeddings | Similarity intuition | Manual similarity check |
| 4. Vector store | Ingest + retrieve | Top-k relevant chunks |
| 5. RAG Q&A | Grounded answers + empty path | Doc Q&A mini-project |
| 6. Chatbot | Memory + sessions | Multi-turn demo |
| 7. Tools | One Spring service tool | Assistant uses tool correctly |
| 8. Harden | Auth, limits, failure notes | Checklist ≥90% |
| 9. Gate | Ready for phase project | Mini-projects A–C runnable |

**Weekly rhythm:** Mon–Thu build; Fri failure/security pass; Sat README + eval questions; Sun checklist.

### Suggested package layout (`ai-lab`)

```text
com.yourname.ailab/
  AiLabApplication.java
  config/           # AI beans, security
  chat/             # complete + chatbot controllers/services
  rag/              # ingest, chunk, retrieve, ask
  tools/            # course lookup tools
  common/           # errors, DTOs
```

---

## 12. Mastery checklist

- [ ] Calls an LLM API from Spring Boot using env-stored credentials
- [ ] Writes effective system/user prompts with clear constraints
- [ ] Configures timeouts and returns safe errors when the provider fails
- [ ] Explains embeddings and why vector search helps document Q&A
- [ ] Ingests documents: chunk → embed → store
- [ ] Implements RAG ask endpoint with grounded answers
- [ ] Handles empty retrieval without hallucinating course facts
- [ ] Builds a chatbot with conversation memory and user isolation
- [ ] Protects `/api/ai/**` with authentication (and basic quota mindset)
- [ ] Exposes at least one tool/function into an existing service method
- [ ] Returns source snippets for Document Q&A
- [ ] Documents cost/rate-limit/failure modes in README
- [ ] Completed mini-projects A (AI app), B (Chatbot), C (Document Q&A)
- [ ] Can explain Spring AI **or** LangChain4j choices used in the project
- [ ] Ready to start [phase-project.md](phase-project.md)

---

## Quick reference — success criteria

You are done with topic 21 when a secured Spring Boot app can **chat**, **answer from your documents (RAG)**, and **fail closed** when retrieval is empty or the model API is unavailable. A demo script with three questions (easy, follow-up, out-of-corpus) is the proof.
