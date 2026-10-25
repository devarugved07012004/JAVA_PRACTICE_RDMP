# Mini-Project A — Stream-Powered Analytics CLI

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/06-java8-plus.md](../../phase-01-core-java/06-java8-plus.md)  
**Code folder suggestion:** `projects/phase-01/06-java8-plus/stream-analytics/`  
**Time:** ~8 hours at 1 hr/day  

## Goal

Load in-memory transactions (amount, category, date). Support filters by date range, group totals, and top categories—**all queries as stream pipelines**.

## Prerequisites

- [ ] Lambdas, Stream API, Collectors basics
- [ ] `java.time.LocalDate` comfort

## Learn (open before coding)

- [Aggregate Operations](https://docs.oracle.com/javase/tutorial/collections/streams/index.html)
- [Reduction](https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html)
- [`Collectors` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html)
- [`Stream` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html)
- [`LocalDate` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html)
- [Lambda Expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)

## Project layout

```text
stream-analytics/
  README.md
  src/
    com/learn/analytics/
      app/Main.java
      model/Transaction.java
      data/SeedTransactions.java
      service/AnalyticsService.java
```

## Build steps

### Step 1 — Transaction model & seed

- [ ] `Transaction`: id, amount, category, `LocalDate date`
- [ ] Seed ≥30 transactions across ≥5 categories and ≥2 months
- [ ] Compile/run stub:
  ```bash
  javac -d out src/com/learn/analytics/**/*.java
  java -cp out com.learn.analytics.app.Main
  ```
- **Done when:** Seed list size prints correctly.

### Step 2 — Filter by date range (streams)

- [ ] `List<Transaction> inRange(LocalDate from, LocalDate to)` using `stream().filter(...).toList()` / collect
- [ ] Inclusive bounds; validate from ≤ to
- [ ] Menu prompts for dates (`ISO_LOCAL_DATE`)
- **Done when:** Known range returns expected count.

### Step 3 — Total amount in range

- [ ] `double total(Stream or list)` via `mapToDouble(Transaction::getAmount).sum()`
- [ ] No manual for-loop for the happy path
- **Done when:** Total matches hand sum for a tiny fixture.

### Step 4 — Group totals by category

- [ ] `Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(...))`
- [ ] Print category → total sorted by total descending
- **Done when:** Top category matches seed expectation.

### Step 5 — Top-N categories

- [ ] From grouping map, stream entrySet, sort, limit N
- [ ] Menu: Top N (prompt N)
- **Done when:** N=3 prints three lines in order.

### Step 6 — Extra stream queries

- [ ] Average amount overall / in range (`average()` OptionalDouble)
- [ ] Count transactions per category (`groupingBy` + `counting`)
- [ ] Filter amount > threshold then list
- **Done when:** ≥2 extra queries implemented as pipelines.

### Step 7 — Keep UI thin

- [ ] `AnalyticsService` holds all stream logic
- [ ] `Main` only parses input and prints maps/lists
- [ ] No business aggregation loops in Main
- **Done when:** Self-check: stream ops live in service.

### Step 8 — README & anti-patterns note

- [ ] Document queries; warn against side-effects inside `map`/`filter`
- [ ] Note when parallel streams are **not** used (and why)
- [ ] Demo script with fixed dates
- **Done when:** Fresh run demos range + group + top-N.

## Definition of Done

- [ ] Date-range filter via streams
- [ ] Group totals + top categories via Collectors
- [ ] ≥2 additional stream queries
- [ ] Service/UI separation
- [ ] README verified

## Demo script

1. Load seed
2. Filter 2024-01-01 → 2024-01-31; show count + total
3. Group by category
4. Top 3 categories
5. Average in range
6. Quit

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Mutating external lists inside `forEach` as “query” | Prefer collect to new structures |
| Parsing dates as strings for compare | Use `LocalDate` compare methods |
| Parallel stream on tiny data | Stay sequential here |
| Giant lambda in Main | Move to service methods |
| `get()` on empty OptionalDouble | Use `orElse(0)` / isPresent |

## After this

Next: [B — Library Search Upgrade](B-library-search-upgrade.md) · Topic: [06-java8-plus.md](../../phase-01-core-java/06-java8-plus.md)
