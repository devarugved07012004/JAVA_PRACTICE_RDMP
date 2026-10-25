# Mini-Project A — Parallel File Word-Count Simulator

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/05-multithreading.md](../../phase-01-core-java/05-multithreading.md)  
**Code folder suggestion:** `projects/phase-01/05-multithreading/parallel-word-count/`  
**Time:** ~8 hours at 1 hr/day  

## Goal

Split “files” (strings or paths) across workers, merge word counts with `ConcurrentHashMap`, and shut down `ExecutorService` correctly. Simulate parallel text analytics safely.

## Prerequisites

- [ ] Threads, Runnable/Callable, ExecutorService basics
- [ ] Concurrent collections intro from Multithreading Weeks 1–3

## Learn (open before coding)

- [Executors](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)
- [`ExecutorService` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html)
- [`ConcurrentHashMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html)
- [Thread Pools](https://docs.oracle.com/javase/tutorial/essential/concurrency/pools.html)
- [Callable and Future](https://docs.oracle.com/javase/tutorial/essential/concurrency/getex.html)
- [Memory Consistency / sync overview](https://docs.oracle.com/javase/tutorial/essential/concurrency/memconsist.html)

## Project layout

```text
parallel-word-count/
  README.md
  src/
    com/learn/wc/
      app/Main.java
      worker/FileWordCountTask.java
      service/WordCountService.java
```

## Build steps

### Step 1 — In-memory “files”

- [ ] Represent files as `Map<String, String>` name → contents (or `List<String>` paths later)
- [ ] Seed ≥4 documents with overlapping words
- [ ] Sequential baseline: count words in one thread into a `HashMap`
- [ ] Compile stub:
  ```bash
  javac -d out src/com/learn/wc/**/*.java
  java -cp out com.learn.wc.app.Main
  ```
- **Done when:** Sequential counts look correct for a known sample.

### Step 2 — Worker task

- [ ] `FileWordCountTask implements Callable<Map<String, Integer>>` (or Runnable updating shared map)
- [ ] Prefer Callable returning local map (easier reasoning) **or** shared `ConcurrentHashMap` with `merge`
- [ ] Split text on `\\s+`; normalize lowercase
- **Done when:** One task counts one document correctly.

### Step 3 — Shared ConcurrentHashMap merge

- [ ] Create `ConcurrentHashMap<String, Integer> global`
- [ ] Each task uses `global.merge(word, 1, Integer::sum)` **or** merge returned maps in main after `Future.get`
- [ ] Document which approach you chose and why
- **Done when:** Two parallel tasks contribute without lost updates (spot-check totals).

### Step 4 — ExecutorService pool

- [ ] `Executors.newFixedThreadPool(n)` with n = min(files, processors)
- [ ] Submit one task per file; collect `Future`s
- [ ] Wait for all: `future.get()` in loop or `invokeAll`
- **Done when:** All files processed; pool used (not one Thread per hand-managed forever).

### Step 5 — Lifecycle: shutdown

- [ ] After submit: `shutdown()`
- [ ] `awaitTermination(timeout, unit)`; if false → `shutdownNow()`
- [ ] Confirm process exits (no non-daemon leftovers)
- **Done when:** App returns to OS prompt every run.

### Step 6 — Print top words

- [ ] Sort entries by count descending; print top 10
- [ ] Compare sequential vs parallel totals for same seed (must match)
- **Done when:** Totals identical; timing print optional (System.nanoTime).

### Step 7 — Failure isolation

- [ ] If one “file” is null/empty, task should not kill others (handle inside task)
- [ ] Propagate unexpected errors via Future.get exception handling—print which file failed
- **Done when:** One bad file doesn’t lose counts from good files (if design allows).

### Step 8 — README thread model

- [ ] Document: who shares what; why ConcurrentHashMap (or merge-after-join)
- [ ] Compile/run + demo
- [ ] Note pitfalls: sync HashMap, forgetting shutdown
- **Done when:** README explains correctness strategy in ≤10 lines.

## Definition of Done

- [ ] Multiple files counted in a pool
- [ ] ConcurrentHashMap (or safe merge) used
- [ ] Executor shutdown correct; process exits
- [ ] Sequential vs parallel totals match
- [ ] README thread model written

## Demo script

1. Run app on seed docs
2. See top words printed
3. Confirm totals match sequential mode (flag or second run)
4. Process exits without hang

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Unsynchronized HashMap sharing | ConcurrentHashMap or join-then-merge |
| Forgetting shutdown | Always shutdown + await |
| `fixed pool` size huge | Cap to files/CPUs |
| Ignoring Future exceptions | Handle ExecutionException |
| Parallel stream “just because” | Prefer explicit pool here for learning |

## After this

Next: [B — Order Processing Pipeline](B-order-processing-pipeline.md) · Topic: [05-multithreading.md](../../phase-01-core-java/05-multithreading.md)
