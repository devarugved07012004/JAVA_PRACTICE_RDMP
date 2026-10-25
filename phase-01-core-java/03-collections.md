# 03 — Collections Framework

**Duration:** ~3–4 weeks (21–28 days) · **Cadence:** 1 hour/day  
**Focus:** List/Set/Map/Queue, implementations, iterators, ordering, generics, complexity, practical choosing

---

## 1. Why this topic is important

Almost every Java application stores and transforms groups of data: users, orders, cache entries, configuration keys, task queues. The Collections Framework is the standard toolkit for that work. Choosing `ArrayList` vs `LinkedList`, `HashMap` vs `TreeMap`, or `HashSet` vs `TreeSet` affects correctness (ordering, uniqueness, nulls) and performance (time complexity).

Collections also introduce **generics**, which make APIs type-safe and are required fluency for Streams (Java 8+) and Spring data layers later.

---

## 2. Prerequisites

- Completed [02-oops.md](02-oops.md) mastery checklist (≥90%)
- Comfortable with classes, interfaces, and `equals`/`hashCode` basics
- Arrays and methods from fundamentals

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Collection hierarchy | `Collection`, `List`, `Set`, `Queue`, `Map` (Map is separate) |
| `ArrayList` | Dynamic array; random access; resizes |
| Basic List ops | add, get, set, remove, size, contains |
| `HashSet` | Uniqueness via hash/`equals`; no duplicates |
| `HashMap` | Key→value; uniqueness of keys |
| Iteration | enhanced-for; basics of `Iterator` |
| Generics intro | `List<String>`, diamond `<>` |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| `LinkedList` as List/Deque | Node structure; when it helps (usually rarely vs ArrayList) |
| `TreeSet` / `TreeMap` | Sorted order; `Comparable` / `Comparator` |
| `LinkedHashMap` / `LinkedHashSet` | Insertion-order predictability |
| Queue / Deque | FIFO; `offer`/`poll`/`peek` |
| `PriorityQueue` | Heap ordering; not fully sorted iteration |
| `Stack` vs `Deque` | Prefer `ArrayDeque` for stack/queue behaviors |
| `Comparable` vs `Comparator` | Natural order vs external strategies |
| Fail-fast iterators | Concurrent modification awareness (single-threaded mistakes) |
| Complexity | Big-O for common ops on common structures |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| Internal working sketches | ArrayList growth; HashMap buckets/trees (conceptual) |
| Load factor & capacity | HashMap sizing intuition |
| Unmodifiable / defensive copies | `List.copyOf`, `Collections.unmodifiable*` |
| Wildcard generics | `? extends` / `? super` (PECS) at a practical level |
| Choosing structures | Decision table by access pattern |
| Views | `keySet`, `values`, `entrySet` and their coupling to the map |
| Algorithms | `Collections.sort`, binary search, shuffle (know they exist) |

### Time complexity cheat-sheet (average / typical)

| Structure | Access by index | Search | Insert end | Insert middle | Notes |
|-----------|-----------------|--------|------------|---------------|-------|
| `ArrayList` | O(1) | O(n) | Amortized O(1) | O(n) | Default List choice |
| `LinkedList` | O(n) | O(n) | O(1) at ends (Deque) | O(n) find + O(1) link | Rarely beat ArrayList |
| `HashSet`/`HashMap` | n/a | O(1) avg | O(1) avg | n/a | Needs good `hashCode` |
| `TreeSet`/`TreeMap` | n/a | O(log n) | O(log n) | n/a | Sorted |
| `PriorityQueue` | n/a | O(n) contains | O(log n) offer | n/a | Peek min/max O(1) |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Lists & generics (Days 1–7)

