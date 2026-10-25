# Phase 3 Project — Task Board (UI + REST Contract + Postman)

**Duration:** ~2 weeks (14 days, after topics 09–10) · **Cadence:** 1 hour/day  
**Outcome:** A browser Task Board UI wired to a documented REST contract, verified with a Postman collection (mock server, JSON Server, or a thin Java HTTP stub — your choice)

---

## 1. Why this topic is important

Topics 09 and 10 teach pieces; this project forces them to **meet in the middle**. The UI must render columns, accept forms, and call real HTTP endpoints. The API contract must be specific enough that Postman and the browser agree on paths, statuses, and JSON shapes.

That skill — **UI ↔ contract ↔ verification** — is exactly how full-stack work happens before and after Spring. In Phase 4 you will replace the mock/stub with Spring Boot controllers; the OpenAPI and Postman artifacts should survive that swap with minimal change.

---

## 2. Prerequisites

- Topics [09](09-html-css-js.md) and [10](10-rest-http.md) mastery checklists ≥90% each
- Frozen Task Board API Contract v1 (OpenAPI file + Postman collection from topic 10 Mini-project B)
- Comfortable with semantic HTML, Flex/Grid or Bootstrap, DOM events, `fetch` + async/await
- Local way to serve static files (Live Server, `npx serve`, etc.)
- Optional but useful: Node.js for [JSON Server](https://github.com/typicode/json-server), or willingness to run a Postman mock

**Allowed backends for this phase (pick one):**

| Option | Good when |
|--------|-----------|
| Postman Mock Server from the collection | Fastest; no local server code |
| JSON Server + `db.json` | Local, easy CRUD, great for UI iteration |
| Thin Java `com.sun.net.httpserver` stub or similar | Ties to Java path; more work |
| Static JSON + only GET (temporary) | Not enough for DoD — mutating methods required |

Do **not** require Spring Boot yet. That is Phase 4.

---

## 3. Concepts to learn (beginner → intermediate → advanced)

This project **applies** topics 09–10; new learning is integration and product polish.

### Beginner (Project v1 — board shell)

| Focus | Apply |
|-------|--------|
| Board layout | Three columns: Todo / Doing / Done (or equivalent statuses) |
| Task card UI | Title, status badge, optional description snippet |
| Static seed | Hardcoded or local JSON until API is connected |
| Semantic structure | `main`, column regions, forms with labels |

### Intermediate (Project v2 — contract + CRUD)

| Focus | Apply |
|-------|--------|
| REST client module | One `api.js` (or similar) wrapping `fetch` |
| List/create/update/delete | Against frozen OpenAPI paths |
| Status move | `PATCH`/`PUT` to change status; card moves columns |
| Postman parity | Same payloads as UI uses |
| Error UX | Toast or inline error on 4xx/network failure |

### Advanced (Project v3 — polish & DoD)

| Focus | Apply |
|-------|--------|
| Loading/empty states | Per board and per action |
| Client validation | Title required; status enum enforced |
| Optimistic UI (optional) | Update UI then confirm — or wait for response (simpler; prefer wait) |
| CORS / same-origin setup | Document how UI reaches API base URL |
| README + demo script | New clone → run mock → open UI → Postman smoke |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Spec lock, mock, UI vertical slice (Days 1–7)

- [ ] **Day 1 — Repo layout & contract copy**
  - [ ] **Learn:** [OpenAPI 3.0.3](https://spec.openapis.org/oas/v3.0.3) · [Postman Getting Started](https://learning.postman.com/docs/getting-started/overview/)
  - [ ] **Practice:** Scaffold task-board folders and copy contract
    1. Create `task-board/` with `ui/`, `api-contract/`, `postman/`, `README.md`.
    2. Copy topic-10 OpenAPI → `api-contract/openapi.yaml`.
    3. Export/copy Postman collection → `postman/`.
    4. README skeleton: title, folders, “setup TBD”.
    5. **Done when:** All three folders have the contract artifacts and README exists.

- [ ] **Day 2 — Resource table lock**
  - [ ] **Learn:** [OpenAPI Paths](https://spec.openapis.org/oas/v3.0.3#paths-object) · [HTTP methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods) · [HTTP status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
  - [ ] **Practice:** Lock README table to OpenAPI
    1. Open `openapi.yaml`; list every path + method.
    2. Write README resource table: path, method, success status, example JSON fields.
    3. Diff against §6 suggested contract; if different, update the table to **your** freeze (not the other way around).
    4. Paste one example Task JSON into README.
    5. **Done when:** README table matches OpenAPI with no missing operations.

- [ ] **Day 3 — Mock / JSON Server live**
  - [ ] **Learn:** [Postman mock servers](https://learning.postman.com/docs/designing-and-developing-your-api/mocking-data/setting-up-mock/) · [JSON Server](https://github.com/typicode/json-server) · [Sending requests](https://learning.postman.com/docs/sending-requests/requests/)
  - [ ] **Practice:** Prove GET list returns contract-shaped JSON
    1. Choose backend: Postman Mock, JSON Server (`mock/db.json`), or thin Java stub.
    2. Seed ≥3 tasks with `todo`/`doing`/`done` statuses matching schema field names.
    3. In Postman: `GET {{baseUrl}}/api/tasks` (adjust path to your contract) → expect status `200`.
    4. Confirm response body is an array of Task-shaped objects.
    5. Document start command in README.
    6. **Done when:** Postman GET list returns `200` + contract-shaped data.

- [ ] **Day 4 — Board shell HTML/CSS**
  - [ ] **Learn:** [Flexbox](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Flexbox) · [Responsive design](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Responsive_Design) · [Bootstrap Grid](https://getbootstrap.com/docs/5.3/layout/grid/)
  - [ ] **Practice:** Three-column board with static cards
    1. Create `ui/index.html`, `ui/css/styles.css`.
    2. Three columns (Todo / Doing / Done) with headings; 2–3 static cards each.
    3. Desktop: columns side-by-side; mobile: stack (media query or Bootstrap).
    4. Open via Live Server; resize to ~360px and verify stack.
    5. **Done when:** Board shell looks correct at desktop and mobile widths.

- [ ] **Day 5 — api.js list + render**
  - [ ] **Learn:** [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) · [Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API) · [async function](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/async_function)
  - [ ] **Practice:** Fetch list and render into columns
    1. Create `ui/js/api.js` with `const BASE_URL = '...'` (or config) and `listTasks()`.
    2. `fetch` GET collection; check `response.ok`; `return response.json()`.
    3. In `app.js`, map tasks into the three column containers by `status`.
    4. Remove hardcoded seed cards (or replace on load).
    5. Network tab: confirm GET status `200`.
    6. **Done when:** Refreshing UI shows cards from the API, not only HTML hardcodes.

- [ ] **Day 6 — Create via POST**
  - [ ] **Learn:** [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) · [Working with JSON](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Objects/JSON) · [Constraint validation](https://developer.mozilla.org/en-US/docs/Web/HTML/Constraint_validation)
  - [ ] **Practice:** Create-task form posts JSON
    1. Add a form with labeled title (required) + optional description.
    2. On submit: `preventDefault`; if title empty, show validation error and stop.
    3. `POST` with headers `Content-Type: application/json` and body matching OpenAPI.
    4. Expect status `201` (or your documented success); then re-fetch list or append card.
    5. Clear the form on success.
    6. **Done when:** Creating a task in the UI persists and appears after refresh.

- [ ] **Day 7 — E2E create+list & CORS**
  - [ ] **Learn:** [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) · [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) · [Postman variables](https://learning.postman.com/docs/sending-requests/variables/)
  - [ ] **Practice:** Stabilize create+list across origins
    1. Demo create then list from UI without console CORS errors.
    2. If blocked: fix mock CORS, proxy, or same-origin serve; document the fix in README.
    3. Align Postman `baseUrl` with the UI config.
    4. Write 5 lines in notes: final `baseUrl`, how UI is served, how API is started.
    5. **Done when:** Create+list works in UI and Postman against the same base URL.

### Week 2 — Mutations, Postman completeness, Definition of Done (Days 8–14)

- [ ] **Day 8 — Update title/description**
  - [ ] **Learn:** [HTTP methods (PUT/PATCH)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods) · [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch)
  - [ ] **Practice:** Edit task via PUT or PATCH
    1. Add edit UI (inline input or small form/modal) for title/description.
    2. Call `PUT` or `PATCH` `/api/tasks/{id}` per contract with JSON body.
    3. Expect status `200`; update the card text from response or re-fetch.
    4. Verify in Postman GET by id that fields changed.
    5. **Done when:** UI edit persists and GET-by-id shows new values.

- [ ] **Day 9 — Status moves between columns**
  - [ ] **Learn:** [HTTP PATCH](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods#patch) · [HTTP status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status) · [DOM](https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model)
  - [ ] **Practice:** Move cards with status PATCH/PUT
    1. Add controls to move a card (buttons or select) to another status.
    2. Send `{ "status": "doing" }` (or full PUT) — expect `200`.
    3. Re-render so the card appears in the target column.
    4. Refresh page — card stays in the new column (mock persistence).
    5. **Done when:** Status moves work in UI and survive refresh.

- [ ] **Day 10 — Delete + 404 handling**
  - [ ] **Learn:** [HTTP DELETE](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods#delete) · [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) · [404](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404)
  - [ ] **Practice:** Delete with confirm and 404 UX
    1. Delete button → `confirm(...)` → `DELETE /api/tasks/{id}`.
    2. Expect `204` (or documented success); remove card from DOM / re-fetch.
    3. Call DELETE again on same id (or fake id); handle `404` with a visible message (no crash).
    4. Confirm in Network tab status codes match expectations.
    5. **Done when:** Delete works once and second delete surfaces `404` gracefully.

- [ ] **Day 11 — Postman happy-path assertions**
  - [ ] **Learn:** [Writing tests](https://learning.postman.com/docs/writing-scripts/test-scripts/) · [Test examples](https://learning.postman.com/docs/writing-scripts/script-references/test-examples/) · [Variables](https://learning.postman.com/docs/sending-requests/variables/)
  - [ ] **Practice:** Automate happy-path status checks
    1. Ensure collection covers list/create/get/update/delete.
    2. Tests: list `200`, create `201`, get `200`, update `200`, delete `204` (adjust to your contract).
    3. Chain: save `id` from create into `pm.collectionVariables` for later requests.
    4. Run the folder; all happy-path tests green.
    5. **Done when:** Full happy-path folder run passes with status assertions.

- [ ] **Day 12 — Postman error cases**
  - [ ] **Learn:** [HTTP status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status) · [Writing tests](https://learning.postman.com/docs/writing-scripts/test-scripts/) · [OpenAPI Responses](https://spec.openapis.org/oas/v3.0.3#responses-object)
  - [ ] **Practice:** Assert 400 and 404 error paths
    1. POST with missing title → expect `400` (or your validation code); assert in Tests.
    2. GET unknown id → expect `404`; assert.
    3. Document expected error JSON bodies in README or `api-contract/examples/`.
    4. Align OpenAPI `responses` if they were missing these codes.
    5. **Done when:** ≥2 error requests have passing status assertions and documented bodies.

- [ ] **Day 13 — UX & accessibility polish**
  - [ ] **Learn:** [Accessibility](https://developer.mozilla.org/en-US/docs/Learn/Accessibility) · [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) · [`<label>`](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/label)
  - [ ] **Practice:** Loading, empty, error, and a11y pass
    1. Show loading indicator while fetching the board.
    2. Empty-column message when a column has zero tasks.
    3. Global/inline error banner on failed fetch/mutations.
    4. Verify labels, focus order, and button names via keyboard tab.
    5. **Done when:** Loading/empty/error are demoable and forms are labeled.

- [ ] **Day 14 — Definition of Done & Phase 4 notes**
  - [ ] **Learn:** [Swagger Editor](https://editor.swagger.io/) · [OpenAPI 3.0.3](https://spec.openapis.org/oas/v3.0.3) · [Postman overview](https://learning.postman.com/docs/getting-started/overview/)
  - [ ] **Practice:** DoD pass, demo script, Spring prep
    1. Walk §12 Definition of Done; fix failures.
    2. Run §11 demo script steps 1–7 once end-to-end.
    3. Re-open OpenAPI in Swagger Editor — zero errors.
    4. Notes: which Spring `@RestController` methods will implement each path.
    5. **Done when:** DoD boxes pass and Phase 4 notes + demo script are recorded.

*On a compressed pace: merge Days 11–12 into one Postman session; keep UI mutations non-negotiable.*

---

## 5. Hands-on exercises

Use these as spikes before or while integrating:

1. Render an array of tasks into three column containers with `document.createElement`.
2. Prototype `fetch` wrapper: `async function request(path, options)` that throws on `!ok`.
3. Map status string → column element id.
4. Form → JSON body builder matching OpenAPI schema exactly (field names!).
5. Simulate 400/404 by pointing at wrong URLs; ensure UI does not crash.
6. Postman: save `id` from POST response; chain GET/PATCH/DELETE.
7. OpenAPI: add `examples` if missing; re-validate in Swagger Editor.
8. Optional: filter chips (All / Todo / …) as client-side filter on already-fetched list.

---

## 6. Mini-projects

The phase project **is** the deliverable, shipped as progressive versions. Treat the feature list below as the product spec.

**Build guide (checkboxes + steps):** [Task Board](../mini-projects/phase-03-frontend-api/phase-project/task-board.md)

### Feature specification

**Board UI**

- [Must] Three status columns (e.g. `todo`, `doing`, `done`) with clear headings
- [Must] Responsive layout: columns side-by-side on desktop; stacked on narrow viewports
- [Must] Task cards show at least `title` and `status`
- [Should] Show description excerpt and/or timestamps if present in API
- [Should] Empty-column message when a column has zero tasks
- [Could] Simple color accent per status (CSS variables)

**Task operations (via REST)**

- [Must] List all tasks (`GET` collection)
- [Must] Create task (`POST`) with required `title`
- [Must] Update task fields and/or status (`PUT` and/or `PATCH` per your contract)
- [Must] Delete task (`DELETE`)
- [Must] After each successful mutation, UI reflects server state (re-fetch or patch local model carefully)
- [Should] Client-side check: non-empty title before POST
- [Could] Inline edit title

**REST contract**

- [Must] OpenAPI 3.x file committed (`api-contract/openapi.yaml` or `.json`)
- [Must] Paths/schemas match what the UI actually calls
- [Must] Document success statuses (`200`/`201`/`204` as chosen) and JSON shapes
- [Should] Shared `Error` schema for 4xx responses
- [Should] Examples for request/response bodies

**Postman**

- [Must] Collection covering list/create/get-by-id/update/delete (as applicable)
- [Must] Environment variable for `baseUrl`
- [Must] At least one 4xx test case with assertion
- [Should] Test scripts asserting status codes on happy paths
- [Could] Newman-ready export note for future CI (optional text only)

**Quality**

- [Must] Separate UI static files from contract/Postman artifacts
- [Must] README: how to start mock/API, how to open UI, how to run Postman smoke
- [Must] No secrets committed
- [Should] Loading and error states visible to the user
- [Could] `localStorage` draft for create form (optional)

### Suggested REST contract (align OpenAPI to this unless you already froze equivalent names)

| Operation | Method & path | Success | Body |
|-----------|---------------|---------|------|
| List tasks | `GET /api/tasks` | `200` | `Task[]` |
| Get task | `GET /api/tasks/{id}` | `200` | `Task` |
| Create | `POST /api/tasks` | `201` | req: `{ "title", "description?", "status?" }` → `Task` |
| Replace/update | `PUT /api/tasks/{id}` | `200` | full `Task` write shape |
| Partial update | `PATCH /api/tasks/{id}` | `200` | e.g. `{ "status" }` |
| Delete | `DELETE /api/tasks/{id}` | `204` | empty |

**Task schema (minimum):**

```json
{
  "id": "string-or-number",
  "title": "string",
  "description": "string",
  "status": "todo | doing | done",
  "createdAt": "ISO-8601 string (optional but recommended)"
}
```

**Error schema (recommended):**

```json
{
  "error": "Human-readable message",
  "details": null
}
```

If your topic-10 freeze used slightly different paths, **keep the freeze** — update this table to match your file, not the other way around mid-project.

---

## 7. Industry use cases

- **Kanban / issue trackers:** Jira-like boards are status columns + card CRUD over APIs
- **Support queues:** Tickets move through states with the same list/update patterns
- **CMS editorial workflows:** Draft → review → published mirrors status transitions
- **Contract-first teams:** OpenAPI + Postman (or Insomnia) before implementation is common
- **Frontend/backend split:** UI engineers develop against mocks while API is built — you practice that split here
- **Spring Boot next step:** Controllers will implement this same contract with `@RestController`

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| UI invents fields not in OpenAPI | Contract is source of truth; change spec first |
| Hard-coding production URLs | Config/`baseUrl` variable only |
| Ignoring `response.ok` | Centralize in `api.js`; map errors to UI |
| Re-rendering only one column after create | Re-fetch list or insert into correct column consistently |
| POST without `Content-Type: application/json` | Always set JSON headers |
| Postman collection drifting from UI | After UI changes, re-export/update collection same day |
| Mock returns different shape than OpenAPI | Fix mock data to match schemas |
| CORS blocked and “fetch is broken” blame | Serve UI and understand mock CORS settings; document setup |
| Giant `script` in HTML | Modules or separate JS files |
| No README run steps | A clean-folder run must work from written steps alone |
| Building Spring “just a little” | Out of scope; keep mock/stub until Phase 4 |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| Topic [09](09-html-css-js.md) / [10](10-rest-http.md) | Concept refresh while building |
| [MDN Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) | UI HTTP client |
| [Postman Learning Center](https://learning.postman.com/docs/getting-started/overview/) | Collections, mocks, tests |
| [Swagger Editor](https://editor.swagger.io/) | Validate OpenAPI |
| [JSON Server (GitHub)](https://github.com/typicode/json-server) | Free local REST mock |
| [Bootstrap 5 Docs](https://getbootstrap.com/docs/5.3/getting-started/introduction/) | Optional UI speed |
| [MDN CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) | When browser blocks calls |

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Full index:

- Fetch API: https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API
- Using Fetch: https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
- HTTP methods: https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
- HTTP status codes: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
- OpenAPI 3.0.3: https://spec.openapis.org/oas/v3.0.3
- Swagger Editor: https://editor.swagger.io/
- Postman requests: https://learning.postman.com/docs/sending-requests/requests/
- Postman variables: https://learning.postman.com/docs/sending-requests/variables/
- Postman test scripts: https://learning.postman.com/docs/writing-scripts/test-scripts/
- Postman mock servers: https://learning.postman.com/docs/designing-and-developing-your-api/mocking-data/setting-up-mock/
- JSON Server README: https://github.com/typicode/json-server#readme
- Java HttpServer (if using thin stub): https://docs.oracle.com/en/java/javase/17/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html
- MDN JSON: https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Objects/JSON

---

## 11. Practice roadmap

### Evolution path

```text
v1 Board shell + static cards
 → v2 GET/POST against mock + OpenAPI aligned
 → v3 Update/delete/status moves + Postman error paths + DoD
```

### Week plan (summary)

| Week | Deliverable |
|------|-------------|
| 1 | Contract in repo; mock live; UI lists & creates tasks |
| 2 | Full CRUD/status moves; Postman happy + error; README demo script; DoD met |

### Suggested folder layout

```text
task-board/
  README.md
  ui/
    index.html
    css/styles.css
    js/api.js
    js/app.js
  api-contract/
    openapi.yaml
    examples/
  postman/
    task-board.postman_collection.json
    local.postman_environment.json
  mock/                 # if JSON Server
    db.json
```

### How this prepares for Phase 4 (Spring)

| Phase 3 artifact | Phase 4 direction |
|------------------|-------------------|
| OpenAPI paths/schemas | `@RestController` + DTOs matching schemas |
| Postman collection | Regression smoke against Boot app |
| `api.js` base URL | Point to `http://localhost:8080` |
| Status enum | Java `enum` + validation annotations |
| Mock/JSON Server | Replace with service + repository (JDBC/JPA later) |
| Error JSON shape | `@ControllerAdvice` consistent errors |

**Phase 4 gate hint:** Keep field names and statuses stable so Spring implements the contract instead of renegotiating the UI.

### Demo script (manual test — do this on Day 14)

1. Start mock/API; confirm Postman `GET /api/tasks` → `200`.
2. Open UI; confirm columns render; empty states OK.
3. Create a task from UI; appears in Todo (or default status).
4. Move to Doing, then Done; refresh page — state persists in mock.
5. Edit title; delete task; confirm removal.
6. Postman: replay create with missing title → expect `400` (or your documented validation code).
7. Postman: `GET` unknown id → `404`.

---

## 12. Mastery checklist

### Definition of Done (project)

- [ ] Static UI opens via documented local steps
- [ ] Mock/stub/API starts via documented steps and matches OpenAPI examples
- [ ] `GET` list populates the board from the API (not only hardcoded cards)
- [ ] Create task works from the UI and persists in the API/mock
- [ ] Update and/or status move works from the UI
- [ ] Delete works from the UI
- [ ] OpenAPI file is committed and opens cleanly in Swagger Editor
- [ ] Postman collection covers happy path for each exposed operation
- [ ] Postman includes ≥1 asserted error case (`4xx`)
- [ ] UI shows loading and error feedback (no silent failures)
- [ ] README includes architecture sketch (UI ↔ contract ↔ mock) and demo script
- [ ] Personal notes: which classes/endpoints Spring will provide in Phase 4

### Personal mastery

- [ ] Can explain every endpoint’s request/response without opening the YAML
- [ ] Can add a new optional field (e.g. `priority`) by updating OpenAPI → mock → UI → Postman in one session
- [ ] Can diagnose a failed `fetch` using Network tab + status/body
- [ ] Topics 09–10 checklists remain ≥90%; no skipped HTTP basics
- [ ] Phase 3 gate ready (see [learning-path.md](../00-master-guide/learning-path.md) Phase 3 gate)
- [ ] Ready to start Phase 4 — Spring Core (when available)

---

## Quick reference — success criteria

You are done with Phase 3 when the Task Board UI, OpenAPI contract, and Postman collection tell the **same story**, and a clean folder run follows your README without tribal knowledge. Do not start Spring until creating, moving, and deleting a task feels routine.
