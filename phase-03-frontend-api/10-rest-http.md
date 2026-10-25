# 10 — REST API & HTTP

**Duration:** ~2 weeks (14 days) · **Cadence:** 1 hour/day  
**Focus:** HTTP methods, status codes, headers, cookies/sessions intuition, REST principles, JSON, API testing with Postman, Swagger/OpenAPI contracts — without Spring yet

---

## 1. Why this topic is important

Browsers, mobile apps, and backend services all speak **HTTP**. REST is the dominant style for resource APIs in Java/Spring shops. If you cannot read a request line, headers, status code, and JSON body fluently, Spring MVC annotations later feel like magic instead of mapping.

This topic makes you **API-literate**: design small resource URLs, pick methods and statuses deliberately, document a contract, and prove it with Postman. The Phase 3 Task Board depends on that contract. Phase 4 Spring Boot controllers will implement the same ideas with annotations — vocabulary first, framework second.

---

## 2. Prerequisites

- Topic [09](09-html-css-js.md) mastery checklist ≥90% (especially `fetch`, JSON, forms)
- Comfort reading JSON objects/arrays from topic 09
- Phase 2 completed recommended (you already think in entities/tables ↔ resources)
- Postman Desktop or Postman web account (free tier is enough)

**Environment you will set up:**

