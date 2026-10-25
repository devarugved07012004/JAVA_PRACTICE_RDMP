# Mini-project B — Chatbot

**Topic:** [21 AI Integration](../../../phase-06-architecture-ai/21-ai-integration.md) · **Phase 6**

---

## Goal

Expose `POST /api/ai/chat` with `sessionId` + message; keep last-N message memory (in-memory or DB); system policy: helpful, no invented enrollment facts without tools/RAG.

---

## Prerequisites

- Mini-project A complete (client, security, env key)
- Understanding of chat vs single-completion

---

## Learn

| Resource | Why |
|----------|-----|
| Chosen AI stack “chat memory” docs | Message window |
| Topic 21 chatbot day tasks | Product shape |

---

## Project layout

```text
ai-chatbot/
  ... extends A or new module
  chat/
    ChatController.java
    ChatService.java
    ChatSessionStore.java
  README.md
```

---

## Build steps

- [ ] **1. API contract**
  1. Request: `{ "sessionId": "uuid", "message": "..." }`.
  2. Response: `{ "sessionId", "reply", "messageCount" }`.
  3. **Done when:** Contract in README.

- [ ] **2. Session store**
  1. In-memory `ConcurrentHashMap` **or** JPA table of messages.
  2. Cap last N (e.g. 10–20) messages sent to the model.
  3. **Done when:** Second message in same session sees prior context.

- [ ] **3. System policy**
  1. Same Learning Hub assistant rules; refuse invented enrollments.
  2. **Done when:** Prompt text includes explicit policy lines.

- [ ] **4. Secure endpoint**
  1. JWT required; sessions scoped to user id.
  2. **Done when:** User A cannot read User B session (if DB-backed) or document in-memory lab limit.

- [ ] **5. Validation**
  1. Max message length; reject blank.
  2. **Done when:** 400 on bad input.

- [ ] **6. (Should) List sessions**
  1. `GET /api/ai/sessions` for current user.
  2. **Done when:** Works or deferred in README.

- [ ] **7. (Should) Auto title**
  1. Title from first message truncated.
  2. **Done when:** Stored alongside session.

- [ ] **8. Demo script**
  1. Two turns same `sessionId`; prove memory.
  2. New `sessionId`; prove isolation.
  3. **Done when:** Script in README.

- [ ] **9. Health + errors**
  1. Reuse A timeout/error JSON patterns.
  2. **Done when:** Provider failure → controlled error.

---

## Definition of Done

- [ ] `POST /api/ai/chat` with session memory
- [ ] Last-N window enforced
- [ ] Policy against invented enrollment facts
- [ ] README demo with two-turn proof

---

## Demo

```bash
SID=$(uuidgen)   # or any UUID
curl -s -X POST http://localhost:8080/api/ai/chat -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID\",\"message\":\"My name is Ada.\"}"
curl -s -X POST http://localhost:8080/api/ai/chat -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID\",\"message\":\"What is my name?\"}"
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Sending entire history unbounded | Cap last N |
| No session isolation notes | Scope by user |
| Treating chatbot as source of DB truth | Defer to RAG/tools |

---

## After this

- [C-document-qa-rag.md](./C-document-qa-rag.md) for grounded answers
