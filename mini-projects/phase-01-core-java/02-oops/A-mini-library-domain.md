# Mini-Project A — Mini Library Domain (OOP)

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/02-oops.md](../../phase-01-core-java/02-oops.md)  
**Code folder suggestion:** `projects/phase-01/02-oops/mini-library/`  
**Time:** ~10–12 hours at 1 hr/day  

## Goal

Build an in-memory console library with packages `model`, `service`, and `app`. Model `Book`, `Member`, `Loan`; implement borrow/return/search in `LibraryService`; keep UI thin. Prefer interfaces for search/borrow capabilities and show at least one polymorphic call site. No database.

## Prerequisites

- [ ] [01-java-fundamentals](../../phase-01-core-java/01-java-fundamentals.md) mastery ≥90%
- [ ] Classes, encapsulation, inheritance/interfaces basics from OOP Weeks 1–3

## Learn (open before coding)

- [OOP Concepts](https://docs.oracle.com/javase/tutorial/java/concepts/index.html)
- [Classes & Objects](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html)
- [Interfaces](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html)
- [Polymorphism](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)
- [Access Control](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html)
- [`LocalDate` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html) (optional but preferred for due dates)

## Project layout

```text
mini-library/
  README.md
  src/
    com/learn/library/
      app/
        Main.java
        ConsoleUI.java
      model/
        Book.java
        Member.java
        Loan.java
      service/
        LibraryService.java
        Searchable.java
        Borrowable.java
```

## Build steps

### Step 1 — Packages & menu skeleton

- [ ] Create package folders: `app`, `model`, `service`
- [ ] `Main` starts a loop: Books / Members / Loans / Search / Quit
- [ ] README with compile/run:
  ```bash
  javac -d out $(find src -name "*.java")   # Git Bash / WSL
  # PowerShell alternative: javac -d out (Get-ChildItem -Recurse src\*.java).FullName
  java -cp out com.learn.library.app.Main
  ```
- **Done when:** Menu prints and quit works.

### Step 2 — Encapsulated `Book` & `Member`

- [ ] `Book`: id/isbn, title, author, available flag; private fields; getters; validate non-blank isbn/title in constructor or factory
- [ ] `Member`: id, name, email; validate non-blank
- [ ] No public mutable fields
- [ ] Unit-smoke from a temporary main or service stubs: construct 2 books, 1 member
- **Done when:** Invalid blank title/isbn is rejected (exception or false + message—pick one and stay consistent).

### Step 3 — In-memory storage in service

- [ ] `LibraryService` holds `List<Book>`, `List<Member>`, `List<Loan>` (arrays OK if collections not started; prefer `ArrayList` if you already know it)
- [ ] Methods: `addBook`, `registerMember`, `listBooks`, `listMembers`
- [ ] UI calls service only—no book lists in `Main`
- **Done when:** Add 2 books + 1 member via menu; list shows them.

### Step 4 — `Loan` & borrow

- [ ] `Loan`: loanId, memberId, bookId/isbn, borrowedOn, dueOn, returnedOn (nullable)
- [ ] `borrow(memberId, isbn)`: find entities; reject if missing or book unavailable; create loan; set available=false
- [ ] Set due date (`LocalDate.now().plusDays(14)` or string date if needed)
- **Done when:** Successful borrow flips availability; second borrow of same book fails clearly.

### Step 5 — Return flow

- [ ] `returnBook(loanId or isbn)`: mark returnedOn; set book available=true
- [ ] Reject return if already returned / not found
- [ ] Menu options Borrow / Return
- **Done when:** Return restores availability; book can be borrowed again.

### Step 6 — Interfaces for capabilities

- [ ] Define `Searchable` with e.g. `boolean matches(String query)` and/or `String getSearchKey()`
- [ ] Implement on `Book` (and optionally `Member`)
- [ ] Define a small `BorrowService` interface or `Borrowable` if it clarifies design—or keep borrow on `LibraryService` but search via `Searchable`
- **Done when:** At least one meaningful interface is used by the service/UI.

### Step 7 — Polymorphic search call site

- [ ] Hold mixed `List<Searchable>` (books + members if both implement) or search books through `Searchable` references
- [ ] Loop polymorphically—avoid `instanceof` chains on the happy path
- [ ] Menu: Search → print matches
- **Done when:** One search path works for 2+ concrete types **or** clearly documents polymorphic book search via interface-typed list.

### Step 8 — Harden & document

- [ ] Empty lists, missing ids, blank input → clear messages, no crash
- [ ] Rename unclear methods; keep UI thin
- [ ] Write README: features, packages, demo script
- [ ] Sketch a tiny class diagram in notes (boxes + arrows is enough)
- **Done when:** Full add → register → borrow → return → search walkthrough succeeds.

## Definition of Done

- [ ] Packages: model / service / app
- [ ] Book, Member, Loan encapsulated
- [ ] Borrow + return rules enforced
- [ ] ≥1 interface + ≥1 polymorphic call site
- [ ] No database; in-memory only
- [ ] README + demo path verified

## Demo script

1. Add book ISBN `978-1` title “Clean Code”
2. Register member `M1` “Ada”
3. Borrow `M1` + `978-1` → success; list books → unavailable
4. Borrow again → rejected
5. Return → available again
6. Search “clean” → finds book
7. Quit

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Public fields on models | Private + controlled mutators |
| All logic in menu switch | Push to `LibraryService` |
| Deep inheritance for reuse | Prefer composition + interfaces |
| Silent failure on missing id | Explicit message / exception |
| UI holds the master lists | Service owns state |

## After this

Alternative practice: [B — Mini Shop](B-mini-shop.md) · Next topic: [03-collections.md](../../phase-01-core-java/03-collections.md)
