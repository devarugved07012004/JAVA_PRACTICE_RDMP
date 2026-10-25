# 05 — Multithreading & Concurrency

**Duration:** ~3–4 weeks (21–28 days) · **Cadence:** 1 hour/day  
**Focus:** Threads, Runnable/Callable, sync, locks, executors, concurrent collections, CompletableFuture

---

## 1. Why this topic is important

Modern apps do overlapping work: handle many requests, call remote services, process batches, keep UIs responsive (desktop), or overlap I/O. Java’s concurrency tools let you use CPU cores and hide latency—but shared mutable state without coordination causes race conditions that are hard to reproduce.

This topic builds mental models for **happens-before**, safe publication, and structured concurrency via executors—skills you will reuse in Spring async, reactive-adjacent thinking, and high-throughput services later.

---

## 2. Prerequisites

- Completed [04-exception-handling.md](04-exception-handling.md) mastery checklist (≥90%)
- Solid OOP + collections
- Comfortable debugging single-threaded code
- Willingness to add logging/`Thread.currentThread().getName()` while learning

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Process vs thread | Shared memory among threads in a JVM process |
| Creating threads | `Thread` subclass vs `Runnable` |
| Lifecycle (simplified) | new → runnable → running → blocked/waiting → terminated |
| `start` vs `run` | `start` schedules; calling `run` directly is single-threaded |
| Sleep / join | `Thread.sleep`, `join` for coordination basics |
| Race conditions | Lost updates on shared counters |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| `synchronized` | Intrinsic locks; monitor on object |
| `volatile` | Visibility for flags (not a full substitute for atomic compound actions) |
| `wait` / `notify` | Low-level coordination (know; prefer higher-level tools) |
| Explicit locks | `ReentrantLock`, tryLock, interruptible lock |
| Deadlocks | Circular waits; prevention strategies |
| `Callable` + `Future` | Return values and exceptions from tasks |
| Executor framework | Decouple task submission from thread creation |
| Thread pools | Fixed, cached, single; queueing behavior |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| Concurrent collections | `ConcurrentHashMap`, `CopyOnWriteArrayList`, blocking queues |
| Producer–consumer | `BlockingQueue` patterns |
| `CompletableFuture` | Async pipelines, combine, exception handling |
| Atomic variables | `AtomicInteger` etc. for simple state |
| Thread confinement / immutability | Design away from shared mutability |
| Common pool pitfalls | Blocking in `parallelStream`/`ForkJoinPool` (awareness) |
| Interruption | Cooperative cancel; respect `InterruptedException` |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Threads & shared state (Days 1–7)

