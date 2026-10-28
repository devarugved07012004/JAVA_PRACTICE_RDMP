# 02 — OOP Concepts

**Duration:** ~3–4 weeks (21–28 days) · **Cadence:** 1 hour/day  
**Focus:** Classes, objects, constructors, four pillars, interfaces, relationships, OOP project design

---

## 1. Why this topic is important

Object-oriented programming is how most Java business code is organized: domain entities, services, repositories, and frameworks (Spring especially) are built around classes, interfaces, and clear boundaries. OOP is not “fancy syntax”—it is a way to model real-world concepts so that change stays local, behavior has a home, and large programs remain understandable.

Mastering OOP here means Phase 1’s library project and every later Spring/JPA entity will feel natural instead of mysterious.

---



## 2. Prerequisites

- Completed [01-java-fundamentals.md](01-java-fundamentals.md) mastery checklist (≥90%)
- Comfortable with methods, arrays, packages, and console I/O
- Can run multi-file Java projects from an IDE or CLI

---



## 3. Concepts to learn (beginner → intermediate → advanced)



### Beginner


| Concept                                | What to internalize                               |
| -------------------------------------- | ------------------------------------------------- |
| Class vs object                        | Blueprint vs instance; state + behavior           |
| Fields & methods                       | Instance members; encapsulation instinct          |
| Constructors                           | Default vs parameterized; `this`                  |
| `new` lifecycle                        | Allocation + constructor run                      |
| Access modifiers                       | `private`, `public`, `protected`, package-private |
| Getters/setters                        | When useful; avoid mindless boilerplate           |
| `toString`, `equals`, `hashCode` intro | Identity vs equality (deepen later)               |




### Intermediate


| Concept                                 | What to internalize                                       |
| --------------------------------------- | --------------------------------------------------------- |
| Inheritance                             | `extends`, `super`, method overriding                     |
| Polymorphism                            | Compile-time (overloading) vs runtime (overriding)        |
| Abstraction                             | Abstract classes; “what” vs “how”                         |
| Encapsulation                           | Hide internals; expose a small API                        |
| Interfaces                              | `implements`, multiple interfaces, default/static methods |
| Composition over inheritance            | Prefer “has-a” when inheritance is forced                 |
| Association / aggregation / composition | Relationship vocabulary with code examples                |




### Advanced


| Concept                                | What to internalize                                      |
| -------------------------------------- | -------------------------------------------------------- |
| Liskov intuition                       | Subtypes must honor parent contracts                     |
| `final` classes/methods/fields         | Design for extension vs safety                           |
| Object class methods                   | Proper `equals`/`hashCode` contracts                     |
| Nested / inner / static nested classes | When they clarify ownership                              |
| Immutability patterns                  | Final fields, no setters, defensive copies (intro)       |
| SOLID at a practical level             | Especially S, O, D as daily habits (not buzzword drills) |
| Modeling domains                       | Entities, value-like objects, service classes            |


---



## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Classes, objects, encapsulation (Days 1–7)

