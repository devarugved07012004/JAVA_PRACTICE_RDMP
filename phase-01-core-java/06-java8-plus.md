# 06 — Java 8+ Features

**Duration:** ~2–3 weeks (14–21 days) · **Cadence:** 1 hour/day  
**Focus:** Lambdas, functional interfaces, Streams, Optional, method references, Date/Time API, parallel streams

---

## 1. Why this topic is important

Java 8 reshaped everyday Java style: collections processing with Streams, concise callbacks with lambdas, and a sane date/time model (`java.time`). Modern codebases, Spring APIs, and technical docs assume this fluency. Parallel streams and `Optional` also teach you to think in **pipelines and explicit absence** instead of nested loops and null-checking sprawl.

This topic upgrades your Phase 1 library project from imperative loops to clear, testable data transformations—preparation for Phase 2 data mapping and later service-layer logic.

---

## 2. Prerequisites

- Completed [05-multithreading.md](05-multithreading.md) mastery checklist (≥90%) recommended; at minimum finish Collections + OOP first
- Comfortable with interfaces, generics, and collections
- Basic concurrency awareness before **parallel** streams (so you don’t treat them as magic)

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Lambda expressions | `(args) -> body` as implementation of a SAM interface |
| Functional interfaces | Single abstract method; `@FunctionalInterface` |
| Common FI types | `Predicate`, `Function`, `Consumer`, `Supplier`, `Comparator` |
| Method references | `Class::method`, `obj::method`, `Class::new` |
| Stream intro | `stream()` → intermediate ops → terminal op |
| `Optional` intro | Replace nullable returns for “may be absent” |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Intermediate ops | `filter`, `map`, `flatMap`, `distinct`, `sorted`, `peek` |
| Terminal ops | `collect`, `forEach`, `reduce`, `count`, `anyMatch`, `findFirst` |
| Collectors | `toList`, `toSet`, `toMap`, `groupingBy`, `joining` |
| Lazy evaluation | Nothing runs until a terminal operation |
| `Optional` patterns | `map`, `flatMap`, `orElse`, `orElseGet`, `orElseThrow`; avoid `get` abuse |
| Date/Time API | `LocalDate`, `LocalDateTime`, `Instant`, `Duration`, `Period`, `ZoneId` |
| Formatting/parsing | `DateTimeFormatter` |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| `flatMap` mastery | Nested structures; `Optional` flattening |
| Custom collectors awareness | Know they exist |
| Parallel streams | `parallelStream`; when speedup happens; when it hurts |
| Stateless vs stateful ops | Implications for parallel correctness/performance |
| Primitive streams | `IntStream`, etc., to avoid boxing when needed |
| Stream pitfalls | Reuse of streams; side effects in `map`; order sensitivity |
| Modern Java extras (awareness) | `List.of`, `var`, switch expressions, records (if on JDK 16+)—optional exploration |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Lambdas & Streams core (Days 1–7)

- [ ] **Day 1 — Lambdas & functional interfaces**
  - [ ] **Learn:** [Lambda Expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html) · [`@FunctionalInterface` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/FunctionalInterface.html)
  - [ ] **Practice:** Lambdas + @FunctionalInterface demo
    1. Sort `List<String>` with anonymous `Comparator`, then rewrite as `(a, b) -> a.compareToIgnoreCase(b)`.
    2. Replace anonymous `Runnable` with `() -> System.out.println("hi")` and run on a `Thread`.
    3. Create `@FunctionalInterface interface StringOp { String apply(String s); }` and use a lambda.
    4. Try adding a second abstract method — confirm compile error.
    5. **Done when:** Both rewrites run and the FI demo shows the single-abstract-method rule.

- [ ] **Day 2 — Common functional interfaces**
  - [ ] **Learn:** [`java.util.function` package](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/package-summary.html) · [`Predicate`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/Predicate.html) · [`Function`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/Function.html)
  - [ ] **Practice:** Predicate / Function / Consumer / Supplier utilities
    1. `Predicate<String> nonBlank = s -> s != null && !s.isBlank();` test 3 inputs.
    2. `Function<String, Integer> len = String::length;` (or lambda) map a list of words.
    3. `Consumer<String> printer = System.out::println;` forEach.
    4. `Supplier<LocalDate> today = LocalDate::now;` print once.
    5. **Done when:** All four interfaces used from `main` with printed results.

