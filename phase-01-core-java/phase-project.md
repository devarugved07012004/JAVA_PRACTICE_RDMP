# Phase 1 Project — Console Library Manager

**Duration:** ~2–3 weeks (after topics 01–06) · **Cadence:** 1 hour/day  
**Outcome:** A console Library Manager that evolves from a simple menu app into a layered domain model using collections, exceptions, streams, and light concurrency

---

## 1. Why this topic is important

A phase project forces concepts to work together. Isolated drills teach syntax; this project teaches **design under change**: start simple, then add layers, richer rules, reporting, and background work without rewriting everything from scratch.

The Library Manager mirrors real systems (catalog, members, loans, search, overdue rules). Finishing it proves Phase 1 fluency and creates a codebase you will later persist with SQL/JDBC in Phase 2—same domain, stronger infrastructure.

---

## 2. Prerequisites

- Topics [01](01-java-fundamentals.md) → [06](06-java8-plus.md) mastery checklists ≥90% each
- Comfortable with packages, OOP, collections, exceptions, basic executors/CompletableFuture, Streams, `java.time`
- Git optional but recommended for personal snapshots (learning only)

---

## 3. Concepts to learn (beginner → intermediate → advanced)

This project **applies** prior topics; new learning is integration and design.

### Beginner (Project v1)

| Focus | Apply |
|-------|--------|
| Console UX | Menus, input validation loops |
| Domain classes | `Book`, `Member`, `Loan` |
| In-memory stores | `List` / `Map` indexes |
| Basic CRUD | Add/list/search |

### Intermediate (Project v2)

| Focus | Apply |
|-------|--------|
| Layering | `model` / `repository` / `service` / `app` |
| Rules | Borrow limits, due dates, overdue fees (simple) |
| Exceptions | `NotFoundException`, `ValidationException`, `LoanException` |
| Streams | Search, filter available, group-by author/genre reports |
| Optional | `findById` returns `Optional` |

### Advanced (Project v3)

| Focus | Apply |
|-------|--------|
| Concurrency | Background overdue scanner or async report generation |
| Thread safety | Concurrent map or confined mutations + clear ownership |
| Design polish | Interfaces for repositories; swap in-memory impl later (Phase 2) |
| Observability lite | Simple action log / timestamps |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35–45 min build) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 40 min Practice steps · 10 min notes · mark boxes

### Week 1 — Vertical slice (Days 1–7)

