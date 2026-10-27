# Mini-Project A — Console Unit Converter Suite

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/01-java-fundamentals.md](../../phase-01-core-java/01-java-fundamentals.md)  
**Code folder suggestion:** `projects/phase-01/01-java-fundamentals/unit-converter/`  
**Time:** ~6–8 hours at 1 hr/day  

## Goal

Build a menu-driven console converter for **temperature**, **length**, and **weight**. Use methods per conversion family, validate input, loop until quit, and organize code in a named package (`converter`) plus `Main`.

## Prerequisites

- [x] JDK installed; `java` / `javac` work from the terminal
- [x] Comfortable with types, operators, `if`/`switch`, loops, methods, `Scanner`, packages
- [x] Topic days through Week 3 (I/O & packages) largely complete



## Learn (open before coding)

- [Operators](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html)
- [switch Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html)
- [Defining Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
- `Scanner` [API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html)
- [Creating and Using Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)
- `String` [equals](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#equals(java.lang.Object))



## Project layout

```text
unit-converter/
  README.md
  src/
    converter/
      Main.java
      TemperatureConverter.java
      LengthConverter.java
      WeightConverter.java
      InputHelper.java
```



## Build steps



### Step 1 — Project skeleton & package

- [x] Create folder `projects/phase-01/01-java-fundamentals/unit-converter/src/converter/`
- [x] Add `Main.java` with `package converter;` and a stub `public static void main`
- [x] Print `"Unit Converter — starting"` once
- [x] Compile and run from project root (adjust path if needed):
  ```bash
  javac -d out src/converter/*.java
  java -cp out converter.Main
  ```

- **Done when:** Hello/stub message prints without compile errors.



### Step 2 — Main menu loop

- [x] Create a `while (true)` (or `running` flag) menu that prints:
  1. Temperature  2) Length  3) Weight  0) Quit
- [x] Read choice with `Scanner` (`nextLine` + parse preferred)
- [x] Use `switch` to route; on `0` break/exit
- [x] On unknown choice, print a clear message and re-show menu

- **Done when:** You can open the menu, pick invalid option safely, and quit.



### Step 3 — Temperature conversions

- [x] Create `TemperatureConverter` with methods such as:
  - `celsiusToFahrenheit(double c)`
  - `fahrenheitToCelsius(double f)`
  - `celsiusToKelvin(double c)` (optional but recommended)
- [x] From menu option 1, prompt for direction (C→F, F→C, …) and value
- [x] Print result with a readable format (e.g. 2 decimal places)

- **Done when:** At least C↔F works for known values (0°C → 32°F; 100°C → 212°F).



### Step 4 — Length conversions

- [x] Create `LengthConverter` with methods for meters ↔ kilometers, meters ↔ feet, inches ↔ centimeters (pick ≥3 pairs)
- [x] Keep formulas in methods; menu only prompts and prints
- [x] Document units in method names / short comments

- **Done when:** 1 meter → ~3.2808 feet (approx) and reverse path is consistent.



### Step 5 — Weight conversions

- [x] Create `WeightConverter` with kg ↔ lb, g ↔ kg, and one more (e.g. oz ↔ g)
- [x] Same pattern: pure methods + thin menu wiring

- **Done when:** 1 kg → ~2.2046 lb; invalid unit choice re-prompts.



### Step 6 — Input validation helper

- [x] Create `InputHelper` with methods like `readDouble(Scanner, String prompt)` and `readIntInRange(...)`
- [x] Reject blank / non-numeric input; re-prompt until valid
- [x] Reject negative values where they don’t make sense (length/weight); allow negatives for temperature
- [x] Replace raw `Scanner` parses in menus with these helpers

- **Done when:** Typing `abc` for a number never crashes; app re-prompts.



### Step 7 — Keep `main` thin

- [x] Move each family submenu into methods: `runTemperatureMenu`, `runLengthMenu`, `runWeightMenu`
- [x] `main` only creates `Scanner`, calls top menu, closes scanner if appropriate
- [x] Ensure no giant conversion formulas live inside `main`

- **Done when:** `Main` is mostly orchestration; converters hold formulas.



### Step 8 — Polish & README

- [ ] Add a short `README.md` with compile/run commands and sample conversions
- [ ] Walk a full session: temp → length → weight → quit
- [ ] Fix naming/style (clear method names; no public fields)

- **Done when:** Fresh clone (you) can compile, run, and convert without reading source.



## Definition of Done

- [x] Menu covers temperature, length, weight, and quit
- [x] Conversions live in dedicated classes/methods
- [x] Invalid numeric input is handled without crash
- [x] Code uses package `converter` (not default package)
- [x] Compile/run commands documented and verified



## Demo script

1. Start app → choose Temperature → C→F → enter `0` → expect `32`.
2. Choose Length → meters to feet → enter `1` → expect ~`3.28`.
3. Choose Weight → kg to lb → enter `1` → expect ~`2.20`.
4. Enter `xyz` when asked for a number → see re-prompt, not a stack trace.
5. Choose Quit → process exits cleanly.



## Common mistakes


| Mistake                           | Fix                                          |
| --------------------------------- | -------------------------------------------- |
| Using `==` for menu string labels | Prefer int menu codes or `equals`            |
| Integer division in formulas      | Use `double` operands / casts                |
| All logic in `main`               | Extract converter + helper classes           |
| Default package                   | Always `package converter;`                  |
| Crash on bad `nextDouble`         | Prefer `nextLine` + parse with try/re-prompt |




## After this

Next: [B — Expense Tracker](B-expense-tracker.md) · Topic day: continue [01-java-fundamentals.md](../../phase-01-core-java/01-java-fundamentals.md) Week 4