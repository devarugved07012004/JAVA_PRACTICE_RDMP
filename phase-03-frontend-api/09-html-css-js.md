# 09 — HTML, CSS & JavaScript

**Duration:** ~4–5 weeks (28–35 days) · **Cadence:** 1 hour/day  
**Focus:** Semantic HTML5, forms, CSS3 layout (Flexbox/Grid), responsive design, Bootstrap, modern JavaScript (ES6+), DOM, events, Fetch API, async/await — then ship small frontend projects

---

## 1. Why this topic is important

Everything users see and click is HTML structure, CSS presentation, and JavaScript behavior. Before Spring controllers and JSON APIs, you need to **build pages that load, lay out correctly across screen sizes, respond to input, and talk to an HTTP endpoint**. That triad is how browsers actually work.

This topic also trains the mental model Spring Boot apps rely on later: the browser is a client; your backend returns data; the UI updates from that data. Without solid DOM/`fetch` skills, API work stays abstract. With them, the Phase 3 Task Board and every later full-stack feature become concrete.

Daily practice here is visual and interactive — you see mistakes immediately. Use that feedback loop: small pages, clear structure, then wire behavior.

---

## 2. Prerequisites

- Phase 2 gate complete (SQL/JDBC mindset helps for “data behind the UI,” but is not required to start HTML)
- Comfortable with files/folders and a text editor
- Basic programming from Phase 1 (variables, conditionals, loops, functions) — JS syntax differs, logic transfers
- Browser DevTools willingness (Chrome, Firefox, or Edge)

**Environment you will set up:**

- Modern browser (latest Chromium-based or Firefox)
- VS Code (or similar) with Live Server / simple static file serving
- Optional: Node.js LTS only if you want a tiny local static server later — not required for Weeks 1–3
- Folder layout suggestion: `phase-03/09-frontend/{html,css,js,projects}`

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| HTML document skeleton | `<!DOCTYPE html>`, `html`, `head`, `body`, charset, viewport meta |
| Elements & attributes | Tags, nesting, `id`, `class`, `href`, `src`, `alt` |
| Text & media | Headings, paragraphs, lists, links, images, figures |
| Semantic HTML5 | `header`, `nav`, `main`, `section`, `article`, `aside`, `footer` — meaning over `div` soup |
| Forms basics | `form`, `input`, `label`, `button`, `textarea`, `select`, `option` |
| Form attributes | `name`, `type`, `required`, `placeholder`, `action`, `method` |
| CSS basics | Selectors, properties, cascade, specificity intuition |
| Box model | Content, padding, border, margin; `box-sizing: border-box` |
| Colors & typography | Units (`px`, `rem`, `%`), fonts, line-height, contrast |
| Display & position intro | `block` / `inline` / `inline-block`; when `position` is needed |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Flexbox | Main/cross axis, `justify-content`, `align-items`, `gap`, wrapping |
| CSS Grid | Tracks, areas, `fr`, auto-fit/minmax for responsive cards |
| Responsive design | Mobile-first media queries; fluid layouts; breakpoints |
| Forms UX | Grouping with `fieldset`/`legend`; associated labels; basic validation attributes |
| Bootstrap 5 | Grid system, utilities, components (navbar, cards, forms, modal) — use as accelerator, not crutch |
| JavaScript ES6+ basics | `let`/`const`, template literals, arrow functions, destructuring, spread |
| Arrays & objects | Map/filter/find; object literals; JSON as text vs object |
| DOM selection & update | `querySelector`, create/append/remove nodes, `textContent` vs `innerHTML` |
| Events | `addEventListener`, event object, `preventDefault`, delegation |
| Modules lite | Separate `.js` files; when to use `type="module"` |

### Advanced (frontend depth for this phase)

| Concept | What to internalize |
|--------|----------------------|
| Accessibility basics | Semantic landmarks, label association, keyboard focus, meaningful `alt` |
| CSS custom properties | Variables for theme/spacing; prefer over scattered magic numbers |
| Responsive images / layout polish | `max-width: 100%`; avoiding overflow; sticky headers carefully |
| Fetch API | `fetch(url)`, Response, status checks, `res.json()` |
| Async/Await | Promises under the hood; `try/catch` around `await`; sequential vs parallel |
| Client-side form validation | Constraint Validation API or manual checks before submit |
| Error & loading UI | Empty/loading/error states for lists and API calls |
| Local persistence lite | `localStorage` for drafts/preferences (not a database) |
| CORS awareness | Why browser blocks cross-origin calls; how local mock/same-origin avoids pain |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — HTML5 structure & forms (Days 1–7)

