# Mini-Project C — API Explorer Lite (stretch)
**Phase:** 3 — Frontend & HTTP  
**Topic:** [09 — HTML, CSS & JavaScript](../../../phase-03-frontend-api/09-html-css-js.md)  
**Time:** ~3–4 hours

## Goal

Build a tiny browser tool: enter a URL (allowlist of safe demo APIs or local JSON), send GET, show status, a headers summary, and pretty-printed JSON — with clear errors for network failures and non-JSON bodies. Trains Network-tab thinking before topic 10.

## Prerequisites

- [ ] Topic 09 Days 22–24 (`fetch`, async/await, error UI)
- [ ] Live Server / static server available
- [ ] Willingness to restrict targets (no arbitrary SSRF-style “fetch anything” in a shared context — use allowlist)

## Learn (open before coding)

- [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch)
- [async function](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/async_function)
- [Response](https://developer.mozilla.org/en-US/docs/Web/API/Response)
- [HTTP overview](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview)
- [Network Monitor](https://developer.mozilla.org/en-US/docs/Tools/Network_Monitor)

## Project layout

```text
projects/api-explorer/
  index.html
  css/styles.css
  js/app.js
  data/sample.json          # local fallback
  README.md
```

## Build steps

### Step 1 — UI shell
- [ ] `index.html`: labeled URL input, “Send GET” button, panels for status, headers, body, errors
- [ ] Optional: `<select>` of preset URLs instead of free text
- [ ] Link `css/styles.css` and `js/app.js`
- **Done when:** Layout shows empty panels ready for results.

### Step 2 — Allowlist presets
- [ ] Hardcode safe presets, e.g.:
  - `./data/sample.json` (local)
  - `https://jsonplaceholder.typicode.com/posts/1`
  - `https://jsonplaceholder.typicode.com/users/1`
  - `https://httpbin.org/get`
- [ ] If free-text URL: reject unless it matches allowlist prefix or is relative `./`
- [ ] Document the allowlist in README
- **Done when:** User can only hit intended targets.

### Step 3 — sample.json
- [ ] Create `data/sample.json`:
  ```json
  {
    "message": "local sample",
    "items": [1, 2, 3]
  }
  ```
- [ ] Confirm Live Server can fetch it (`200`)
- **Done when:** Local GET works without CORS pain.

### Step 4 — fetch GET + status
- [ ] `async function explore(url)` with `try/catch`
- [ ] `const res = await fetch(url)`
- [ ] Display `res.status` and `res.statusText` in `#status`
- [ ] Do **not** assume `fetch` throws on 404 — check `res.ok` separately if you want a warning banner
- **Done when:** Status panel updates for success and 404 demo URLs.

### Step 5 — Headers summary
- [ ] Iterate `res.headers` (Headers are iterable):
  ```js
  const lines = [];
  for (const [k, v] of res.headers) lines.push(`${k}: ${v}`);
  ```
- [ ] Show in `<pre id="headers">` via `textContent`
- [ ] Note: some headers are forbidden from JS access — that’s OK; show what you get
- **Done when:** At least `content-type` (when exposed) appears for JSON responses.

### Step 6 — Pretty JSON body
- [ ] Read `Content-Type`; if JSON-ish, `await res.json()` then `JSON.stringify(data, null, 2)`
- [ ] Put result in `<pre id="body">`
- [ ] If parse fails, show raw `await res.text()` truncated + error note
- **Done when:** Happy path shows indented JSON.

### Step 7 — Error panel
- [ ] Network failure (offline / bad host): catch → `#error` message, clear body
- [ ] Non-OK status: still show status + body/text if any; highlight error panel
- [ ] Clear error on next successful request
- **Done when:** Failure paths never leave a blank silent UI.

### Step 8 — Loading state
- [ ] Disable Send while in flight; show “Loading…”
- [ ] Re-enable in `finally`
- **Done when:** Double-clicks don’t stack confusing results.

### Step 9 — Network tab correlation
- [ ] Open DevTools Network; send a request; click it
- [ ] In notes: method, URL, status, content-type — match what your UI shows
- **Done when:** UI status matches Network for one request.

### Step 10 — README
- [ ] How to open, preset list, CORS caveats for some public APIs
- [ ] Reminder: this is a learning tool, not a general proxy
- **Done when:** Someone else can run the explorer from README alone.

## Definition of Done

- [ ] GET only; allowlisted/local targets
- [ ] Displays status, headers summary, pretty JSON
- [ ] Clear error panel for network failures and non-JSON responses
- [ ] Loading state; README with presets

## Demo script

1. Select local `./data/sample.json` → Send → status 200 + pretty JSON
2. Select JSONPlaceholder post → show status + body
3. Point at a known 404 URL on JSONPlaceholder → show status 404 + error/warning
4. Brief Network tab compare

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Assuming `fetch` rejects on 404 | Check `res.status` / `res.ok` |
| `innerHTML` for JSON dump | `textContent` with pretty string |
| Allowing any URL blindly | Presets / allowlist |
| Ignoring CORS errors | Prefer same-origin local JSON + known CORS-friendly demos |
| No loading/disable | Disable button in flight |

## After this

- Start [10 — REST & HTTP](../../../phase-03-frontend-api/10-rest-http.md) with stronger vocabulary
- Reuse patterns in the Task Board `api.js` later
