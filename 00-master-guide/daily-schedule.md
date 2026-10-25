# Daily Schedule — 1 Hour / Day

Practical rhythm for the Java Full Stack Mastery Roadmap. Protect the habit; depth comes from weeks of consistency, not marathon days.

**Cadence:** 1 hour/day · ~7 hours/week · ~72–83 weeks total.

---

## Daily 60-minute template

Every topic file’s **§4 Daily learning tasks** already has the day’s **Learn** links and **Practice** steps with checkboxes. Use that section as your session script.

| Block | Minutes | What you do | What counts as success |
|-------|--------:|-------------|------------------------|
| Learn | 10 | Open that day’s **Learn** links in §4; skim only what’s needed | Can restate the idea in 2–3 sentences |
| Practice | 35 | Do that day’s **Practice** coding task | Something compiles/runs |
| Notes | 10 | Capture patterns, pitfalls, commands, “why” | Future-you can reuse the note |
| Checklist | 5 | Tick **Learn** + **Practice** + day boxes in §4; weekly → [progress-tracker.md](./progress-tracker.md) | Honest checkmarks only |

### Rules for the hour

- Open the current topic’s **§4** and work the next unchecked day — resources are on that day, not at the end of the file.
- Start with a **single next action** (the Practice line).
- Coding time is sacred: use Learn links for 10 minutes max, then build.
- If Learn runs long, **cut reading**, not Practice.
- End by checking boxes and writing “tomorrow starts with Day N…”.

### Example day (Topic 01 Fundamentals — Day 3)

| Min | Activity |
|----:|----------|
| 0–10 | Open Day 3 **Learn** links (Variables + Primitive Data Types) |
| 10–45 | Do Day 3 **Practice** (all primitives + overflow demo) |
| 45–55 | Note overflow gotcha in your journal |
| 55–60 | Check Learn + Practice + Day 3 boxes |

---

## Weekly rhythm

| Day | Focus | Typical work |
|-----|--------|--------------|
| **1–4** | New material | Concepts + exercises for the current topic |
| **5** | Deepen | Harder exercises, edge cases, refactor yesterday’s code |
| **6** | Project | Phase project or capstone slice only |
| **7** | Review | Re-read notes, re-run demos, fill tracker, plan next week |

### Weekly checklist (Day 7)

- [ ] Hours logged ≈ 7 (or honest miss count)
- [ ] Exercises for the week listed as done/partial
- [ ] Project % updated
- [ ] Blockers written (not just remembered)
- [ ] Next week focus = one topic + one project outcome
- [ ] Confidence 1–5 recorded

---

## Handling missed days

| Situation | Response |
|-----------|----------|
| Missed 1 day | Continue; do **not** double tomorrow. Note it in the tracker. |
| Missed 2–3 days | Resume at the last unfinished slice. Optional: use Day 7 review to recover one concept, still 1 hour. |
| Missed a full week | Restart from last green checklist item. Shrink scope (one method, one query) until momentum returns. |
| Feeling behind timeline | Stay on topic until ≥90% checklist. Timeline is a guide, not a race. |
| Want to “catch up” with 3-hour days | Prefer **extra Day-6 project hours** on weekends over cramming concepts. Max 2 hours/day if you must — quality over volume. |

**Do not skip phase gates** to “make up time.” Gaps compound in Spring and distributed phases.

---

## Tools setup checklist

Complete this **before Topic 01** coding days. Re-check after OS updates.

### JDK 21 LTS

- [ ] JDK 21 (LTS) installed (`java -version` shows 21.x)
- [ ] `JAVA_HOME` set
- [ ] `javac` available on PATH
- [ ] Know how to create a simple project (Maven or Gradle) — pick one and stick with it for Phase 1–2

### IDE

- [ ] IntelliJ IDEA (Community is fine) **or** VS Code + Java Extension Pack
- [ ] Can run a `main` method and debug with a breakpoint
- [ ] Formatter / organize imports configured lightly (don’t over-tune)

### Git

- [ ] Git installed (`git --version`)
- [ ] User name/email set locally
- [ ] Can `init`, `add`, `commit`, `status`, `log`
- [ ] `.gitignore` ready for `target/`, `.idea/`, `.env`, `*.class`

### PostgreSQL

- [ ] PostgreSQL installed locally **or** via Docker
- [ ] Can connect with `psql` or a GUI (pgAdmin / DBeaver)
- [ ] Create a practice database and run a `SELECT 1`

### Postman (or Insomnia)

- [ ] App installed
- [ ] Can create a collection and send GET/POST with JSON body
- [ ] Know where to save collection exports for phase projects

### Docker Desktop

- [ ] Docker Desktop installed and running (Windows)
- [ ] `docker version` and `docker compose version` work
- [ ] Can pull and run a sample image (e.g. `hello-world` or `postgres`)

### Optional but useful early

- [ ] Markdown preview for notes
- [ ] Terminal you’re comfortable with (PowerShell / Windows Terminal)
- [ ] Account for cloud labs when you reach AWS (free-tier aware; no production spend)

---

## Environment smoke test (30–45 min once)

Run once after setup; tick when green:

```text
1. java -version          → 21.x
2. New Maven/Gradle hello → prints Hello
3. git commit             → works in a scratch repo
4. CREATE DATABASE practice; SELECT 1;
5. Postman GET https://httpbin.org/get (or local)
6. docker run hello-world
```

- [ ] All six smoke checks pass

---

## Session hygiene

| Do | Don’t |
|----|-------|
| Keep one active branch/folder per phase project | Scatter unfinished tutorials across random folders |
| Commit when a slice works | Wait for “perfect” before committing |
| Write blockers in the tracker | Restart the whole topic from scratch every miss |
| Prefer official docs + topic files in this PLAN | Chase every YouTube series for the same concept |

---

## Related docs

- [learning-path.md](./learning-path.md) — order and gates  
- [timeline-estimates.md](./timeline-estimates.md) — expected weeks  
- [weekly-milestones.md](./weekly-milestones.md) — week checkpoints  
- [progress-tracker.md](./progress-tracker.md) — log hours and confidence  
