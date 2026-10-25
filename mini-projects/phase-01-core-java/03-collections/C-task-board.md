# Mini-Project C — Task Board (Phase-Project Prep)

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/03-collections.md](../../phase-01-core-java/03-collections.md)  
**Code folder suggestion:** `projects/phase-01/03-collections/task-board/`  
**Time:** ~8 hours at 1 hr/day  

## Goal

Model tasks with priority and status; keep **queues per status column**; provide Comparator-sorted “My Day” views. Practice the collection patterns you’ll reuse in the Console Library Manager (indexes, queues, sorting).

## Prerequisites

- [ ] List / Map / Queue / Comparator comfort
- [ ] Contact Book or Warehouse recommended

## Learn (open before coding)

- [Queue Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/queue.html)
- [`ArrayDeque` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayDeque.html)
- [Object Ordering / Comparator](https://docs.oracle.com/javase/tutorial/collections/interfaces/order.html)
- [Map Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html)
- [`Enum` types](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html) (optional for Status/Priority)

## Project layout

```text
task-board/
  README.md
  src/
    com/learn/tasks/
      app/Main.java
      model/Task.java
      model/Status.java
      model/Priority.java
      service/TaskBoard.java
```

## Build steps

### Step 1 — Task model & enums

- [ ] `Status`: TODO, IN_PROGRESS, DONE (enum or constants)
- [ ] `Priority`: LOW, MEDIUM, HIGH (ordered)
- [ ] `Task`: id, title, priority, status, optional due date string/`LocalDate`
- [ ] Compile stub
  ```bash
  javac -d out src/com/learn/tasks/model/*.java src/com/learn/tasks/app/Main.java
  java -cp out com.learn.tasks.app.Main
  ```
- **Done when:** Tasks construct with default status TODO.

### Step 2 — Master map + column queues

- [ ] `Map<String, Task> byId`
- [ ] `Map<Status, Queue<Task>> columns` — initialize each status with `ArrayDeque`
- [ ] On create: put in map + offer to TODO queue
- **Done when:** Create 3 tasks; TODO column size 3.

### Step 3 — Move between columns

- [ ] `move(taskId, newStatus)`: remove from old queue, update status, offer to new queue
- [ ] Reject unknown id; reject no-op moves quietly or with message
- [ ] Keep `byId` as source of truth for task fields
- **Done when:** Move TODO→IN_PROGRESS→DONE updates columns correctly.

### Step 4 — Queue operations

- [ ] “Pull next TODO” = `poll` from TODO (FIFO) and move to IN_PROGRESS
- [ ] List column without destroying queue (iterate or copy)
- **Done when:** Pull next changes both TODO and IN_PROGRESS.

### Step 5 — Comparator “My Day” view

- [ ] Collect open tasks (TODO + IN_PROGRESS)
- [ ] Sort with `Comparator`: HIGH first, then earlier due date, then title
- [ ] Print top of sorted list as “My Day”
- [ ] Do not rely on PriorityQueue alone unless you also document rebuild rules
- **Done when:** Higher priority appears above lower on equal due dates.

### Step 6 — Search & list helpers

- [ ] Search title contains (case-insensitive) across `byId.values()`
- [ ] Count by status (simple loop or map merge)
- [ ] Menu: create / move / pull next / my day / search / board view / quit
- **Done when:** Board view prints three columns with titles only.

### Step 7 — Edge cases

- [ ] Empty TODO pull → friendly message
- [ ] Duplicate id rejected
- [ ] Blank title rejected
- [ ] Moving DONE back to TODO allowed or blocked—document rule
- **Done when:** Edge cases don’t throw uncaught exceptions.

### Step 8 — Seed + README (phase-project bridge)

- [ ] Seed 6 tasks mixed priorities/statuses
- [ ] README: how queues + map mirror “loans by member” / “books by isbn” thinking
- [ ] Note 3 ideas you’ll reuse in Library Manager
- **Done when:** Notes exist linking this drill to the phase project.

## Definition of Done

- [ ] Per-status queues + id map maintained
- [ ] Move / pull-next work
- [ ] Comparator “My Day” view works
- [ ] Menu complete; README links learning to phase project

## Demo script

1. Seed board
2. Board view — see columns
3. Pull next TODO
4. My Day — HIGH tasks first
5. Move a task to DONE
6. Search “report”
7. Quit

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Only List; scanning for “next” | Use Queue poll for FIFO columns |
| Updating status but forgetting old queue | Always remove-then-offer |
| Comparator inconsistent with equals | Keep sort keys stable and documented |
| Iterating PriorityQueue expecting sorted order | Sort a List copy for display |
| Losing tasks not in any queue | byId is mandatory source of truth |

## After this

Next topic: [04-exception-handling.md](../../phase-01-core-java/04-exception-handling.md) · Later: [phase-project Library Manager](../phase-project/library-manager.md)
