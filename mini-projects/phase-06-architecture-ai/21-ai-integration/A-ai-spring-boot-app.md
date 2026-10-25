# Mini-project A — AI-powered Spring Boot App

**Topic:** [21 AI Integration](../../../phase-06-architecture-ai/21-ai-integration.md) · **Phase 6**

---

## Goal

Build a Spring Boot service with secured `POST /api/ai/complete`, a Learning Hub teaching-assistant system prompt, env-based API key, timeouts + consistent error JSON, and Actuator health (app up even if the AI provider is down).

---

## Prerequisites

- Spring Boot 3 + Security/JWT basics
- LLM API key (OpenAI or compatible) via env
- LangChain4j **or** Spring AI (pick one; stick to it)
- Topic 21 client + prompt basics

---

## Learn

| Resource | Why |
|----------|-----|
| Spring AI or LangChain4j docs (chosen stack) | Chat client |
| [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html) | Health |
| Provider timeout/retry docs | Fail fast |

---

## Project layout

```text
ai-spring-boot-app/
  src/main/java/.../ai/
    AiController.java
    AiService.java
    AiProperties.java
  src/main/resources/application.yml
  .env.example              # OPENAI_API_KEY=  (never commit .env)
  README.md
  scripts/demo-complete.http
```

---

## Build steps

- [ ] **1. Skeleton + dependency**
  1. Boot app; add Spring AI or LangChain4j starter.
  2. **Done when:** App starts without calling the model yet.

- [ ] **2. Secrets via env**
  1. `OPENAI_API_KEY` (or provider equivalent) from env; `.env.example` only.
  2. Fail clear if missing in `prod` profile.
  3. **Done when:** Key never appears in Git history of this lab.

- [ ] **3. System prompt**
  1. Teaching assistant for Learning Hub: helpful, no invented enrollment facts.
  2. Externalize prompt text (config or classpath file).
  3. **Done when:** Prompt reviewed and documented in README.

- [ ] **4. `POST /api/ai/complete`**
  1. Body: `{ "prompt": "..." }` with max length validation.
  2. JWT-secured (reuse simple Security config).
  3. **Done when:** Authenticated call returns completion text.

- [ ] **5. Timeouts + errors**
  1. Configure client timeout (e.g. 10–30s).
  2. Map provider failures to consistent JSON (`code`, `message`).
  3. **Done when:** Kill network / bad key → structured 502/503, not stack trace HTML.

- [ ] **6. Actuator health**
  1. App health UP even if AI ping fails; optional separate indicator component.
  2. **Done when:** Documented behavior in README.

- [ ] **7. (Should) Usage logging**
  1. Log user id, latency ms, token estimate (no full secret prompts in prod logs).
  2. **Done when:** One sample log line in NOTES.

- [ ] **8. `dev` profile**
  1. Verbose AI logs with redaction.
  2. **Done when:** Profile documented.

- [ ] **9. README**
  1. Run steps, env vars, sample HTTP, limits.
  2. **Done when:** Cold start demo works from README.

---

## Definition of Done

- [ ] Secured `/api/ai/complete`
- [ ] System prompt for teaching assistant
- [ ] Env-based API key
- [ ] Timeouts + error JSON
- [ ] Actuator health story
- [ ] README run steps

---

## Demo

```bash
export OPENAI_API_KEY=...   # or set in shell/IDE
mvn spring-boot:run
curl -s -X POST http://localhost:8080/api/ai/complete \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"prompt":"Explain what a REST GET is in one paragraph"}'
curl -s http://localhost:8080/actuator/health
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| API key in Git | Env only; rotate if leaked |
| Unbounded prompts | Max length + auth |
| No timeouts | Configure client timeouts |
| Logging full PII prompts | Redact |

---

## After this

- [B-chatbot.md](./B-chatbot.md)
- [C-document-qa-rag.md](./C-document-qa-rag.md)
