# Mini-Project C — Parallel Bulk Validator

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/06-java8-plus.md](../../phase-01-core-java/06-java8-plus.md)  
**Code folder suggestion:** `projects/phase-01/06-java8-plus/parallel-bulk-validator/`  
**Time:** ~6 hours at 1 hr/day  

## Goal

Validate many fake records with CPU-style work (checksum/hash loops). Compare **sequential vs parallel** stream throughput and document conclusions—when parallel helps and when it doesn’t.

## Prerequisites

- [ ] Streams + lambdas
- [ ] Awareness of parallel stream pitfalls from topic Week 3
- [ ] Prior concurrency topic helpful

## Learn (open before coding)

- [Parallelism (streams)](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html)
- [`BaseStream.parallel`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/BaseStream.html#parallel())
- [`Stream` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html)
- [Executors vs parallel streams (conceptual)](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)
- [Fork/Join skim](https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html)

## Project layout

```text
parallel-bulk-validator/
  README.md
  RESULTS.md
  src/
    com/learn/validate/
      app/Main.java
      model/Record.java
      service/RecordFactory.java
      service/Validator.java
      service/Benchmark.java
```

## Build steps

### Step 1 — Fake record factory

- [ ] `Record`: id, payload string, expectedChecksum (or compute both ways)
- [ ] Generate N records (start N=50_000; adjustable)
- [ ] Compile stub:
  ```bash
  javac -d out src/com/learn/validate/**/*.java
  java -cp out com.learn.validate.app.Main
  ```
- **Done when:** Factory creates N records quickly.

### Step 2 — CPU-heavy validate function

- [ ] `boolean validate(Record r)` runs a tight loop / repeated hash (e.g. iterate payload chars many times) so work is CPU-bound
- [ ] Return false for intentionally corrupt records (~1%)
- [ ] Keep function pure (no shared mutable state)
- **Done when:** Single record validate takes measurable time in a micro spot-check.

### Step 3 — Sequential stream baseline

- [ ] `records.stream().filter(v -> !validator.validate(v)).count()` (or map to results)
- [ ] Time with `System.nanoTime()`
- [ ] Print invalid count + millis
- **Done when:** Stable-ish timing over 2 runs (warm-up note OK).

### Step 4 — Parallel stream run

- [ ] Same pipeline with `.parallelStream()` or `.stream().parallel()`
- [ ] Same invalid count must match sequential
- [ ] Print millis + speedup ratio
- **Done when:** Counts match; timings recorded.

### Step 5 — Warm-up & fair compare

- [ ] Run one throwaway parallel pass for JIT warm-up
- [ ] Then measure seq vs parallel (average 3 runs optional)
- [ ] Fix thread-safety if you accidentally shared counters without atomics—prefer collecting results
- **Done when:** Methodology described in RESULTS.md.

### Step 6 — Size sweep

- [ ] Repeat for N = 1_000, 10_000, 50_000 (and optional 200_000)
- [ ] Table: N | seq ms | par ms | speedup | invalid count
- [ ] Observe overhead on small N
- **Done when:** Table has ≥3 sizes.

### Step 7 — Control experiment (light work)

- [ ] Change validate to trivial check (length > 0)
- [ ] Show parallel often **not** faster (or slower) for cheap per-element work
- [ ] Document: parallel ≠ free performance
- **Done when:** Contrast heavy vs light work in RESULTS.md.

### Step 8 — Conclusions write-up

- [ ] RESULTS.md: when you would use parallelStream vs sequential vs explicit ExecutorService
- [ ] Warn: shared mutable reduction, order sensitivity, blocking I/O inside parallel streams
- [ ] Main prints summary + points to RESULTS.md
- **Done when:** Clear written conclusion, not just numbers.

## Definition of Done

- [ ] Sequential + parallel pipelines implemented
- [ ] Invalid counts match
- [ ] Timing table for multiple N
- [ ] Light-work control experiment
- [ ] RESULTS.md conclusions written

## Demo script

1. Run with default N (CPU-heavy)
2. Show matching invalid counts
3. Show speedup (or lack) printed
4. Open RESULTS.md table

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Parallel + shared `ArrayList.add` | Use collect / Concurrent structures / avoid shared mutability |
| Timing includes huge print loops | Time only validation pipeline |
| Declaring “parallel always wins” | Show small-N / light-work counterexamples |
| Blocking I/O inside parallelStream | Keep CPU-only for this experiment |
| No warm-up | Mention JIT; optional warm-up pass |

## After this

Complete [06-java8-plus.md](../../phase-01-core-java/06-java8-plus.md) mastery checklist · Start [Library Manager phase project](../phase-project/library-manager.md)
