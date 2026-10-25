# 04 — Exception Handling

**Duration:** ~1–2 weeks (7–14 days) · **Cadence:** 1 hour/day  
**Focus:** Hierarchy, try/catch/finally, throw/throws, custom exceptions, clean error design

---

## 1. Why this topic is important

Real programs fail: bad input, missing files, network blips, violated business rules. Exception handling is how Java surfaces and recovers from those failures without corrupting state or crashing silently. Good error design makes APIs honest (`throws` / documented unchecked exceptions), keeps `main`/UI layers user-friendly, and keeps domain logic free of messy nested checks.

Every later topic (I/O, JDBC, Spring, concurrency) assumes you can catch what you can handle, propagate what you cannot, and never swallow errors empty-handed.

---

## 2. Prerequisites

- Completed [03-collections.md](03-collections.md) mastery checklist (≥90%)
- Comfortable with methods, classes, and multi-layer packages (model/service/app)
- Basic file/console I/O awareness helpful

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| What an exception is | Disruptive event; stack unwinding idea |
| `try` / `catch` | Guard + handle |
| `finally` | Always-run cleanup (pre–try-with-resources mindset) |
| Checked vs unchecked | `Exception` vs `RuntimeException` intuition |
| Common runtime exceptions | `NullPointerException`, `IllegalArgumentException`, `IndexOutOfBoundsException` |
| Reading stack traces | Top → cause; find *your* code frames |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| `throw` | Create/signal failures intentionally |
| `throws` | Declare checked exceptions on methods |
| Multiple catch / multi-catch | Specific before general |
| Exception hierarchy | `Throwable` → `Error` / `Exception` |
| Custom exceptions | Domain failures as types |
| Try-with-resources | `AutoCloseable`; prefer over manual `finally` |
| Wrapping exceptions | Preserve cause with `initCause` / constructors |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| When checked vs unchecked for APIs | Design choice; consistency |
| Error vs Exception | Don’t catch `Error` in app code |
| Best practices | Fail fast; meaningful messages; don’t use exceptions for flow control |
| Layered handling | Translate low-level exceptions at boundaries |
| Suppressed exceptions | Try-with-resources details |
| Assertions vs exceptions | `assert` for impossible internals (disabled by default) |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Core mechanics (Days 1–7)