- [ ] **Day 1 — Document skeleton & Elements panel**
  - [ ] **Learn:** [Getting started with HTML](https://developer.mozilla.org/en-US/docs/Learn/HTML/Introduction_to_HTML/Getting_started) · [Document and website structure](https://developer.mozilla.org/en-US/docs/Learn/HTML/Introduction_to_HTML/Document_and_website_structure)
  - [ ] **Practice:** First HTML page and DevTools inspect
    1. Create folder `09-frontend/day01/` and file `index.html`.
    2. Add `<!DOCTYPE html>`, `html`, `head` with charset + `<meta name="viewport" content="width=device-width, initial-scale=1">`, title, and `body`.
    3. In body: one `h1`, a short paragraph, a `ul` with 3 links (`href`).
    4. Open `index.html` in the browser (double-click or Live Server).
    5. Right-click → Inspect → Elements: find your `h1` and edit its text live once.
    6. **Done when:** Page renders and you changed text via Elements panel.

- [ ] **Day 2 — Semantic layout**
  - [ ] **Learn:** [HTML sections and outlines](https://developer.mozilla.org/en-US/docs/Web/HTML/Element#content_sectioning) · [Semantic HTML](https://developer.mozilla.org/en-US/docs/Learn/Accessibility/HTML)
  - [ ] **Practice:** Learning-hub page with landmarks
    1. Create `day02/learning-hub.html`.
    2. Build `header` (site name), `nav` (3 in-page links), `main` (intro), `footer` (copyright).
    3. Avoid wrapping everything in only `div`s — landmarks must be real tags.
    4. Open in browser; click nav links to jump to sections (`id` + `#hash`).
    5. **Done when:** Landmarks exist in Elements tree and in-page nav works.

- [ ] **Day 3 — Images, figures, tables**
  - [ ] **Learn:** [Images in HTML](https://developer.mozilla.org/en-US/docs/Learn/HTML/Multimedia_and_embedding/Images_in_HTML) · [HTML table basics](https://developer.mozilla.org/en-US/docs/Learn/HTML/Tables/Basics) · [`<figure>`](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/figure)
  - [ ] **Practice:** Media and a schedule table
    1. Create `day03/media.html`. Add an `img` with meaningful `alt` (not “image”).
    2. Wrap one image in `figure` + `figcaption`.
    3. Build a 3×4 schedule `table` with `th` headers (Day / Topic / Status).
    4. Open in browser; confirm table aligns and alt is visible in Elements.
    5. **Done when:** Figure + table render and every img has non-empty `alt`.

- [ ] **Day 4 — Forms & labels**
  - [ ] **Learn:** [HTML forms guide](https://developer.mozilla.org/en-US/docs/Learn/Forms) · [`<label>`](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/label) · [`<input>`](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input)
  - [ ] **Practice:** Registration form with labeled controls
    1. Create `day04/form.html` with a `form` (`action="#"` `method="get"`).
    2. Add inputs: text, email, password, checkbox, radio (2+), `select`+`option`, `textarea`.
    3. Every control gets a `<label for="...">` matching `id` (or wrap label).
    4. Open in browser; click each label — focus must jump to its control.
    5. **Done when:** All listed control types exist and label clicks focus correctly.

- [ ] **Day 5 — Form validation attributes**
  - [ ] **Learn:** [Client-side form validation](https://developer.mozilla.org/en-US/docs/Learn/Forms/Form_validation) · [Constraint Validation API](https://developer.mozilla.org/en-US/docs/Web/HTML/Constraint_validation)
  - [ ] **Practice:** Browser-native validation demo
    1. Copy/extend `day04/form.html` → `day05/validate.html`.
    2. Add `required` on name/email; `type="email"`; `min`/`max` on a number field; `pattern` on a username.
    3. Set `action="#"`; add a Submit button.
    4. In browser: submit empty → see native errors; fix fields → submit navigates to `#`.
    5. **Done when:** Empty submit is blocked and valid submit reaches `#`.

- [ ] **Day 6 — Multi-section landing structure**
  - [ ] **Learn:** [HTML elements reference](https://developer.mozilla.org/en-US/docs/Web/HTML/Element) · [Structuring a page](https://developer.mozilla.org/en-US/docs/Learn/HTML/Introduction_to_HTML/Document_and_website_structure)
  - [ ] **Practice:** Landing sections with almost no CSS
    1. Create `day06/landing.html`: hero (`h1` + one sentence), features (`ul` or 3 `section`s), contact form.
    2. Use semantic tags; one CTA link/button in the hero.
    3. Optional: 5–10 lines of CSS only for spacing — layout comes later.
    4. Open and scroll; confirm heading order `h1` then `h2`s.
    5. **Done when:** Three sections render and heading hierarchy is sensible.

- [ ] **Day 7 — Week review: semantics & a11y**
  - [ ] **Learn:** [HTML: A good basis for accessibility](https://developer.mozilla.org/en-US/docs/Learn/Accessibility/HTML) · [Heading elements](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/Heading_Elements)
  - [ ] **Practice:** Refactor Day 2 for accessibility wins
    1. Open `day02/learning-hub.html`; replace leftover `div` soup with landmarks/sections.
    2. Fix heading levels (no skipped levels); ensure one `h1`.
    3. Tab through the page with keyboard; note focus order in notes.
    4. List 3 accessibility wins you made (labels, landmarks, headings).
    5. **Done when:** Refactored page opens cleanly and notes list 3 wins.

### Week 2 — CSS3, Flexbox, Grid, responsive (Days 8–14)

- [ ] **Day 8 — Cascade, specificity & box model**
  - [ ] **Learn:** [Cascade and inheritance](https://developer.mozilla.org/en-US/docs/Learn/CSS/Building_blocks/Cascade_and_inheritance) · [The box model](https://developer.mozilla.org/en-US/docs/Learn/CSS/Building_blocks/The_box_model)
  - [ ] **Practice:** External CSS box-model lab
    1. Create `day08/index.html` + `styles.css`; link with `<link rel="stylesheet">`.
    2. Style a box with padding, border, margin; set `box-sizing: border-box`.
    3. Add competing rules (element vs class vs id) and note which wins in DevTools Computed.
    4. Color borders differently so content/padding/border are visible.
    5. **Done when:** You can point to which selector won and show the box model in DevTools.

- [ ] **Day 9 — Flexbox nav & hero**
  - [ ] **Learn:** [Flexbox](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_flexible_box_layout) · [Flexbox guide (MDN)](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Flexbox)
  - [ ] **Practice:** Flex nav and centered hero
    1. Create `day09/flex-hero.html` + CSS.
    2. Nav: `display: flex; justify-content: space-between; align-items: center; gap: ...`.
    3. Hero content centered with flex on a full-width section (`min-height` ~60vh).
    4. Open in browser; resize width — nav items stay on one row with gap.
    5. **Done when:** Nav and hero use Flexbox (not floats) and look intentional.

- [ ] **Day 10 — Flexbox wrap cards**
  - [ ] **Learn:** [flex-wrap](https://developer.mozilla.org/en-US/docs/Web/CSS/flex-wrap) · [Aligning items in Flexbox](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_flexible_box_layout/Aligning_items_in_a_flex_container)
  - [ ] **Practice:** Wrapping card row
    1. Create `day10/cards.html` with 6 card `article`s in a flex container.
    2. Set `flex-wrap: wrap`, fixed/min card width, `gap`.
    3. Give cards different content lengths; observe equal stretch (`align-items: stretch` default).
    4. Narrow the window until cards wrap to the next line.
    5. **Done when:** Cards wrap cleanly and heights look even in a row.

- [ ] **Day 11 — CSS Grid shell**
  - [ ] **Learn:** [CSS Grid Layout](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_grid_layout) · [Grids (Learn)](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Grids)
  - [ ] **Practice:** Header / sidebar / content grid
    1. Create `day11/grid-shell.html` + CSS.
    2. Use `display: grid` with areas or template columns for header, sidebar, main.
    3. Put placeholder text in each region; visible borders/backgrounds for debugging.
    4. Optional: small image gallery grid with `repeat` + `fr`.
    5. **Done when:** Shell regions sit correctly and Grid is visible in DevTools Layout.

- [ ] **Day 12 — Mobile-first media queries**
  - [ ] **Learn:** [Using media queries](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_media_queries/Using_media_queries) · [Responsive design](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Responsive_Design)
  - [ ] **Practice:** Narrow-first then enhance
    1. Take Day 11 (or 10) page; write base CSS for ~360px width first.
    2. Add `@media (min-width: 768px)` and `@media (min-width: 1024px)` layout upgrades.
    3. In DevTools, toggle device toolbar; check 360 / 768 / 1200.
    4. Note one layout bug you fixed when narrowing.
    5. **Done when:** Page is usable at 360px and improved at desktop breakpoints.

- [ ] **Day 13 — Responsive form layout**
  - [ ] **Learn:** [Styling web forms](https://developer.mozilla.org/en-US/docs/Learn/Forms/Styling_web_forms) · [Media queries](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_media_queries/Using_media_queries)
  - [ ] **Practice:** Stacked mobile form, two-column desktop
    1. Create `day13/form-layout.html` with 4–6 fields.
    2. Mobile: fields stack full width.
    3. Desktop media query: two columns (Grid or Flex) for field pairs.
    4. Type into fields in the browser; labels remain aligned.
    5. **Done when:** Form stacks on narrow and becomes two-column on wide.

- [ ] **Day 14 — Flex + Grid polish**
  - [ ] **Learn:** [CSS layout overview](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout) · [Overflow](https://developer.mozilla.org/en-US/docs/Web/CSS/overflow)
  - [ ] **Practice:** Combined layout and overflow fix
    1. Build one page using Grid for page shell and Flex for nav/cards.
    2. Intentionally cause horizontal overflow (wide content); fix with `max-width: 100%` / `minmax` / wrapping.
    3. Recheck at 360px — no sideways scrollbar (or only intentional).
    4. Save as `day14/combined.html`.
    5. **Done when:** Flex+Grid page has no accidental horizontal overflow on mobile.

### Week 3 — Bootstrap + JavaScript ES6+ (Days 15–21)

- [ ] **Day 15 — Bootstrap 5 grid via CDN**
  - [ ] **Learn:** [Bootstrap Getting started](https://getbootstrap.com/docs/5.3/getting-started/introduction/) · [Bootstrap Grid](https://getbootstrap.com/docs/5.3/layout/grid/)
  - [ ] **Practice:** CDN Bootstrap container/row/col page
    1. Create `day15/bootstrap-grid.html`; paste Bootstrap 5.3 CSS CDN link in `head`.
    2. Build `container` > `row` > three `col-md-4` feature blocks.
    3. Open in browser; resize — columns stack on small screens.
    4. Compare briefly to your Day 10 custom flex cards in notes (1–2 sentences).
    5. **Done when:** Three columns respond via Bootstrap grid without custom layout CSS.

- [ ] **Day 16 — Bootstrap forms & navbar**
  - [ ] **Learn:** [Bootstrap Forms](https://getbootstrap.com/docs/5.3/forms/overview/) · [Navbar](https://getbootstrap.com/docs/5.3/components/navbar/) · [Buttons](https://getbootstrap.com/docs/5.3/components/buttons/)
  - [ ] **Practice:** Navbar + form with Bootstrap components
    1. Create `day16/bootstrap-ui.html` with responsive `navbar`.
    2. Add a form using Bootstrap form classes; primary/secondary `btn` variants.
    3. Include JS bundle CDN only if navbar toggler needs it; click toggler on narrow width.
    4. Keep custom CSS under ~20 lines.
    5. **Done when:** Navbar collapses/expands on mobile and form fields use Bootstrap styles.

- [ ] **Day 17 — JS basics**
  - [ ] **Learn:** [JavaScript first steps](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/First_steps) · [Grammar and types](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Grammar_and_types)
  - [ ] **Practice:** Console + script file drills
    1. Create `day17/basics.html` + `app.js`; load script with `<script src="app.js" defer>`.
    2. In `app.js`: `const`/`let`, a function, an array, an object; `console.log` them.
    3. Open page; DevTools Console — confirm logs.
    4. In Console, manually call your function once.
    5. **Done when:** Script file runs on load and Console shows your values.

- [ ] **Day 18 — ES6+ syntax drills**
  - [ ] **Learn:** [Arrow functions](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Functions/Arrow_functions) · [Template literals](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Template_literals) · [Destructuring](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Destructuring_assignment)
  - [ ] **Practice:** Arrows, templates, destructure, spread
    1. In `day18/es6.js`, rewrite a normal function as an arrow.
    2. Build a string with template literals including a variable.
    3. Destructure an object and an array; use spread to copy/extend an array.
    4. Log all results from an HTML page.
    5. **Done when:** All four syntax forms appear in your script and log correctly.

- [ ] **Day 19 — DOM select & render**
  - [ ] **Learn:** [Document Object Model](https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model) · [Manipulating documents](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Client-side_web_APIs/Manipulating_documents)
  - [ ] **Practice:** Render a list from an array
    1. Create `day19/dom.html` with empty `<ul id="list">`.
    2. In JS, define an array of strings; `querySelector` the ul.
    3. For each item, `createElement('li')`, set `textContent`, `append`.
    4. Add a “Clear” path that empties the ul and re-renders a second array.
    5. **Done when:** List appears from data and clear/re-render works without `innerHTML` for item text.

- [ ] **Day 20 — Events & preventDefault**
  - [ ] **Learn:** [addEventListener](https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/addEventListener) · [Introduction to events](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Building_blocks/Events)
  - [ ] **Practice:** Click, input, submit handlers
    1. Create `day20/events.html` with a form, a text input, and a toggle button.
    2. `click` toggles a CSS class on a panel.
    3. `input` updates a live preview `textContent`.
    4. `submit` handler calls `preventDefault()` and logs/fields without full page reload.
    5. **Done when:** Toggle, live preview, and submit-without-reload all work.

- [ ] **Day 21 — Event delegation**
  - [ ] **Learn:** [Event bubbling/delegation](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Building_blocks/Events#event_delegation) · [Event](https://developer.mozilla.org/en-US/docs/Web/API/Event)
  - [ ] **Practice:** Add/remove list items via one listener
    1. Create `day21/delegate.html`: parent `ul`, form to add items, each `li` has a Delete button.
    2. Put **one** `click` listener on the `ul` (delegation); detect Delete via `event.target`.
    3. Add items dynamically; delete works on new items without rebinding.
    4. Confirm in Console you did not attach per-row listeners in a loop.
    5. **Done when:** Add + delete work for newly created rows via delegation.

### Week 4 — Fetch, async/await, integrated UI (Days 22–28)

- [ ] **Day 22 — Promises & first fetch**
  - [ ] **Learn:** [Promise](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise) · [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) · [Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
  - [ ] **Practice:** Fetch JSON and log it
    1. Serve files via Live Server (or local static server) — not always required for public APIs.
    2. `fetch('https://jsonplaceholder.typicode.com/posts/1')` (or local `data.json`).
    3. `.then(res => res.json()).then(data => console.log(data))`.
    4. Open Network tab; click the request; note status `200`.
    5. **Done when:** Console shows JSON and Network shows status 200.

- [ ] **Day 23 — async/await & error UI**
  - [ ] **Learn:** [async function](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/async_function) · [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch)
  - [ ] **Practice:** async fetch with on-page errors
    1. Rewrite yesterday’s fetch with `async function load() { try { ... } catch }`.
    2. Check `if (!res.ok) throw new Error(...)` before `res.json()`.
    3. Intentionally fetch a bad URL or `/posts/99999` and show message in a `<p id="error">`.
    4. Clear error on success path.
    5. **Done when:** Happy path logs data and failure path shows visible error text.

- [ ] **Day 24 — List UI from JSON**
  - [ ] **Learn:** [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) · [Manipulating documents](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Client-side_web_APIs/Manipulating_documents)
  - [ ] **Practice:** Loading state then cards from JSON
    1. Create `day24/list-ui.html` with a container and “Loading…” text.
    2. Fetch a list endpoint (e.g. `/posts?_limit=5`).
    3. On success, replace loading with cards/titles via DOM APIs.
    4. On failure, show error state instead of cards.
    5. **Done when:** You see Loading → cards (or Loading → error) in the browser.

- [ ] **Day 25 — Form → array → localStorage**
  - [ ] **Learn:** [Web Storage API](https://developer.mozilla.org/en-US/docs/Web/API/Web_Storage_API) · [Working with JSON](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Objects/JSON)
  - [ ] **Practice:** In-memory list with persistence
    1. Form adds `{ id, title }` objects to an array; re-render list.
    2. `localStorage.setItem('items', JSON.stringify(array))` after each change.
    3. On load, `JSON.parse(localStorage.getItem(...))` restore (guard null).
    4. Refresh the browser — items remain.
    5. **Done when:** Add works, refresh restores list from `localStorage`.

- [ ] **Day 26 — Mini-project A structure**
  - [ ] **Learn:** [Document structure](https://developer.mozilla.org/en-US/docs/Learn/HTML/Introduction_to_HTML/Document_and_website_structure) · [CSS custom properties](https://developer.mozilla.org/en-US/docs/Web/CSS/Using_CSS_custom_properties)
  - [ ] **Practice:** Portfolio / Learning Hub HTML+CSS shell
    1. Create `projects/portfolio/` with `index.html`, `css/styles.css`.
    2. Sections: About, Skills, Projects, Contact (semantic).
    3. Define CSS variables for colors/spacing; responsive Flex/Grid layout.
    4. No JS required yet beyond empty `js/app.js` stub.
    5. **Done when:** Multi-section responsive shell looks acceptable at 360px and desktop.

- [ ] **Day 27 — Mini-project A JS & responsive**
  - [ ] **Learn:** [Events](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Building_blocks/Events) · [Responsive design](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Responsive_Design)
  - [ ] **Practice:** Wire theme toggle and contact validation
    1. Theme toggle button flips a class on `document.documentElement` / `body` using CSS variables.
    2. Persist theme in `localStorage`.
    3. Contact form: `preventDefault` + basic validation messages.
    4. Recheck layout at 360px and 1200px; fix issues.
    5. **Done when:** Theme persists across refresh and invalid contact submit shows errors.

- [ ] **Day 28 — Finish A & Network tab**
  - [ ] **Learn:** [Network Monitor (MDN)](https://developer.mozilla.org/en-US/docs/Tools/Network_Monitor) · [HTTP overview](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview)
  - [ ] **Practice:** Ship Mini-project A and Network notes
    1. Finish remaining Portfolio polish from §6 Mini-project A.
    2. Open DevTools Network; reload; note document + CSS + JS requests and statuses.
    3. Write 5–8 lines: request line, status, what `200` means for a stylesheet.
    4. Quick self-check against Mini-project A bullets.
    5. **Done when:** Project A meets its must-haves and Network notes exist.

### Week 5 — Projects & gate (Days 29–35) *(use if on 5-week pace)*

- [ ] **Day 29 — Mini-project B shell**
  - [ ] **Learn:** [Bootstrap Grid](https://getbootstrap.com/docs/5.3/layout/grid/) · [Flexbox](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Flexbox)
  - [ ] **Practice:** Task/Notes UI shell
    1. Create `projects/task-notes/` with `index.html`, CSS, JS.
    2. Layout: header, add form, list/board region (Bootstrap or Flex).
    3. Static placeholder cards for Todo/Doing/Done or a flat list.
    4. Open in browser; confirm structure before logic.
    5. **Done when:** Shell renders with form + list/board placeholders.

- [ ] **Day 30 — Dynamic columns / filters**
  - [ ] **Learn:** [Array methods](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array) · [DOM](https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model)
  - [ ] **Practice:** Filterable dynamic task list
    1. In-memory array of tasks with `status`.
    2. Render into columns or one list; filters All / Active / Done.
    3. Add / toggle complete / delete via DOM + events (delegation OK).
    4. Empty and “no matches” messages when appropriate.
    5. **Done when:** Filters and mutations update the UI from the array.

- [ ] **Day 31 — Fetch integration**
  - [ ] **Learn:** [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) · [async function](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/async_function)
  - [ ] **Practice:** Load tasks from JSON/API
    1. Add `data/tasks.json` (or use a public API / JSONPlaceholder).
    2. On load, `fetch` → parse → set state array → render.
    3. Keep `localStorage` optional merge strategy documented in a comment.
    4. Verify Network status `200` for the JSON request.
    5. **Done when:** Initial board/list populates from fetched JSON.

- [ ] **Day 32 — Loading / empty / error states**
  - [ ] **Learn:** [Using Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch) · [Promise](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise)
  - [ ] **Practice:** Harden UI states (B or stretch C)
    1. Show loading until fetch settles.
    2. Empty state when array length 0; error banner on failure.
    3. Break the URL once to test error UI; restore after.
    4. Optional: start Mini-project C API Explorer shell.
    5. **Done when:** Loading, empty, and error are all visible in demos.

- [ ] **Day 33 — Accessibility pass**
  - [ ] **Learn:** [Accessibility basics](https://developer.mozilla.org/en-US/docs/Learn/Accessibility) · [HTML: A good basis for accessibility](https://developer.mozilla.org/en-US/docs/Learn/Accessibility/HTML)
  - [ ] **Practice:** Labels, focus, headings on projects
    1. Audit Portfolio + Task UI: every input has a label.
    2. Tab through primary flows; fix focus traps/skips.
    3. Fix heading hierarchy; ensure buttons have accessible names.
    4. Note remaining issues (max 5) if any.
    5. **Done when:** Keyboard tab order works for main flows and labels are complete.

- [ ] **Day 34 — CSS variables & README**
  - [ ] **Learn:** [Using CSS custom properties](https://developer.mozilla.org/en-US/docs/Web/CSS/Using_CSS_custom_properties) · [Bootstrap utilities](https://getbootstrap.com/docs/5.3/utilities/api/)
  - [ ] **Practice:** Cleanup and document projects
    1. Centralize colors/spacing in CSS variables; remove dead rules.
    2. Remove unused Bootstrap classes if present.
    3. Write a short README per project (how to open, features).
    4. Spot-check both projects in the browser once more.
    5. **Done when:** READMEs exist and CSS is noticeably tidier.

- [ ] **Day 35 — Mastery dry-run & HTTP preview**
  - [ ] **Learn:** [HTTP overview](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview) · [Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
  - [ ] **Practice:** Checklist dry-run and topic-10 vocab
    1. Walk §12 mastery checklist; tick honest results.
    2. Confirm Mini-projects A + B meet §6 musts.
    3. In notes, define in your words: request, response, status code, header, JSON body.
    4. List gaps before starting [10-rest-http.md](10-rest-http.md).
    5. **Done when:** Checklist ≥90% path is clear and HTTP vocab notes exist.

*On a 4-week pace: fold Week 5 polish into Days 26–28; ship Mini-projects A + B only.*

---

## 5. Hands-on exercises

### HTML & semantics

1. Convert a “div-only” page into semantic landmarks without changing visuals much.
2. Build a registration form with fieldset groups (Account, Profile, Preferences).
3. Create an accessible FAQ with headings and lists (no JS yet).
4. Mark up a blog article: `article`, `time`, author byline, related links in `aside`.

### CSS layout

5. Sticky footer layout (flex column on `body`/`main`).
6. Three-column desktop → single column mobile using Grid or Flex + media queries.
7. Pricing-style three cards equal height; middle card visually emphasized with CSS only.
8. Navigation that collapses to a vertical stack under 600px (CSS only first).

### Bootstrap

9. Recreate exercise 7 with Bootstrap grid + utilities; compare CSS size vs custom.
10. Modal confirm dialog for “Delete item?” (Bootstrap modal + your JS hook).

### JavaScript & DOM

11. Counter widget (+ / − / reset) with disabled state at bounds.
12. Tab switcher: three panels, one visible; ARIA `role="tab"` optional stretch.
13. Filter a list of objects by search input (case-insensitive).
14. Sortable table headers for a small static dataset.
15. Debounced search feel: update results on `input` (simple; ignore true debounce libs).

### Fetch & async

16. `GET` JSON; render titles; handle 404 by checking `response.ok`.
17. Parallel fetches with `Promise.all` for two resources; render both.
18. AbortController stretch: cancel in-flight fetch when user types a new query.
19. POST JSON to a mock endpoint (httpbin.org or your later local mock); show returned body.
20. Persist a todo list in `localStorage`; reload restores state.

---

## 6. Mini-projects

> **Build guides (checkboxes + steps):** open the linked markdown before coding.

### A. Personal Portfolio / Learning Hub (static + light JS)

**Guide:** [A — Personal Portfolio](../mini-projects/phase-03-frontend-api/09-html-css-js/A-personal-portfolio.md)

- Semantic multi-section page: About, Skills, Projects, Contact form (client-side validation only).
- Responsive Flex/Grid layout; works at ~360px and desktop.
- Theme toggle (light/dark) via CSS variables + `localStorage`.
- Optional Bootstrap for nav/form only — keep custom layout practice visible.

### B. Task / Notes UI (DOM-heavy)

**Guide:** [B — Task / Notes UI](../mini-projects/phase-03-frontend-api/09-html-css-js/B-task-notes-ui.md)

- Add / edit / complete / delete notes or tasks in the browser.
- Filter: All / Active / Done.
- Empty and “no matches” states.
- Persist to `localStorage`.
- Prep shape for Phase project: columns or status field (`todo` | `doing` | `done`).

### C. (Stretch) API Explorer Lite

**Guide:** [C — API Explorer Lite](../mini-projects/phase-03-frontend-api/09-html-css-js/C-api-explorer-lite.md)

- Input a URL (restrict to known safe demo APIs or local JSON).
- Method GET; display status, headers summary, pretty-printed JSON.
- Clear error panel for network failures and non-JSON responses.
- Teaches Network-tab thinking before formal REST topic.

---

## 7. Industry use cases

- **Marketing & product sites:** Semantic HTML + responsive CSS for public pages and docs hubs.
- **Admin dashboards:** Bootstrap/Grid layouts; tables and forms for internal tools.
- **SPA-style screens (pre-framework):** DOM updates + `fetch` power many “simple” internal UIs without React.
- **Design system consumption:** Utility classes and components mirror how teams ship consistent UI fast.
- **Full-stack Java path:** Your future Thymeleaf or separate JS frontends still need this foundation.
- **API consumers:** Every mobile/web client speaks HTTP; browser `fetch` is the clearest classroom for that.

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Using `<div>` for everything | Prefer semantic tags; `div`/`span` for styling hooks only |
| Inputs without `<label>` | `for`/`id` pairing or wrap input in label |
| Forgetting viewport meta | Add `<meta name="viewport" content="width=device-width, initial-scale=1">` |
| Fighting Flex/Grid with excess `position: absolute` | Layout with Flex/Grid first; position sparingly |
| Fixed pixel widths everywhere | Prefer `%`, `fr`, `max-width`, fluid containers |
| Bootstrap without understanding grid | Learn Flex/Grid first; then map to Bootstrap classes |
| `var` everywhere / accidental globals | Prefer `const`/`let`; avoid implicit globals |
| `innerHTML` with untrusted strings | Prefer `textContent` / `createElement` for user data |
| Ignoring `response.ok` after `fetch` | `fetch` only rejects on network failure — check status |
| Nesting `async` without `await` / swallowing errors | `try/catch`; surface errors in the UI |
| Giant single HTML file | Split CSS/JS; keep structure readable |
| No empty/loading states | Always design the “nothing here yet” and “please wait” UI |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [MDN — Learn HTML](https://developer.mozilla.org/en-US/docs/Learn/HTML) | Official-quality HTML path |
| [MDN — Learn CSS](https://developer.mozilla.org/en-US/docs/Learn/CSS) | CSS path including layout |
| [MDN — Learn JavaScript](https://developer.mozilla.org/en-US/docs/Learn/JavaScript) | JS language + DOM |
| [web.dev — Learn CSS](https://web.dev/learn/css/) | Modern CSS curriculum (Google) |
| [web.dev — Learn HTML](https://web.dev/learn/html/) | HTML modern practices |
| [Flexbox Froggy](https://flexboxfroggy.com/) | Flexbox drills (free game) |
| [Grid Garden](https://cssgridgarden.com/) | Grid drills (free game) |
| [Bootstrap 5 Docs](https://getbootstrap.com/docs/5.3/getting-started/introduction/) | Components & grid reference |
| [JavaScript.info](https://javascript.info/) | Deep free modern JS tutorials |
| [freeCodeCamp Responsive Web Design](https://www.freecodecamp.org/learn/2022/responsive-web-design/) | Structured free practice (optional) |

Prefer **MDN / web.dev / Bootstrap docs** as source of truth when tutorials disagree.

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Full index:

- HTML elements reference: https://developer.mozilla.org/en-US/docs/Web/HTML/Element
- HTML forms guide: https://developer.mozilla.org/en-US/docs/Learn/Forms
- CSS box model: https://developer.mozilla.org/en-US/docs/Learn/CSS/Building_blocks/The_box_model
- CSS Flexbox: https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_flexible_box_layout
- CSS Grid: https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_grid_layout
- Media queries: https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_media_queries/Using_media_queries
- Bootstrap 5 layout grid: https://getbootstrap.com/docs/5.3/layout/grid/
- Bootstrap 5 forms: https://getbootstrap.com/docs/5.3/forms/overview/
- JavaScript guide (MDN): https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide
- DOM intro: https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model
- EventTarget.addEventListener: https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/addEventListener
- Fetch API: https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API
- Using Fetch: https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
- async function: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/async_function
- Promise: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise
- Constraint Validation API: https://developer.mozilla.org/en-US/docs/Web/HTML/Constraint_validation
- Web Storage API (`localStorage`): https://developer.mozilla.org/en-US/docs/Web/API/Web_Storage_API
- HTTP overview (preview for topic 10): https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. HTML fluency | Semantic pages + solid forms | Can mark up a multi-section page without looking up every tag |
| 2. CSS layout | Flex + Grid + responsive | One page looks correct at 360px and 1200px |
| 3. Bootstrap accelerator | Faster scaffolding | Can rebuild a form/nav with Bootstrap docs open |
| 4. JS language | ES6+ comfort | Small scripts without copying syntax blindly |
| 5. DOM & events | Interactive UI | Dynamic list with add/remove/filter |
| 6. Async HTTP client | `fetch` + async/await | Load remote/local JSON into UI with error handling |
| 7. Projects | Portfolio + Task/Notes UI | Mini-projects A + B complete |
| 8. Gate | Ready for REST topic | Mastery checklist ≥90% |

**Weekly rhythm:** Mon–Fri new skills; Sat rebuild yesterday’s page from memory; Sun responsive + a11y polish + notes.

**How this feeds topic 10 & the phase project:** Your Task/Notes UI becomes the Task Board shell; `fetch` practice becomes real REST calls once the contract exists.

---

## 12. Mastery checklist

- [ ] Writes valid HTML5 with correct document head (charset, viewport, title)
- [ ] Uses semantic landmarks (`header`, `nav`, `main`, `section`/`article`, `footer`) deliberately
- [ ] Builds forms with associated labels and useful input types
- [ ] Explains the box model and uses `box-sizing: border-box` intentionally
- [ ] Lays out pages with Flexbox for 1D alignment problems
- [ ] Lays out pages with CSS Grid for 2D structure
- [ ] Applies mobile-first responsive design with media queries
- [ ] Can scaffold a usable page with Bootstrap 5 grid and form components
- [ ] Writes ES6+ JavaScript (`let`/`const`, arrows, template literals, destructuring)
- [ ] Selects and updates DOM nodes; creates elements without relying only on `innerHTML`
- [ ] Handles click/input/submit events; uses `preventDefault` correctly
- [ ] Uses event delegation for dynamic lists
- [ ] Calls `fetch`, checks `response.ok`, parses JSON with `async`/`await` and `try/catch`
- [ ] Implements loading, empty, and error UI states
- [ ] Uses `localStorage` for a simple client-side persistence demo
- [ ] Completed Mini-projects A and B from §6
- [ ] Can use browser DevTools (Elements, Console, Network) to debug layout and requests
- [ ] Ready to start [10-rest-http.md](10-rest-http.md)
