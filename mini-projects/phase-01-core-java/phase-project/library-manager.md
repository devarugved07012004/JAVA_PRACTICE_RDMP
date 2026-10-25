# Mini-Project — Console Library Manager (v1 → v3)

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/phase-project.md](../../phase-01-core-java/phase-project.md)  
**Code folder suggestion:** `projects/phase-01/phase-project/library-console/`  
**Time:** ~21 hours at 1 hr/day (or ~14 on a compressed pace)  

## Goal

Ship a console Library Manager that evolves from a simple menu CRUD app (**v1**) into a layered domain with rules, exceptions, Optional, and stream reports (**v2**), then adds a background task with clean shutdown and polish (**v3**). Meet the phase Definition of Done; prepare repository interfaces for Phase 2 JDBC.

## Prerequisites

- [ ] Topics [01](../../phase-01-core-java/01-java-fundamentals.md) → [06](../../phase-01-core-java/06-java8-plus.md) mastery ≥90% each
- [ ] Prefer having done: Mini Library OOP, collections contact/task board, exception hardening, background indexer prep, library search upgrade

## Learn (open before coding)

- [Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)
- [Collections trail](https://docs.oracle.com/javase/tutorial/collections/index.html)
- [Exceptions trail](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html)
- [Streams trail](https://docs.oracle.com/javase/tutorial/collections/streams/index.html)
- [`LocalDate`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html) · [`Optional`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Optional.html)
- [Executors](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)
- Daily Learn links in [phase-project.md §4](../../phase-01-core-java/phase-project.md)

## Project layout

```text
library-console/
  README.md
  THREAD-MODEL.md                 # before v3 concurrency
  PHASE2-PREP.md                  # tables sketch
  reports/                        # export output (optional)
  src/
    com/learn/library/
      app/
        Main.java
        ConsoleUI.java
      model/
        Book.java
        Member.java
        Loan.java
      repository/
        BookRepository.java
        MemberRepository.java
        LoanRepository.java
        InMemoryBookRepository.java
        InMemoryMemberRepository.java
        InMemoryLoanRepository.java
      service/
        LibraryService.java
      exception/
        NotFoundException.java
        ValidationException.java
        BookUnavailableException.java
        LoanLimitException.java
      report/
        LibraryReports.java
      seed/
        SeedData.java
```

## Build steps

### Step 1 — Repo layout & menu skeleton (v1 start)

- [ ] Create `src/com/learn/library/{model,repository,service,app,exception,report,seed}`
- [ ] `app.Main`: while-loop menu stubs — Books / Members / Loans / Reports / Quit
- [ ] Write `README.md` with compile/run (IDE or CLI)
- [ ] Run once and quit
  ```bash
  javac -d out $(find src -name "*.java")    # Git Bash / WSL
  # PowerShell:
  # javac -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
  java -cp out com.learn.library.app.Main
  ```
- **Done when:** Menu prints; quit works; README exists.

### Step 2 — Book model & in-memory repository

- [ ] `model.Book`: isbn, title, author, genre, year, availableCopies (or available flag + totalCopies)—validate non-blank isbn/title; copies ≥ 0
- [ ] `InMemoryBookRepository` with `Map<String, Book>` by isbn
- [ ] Methods: `save`, `findByIsbn` → later Optional, `findAll`
- [ ] Smoke-test: save 2 books; find one
- **Done when:** Map-backed repo returns saved books by isbn.

### Step 3 — Add / list / search books

- [ ] Menu: Add book → prompt fields → `repository.save`
- [ ] List all via `findAll`
- [ ] Search: stream filter title/author/genre contains query (case-insensitive)
- [ ] Prevent duplicate ISBN on save ([Should](../../phase-01-core-java/phase-project.md))
- **Done when:** Add, list, and search by title or author succeed.

### Step 4 — Members

- [ ] `model.Member`: id, name, email; validate non-blank
- [ ] `InMemoryMemberRepository` (`Map` by id)
- [ ] Menu: Register member / List members / View member’s active loans (stub OK until loans exist)
- **Done when:** Two members register and list correctly.

### Step 5 — Loan model & borrow flow

- [ ] `model.Loan`: loanId, memberId, isbn, borrowedOn, dueOn, returnedOn (nullable)
- [ ] Borrow: create loan; decrement available copies (or set unavailable)
- [ ] Reject if book missing or no copies left
- [ ] Menu Borrow; print confirmation
- **Done when:** Successful borrow reduces availability; second borrow of last copy fails clearly.

### Step 6 — Return & due dates

- [ ] On borrow: `dueOn = LocalDate.now().plusDays(14)` (configurable constant)
- [ ] Return: set `returnedOn = now`; increment availability
- [ ] Helper `boolean isOverdue(Loan)`
- [ ] Force one loan dueOn to yesterday for demo
- **Done when:** Return restores copies; overdue helper true for past-due open loan.

### Step 7 — Exception hardening (end of v1)

- [ ] Add `NotFoundException`, `ValidationException`, `BookUnavailableException` (and optionally `LoanLimitException`)
- [ ] Service/repo throws; `Main`/`ConsoleUI` catches, prints user message, continues loop
- [ ] Walk: add book → register → borrow → return
- [ ] Tag commit/note: **v1 complete**
- **Done when:** Bad isbn/member never crashes menu; week-1 demo path works end-to-end.

### Step 8 — Extract LibraryService (v2 layering)

- [ ] `service.LibraryService` holds repositories
- [ ] Move borrow/return/add/search rules into service methods
- [ ] UI only: read input → call service → print
- [ ] Self-check: no Map/business `if` left in Main beyond menu switches
- **Done when:** Full borrow/return works; Main is orchestration-only.

### Step 9 — Repository interfaces

- [ ] Extract `BookRepository`, `MemberRepository`, `LoanRepository`
- [ ] Rename impls `InMemory*Repository implements …`
- [ ] `LibraryService` depends on interfaces only (constructor injection)
- [ ] Wire impls in `Main`
- **Done when:** Service compiles against interfaces only.

### Step 10 — Business rules + Optional

- [ ] `MAX_LOANS_PER_MEMBER = 3` (configurable)
- [ ] Before borrow: count open loans; throw `LoanLimitException` if at max
- [ ] `findByIsbn` / `findById` return `Optional`; use `orElseThrow`
- [ ] Cannot borrow when availableCopies < 1
- **Done when:** Fourth concurrent loan fails; unavailable book fails with clear exception.

### Step 11 — Stream reports

- [ ] Increment borrow counter on Book (or separate map) each successful borrow
- [ ] Report: `groupingBy(Book::getGenre)` counts
- [ ] Report: top N borrowed (stream sort descending)
- [ ] Report: overdue loans (filter + print member, isbn, dueOn)
- [ ] Report: inventory summary (titles, total copies available)
- [ ] Menu section Reports
- **Done when:** Reports match seed/borrow activity; overdue lists only open past-due.

### Step 12 — Seed data + E2E checklist (end of v2)

- [ ] `SeedData.load(LibraryService)`: 8–12 books, 3–5 members, optional sample loans (include 1 overdue)
- [ ] Call on startup
- [ ] Write manual test checklist: register, add, borrow, return, max-loans fail, search, reports
- [ ] Execute; fix failures
- [ ] Tag/note: **v2 complete**
- **Done when:** Fresh run is demoable without typing a full catalog; checklist green.

### Step 13 — Thread model note (v3 start)

- [ ] Write `THREAD-MODEL.md` **before** coding concurrency
- [ ] Main thread = CLI only
- [ ] One background executor for overdue refresh or stats
- [ ] Document shared objects + race avoidance (sync / concurrent collection / single-thread executor)
- **Done when:** Note exists before Step 14 code.

### Step 14 — Background overdue/stats + shutdown

- [ ] `Executors.newSingleThreadScheduledExecutor()` (or fixed pool)
- [ ] Schedule overdue recount / stats every N seconds; quiet log line
- [ ] Ensure shared reads safe per THREAD-MODEL.md
- [ ] On Quit: `shutdown` → `awaitTermination` → `shutdownNow` if needed
- [ ] Confirm process exits promptly every time
- **Done when:** Background task runs while menu responsive; Quit returns to OS prompt.

### Step 15 — Optional export, polish, Phase 2 prep (v3 finish)

- [ ] Menu: Export overdue/catalog to `reports/overdue.txt` with try-with-resources; IO errors don’t crash
- [ ] Rename/split god classes; packages match layout above
- [ ] Re-run E2E checklist
- [ ] Write `PHASE2-PREP.md`: entities → tables/columns/PKs/FKs; which repo methods become SQL
- [ ] README: features, packages, sample usage, compile/run
- [ ] Tick Definition of Done below honestly
- [ ] Tag/note: **v3 / Phase 1 project complete**
- **Done when:** DoD critical items met; Phase 2 prep note exists.

## Definition of Done

- [ ] App runs from documented command (IDE or `javac`/`java`)
- [ ] Seed catalog loads; demo borrow/return works end-to-end
- [ ] Search and overdue report use Streams and `LocalDate`
- [ ] Invalid operations → clear messages (no crash, no empty catch)
- [ ] Packages: UI / service / repository / model (+ exception)
- [ ] Repository interfaces for books, members, loans
- [ ] `Optional` for find-by-id style lookups
- [ ] At least one background/async feature with clean shutdown
- [ ] README explains features, packages, sample script
- [ ] PHASE2-PREP notes map domain to future SQL tables
- [ ] Must features from [phase-project.md §6](../../phase-01-core-java/phase-project.md) implemented

## Demo script

1. Start app → seed loads
2. List books; search “clean” (or seeded title fragment)
3. Register member (or use seeded); borrow a book → copies decrement
4. Attempt borrow beyond max loans → clear error
5. Reports → genre grouping; overdue list shows seeded overdue
6. Return book → copies restore
7. (v3) Wait for background log line; export overdue file
8. Quit → shell prompt returns immediately

## Common mistakes

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

## After this

Phase 1 complete when DoD is met · Next: [Phase 2 — SQL & PostgreSQL](../../phase-02-data/07-sql-postgresql.md) (when available) · Keep repository interfaces stable for JDBC swap