- [ ] **Day 1 — Hierarchy & ArrayList**
  - [ ] **Learn:** [Introduction to Collections](https://docs.oracle.com/javase/tutorial/collections/intro/index.html) · [List Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/list.html) · [`ArrayList` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html)
  - [ ] **Practice:** Hierarchy sketch + ArrayList CRUD
    1. In notes, sketch: `Collection` → `List`/`Set`/`Queue`; `Map` separate; note `ArrayList` under `List`.
    2. Create `ArrayListLab.java` with `List<String> names = new ArrayList<>();`
    3. `add` three names; `get(0)`; `set(1, …)`; `remove(0)`; print `size()` after each change.
    4. Loop with enhanced-for and print remaining names.
    5. **Done when:** Notes sketch exists and CRUD sequence prints expected sizes.

- [ ] **Day 2 — Generics basics**
  - [ ] **Learn:** [Generics lesson](https://docs.oracle.com/javase/tutorial/java/generics/index.html) · [Generic Types](https://docs.oracle.com/javase/tutorial/java/generics/types.html) · [Type Inference / Diamond](https://docs.oracle.com/javase/tutorial/java/generics/genTypeInference.html)
  - [ ] **Practice:** Parameterized lists only (no raw types)
    1. Write `List<Integer> nums = new ArrayList<>();` (diamond).
    2. Add ints; try `nums.add("oops")` — confirm compile error; remove it.
    3. Once, declare a raw `List raw = new ArrayList(); raw.add("x");` then assign/get into typed list — note the warning/`ClassCastException` risk.
    4. Fix everything back to fully parameterized code.
    5. **Done when:** Only typed lists remain and you noted why raw types are banned.

- [ ] **Day 3 — List algorithms**
  - [ ] **Learn:** [Algorithms](https://docs.oracle.com/javase/tutorial/collections/algorithms/index.html) · [`List` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/List.html)
  - [ ] **Practice:** Reverse, frequency, safe remove
    1. Create `List<Integer>` with duplicates; reverse with a loop (or `Collections.reverse`) and print.
    2. Count frequency of a target value with a loop.
    3. Remove all even numbers using an `Iterator` (or index loop carefully).
    4. Redo even-removal once with `list.removeIf(n -> n % 2 == 0)`.
    5. **Done when:** Reverse + count correct; both removal styles leave only odds.

- [ ] **Day 4 — LinkedList / Deque**
  - [ ] **Learn:** [List Implementations](https://docs.oracle.com/javase/tutorial/collections/implementations/list.html) · [`LinkedList` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/LinkedList.html) · [`Deque` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Deque.html)
  - [ ] **Practice:** LinkedList as List and Deque
    1. Use `LinkedList<String>` as `List`: add/get/remove by index.
    2. Use same type as `Deque`: `addFirst`, `addLast`, `pollFirst`, `pollLast`.
    3. Print after each deque op so order is obvious.
    4. Optional: time 10_000 mid-list inserts vs `ArrayList` (rough `System.nanoTime`).
    5. **Done when:** Deque ops demonstrate front/back behavior clearly.

- [ ] **Day 5 — Choosing ArrayList**
  - [ ] **Learn:** [Implementations](https://docs.oracle.com/javase/tutorial/collections/implementations/index.html) · Re-skim [List](https://docs.oracle.com/javase/tutorial/collections/interfaces/list.html)
  - [ ] **Practice:** Decision notes for ArrayList
    1. Write 3 scenarios (e.g. random access report, queue-like work, frequent head inserts).
    2. For each, pick `ArrayList` / `LinkedList` / `ArrayDeque` and one-sentence why.
    3. Default rule in notes: “For `List`, start with `ArrayList` unless …”
    4. **Done when:** Three scenarios + default rule are written.

- [ ] **Day 6 — Iterator & safe removal**
  - [ ] **Learn:** [Iterators](https://docs.oracle.com/javase/tutorial/collections/interfaces/collection.html) · [`Iterator` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Iterator.html)
  - [ ] **Practice:** Iterator remove vs ConcurrentModificationException
    1. Build a list of names; use `Iterator` + `it.remove()` to drop names starting with `"A"`.
    2. Copy the list; in enhanced-for call `list.remove(...)` — catch `ConcurrentModificationException`.
    3. Write 2 sentences: when enhanced-for remove fails; when Iterator is safe.
    4. **Done when:** Safe path works; CME demo is observed and noted.

- [ ] **Day 7 — Unique words kata**
  - [ ] **Learn:** Skim [Collections trail hub](https://docs.oracle.com/javase/tutorial/collections/index.html)
  - [ ] **Practice:** Order-preserving unique words
    1. Read a sentence string; `split("\\s+")` into words.
    2. Build unique list preserving first-seen order (loop + `contains`, or `LinkedHashSet` then `new ArrayList<>(…)`).
    3. Print original word count vs unique count.
    4. Test with a sentence that has repeats.
    5. **Done when:** Order of first appearances is preserved and duplicates are gone.

### Week 2 — Sets & Maps (Days 8–14)

- [ ] **Day 8 — HashSet uniqueness**
  - [ ] **Learn:** [Set Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/set.html) · [`HashSet` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashSet.html)
  - [ ] **Practice:** HashSet uniqueness and broken equals/hashCode
    1. Add duplicate strings to `HashSet`; print `size()` (expect unique count).
    2. Create class `Item` with `id` but **only** `equals` (no `hashCode`) — add equal items; observe duplicates possible.
    3. Fix with matching `hashCode`; confirm set size collapses.
    4. **Done when:** Broken case shows duplicates; fixed case size is 1 for equals items.

- [ ] **Day 9 — TreeSet & Comparable**
  - [ ] **Learn:** [SortedSet](https://docs.oracle.com/javase/tutorial/collections/interfaces/sorted-set.html) · [`Comparable` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Comparable.html) · [`TreeSet` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/TreeSet.html)
  - [ ] **Practice:** Natural order with Comparable
    1. Create `Student` with `name`, `score`; implement `Comparable<Student>` by score then name.
    2. Add several students to `TreeSet<Student>`; print in order.
    3. Try adding a student without Comparable to a raw/wrong TreeSet path — note the failure mode.
    4. **Done when:** Printed order matches your compare rules.

- [ ] **Day 10 — Comparator sorting**
  - [ ] **Learn:** [Object Ordering](https://docs.oracle.com/javase/tutorial/collections/interfaces/order.html) · [`Comparator` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Comparator.html)
  - [ ] **Practice:** Multi-field Comparator
    1. Create `Employee` with `name`, `salary` (no need for Comparable).
    2. Sort `List<Employee>` with `Comparator.comparingDouble(Employee::getSalary).thenComparing(Employee::getName)`.
    3. Also sort with a reverse-salary comparator; print both.
    4. **Done when:** Both sort orders match expectations for your sample data.

- [ ] **Day 11 — HashMap basics**
  - [ ] **Learn:** [Map Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html) · [`HashMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashMap.html)
  - [ ] **Practice:** put / get / entrySet
    1. `Map<String, Integer> ages = new HashMap<>();` put 4 entries.
    2. `get`, `containsKey`, `getOrDefault` for missing key.
    3. Iterate `ages.entrySet()` and print `key=value`.
    4. **Done when:** Missing key path is safe and entrySet loop prints all pairs.

- [ ] **Day 12 — Frequency & group-by**
  - [ ] **Learn:** Revisit [Map Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html) (compute patterns in Javadoc too)
  - [ ] **Practice:** Frequency map and group-by lists
    1. Word-frequency: for each word, `map.merge(word, 1, Integer::sum)` (or get/put).
    2. Group students by grade band into `Map<String, List<Student>>`.
    3. Print both structures.
    4. **Done when:** Frequencies and groups match manual counts for your sample.

- [ ] **Day 13 — TreeMap navigation**
  - [ ] **Learn:** [SortedMap](https://docs.oracle.com/javase/tutorial/collections/interfaces/sorted-map.html) · [`TreeMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/TreeMap.html)
  - [ ] **Practice:** Sorted key navigation
    1. Put several string→value pairs in `TreeMap`.
    2. Print `firstKey()`, `lastKey()`, `higherKey(...)`, `lowerKey(...)`.
    3. Print a `subMap` (or `headMap`/`tailMap`) range.
    4. **Done when:** Navigation results match sorted key order.

- [ ] **Day 14 — LinkedHashMap & review**
  - [ ] **Learn:** [`LinkedHashMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/LinkedHashMap.html) · [Map Implementations](https://docs.oracle.com/javase/tutorial/collections/implementations/map.html)
  - [ ] **Practice:** Insertion-order report + week review
    1. Build a report map with `LinkedHashMap`; put keys in a deliberate order; print — confirm order.
    2. Contrast with `HashMap` print order (may differ).
    3. Re-solve: unique words (Day 7) and frequency (Day 12) once each without peeking.
    4. **Done when:** LinkedHashMap order is stable insertion order; review problems pass.

### Week 3 — Queues, stacks, deeper maps (Days 15–21)

- [ ] **Day 15 — Queue & ArrayDeque**
  - [ ] **Learn:** [Queue Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/queue.html) · [`ArrayDeque` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayDeque.html)
  - [ ] **Practice:** Queue offer / poll / peek
    1. `Queue<String> q = new ArrayDeque<>();`
    2. `offer` three tasks; `peek` without removing; `poll` until empty.
    3. Print queue state after each op.
    4. **Done when:** Peek leaves size unchanged; poll drains FIFO.

- [ ] **Day 16 — Stack via Deque**
  - [ ] **Learn:** [`Deque` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Deque.html) · Prefer Deque over legacy `Stack` (see API notes)
  - [ ] **Practice:** Stack + balanced parentheses
    1. Use `Deque<Character>` as stack: `push`/`pop`/`peek` (or `addFirst`/`removeFirst`).
    2. Write `boolean isBalanced(String s)` for `()[]{}`.
    3. Test `"([])"`, `"([)]"`, `"(()"`, `""`.
    4. **Done when:** All four test cases return correct booleans.

- [ ] **Day 17 — PriorityQueue**
  - [ ] **Learn:** [`PriorityQueue` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html) · [Queue Implementations](https://docs.oracle.com/javase/tutorial/collections/implementations/queue.html)
  - [ ] **Practice:** Top-K / next-task scheduling
    1. Create tasks with priority int; `PriorityQueue` ordered by priority.
    2. Offer several; poll and print next-to-run order.
    3. Top-K: given numbers, keep a min-heap of size K; print top K.
    4. **Done when:** Poll order matches priorities; top-K matches hand calculation.

- [ ] **Day 18 — HashMap internals sketch**
  - [ ] **Learn:** [`HashMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashMap.html) (read class-level docs) · [HashMap (Oracle tutorial implementations)](https://docs.oracle.com/javase/tutorial/collections/implementations/map.html)
  - [ ] **Practice:** Buckets / hash / collisions note
    1. Draw array of buckets; show key → hash → index.
    2. Draw two keys colliding in one bucket (chain/tree idea — high level).
    3. Write 5–8 sentences explaining put/get in your own words.
    4. **Done when:** Diagram + paragraph exist (no need for real JDK source diving).

- [ ] **Day 19 — ArrayList capacity growth**
  - [ ] **Learn:** [`ArrayList` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html) · [List Implementations](https://docs.oracle.com/javase/tutorial/collections/implementations/list.html)
  - [ ] **Practice:** Capacity vs size notes
    1. Draw: empty list → add until grow → new larger array copy.
    2. Call `ensureCapacity(100)` then add 50 elements; note why pre-sizing helps.
    3. Write: size = elements; capacity = backing array length (not publicly readable — conceptual).
    4. **Done when:** Notes distinguish size vs capacity and mention growth copy cost.

- [ ] **Day 20 — Unmodifiable collections**
  - [ ] **Learn:** [`Collections` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collections.html) · [`List.copyOf`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/List.html#copyOf(java.util.Collection))
  - [ ] **Practice:** Unmodifiable views and copyOf
    1. Create mutable list; wrap with `Collections.unmodifiableList`; try `add` — catch `UnsupportedOperationException`.
    2. Mutate the **original** list; observe the view changes (aliasing).
    3. Fix with `List.copyOf(original)` and show original mutation no longer affects the copy.
    4. **Done when:** Both failure and fixed copy behaviors are demonstrated.

- [ ] **Day 21 — Wildcard generics lab**
  - [ ] **Learn:** [Wildcards](https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html) · [Upper Bounded Wildcards](https://docs.oracle.com/javase/tutorial/java/generics/upperBounded.html) · [Guidelines for Wildcard Use (PECS)](https://docs.oracle.com/javase/tutorial/java/generics/wildcardGuidelines.html)
  - [ ] **Practice:** Sum `List<? extends Number>`
    1. Write `static double sum(List<? extends Number> nums)`.
    2. Call with `List<Integer>` and `List<Double>`; print sums.
    3. Try `nums.add(1)` inside sum — confirm it doesn’t compile (PECS).
    4. One-line note: producer extends, consumer super.
    5. **Done when:** Both list types sum correctly and add is rejected.

### Week 4 — Projects & mastery (Days 22–28)

- [ ] **Day 22 — Mini-project start**
  - [ ] **Learn:** Skim [Collections Interfaces](https://docs.oracle.com/javase/tutorial/collections/interfaces/index.html)
  - [ ] **Practice:** Inventory/contact with List + Map index
    1. Choose Contact Book or Inventory; create package + `Main` menu.
    2. Store entities in `List<T>`; also keep `Map<String, T>` by id for O(1) lookup.
    3. Implement add + list + find-by-id.
    4. **Done when:** Add three items; list all; find one by id.

- [ ] **Day 23 — Sets & search**
  - [ ] **Learn:** [Set Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/set.html)
  - [ ] **Practice:** Tags/categories with Set
    1. Add `Set<String> tags` (or categories) on each entity.
    2. Menu: add tag; search “has tag X” by scanning list.
    3. Print matching entities.
    4. **Done when:** Tag search returns only tagged items.

- [ ] **Day 24 — Priority feature**
  - [ ] **Learn:** [`PriorityQueue` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html)
  - [ ] **Practice:** Urgent tasks via PriorityQueue
    1. Model `Task` with priority; hold pending work in `PriorityQueue`.
    2. Menu: add task; “next urgent” polls highest priority.
    3. Seed 4 tasks; poll twice; show remaining.
    4. **Done when:** Poll order matches priorities.

- [ ] **Day 25 — Sorting UI options**
  - [ ] **Learn:** [`Comparator` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Comparator.html) · [Object Ordering](https://docs.oracle.com/javase/tutorial/collections/interfaces/order.html)
  - [ ] **Practice:** Sort menu options
    1. Menu choices: sort by name, by id, by priority/price.
    2. Each choice applies a different `Comparator` and prints the list.
    3. Do not mutate stored order permanently unless you intend to (copy then sort is fine).
    4. **Done when:** Three sort options produce three correct orders.

- [ ] **Day 26 — Justify structure choices**
  - [ ] **Learn:** Recap complexity from §3 cheat-sheet + [Implementations](https://docs.oracle.com/javase/tutorial/collections/implementations/index.html)
  - [ ] **Practice:** README complexity justifications
    1. Open/create project `README.md`.
    2. For each structure used (`List`, `Map`, `Set`, `PriorityQueue`), write: why + big-O for main ops.
    3. Note one alternative you rejected and why.
    4. **Done when:** README section lists every collection choice with a reason.

- [ ] **Day 27 — Defensive returns**
  - [ ] **Learn:** [`Collections.unmodifiableList`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collections.html#unmodifiableList(java.util.List)) · [`List.copyOf`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/List.html#copyOf(java.util.Collection))
  - [ ] **Practice:** Service returns copies/unmodifiable
    1. Change service `listAll()` to return `List.copyOf(internal)` or unmodifiable view.
    2. From menu code, try mutating the returned list — expect failure or no effect on store.
    3. Confirm internal store still mutable only via service methods.
    4. **Done when:** Caller cannot corrupt internal list via returned reference.

- [ ] **Day 28 — Mastery gate**
  - [ ] **Learn:** Recap [Collections trail](https://docs.oracle.com/javase/tutorial/collections/index.html)
  - [ ] **Practice:** Checklist + timed drills
    1. Tick §12 checklist only for what you can demo.
    2. Timed: pick 10 problems from §5; solve as many as you can in 40 minutes.
    3. Note weak spots (Set vs Map, Iterator, PriorityQueue, etc.).
    4. **Done when:** Checklist reviewed and drill score/notes recorded.

---

## 5. Hands-on exercises

1. Remove duplicates from a list (preserve order → `LinkedHashSet` path).
2. Find intersection/union/difference of two sets.
3. Word frequency counter from a paragraph.
4. Group students by grade band using `Map<String, List<Student>>`.
5. First non-repeating character using LinkedHashMap.
6. Top-K frequent elements with `PriorityQueue`.
7. Implement LRU-ish sketch with `LinkedHashMap` (accessOrder constructor)—read docs carefully.
8. Sort employees by salary then name (`Comparator` chaining).
9. Detect anagrams: sort chars or frequency maps.
10. Browser history back/forward with two deques/stacks.
11. Merge intervals style problem using sorting + lists (algorithmic practice).
12. Phone book: `Map<String, List<String>>` name → numbers.

---

## 6. Mini-projects

### A. Contact Book

- Store contacts in a `List`
- Index by phone/email with `Map` for O(1) lookup
- Tags with `Set`
- Search by name prefix (list scan OK)
- **Full steps:** [A — Contact Book](../mini-projects/phase-01-core-java/03-collections/A-contact-book.md)

### B. Warehouse Inventory

- SKU → quantity (`HashMap`)
- Categories → set of SKUs
- Low-stock priority queue for restock suggestions
- Export sorted reports (`TreeMap` or sort list)
- **Full steps:** [B — Warehouse Inventory](../mini-projects/phase-01-core-java/03-collections/B-warehouse-inventory.md)

### C. Task Board (prep for phase project)

- Tasks with priority, status
- Queues per status column
- Comparator sorts for “my day” views
- **Full steps:** [C — Task Board](../mini-projects/phase-01-core-java/03-collections/C-task-board.md)

---

## 7. Industry use cases

- **Caching & lookups:** HashMap-style indexes (in-memory)
- **Configuration & feature flags:** key/value maps
- **Deduplication:** Sets for IDs already processed
- **Job scheduling:** Priority queues for next work item
- **API responses:** Lists of DTOs; maps for aggregation results
- **Domain modeling:** One-to-many as `List`/`Set` fields on entities (later JPA)

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Using raw types (`List` without `<>`) | Always parameterize |
| Mutating list while enhanced-for looping | Use `Iterator.remove` or `removeIf` |
| Relying on `LinkedList` for performance blindly | Measure; default to `ArrayList` |
| Mutable keys in HashMap | Keys should be immutable for stability |
| Broken `hashCode`/`equals` | Same fields; consistent contract |
| Assuming `PriorityQueue` iteration is sorted | Only peek/poll give order guarantees |
| Returning live internal lists | Defensive copy / unmodifiable view |
| Using `Stack` class legacy API | Prefer `Deque` (`ArrayDeque`) |
| Ignoring null rules | Know which impls allow null keys/values |
| Sorting without `Comparable`/`Comparator` | Provide one explicitly |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Oracle Tutorials — Collections](https://docs.oracle.com/javase/tutorial/collections/index.html) | Primary trail |
| [Collections Framework overview](https://docs.oracle.com/javase/tutorial/collections/intro/index.html) | Big picture |
| [Aggregate operations (preview of Streams)](https://docs.oracle.com/javase/tutorial/collections/streams/index.html) | Bridge to Java 8+ |
| [Baeldung Collections (free articles)](https://www.baeldung.com/java-collections) | Extra examples (cross-check with Oracle) |
| [dev.java](https://dev.java/learn/) | Modern Java learning |

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Use this as backup index.

- Collections trail: https://docs.oracle.com/javase/tutorial/collections/index.html
- Interfaces: https://docs.oracle.com/javase/tutorial/collections/interfaces/index.html
- Implementations: https://docs.oracle.com/javase/tutorial/collections/implementations/index.html
- Algorithms: https://docs.oracle.com/javase/tutorial/collections/algorithms/index.html
- `List` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/List.html
- `ArrayList` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html
- `LinkedList` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/LinkedList.html
- `Set` / `HashSet` / `TreeSet`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Set.html
- `Map` / `HashMap` / `TreeMap`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Map.html
- `Queue` / `PriorityQueue`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Queue.html
- `Deque` / `ArrayDeque`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Deque.html
- `Iterator`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Iterator.html
- `Comparable`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Comparable.html
- `Comparator`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Comparator.html
- Generics tutorial: https://docs.oracle.com/javase/tutorial/java/generics/index.html

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. List fluency | ArrayList daily driver | 15 list drills |
| 2. Set/Map fluency | Dedupe + frequency | 10 map drills |
| 3. Ordering | Comparable/Comparator | Custom sort demos |
| 4. Queues | Deque + PriorityQueue | 2 mini features |
| 5. Internals | Explain ArrayList + HashMap | Drawn notes exist |
| 6. Project | Structure choices justified | Mini-project A or B done |
| 7. Gate | Ready for Exceptions | Checklist ≥90% |

---

## 12. Mastery checklist

- [ ] Navigates the Collections hierarchy without confusion
- [ ] Uses `ArrayList` confidently for common List needs
- [ ] Knows when `LinkedList`/`ArrayDeque` is appropriate
- [ ] Uses `HashSet`/`TreeSet` and explains uniqueness + ordering
- [ ] Uses `HashMap`/`TreeMap`/`LinkedHashMap` appropriately
- [ ] Implements `Comparable` and `Comparator` correctly
- [ ] Iterates safely; removes elements without CME surprises
- [ ] States typical Big-O for get/add/contains on major structures
- [ ] Explains HashMap hashing at a conceptual level
- [ ] Uses generics (no raw types) including basic wildcards
- [ ] Chooses structures based on access patterns, not habit alone
- [ ] Completed at least one collections mini-project
- [ ] Ready to start [04-exception-handling.md](04-exception-handling.md)
