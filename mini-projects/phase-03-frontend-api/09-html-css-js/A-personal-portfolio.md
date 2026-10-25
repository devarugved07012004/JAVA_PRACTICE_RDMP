# Mini-Project A — Personal Portfolio / Learning Hub
**Phase:** 3 — Frontend & HTTP  
**Topic:** [09 — HTML, CSS & JavaScript](../../../phase-03-frontend-api/09-html-css-js.md)  
**Time:** ~6–8 hours

## Goal

Ship a responsive, semantic multi-section portfolio (About, Skills, Projects, Contact) with CSS variables, light/dark theme toggle persisted in `localStorage`, and client-side contact validation — no backend required.

## Prerequisites

- [ ] Topic 09 Weeks 1–3 practiced (HTML semantics, Flex/Grid, JS events)
- [ ] Browser DevTools comfort (Elements, Console)
- [ ] Live Server or equivalent static file serving

## Learn (open before coding)

- [Document structure](https://developer.mozilla.org/en-US/docs/Learn/HTML/Introduction_to_HTML/Document_and_website_structure)
- [CSS custom properties](https://developer.mozilla.org/en-US/docs/Web/CSS/Using_CSS_custom_properties)
- [Flexbox](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Flexbox)
- [Responsive design](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Responsive_Design)
- [Client-side form validation](https://developer.mozilla.org/en-US/docs/Learn/Forms/Form_validation)
- [Web Storage API](https://developer.mozilla.org/en-US/docs/Web/API/Web_Storage_API)

## Project layout

```text
projects/portfolio/
  index.html
  css/styles.css
  js/app.js
  assets/               # optional images
  README.md
```

## Build steps

### Step 1 — HTML shell & landmarks
- [ ] Create `index.html` with `<!DOCTYPE html>`, charset, viewport meta, title
- [ ] Structure:
  - `header` + `nav` (in-page links: About, Skills, Projects, Contact)
  - `main` with `section#about`, `#skills`, `#projects`, `#contact`
  - `footer`
- [ ] One `h1` (brand/name); section `h2`s — no skipped heading levels
- **Done when:** Landmarks appear in Elements; in-page `#` nav jumps work.

### Step 2 — Content for each section
- [ ] About: short bio paragraph + optional figure/image with meaningful `alt`
- [ ] Skills: list or tags (`ul` / spans) — your learning stack
- [ ] Projects: ≥3 project blurb blocks (`article`) with title + 1–2 sentences + link
- [ ] Contact: form stub (fields next step)
- **Done when:** All four sections have real placeholder content (not lorem-only soup).

### Step 3 — CSS variables & base styles
- [ ] In `css/styles.css` define variables, e.g.:
  ```css
  :root {
    --bg: #f7f7f5;
    --text: #1a1a1a;
    --accent: #0b6e4f;
    --muted: #5c5c5c;
    --space: 1rem;
    --max: 60rem;
  }
  ```
- [ ] Link stylesheet; set `box-sizing: border-box`; readable typography
- [ ] Avoid default “AI purple gradient” look — pick a clear direction and stick to it
- **Done when:** Page uses variables for colors/spacing; no unreadable contrast.

### Step 4 — Layout: nav + sections (Flex/Grid)
- [ ] Nav: `display: flex; justify-content: space-between; align-items: center; gap: ...`
- [ ] Main content max-width centered; section spacing via variables
- [ ] Projects: Flex wrap or Grid for cards/articles
- [ ] Optional Bootstrap **only** for nav/form — keep custom layout visible
- **Done when:** Desktop layout looks intentional without floats.

### Step 5 — Responsive pass
- [ ] Mobile-first base (~360px): stacked nav links or simple wrap; single column projects
- [ ] `@media (min-width: 768px)` and `1024px` upgrades
- [ ] DevTools device toolbar: check 360 / 768 / 1200 — no accidental horizontal scroll
- [ ] Images: `max-width: 100%`
- **Done when:** Usable at 360px and improved at desktop.

### Step 6 — Theme toggle (CSS + JS)
- [ ] Add dark theme overrides:
  ```css
  [data-theme="dark"] {
    --bg: #121212;
    --text: #f0f0f0;
    --muted: #a8a8a8;
  }
  ```
- [ ] Button in header; `js/app.js` toggles `data-theme` on `document.documentElement`
- [ ] Persist: `localStorage.setItem('theme', ...)` on toggle; restore on load
- **Done when:** Theme flips and survives refresh.

### Step 7 — Contact form markup
- [ ] Form fields with associated labels (`for`/`id`): name, email, message
- [ ] `required` on name/email/message; `type="email"` on email
- [ ] Submit button; `action="#"` (no real backend)
- **Done when:** Label clicks focus controls; native required works.

### Step 8 — Client-side validation JS
- [ ] `submit` listener: `preventDefault()`
- [ ] Extra checks (e.g. message min length); show errors in a `#form-errors` element via `textContent`
- [ ] On valid submit: show success message (no page reload); optionally clear fields
- [ ] Prefer `textContent` over `innerHTML` for user-facing strings
- **Done when:** Invalid submit shows errors; valid submit shows success without navigation.

### Step 9 — Accessibility polish
- [ ] Keyboard tab through nav, toggle, form — sensible order
- [ ] Buttons have accessible names; theme button not icon-only without text/aria
- [ ] Heading hierarchy intact after polish
- **Done when:** Main flows work by keyboard; labels complete.

### Step 10 — Network notes + README
- [ ] DevTools Network: reload; note document + CSS + JS statuses (`200`)
- [ ] README: how to open (`index.html` / Live Server), features list, theme persistence
- **Done when:** README exists and Network notes are in your learning notes.

## Definition of Done

- [ ] Semantic sections: About, Skills, Projects, Contact
- [ ] Responsive Flex/Grid; good at ~360px and desktop
- [ ] Theme toggle via CSS variables + `localStorage`
- [ ] Contact form with client-side validation (`preventDefault`)
- [ ] README with open instructions

## Demo script

1. Open `index.html` via Live Server
2. Jump via nav to each section
3. Resize to mobile width — layout stacks cleanly
4. Toggle theme; refresh — theme persists
5. Submit empty contact form — errors; fix fields — success message

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Div soup instead of landmarks | Use `header`/`nav`/`main`/`section`/`footer` |
| Inputs without labels | `for`/`id` pairing |
| Theme only in JS colors | Drive theme through CSS variables |
| `innerHTML` for messages | `textContent` / createElement |
| Forgetting viewport meta | Add it in `<head>` |

## After this

- Build [B — Task / Notes UI](B-task-notes-ui.md)
- Optional stretch: [C — API Explorer Lite](C-api-explorer-lite.md)
