# Mini-Project B — Order Processing Pipeline

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/05-multithreading.md](../../phase-01-core-java/05-multithreading.md)  
**Code folder suggestion:** `projects/phase-01/05-multithreading/order-pipeline/`  
**Time:** ~10 hours at 1 hr/day  

## Goal

Producer enqueues orders; workers validate → charge → notify (use `Thread.sleep` as fake I/O). Track metrics: processed count, failures. Optional: `CompletableFuture` per order. Clean shutdown.

## Prerequisites

- [ ] BlockingQueue, ExecutorService, synchronization/atomics basics
- [ ] Parallel word-count project recommended

## Learn (open before coding)

- [Blocking Queue](https://docs.oracle.com/javase/tutorial/essential/concurrency/collections.html) (concurrent collections section)
- [`BlockingQueue` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/BlockingQueue.html)
- [`CompletableFuture` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html)
- [Atomic Variables](https://docs.oracle.com/javase/tutorial/essential/concurrency/atomicvars.html)
- [Executors](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)
- [Guard Methods / wait notify skim](https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html) (optional)

## Project layout

```text
order-pipeline/
  README.md
  src/
    com/learn/orders/
      app/Main.java
      model/Order.java
      model/OrderStatus.java
      pipeline/OrderProducer.java
      pipeline/OrderWorker.java
      pipeline/PipelineMetrics.java
      pipeline/OrderPipeline.java
```

## Build steps

### Step 1 — Order model

- [ ] `Order`: id, customerId, amount, status (NEW/VALIDATED/CHARGED/NOTIFIED/FAILED)
- [ ] Validation rule: amount > 0; customerId non-blank
- [ ] Compile stub main
  ```bash
  javac -d out src/com/learn/orders/**/*.java
  java -cp out com.learn.orders.app.Main
  ```
- **Done when:** Invalid amount detectable in a pure method.

### Step 2 — Blocking queue + producer

- [ ] `BlockingQueue<Order> queue = new LinkedBlockingQueue<>(capacity)`
- [ ] Producer thread (or main) `put`s N sample orders (include 1–2 invalid)
- [ ] After producing, put poison-pill orders **or** use countdown + interrupt strategy—document choice
- **Done when:** Queue receives all produced orders.

### Step 3 — Worker stages with fake I/O

- [ ] Worker loop: `take` order → validate → sleep 50–100ms → charge sleep → notify sleep
- [ ] On validation fail: mark FAILED; do not charge
- [ ] Use 2–4 worker threads
- **Done when:** Valid orders reach NOTIFIED; invalid end FAILED.

### Step 4 — Metrics

- [ ] `PipelineMetrics` with `AtomicInteger` processed, failed, validated
- [ ] Increment safely from workers
- [ ] Print metrics at end
- **Done when:** processed + failed accounts for all non-poison orders.

### Step 5 — Orchestrator lifecycle

- [ ] Start producer + workers via ExecutorService
- [ ] Await producer finish; signal workers to stop; shutdown pool
- [ ] `awaitTermination`; verify exit
- **Done when:** No hang after run; metrics printed once.

### Step 6 — Optional CompletableFuture path

- [ ] Alternative mode: for each order `CompletableFuture.supplyAsync(validate).thenApply(charge).thenAccept(notify)`
- [ ] Use a common pool or custom executor
- [ ] `CompletableFuture.allOf(...).join()`
- [ ] Keep metrics updates thread-safe
- **Done when:** CF mode completes same seed with matching counts (or document differences).

### Step 7 — Backpressure / capacity demo

- [ ] Set small queue capacity; producer faster than workers
- [ ] Observe blocking `put` (log timestamps)—understand backpressure
- [ ] Note findings in README
- **Done when:** You can explain why bounded queue matters.

### Step 8 — Harden & document

- [ ] Handle InterruptedException correctly (restore interrupt flag)
- [ ] No swallowed interrupts
- [ ] README: thread diagram (producer → queue → workers), shutdown steps, how to run
- **Done when:** Demo script passes; process always exits.

## Definition of Done

- [ ] Producer/queue/workers pipeline runs
- [ ] Fake I/O sleeps present
- [ ] Metrics: processed + failures
- [ ] Clean shutdown
- [ ] Optional CF path or explicit skip noted
- [ ] README thread model

## Demo script

1. Start pipeline with 20 orders (2 invalid)
2. Watch/log stage transitions (optional verbose)
3. Final metrics: failed=2; processed=18 (or your definitions)
4. Process exits

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Unbounded queue hiding slow consumers | Bound the queue |
| Shared int++ for metrics | Use AtomicInteger |
| Swallowing InterruptedException | Restore interrupt; break loop |
| Workers never stop | Poison pill or volatile running flag + interrupt |
| Business logic in Main only | Pipeline classes |

## After this

Next: [C — Phase Project Prep](C-phase-project-prep.md) · Topic: [05-multithreading.md](../../phase-01-core-java/05-multithreading.md)
