# Mini-Project C — Public API Exploration (stretch)
**Phase:** 3 — Frontend & HTTP  
**Topic:** [10 — REST API & HTTP](../../../phase-03-frontend-api/10-rest-http.md)  
**Time:** ~3–4 hours

## Goal

Pick a free, well-documented public API; build ≥5 Postman requests; write a short critique of which REST rules it follows or breaks. Learning exercise — not a complaint letter to the API owner.

## Prerequisites

- [ ] Postman environments/collections from Mini-project B
- [ ] Comfort reading API docs (auth notes, rate limits, base URL)
- [ ] Prefer APIs that allow anonymous or free-key access for learning

## Learn (open before coding)

- [Postman Getting Started](https://learning.postman.com/docs/getting-started/overview/)
- [HTTP methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods)
- [HTTP status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
- [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) *(if you also try from browser)*
- Docs for **your chosen API** (official only)

## Project layout

```text
public-api-lab/
  README.md                 # which API, why, base URL, key setup
  NOTES-critique.md         # REST follow/break observations
  postman/
    public-api.postman_collection.json
    public-api.postman_environment.json
  samples/                  # optional saved response snippets (redacted)
```

**Suggested picks (examples — verify current terms yourself):**

- Open data / government JSON APIs with clear docs
- [httpbin.org](https://httpbin.org/) for HTTP mechanics (not “domain REST”)
- A documented open API you already use for learning (weather, dictionaries, etc.)

Avoid APIs that require paid plans or scrape HTML.

## Build steps

### Step 1 — Choose & document
- [ ] Pick one API; read “Getting started” + auth + rate limit pages
- [ ] README: name, docs URL, base URL, whether a free key is needed
- [ ] Create free key if required; store in Postman env as `apiKey` — **never commit the real key**
- **Done when:** README states base URL and auth shape (`query` vs `header`).

### Step 2 — Environment
- [ ] Variables: `baseUrl`, `apiKey` (secret type in Postman if available)
- [ ] Optional: `itemId` for chaining
- [ ] Export environment **without** real secrets (or use blank placeholder in Git)
- **Done when:** Requests use `{{baseUrl}}` only.

### Step 3 — Five requests minimum
- [ ] Build ≥5 requests, mixed intents where the API allows, e.g.:
  1. List / search (`GET` collection)
  2. Get by id (`GET` item)
  3. Filtered query (`GET` + query params)
  4. A documented error case (bad id / missing key)
  5. Another resource or HTTP echo (`POST` if API supports writes; else second read shape)
- [ ] Name requests clearly in a folder
- **Done when:** Five requests exist and each has been Sent at least once.

### Step 4 — Auth wiring
- [ ] If key required: add header or query param via env variable
  - Example header style: `Authorization: Bearer {{apiKey}}` **or** vendor-specific header from docs
- [ ] Prove one call fails without key (if applicable) and succeeds with key
- **Done when:** Auth pattern matches docs and is not hard-coded in request URL history you will commit.

### Step 5 — Status & body notes
- [ ] For each request, note: method, path, status, 2–3 interesting fields
- [ ] Save one redacted sample JSON in `samples/` (no PII/keys)
- **Done when:** NOTES or README table summarizes the five calls.

### Step 6 — Test scripts (light)
- [ ] Add `pm.response.to.have.status(200)` (or documented success) on ≥2 happy requests
- [ ] Add one assertion on a JSON field (`pm.expect(json...).to.be.ok`)
- [ ] Add one expected error status assertion
- **Done when:** Test Results show green for those scripts.

### Step 7 — REST critique
- [ ] In `NOTES-critique.md`, answer:
  - Nouns vs verbs in URLs?
  - Consistent status code use?
  - Filtering via query params?
  - Versioning style (`/v1` vs headers)?
  - Any RPC-ish endpoints (`/getThings`)?
- [ ] List **3 follows** and **3 breaks/quirks** — neutral tone
- **Done when:** Critique is specific to this API with examples.

### Step 8 — Browser contrast (optional)
- [ ] Try one `fetch` from a local HTML page; note CORS success/fail
- [ ] One paragraph: why Postman succeeded where browser failed (if it did)
- **Done when:** Optional section filled or explicitly skipped.

### Step 9 — Export collection
- [ ] Export collection JSON into `postman/`
- [ ] Sanitize: remove real keys from exported JSON before commit
- **Done when:** Repo has importable collection with placeholders.

### Step 10 — README finish
- [ ] Import steps; which env to select; which request to run first
- [ ] Link to official docs
- **Done when:** Clean clone + your own key is enough to replay.

## Definition of Done

- [ ] ≥5 Postman requests against one public documented API
- [ ] Environment uses `baseUrl` (+ key var if needed, not committed)
- [ ] Short critique: REST rules followed vs broken
- [ ] Collection exported; secrets scrubbed

## Demo script

1. Open README — name the API and base URL
2. Import collection; set your local key in env
3. Run happy-path request → show `200` + body fields
4. Run error request → show documented 4xx
5. Read two critique bullets aloud

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Committing API keys | Env secrets + scrub exports |
| Judging HTML scrapes as REST | Use documented HTTP JSON APIs |
| Vague critique (“it’s bad”) | Cite concrete paths/statuses |
| Only one GET | Diversify query/error/item paths |
| Ignoring rate limits | Slow down; read docs |

## After this

- Return to freeze [B — Task Board contract](B-task-board-api-contract.md) if not done
- Phase project: [task-board.md](../phase-project/task-board.md)
