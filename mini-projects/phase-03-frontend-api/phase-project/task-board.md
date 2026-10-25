# Mini-Project — Task Board (UI + REST Contract + Postman)
**Phase:** 3 — Frontend & HTTP (phase project)  
**Topic:** [Phase 3 Project](../../../phase-03-frontend-api/phase-project.md)  
**Time:** ~2 weeks at 1 hour/day

## Goal

Ship a browser Task Board with three status columns, wired to a frozen REST contract, verified with Postman (and a mock/JSON Server/thin stub). UI, OpenAPI, and Postman must tell the same story — ready for Spring to implement the contract in Phase 4.

## Prerequisites

- [ ] Topics [09](../../../phase-03-frontend-api/09-html-css-js.md) and [10](../../../phase-03-frontend-api/10-rest-http.md) mastery ≥90%
- [ ] Frozen contract from [B — Task Board API Contract](../10-rest-http/B-task-board-api-contract.md)
- [ ] Comfortable with `fetch` + async/await, DOM events, Flex/Grid or Bootstrap
- [ ] Way to serve static files (Live Server, `npx serve`, etc.)

**Allowed backends (pick one):** Postman Mock Server · JSON Server + `db.json` · thin Java `HttpServer` stub. No Spring Boot yet.

## Learn (open before coding)