- Postman installed: https://www.postman.com/downloads/
- Browser DevTools Network tab (already from topic 09)
- Optional mock: [Postman Mock Server](https://learning.postman.com/docs/designing-and-developing-your-api/mocking-data/setting-up-mock/), [JSON Server](https://github.com/typicode/json-server) (free/open source), or httpbin.org for HTTP echo drills
- A markdown or YAML file for your OpenAPI sketch (text editor is enough)

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| HTTP as request/response | Client sends request; server returns status + headers + optional body |
| URL anatomy | Scheme, host, port, path, query string, fragment (fragment not sent to server) |
| Methods | `GET`, `POST`, `PUT`, `PATCH`, `DELETE` — intent of each |
| Safe vs unsafe | Safe methods should not change server state (`GET`, `HEAD`, `OPTIONS`) |
| Idempotency intuition | Repeating `PUT`/`DELETE` vs repeating `POST` |
| Status code families | 1xx info, 2xx success, 3xx redirect, 4xx client error, 5xx server error |
| Common statuses | `200`, `201`, `204`, `400`, `401`, `403`, `404`, `409`, `422`, `500` |
| Headers intro | `Content-Type`, `Accept`, `Authorization` (concept), `Cache-Control` awareness |
| JSON | Objects/arrays; types; nesting; UTF-8; Content-Type `application/json` |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| REST principles | Resources, nouns in URLs, methods as verbs, representations (JSON) |
| Resource modeling | Collection vs item: `/tasks`, `/tasks/{id}` |
| Query params | Filtering, sorting, pagination *ideas* (`?status=todo&page=1`) |
| Request/response bodies | When GET has no body; POST/PUT/PATCH bodies |
| Error body shape | Consistent `{ "error": "...", "details": ... }` style for learning |
| Cookies | `Set-Cookie` / `Cookie`; what browsers store; Path/HttpOnly/Secure *ideas* |
| Sessions (conceptual) | Server remembers client via session id cookie — contrast with later JWT lessons |
| CORS (practical) | Browser enforces origin rules; APIs used from JS may need CORS headers |
| Postman collections | Requests, folders, environments, variables (`{{baseUrl}}`) |
| Happy vs error paths | Always test 2xx *and* at least one 4xx |

### Advanced (contract & testing depth)

| Concept | What to internalize |
|--------|----------------------|
| HATEOAS awareness | Links in responses exist in some APIs — know the term; not required to implement |
| Content negotiation | `Accept` / `Content-Type` mismatch → `415` / `406` awareness |
| Versioning styles | URI `/v1/...` vs headers — pick one for your contract and stick to it |
| OpenAPI / Swagger | Machine-readable API description; paths, schemas, examples |
| Swagger UI | Interactive docs generated from OpenAPI |
| Contract-first workflow | Write OpenAPI → mock → UI against mock → later real Spring impl |
| Auth header patterns | `Authorization: Bearer ...` as a *shape* you’ll use later (no production auth yet) |
| Rate limits / retries | `429`, idempotency keys as industry awareness |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — HTTP fluency & REST modeling (Days 1–7)

- [ ] **Day 1 — HTTP overview**
  - [ ] **Learn:** [HTTP overview](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview) · [HTTP messages](https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages) · [RFC 9110 — HTTP Semantics](https://www.rfc-editor.org/rfc/rfc9110)
  - [ ] **Practice:** Sketch a real browser request/response
    1. Open any simple site; DevTools → Network → reload.
    2. Click the document request; note method, URL, status, a few request/response headers.
    3. On paper, draw Request (method, path, headers, body?) and Response (status, headers, body?).
    4. Label where the browser vs server sits.
    5. **Done when:** Sketch matches what Network showed for one request.

- [ ] **Day 2 — HTTP methods**
  - [ ] **Learn:** [HTTP request methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods) · [RFC 9110 §9 Methods](https://www.rfc-editor.org/rfc/rfc9110#name-methods)
  - [ ] **Practice:** Map 10 actions to methods
    1. List 10 actions (include: list books, create order, update email, delete comment, plus 6 more).
    2. Assign each `GET`/`POST`/`PUT`/`PATCH`/`DELETE` and a sample path.
    3. Mark which are safe; mark which should be idempotent.
    4. Rewrite one bad verb-URL (`/createTask`) into noun + method.
    5. **Done when:** Table of 10 actions with method + path exists in notes.

- [ ] **Day 3 — Status codes**
  - [ ] **Learn:** [HTTP status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status) · [RFC 9110 §15 Status Codes](https://www.rfc-editor.org/rfc/rfc9110#name-status-codes)
  - [ ] **Practice:** Scenario → status flash cards
    1. For each scenario pick a code: resource created; missing id; validation fail; conflict (duplicate); success with empty body.
    2. Write one-line justifications (expect codes like `201`, `404`, `400`/`422`, `409`, `204`).
    3. Add `401` vs `403` distinction in your own words.
    4. Quiz yourself once without looking.
    5. **Done when:** Notes cover those scenarios with justified codes.

- [ ] **Day 4 — Headers lab (httpbin)**
  - [ ] **Learn:** [HTTP headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers) · [Content-Type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type) · [Accept](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept) · [Sending requests (Postman)](https://learning.postman.com/docs/sending-requests/requests/)
  - [ ] **Practice:** Echo headers with Postman → httpbin
    1. Install/open Postman; create request `GET https://httpbin.org/headers`.
    2. Add headers: `Accept: application/json`, custom `X-Learning: day04`; Send — expect status `200`.
    3. New request `POST https://httpbin.org/post` with Body raw JSON and `Content-Type: application/json`.
    4. Confirm response JSON echoes your headers/body; screenshot or paste into notes.
    5. **Done when:** Both calls return `200` and echo shows your headers/JSON.

- [ ] **Day 5 — JSON schemas by hand**
  - [ ] **Learn:** [Working with JSON](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Objects/JSON) · [OpenAPI Schema Object](https://spec.openapis.org/oas/v3.0.3#schema-object)
  - [ ] **Practice:** Author Task / list / error JSON shapes
    1. Write example JSON for one `Task` (`id`, `title`, `status`, `createdAt`).
    2. Write a JSON array of 2 tasks (list response).
    3. Write `ErrorMessage` example: `{ "error": "...", "details": null }`.
    4. Check types mentally (string vs number id — pick one and stay consistent).
    5. Save as `examples/task.json`, `tasks.json`, `error.json`.
    6. **Done when:** Three example files exist and field names are consistent.

- [ ] **Day 6 — Task Board resource design**
  - [ ] **Learn:** [HTTP methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods) · [OpenAPI Paths](https://spec.openapis.org/oas/v3.0.3#paths-object)
  - [ ] **Practice:** Paths + methods + success statuses table
    1. Design collection `/api/tasks` and item `/api/tasks/{id}`.
    2. Table columns: Operation, Method, Path, Success status, Body (yes/no).
    3. Include list, create (`201`), get, update (`PUT`/`PATCH`), delete (`204` or `200`).
    4. Add query param idea: `?status=todo`.
    5. **Done when:** Resource table is written and matches noun-style URLs.

- [ ] **Day 7 — Cookies & sessions**
  - [ ] **Learn:** [HTTP cookies](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies) · [Set-Cookie](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie) · [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)
  - [ ] **Practice:** Observe cookies and note CORS contrast
    1. Visit a site you’re logged into; DevTools → Application → Cookies; note names (do not share values).
    2. In Network, find a response with `Set-Cookie` if present; note `HttpOnly`/`Secure` flags if shown.
    3. Write 5–8 lines: cookie session vs later `Authorization: Bearer` token.
    4. One sentence on why a browser JS app calling another origin hits CORS.
    5. **Done when:** Notes cover cookies/session vs token and mention CORS.

### Week 2 — Postman, OpenAPI, contract freeze (Days 8–14)

- [ ] **Day 8 — Postman setup & environment**
  - [ ] **Learn:** [Postman Getting Started](https://learning.postman.com/docs/getting-started/overview/) · [Variables](https://learning.postman.com/docs/sending-requests/variables/)
  - [ ] **Practice:** Environment and Tasks collection folder
    1. Create Postman Environment `local` with variable `baseUrl` (e.g. `https://httpbin.org` or mock URL).
    2. Create Collection `Task Board`; folder `Tasks`.
    3. Add request `GET {{baseUrl}}/get` (or `/api/tasks` if mock ready); select env `local`; Send.
    4. Confirm status `200` (or documented mock status).
    5. **Done when:** Env variable resolves in the URL bar and request succeeds.

- [ ] **Day 9 — Collection requests & path vars**
  - [ ] **Learn:** [Sending requests](https://learning.postman.com/docs/sending-requests/requests/) · [Variables](https://learning.postman.com/docs/sending-requests/variables/)
  - [ ] **Practice:** CRUD requests with `:id` path variable
    1. Add requests matching Day 6 table: GET list, POST create, GET by id, PATCH/PUT, DELETE.
    2. Use path variable `:id` (or `{{taskId}}`) on item routes.
    3. For POST, set Body → raw → JSON with `Content-Type: application/json`.
    4. Against httpbin or mock: send POST; expect `200` (httpbin) or `201` (real mock).
    5. **Done when:** All planned requests exist and at least GET + POST return success statuses.

- [ ] **Day 10 — Postman test scripts**
  - [ ] **Learn:** [Writing tests](https://learning.postman.com/docs/writing-scripts/test-scripts/) · [Test script examples](https://learning.postman.com/docs/writing-scripts/script-references/test-examples/)
  - [ ] **Practice:** Assert status and JSON fields
    1. On GET list (or httpbin `/get`): Tests tab → `pm.response.to.have.status(200)`.
    2. Assert a JSON property exists (e.g. `pm.expect(json.url).to.be.ok` on httpbin, or `title` on Task).
    3. On POST create (when mock returns `201`): assert status `201`.
    4. Run requests; confirm green tests in Postman Test Results.
    5. **Done when:** At least two requests have passing automated status assertions.

- [ ] **Day 11 — Error-case documentation**
  - [ ] **Learn:** [HTTP status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status) · [Writing tests](https://learning.postman.com/docs/writing-scripts/test-scripts/)
  - [ ] **Practice:** Document and assert 4xx cases
    1. Document expected codes: missing/invalid body → `400`; unknown id → `404`; illegal status transition → `409` or `422` (pick one).
    2. Create Postman requests that trigger errors against mock (or note intended codes if mock can’t yet).
    3. Add test: `pm.response.to.have.status(404)` (or your chosen code).
    4. Write example error JSON next to each case.
    5. **Done when:** Error matrix exists and ≥1 failing-path assertion is written.

- [ ] **Day 12 — OpenAPI 3.x sketch**
  - [ ] **Learn:** [OpenAPI 3.0.3 Specification](https://spec.openapis.org/oas/v3.0.3) · [OpenAPI Guide](https://swagger.io/docs/specification/about/)
  - [ ] **Practice:** Write openapi-task-board.yaml skeleton
    1. Create `openapi-task-board.yaml` with `openapi: 3.0.3`, `info`, `paths`.
    2. Add `components.schemas.Task` with properties/types/required.
    3. Document `GET /api/tasks` and `POST /api/tasks` with response codes `200`/`201`.
    4. Reference Task schema in responses.
    5. **Done when:** File exists with info + ≥2 paths + Task schema.

- [ ] **Day 13 — Swagger Editor validation**
  - [ ] **Learn:** [Swagger Editor](https://editor.swagger.io/) · [OpenAPI 3.0.3](https://spec.openapis.org/oas/v3.0.3) · [Basic structure](https://swagger.io/docs/specification/basic-structure/)
  - [ ] **Practice:** Validate and fix in Swagger Editor
    1. Open https://editor.swagger.io/; paste your YAML.
    2. Fix all editor errors (indentation, `$ref`, missing types).
    3. Add remaining paths (GET/PUT|PATCH/DELETE by id) + `404` response component.
    4. Add one request/response `example` per major operation.
    5. Save the cleaned file back to disk.
    6. **Done when:** Swagger Editor shows the UI with no errors and examples visible.

- [ ] **Day 14 — Freeze contract & mastery dry-run**
  - [ ] **Learn:** [Postman mock servers](https://learning.postman.com/docs/designing-and-developing-your-api/mocking-data/setting-up-mock/) · [OpenAPI Specification](https://swagger.io/specification/)
  - [ ] **Practice:** Freeze v1 artifacts for the phase project
    1. Export Postman collection JSON into your learning repo.
    2. Optionally create a Postman Mock Server from the collection; hit `GET /api/tasks` → expect `200` + Task-shaped JSON.
    3. Write README section: how to import collection, select env, run smoke tests.
    4. Walk §12 mastery checklist; note gaps.
    5. **Done when:** OpenAPI + Postman export are frozen and checklist dry-run is done.

*If a day runs long, prefer finishing the resource table + Postman happy path over perfect OpenAPI examples — expand examples on Day 13.*

---

## 5. Hands-on exercises

### HTTP & status codes

1. Map CRUD for `Book` to methods + paths + success statuses.
2. Decide: update email with `PUT` whole user vs `PATCH` email only — document tradeoff.
3. For “delete already deleted id,” choose `404` vs `204` — pick one policy and stay consistent.
4. Explain why `GET /tasks/delete/1` is not RESTful.

### Headers & bodies

5. Send JSON POST to httpbin `/post`; confirm echoed JSON.
6. Send wrong `Content-Type` and observe behavior on a mock that validates it.
7. Use query params vs path params appropriately in 5 example URLs.

### Cookies / sessions (conceptual labs)

8. Log into a site you already use; inspect `Set-Cookie` in Network (learning observation only).
9. Write a short note: cookie session vs sending API key/token header — when UIs use which.

### Postman

10. Create an environment switch: `mock` vs `local` base URLs.
11. Chain requests: create task → save `id` to collection variable → GET by id.
12. Write a test script: `pm.response.to.have.status(201)` and JSON schema-ish field checks.
13. Folder organize: `Tasks` / `Health` / `Errors`.
14. Export collection JSON into your project repo (learning artifacts).

### OpenAPI

15. Define `Task` schema: `id`, `title`, `status`, `createdAt` with types and required fields.
16. Document `GET /tasks` response as array of Task.
17. Document `POST /tasks` request body + `201` response.
18. Add `404` response component reused by `GET`/`PUT`/`DELETE` by id.
19. Add an example payload for each operation.
20. Generate a mock from Postman/OpenAPI if available; hit it from topic 09 UI.

---

## 6. Mini-projects

> **Build guides (checkboxes + steps):** open the linked markdown before coding.

### A. HTTP Pocket Guide (your notes repo)

**Guide:** [A — HTTP Pocket Guide](../mini-projects/phase-03-frontend-api/10-rest-http/A-http-pocket-guide.md)

- One-page markdown cheat sheet: methods, statuses you actually use, header checklist.
- Include 5 “scenario → status” flash rows.
- Keep it personal — this becomes your Phase 4 quick reference.

### B. Task Board API Contract v1

**Guide:** [B — Task Board API Contract](../mini-projects/phase-03-frontend-api/10-rest-http/B-task-board-api-contract.md)

- Resource table + OpenAPI file (`openapi-task-board.yaml`).
- Postman collection with happy path + ≥1 error per mutating endpoint.
- Example payloads committed next to the spec.
- **This is required input** for [phase-project.md](phase-project.md).

### C. (Stretch) Public API Exploration

**Guide:** [C — Public API Exploration](../mini-projects/phase-03-frontend-api/10-rest-http/C-public-api-exploration.md)

- Pick a free documented public API (e.g. a government or open data API with clear docs).
- Import or hand-build 5 Postman requests.
- Note which REST rules it follows or breaks (learning critique, not complaining).

---

## 7. Industry use cases

- **Microservices & backends:** Nearly all Spring Boot apps expose REST/JSON over HTTP.
- **Mobile & SPA clients:** Same contracts consumed by web JS and mobile apps.
- **Partner integrations:** External systems integrate via documented OpenAPI contracts.
- **Gateway & ops:** Load balancers and API gateways route and observe HTTP methods/statuses.
- **Testing & QA:** Postman/Newman collections run in CI as smoke tests.
- **Support/debugging:** Reading status codes and correlation headers is daily production literacy.

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Verbs in URLs (`/getTasks`, `/createTask`) | Nouns for resources; HTTP method carries the verb |
| Everything returns `200` with error in body | Use proper 4xx/5xx; body explains details |
| `GET` that creates/deletes | Never; GET must be safe |
| Ignoring idempotency | Design `PUT`/`DELETE` so retries are safe |
| Inconsistent error shapes | One error JSON schema for the whole API |
| No `Content-Type` on JSON bodies | Always `application/json` for JSON APIs |
| Path param vs body id mismatch | Single source of truth; prefer path id for item ops |
| Huge unbounded list endpoints | Plan pagination/filter query params early |
| Documenting after coding only | Contract-first (or parallel) reduces UI rework |
| Testing only happy path | Every collection needs deliberate 4xx cases |
| Confusing 401 vs 403 | 401 = unauthenticated; 403 = authenticated but not allowed |
| Treating cookies as “secure storage” for secrets in JS | HttpOnly cookies aren’t readable by JS — know the model |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [MDN — HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP) | Methods, status, headers |
| [MDN — HTTP messages](https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages) | Request/response structure |
| [MDN — CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) | Browser cross-origin rules |
| [MDN — Cookies](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies) | Cookie model |
| [Postman Learning Center](https://learning.postman.com/docs/getting-started/overview/) | Collections, environments, tests |
| [OpenAPI Specification](https://swagger.io/specification/) | Official OpenAPI reference |
| [Swagger Editor](https://editor.swagger.io/) | Author & visualize OpenAPI free in browser |
| [OpenAPI Guide (Swagger)](https://swagger.io/docs/specification/about/) | Practical OpenAPI tutorials |
| [httpbin.org](https://httpbin.org/) | Free HTTP echo playground |
| [RFC 9110 HTTP Semantics](https://www.rfc-editor.org/rfc/rfc9110) | Skim when you need precision (advanced) |

Roy Fielding’s REST dissertation is historical context — optional; prefer MDN + your own contract practice for this phase.

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Full index:

- HTTP overview: https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview
- HTTP request methods: https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
- HTTP status codes: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
- HTTP headers: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers
- Content-Type: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type
- Accept: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept
- Cookie header: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cookie
- Set-Cookie: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie
- CORS: https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
- JSON MDN: https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Objects/JSON
- Postman sending requests: https://learning.postman.com/docs/sending-requests/requests/
- Postman variables: https://learning.postman.com/docs/sending-requests/variables/
- Postman writing tests: https://learning.postman.com/docs/writing-scripts/test-scripts/
- Postman mock servers: https://learning.postman.com/docs/designing-and-developing-your-api/mocking-data/setting-up-mock/
- OpenAPI 3.0 specification: https://spec.openapis.org/oas/v3.0.3
- OpenAPI 3.1 specification: https://spec.openapis.org/oas/v3.1.0
- Swagger Editor: https://editor.swagger.io/
- Swagger OpenAPI docs hub: https://swagger.io/docs/
- httpbin: https://httpbin.org/
- WHATWG Fetch (browser fetch ↔ HTTP): https://fetch.spec.whatwg.org/

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. HTTP vocabulary | Methods + statuses + headers | Explain a captured Network request end-to-end |
| 2. JSON bodies | Read/write API payloads | Hand-author Task JSON without typos in types |
| 3. REST design | Resource table for Task Board | Paths/methods/statuses reviewed once without changing daily |
| 4. Postman fluency | Collection + env + tests | Happy path + error path automated assertions |
| 5. OpenAPI | Documented contract | YAML opens cleanly in Swagger Editor |
| 6. Mock ready | UI can target a base URL | Mock or stub returns contract-shaped JSON |
| 7. Gate | Ready for phase project | Mastery checklist ≥90%; contract frozen |

**Weekly rhythm:** Week 1 = theory + design; Week 2 = Postman + OpenAPI freeze. Do not start Spring until the contract is boringly clear.

**Bridge to phase project:** Mini-project B *is* the API half of the Task Board. UI work consumes this contract; do not invent endpoints ad hoc in HTML/JS.

---

## 12. Mastery checklist

- [ ] Explains HTTP request/response parts (line, headers, body) in plain language
- [ ] Chooses appropriate methods for list/create/read/update/delete operations
- [ ] Distinguishes safe vs idempotent behavior at a practical level
- [ ] Picks sensible status codes for success and common client errors
- [ ] Uses `Content-Type: application/json` and reads `Accept` intentionally
- [ ] Models resources as nouns with collection/item URL patterns
- [ ] Designs query params for filter/sort/pagination *ideas* on list endpoints
- [ ] Describes what cookies and sessions are for (browser ↔ server memory)
- [ ] States why CORS matters for browser JS calling APIs
- [ ] Builds a Postman collection with environments/variables
- [ ] Includes happy-path and at least one 4xx test per major endpoint
- [ ] Writes an OpenAPI 3.x document with schemas and paths for Task Board
- [ ] Opens and validates the contract in Swagger Editor
- [ ] Can sketch request/response for each Task Board endpoint from memory
- [ ] Completed Mini-projects A and B from §6
- [ ] Contract + Postman export saved for [phase-project.md](phase-project.md)
- [ ] Ready to build the Task Board phase project
