# Mini-Project B — Task Board API Contract v1
**Phase:** 3 — Frontend & HTTP  
**Topic:** [10 — REST API & HTTP](../../../phase-03-frontend-api/10-rest-http.md)  
**Time:** ~6–8 hours

## Goal

Freeze Task Board API v1: resource table, OpenAPI 3.x file, example payloads, and a Postman collection with happy paths plus ≥1 error case per mutating endpoint. This is **required input** for the Phase 3 project.

## Prerequisites

- [ ] Mini-project A pocket guide started (methods/statuses clear)
- [ ] Topic 10 Days 5–13 practiced (JSON shapes, Postman, OpenAPI sketch)
- [ ] Postman Desktop/web available
- [ ] Swagger Editor: https://editor.swagger.io/

## Learn (open before coding)

- [OpenAPI 3.0.3](https://spec.openapis.org/oas/v3.0.3)
- [Swagger Editor](https://editor.swagger.io/)
- [Postman requests](https://learning.postman.com/docs/sending-requests/requests/)
- [Postman variables](https://learning.postman.com/docs/sending-requests/variables/)
- [Postman test scripts](https://learning.postman.com/docs/writing-scripts/test-scripts/)
- [Postman mock servers](https://learning.postman.com/docs/designing-and-developing-your-api/mocking-data/setting-up-mock/)
- [HTTP methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods) · [Status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)

## Project layout

```text
task-board-contract/
  README.md
  openapi-task-board.yaml
  examples/
    task.json
    tasks.json
    create-task.request.json
    error.json
  postman/
    task-board.postman_collection.json
    local.postman_environment.json
```

## Build steps

### Step 1 — Resource table lock
- [ ] In README, table:

  | Operation | Method & path | Success | Body |
  |-----------|---------------|---------|------|
  | List | `GET /api/tasks` | `200` | `Task[]` |
  | Get | `GET /api/tasks/{id}` | `200` | `Task` |
  | Create | `POST /api/tasks` | `201` | request → `Task` |
  | Replace/update | `PUT /api/tasks/{id}` | `200` | write shape |
  | Partial update | `PATCH /api/tasks/{id}` | `200` | e.g. `{ "status" }` |
  | Delete | `DELETE /api/tasks/{id}` | `204` | empty |

- [ ] Add query idea: `GET /api/tasks?status=todo`
- [ ] Decide: include both PUT and PATCH, or only one — document choice
- **Done when:** Table is written and noun-style URLs only.

### Step 2 — Example JSON files
- [ ] `examples/task.json`:
  ```json
  {
    "id": "1",
    "title": "Write OpenAPI",
    "description": "Document Task Board v1",
    "status": "doing",
    "createdAt": "2026-10-22T10:00:00Z"
  }
  ```
- [ ] `tasks.json` = array of ≥2 tasks; `error.json` = `{ "error": "...", "details": null }`
- [ ] `create-task.request.json` = body without `id`/`createdAt` if server assigns them
- [ ] Keep field names consistent everywhere
- **Done when:** Four example files exist and match each other.

### Step 3 — OpenAPI skeleton
- [ ] Create `openapi-task-board.yaml`:
  ```yaml
  openapi: 3.0.3
  info:
    title: Task Board API
    version: 1.0.0
  paths: {}
  components:
    schemas:
      Task:
        type: object
        required: [id, title, status]
        properties:
          id: { type: string }
          title: { type: string }
          description: { type: string }
          status:
            type: string
            enum: [todo, doing, done]
          createdAt: { type: string, format: date-time }
      Error:
        type: object
        required: [error]
        properties:
          error: { type: string }
          details: {}
  ```
- [ ] Paste into Swagger Editor; fix indentation errors
- **Done when:** Editor shows no errors for schemas (paths can still be empty).

### Step 4 — Document GET list + POST create
- [ ] `GET /api/tasks` → `200` application/json array of Task + example
- [ ] `POST /api/tasks` → request body schema + `201` returns Task
- [ ] Document `400` for missing title on POST
- **Done when:** Both paths render in Swagger UI with examples.

### Step 5 — Document item routes
- [ ] `GET /api/tasks/{id}` → `200` / `404`
- [ ] `PUT` and/or `PATCH` → `200` / `404` / `400`
- [ ] `DELETE` → `204` / `404`
- [ ] Reuse `#/components/responses/NotFound` if you factor it
- **Done when:** All operations from Step 1 appear in OpenAPI.

### Step 6 — Postman environment
- [ ] Environment `local` with `baseUrl`:
  - Start with `https://httpbin.org` for plumbing, **or**
  - Postman Mock URL once mock exists, **or**
  - `http://localhost:3000` if JSON Server later
- [ ] Variable `taskId` (empty initially)
- [ ] Export `local.postman_environment.json` into `postman/`
- **Done when:** `{{baseUrl}}` resolves in the URL bar when env selected.

### Step 7 — Postman collection requests
- [ ] Collection `Task Board` / folder `Tasks`
- [ ] Requests:
  1. `GET {{baseUrl}}/api/tasks`
  2. `POST {{baseUrl}}/api/tasks` — Body raw JSON from `create-task.request.json`; header `Content-Type: application/json`
  3. `GET {{baseUrl}}/api/tasks/:id` — path var `id` = `{{taskId}}`
  4. `PATCH {{baseUrl}}/api/tasks/:id` — body `{ "status": "doing" }` (or PUT)
  5. `DELETE {{baseUrl}}/api/tasks/:id`
- [ ] Folder `Errors` with bad POST (no title) and GET unknown id
- **Done when:** All planned requests exist in the collection.

### Step 8 — Test scripts (happy path)
- [ ] On list: `pm.response.to.have.status(200);`
- [ ] On create (when mock returns 201):
  ```js
  pm.test('created', () => pm.response.to.have.status(201));
  const json = pm.response.json();
  pm.collectionVariables.set('taskId', json.id);
  ```
- [ ] On get/update/delete: assert your success statuses (`200` / `204`)
- [ ] Run folder against mock or httpbin (adjust expectations for httpbin — it often returns `200`)
- **Done when:** ≥2 requests have passing status assertions against a real target.

### Step 9 — Error assertions
- [ ] POST missing title → expect `400` (or your documented code); Tests tab asserts it
- [ ] GET unknown id → expect `404`; assert
- [ ] Document expected error JSON next to each case in README or `examples/`
- **Done when:** ≥1 error path per mutating endpoint is documented; ≥2 error assertions written (create validation + not-found minimum).

### Step 10 — Mock server (recommended)
- [ ] Postman: create Mock Server from collection **or** note JSON Server plan for phase project
- [ ] Hit `GET {{baseUrl}}/api/tasks` → `200` + Task-shaped JSON
- [ ] Save mock URL into environment `baseUrl`
- **Done when:** Collection can smoke against a Task-shaped mock, **or** README clearly defers mock to phase project Day 3 with OpenAPI frozen.

### Step 11 — Export & freeze
- [ ] Export collection JSON → `postman/task-board.postman_collection.json`
- [ ] Re-open YAML in Swagger Editor — zero errors
- [ ] README: import steps, select env, run smoke order (list → create → get → patch → delete)
- [ ] Do not change paths daily after freeze without updating UI notes
- **Done when:** Artifacts committed; freeze declared in README.

## Definition of Done

- [ ] Resource table + `openapi-task-board.yaml` validated in Swagger Editor
- [ ] Example payloads committed
- [ ] Postman collection: happy path + ≥1 error per mutating endpoint (documented/asserted)
- [ ] Environment variable `baseUrl`
- [ ] Ready to copy into [phase-project Task Board](../phase-project/task-board.md)

## Demo script

1. Open YAML in https://editor.swagger.io/ — show paths + Task schema
2. Import Postman collection + `local` env
3. Run GET list → `200`
4. Run POST create → `201` (mock) → `taskId` saved
5. Run GET by id → `200`
6. Run POST with `{}` → `400` assertion green
7. Run GET `/api/tasks/does-not-exist` → `404` assertion green

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Verbs in URLs | Nouns + HTTP methods |
| UI field names ≠ OpenAPI | Contract is source of truth |
| Only happy-path Postman | Add deliberate 4xx requests |
| Everything `200` with error in body | Use proper status codes |
| Forgetting `Content-Type` on POST | Always `application/json` |
| Editing paths after UI starts without sync | Freeze; change spec first |

## After this

- Optional: [C — Public API exploration](C-public-api-exploration.md)
- Start Phase 3 project: [task-board.md](../phase-project/task-board.md)
