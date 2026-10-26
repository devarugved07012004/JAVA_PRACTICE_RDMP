# 01 — Java Fundamentals

**Duration:** ~3–4 weeks (21–28 days) · **Cadence:** 1 hour/day  
**Focus:** Setup, language basics, control flow, arrays, methods, I/O, packages, clean coding habits

---

## 1. Why this topic is important

Java fundamentals are the foundation of every later topic: OOP, collections, multithreading, Spring, and JDBC all assume you can read and write clear Java without fighting the syntax. A solid grasp of the JDK/JRE/JVM model, types, control flow, methods, and packages means you debug faster, structure code into reusable pieces, and avoid “it compiles but does the wrong thing” mistakes that waste hours later.

This phase trains a daily coding muscle: small programs, console I/O, and readable structure. That habit carries through every project in the roadmap.

---



## 2. Prerequisites

- Comfortable using a computer (files, folders, terminal/command prompt)
- Basic algebra / logic (if/else thinking)
- Text editor or IDE willingness (VS Code, IntelliJ Community, or Eclipse)
- No prior Java required

**Environment you will set up:**

- JDK 17 LTS or newer (21 LTS recommended if available)
- `java` and `javac` on PATH
- A simple project folder layout (`src`, later packages)

---



## 3. Concepts to learn (beginner → intermediate → advanced)



### Beginner


| Concept               | What to internalize                                                             |
| --------------------- | ------------------------------------------------------------------------------- |
| JDK vs JRE vs JVM     | Compile (`javac`) → bytecode → JVM runs it; JRE is runtime bits; JDK adds tools |
| Installation & setup  | Install JDK, verify versions, first `HelloWorld`                                |
| Program structure     | Class, `main`, statements, statements end with `;`                              |
| Variables & scope     | Declaration, initialization, block scope                                        |
| Primitive types       | `byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`            |
| Reference types intro | Strings are objects; `null` idea                                                |
| Operators             | Arithmetic, relational, logical, assignment, `++`/`--`, ternary                 |
| Control statements    | `if` / `else if` / `else`, `switch`                                             |
| Loops                 | `for`, `while`, `do-while`, `break`, `continue`                                 |




### Intermediate


| Concept                | What to internalize                                            |
| ---------------------- | -------------------------------------------------------------- |
| Arrays                 | 1D/2D, length, iteration, common algorithms (sum, max, search) |
| Methods                | Signature, parameters, return types, overloading, `void`       |
| Call by value          | Primitives copied; object references copy the reference        |
| String basics          | Immutability intuition, common methods, concatenation          |
| Console I/O            | `Scanner`, `System.out`, reading ints/strings safely           |
| Packages               | Package declaration, import, default package pitfalls          |
| Access modifiers intro | `public`, package-private (default) on classes/methods         |
| Coding style           | Naming, indentation, small methods, meaningful names           |




### Advanced (fundamentals depth)


| Concept                                              | What to internalize                                                   |
| ---------------------------------------------------- | --------------------------------------------------------------------- |
| Type conversion                                      | Widening vs narrowing; casting; overflow awareness                    |
| `switch` expressions / enhanced switch (modern Java) | Patterns you will see in Java 14+ codebases                           |
| Varargs (`...`)                                      | Flexible method parameters                                            |
| Static vs instance                                   | `static` methods/fields; when static is appropriate                   |
| Command-line args                                    | `String[] args`                                                       |
| Basic debugging                                      | Compile errors vs runtime errors; reading stack traces (simple cases) |
| Build from CLI                                       | Multi-file compile, classpath basics for local packages               |


---



## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Platform & first programs (Days 1–7)

