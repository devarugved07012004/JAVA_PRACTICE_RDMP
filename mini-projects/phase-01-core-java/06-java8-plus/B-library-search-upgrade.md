# Mini-Project B — Library Search Upgrade

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/06-java8-plus.md](../../phase-01-core-java/06-java8-plus.md)  
**Code folder suggestion:** `projects/phase-01/06-java8-plus/library-search-upgrade/` (or upgrade your OOP/collections library in place)  
**Time:** ~8 hours at 1 hr/day  

## Goal

Upgrade an existing library domain: implement search/sort/availability reports with **Streams**, use **`Optional`** for find-by-id, and **`LocalDate`** for due dates and overdue filters.

## Prerequisites

- [ ] Stream analytics CLI helpful
- [ ] Existing Mini Library / phase-project v1 code preferred
- [ ] Optional + java.time from topic Weeks 2–3

## Learn (open before coding)

- [`Optional` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Optional.html)
- [Aggregate Operations](https://docs.oracle.com/javase/tutorial/collections/streams/index.html)
- [`Collectors.groupingBy`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html#groupingBy(java.util.function.Function))
- [`LocalDate` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html)
- [Date-Time trail](https://docs.oracle.com/javase/tutorial/datetime/index.html)
- [Method References](https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html)

## Project layout

```text
library-search-upgrade/
  README.md
  src/
    com/learn/library/
      app/Main.java
      model/Book.java
      model/Member.java
      model/Loan.java
      repository/...
      service/LibraryService.java
      service/ReportService.java
```

## Build steps

### Step 1 — Baseline library compiles

- [ ] Import/copy prior library models + in-memory stores
- [ ] Ensure borrow/return still work with `LocalDate` due dates
- [ ] Compile/run:
  ```bash
  javac -d out src/com/learn/library/**/*.java
  java -cp out com.learn.library.app.Main
  ```
- **Done when:** Baseline happy path works.

### Step 2 — Optional find-by-id

- [ ] `Optional<Book> findByIsbn(String isbn)`
- [ ] `Optional<Member> findById(String id)`
- [ ] Service borrow uses `orElseThrow(() -> new NotFoundException(...))` (or equivalent)
- [ ] Remove null-return find methods
- **Done when:** Missing isbn throws/clears via Optional path—no raw null checks in callers.

### Step 3 — Stream search

- [ ] Search title/author/genre with `stream().filter(b -> ... contains ignoreCase).toList()`
- [ ] Case-insensitive; trim query; empty query → Validation or empty list (document)
- [ ] Menu wired to stream search only
- **Done when:** Partial title match returns correct subset.

### Step 4 — Availability report

- [ ] `availableBooks()` = filter `Book::isAvailable` (or copies > 0)
- [ ] `unavailableBooks()` counterpart
- [ ] Print counts + titles via streams
- **Done when:** After borrow, availability report changes.

### Step 5 — Sort helpers

- [ ] Sort books by title, by author (`Comparator.comparing(Book::getTitle)`)
- [ ] Sort loans by due date
- [ ] Menu options or flags on list commands
- **Done when:** Sorted list order verified on seed data.

### Step 6 — Overdue filter with LocalDate

- [ ] `isOverdue(Loan)` → returnedOn empty && `LocalDate.now().isAfter(dueOn)`
- [ ] `overdueLoans()` stream filter
- [ ] Seed one loan with dueOn yesterday for demo
- **Done when:** Overdue report lists only open past-due loans.

### Step 7 — Group-by genre report

- [ ] `Collectors.groupingBy(Book::getGenre, Collectors.counting())`
- [ ] Print genre → count
- [ ] Optional: groupingBy genre → list of titles
- **Done when:** Counts match seed catalog.

### Step 8 — README upgrade notes

- [ ] Before/after: null finds → Optional; loops → streams
- [ ] Demo script covering search, available, overdue, genre
- [ ] Note what remains for phase-project v2/v3
- **Done when:** Documented upgrade is reproducible.

## Definition of Done

- [ ] Optional used for find-by-id style APIs
- [ ] Search/sort/availability via Streams
- [ ] Overdue uses LocalDate
- [ ] Genre grouping report works
- [ ] Happy borrow/return still works

## Demo script

1. Seed catalog + one overdue loan
2. Search author “Ada”
3. List available only
4. Overdue report shows seeded loan
5. Genre grouping
6. Borrow then re-check availability
7. Quit

## Common mistakes

| Mistake | Fix |
|--------|-----|
| `optional.get()` without check | `orElseThrow` / `ifPresent` |
| Date strings compared lexicographically | `LocalDate` API |
| Side-effecting filters | Pure predicates |
| Keeping null-based find alongside Optional | Delete null API |
| Sorting with mutable stateful comparator | Use `Comparator.comparing` |

## After this

Next: [C — Parallel Bulk Validator](C-parallel-bulk-validator.md) · Integrate into [Library Manager](../phase-project/library-manager.md)
