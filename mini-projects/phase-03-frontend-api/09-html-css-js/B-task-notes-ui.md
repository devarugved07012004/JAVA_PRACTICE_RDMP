# Mini-Project B — Task / Notes UI
**Phase:** 3 — Frontend & HTTP  
**Topic:** [09 — HTML, CSS & JavaScript](../../../phase-03-frontend-api/09-html-css-js.md)  
**Time:** ~6–8 hours

## Goal

Build a DOM-heavy task/notes UI: add, edit, complete, delete; filter All / Active / Done; empty and no-match states; persist to `localStorage`. Shape statuses as `todo` | `doing` | `done` to prep the Phase 3 Task Board.

## Prerequisites

- [ ] Mini-project A or equivalent Flex/Grid + events comfort
- [ ] Topic 09 Days 19–25 practiced (DOM, events, localStorage, fetch intro)
- [ ] Optional: Bootstrap 5 CDN for grid scaffolding

## Learn (open before coding)

- [Manipulating documents](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Client-side_web_APIs/Manipulating_documents)
- [Events / delegation](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Building_blocks/Events#event_delegation)
- [Array methods](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array)
- [Web Storage API](https://developer.mozilla.org/en-US/docs/Web/API/Web_Storage_API)
- [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) *(optional JSON seed)*
- [Bootstrap Grid](https://getbootstrap.com/docs/5.3/layout/grid/) *(optional)*

## Project layout

```text
projects/task-notes/
  index.html
  css/styles.css
  js/app.js
  js/storage.js           # optional split
  data/tasks.json         # optional seed for fetch
  README.md
```

## Build steps

### Step 1 — Shell HTML
- [ ] Create `index.html` with header (title), add form, filter controls, list/board region
- [ ] Semantic: `header`, `main`, form with labeled inputs
- [ ] Placeholders: static cards or empty `ul#task-list` / three column `div`s
- [ ] Files: `css/styles.css`, `js/app.js` linked (`defer` or `type="module"`)
- **Done when:** Shell renders with form + list/board placeholders.

### Step 2 — Layout (list or columns)
- [ ] Choose flat list **or** three columns (`todo` / `doing` / `done`) — columns preferred for Phase project prep
- [ ] Flex or Bootstrap row/cols; stack on narrow viewports
- [ ] CSS variables for status accents (subtle — not neon glow spam)
- **Done when:** Layout works at 360px and desktop.

### Step 3 — In-memory model
- [ ] Task shape: `{ id, title, status, done? }` where `status` ∈ `todo|doing|done` (or derive done from status)
- [ ] `let tasks = []` as source of truth
- [ ] `function render()` clears containers and rebuilds from `tasks`
- [ ] Prefer `createElement` + `textContent` for titles
- **Done when:** Calling `render()` with a hardcoded array paints the UI.

### Step 4 — Add task
- [ ] Form submit: `preventDefault()`; require non-empty title
- [ ] Push new task with `crypto.randomUUID()` or `Date.now()` id; default `status: 'todo'`
- [ ] Clear input; `render()`
- [ ] Show validation message if title empty
- **Done when:** New tasks appear without full page reload.

### Step 5 — Complete / cycle status / delete
- [ ] Toggle complete or move status (buttons: Todo → Doing → Done)
- [ ] Delete button removes from array
- [ ] Use **one** delegated `click` listener on the list/board parent (`event.target` / `closest`)
- [ ] New rows work without rebinding per card
- **Done when:** Add, status change, and delete work for newly created items via delegation.

### Step 6 — Filters
- [ ] Controls: All / Active / Done (map Active = not done / not `done` status)
- [ ] Filter in `render()` or pre-filter array copy — do not destroy source data
- [ ] “No matches” message when filter yields empty but `tasks.length > 0`
- [ ] Empty state when `tasks.length === 0`
- **Done when:** Filters and empty/no-match messages are demoable.

### Step 7 — Edit title
- [ ] Inline edit (input replace) or small prompt/form
- [ ] Save updates the object in `tasks` and re-renders
- [ ] Cancel path restores previous title
- **Done when:** Edit persists in the in-memory model after re-render.

### Step 8 — localStorage persistence
- [ ] After each mutation: `localStorage.setItem('tasks', JSON.stringify(tasks))`
- [ ] On load: parse with null/invalid JSON guards; then `render()`
- [ ] Refresh browser — tasks remain
- **Done when:** Reload restores the list/board.

### Step 9 — Optional fetch seed
- [ ] Add `data/tasks.json` with 3–5 sample tasks matching your shape
- [ ] On first load (empty storage), `fetch('./data/tasks.json')` → set `tasks` → save → render
- [ ] Check `response.ok`; show error banner on failure
- [ ] Network tab: status `200` for JSON (serve via Live Server, not always `file://`)
- **Done when:** Initial board can populate from JSON when storage is empty, **or** you document localStorage-only scope.

### Step 10 — Loading / error polish + README
- [ ] If using fetch: show “Loading…” until settled
- [ ] README: features, how to open, status field meaning for Phase project
- [ ] Quick a11y: labels, focusable buttons, keyboard delete/filter
- **Done when:** README exists; loading/empty/error (as applicable) visible.

## Definition of Done

- [ ] Add / edit / complete-or-move / delete work in the browser
- [ ] Filter: All / Active / Done
- [ ] Empty and “no matches” states
- [ ] Persist to `localStorage`
- [ ] Status field shape ready for Task Board (`todo` | `doing` | `done`)

## Demo script

1. Open via Live Server; show empty state (or seeded tasks)
2. Add two tasks; mark one done / move one to Doing
3. Filter Active vs Done; show no-matches if needed
4. Edit a title; delete a task
5. Refresh — state restored from `localStorage`

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Rebinding listeners every render | Event delegation on parent |
| Filtering by mutating/deleting source array | Keep full `tasks`; filter for display |
| `innerHTML` with raw titles | `textContent` |
| Forgetting to save after delete | Persist on every mutation |
| `file://` fetch failures | Use Live Server / static server |

## After this

- Optional: [C — API Explorer Lite](C-api-explorer-lite.md)
- Topic [10 REST/HTTP](../../../phase-03-frontend-api/10-rest-http.md); later wire this UI to a real contract in the phase project
