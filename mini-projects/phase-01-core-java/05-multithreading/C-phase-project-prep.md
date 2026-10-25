# Mini-Project C — Phase Project Prep (Background Indexer)

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/05-multithreading.md](../../phase-01-core-java/05-multithreading.md)  
**Code folder suggestion:** `projects/phase-01/05-multithreading/background-indexer/`  
**Time:** ~6–8 hours at 1 hr/day  

## Goal

Keep a console CLI responsive while a **background indexer thread** builds a search map (e.g. title token → book ids). Practice the concurrency design you’ll need for Library Manager v3 (background overdue/stats + clean shutdown). Careful shared-state design required.

## Prerequisites

- [ ] ExecutorService lifecycle
- [ ] ConcurrentHashMap or clear locking/confinement strategy
- [ ] A simple in-memory book list (can be hardcoded)

## Learn (open before coding)

- [Processes and Threads](https://docs.oracle.com/javase/tutorial/essential/concurrency/procthread.html)
- [Executors](https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html)
- [`ScheduledExecutorService`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ScheduledExecutorService.html)
- [`ConcurrentHashMap`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html)
- [Synchronization](https://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html)
- [Interrupts](https://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html)

## Project layout

```text
background-indexer/
  README.md
  THREAD-MODEL.md
  src/
    com/learn/indexer/
      app/Main.java
      model/Book.java
      index/SearchIndex.java
      index/IndexerJob.java
      service/CatalogService.java
```

## Build steps

### Step 1 — Write THREAD-MODEL.md first

- [ ] State: main thread = CLI only
- [ ] State: one background executor for indexing/refresh
- [ ] State: shared structures and how races are avoided (ConcurrentHashMap / single-thread executor / synchronized)
- [ ] No indexer code until this note exists
- **Done when:** One-page thread model note is written.

### Step 2 — Catalog without concurrency

- [ ] `List`/`Map` of books; menu Add book / List / Search (linear scan)
- [ ] Seed 20+ books
- [ ] Compile/run:
  ```bash
  javac -d out src/com/learn/indexer/**/*.java
  java -cp out com.learn.indexer.app.Main
  ```
- **Done when:** CLI add/list/search works single-threaded.

### Step 3 — SearchIndex structure

- [ ] `ConcurrentHashMap<String, Set<String>> tokenToIsbns` (or CopyOnWrite / confined map—match your model)
- [ ] Methods: `addBook(Book)`, `clear()`, `lookup(token)`
- [ ] Tokenize titles on whitespace; lowercase
- **Done when:** Manually adding one book updates index lookup.

### Step 4 — Background indexer job

- [ ] `IndexerJob` rebuilds or incrementally updates index from catalog snapshot
- [ ] Run via `Executors.newSingleThreadExecutor()` or scheduled every N seconds
- [ ] Simulate work with short sleep so CLI overlap is visible
- **Done when:** Index refreshes without you calling it from the menu action path.

### Step 5 — CLI remains responsive

- [ ] While indexer runs, menu still accepts input (type list/search during rebuild)
- [ ] Search can use index if `ready` flag true; else fall back to scan or say “index warming”
- [ ] Document UX choice
- **Done when:** You can interact during a slow rebuild.

### Step 6 — Safe publication of results

- [ ] Avoid half-built index visibility: build into a local map then atomically swap reference, **or** only mutate ConcurrentHashMap with clear semantics
- [ ] Document approach in THREAD-MODEL.md
- **Done when:** Search never throws CME / sees torn state (for your chosen model).

### Step 7 — Clean shutdown on Quit

- [ ] Quit: stop scheduler/executor; `awaitTermination`; `shutdownNow` if needed
- [ ] Process exits promptly every time
- [ ] No “index thread” left running
- **Done when:** Quit returns to shell without Force Close.

### Step 8 — Bridge notes to phase project

- [ ] List what maps to Library Manager: overdue scanner ≈ indexer job; CLI ownership; shutdown
- [ ] README compile/run + demo
- [ ] Optional: log “index rebuilt at …” quietly every refresh
- **Done when:** Bridge notes exist for Week 3 of phase project.

## Definition of Done

- [ ] THREAD-MODEL.md written before coding concurrency
- [ ] Background indexer builds search map
- [ ] CLI stays responsive
- [ ] Clean shutdown
- [ ] Phase-project bridge notes written

## Demo script

1. Start app; seed loads
2. Immediately open menu; list books while index warms
3. Search by token; see index hit (or warming message then hit)
4. Add a book; wait for refresh; search new title token
5. Quit → prompt returns

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Indexer and CLI mutate ArrayList unsafely | Snapshot, concurrent structure, or single writer thread |
| Daemon=false thread forgotten on quit | Shutdown executor on Quit |
| Searching while clear()+rebuild on plain HashMap | Atomic swap or ConcurrentHashMap carefully |
| Doing heavy index work on CLI thread | Keep menu handlers light |
| No design note | Always write thread model first |

## After this

Next topic: [06-java8-plus.md](../../phase-01-core-java/06-java8-plus.md) · Apply in [Library Manager](../phase-project/library-manager.md) Days 15–17