- [ ] **Day 1 — Creating threads**
  - [ ] **Learn:** [Processes and Threads](https://docs.oracle.com/javase/tutorial/essential/concurrency/procthread.html) · [Defining and Starting a Thread](https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html) · [`Thread` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Thread.html)
  - [ ] **Practice:** Two ways to create threads; start vs run
    1. Create `extends Thread` subclass that prints `Thread.currentThread().getName()` in `run`.
    2. Create `implements Runnable` that does the same; wrap in `new Thread(runnable, "worker-B")`.
    3. Call `start()` on both; confirm interleaved/named output.
    4. Demo mistake: call `run()` instead of `start()` once — note it runs on main.
    5. **Done when:** Both start paths show worker names; run-vs-start difference is noted.

- [ ] **Day 2 — Race conditions**
  - [ ] **Learn:** [Memory Consistency Errors](https://docs.oracle.com/javase/tutorial/essential/concurrency/memconsist.html) · [Thread Interference](https://docs.oracle.com/javase/tutorial/essential/concurrency/interfere.html)
  - [ ] **Practice:** Race a shared counter
    1. Shared `int counter = 0`; two threads each loop 100_000 times doing `counter++`.
    2. `start` both; `join` both; print final counter.
    3. Run 5 times; record finals (often &lt; 200_000).
    4. Journal 4–6 sentences: why increments race (read-modify-write).
    5. **Done when:** You observed at least one wrong total and explained why.

- [ ] **Day 3 — synchronized**
  - [ ] **Learn:** [Synchronization](https://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html) · [Intrinsic Locks and Synchronization](https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html)
  - [ ] **Practice:** Fix the counter with synchronized
    1. Wrap increment in a `synchronized` method or `synchronized (lock) { counter++; }`.
    2. Re-run the Day 2 stress; expect exactly 200_000 every time (try 5 runs).
    3. Optionally compare synchronized method vs block on a private `final Object lock`.
    4. **Done when:** Five consecutive runs print 200_000.

- [ ] **Day 4 — Visibility & volatile**
  - [ ] **Learn:** [Atomic Access](https://docs.oracle.com/javase/tutorial/essential/concurrency/atomic.html) · [`volatile` in JLS (skim)](https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.3.1.4)
  - [ ] **Practice:** Stop-flag visibility with volatile
    1. Worker loops `while (!stop)` doing light work; main sleeps then sets `stop = true`.
    2. First try **without** `volatile` (may hang on some JVMs/settings — don’t spin forever; kill if needed).
    3. Mark `stop` as `volatile boolean`; confirm worker exits promptly.
    4. Notes: volatile ≠ safe for `counter++`; only visibility/ordering for that flag.
    5. **Done when:** Volatile flag stops the worker; notes warn against using it for compounds.

- [ ] **Day 5 — join & timing**
  - [ ] **Learn:** [Joins](https://docs.oracle.com/javase/tutorial/essential/concurrency/join.html) · [`Thread.sleep`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Thread.html#sleep(long))
  - [ ] **Practice:** join workers; timed sleep tasks
    1. Start 3 workers that `sleep` 200/400/600 ms then print done.
    2. `join` each (or join in a loop); print `"all done"` only after all finish.
    3. Measure roughly with `System.currentTimeMillis()` before/after.
    4. **Done when:** “all done” never prints before the slowest worker.

- [ ] **Day 6 — Bank account transfers**
  - [ ] **Learn:** Re-skim [Synchronization](https://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html) · [Liveness](https://docs.oracle.com/javase/tutorial/essential/concurrency/liveness.html)
  - [ ] **Practice:** Synchronized transfer simulation
    1. `BankAccount` with `balance`; `synchronized void deposit/withdraw`.
    2. `transfer(from, to, amount)`: withdraw then deposit (synchronize carefully — one account at a time for now is OK).
    3. Many threads transferring between two accounts; assert total money conserved.
    4. Note in journal: lock ordering matters when locking two accounts (preview Day 9).
    5. **Done when:** Total money invariant holds after stress run.

- [ ] **Day 7 — Week review**
  - [ ] **Learn:** Skim [Concurrency trail hub](https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html)
  - [ ] **Practice:** One-page “what is a race?”
    1. Paste or reference your Day 2 wrong totals.
    2. Explain race in your words + how `synchronized` fixed it.
    3. List start vs run, join, volatile flag use-case in bullet form.
    4. **Done when:** One-page note exists and covers race + fix.

### Week 2 — Locks, deadlock, callables (Days 8–14)

- [ ] **Day 8 — ReentrantLock**
  - [ ] **Learn:** [Lock Objects](https://docs.oracle.com/javase/tutorial/essential/concurrency/newlocks.html) · [`ReentrantLock` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/locks/ReentrantLock.html)
  - [ ] **Practice:** Counter with ReentrantLock + tryLock
    1. Rewrite Day 3 counter using `ReentrantLock`; always `unlock()` in `finally`.
    2. Add path: `if (lock.tryLock(100, TimeUnit.MILLISECONDS)) { … } else print "busy"`.
    3. Stress to 200_000 again.
    4. **Done when:** Count correct and tryLock timeout path is reachable in a demo.

- [ ] **Day 9 — Deadlock**
  - [ ] **Learn:** [Deadlock](https://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html) · [Liveness](https://docs.oracle.com/javase/tutorial/essential/concurrency/liveness.html)
  - [ ] **Practice:** Cause deadlock, then fix ordering
    1. Two locks `a` and `b`; thread1 locks a then b; thread2 locks b then a — with sleeps to force deadlock.
    2. Observe hang; kill process.
    3. Fix: both threads lock in same order (`a` then `b`).
    4. Re-run; confirm completion.
    5. **Done when:** Deadlock reproduced once and fixed with consistent ordering.

- [ ] **Day 10 — wait / notify**
  - [ ] **Learn:** [Guarded Blocks](https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html)
  - [ ] **Practice:** Tiny bounded buffer
    1. Buffer capacity 1 or 2; `put` waits while full; `take` waits while empty (`wait`/`notifyAll`).
    2. One producer, one consumer thread.
    3. In notes: “prefer `BlockingQueue` in real code — wait/notify is error-prone.”
    4. **Done when:** Producer/consumer exchange N items without busy-spin.

- [ ] **Day 11 — Callable & Future**
  - [ ] **Learn:** [`Callable` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Callable.html) · [`Future` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Future.html)
  - [ ] **Practice:** Callable returning a value
    1. `ExecutorService exec = Executors.newSingleThreadExecutor();`
    2. Submit `Callable<Integer>` that returns 42 (or sums 1..n).
    3. `Future.get()`; print result; shutdown.
    4. Submit a failing Callable; catch `ExecutionException` and print cause.
    5. **Done when:** Success path returns value; failure path surfaces cause.

- [ ] **Day 12 — ExecutorService**
  - [ ] **Learn:** [Executors](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html) · [`ExecutorService` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html)
  - [ ] **Practice:** Fixed pool submit + shutdown
    1. `Executors.newFixedThreadPool(4)`.
    2. Submit 20 Runnables that print task id + thread name.
    3. `shutdown()` then `awaitTermination(5, SECONDS)`.
    4. **Done when:** All tasks finish and awaitTermination returns true (or you handle timeout).

- [ ] **Day 13 — Threads vs pools**
  - [ ] **Learn:** [Thread Pools](https://docs.oracle.com/javase/tutorial/essential/concurrency/pools.html) · [`Executors` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Executors.html)
  - [ ] **Practice:** 1000 Threads vs pool of 8
    1. Time creating/starting/joining 1000 short-lived `Thread`s (tiny work).
    2. Time same work via fixed pool of 8 + shutdown.
    3. Record both timings in notes; conclude why pools win.
    4. **Done when:** Both approaches complete and timing comparison is written.

- [ ] **Day 14 — Task exceptions**
  - [ ] **Learn:** [`ExecutionException` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutionException.html) · Revisit [Executors](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)
  - [ ] **Practice:** Never-silent pooled failures
    1. Submit tasks where every 5th throws RuntimeException.
    2. For `Future` tasks: `get()` in try/catch and log cause.
    3. For plain `execute(Runnable)`: wrap runnable body in try/catch that logs.
    4. **Done when:** Failures are visible in output; pool still shuts down cleanly.

### Week 3 — Concurrent collections & pipelines (Days 15–21)

- [ ] **Day 15 — BlockingQueue producer–consumer**
  - [ ] **Learn:** [High-Level Concurrency Objects](https://docs.oracle.com/javase/tutorial/essential/concurrency/highlevel.html) · [`BlockingQueue` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/BlockingQueue.html)
  - [ ] **Practice:** 1 producer / 2 consumers
    1. `BlockingQueue<Integer> q = new ArrayBlockingQueue<>(10);`
    2. Producer puts 1..50 then poison pills (e.g. -1 twice).
    3. Two consumers `take` until poison; print consumed counts.
    4. **Done when:** All 50 items consumed exactly once across both consumers.

- [ ] **Day 16 — ConcurrentHashMap**
  - [ ] **Learn:** [`ConcurrentHashMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html) · [Concurrent Collections](https://docs.oracle.com/javase/tutorial/essential/concurrency/collections.html)
  - [ ] **Practice:** Concurrent word histogram
    1. Split text into words; many threads call `map.merge(word, 1, Integer::sum)`.
    2. Use `ConcurrentHashMap`.
    3. Join threads; print top counts.
    4. Optional: contrast broken `HashMap` under same stress (may throw or wrong counts).
    5. **Done when:** Concurrent totals match a single-threaded count.

- [ ] **Day 17 — CopyOnWriteArrayList**
  - [ ] **Learn:** [`CopyOnWriteArrayList` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CopyOnWriteArrayList.html)
  - [ ] **Practice:** Read-heavy listeners demo
    1. `CopyOnWriteArrayList<String> listeners`; readers iterate often; rare `add`.
    2. Show iteration doesn’t throw CME when another thread adds.
    3. Notes: write copies whole array — bad for write-heavy.
    4. **Done when:** Concurrent iteration+add works; write-cost note exists.

- [ ] **Day 18 — CompletableFuture basics**
  - [ ] **Learn:** [`CompletableFuture` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html) · [CompletableFuture (package summary)](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/package-summary.html)
  - [ ] **Practice:** supplyAsync → thenApply → thenAccept
    1. `supplyAsync(() -> "hello")`.
    2. `thenApply(String::toUpperCase)`.
    3. `thenAccept(System.out::println)`.
    4. `join()` on the chain so main waits.
    5. **Done when:** Program prints `HELLO` and exits cleanly.

- [ ] **Day 19 — Combining futures**
  - [ ] **Learn:** [`CompletableFuture` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html) (`thenCombine`, `allOf`, `orTimeout`)
  - [ ] **Practice:** thenCombine / allOf / timeout
    1. Two `supplyAsync` ints; `thenCombine` to sum; print.
    2. `CompletableFuture.allOf(f1, f2, f3).join();`
    3. If JDK 9+: `orTimeout(1, SECONDS)` on a slow future; handle `TimeoutException`.
    4. **Done when:** Combine and allOf demos run; timeout attempted if available.

- [ ] **Day 20 — CF exception handling**
  - [ ] **Learn:** [`CompletableFuture` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html) (`exceptionally`, `handle`)
  - [ ] **Practice:** exceptionally and handle
    1. Chain that throws in `supplyAsync`.
    2. Recover with `exceptionally(ex -> fallback)`.
    3. Alternate demo with `handle((ok, ex) -> …)` branching on null ex.
    4. **Done when:** Both recovery styles print a fallback instead of crashing.

- [ ] **Day 21 — Design notes**
  - [ ] **Learn:** Skim [Concurrency trail](https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html) · [`BlockingQueue` again](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/BlockingQueue.html)
  - [ ] **Practice:** Prefer queues over shared mutable state
    1. Write one page: shared mutable objects vs message passing via queues.
    2. Map your Week 4 project idea to “who produces / who consumes.”
    3. List 3 tools you’ll use (pool, BlockingQueue, CF, CHM…).
    4. **Done when:** Design note exists before coding Week 4.

### Week 4 — Mini-project & mastery (Days 22–28)

- [ ] **Day 22 — Project start (pool)**
  - [ ] **Learn:** [Executors](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)
  - [ ] **Practice:** Download simulator or report generator
    1. Create project with `ExecutorService` fixed pool (e.g. 4).
    2. Fake “download” tasks: sleep random 100–500 ms; print start/end.
    3. Submit 10+ tasks from `main`.
    4. **Done when:** Tasks run concurrently (overlapping timestamps) on pool threads.

- [ ] **Day 23 — Progress tracking**
  - [ ] **Learn:** [`AtomicInteger` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/AtomicInteger.html) · [`ConcurrentHashMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html)
  - [ ] **Practice:** Track progress with atomics / CHM
    1. `AtomicInteger completed`; each task `incrementAndGet` on finish.
    2. Optional: `ConcurrentHashMap` status per task id (`RUNNING`/`DONE`).
    3. Main thread periodically prints progress until done.
    4. **Done when:** Progress reaches total task count without races.

- [ ] **Day 24 — CF pipeline**
  - [ ] **Learn:** [`CompletableFuture` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html)
  - [ ] **Practice:** Multi-step fake I/O pipeline
    1. Steps: fetch → parse → enrich → save (each sleeps / prints).
    2. Chain with `thenCompose`/`thenApply` on `CompletableFuture`.
    3. Run for 3 items (parallel supplyAsync ok).
    4. **Done when:** Each item prints all four steps in order.

- [ ] **Day 25 — Stress & races**
  - [ ] **Learn:** Revisit [Thread Interference](https://docs.oracle.com/javase/tutorial/essential/concurrency/interfere.html)
  - [ ] **Practice:** Tests that fail without sync
    1. Temporarily break a shared counter/status (remove sync/atomic).
    2. Loop test 100 times; assert expected total — should fail sometimes.
    3. Restore correct sync; confirm 100/100 pass.
    4. **Done when:** Broken mode fails at least once; fixed mode is green.

- [ ] **Day 26 — Graceful shutdown**
  - [ ] **Learn:** [Interrupts](https://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html) · [`ExecutorService` shutdown](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html)
  - [ ] **Practice:** Cooperative interrupt + shutdown
    1. Long sleeper checks `Thread.currentThread().isInterrupted()` or catches `InterruptedException` and restores interrupt status.
    2. On quit: `shutdown()`; if needed `shutdownNow()`; `awaitTermination`.
    3. Confirm JVM exits (no non-daemon leftovers).
    4. **Done when:** Quit ends promptly; no hang after Ctrl-path / menu quit.

- [ ] **Day 27 — Document thread model**
  - [ ] **Learn:** Skim your notes + [Processes and Threads](https://docs.oracle.com/javase/tutorial/essential/concurrency/procthread.html)
  - [ ] **Practice:** Write the project thread model
    1. Document: main thread responsibilities.
    2. Document: pool size, task types, shared state owners.
    3. Document: shutdown sequence.
    4. **Done when:** One-page thread-model doc matches the running code.

- [ ] **Day 28 — Mastery gate**
  - [ ] **Learn:** Recap [Concurrency trail](https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html)
  - [ ] **Practice:** Checklist + optional deadlock quiz
    1. Tick §12 checklist only for demoable skills.
    2. Optional: re-code Day 9 deadlock killer from memory.
    3. Note weak areas (CF, locks, queues).
    4. **Done when:** Checklist reviewed; optional quiz compiles if attempted.

---

## 5. Hands-on exercises

1. Parallel sum of a large array using a fixed pool + partial sums.
2. Thread-safe lazy init patterns (wrong double-check vs safe holder)—observe.
3. Timeout a task using `Future.get(timeout)` and cancel.
4. Dining philosophers simplified (4 threads, 4 locks)—detect/prevent deadlock.
5. Multithreaded web-crawler *simulation* (fake URLs, sleep) with bounded queue.
6. Rate-limited workers: only N tasks run at once (pool size).
7. Concurrent cache map with `computeIfAbsent`.
8. Chain: fetch → parse → save as three `CompletableFuture` stages (mocked).
9. Prove `ArrayList` is unsafe under concurrent add; replace with concurrent structure.
10. Interrupt a thread stuck in a loop checking `Thread.interrupted()`.

---

## 6. Mini-projects

### A. Parallel File Word-Count Simulator

- Split “files” (strings/paths) across workers
- Merge counts with `ConcurrentHashMap`
- ExecutorService lifecycle done right
- **Full steps:** [A — Parallel Word Count](../mini-projects/phase-01-core-java/05-multithreading/A-parallel-word-count.md)

### B. Order Processing Pipeline

- Producer enqueues orders
- Workers validate → charge → notify (sleeps as fake I/O)
- Metrics: processed count, failures
- Optional: `CompletableFuture` per order
- **Full steps:** [B — Order Processing Pipeline](../mini-projects/phase-01-core-java/05-multithreading/B-order-processing-pipeline.md)

### C. Prep slice for phase project

- Background indexer thread building a search map while CLI remains responsive (careful design)
- **Full steps:** [C — Phase Project Prep](../mini-projects/phase-01-core-java/05-multithreading/C-phase-project-prep.md)

---

## 7. Industry use cases

- **Request handling:** Servers use pools so one slow call doesn’t block everything.
- **Batch processing:** Partition work across cores.
- **Async I/O orchestration:** `CompletableFuture` for dependent remote calls.
- **Messaging consumers:** Multiple workers pulling from queues.
- **Caching & metrics:** Concurrent maps for shared read/write statistics.
- **Scheduled jobs:** Executor scheduling variants for periodic work.

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Calling `run()` instead of `start()` | Always `start` (or submit to executor) |
| Shared mutable state without sync | Synchronize, confine, or make immutable |
| Synchronizing on wrong object | Lock the object that guards the state |
| Nested locks in inconsistent order | Global lock order; or reduce lock scope |
| Not shutting down executors | `shutdown` + `awaitTermination` |
| Swallowing `InterruptedException` | Restore interrupt flag; exit cooperatively |
| Using `Thread.sleep` for coordination logic | Prefer locks/conditions/queues |
| Blocking forever on `Future.get()` | Timeouts; cancellation policy |
| Assuming `ConcurrentHashMap` makes compound actions atomic | Use atomic methods (`compute`, etc.) |
| Over-threading | Cap pool size; measure |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Oracle Tutorials — Concurrency](https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html) | Primary trail |
| [Executor tutorial section](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html) | Pools |
| [CompletableFuture (JEP/API docs)](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html) | Async API |
| [java.util.concurrent package summary](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/package-summary.html) | Map of tools |
| Baeldung concurrency articles (free) | Extra examples; verify with Oracle |

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Use this as backup index.

- Concurrency trail: https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html
- Processes & threads: https://docs.oracle.com/javase/tutorial/essential/concurrency/procthread.html
- Synchronization: https://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html
- Liveness (deadlock): https://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html
- Executors: https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html
- High-level concurrency objects: https://docs.oracle.com/javase/tutorial/essential/concurrency/highlevel.html
- `Thread` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Thread.html
- `Runnable` / `Callable`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Runnable.html · https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Callable.html
- `ExecutorService`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html
- `Future`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Future.html
- `ReentrantLock`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/locks/ReentrantLock.html
- `BlockingQueue`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/BlockingQueue.html
- `ConcurrentHashMap`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html
- `CompletableFuture`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html
- `AtomicInteger`: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/AtomicInteger.html

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Thread basics | Start/join/race awareness | Broken + fixed counter |
| 2. Synchronization | `synchronized` / locks | Thread-safe account demo |
| 3. Deadlock literacy | Cause + prevent | Intentional + fixed demo |
| 4. Executors | Pools + Futures | Proper shutdown |
| 5. Concurrent utils | Queues + CHM | Producer–consumer |
| 6. Async | CompletableFuture chains | Multi-stage demo |
| 7. Project + gate | Documented thread model | Checklist ≥90% |

---

## 12. Mastery checklist

- [ ] Creates tasks with `Runnable` and `Callable` correctly
- [ ] Explains race conditions and demonstrates a fix
- [ ] Uses `synchronized` and knows lock scope matters
- [ ] Uses `ReentrantLock` for at least one example
- [ ] Can create, explain, and eliminate a simple deadlock
- [ ] Submits work to `ExecutorService` and shuts it down safely
- [ ] Uses `Future` results and handles task exceptions
- [ ] Builds a producer–consumer with `BlockingQueue`
- [ ] Updates shared maps safely (`ConcurrentHashMap` or locking)
- [ ] Composes simple `CompletableFuture` pipelines
- [ ] Treats interruption as cooperative cancellation
- [ ] Completed a concurrency mini-project with notes on shared state
- [ ] Ready to start [06-java8-plus.md](06-java8-plus.md)