- [ ] **Day 3 — Method references**
  - [ ] **Learn:** [Method References](https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html)
  - [ ] **Practice:** Convert single-call lambdas to method refs
    1. Start with lambdas: `list.forEach(s -> System.out.println(s))`, `list.stream().map(s -> s.toUpperCase())`.
    2. Rewrite to `System.out::println`, `String::toUpperCase`.
    3. Demo `Person::new` as a `Supplier` or `Function` for construction.
    4. Keep a side-by-side comment: lambda vs method ref.
    5. **Done when:** At least three method refs compile and behave identically to lambdas.

- [ ] **Day 4 — Stream pipeline basics**
  - [ ] **Learn:** [Aggregate Operations](https://docs.oracle.com/javase/tutorial/collections/streams/index.html) · [`Stream` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html)
  - [ ] **Practice:** filter + map + collect
    1. Create `List<String> words` with mixed lengths/cases.
    2. Pipeline: `filter(w -> w.length() > 3).map(String::toLowerCase).collect(Collectors.toList())`.
    3. Print input and output.
    4. Confirm original list unchanged.
    5. **Done when:** Output list matches filter+map rules and source list is intact.

- [ ] **Day 5 — Intermediate ops**
  - [ ] **Learn:** [Intermediate and Terminal Operations](https://docs.oracle.com/javase/tutorial/collections/streams/index.html) · [`Stream` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html) (`distinct`, `sorted`, `limit`, `skip`)
  - [ ] **Practice:** distinct / sorted / limit / skip
    1. Stream of ints with duplicates: `distinct().sorted().toList()` (or collect).
    2. Same stream source: `skip(2).limit(3)` — print window.
    3. Note: encounter order for `distinct` on ordered streams is stable for first occurrences.
    4. **Done when:** Distinct sorted list and skip/limit window match hand expectations.

- [ ] **Day 6 — Matching & finding**
  - [ ] **Learn:** [`Stream` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html) (`anyMatch`, `allMatch`, `findFirst`, `findAny`) · [Reduction (find/match context)](https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html)
  - [ ] **Practice:** anyMatch / allMatch / findFirst vs findAny
    1. On a list of ints: `anyMatch(n -> n < 0)`, `allMatch(n -> n > 0)`, `noneMatch`.
    2. `findFirst` on ordered list; print `Optional`.
    3. On a parallel stream, call `findAny` once; note it may not be first.
    4. Notes: prefer `findFirst` when order matters.
    5. **Done when:** Match results correct; findFirst vs findAny difference noted.

- [ ] **Day 7 — reduce vs collect**
  - [ ] **Learn:** [Reduction](https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html) · [`Collectors` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html)
  - [ ] **Practice:** Sum/average two ways
    1. Sum with `stream.reduce(0, Integer::sum)` (on `Stream<Integer>` or mapToInt).
    2. Sum/average with `Collectors.summingInt` / `averagingInt` (or `IntSummaryStatistics`).
    3. Print both; confirm they agree.
    4. **Done when:** reduce and collect totals match for the same data.

### Week 2 — Collectors, Optional, Date/Time (Days 8–14)

- [ ] **Day 8 — groupingBy & friends**
  - [ ] **Learn:** [`Collectors` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html) · [Collecting with Collectors (tutorial)](https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html)
  - [ ] **Practice:** groupingBy, partitioningBy, joining
    1. `Person` list with name/age; `groupingBy(p -> p.getAge() / 10 * 10)` (decade bands).
    2. `partitioningBy(p -> p.getAge() >= 18)` → adults vs minors.
    3. `joining(", ")` on names.
    4. Print all three results.
    5. **Done when:** Groups, partitions, and joined string match sample data.

- [ ] **Day 9 — toMap & merges**
  - [ ] **Learn:** [`Collectors.toMap`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html#toMap(java.util.function.Function,java.util.function.Function)) · [`Map.merge` idea via Map API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Map.html)
  - [ ] **Practice:** toMap with collision merge
    1. Build stream that produces duplicate keys on purpose.
    2. First `toMap` without merge — observe `IllegalStateException`.
    3. Fix: `toMap(keyFn, valFn, (a, b) -> a + "|" + b)` (or sum).
    4. **Done when:** Collision throws once, then merge path succeeds.

- [ ] **Day 10 — flatMap**
  - [ ] **Learn:** [`Stream.flatMap`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html#flatMap(java.util.function.Function)) · [Aggregate Operations](https://docs.oracle.com/javase/tutorial/collections/streams/index.html)
  - [ ] **Practice:** Flatten list-of-lists
    1. `List<List<String>> nested = List.of(List.of("a","b"), List.of("c"));`
    2. `nested.stream().flatMap(List::stream).collect(toList())` → `[a,b,c]`.
    3. Optional: `orders.stream().flatMap(o -> o.getLines().stream())`.
    4. **Done when:** Flat list equals concatenation of inners.

- [ ] **Day 11 — Optional**
  - [ ] **Learn:** [`Optional` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Optional.html) · [dev.java — Learn (modern Java)](https://dev.java/learn/)
  - [ ] **Practice:** Service returns Optional; no naked get()
    1. `Optional<User> findById(String id)` returning `Optional.of` / `empty`.
    2. Caller uses `map`, `flatMap`, `orElse`, `orElseThrow` — never bare `get()`.
    3. Grep/self-check: no `.get()` without isPresent guard (prefer orElseThrow).
    4. **Done when:** Missing id path is safe; present path prints user.

- [ ] **Day 12 — java.time basics**
  - [ ] **Learn:** [Date-Time trail](https://docs.oracle.com/javase/tutorial/datetime/index.html) · [`LocalDate` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html)
  - [ ] **Practice:** LocalDate / LocalDateTime create & compare
    1. `LocalDate.of(2026, 10, 22)`, `LocalDate.now()`, `plusDays(7)`, `minusMonths(1)`.
    2. Compare with `isBefore` / `isAfter` / `ChronoUnit.DAYS.between`.
    3. `LocalDateTime.now()` plus 2 hours; print.
    4. **Done when:** Plus/minus and comparison prints match calendar expectations.

- [ ] **Day 13 — Zones, Instant, formatting**
  - [ ] **Learn:** [Time Zone and Offset](https://docs.oracle.com/javase/tutorial/datetime/iso/timezones.html) · [`Instant` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/Instant.html) · [`DateTimeFormatter` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/format/DateTimeFormatter.html)
  - [ ] **Practice:** Instant, zones, format/parse
    1. `Instant.now()`; convert to `ZonedDateTime` in `ZoneId.of("Asia/Kolkata")` and `"UTC"`.
    2. Format with `DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")`.
    3. Parse a date string back to `LocalDate`.
    4. **Done when:** Two zones print different offsets/times and parse round-trips.

- [ ] **Day 14 — Refactor dates in a project**
  - [ ] **Learn:** Re-skim [Date-Time trail](https://docs.oracle.com/javase/tutorial/datetime/index.html)
  - [ ] **Practice:** Replace date strings with LocalDate
    1. Pick a prior project that stores dates as `String` (or add dueDate string temporarily).
    2. Change field type to `LocalDate`; update constructors/parsers.
    3. Format for display with a formatter; keep parsing in one place.
    4. **Done when:** Project compiles; date math uses `LocalDate` APIs not string compare.

### Week 3 — Parallel streams & project upgrade (Days 15–21)

- [ ] **Day 15 — Parallel stream micro-demo**
  - [ ] **Learn:** [Parallelism](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html) · [`BaseStream.parallel`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/BaseStream.html#parallel())
  - [ ] **Practice:** Time sequential vs parallel map
    1. Create large `List<Integer>` (e.g. 5–10 million) or `IntStream.range`.
    2. CPU-ish map (e.g. compute something non-trivial per element).
    3. Time sequential vs `.parallel()` with `System.nanoTime`.
    4. Record both times in notes.
    5. **Done when:** Both complete and timings are written (parallel may or may not win).

- [ ] **Day 16 — When parallel is wrong**
  - [ ] **Learn:** [Parallelism (side effects / ordering)](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html) · [Interference](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html#interference)
  - [ ] **Practice:** Document anti-patterns
    1. Note 1: tiny lists — parallel overhead dominates.
    2. Note 2: blocking I/O inside parallel stream — bad.
    3. Note 3: mutating shared `ArrayList` from parallel forEach — race.
    4. Optional micro-demo of shared mutable add (wrong totals).
    5. **Done when:** Three anti-pattern notes exist (demo optional but ideal).

- [ ] **Day 17 — Primitive streams**
  - [ ] **Learn:** [`IntStream` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/IntStream.html) · [Primitive Stream Specializations](https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html)
  - [ ] **Practice:** IntStream / LongStream without boxing
    1. `IntStream.rangeClosed(1, 1_000_000).sum()`.
    2. Contrast mentally with `Stream<Integer>` boxing cost (note it).
    3. `mapToObj` only when you need objects; prefer `map`/`filter` on IntStream for ints.
    4. **Done when:** Sum runs via IntStream and notes mention boxing avoidance.

- [ ] **Day 18 — Project search upgrade**
  - [ ] **Learn:** Recap [Aggregate Operations](https://docs.oracle.com/javase/tutorial/collections/streams/index.html)
  - [ ] **Practice:** Search/filter/sort via Streams
    1. Open Library or Inventory project.
    2. Replace manual loops for search with `stream().filter(...).collect(...)`.
    3. Add sort via `sorted(Comparator.…)`.
    4. Menu still works for search by title/author (or name/sku).
    5. **Done when:** Stream-based search returns same results as old loop for sample queries.

- [ ] **Day 19 — Report grouping**
  - [ ] **Learn:** [`Collectors.groupingBy`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html#groupingBy(java.util.function.Function))
  - [ ] **Practice:** groupingBy reports
    1. Report: books by author **or** expenses by category via `groupingBy`.
    2. Print each group key and size (or items).
    3. Optional: downstream `counting()` or `summingDouble`.
    4. **Done when:** Report matches manual group counts for seed data.

- [ ] **Day 20 — Optional + Stream cleanups**
  - [ ] **Learn:** [`Optional.stream`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Optional.html#stream()) · [`Stream` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html)
  - [ ] **Practice:** Optional.stream flatMap cleanup
    1. Have `List<Optional<T>>` or stream of ids mapped to `Optional`.
    2. Use `.flatMap(Optional::stream)` (Java 9+) to drop empties.
    3. Collect present values; print.
    4. **Done when:** Empties gone without manual isPresent loops.

- [ ] **Day 21 — Mastery gate**
  - [ ] **Learn:** Recap [Lambda Expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html) · [Streams](https://docs.oracle.com/javase/tutorial/collections/streams/index.html) · [Date-Time](https://docs.oracle.com/javase/tutorial/datetime/index.html)
  - [ ] **Practice:** Checklist + streams-vs-loops style page
    1. Tick §12 checklist honestly.
    2. Write a style page: when streams (transform/filter/group) vs loops (complex mutable algorithms, early index tricks).
    3. List 3 personal rules (e.g. no parallel by default; no side effects in map).
    4. **Done when:** Checklist + style page exist.

*On a 2-week pace: merge Days 15–17 into one caution day; prioritize Days 18–20 upgrades.*

---

## 5. Hands-on exercises

1. Filter even numbers; square them; collect.
2. Sort people by last name then first name (Comparator composition).
3. Word count with Streams (`Collectors.groupingBy` + `counting`).
4. Join names with commas via `Collectors.joining`.
5. Flatten `List<List<String>>` with `flatMap`.
6. Find the longest string; return `Optional`.
7. Partition numbers into even/odd (`partitioningBy`).
8. Build a frequency `Map<Character, Long>` from a string.
9. Parse a list of date strings; skip invalid with a clean pattern.
10. Compute total loan days between borrow/return using `Period`/`ChronoUnit`.
11. Parallel sum of 10_000_000 ints; compare to sequential (rough timing).
12. Rewrite a nested for-loop search as a stream pipeline; keep readability.

---

## 6. Mini-projects

### A. Stream-Powered Analytics CLI

- Load in-memory transactions (amount, category, date)
- Filters by date range; group totals; top categories
- All queries expressed as stream pipelines
- **Full steps:** [A — Stream Analytics CLI](../mini-projects/phase-01-core-java/06-java8-plus/A-stream-analytics-cli.md)

### B. Library Search Upgrade

- Take OOP library models
- Implement search/sort/availability reports with Streams
- Use `Optional` for find-by-id
- Use `LocalDate` for due dates and overdue filters
- **Full steps:** [B — Library Search Upgrade](../mini-projects/phase-01-core-java/06-java8-plus/B-library-search-upgrade.md)

### C. Parallel Bulk Validator

- Validate many fake records CPU-style (checksum/hash loops)
- Compare sequential vs parallel throughput
- Document conclusions
- **Full steps:** [C — Parallel Bulk Validator](../mini-projects/phase-01-core-java/06-java8-plus/C-parallel-bulk-validator.md)

---

## 7. Industry use cases

- **Service-layer mapping:** Entity → DTO transforms with `map`.
- **Reporting/aggregations:** `groupingBy` for dashboards and exports.
- **Cleaner APIs:** `Optional` for absent values in find methods.
- **Scheduling & billing:** `java.time` for due dates, trial periods, SLAs.
- **Bulk transforms:** Parallel streams for pure CPU-bound in-memory work (carefully).
- **Framework fit:** Spring and modern libraries expect lambda-friendly APIs.

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Side effects inside `map`/`filter` | Keep ops pure; use `forEach` sparingly for effects |
| Reusing a Stream after a terminal op | Create a new stream |
| `optional.get()` without check | `orElseThrow` / `ifPresent` / `orElseGet` |
| `Optional` as fields/parameters everywhere | Best as return type for absence |
| Parallel streams on small lists | Stick to sequential by default |
| Parallel + shared mutable accumulation | Use proper collectors/reducers |
| Ignoring timezone when you need instants | Use `Instant`/`ZonedDateTime` appropriately |
| Legacy `Date`/`Calendar` for new code | Prefer `java.time` |
| Ultra-long stream pipelines | Break into named methods for readability |
| Using streams where a simple loop is clearer | Choose clarity |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Oracle — Lambda expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html) | Lambdas |
| [Oracle — Aggregate operations / Streams](https://docs.oracle.com/javase/tutorial/collections/streams/index.html) | Streams |
| [Oracle — Date-Time trail](https://docs.oracle.com/javase/tutorial/datetime/index.html) | `java.time` |
| [dev.java — Modern Java](https://dev.java/learn/) | Updated learning path |
| [Stream API Javadoc](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/package-summary.html) | Operation reference |

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Use this as backup index.

- Lambda expressions: https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
- Method references: https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
- Functional interfaces tutorial: https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#approach5
- Streams aggregate ops: https://docs.oracle.com/javase/tutorial/collections/streams/index.html
- `Stream` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html
- `Collectors` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Collectors.html
- `Optional` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Optional.html
- `java.util.function` package: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/package-summary.html
- Date-Time trail: https://docs.oracle.com/javase/tutorial/datetime/index.html
- `LocalDate` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html
- `DateTimeFormatter` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/format/DateTimeFormatter.html
- Parallelism in streams: https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Lambdas | Replace anonymous classes | 10 rewrites |
| 2. Stream CRUD-ish | filter/map/collect fluency | 15 pipelines |
| 3. Collectors | grouping/toMap/joining | 5 report queries |
| 4. Optional | Safe absence handling | No reckless `get` |
| 5. Date/Time | Model real dates | Overdue logic works |
| 6. Parallel caution | Know when *not* to | Written guidelines |
| 7. Gate | Phase project ready | Checklist ≥90% |

---

## 12. Mastery checklist

- [ ] Writes clear lambdas for common functional interfaces
- [ ] Uses method references where they improve readability
- [ ] Builds stream pipelines with filter/map/flatMap/sorted
- [ ] Uses terminal operations appropriately (`collect`, match, find, reduce)
- [ ] Applies `Collectors.groupingBy`, `toMap`, `joining`
- [ ] Uses `Optional` without abusing `get()`
- [ ] Handles dates with `java.time` (create, parse, format, compare)
- [ ] Explains lazy evaluation of streams
- [ ] Explains when parallel streams help vs hurt
- [ ] Upgraded at least one prior project to Streams + `Optional` + `LocalDate`
- [ ] Ready for [phase-project.md](phase-project.md)