- [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch)
- [OpenAPI 3.0.3](https://spec.openapis.org/oas/v3.0.3)
- [Postman mock servers](https://learning.postman.com/docs/designing-and-developing-your-api/mocking-data/setting-up-mock/)
- [JSON Server](https://github.com/typicode/json-server)
- [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)
- Full spec: [phase-project.md §6](../../../phase-03-frontend-api/phase-project.md)

## Project layout

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
  mock/                     # if JSON Server
    db.json
```

## Build steps

### Step 1 — Repo scaffold & contract copy
- [ ] Create folders `ui/`, `api-contract/`, `postman/`
- [ ] Copy OpenAPI → `api-contract/openapi.yaml`
- [ ] Copy/export Postman collection + env → `postman/`
- [ ] README skeleton: title, folders, setup TBD
- **Done when:** Contract artifacts live in-repo next to empty UI.

### Step 2 — Lock resource table in README
- [ ] List every path + method from OpenAPI into a README table (success status + body)
- [ ] Paste one example Task JSON
- [ ] If your freeze differs from the suggested table below, **keep your freeze**

  | Operation | Method & path | Success |
  |-----------|---------------|---------|
  | List | `GET /api/tasks` | `200` |
  | Get | `GET /api/tasks/{id}` | `200` |
  | Create | `POST /api/tasks` | `201` |
  | Update | `PUT`/`PATCH /api/tasks/{id}` | `200` |
  | Delete | `DELETE /api/tasks/{id}` | `204` |

- **Done when:** README table matches OpenAPI with no missing operations.

### Step 3 — Mock / JSON Server live
- [ ] Choose backend; seed ≥3 tasks with `todo` / `doing` / `done`
- [ ] Example JSON Server `mock/db.json`:
  ```json
  {
    "tasks": [
      { "id": "1", "title": "Draft contract", "description": "", "status": "done", "createdAt": "2026-10-20T09:00:00Z" },
      { "id": "2", "title": "Build board UI", "description": "", "status": "doing", "createdAt": "2026-10-21T09:00:00Z" },
      { "id": "3", "title": "Wire fetch", "description": "", "status": "todo", "createdAt": "2026-10-22T09:00:00Z" }
    ]
  }
  ```
  *(Adjust paths: JSON Server often serves `/tasks` — align OpenAPI or use a rewrite/proxy; document the actual paths you call.)*
- [ ] Postman: `GET {{baseUrl}}/api/tasks` (or your path) → `200` + Task-shaped array
- [ ] Document start command in README
- **Done when:** Postman GET list returns contract-shaped data.

### Step 4 — Board shell HTML/CSS
- [ ] Files: `ui/index.html`, `ui/css/styles.css`
- [ ] Three columns: Todo / Doing / Done with headings; 2–3 static cards each initially
- [ ] Desktop side-by-side; mobile stack (media query or Bootstrap)
- [ ] Open via Live Server; verify ~360px
- **Done when:** Board shell looks correct at desktop and mobile.

### Step 5 — api.js list + render
- [ ] `ui/js/api.js`:
  ```js
  const BASE_URL = 'http://localhost:3000'; // or mock URL — config only
  export async function listTasks() {
    const res = await fetch(`${BASE_URL}/api/tasks`);
    if (!res.ok) throw new Error(`List failed: ${res.status}`);
    return res.json();
  }
  ```
- [ ] `app.js`: map tasks into columns by `status`; replace hardcoded cards
- [ ] Network tab: GET `200`
- **Done when:** Refresh shows cards from the API.

### Step 6 — Create via POST
- [ ] Form: labeled title (required) + optional description
- [ ] Submit: `preventDefault`; validate title; `POST` with `Content-Type: application/json`
- [ ] Expect `201` (or your documented success); re-fetch or append card; clear form
- [ ] Example body:
  ```json
  { "title": "New task", "description": "", "status": "todo" }
  ```
- **Done when:** Create persists and appears after refresh.

### Step 7 — Stabilize CORS / baseUrl
- [ ] Demo create+list without console CORS errors
- [ ] Fix via mock CORS, proxy, or same-origin serve; document in README
- [ ] Align Postman `baseUrl` with UI config
- **Done when:** Same base URL works in UI and Postman.

### Step 8 — Update title/description
- [ ] Edit UI (inline or modal)
- [ ] `PUT` or `PATCH /api/tasks/{id}` per contract
- [ ] Expect `200`; update card or re-fetch
- [ ] Verify with Postman GET by id
- **Done when:** Edits persist server-side.

### Step 9 — Status moves between columns
- [ ] Buttons/select to move card → send `{ "status": "doing" }` (or full PUT)
- [ ] Re-render into target column; refresh keeps placement
- **Done when:** Moves work in UI and survive refresh.

### Step 10 — Delete + 404 handling
- [ ] Delete → `confirm` → `DELETE /api/tasks/{id}` → expect `204`
- [ ] Second delete / fake id → handle `404` with visible message (no crash)
- [ ] Network tab statuses match expectations
- **Done when:** Delete once works; repeat surfaces `404` gracefully.

### Step 11 — Postman happy-path assertions
- [ ] Collection covers list/create/get/update/delete
- [ ] Tests: list `200`, create `201`, get `200`, update `200`, delete `204` (adjust to contract)
- [ ] Chain: save `id` from create into collection variable
- [ ] Folder run all green
- **Done when:** Full happy-path folder passes.

### Step 12 — Postman error cases + UX polish + DoD
- [ ] POST missing title → `400` asserted; GET unknown id → `404` asserted
- [ ] Document error JSON in README or `api-contract/examples/`
- [ ] UI: loading indicator, empty-column messages, error banner
- [ ] Labels + keyboard tab order
- [ ] Walk [phase-project.md §12](../../../phase-03-frontend-api/phase-project.md) DoD
- [ ] Notes: which Spring `@RestController` methods will implement each path
- [ ] Re-open OpenAPI in Swagger Editor — zero errors
- **Done when:** DoD boxes pass; demo script runnable from README.

## Definition of Done

- [ ] UI opens via documented local steps
- [ ] Mock/API starts via documented steps; matches OpenAPI examples
- [ ] GET list populates board from API
- [ ] Create / update-or-status-move / delete work from UI
- [ ] OpenAPI committed; Swagger Editor clean
- [ ] Postman happy path + ≥1 asserted `4xx`
- [ ] Loading and error feedback visible
- [ ] README: architecture sketch + demo script; Phase 4 notes present

## Demo script

1. Start mock/API; Postman `GET /api/tasks` → `200`
2. Open UI; columns render; empty states OK
3. Create a task → appears in Todo
4. Move to Doing, then Done; refresh — persists
5. Edit title; delete task; confirm removal
6. Postman: create with missing title → `400`
7. Postman: `GET` unknown id → `404`

## Common mistakes

| Mistake | Fix |
|--------|-----|
| UI invents fields not in OpenAPI | Change spec first |
| Hard-coded URLs everywhere | Single `BASE_URL` / env |
| Ignoring `response.ok` | Centralize in `api.js` |
| Postman drifting from UI | Update collection same day |
| Building Spring “just a little” | Keep mock/stub until Phase 4 |
| CORS blamed as “fetch broken” | Document serve + mock CORS setup |

## After this

- Phase 3 gate complete → Phase 4 Spring (implement the same OpenAPI)
- Keep field names and statuses stable so Boot replaces the mock, not the UI