- [ ] **Day 1 — Repo layout & menu skeleton**
  - [ ] **Learn:** [Creating and Using Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html) · [Classes](https://docs.oracle.com/javase/tutorial/java/javaOO/classes.html)
  - [ ] **Practice:** Repo layout, packages, Main menu, README
    1. Create project root `library-console/` with `src/com/learn/library/{model,repository,service,app}`.
    2. Add `app.Main` with while-loop menu stubs: Books / Members / Loans / Quit.
    3. Write `README.md`: how to compile/run (IDE or `javac`/`java` with packages).
    4. Run and quit once.
    5. **Done when:** Menu prints, quit works, README exists.

- [ ] **Day 2 — Book model & repository**
  - [ ] **Learn:** [Map Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html) · [`HashMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashMap.html)
  - [ ] **Practice:** Book + in-memory BookRepository
    1. `model.Book`: isbn, title, author, genre, available flag; validate non-blank isbn/title.
    2. `repository.InMemoryBookRepository` with `Map<String, Book>` by isbn.
    3. Methods: `save`, `findByIsbn`, `findAll`.
    4. Smoke-test from a tiny main or temporary calls: save 2 books, find one.
    5. **Done when:** Map-backed repo returns saved books by isbn.

- [ ] **Day 3 — Add / list / search books**
  - [ ] **Learn:** [Aggregate Operations / Streams](https://docs.oracle.com/javase/tutorial/collections/streams/index.html) · [`Stream` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html)
  - [ ] **Practice:** Wire add / list / search in the menu
    1. Menu: Add book (prompt fields) → `repository.save`.
    2. List all books via `findAll`.
    3. Search: `findAll().stream().filter(b -> title/author contains query)` (case-insensitive).
    4. Print matches.
    5. **Done when:** You can add, list, and search by title or author successfully.

- [ ] **Day 4 — Members**
  - [ ] **Learn:** [Classes & Objects](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html) · [List Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/list.html)
  - [ ] **Practice:** Member model + repository
    1. `model.Member`: id, name, email; validate non-blank.
    2. `InMemoryMemberRepository` (`Map` by id).
    3. Menu: Register member / List members.
    4. **Done when:** Two members register and list correctly.

- [ ] **Day 5 — Borrow flow**
  - [ ] **Learn:** [Encapsulation / access](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html) · [Exceptions trail (skim)](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html)
  - [ ] **Practice:** Loan model + borrow
    1. `model.Loan`: loanId, memberId, isbn, borrowedOn, dueOn, returnedOn (nullable).
    2. Borrow: create loan; set book `available = false`.
    3. Reject borrow if book missing or already unavailable (throw or clear error for now).
    4. Menu option Borrow; print confirmation.
    5. **Done when:** Successful borrow marks book unavailable; second borrow of same book fails.

- [ ] **Day 6 — Return & due dates**
  - [ ] **Learn:** [`LocalDate` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html) · [Date-Time trail](https://docs.oracle.com/javase/tutorial/datetime/index.html)
  - [ ] **Practice:** Return flow + overdue flag
    1. On borrow set `dueOn = LocalDate.now().plusDays(14)`.
    2. Return: set `returnedOn = now`; mark book available.
    3. Helper `boolean isOverdue(Loan)` → `returnedOn == null && LocalDate.now().isAfter(dueOn)`.
    4. Demo with a loan forced to past due (temporarily set dueOn to yesterday).
    5. **Done when:** Return restores availability; overdue helper returns true for past-due open loan.

- [ ] **Day 7 — Exception hardening**
  - [ ] **Learn:** [Catching and Handling Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/handling.html) · [Creating Exception Classes](https://docs.oracle.com/javase/tutorial/essential/exceptions/creating.html)
  - [ ] **Practice:** Domain exceptions + UI recovery
    1. Add `NotFoundException`, `BookUnavailableException` (unchecked or checked — pick one style).
    2. Service/repo throws them; `Main` catches, prints user message, continues loop.
    3. Walk happy path: add book → register member → borrow → return.
    4. **Done when:** Bad isbn/member never crashes the menu; week demo path works end-to-end.

### Week 2 — Layering & reports (Days 8–14)

- [ ] **Day 8 — Extract LibraryService**
  - [ ] **Learn:** [Defining Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html) · [OO Concepts](https://docs.oracle.com/javase/tutorial/java/concepts/index.html)
  - [ ] **Practice:** Thin UI via LibraryService
    1. Create `service.LibraryService` holding repositories.
    2. Move borrow/return/add/search rules into service methods.
    3. `Main` only: read input → call service → print results.
    4. Grep/self-check: no Map/business `if` left in `Main` beyond menu switches.
    5. **Done when:** Full borrow/return still works and `Main` is orchestration-only.

- [ ] **Day 9 — Repository interfaces**
  - [ ] **Learn:** [Defining an Interface](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html) · [Implementing an Interface](https://docs.oracle.com/javase/tutorial/java/IandI/usinginterface.html)
  - [ ] **Practice:** BookRepository / MemberRepository / LoanRepository
    1. Extract interfaces with the methods you already use.
    2. Rename impls to `InMemory*Repository implements …`.
    3. `LibraryService` depends on interfaces only (constructor injection).
    4. **Done when:** Service compiles against interfaces; in-memory impls wired in `Main`.

- [ ] **Day 10 — Business rules**
  - [ ] **Learn:** [How to Throw Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/throwing.html) · [`Optional` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Optional.html)
  - [ ] **Practice:** Max loans + unavailable checks
    1. Constant `MAX_LOANS_PER_MEMBER = 3` (or configurable).
    2. Before borrow: count open loans for member; throw if at max.
    3. Cannot borrow if `!book.isAvailable()`.
    4. Use `Optional` from `findByIsbn` / `findById`; `orElseThrow`.
    5. **Done when:** Fourth concurrent loan fails; unavailable book fails with clear exception.

- [ ] **Day 11 — Genre & top-borrowed reports**
  - [ ] **Learn:** [`Collectors.groupingBy`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html#groupingBy(java.util.function.Function)) · [Reduction](https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html)
  - [ ] **Practice:** Reports menu
    1. Increment a borrow counter on `Book` (or separate map) each successful borrow.
    2. Report A: `groupingBy(Book::getGenre)` print counts.
    3. Report B: top N borrowed via stream sorted by counter descending.
    4. **Done when:** Both reports match your seed/borrow activity.

- [ ] **Day 12 — Overdue report**
  - [ ] **Learn:** [`LocalDate` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html) · [`Stream` filter/map](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html)
  - [ ] **Practice:** Overdue loans report
    1. `loans.stream().filter(this::isOverdue).…` print member, isbn, dueOn.
    2. Menu option “Overdue report”.
    3. Seed at least one overdue loan for the demo.
    4. **Done when:** Report lists only open past-due loans.

- [ ] **Day 13 — Seed data**
  - [ ] **Learn:** [List / Arrays helpers](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/List.html) · [Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)
  - [ ] **Practice:** Hardcoded catalog loader
    1. Create `SeedData.load(LibraryService)` or repository-level seeder.
    2. Insert 8–12 books, 3–5 members, optional sample loans.
    3. Call from `Main` on startup (flag or always).
    4. **Done when:** Fresh run shows populated list without manual typing.

- [ ] **Day 14 — Manual E2E test pass**
  - [ ] **Learn:** Skim your README + [Exceptions handling](https://docs.oracle.com/javase/tutorial/essential/exceptions/handling.html)
  - [ ] **Practice:** Scripted manual test cases
    1. Write a checklist: register, add, borrow, return, max-loans fail, search, reports.
    2. Execute each; mark pass/fail.
    3. Fix any failures found.
    4. **Done when:** All checklist items pass in one clean run.

### Week 3 — Concurrency & Definition of Done (Days 15–21)

- [ ] **Day 15 — Thread model design**
  - [ ] **Learn:** [Processes and Threads](https://docs.oracle.com/javase/tutorial/essential/concurrency/procthread.html) · [Executors](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)
  - [ ] **Practice:** Write thread model note first
    1. Document: main thread = CLI only.
    2. Document: one background executor for overdue refresh or stats.
    3. Document: which objects are shared and how you’ll avoid races (sync, concurrent collection, or single-thread executor).
    4. **Done when:** One-page note exists before coding Day 16.

- [ ] **Day 16 — Background overdue/stats**
  - [ ] **Learn:** [`ExecutorService` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html) · [`CompletableFuture` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html)
  - [ ] **Practice:** Periodic background refresh
    1. `Executors.newSingleThreadScheduledExecutor()` (or fixed pool).
    2. Schedule overdue recount / stats recompute every N seconds; print a quiet log line.
    3. Ensure shared reads are safe for your chosen model.
    4. **Done when:** Background task runs while menu remains responsive.

- [ ] **Day 17 — Clean shutdown**
  - [ ] **Learn:** [Interrupts](https://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html) · [`ExecutorService` shutdown methods](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html)
  - [ ] **Practice:** Quit shuts down executors
    1. On Quit: `scheduler.shutdown()`; `awaitTermination`; if needed `shutdownNow()`.
    2. Confirm process exits (no non-daemon thread left).
    3. **Done when:** Quit returns to OS prompt promptly every time.

- [ ] **Day 18 — Optional text export**
  - [ ] **Learn:** [The try-with-resources Statement](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) · [Byte Streams / Character Streams (I/O)](https://docs.oracle.com/javase/tutorial/essential/io/streams.html)
  - [ ] **Practice:** Export a text report file
    1. Menu: Export overdue (or catalog) to `reports/overdue.txt`.
    2. Write with `try (PrintWriter/BufferedWriter …)`.
    3. Catch IO errors; show user message; don’t crash.
    4. **Done when:** File exists with expected lines after export.

- [ ] **Day 19 — Refactor polish**
  - [ ] **Learn:** [Google Java Style Guide (skim)](https://google.github.io/styleguide/javaguide.html) · [Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)
  - [ ] **Practice:** Names, packages, no god classes
    1. Rename unclear types/methods; keep packages coherent.
    2. Split any class doing UI + persistence + rules.
    3. Re-run Day 14 checklist quickly.
    4. **Done when:** Structure matches model/repository/service/app; checklist still green.

- [ ] **Day 20 — Definition of Done pass**
  - [ ] **Learn:** Recap links from prior topics as needed ([Collections](https://docs.oracle.com/javase/tutorial/collections/index.html) · [Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html) · [Concurrency](https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html))
  - [ ] **Practice:** Full DoD / §12 pass
    1. Open §12 / Definition of Done below; tick each item only if demoable.
    2. Fix gaps (exceptions, reports, shutdown, README).
    3. Record remaining gaps honestly.
    4. **Done when:** DoD is ≥ your personal ship bar (aim all critical items).

- [ ] **Day 21 — Phase 2 prep notes**
  - [ ] **Learn:** Skim [Java SE API hub](https://docs.oracle.com/en/java/javase/17/docs/api/) + your repository interfaces
  - [ ] **Practice:** Map domain to future tables
    1. List entities → proposed tables (`book`, `member`, `loan`).
    2. List columns + PKs/FKs from your fields.
    3. Note which repository methods become SQL later.
    4. **Done when:** One-page Phase 2 prep note exists with tables/columns sketched.

*On a 2-week pace: merge Week 3 into Days 12–14 by shipping one small background feature only.*

---

## 5. Hands-on exercises

Use these as spike tasks before integrating into the main app:

1. Prototype ISBN → `Book` HashMap index.
2. Prototype borrow validation in a pure function/service method with tests-by-main.
3. Stream query: list books published after a year, sorted by title.
4. Optional pipeline: find member by id or throw `NotFoundException`.
5. Simulate 2 threads calling borrow on the last copy—decide your consistency approach.
6. Format overdue messages with `DateTimeFormatter`.
7. Group loans by member id using `Collectors.groupingBy`.
8. Write seed JSON-like plaintext and parse lines into books (keep it simple).

---

## 6. Mini-projects

The phase project **is** the mini-project suite, shipped as progressive versions:

- **Full steps:** [Console Library Manager v1→v3](../mini-projects/phase-01-core-java/phase-project/library-manager.md)

### Feature specification (build all marked Must)

**Catalog**

- [Must] Add / update / list books (id, ISBN, title, author, genre, year, available copies)
- [Must] Search by title/author/genre (case-insensitive contains)
- [Should] Prevent duplicate ISBN

**Members**

- [Must] Register / list members
- [Must] View a member’s active loans

**Loans**

- [Must] Borrow book (decrement availability; create loan with borrowed + due date)
- [Must] Return book (increment availability; close loan)
- [Must] Reject invalid borrows with clear errors
- [Should] Configurable loan period (e.g., 14 days) and max active loans

**Reports**

- [Must] List overdue loans
- [Must] Inventory summary (total titles, total copies available)
- [Should] Group books by genre (Streams)
- [Could] “Most borrowed” leaderboard

**Quality**

- [Must] Layered packages; thin menu/UI
- [Must] Custom exceptions for not-found / validation / loan rules
- [Must] `Optional` for find methods
- [Should] Background overdue/stats task with clean shutdown
- [Could] Text report export

---

## 7. Industry use cases

- **Library / media systems:** Catalog + circulation rules
- **Inventory + reservations:** Availability counts and loans ≈ stock movements
- **SaaS trial entitlements:** Due dates and limits resemble subscription quotas
- **Layered architecture practice:** Same shape as service + repository apps in Spring
- **Async maintenance jobs:** Background overdue scanner ≈ scheduled tasks in production

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| All logic in `main` / menu switch | Push rules into `LibraryService` |
| UI printing inside repositories | Repositories only store/retrieve |
| Mutable shared lists escaped to UI | Return copies or unmodifiable views |
| Silent failures on bad input | Throw/catch with clear messages |
| Date as `String` everywhere | Use `LocalDate` end-to-end |
| Premature parallel streams | One careful background executor is enough |
| No seed data | Always ship a demo catalog |
| Skipping README run steps | Document compile/run commands |
| Mixing IDs casually | One clear ID strategy (UUID string or long sequence) |
| Big-bang rewrite each week | Evolve v1 → v2 → v3 |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| Prior topic files in this folder | Concept refresh while building |
| [Oracle OO trail](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html) | Modeling |
| [Collections trail](https://docs.oracle.com/javase/tutorial/collections/index.html) | Store choices |
| [Exceptions trail](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html) | Error design |
| [Concurrency trail](https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html) | Background tasks |
| [Streams trail](https://docs.oracle.com/javase/tutorial/collections/streams/index.html) | Reports |
| [Date-Time trail](https://docs.oracle.com/javase/tutorial/datetime/index.html) | Due/overdue |

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Use this as backup index.

- Packages: https://docs.oracle.com/javase/tutorial/java/package/index.html
- `LocalDate`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html
- `Optional`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Optional.html
- `Collectors`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html
- `ExecutorService`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html
- `CompletableFuture`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html
- `ConcurrentHashMap`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html
- Try-with-resources: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
- Java SE API hub (17): https://docs.oracle.com/en/java/javase/17/docs/api/

---

## 11. Practice roadmap

### Evolution path

```text
v1 Console CRUD  →  v2 Layered domain + rules + streams reports  →  v3 Background task + polish
```

### Week plan (summary)

| Week | Deliverable |
|------|-------------|
| 1 | Working borrow/return with in-memory data |
| 2 | Interfaces, rules, stream reports, seed data |
| 3 | Concurrency feature, export optional, DoD met |

### How this prepares for Phase 2 (SQL & JDBC)

| Phase 1 artifact | Phase 2 direction |
|------------------|-------------------|
| `Book` / `Member` / `Loan` models | Tables + primary/foreign keys |
| Repository interfaces | JDBC implementations behind same interfaces |
| In-memory `Map` indexes | Indexes/constraints in PostgreSQL |
| Borrow/return service rules | Transactions (`commit`/`rollback`) |
| Seed data class | SQL seed scripts |
| Console UI | Keep temporarily; later replace with API/UI in later phases |

**Phase 2 gate hint:** Keep repositories interface-based and services free of console code so swapping storage does not rewrite business rules.

### Suggested package layout

```text
com.yourname.library/
  app/           # Main, ConsoleUI
  model/         # Book, Member, Loan, enums
  repository/    # interfaces + memory impl
  service/       # LibraryService, rules
  exception/     # custom exceptions
  report/        # stream-based reports (optional)
```

---

## 12. Mastery checklist

### Definition of Done (project)

- [ ] App runs from a documented command (IDE or `javac`/`java`)
- [ ] Seed catalog loads; demo borrow/return works end-to-end
- [ ] Search and overdue report work using Streams and `LocalDate`
- [ ] Invalid operations surface clear messages (no crash, no empty catch)
- [ ] Packages separated: UI / service / repository / model
- [ ] Repository interfaces exist for books and members (and loans if modeled)
- [ ] `Optional` used for find-by-id style lookups
- [ ] At least one background/async feature with clean shutdown
- [ ] README explains features, packages, and sample usage script
- [ ] Personal notes list what will map to SQL tables in Phase 2

### Personal mastery

- [ ] Can explain every package’s responsibility in 30 seconds
- [ ] Can add a new report (e.g., loans due in N days) in under an hour
- [ ] Can point to where Phase 2 JDBC will plug in
- [ ] Phase 1 topic checklists remain complete; no skipped fundamentals
- [ ] Ready to start Phase 2 — [SQL & PostgreSQL](../phase-02-data/07-sql-postgresql.md) (when available)

---

## Quick reference — success criteria

You are done with Phase 1 when the Library Manager meets the Definition of Done above and you can maintain it without breaking layers. Do not rush into Spring until this console system feels boringly clear.
