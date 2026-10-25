# Mini-Project A — Robust Console Calculator

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/04-exception-handling.md](../../phase-01-core-java/04-exception-handling.md)  
**Code folder suggestion:** `projects/phase-01/04-exception-handling/robust-calculator/`  
**Time:** ~6–8 hours at 1 hr/day  

## Goal

Menu-driven calculator where **invalid input never crashes** the app. Domain errors (e.g. divide by zero) become clear messages. Separate `Calculator` (throws) from `ConsoleApp` (catches/displays).

## Prerequisites

- [ ] try/catch/finally, throw, custom exceptions basics
- [ ] Clean methods and packages from earlier phases

## Learn (open before coding)

- [Exceptions trail](https://docs.oracle.com/javase/tutorial/essential/exceptions/index.html)
- [Catching and Handling](https://docs.oracle.com/javase/tutorial/essential/exceptions/handling.html)
- [How to Throw Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/throwing.html)
- [Creating Exception Classes](https://docs.oracle.com/javase/tutorial/essential/exceptions/creating.html)
- [The catch Blocks](https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html)

## Project layout

```text
robust-calculator/
  README.md
  src/
    com/learn/calc/
      app/ConsoleApp.java
      app/Main.java
      engine/Calculator.java
      engine/Operation.java
      exception/InvalidInputException.java
      exception/CalculationException.java
```

## Build steps

### Step 1 — Custom exceptions

- [ ] `InvalidInputException` (checked or unchecked—pick one style and stick to it)
- [ ] `CalculationException` for domain math errors (divide by zero, etc.)
- [ ] Meaningful messages in constructors
- [ ] Compile exception classes alone first
- **Done when:** You can `throw new CalculationException("divide by zero")` in a tiny test main.

### Step 2 — `Calculator` that throws

- [ ] Methods: `add`, `subtract`, `multiply`, `divide(a,b)`
- [ ] `divide`: if `b == 0` throw `CalculationException` (do **not** return a magic number)
- [ ] No console printing inside `Calculator`
- [ ] Compile:
  ```bash
  javac -d out src/com/learn/calc/exception/*.java src/com/learn/calc/engine/*.java
  ```
- **Done when:** Unit-style main calling divide(1,0) throws as expected.

### Step 3 — Parse helpers that throw

- [ ] `parseDouble(String)` → throws `InvalidInputException` on blank/non-numeric
- [ ] Optional: `parseOperation(String)` for `+ - * /`
- [ ] Keep parsing out of raw `Scanner.nextDouble` crash path
- **Done when:** `"abc"` yields your exception, not uncaught `NumberFormatException` at UI boundary (wrap/translate).

### Step 4 — Console loop catches

- [ ] `ConsoleApp` menu: choose op → read two numbers → call Calculator → print result
- [ ] `catch (InvalidInputException | CalculationException ex)` print `ex.getMessage()` and continue
- [ ] Never empty `catch`
- [ ] Quit option
- **Done when:** Bad input and /0 show messages; loop continues.

### Step 5 — Multi-catch / specific order

- [ ] Demonstrate specific catches before broader ones if you use a hierarchy
- [ ] Optionally catch `Exception` only as last resort logging “unexpected” (still message user)
- **Done when:** You can explain catch order in README one-liner.

### Step 6 — Extra operations (optional but good)

- [ ] `%` modulo with zero divisor guard
- [ ] Power / sqrt with domain checks (sqrt of negative → CalculationException)
- [ ] Same throw/catch pattern
- **Done when:** ≥1 extra op follows the same architecture.

### Step 7 — No logic leak into UI

- [ ] Grep/self-check: no `a/b` division in `ConsoleApp`
- [ ] All math in `Calculator`
- [ ] README documents the separation rule
- **Done when:** Architecture rule holds.

### Step 8 — Demo hardening pass

- [ ] Script: valid add, divide by zero, garbage input, quit
- [ ] Ensure JVM doesn’t dump stack to user for expected errors (stack OK for unexpected if you choose—document)
- [ ] Compile full app:
  ```bash
  javac -d out src/com/learn/calc/**/*.java
  java -cp out com.learn.calc.app.Main
  ```
- **Done when:** Demo script passes cleanly.

## Definition of Done

- [ ] Calculator throws; Console catches/displays
- [ ] Invalid input never kills the process
- [ ] Divide-by-zero is a clear domain message
- [ ] No empty catch blocks
- [ ] README + demo verified

## Demo script

1. Add `2` + `3` → `5`
2. Divide `8` / `0` → friendly error
3. Enter `nope` as a number → friendly error; menu returns
4. Multiply `4` * `5` → `20`
5. Quit

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Catching and ignoring | Always message or rethrow |
| Returning `Double.NaN` for errors | Throw domain exception |
| Printing inside Calculator | UI layer only |
| Catching `Exception` too early | Catch specific types first |
| Letting `nextDouble` crash | Parse strings; translate to InvalidInput |

## After this

Next: [B — Library Service Hardening](B-library-service-hardening.md) · Topic: [04-exception-handling.md](../../phase-01-core-java/04-exception-handling.md)
