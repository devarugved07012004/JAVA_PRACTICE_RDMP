# Mini-Project A — HTTP Pocket Guide
**Phase:** 3 — Frontend & HTTP  
**Topic:** [10 — REST API & HTTP](../../../phase-03-frontend-api/10-rest-http.md)  
**Time:** ~2–3 hours

## Goal

Create a personal one-page markdown cheat sheet you will actually use in Phase 4: methods, status codes, header checklist, and scenario→status flash rows — grounded in what you practiced in Postman/Network, not copied blindly.

## Prerequisites

- [ ] Topic 10 Days 1–4 practiced (methods, statuses, headers lab)
- [ ] Postman installed; at least one httpbin request sent
- [ ] A notes folder in your learning repo

## Learn (open before coding)

- [HTTP request methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods)
- [HTTP status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
- [HTTP headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers)
- [Content-Type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type)
- [Accept](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept)
- [RFC 9110 — skim methods/statuses](https://www.rfc-editor.org/rfc/rfc9110)

## Project layout

```text
http-notes/
  HTTP-POCKET-GUIDE.md      # the deliverable
  examples/
    captured-request.md     # optional Network dump paraphrase
  README.md                 # one paragraph: where this lives / how you use it
```

## Build steps

### Step 1 — Create the file skeleton
- [ ] Create `HTTP-POCKET-GUIDE.md` with sections:
  - Methods
  - Status codes I use
  - Header checklist
  - Scenario → status flash cards
  - Safe vs idempotent (short)
  - Links (MDN bookmarks)
- [ ] Put it where you will open it during Spring work
- **Done when:** Empty section headings exist in the file.

### Step 2 — Methods table (your words)
- [ ] Table: Method | Intent | Body? | Safe? | Idempotent?
- [ ] Cover `GET`, `POST`, `PUT`, `PATCH`, `DELETE` at minimum
- [ ] Add one line: “Why `/createTask` is wrong”
- **Done when:** Table is filled without pasting an entire MDN page.

### Step 3 — Status codes you actually use
- [ ] List with one-line meaning: `200`, `201`, `204`, `400`, `401`, `403`, `404`, `409`, `422`, `500`
- [ ] Star the ones you expect in Task Board v1
- [ ] Write `401` vs `403` in your own sentence
- **Done when:** You can quiz yourself from this section alone.

### Step 4 — Header checklist
- [ ] Checklist for JSON APIs:
  - [ ] `Content-Type: application/json` on JSON bodies
  - [ ] `Accept: application/json` when you want JSON back
  - [ ] `Authorization: Bearer …` (shape only — later)
  - [ ] Cache awareness (`Cache-Control`) — one line
- [ ] Note what you sent in the httpbin lab
- **Done when:** Checklist is copy-paste useful before Send in Postman.

### Step 5 — Five scenario → status flash rows
- [ ] Exactly five rows (add more only if useful), e.g.:
  | Scenario | Status |
  |----------|--------|
  | Resource created | `201` |
  | Missing / unknown id | `404` |
  | Validation failed | `400` or `422` (pick one policy) |
  | Duplicate / conflict | `409` |
  | Success, no body | `204` |
- [ ] Write your chosen validation code policy once and stick to it
- **Done when:** Five flash rows exist with your policy noted.

### Step 6 — Capture one real request
- [ ] Browser Network or Postman: capture one request/response
- [ ] In `examples/captured-request.md` (or a section): method, path, 3 headers, status, body yes/no — **no secrets/cookies values**
- [ ] Link it from the pocket guide
- **Done when:** Guide references a real capture you made.

### Step 7 — REST noun reminder
- [ ] 5 bullet resource URL examples for Task Board (`/api/tasks`, `/api/tasks/{id}`, query `?status=todo`)
- [ ] Mark collection vs item
- **Done when:** Bullets match the design you will freeze in Mini-project B.

### Step 8 — Cookies / CORS one-pagers
- [ ] 4–6 lines: cookie session vs `Authorization` header
- [ ] 2–3 lines: why browser JS hits CORS on other origins
- **Done when:** Both topics have a “future me” reminder, not a thesis.

### Step 9 — Peer/self quiz
- [ ] Hide the status column; quiz yourself once; fix weak rows
- [ ] Timebox 10 minutes
- **Done when:** You score yourself and update any fuzzy lines.

### Step 10 — README pointer
- [ ] Short README: “Open `HTTP-POCKET-GUIDE.md` before designing endpoints”
- [ ] Optional: add link from topic 10 notes
- **Done when:** File is findable without searching the whole disk.

## Definition of Done

- [ ] One markdown cheat sheet with methods, statuses, header checklist
- [ ] ≥5 scenario → status flash rows
- [ ] Personal (your wording) — usable as Phase 4 quick reference
- [ ] No secrets/cookies pasted into the guide

## Demo script

1. Open `HTTP-POCKET-GUIDE.md`
2. Explain three methods and three statuses aloud in <2 minutes
3. Show the five flash rows
4. Point to header checklist you will use in Postman

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Dumping entire MDN into notes | Distill; link out |
| Listing 40 statuses you will never use | Focus on Task Board set |
| Leaving 401/403 fuzzy | Write the distinction once clearly |
| Pasting Authorization tokens | Redact; describe shape only |

## After this

- Build required [B — Task Board API Contract](B-task-board-api-contract.md)
- Optional: [C — Public API exploration](C-public-api-exploration.md)
