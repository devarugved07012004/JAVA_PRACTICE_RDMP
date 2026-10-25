# Mini-Project B — Personal Expense Tracker (In-Memory)

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/01-java-fundamentals.md](../../phase-01-core-java/01-java-fundamentals.md)  
**Code folder suggestion:** `projects/phase-01/01-java-fundamentals/expense-tracker/`  
**Time:** ~6–8 hours at 1 hr/day  

## Goal

Build a console expense tracker that **adds**, **lists**, and **totals** expenses (overall and by category) using arrays (or parallel arrays). Persist nothing—session only. Focus on clean methods and a readable menu; no giant `main`.

## Prerequisites

- [ ] Mini-project A recommended (menu + validation habits)
- [ ] Arrays, methods, loops, `Scanner`, packages from topic Weeks 2–3

## Learn (open before coding)

- [Arrays](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
- [Defining Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
- [for Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/for.html)
- [`Scanner` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html)
- [Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)
- [switch Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html)

## Project layout

```text
expense-tracker/
  README.md
  src/
    expenses/
      Main.java
      ExpenseStore.java
      Menu.java
      InputHelper.java
```

## Build steps

### Step 1 — Package & fixed-capacity store

- [ ] Create package `expenses` under `src/`
- [ ] Decide capacity (e.g. max 100 expenses) with parallel arrays:
  - `String[] categories`, `double[] amounts`, `String[] notes`, `int count`
- [ ] Or a single class `Expense` later—**for this topic prefer arrays / parallel arrays**
- [ ] Stub `Main` that prints package name and exits
- [ ] Compile/run:
  ```bash
  javac -d out src/expenses/*.java
  java -cp out expenses.Main
  ```
- **Done when:** Project compiles; arrays declared with a clear `count`.

### Step 2 — Add expense

- [ ] Implement `addExpense(category, amount, note)` that writes at index `count` then increments
- [ ] Reject add when `count == capacity` with a clear message
- [ ] Reject blank category; reject amount ≤ 0
- [ ] Wire menu option “Add expense”
- **Done when:** You can add 2–3 expenses and they occupy consecutive slots.

### Step 3 — List expenses

- [ ] Implement `listExpenses()` printing index, category, amount, note for `0..count-1`
- [ ] Format amounts consistently (e.g. 2 decimals)
- [ ] If `count == 0`, print “No expenses yet”
- **Done when:** List matches what you added, in order.

### Step 4 — Grand total

- [ ] Implement `double totalAll()` summing `amounts[0..count)`
- [ ] Menu option “Show total”
- [ ] Verify with a hand-calculated example (e.g. 10.50 + 3.25 = 13.75)
- **Done when:** Printed total matches sum of listed amounts.

### Step 5 — Total by category

- [ ] Implement `double totalByCategory(String category)` (case-insensitive match with `equalsIgnoreCase`)
- [ ] Menu: prompt for category → print total (0 if none)
- [ ] Optional stretch: print a breakdown for each distinct category found so far
- **Done when:** Food vs Transport totals differ correctly on sample data.

### Step 6 — Input helpers & menu shell

- [ ] Extract `InputHelper` for safe int/double/line reads
- [ ] Put menu loop in `Menu` or methods on `Main`—keep `main` thin
- [ ] Options: Add / List / Total / Total by category / Quit
- **Done when:** Bad numbers re-prompt; quit exits cleanly.

### Step 7 — Sample seed for demos

- [ ] Optional `seedSampleData()` called once at start (or menu “Load samples”)
- [ ] Seed ≥5 expenses across ≥3 categories
- [ ] Document sample categories in README
- **Done when:** Fresh run can demo totals without typing everything.

### Step 8 — Harden edge cases

- [ ] Trim whitespace on category/note
- [ ] Decide: store money as `double` for learning, note in README that real apps use cents/`BigDecimal`
- [ ] Prevent array overflow message from being cryptic
- [ ] Full walkthrough; fix crashes
- **Done when:** Empty store, full store, and bad input all behave calmly.

## Definition of Done

- [ ] Add / list / grand total / total-by-category work
- [ ] Capacity limit handled
- [ ] No persistence required; session-only is fine
- [ ] Methods are clear; `main` is not a wall of logic
- [ ] README with compile/run + sample session

## Demo script

1. Add: Food / 12.50 / “lunch”
2. Add: Transport / 40 / “metro card”
3. Add: Food / 5.25 / “coffee”
4. List → three rows
5. Total → `57.75`
6. Total by category `Food` → `17.75`
7. Quit

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Forgetting to increment `count` | Increment only after a successful write |
| Looping to `array.length` instead of `count` | Always use `count` as live size |
| Using `==` for category strings | Use `equals` / `equalsIgnoreCase` |
| Mixing money as int and double inconsistently | Pick one; format on print |
| Giant `main` | Extract store + menu methods |

## After this

Optional stretch: [C — Text Analyzer](C-text-analyzer.md) · Or start [02-oops.md](../../phase-01-core-java/02-oops.md) when fundamentals checklist ≥90%