- [ ] **Day 1 — Catch & read stack traces**
  - [ ] **Learn:** [What Is an Exception?](https://docs.oracle.com/javase/tutorial/essential/exceptions/definition.html) · [Catching and Handling Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/handling.html) · [`ArithmeticException` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/ArithmeticException.html)
  - [ ] **Practice:** Catch ArithmeticException / NumberFormatException
    1. Create `ExceptionBasics.java`. In try: `int x = 10 / 0;` catch `ArithmeticException`; print `e.getMessage()`.
    2. In another try: `Integer.parseInt("abc")`; catch `NumberFormatException`.
    3. Call `e.printStackTrace()` once; find **your** class name and line number in the trace.
    4. Write 2 sentences: what each frame means (method → caller).
    5. **Done when:** Both catches run and you can point to your line in the stack trace.

- [ ] **Day 2 — Exception hierarchy**
  - [ ] **Learn:** [The Catch or Specify Requirement](https://docs.oracle.com/javase/tutorial/essential/exceptions/catchOrSpecify.html) · [Unchecked Exceptions — The Controversy](https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html) · [`Throwable` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Throwable.html)
  - [ ] **Practice:** Draw Throwable tree; classify 10 types
    1. Draw: `Throwable` → `Error` / `Exception` → `RuntimeException` under Exception.
    2. List 10 types (e.g. `IOException`, `SQLException`, `NullPointerException`, `IllegalArgumentException`, `FileNotFoundException`, `ClassCastException`, `InterruptedException`, `NumberFormatException`, `OutOfMemoryError`, `ArithmeticException`).
    3. Mark each checked vs unchecked (or Error).
    4. **Done when:** Diagram + 10 classifications are written in notes.

- [ ] **Day 3 — `finally` cleanup**
  - [ ] **Learn:** [The finally Block](https://docs.oracle.com/javase/tutorial/essential/exceptions/finally.html)
  - [ ] **Practice:** finally always runs (even with return)
    1. Method `demoFinally()`: try prints `"try"`, returns 1; finally prints `"finally"`; catch unused.
    2. Call it and print the return value — confirm `"finally"` still printed.
    3. Second demo: throw inside try; catch; finally still prints.
    4. **Done when:** Both demos show finally executing; notes say when finally is skipped (JVM kill — rare).

- [ ] **Day 4 — `throw` for validation**
  - [ ] **Learn:** [How to Throw Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/throwing.html) · [`IllegalArgumentException` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/IllegalArgumentException.html)
  - [ ] **Practice:** Service methods throw on bad input
    1. Create `UserService.register(String email, int age)`.
    2. If email blank/null → `throw new IllegalArgumentException("email required")`.
    3. If age &lt; 0 or &gt; 150 → throw with clear message.
    4. From `main`, call valid once + two invalid (try/catch each).
    5. **Done when:** Valid path succeeds; invalid paths throw and are caught with messages.

- [ ] **Day 5 — Checked exceptions (I/O)**
  - [ ] **Learn:** [Specifying the Exceptions Thrown by a Method](https://docs.oracle.com/javase/tutorial/essential/exceptions/declaring.html) · [`IOException` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/IOException.html) · [`FileNotFoundException` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/FileNotFoundException.html)
  - [ ] **Practice:** Read a file; handle missing file
    1. Create `notes.txt` with 2 lines next to your class (or absolute path).
    2. Use `BufferedReader`/`FileReader` (or `Files.readAllLines`) to print lines.
    3. Point at a missing path; catch `FileNotFoundException` or `IOException`; print friendly message.
    4. Note: method must `throws IOException` **or** catch — try both briefly.
    5. **Done when:** Existing file prints; missing file is handled without crash.

- [ ] **Day 6 — Propagate vs handle**
  - [ ] **Learn:** [Catch or Specify](https://docs.oracle.com/javase/tutorial/essential/exceptions/catchOrSpecify.html) · [Putting It All Together](https://docs.oracle.com/javase/tutorial/essential/exceptions/putItTogether.html)
  - [ ] **Practice:** Same I/O method, two designs
    1. Version A: `readLines(path)` declares `throws IOException`; `main` catches.
    2. Version B: `readLinesHandled(path)` catches inside and returns `List` or empty + logs message.
    3. Call both from `main` with good and bad paths.
    4. Write 3 sentences: when you’d propagate vs handle at the boundary.
    5. **Done when:** Both versions work and notes explain the tradeoff.

- [ ] **Day 7 — Multi-catch & catch order**
  - [ ] **Learn:** [Catching Multiple Exception Types](https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html) · [The try Block](https://docs.oracle.com/javase/tutorial/essential/exceptions/try.html)
  - [ ] **Practice:** Multi-catch and catch-order lab
    1. Write multi-catch: `catch (NumberFormatException | ArithmeticException e)`.
    2. Intentionally put `catch (Exception e)` **above** a more specific catch — confirm compile error.
    3. Fix order: specific first, general last.
    4. Trigger each branch once with different inputs.
    5. **Done when:** Multi-catch works and wrong-order compile error is fixed.

### Week 2 — Design & polish (Days 8–14)

- [ ] **Day 8 — Custom exceptions**
  - [ ] **Learn:** [Creating Exception Classes](https://docs.oracle.com/javase/tutorial/essential/exceptions/creating.html) · [`Exception` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Exception.html) · [`RuntimeException` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/RuntimeException.html)
  - [ ] **Practice:** LibraryException (checked) + NotFoundException (unchecked)
    1. `class LibraryException extends Exception` with `(String msg)` and `(String msg, Throwable cause)`.
    2. `class NotFoundException extends RuntimeException` with message constructor.
    3. Method that `throws LibraryException`; method that throws `NotFoundException`.
    4. Show: checked forces handle/declare; unchecked does not.
    5. **Done when:** Both types compile and the checked-force behavior is demonstrated.

- [ ] **Day 9 — Wrapping with cause**
  - [ ] **Learn:** [Chained Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/chained.html) · [`Throwable` constructors](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Throwable.html)
  - [ ] **Practice:** Catch IO, rethrow domain with cause
    1. In a service: catch `IOException`, `throw new LibraryException("Failed to load catalog", ioEx)`.
    2. In `main`, catch `LibraryException`; print message; print `e.getCause()`.
    3. Confirm cause is the original `IOException`.
    4. **Done when:** `getCause()` is non-null and type-matches the IO failure.

- [ ] **Day 10 — Try-with-resources**
  - [ ] **Learn:** [The try-with-resources Statement](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) · [`AutoCloseable` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/AutoCloseable.html)
  - [ ] **Practice:** try-with-resources vs finally close
    1. Read a file with `try (BufferedReader br = …) { … }` — no manual close.
    2. Rewrite once with manual `close()` in `finally` (older style).
    3. Prefer try-with-resources going forward; delete or comment the manual version.
    4. Optional: tiny class implementing `AutoCloseable` that prints `"closed"` in `close()`.
    5. **Done when:** File read works with try-with-resources and you noted why it’s safer.

- [ ] **Day 11 — Harden an old project**
  - [ ] **Learn:** Skim [Exceptions trail](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html)
  - [ ] **Practice:** Refactor an older mini-project
    1. Pick Unit Converter, Expense Tracker, or OOP BankAccount.
    2. Replace silent failures / vague returns with `IllegalArgumentException` (or domain exceptions).
    3. Catch at UI boundary; print clear messages; keep looping.
    4. List 3 places you changed in notes.
    5. **Done when:** Bad input no longer crashes; changes list exists.

- [ ] **Day 12 — User vs detail messages**
  - [ ] **Learn:** Revisit [Catching and Handling](https://docs.oracle.com/javase/tutorial/essential/exceptions/handling.html) (messages & logging mindset)
  - [ ] **Practice:** Separate user message from detail/log
    1. On failure: print user line like `"Could not save. Try again."`
    2. Also print or log detail: exception class + message (simulate log with `System.err`).
    3. Ensure no passwords/tokens appear in either message (add a fake secret field and prove it’s omitted).
    4. **Done when:** User sees short text; detail goes to err/log; secrets absent.

- [ ] **Day 13 — Robust CLI mini-project**
  - [ ] **Learn:** [`Scanner` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html) · [Exceptions trail hub](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html)
  - [ ] **Practice:** CLI that never crashes on bad input
    1. Menu: `1) Parse int  2) Divide  3) Quit`.
    2. Parse int from line; on failure re-prompt (no crash).
    3. Divide two ints; catch divide-by-zero; continue loop.
    4. Quit cleanly.
    5. **Done when:** Spam bad input for 30 seconds — process stays alive.

- [ ] **Day 14 — Mastery gate**
  - [ ] **Learn:** Recap [Exceptions trail](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html)
  - [ ] **Practice:** Checklist + personal guidelines
    1. Tick §12 checklist honestly.
    2. Write one-page “error handling guidelines”: when checked vs unchecked; where to catch; message rules; try-with-resources default.
    3. Note 2 weak spots to revisit.
    4. **Done when:** Checklist + guidelines page exist.

*On a 1-week pace: complete Days 1–7 thoroughly, then do Days 8–10 and one refactor day.*

---

## 5. Hands-on exercises

1. Safe division method: return Optional-like result **or** throw on divide-by-zero (compare styles).
2. Parse integers from a list of strings; collect successes and failures separately.
3. Implement `withdraw` that throws `InsufficientFundsException` (custom).
4. Read a file of numbers; skip bad lines; report count of failures (don’t crash).
5. Translate `NumberFormatException` into a user-friendly CLI message.
6. Demonstrate suppressed exceptions with try-with-resources (two failing closeables)—observe output.
7. Write a method that validates a `Book` and throws a single custom exception with a detail list message.
8. Intentionally catch too broadly (`Exception`) then refactor to narrow catches.
9. Re-throw vs wrap: two versions of a service method; document tradeoffs.
10. Build a retry loop for a flaky operation mock (retry 3 times on a custom `TransientException`).

---

## 6. Mini-projects

### Robust Console Calculator

- Operations via menu
- Invalid input never crashes the app
- Domain errors (divide by zero) as clear messages
- Separate `Calculator` (throws) from `ConsoleApp` (catches/displays)
- **Full steps:** [A — Robust Console Calculator](../mini-projects/phase-01-core-java/04-exception-handling/A-robust-console-calculator.md)

### Library Service Hardening

- Take any prior library/contact project
- Add `NotFoundException`, `ValidationException`
- Service throws; UI catches and continues loop
- No empty `catch` blocks
- **Full steps:** [B — Library Service Hardening](../mini-projects/phase-01-core-java/04-exception-handling/B-library-service-hardening.md)

---

## 7. Industry use cases

- **API boundaries:** Convert infrastructure failures into domain/application errors.
- **Input validation:** Reject bad requests early with precise exceptions or result types.
- **Resource management:** Files, sockets, DB connections closed via try-with-resources.
- **Batch jobs:** Per-record failure isolation so one bad row doesn’t kill the run.
- **Observability:** Stack traces and causes feed logs/monitoring (Phase 5+), but the habit starts here.

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Empty `catch` | At least log/message; never silent swallow |
| Catching `Exception` / `Throwable` everywhere | Catch specific types; handle at the right layer |
| Using exceptions for normal branching | Prefer conditionals / Optional for expected empties |
| Losing the cause when wrapping | Pass `cause` into the new exception |
| Huge `try` blocks | Keep try narrow around the failing call |
| Declaring `throws Exception` | Be precise with checked types |
| Returning `null` on error without documenting | Prefer exceptions or explicit result objects |
| Catching `Error` | Don’t—JVM errors aren’t for app recovery |
| Finally mutating return values confusingly | Keep `finally` for cleanup only |
| Exposing internal details to end users | Friendly message outward; detail inward |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Oracle Tutorials — Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html) | Primary trail |
| [dev.java exceptions material](https://dev.java/learn/) | Supplementary |
| Baeldung exception guides (free) | Extra patterns; verify against Oracle |

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Use this as backup index.

- Exceptions trail: https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html
- Catching & handling: https://docs.oracle.com/javase/tutorial/essential/exceptions/handling.html
- `throw` / `throws`: https://docs.oracle.com/javase/tutorial/essential/exceptions/throwing.html
- Try-with-resources: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
- Unchecked exceptions: https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html
- `Throwable` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Throwable.html
- `Exception` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Exception.html
- `RuntimeException` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/RuntimeException.html
- `AutoCloseable` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/AutoCloseable.html

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Mechanics | try/catch/finally fluency | 10 small demos |
| 2. Hierarchy | Classify checked/unchecked | Drawn tree + examples |
| 3. Signaling | throw/throws correctly | Service validation throws |
| 4. Custom types | Domain exceptions | ≥2 custom types used |
| 5. Resources | try-with-resources | File/Scanner demos |
| 6. Refactor | Harden an old project | No empty catches |
| 7. Gate | Ready for Multithreading | Checklist ≥90% |

---

## 12. Mastery checklist

- [ ] Explains checked vs unchecked exceptions with examples
- [ ] Reads stack traces and finds the failing line quickly
- [ ] Uses `try` / `catch` / `finally` correctly
- [ ] Prefers try-with-resources for `AutoCloseable`
- [ ] Uses `throw` for invalid states/inputs
- [ ] Declares `throws` appropriately for checked exceptions
- [ ] Writes custom exceptions (checked and unchecked) with causes
- [ ] Avoids empty catches and overly broad catches
- [ ] Handles errors at UI/boundary; lets domain signal clearly
- [ ] Refactored at least one prior project for robust error handling
- [ ] Ready to start [05-multithreading.md](05-multithreading.md)
