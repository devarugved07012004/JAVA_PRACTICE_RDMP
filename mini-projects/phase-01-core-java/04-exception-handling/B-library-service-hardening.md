# Mini-Project B — Library Service Hardening

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/04-exception-handling.md](../../phase-01-core-java/04-exception-handling.md)  
**Code folder suggestion:** `projects/phase-01/04-exception-handling/library-hardening/` (or harden your existing mini-library in place)  
**Time:** ~6–8 hours at 1 hr/day  

## Goal

Take a prior library or contact project and harden it: add `NotFoundException` and `ValidationException`. **Service throws; UI catches and continues the loop.** No empty `catch` blocks.

## Prerequisites

- [ ] Robust Calculator (pattern practice)
- [ ] Existing [Mini Library](../02-oops/A-mini-library-domain.md) or [Contact Book](../03-collections/A-contact-book.md) codebase to modify

## Learn (open before coding)

- [Creating Exception Classes](https://docs.oracle.com/javase/tutorial/essential/exceptions/creating.html)
- [Catching and Handling](https://docs.oracle.com/javase/tutorial/essential/exceptions/handling.html)
- [Chained Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/chained.html)
- [Best Practices (unchecked vs checked — skim discussions in trail)](https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html)
- [try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) (if you touch files)

## Project layout

```text
library-hardening/
  README.md
  src/
    com/learn/library/
      app/Main.java
      exception/
        NotFoundException.java
        ValidationException.java
        LoanException.java          # optional
      model/...
      service/LibraryService.java
```

## Build steps

### Step 1 — Choose base project & baseline test

- [ ] Copy or open Mini Library / Contact Book
- [ ] Record a short “before” note: what happens today on bad ISBN? (crash / silent / message)
- [ ] Ensure it still compiles:
  ```bash
  javac -d out src/com/learn/library/**/*.java
  java -cp out com.learn.library.app.Main
  ```
- **Done when:** Baseline app runs; you know current failure behavior.

### Step 2 — Add exception types

- [ ] `NotFoundException` — missing book/member/loan/contact
- [ ] `ValidationException` — blank fields, bad formats, invariant violations
- [ ] Optional `LoanException` — unavailable book, max loans, already returned
- [ ] Include detail in message: id/isbn that failed
- **Done when:** Exception classes compile; messages are specific.

### Step 3 — Service throws on not-found

- [ ] Replace null returns / vague messages in `find*` / borrow paths with `throw new NotFoundException(...)`
- [ ] Keep service free of `Scanner` / `System.out` for errors
- **Done when:** Calling borrow with fake id from a tiny test main surfaces `NotFoundException`.

### Step 4 — Service throws on validation

- [ ] Blank title/isbn/name → `ValidationException`
- [ ] Negative qty / empty search query (if applicable) → `ValidationException`
- [ ] Validate at the start of public service methods
- **Done when:** Blank book title cannot enter the store.

### Step 5 — UI catch & continue

- [ ] In menu handlers: try service call → catch domain exceptions → print message → continue loop
- [ ] Do **not** catch and swallow
- [ ] Do **not** `catch (Exception e) {}`
- [ ] Unexpected exceptions may print “Unexpected error” + optional brief cause
- **Done when:** Bad isbn never exits the process; menu redisplays.

### Step 6 — Cover all mutating paths

- [ ] Audit: add, update, delete, borrow, return, search-by-id
- [ ] Each path either succeeds or throws a domain exception the UI knows
- [ ] Checklist in README of paths hardened
- **Done when:** Audit list is complete and tested.

### Step 7 — Optional cause chaining

- [ ] If you wrap `NumberFormatException` from parsing IDs, use `new ValidationException("bad id", cause)`
- [ ] UI still shows a clean message
- **Done when:** At least one chained example exists or you document why not needed.

### Step 8 — Regression demo & README

- [ ] Happy path still works: add → register → borrow → return
- [ ] Failure path: missing ids, blank fields, double-return
- [ ] README: exception strategy (checked vs unchecked) + demo script
- **Done when:** Before/after behavior is clearly improved and documented.

## Definition of Done

- [ ] `NotFoundException` + `ValidationException` in use
- [ ] Service throws; UI catches and continues
- [ ] No empty catch blocks
- [ ] Happy path unbroken
- [ ] README lists hardened operations

## Demo script

1. Add valid book + member; borrow OK
2. Borrow with unknown member → NotFound message; still in menu
3. Add book with blank title → Validation message
4. Return unknown loan → NotFound
5. Complete valid return
6. Quit

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Empty catch “to stop crash” | Print message; continue deliberately |
| Throwing from UI for domain rules | Service owns rules |
| Returning null and hoping | Prefer throw NotFound |
| Same exception for all failures | Split not-found vs validation vs loan rules |
| Logging stack traces as the only UX | User message first; log optional |

## After this

Next topic: [05-multithreading.md](../../phase-01-core-java/05-multithreading.md) · Reuse exceptions in [phase-project](../phase-project/library-manager.md)