- [x] **Day 1 — Install JDK & HelloWorld**
  - [x] **Learn:** [Adoptium Temurin (download JDK)](https://adoptium.net/) · [Oracle Getting Started](https://docs.oracle.com/javase/tutorial/getStarted/index.html) · [dev.java — Get started](https://dev.java/learn/)
  - [x] **Practice:** Install JDK and run HelloWorld
    1. Go to Adoptium → download **JDK 21** (or 17) for your OS → run the installer.
    2. Open a **new** terminal. Run `java -version` and `javac -version`. Both must print a version (not “command not found”).
    3. Create folder `java-fundamentals/day01`. Inside it create `HelloWorld.java`.
    4. Write a public class `HelloWorld` with `public static void main(String[] args)` that prints `Hello, Java`.
    5. In that folder run: `javac HelloWorld.java` then `java HelloWorld`.
    6. **Done when:** You see `Hello, Java` in the terminal and both version commands work.

- [x] **Day 2 — JDK / JRE / JVM**
  - [x] **Learn:** [JVM overview (Oracle)](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/) · [Compile and run workflow](https://docs.oracle.com/javase/tutorial/getStarted/cupojava/index.html)
  - [x] **Practice:** Prove compile vs run and explain the platform
    1. In notes, draw three boxes labeled JDK, JRE, JVM and write one sentence under each (what it contains / does).
    2. Open yesterday’s `HelloWorld.java`. Change the message. Save.
    3. Run `javac HelloWorld.java` again. Confirm `HelloWorld.class` timestamp updates.
    4. Run `java HelloWorld` twice without recompiling. Confirm same output both times.
    5. Write 3–5 sentences: what is bytecode, who produces it, who runs it.
    6. **Done when:** Notes diagram + paragraph exist and you can say “javac creates .class; java runs it on the JVM.”

- [x] **Day 3 — Variables & primitive types**
  - [x] **Learn:** [Variables](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/variables.html) · [Primitive Data Types](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html)
  - [x] **Practice:** Declare primitives and observe overflow
    1. Create `PrimitivesDemo.java` with a `main`.
    2. Declare and print one variable of each: `byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`.
    3. Print `Integer.MAX_VALUE` and `Integer.MIN_VALUE`.
    4. Set `int x = Integer.MAX_VALUE; x = x + 1;` and print `x` (expect wrap to negative).
    5. Try assigning a `double` literal to an `int` without cast — note the compile error, then fix with `(int)`.
    6. **Done when:** All 8 types print and you can explain why `MAX_VALUE + 1` wrapped.

- [x] **Day 4 — Operators**
  - [x] **Learn:** [Operators](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html) · [Assignment, Arithmetic, Unary](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op1.html) · [Equality, Relational, Conditional](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html)
  - [x] **Practice:** Operator worksheet in code
    1. Create `OperatorsLab.java`.
    2. Print `5 / 2` and `5 / 2.0` — write a comment explaining the difference.
    3. Demo `++` / `--`: print `a` before and after `a++` and `++a`.
    4. Demo precedence: print `2 + 3 * 4` and `(2 + 3) * 4`.
    5. Demo short-circuit: create method `boom()` that prints `"boom"` and returns `true`; evaluate `false && boom()` and `true || boom()` — confirm when `boom` runs.
    6. **Done when:** All five demos print and you noted when `boom()` is skipped.

- [x] **Day 5 — if / else**
  - [x] **Learn:** [The if-then and if-then-else Statements](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/if.html)
  - [x] **Practice:** Three small decision programs
    1. Create class `Decisions` with three static methods (hardcode inputs for now or use literals).
    2. `gradeBand(int score)` → return `"A"/"B"/"C"/"D"/"F"` using if/else if.
    3. `isOdd(int n)` → return boolean.
    4. `isLeapYear(int year)` → leap year rules (div by 400, or by 4 but not 100).
    5. In `main`, call each with 2–3 test values and print results.
    6. **Done when:** Methods have clear names and test prints look correct for your cases.

- [x] **Day 6 — switch**
  - [x] **Learn:** [The switch Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html) · [Switch Expressions (Java 14+)](https://docs.oracle.com/en/java/javase/17/language/switch-expressions.html)
  - [x] **Practice:** Classic switch + modern switch
    1. Create `DayOfWeek.java`. Read an int 1–7 (hardcoded or Scanner).
    2. Use classic `switch` with `break` to print the day name; include `default`.
    3. If JDK 14+, rewrite the same mapping as a **switch expression** assigning to a `String` and print it.
    4. Intentionally remove one `break` in classic switch, run once, then put it back — note fall-through.
    5. **Done when:** Both styles (if available) print the correct day for at least three inputs.

- [ ] **Day 7 — Week review & style**
  - [x] **Learn:** [Google Java Style Guide (skim naming)](https://google.github.io/styleguide/javaguide.html) · [Oracle Code Conventions (skim)](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
  - [x] **Practice:** Clean up Day 5–6 code
    1. Open `Decisions` and `DayOfWeek`.
    2. Rename unclear variables (`n`, `x`) to meaningful names.
    3. Extract one repeated print/test block into a helper method.
    4. Make `main` thin: only call helpers and print.
    5. Re-run both programs to confirm behavior unchanged.
    6. **Done when:** Diff shows cleaner names + thinner `main`, same outputs.



### Week 2 — Loops, arrays, methods (Days 8–14)

- [x] **Day 8 — Loops**
  - [x] **Learn:** [The while and do-while Statements](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/while.html) · [The for Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/for.html) · [Branching (break/continue)](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/branch.html)
  - [x] **Practice:** Sum 1..N three ways + star line
    1. Create `LoopsLab.java` with `sumFor(int n)`, `sumWhile(int n)`, `sumDoWhile(int n)`.
    2. Each method returns the sum of 1..n using that loop type.
    3. In `main`, print all three for `n = 10` (expect 55).
    4. Write a loop that prints `*****` (5 stars) on one line.
    5. Add one `break` demo: loop 1..20, stop when value equals 7; print values before break.
    6. **Done when:** All three sums match and star line + break demo run.

- [x] **Day 9 — Nested loops**
  - [x] **Learn:** Re-skim [for Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/for.html) (nested examples)
  - [x] **Practice:** Tables and patterns
    1. Print multiplication table 1–10 (nested for; tab-separated).
    2. Print a right triangle of `*` with 5 rows (`*`, `**`, …).
    3. Print numbers 1–20 but `continue` when divisible by 3 (skip those).
    4. Save as `NestedLoopsLab.java` and run.
    5. **Done when:** Table, triangle, and skip-by-3 output are correct.

- [x] **Day 10 — 1D arrays**
  - [x] **Learn:** [Arrays](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html) · `Arrays` [API (skim)](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Arrays.html)
  - [x] **Practice:** Array helpers
    1. Create `ArrayLab.java` with methods: `fillSample()` returns `int[]` of 8 numbers.
    2. `print(int[] a)`, `reverse(int[] a)` (new array or in-place — pick one and comment which).
    3. `max(int[] a)` and `min(int[] a)`.
    4. In `main`: fill → print → reverse → print → print max/min.
    5. **Done when:** All methods used from `main` and output matches the data.

- [x] **Day 11 — Search & frequency**
  - [x] **Learn:** [Arrays](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html) (revisit traversal)
  - [x] **Practice:** Linear search + count
    1. Add `indexOf(int[] a, int target)` → first index or `-1`.
    2. Add `count(int[] a, int target)` → how many times target appears.
    3. Test with array containing duplicates; print index and count for two targets (one missing).
    4. **Done when:** Missing target returns `-1` / `0` and present target is correct.

- [x] **Day 12 — Methods & overloading**
  - [x] **Learn:** [Defining Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html) · [Passing Information to a Method](https://docs.oracle.com/javase/tutorial/java/javaOO/arguments.html) · [Returning a Value](https://docs.oracle.com/javase/tutorial/java/javaOO/returnvalue.html)
  - [x] **Practice:** Overload `max` and thin `main`
    1. Create `max(int a, int b)` and `max(int a, int b, int c)` (overload).
    2. Move Day 10 helpers into this class or call them if same package.
    3. From `main`, call both `max` overloads and print results.
    4. Confirm: changing an `int` parameter inside a method does not change caller’s variable (print before/after).
    5. **Done when:** Overloads resolve correctly and call-by-value demo is noted.

- [x] **Day 13 — 2D arrays**
  - [x] **Learn:** [Arrays (multidimensional section)](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
  - [x] **Practice:** Small matrix ops
    1. Create `int[][] m = { {1,2,3}, {4,5,6} };`
    2. Method `sum(int[][] m)` returns total of all elements.
    3. Method `printRows` and `printColumns` (nested loops).
    4. Method `transpose` for a rectangular matrix; print result.
    5. **Done when:** Sum is 21 for sample; transpose prints 3×2 correctly.

- [x] **Day 14 — Mixed drill**
  - [x] **Learn:** [Language Basics trail hub](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/index.html) (quick recap)
  - [x] **Practice:** Five problems, no peeking at old solutions first
    1. Create `Week2Drill.java`. Solve each in its own method:
    2. Factorial of n (loop).
    3. Count primes from 1..n.
    4. Second-largest in an array.
    5. Check if array is sorted ascending.
    6. Average of a 2D matrix.
    7. **Done when:** All five methods run with at least one test each.



### Week 3 — Strings, I/O, packages (Days 15–21)

- [x] **Day 15 — String methods**
  - [x] **Learn:** [Strings](https://docs.oracle.com/javase/tutorial/java/data/strings.html) · `String` [API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html)
  - [x] **Practice:** String API drill
    1. Create `StringDrill.java` with a sample string `"Java Fundamentals"`.
    2. Print length, `charAt(0)`, `substring(0, 4)`, `toLowerCase()`, `toUpperCase()`.
    3. Compare `"java"` with `"Java"` using `==`, `equals`, `equalsIgnoreCase` — print all three results.
    4. Write one sentence in notes: why `==` is wrong for content.
    5. **Done when:** Prints match expectations and notes mention `equals`.

- [x] **Day 16 — String exercises**
  - [x] **Learn:** [Comparing Strings](https://docs.oracle.com/javase/tutorial/java/data/comparestrings.html) · [Manipulating Characters in a String](https://docs.oracle.com/javase/tutorial/java/data/manipstrings.html)
  - [x] **Practice:** Palindrome, vowels, reverse words
    1. `isPalindrome(String s)` — ignore case; test `"Racecar"` and `"hello"`.
    2. `countVowels(String s)` — count aeiouAEIOU.
    3. `reverseWords(String sentence)` — `"one two three"` → `"three two one"` (split on spaces).
    4. Call all three from `main` with printed results.
    5. **Done when:** All three methods pass your test cases.

- [x] **Day 17 — Scanner & console I/O**
  - [x] **Learn:** [I/O Streams (intro)](https://docs.oracle.com/javase/tutorial/essential/io/streams.html) · `Scanner` [API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html) · `System` [API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html)
  - [x] **Practice:** Safe console input
    1. Create `SafeInput.java`. Use `Scanner` on `System.in`.
    2. Prompt for a name with `nextLine()`.
    3. Prompt for an age: read with `nextLine()`, then `Integer.parseInt` inside try/catch (or check digits).
    4. On bad age input, print `"Invalid number, try again"` and re-prompt in a loop until valid.
    5. Print `"Hello, <name> (<age>)"`.
    6. **Done when:** Invalid age does not crash; valid path prints greeting.

- [x] **Day 18 — Menu-driven program**
  - [x] **Learn:** Reuse [switch](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html) + [while](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/while.html)
  - [x] **Practice:** Menu skeleton
    1. Create `MenuApp.java` with `boolean running = true` while-loop.
    2. Each iteration print: `1) Add  2) List  3) Quit`.
    3. Read choice; `switch` to call `add()`, `list()`, or set `running = false`.
    4. Stub `add`/`list` to print `"add stub"` / `"list stub"` for now.
    5. Run and walk through all three choices.
    6. **Done when:** Quit exits cleanly; Add/List print stubs.

- [ ] **Day 19 — Packages**
  - [x] **Learn:** [Creating and Using Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html) · [Using Package Members](https://docs.oracle.com/javase/tutorial/java/package/usepkgs.html)
  - [x] **Practice:** Multi-file package project
    1. Create folders `src/com/learn/basics/`.
    2. Put `Greeter.java` in that package with method `greet(String name)`.
    3. Put `Main.java` in the same package; call `Greeter` from `main`.
    4. Compile/run from IDE **or** CLI from `src`: `javac com/learn/basics/*.java` then `java com.learn.basics.Main`.
    5. **Done when:** Program runs with package declaration present (not default package).

- [ ] **Day 20 — static & constants**
  - [ ] **Learn:** [Understanding Class Members (static)](https://docs.oracle.com/javase/tutorial/java/javaOO/classvars.html) · [More on classes](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html)
  - [ ] **Practice:** Utility class
    1. Create `com.learn.basics.MathUtils` with `public static final double PI = 3.14159;`
    2. Add `static int clamp(int value, int min, int max)`.
    3. From `Main`, print `MathUtils.PI` and results of clamp for three cases (below min, in range, above max).
    4. **Done when:** No `MathUtils` instance created; all calls are static.

- [x] **Day 21 — Coding best practices**
  - [x] **Learn:** [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) · [Oracle Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
  - [x] **Practice:** Refactor one earlier program
    1. Pick Day 17 or Day 18 source.
    2. Rename files/classes to clear names if needed.
    3. Extract methods until `main` is ≤10 lines.
    4. Remove unused variables; keep comments only for non-obvious “why”.
    5. Re-run; confirm same behavior.
    6. **Done when:** Before/after notes list what you improved.



### Week 4 — Consolidation & mini-projects (Days 22–28)

- [ ] **Day 22 — Command-line args**
  - [ ] **Learn:** [Command-Line Arguments](https://docs.oracle.com/javase/tutorial/essential/environment/cmdLineArgs.html) · `javac`[/](https://docs.oracle.com/en/java/javase/17/docs/specs/man/index.html)`java` [tools](https://docs.oracle.com/en/java/javase/17/docs/specs/man/index.html)
  - [ ] **Practice:** CLI calculator
    1. Create `CliCalc.java`. Expect args like: `add 3 4` or `mul 2 5`.
    2. If `args.length != 3`, print usage `java CliCalc <add|sub|mul|div> <a> <b>` and exit.
    3. Parse two numbers; switch on operation; print result.
    4. Run from terminal with good args and bad args.
    5. **Done when:** Good path prints result; bad path prints usage (no crash).

- [ ] **Day 23 — Casting & overflow**
  - [ ] **Learn:** [Primitive Data Types (conversions)](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) · [Conversion and Casting](https://docs.oracle.com/en/java/javase/specs/jls/se17/html/jls-5.html) (skim)
  - [ ] **Practice:** Safe conversion helpers
    1. Show widening: assign `int` to `long` (no cast) and print.
    2. Show narrowing: cast `double` 3.9 to `int` and print (truncation).
    3. Write `safeToByte(int n)` that returns `Byte`/`null` or throws if out of byte range — your choice, document it.
    4. Test with 100, 200, -200.
    5. **Done when:** Tests show which values are accepted vs rejected.

- [ ] **Day 24 — Mini-project A (start)**
  - [ ] **Learn:** Skim your notes + [Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)
  - [ ] **Practice:** Unit Converter — core path
    1. Create package `converter` with `Main` and `TemperatureConverter`.
    2. Implement C→F and F→C methods.
    3. Menu: `1) C to F  2) F to C  0) Quit`.
    4. Hardcode or read one value; print converted result.
    5. **Done when:** Both conversions work for at least one input each; quit works.

- [ ] **Day 25 — Mini-project A (finish)**
  - [ ] **Learn:** `Scanner` [API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html) (validation patterns)
  - [ ] **Practice:** Harden converter
    1. Add length or weight family (at least one more: e.g. km↔miles).
    2. Validate numeric input (re-prompt on bad input).
    3. Keep looping until quit.
    4. Ensure `main` only orchestrates; conversion math stays in helper classes.
    5. **Done when:** Invalid input doesn’t crash; two conversion families work.

- [ ] **Day 26 — Mini-project B (start)**
  - [ ] **Learn:** Revisit [Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html) + [Arrays](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
  - [ ] **Practice:** Expense Tracker — add & list
    1. Create package `expenses` with fixed-size arrays (e.g. capacity 50) for category, amount, note.
    2. Menu: Add / List / Quit.
    3. `addExpense` stores next free slot; reject when full.
    4. `listExpenses` prints all used slots.
    5. **Done when:** You can add 2+ expenses and list them.

- [ ] **Day 27 — Mini-project B (finish)**
  - [ ] **Learn:** [Creating and Using Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)
  - [ ] **Practice:** Totals + README
    1. Add `totalAll()` and `totalByCategory(String category)`.
    2. Menu options to print those totals.
    3. Write a short `README.md` in the project folder: how to compile/run.
    4. **Done when:** Totals match manual math for your sample data; README exists.

- [ ] **Day 28 — Mastery gate**
  - [ ] **Learn:** Recap [Language Basics](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/index.html) · [Java SE API hub](https://docs.oracle.com/en/java/javase/17/docs/api/)
  - [ ] **Practice:** Checklist + rewrite
    1. Open §12 Mastery checklist; tick only what you can demo without notes.
    2. Pick one program from Days 1–7; rewrite it with packages, methods, and clear names.
    3. Run old vs new mentally — list 3 improvements you made.
    4. If checklist < 90%, note which days to repeat tomorrow.
    5. **Done when:** Checklist reviewed honestly and rewrite compiles/runs.

*If on a 3-week pace, compress Weeks 3–4 — keep numbered Practice steps; do fewer pattern drills.*

---



## 5. Hands-on exercises

> Extra drills beyond daily §4. Prefer §4 first; use these on Day 5 deepen / catch-up days.



### Warm-ups

- [ ] Celsius ↔ Fahrenheit converter — [Operators](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html)
- [ ] Swap two numbers (with/without temp) — [Variables](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/variables.html)
- [ ] Factorial (loop), Fibonacci (first N) — [for Statement](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/for.html)
- [ ] Prime check; print primes up to N — [while / do-while](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/while.html)
- [ ] Count digits; reverse an integer (watch overflow) — [Primitive Data Types](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html)
- [ ] Simple / compound interest calculators — [Operators](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op1.html)



### Arrays & methods

- [ ] Second-largest in an array — [Arrays](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
- [ ] Remove duplicates from a sorted array — [Arrays](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
- [ ] Merge two sorted arrays — [Defining Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
- [ ] Matrix addition and transpose — [Arrays (2D)](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
- [ ] Character frequency in a string — `String` [API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html)



### I/O & structure

- [ ] Student marks → average, highest, pass/fail — `[Scanner](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html)`
- [ ] Number guessing game (random + hints) — `Math` [/ random](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Math.html)
- [ ] Multi-file `MathUtils` + `Main` in a package — [Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)



### Stretch

- [ ] Tiny `+ - * /` calculator (two operands) — [Command-Line Arguments](https://docs.oracle.com/javase/tutorial/essential/environment/cmdLineArgs.html)
- [ ] Console TODO list (fixed array + menu) — [switch](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html)

---



## 6. Mini-projects



### A. Console Unit Converter Suite

- Convert length, weight, temperature between common units.
- Menu-driven; methods per conversion family.
- Validate input; loop until quit.
- Package: `converter` + `Main`.
- **Full steps:** [A — Unit Converter](../mini-projects/phase-01-core-java/01-java-fundamentals/A-console-unit-converter.md)



### B. Personal Expense Tracker (in-memory)

- Add expense (category, amount, note).
- List expenses; show total and total-by-category (arrays + parallel arrays or simple objects later in OOP).
- Persist nothing yet (console session only).
- Focus: clean methods, readable menus, no giant `main`.
- **Full steps:** [B — Expense Tracker](../mini-projects/phase-01-core-java/01-java-fundamentals/B-expense-tracker.md)



### C. (Optional stretch) Text Analyzer

- Read a paragraph (multi-line until sentinel).
- Word count, character count, longest word, word frequency (basic).
- **Full steps:** [C — Text Analyzer](../mini-projects/phase-01-core-java/01-java-fundamentals/C-text-analyzer.md)

---



## 7. Industry use cases

- **Backend services:** Every Spring endpoint method is still plain Java control flow and types.
- **Batch / CLI tools:** Internal admin scripts, data fix utilities, report generators often start as console Java.
- **Embedded logic in larger systems:** Validation rules, scoring, pricing formulas are methods + operators + conditionals.
- **Cross-platform apps:** JVM model explains why “write once, run anywhere” works in practice (with caveats).
- **Build & deploy literacy:** Understanding JDK vs runtime image helps when reading Dockerfiles and CI that install a JDK.

---



## 8. Common mistakes


| Mistake                                 | Fix                                                                 |
| --------------------------------------- | ------------------------------------------------------------------- |
| Using `==` for String content           | Use `equals` / `equalsIgnoreCase`                                   |
| Off-by-one in loops/arrays              | Trace with small N; prefer enhanced-for when read-only              |
| Not initializing variables              | Compiler forces it—listen to it                                     |
| Giant `main` methods                    | Extract methods early                                               |
| Ignoring `Scanner` newline leftovers    | Be consistent: prefer `nextLine` + parse, or clear buffer carefully |
| Default package for multi-file projects | Always use a named package                                          |
| Mixing `float` for money                | Prefer `int` cents or later `BigDecimal` (note for future)          |
| Assuming integer division is “wrong”    | `5/2 == 2` is correct for `int`; cast if you need decimal           |
| Not verifying PATH after install        | `where java` / `which java`; restart terminal                       |
| Copy-paste without understanding        | Rewrite from memory the next day                                    |


---



## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.


| Resource                                                                                                               | Use for                  |
| ---------------------------------------------------------------------------------------------------------------------- | ------------------------ |
| [Oracle Java Tutorials — Getting Started](https://docs.oracle.com/javase/tutorial/getStarted/index.html)               | Official onboarding      |
| [Oracle Java Tutorials — Learning the Language](https://docs.oracle.com/javase/tutorial/java/index.html)               | Language trail           |
| [The Java™ Tutorials — Essential Classes (I/O intro)](https://docs.oracle.com/javase/tutorial/essential/io/index.html) | I/O overview             |
| [OpenJDK](https://openjdk.org/) / [Adoptium Temurin](https://adoptium.net/)                                            | Free JDK builds          |
| [dev.java — Learn](https://dev.java/learn/)                                                                            | Modern free learning hub |
| [Java Language Specification (skim later)](https://docs.oracle.com/javase/specs/)                                      | Precision when confused  |


---



## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- JDK download / versions: [https://adoptium.net/](https://adoptium.net/)
- Oracle JDK docs hub: [https://docs.oracle.com/en/java/javase/](https://docs.oracle.com/en/java/javase/)
- Java SE API (e.g. 17): [https://docs.oracle.com/en/java/javase/17/docs/api/](https://docs.oracle.com/en/java/javase/17/docs/api/)
- Language tutorial trail: [https://docs.oracle.com/javase/tutorial/java/nutsandbolts/index.html](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/index.html)
- Arrays: [https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
- Packages: [https://docs.oracle.com/javase/tutorial/java/package/index.html](https://docs.oracle.com/javase/tutorial/java/package/index.html)
- `Scanner`: [https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html)
- `String`: [https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html)
- Tools (`javac`, `java`): [https://docs.oracle.com/en/java/javase/17/docs/specs/man/index.html](https://docs.oracle.com/en/java/javase/17/docs/specs/man/index.html)
- Google Java Style: [https://google.github.io/styleguide/javaguide.html](https://google.github.io/styleguide/javaguide.html)

---



## 11. Practice roadmap


| Stage                | Goal                                 | Exit criteria              |
| -------------------- | ------------------------------------ | -------------------------- |
| 1. Setup             | JDK works; HelloWorld from CLI       | Can explain compile/run    |
| 2. Types & operators | No confusion on primitives vs String | 10 small programs done     |
| 3. Control flow      | Loops/`switch` without notes         | Solve 15 mixed problems    |
| 4. Arrays & methods  | Decompose problems into methods      | 2D + overload demos        |
| 5. I/O & packages    | Menu apps in packages                | Multi-file project runs    |
| 6. Style             | Readable code                        | Mini-projects A+B complete |
| 7. Gate              | Ready for OOP                        | Mastery checklist ≥90%     |


**Weekly rhythm:** Mon–Fri new skills; Sat mixed drills; Sun refactor + notes cleanup.

---



## 12. Mastery checklist

- [ ] JDK installed; `java` and `javac` versions match expectations
- [ ] Can explain JDK vs JRE vs JVM in plain language
- [ ] Comfortable with all primitive types and when to use each
- [ ] Uses operators and understands integer division / precedence
- [ ] Writes `if`/`else` and `switch` confidently
- [ ] Uses `for`, `while`, `do-while`, `break`, `continue` correctly
- [ ] Creates and traverses 1D and 2D arrays
- [ ] Writes methods with parameters, returns, and overloading
- [ ] Reads console input safely with `Scanner`
- [ ] Uses `String` comparisons and common methods correctly
- [ ] Organizes code into packages with multiple classes
- [ ] Keeps `main` thin; names variables/methods clearly
- [ ] Completed at least two mini-projects from §6
- [ ] Can debug simple compile and runtime errors independently
- [ ] Ready to start [02-oops.md](02-oops.md)