- [x] **Day 1 — Classes & objects (**`Book`**)**
  - [x] **Learn:** [What Is an Object?](https://docs.oracle.com/javase/tutorial/java/concepts/object.html) · [Classes](https://docs.oracle.com/javase/tutorial/java/javaOO/classes.html) · [Objects](https://docs.oracle.com/javase/tutorial/java/javaOO/objects.html)
  - [x] **Practice:** Model a `Book` class and create objects
    1. Create folder `oops/day01` with `Book.java` and `Main.java`.
    2. In `Book`: fields `title`, `author`, `pages` (public for today only); a constructor taking all three; method `describe()` returning a short string.
    3. In `Main`: create two `Book` objects with different data.
    4. Print each via `describe()` and via field access.
    5. **Done when:** Two distinct books print correctly and `javac`/`java` succeed.

- [x] **Day 2 — Encapsulation & access**
  - [x] **Learn:** [Controlling Access to Members](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html) · [Declaring Member Variables](https://docs.oracle.com/javase/tutorial/java/javaOO/variables.html)
  - [x] **Practice:** Private fields with validated setters
    1. Make all `Book` fields `private`.
    2. Add getters; add `setPages(int pages)` that throws `IllegalArgumentException` if `pages <= 0`.
    3. Try `book.setPages(-1)` in `main` inside try/catch; print the message.
    4. Confirm direct `book.pages = 5` no longer compiles.
    5. **Done when:** Invalid pages are rejected and fields are only reachable via methods.

- [x] **Day 3 — Constructors & chaining**
  - [x] **Learn:** [Providing Constructors](https://docs.oracle.com/javase/tutorial/java/javaOO/constructors.html) · [Using the](https://docs.oracle.com/javase/tutorial/java/javaOO/thiskey.html) `this` [Keyword](https://docs.oracle.com/javase/tutorial/java/javaOO/thiskey.html)
  - [x] **Practice:** Overload and chain constructors
    1. Add `Book(String title)` that chains to `this(title, "Unknown", 1)`.
    2. Add `Book(String title, String author)` that chains to the full constructor with `pages = 1`.
    3. Keep the full 3-arg constructor as the “real” initializer.
    4. In `main`, construct one book with each overload and print getters.
    5. **Done when:** All three constructors compile and produce sensible defaults.

- [x] **Day 4 — Collaborating classes**
  - [x] **Learn:** [Using Objects](https://docs.oracle.com/javase/tutorial/java/javaOO/usingobject.html) · [OOP Concepts (Association idea)](https://docs.oracle.com/javase/tutorial/java/concepts/index.html)
  - [x] **Practice:** `Author` + `Book` association
    1. Create `Author.java` with `name`, `email`, constructor, getters, `toString`.
    2. Change `Book` to hold an `Author` reference (not just a String).
    3. In `main`, create one `Author`, two `Book`s pointing at that author.
    4. Print books and show both share the same author object (same name/email).
    5. **Done when:** Changing author name (via setter) is visible through both books’ prints.

- [x] **Day 5 — static vs instance (OOP)**
  - [x] **Learn:** [Understanding Class Members](https://docs.oracle.com/javase/tutorial/java/javaOO/classvars.html) · [Initializing Fields](https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html)
  - [x] **Practice:** Instance counter and static factory
    1. Add `private static int createdCount` to `Book`; increment in every constructor.
    2. Add `public static int getCreatedCount()`.
    3. Add `public static Book untitled()` returning `new Book("Untitled", new Author("N/A", ""), 1)`.
    4. Create 3 books (mix of constructors + factory); print `getCreatedCount()`.
    5. **Done when:** Count matches how many `Book` instances you constructed.

- [x] **Day 6 —** `toString`
  - [x] **Learn:** `Object` [API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html) · [The](https://docs.oracle.com/javase/tutorial/java/IandI/objectclass.html) `Object`[s Methods (Oracle)](https://docs.oracle.com/javase/tutorial/java/IandI/objectclass.html)
  - [x] **Practice:** Override `toString` for readable prints
    1. Override `toString` on `Author` and `Book` with `@Override`.
    2. Format like `Book{title='…', pages=…, author=…}`.
    3. Put books in an array; loop and `System.out.println(book)` (no manual field dumps).
    4. Compare one print before vs after override (default is `Class@hash`).
    5. **Done when:** `println(book)` shows meaningful fields, not only the default Object form.

- [x] **Day 7 — Mini domain review**
  - [x] **Learn:** Skim [Classes & Objects trail](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html)
  - [x] **Practice:** `BankAccount` with validated deposit/withdraw
    1. Create `BankAccount` with private `owner`, `balance`; constructor requiring `balance >= 0`.
    2. `deposit(double amount)` rejects `amount <= 0`.
    3. `withdraw(double amount)` rejects if amount invalid or exceeds balance.
    4. In `main`, run happy path + 2 failure paths (catch and print messages).
    5. **Done when:** Invalid ops leave balance unchanged and print clear errors.



### Week 2 — Inheritance & polymorphism (Days 8–14)

- [ ] **Day 8 — Inheritance hierarchy**
  - [ ] **Learn:** [Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html) · [Overriding and Hiding Methods](https://docs.oracle.com/javase/tutorial/java/IandI/override.html)
  - [ ] **Practice:** `Vehicle` → `Car` / `Bike`
    1. Create `Vehicle` with `brand`, `year`, constructor, `describe()`.
    2. Create `Car` (extra `doors`) and `Bike` (extra `hasBasket`) extending `Vehicle`.
    3. Subclass constructors call `super(brand, year)`.
    4. In `main`, construct one of each and print shared + specific fields.
    5. **Done when:** Both subclasses compile and reuse parent fields via `super`.

- [ ] **Day 9 — Overriding &** `super`
  - [ ] **Learn:** [Using the Keyword](https://docs.oracle.com/javase/tutorial/java/IandI/super.html) `super` · `@Override` [API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Override.html)
  - [ ] **Practice:** Override with `@Override` and `super`
    1. Override `describe()` in `Car` and `Bike`; each calls `super.describe()` then appends subclass info.
    2. Annotate every override with `@Override`.
    3. Intentionally misspell a method name once — confirm `@Override` fails compile — then fix.
    4. Print both subclass describes.
    5. **Done when:** Output includes parent + child text and all overrides are annotated.

- [ ] **Day 10 — Runtime polymorphism**
  - [ ] **Learn:** [Polymorphism](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)
  - [ ] **Practice:** Polymorphic array loop
    1. Create `Vehicle[] fleet = { new Car(...), new Bike(...), new Car(...) };`
    2. Loop with enhanced-for and call `describe()` on each `Vehicle` reference.
    3. Add abstract-feeling method `start()` on `Vehicle` (concrete for now) overridden differently in each subclass.
    4. Confirm the loop invokes subclass versions (different print text).
    5. **Done when:** One loop, three different runtime behaviors, no `instanceof` required.

- [ ] **Day 11 — Abstract classes**
  - [ ] **Learn:** [Abstract Methods and Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html)
  - [ ] **Practice:** Abstract `Shape` with concrete subclasses
    1. Create abstract `Shape` with abstract `double area()` and concrete `String name()`.
    2. Implement `Circle` (`radius`) and `Rectangle` (`width`, `height`).
    3. Try `new Shape()` — confirm it does not compile.
    4. Store both in `Shape[]`, print name + area.
    5. **Done when:** Areas match hand math (πr², w×h) and abstract instantiation fails.

- [ ] **Day 12 —** `final` **& stopping inheritance**
  - [ ] **Learn:** [Writing Final Classes and Methods](https://docs.oracle.com/javase/tutorial/java/IandI/final.html)
  - [ ] **Practice:** `final` method and class lab
    1. Mark `Shape.name()` as `final`; try overriding in `Circle` — fix after seeing compile error.
    2. Create `final class CircleMath` with a static helper; try `extends CircleMath` — note the error.
    3. In notes: 3 sentences on when to stop inheritance (`final` class/method).
    4. **Done when:** Both intentional compile errors are observed and notes exist.

- [ ] **Day 13 —** `equals` **/** `hashCode`
  - [ ] **Learn:** [Comparing Objects](https://docs.oracle.com/javase/tutorial/java/IandI/objectclass.html) · `Objects` [API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Objects.html)
  - [ ] **Practice:** Implement `equals`/`hashCode` for `Book`
    1. Decide identity: two books equal if same `title` + `author.name` (document choice).
    2. Generate or hand-write `equals`/`hashCode` using `Objects.equals` / `Objects.hash`.
    3. Test: `a.equals(b)` true for same data, false for different; `a.equals(null)` false.
    4. Put equal books in a `HashSet` and print `size()` (expect 1 if truly equal).
    5. **Done when:** Equality tests pass and `HashSet` collapses duplicates.

- [ ] **Day 14 — Refactor Book domain**
  - [ ] **Learn:** Re-skim [Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html) · [Composition over inheritance (design note via OO trail)](https://docs.oracle.com/javase/tutorial/java/concepts/index.html)
  - [ ] **Practice:** Inheritance only where it fits
    1. Review Week 1 `Book`/`Author` code.
    2. Try sketching `Ebook extends Book` — list what is shared vs awkward.
    3. Either implement a thin `Ebook` **or** write 5–8 sentences explaining why composition/`format` field is better.
    4. Keep compiling code either way.
    5. **Done when:** Journal note exists justifying the choice with a concrete example.



### Week 3 — Interfaces & relationships (Days 15–21)

- [ ] **Day 15 — Interfaces as contracts**
  - [ ] **Learn:** [Defining an Interface](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html) · [Implementing an Interface](https://docs.oracle.com/javase/tutorial/java/IandI/usinginterface.html)
  - [ ] **Practice:** Three contracts, three implementors
    1. Create interfaces `Payable` (`pay(double amount)`), `Printable` (`print()`), `Storable` (`save()`).
    2. Create `Invoice implements Payable`, `Report implements Printable`, `Document implements Storable`.
    3. Call each through the interface type in `main`.
    4. **Done when:** Three interface-typed variables invoke three implementations.

- [ ] **Day 16 — Multiple interfaces**
  - [ ] **Learn:** [Interfaces as Types](https://docs.oracle.com/javase/tutorial/java/IandI/interfaceAsType.html) · [Implementing an Interface](https://docs.oracle.com/javase/tutorial/java/IandI/usinginterface.html)
  - [ ] **Practice:** One class, multiple roles
    1. Create `SmartDocument implements Printable, Storable`.
    2. Pass the same instance to method `void printIt(Printable p)` and `void storeIt(Storable s)`.
    3. In notes: one sentence per role (“as Printable it… / as Storable it…”).
    4. **Done when:** Same object satisfies both method signatures without casting.

- [ ] **Day 17 — Default methods**
  - [ ] **Learn:** [Default Methods](https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html)
  - [ ] **Practice:** Evolve an interface with `default`
    1. Add `default void printHeader()` to `Printable` that prints `"=== REPORT ==="`.
    2. Call `printHeader()` then `print()` on an existing implementor — no class changes required.
    3. Override `printHeader` in one class to customize.
    4. **Done when:** Old implementors still compile and default + override both demo.

- [ ] **Day 18 — Association / aggregation / composition**
  - [ ] **Learn:** [OOP Concepts](https://docs.oracle.com/javase/tutorial/java/concepts/index.html) · [Nested Classes (ownership feel)](https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html)
  - [ ] **Practice:** Three relationship demos
    1. **Association:** `Teacher` knows `Student` (field set from outside; both can live independently).
    2. **Aggregation:** `Department` holds `List<Teacher>` passed in / replaceable.
    3. **Composition:** `House` creates its own `Room[]` in the constructor; rooms are not shared.
    4. Print a one-line comment above each demo naming the relationship.
    5. **Done when:** All three demos run and comments name the relationship correctly.

- [ ] **Day 19 — Composition deep dive**
  - [ ] **Learn:** Revisit [Classes](https://docs.oracle.com/javase/tutorial/java/javaOO/classes.html) · [Creating and Using Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html)
  - [ ] **Practice:** `Library` → `Shelf` → `Book`
    1. `Shelf` owns a `List<Book>` created inside the shelf; `addBook` / `listBooks`.
    2. `Library` owns a `List<Shelf>` created inside the library; `addShelf` / `totalBooks`.
    3. Do not expose raw mutable lists; return sizes or copies.
    4. Seed 2 shelves, 3+ books; print total.
    5. **Done when:** Totals match seed data and lists are not public fields.

- [ ] **Day 20 — Prefer composition**
  - [ ] **Learn:** [Inheritance vs other relationships (OO concepts)](https://docs.oracle.com/javase/tutorial/java/concepts/inheritance.html)
  - [ ] **Practice:** Replace bad inheritance with composition
    1. Start with a deliberate misuse: `class Stack extends ArrayList` (or similar) — note why it’s wrong in a comment.
    2. Rewrite as `class Stack { private final Deque<String> data = new ArrayDeque<>(); }` with `push`/`pop`/`peek`.
    3. Optionally expose behavior via a small interface `LIFO`.
    4. **Done when:** Misuse version is deleted or commented out; composition version passes push/pop tests.

- [ ] **Day 21 — Design sketch**
  - [ ] **Learn:** Skim [Interfaces & Inheritance trail](https://docs.oracle.com/javase/tutorial/java/IandI/index.html)
  - [ ] **Practice:** UML-ish sketch for mini-project
    1. Choose Library **or** Shop for Week 4.
    2. On paper/notes: boxes for packages `model`, `service`, `app`.
    3. List 4–6 classes and 2–3 interfaces; draw arrows (extends / implements / uses).
    4. Write 5 bullets: what each package owns.
    5. **Done when:** Sketch + package bullets exist before writing Week 4 code.



### Week 4 — OOP project & polish (Days 22–28)

- [ ] **Day 22 — Project packages**
  - [ ] **Learn:** [Creating and Using Packages](https://docs.oracle.com/javase/tutorial/java/package/index.html) · [OO trail hub](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html)
  - [ ] **Practice:** Scaffold Library/Shop packages
    1. Create `src/com/learn/oop/{model,service,app}`.
    2. Add empty/`TODO` classes matching your Day 21 sketch (at least 3 model types).
    3. Create `app.Main` with a while-loop menu skeleton (stubs only).
    4. Compile/run from IDE or CLI with packages.
    5. **Done when:** Menu prints and quits; packages compile.

- [ ] **Day 23 — Entities & validation**
  - [ ] **Learn:** [Constructors](https://docs.oracle.com/javase/tutorial/java/javaOO/constructors.html) · [Access Control](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html)
  - [ ] **Practice:** Entities with invariants
    1. Implement core entities (e.g. `Book`, `Member` or `Product`, `Customer`) with private fields.
    2. Validate in constructors/setters (non-blank ids/names, non-negative prices/stock).
    3. Override `toString` on each entity.
    4. Unit-smoke in `main`: construct valid + catch one invalid.
    5. **Done when:** Invalid construction fails loudly; valid objects print cleanly.

- [ ] **Day 24 — Thin UI, clear services**
  - [ ] **Learn:** Re-skim [Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
  - [ ] **Practice:** Move rules into `service`
    1. Create e.g. `LibraryService` / `ShopService` with methods `add…`, `list…`, `find…`.
    2. Store data in service fields (`List`/`Map`) — not in `Main`.
    3. `Main` only reads input and calls service methods.
    4. Confirm `Main` has no business `if` rules beyond menu routing.
    5. **Done when:** Add + list work via service; `Main` stays thin.

- [ ] **Day 25 — Behavior interfaces**
  - [ ] **Learn:** [Defining an Interface](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html)
  - [ ] **Practice:** `Borrowable` / `Searchable` (or shop equivalents)
    1. Define `Searchable` with `boolean matches(String query)`.
    2. Define domain behavior interface (`Borrowable` or `Purchasable`).
    3. Implement on model classes; service methods accept interface types where useful.
    4. **Done when:** At least one service method parameter is an interface type.

- [ ] **Day 26 — Polymorphic menu paths**
  - [ ] **Learn:** [Polymorphism](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)
  - [ ] **Practice:** Menu operates via interfaces
    1. Add menu option that lists all `Searchable` items matching a query.
    2. Hold mixed types in a `List<Searchable>` (or similar) and loop polymorphically.
    3. Avoid `instanceof` chains for the happy path.
    4. **Done when:** Query path works for 2+ concrete types through one interface.

- [ ] **Day 27 — Harden & rename**
  - [ ] **Learn:** [Google Java Style Guide (skim)](https://google.github.io/styleguide/javaguide.html)
  - [ ] **Practice:** Edge cases and cleanup
    1. Handle empty lists, missing ids, blank input (re-prompt or clear message).
    2. Rename unclear classes/methods; split any “god” class (>200 lines / mixed concerns).
    3. Re-run full menu walkthrough.
    4. **Done when:** Bad input doesn’t crash; names match responsibilities.

- [ ] **Day 28 — Mastery gate**
  - [ ] **Learn:** Recap [OOP Concepts](https://docs.oracle.com/javase/tutorial/java/concepts/index.html) · [Interfaces & Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/index.html)
  - [ ] **Practice:** Checklist + design decisions note
    1. Open §12 Mastery checklist; tick only what you can demo.
    2. Write a one-page journal note: inheritance used where, composition where, key interfaces.
    3. List 3 design decisions and one thing you’d change next.
    4. If checklist < 90%, note which days to repeat.
    5. **Done when:** Checklist reviewed honestly and design note exists.

---



## 5. Hands-on exercises

1. **Student & Course:** enroll, list courses, compute average grade (composition).
2. **Shape hierarchy:** abstract `Shape` → `Circle`, `Rectangle`; `area()` / `perimeter()`.
3. **Employee payroll:** `Employee` abstract/base; `Salaried`, `Hourly`; polymorphic pay calculation.
4. **Media player interfaces:** `Playable` with `play()`, `pause()`, `stop()`; `Audio`/`Video` implementations.
5. **E-commerce cart:** `Product`, `CartItem`, `Cart`; totals; cannot add invalid quantities.
6. **Zoo:** `Animal` with `makeSound()`; demonstrate runtime polymorphism.
7. **Composition kitchen:** `Order` has `List<LineItem>`; destroying order destroys line items conceptually.
8. **Aggregation classroom:** `Department` has `Teacher`s that can exist independently.
9. **Immutable-ish** `Money`**:** amount + currency; operations return new instances.
10. **Interface segregation practice:** split a fat `Machine` interface into `Printer`, `ScannerDevice`.

---



## 6. Mini-projects



### Primary: Mini Library Domain (OOP)

Console app (in-memory) with packages:

- `model`: `Book`, `Member`, `Loan`
- `service`: `LibraryService` (borrow, return, search)
- `app`: menu UI only

**Rules:**

- Encapsulate fields; validate IDs and dates (use `LocalDate` if already comfortable; else strings carefully).
- Prefer interfaces for search/borrow capabilities.
- Show at least one polymorphic call site.
- No database yet.
- **Full steps:** [A — Mini Library Domain](../mini-projects/phase-01-core-java/02-oops/A-mini-library-domain.md)



### Alternative: Mini Shop

- `Product`, `Customer`, `Order`, `OrderService`
- Discounts via strategy-like interface (`DiscountPolicy`)
- **Full steps:** [B — Mini Shop](../mini-projects/phase-01-core-java/02-oops/B-mini-shop.md)

---



## 7. Industry use cases

- **Domain models:** Orders, users, invoices, tickets—classes with invariants.
- **Framework integration:** Spring beans are objects with dependencies (composition/DI).
- **API boundaries:** Interfaces define ports (service contracts) before infrastructure exists.
- **Plugin/extension points:** Interfaces + polymorphism for payment gateways, notifiers, exporters.
- **Maintainability:** Encapsulation prevents “everyone mutates everything” bugs in teams.

---



## 8. Common mistakes


| Mistake                                      | Fix                                                                     |
| -------------------------------------------- | ----------------------------------------------------------------------- |
| Deep inheritance trees for convenience       | Prefer composition; keep hierarchies shallow                            |
| Public fields everywhere                     | Make fields private; expose behavior                                    |
| Anemic objects + all logic in `main`         | Put behavior next to data when it belongs there; services for workflows |
| Overusing getters/setters with no rules      | Enforce invariants in methods                                           |
| Incorrect `equals`/`hashCode`                | Follow the contract; include same fields                                |
| “Interface for every class” cargo cult       | Introduce interfaces at real variation/seams                            |
| God class (`LibraryManager` does everything) | Split model vs service vs UI                                            |
| Using inheritance only to reuse fields       | Shared data ≠ is-a relationship                                         |
| Ignoring `super()` constructor rules         | Understand constructor chaining                                         |
| Mutable shared objects leaking out           | Return copies or unmodifiable views later (collections topic)           |


---



## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.


| Resource                                                                                                                    | Use for                                          |
| --------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------ |
| [Oracle Tutorials — Object-Oriented Programming Concepts](https://docs.oracle.com/javase/tutorial/java/concepts/index.html) | Pillars overview                                 |
| [Oracle Tutorials — Classes & Objects](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html)                      | Core OO trail                                    |
| [Oracle Tutorials — Interfaces & Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/index.html)                | Interfaces & inheritance                         |
| [dev.java — OOP](https://dev.java/learn/)                                                                                   | Modern explanations                              |
| [Effective Java (book, not free fully)](https://www.oracle.com/java/technologies/java-books.html)                           | Optional later depth—prefer free tutorials first |


---



## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Use this as backup index.

- OOP concepts: [https://docs.oracle.com/javase/tutorial/java/concepts/index.html](https://docs.oracle.com/javase/tutorial/java/concepts/index.html)
- Classes: [https://docs.oracle.com/javase/tutorial/java/javaOO/classes.html](https://docs.oracle.com/javase/tutorial/java/javaOO/classes.html)
- Constructors: [https://docs.oracle.com/javase/tutorial/java/javaOO/constructors.html](https://docs.oracle.com/javase/tutorial/java/javaOO/constructors.html)
- Inheritance: [https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html)
- Polymorphism: [https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)
- Abstract methods/classes: [https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html)
- Interfaces: [https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html)
- `Object` API: [https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html)
- `Override` annotation: [https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Override.html](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Override.html)
- Nested classes: [https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html](https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html)
- Access control: [https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html)

---



## 11. Practice roadmap


| Stage               | Goal                         | Exit criteria                        |
| ------------------- | ---------------------------- | ------------------------------------ |
| 1. Objects          | Create/use classes fluently  | 5 small models built                 |
| 2. Encapsulation    | Invariants held              | No public mutable fields in projects |
| 3. Inheritance/poly | Override + polymorphic calls | Shape/Employee demos work            |
| 4. Interfaces       | Design by contract           | ≥2 interfaces in project             |
| 5. Relationships    | Correct has-a modeling       | Composition demo + notes             |
| 6. Project          | Layered packages             | Mini Library/Shop complete           |
| 7. Gate             | Ready for Collections        | Checklist ≥90%                       |


---



## 12. Mastery checklist

- [ ] Explains class vs object with a domain example
- [ ] Writes constructors (default, parameterized, chaining)
- [ ] Uses access modifiers intentionally
- [ ] Encapsulates state and validates updates
- [ ] Implements inheritance and overriding correctly (`@Override`)
- [ ] Demonstrates runtime polymorphism
- [ ] Uses abstract classes appropriately
- [ ] Designs and implements interfaces (including default methods awareness)
- [ ] Distinguishes association, aggregation, and composition in code
- [ ] Prefers composition when inheritance is a stretch
- [ ] Structures packages: model / service / app (or equivalent)
- [ ] Completed the OOP mini-project with a thin UI layer
- [ ] Can sketch a simple class diagram for their project
- [ ] Ready to start [03-collections.md](03-collections.md)