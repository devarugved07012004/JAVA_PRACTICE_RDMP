# Phase 2 Project — Student / Course Registry (PostgreSQL + JDBC)

**Duration:** ~1–2 weeks (after topics 07–08) · **Cadence:** 1 hour/day  
**Outcome:** A PostgreSQL schema for students, courses, and enrollments, plus a Java JDBC repository layer with real transactions, CRUD, and multi-table reports

---

## 1. Why this topic is important

Topics 07 and 08 teach SQL and JDBC in pieces. This project forces them to work together: **schema design → seed data → Java repositories → transactional use-cases → reports**. That is the same shape as production data access before ORMs appear.

Finishing this project is the Phase 2 gate. It also seeds later work: the same domain can grow an HTTP API (Phase 3) and Spring/JPA persistence (Phase 4) without throwing away your model or rules.

---

## 2. Prerequisites

- Topics [07](07-sql-postgresql.md) and [08](08-jdbc.md) mastery checklists ≥90% each
- Local PostgreSQL running; comfortable with `.sql` scripts and `psql`
- JDBC + HikariCP (or equivalent free pool) working in a small sample app
- Phase 1 Java fluency: packages, OOP, exceptions, try-with-resources
- Git optional but recommended for personal snapshots (learning only)

**Do not start** until you can commit/rollback a JDBC transaction and write a multi-table join in pure SQL without guessing.

---

## 3. Concepts to learn (beginner → intermediate → advanced)

This project **applies** 07–08; new learning is integration and product-shaped design.

### Beginner (Project v1 — schema + read/write)

| Focus | Apply |
|-------|--------|
| Schema | `students`, `courses`, `enrollments` with PK/FK/unique |
| SQL scripts | `schema.sql`, `seed.sql`, ordered migrate-by-hand |
| JDBC CRUD | DAO per aggregate root |
| Config | Env-based JDBC URL/user/password |
| Console UI | Thin menu over services |

### Intermediate (Project v2 — transactions + reports)

| Focus | Apply |
|-------|--------|
| Service layer | `EnrollmentService` owns transaction boundaries |
| Invariants | No duplicate enrollment; optional course capacity |
| Multi-table queries | Roster, student schedule, enrollment counts |
| Exceptions | Not-found, conflict, validation → clear messages |
| Pooling | Single shared `DataSource` |

### Advanced (Project v3 — polish + Phase 3/4 readiness)

| Focus | Apply |
|-------|--------|
| Interfaces | `StudentRepository`, `CourseRepository`, `EnrollmentRepository` |
| Transaction template | Helper that runs work with commit/rollback |
| Auditing (optional) | Trigger or JDBC-written audit row in same TX |
| Callable (optional) | Capacity check or GPA function from Java |
| Docs | README + ER diagram + sample demo script |
| Future hooks | Notes on REST resources and JPA entity mapping |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Schema + JDBC vertical slice (Days 1–7)

- [ ] **Day 1 — Schema & ER**
  - [ ] **Learn:** [CREATE TABLE](https://www.postgresql.org/docs/current/sql-createtable.html) · [Constraints](https://www.postgresql.org/docs/current/ddl-constraints.html)
  - [ ] **Practice:** Create campus_registry schema and ER
    1. `CREATE DATABASE campus_registry;` then `\c campus_registry`.
    2. Write `schema.sql` with `students`, `courses`, `enrollments` (PK/FK/UNIQUE on student+course; CHECK on credits/grade as in §11 starter).
    3. Run: `psql -U postgres -d campus_registry -f schema.sql` — fix errors until clean.
    4. `\dt` and `\d enrollments` to confirm constraints.
    5. Draw ER in notes (3 entities + relationships).
    6. **Done when:** Schema applies cleanly and ER matches the tables.

- [ ] **Day 2 — Seed data**
  - [ ] **Learn:** [INSERT](https://www.postgresql.org/docs/current/sql-insert.html) · [psql](https://www.postgresql.org/docs/current/app-psql.html)
  - [ ] **Practice:** Seed realistic demo data
    1. Write `seed.sql` with ≥10 students, ≥6 courses, ≥20 enrollments (valid FKs only).
    2. Run `-f seed.sql` against `campus_registry`.
    3. Verify: `SELECT COUNT(*) FROM students;` / `courses` / `enrollments`.
    4. Spot-check one join: student name + course title for one enrollment.
    5. **Done when:** Counts meet minima and the sample join returns rows.

- [ ] **Day 3 — Project skeleton & HikariCP**
  - [ ] **Learn:** [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP/wiki/Configuration) · [DataSource](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/javax/sql/DataSource.html) · [PostgreSQL JDBC Connecting](https://jdbc.postgresql.org/documentation/use/)
  - [ ] **Practice:** Maven/Gradle app with pooled DataSource
    1. Create project; add `postgresql` + `HikariCP` dependencies.
    2. Packages: `config`, `model`, `repository`, `service`, `app`, `exception`.
    3. Implement `DataSourceFactory` reading `jdbc:postgresql://localhost:5432/campus_registry` from env.
    4. Smoke: `dataSource.getConnection()` + `SELECT 1` in try-with-resources.
    5. Add `.gitignore` for local secrets; commit only `*.example` config.
    6. **Done when:** `SELECT 1` works through HikariCP and package folders exist.

- [ ] **Day 4 — StudentRepository JDBC**
  - [ ] **Learn:** [PreparedStatement](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html) · [Retrieving results](https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html) · [Try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html)
  - [ ] **Practice:** Student model + JDBC repository
    1. Create `Student` model matching columns.
    2. Interface `StudentRepository`: `insert`, `findById`, `findAll`, `updateEmail`.
    3. Implement `JdbcStudentRepository` with `PreparedStatement` + try-with-resources; return generated keys on insert.
    4. From a small test `main`/demo class, insert one student and `findById`.
    5. **Done when:** Inserted student is readable by id from Java.

- [ ] **Day 5 — CourseRepository & counts**
  - [ ] **Learn:** [Joins](https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-JOIN) · [Aggregates](https://www.postgresql.org/docs/current/tutorial-agg.html) · [PreparedStatement](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html)
  - [ ] **Practice:** Course CRUD plus enrollment counts
    1. `Course` model + `CourseRepository` CRUD (insert/find/update/list).
    2. Add method `listWithEnrollmentCounts()` using join/aggregate SQL (`LEFT JOIN` + `COUNT` + `GROUP BY`).
    3. Map to a DTO/record `CourseSummary(id, code, title, enrollmentCount)`.
    4. Print summaries; confirm a zero-enrollment course shows `0`.
    5. **Done when:** CRUD works and counts match `psql` for at least two courses.

- [ ] **Day 6 — EnrollmentRepository**
  - [ ] **Learn:** [SQLException](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/SQLException.html) · [Postgres error codes](https://www.postgresql.org/docs/current/errcodes-appendix.html) · [INSERT](https://www.postgresql.org/docs/current/sql-insert.html)
  - [ ] **Practice:** Enrollment insert and lookups with conflict handling
    1. `EnrollmentRepository`: `insert`, `findByStudentId`, `findByCourseId`.
    2. Insert a valid enrollment; list by student and by course.
    3. Insert duplicate pair; catch SQLState `23505` → throw domain `ConflictException` / `AlreadyEnrolledException`.
    4. Confirm message is clear when called from demo code.
    5. **Done when:** Happy insert works and duplicate yields a domain exception.

- [ ] **Day 7 — Console menu end-to-end**
  - [ ] **Learn:** [JDBC Basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html) · [Connection](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Connection.html)
  - [ ] **Practice:** Thin console UI over repositories
    1. Build `ConsoleUI` / menu: list/add students, list/add courses, list enrollments (read paths).
    2. Wire only through repositories/services — no SQL in the UI class.
    3. Run against seed data; perform one create student + one create course from the menu.
    4. Verify new rows in `psql`.
    5. **Done when:** Menu demos seed data and creates persist in the DB.

### Week 2 — Transactions, reports, Definition of Done (Days 8–14)

- [ ] **Day 8 — EnrollmentService.enroll TX**
  - [ ] **Learn:** [Using Transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html) · [Transactions (Postgres)](https://www.postgresql.org/docs/current/tutorial-transactions.html)
  - [ ] **Practice:** Service-owned enroll transaction
    1. Create `EnrollmentService.enroll(studentId, courseId)`.
    2. Get Connection from DataSource; `setAutoCommit(false)`.
    3. Validate student and course exist (SELECT); insert enrollment; `commit()` on success.
    4. On any exception: `rollback()`; rethrow domain/wrapped error.
    5. Demo success from menu; confirm row in DB.
    6. **Done when:** Successful enroll commits and failure path is coded to rollback.

- [ ] **Day 9 — Capacity rule**
  - [ ] **Learn:** [SELECT](https://www.postgresql.org/docs/current/sql-select.html) · [Using Transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html)
  - [ ] **Practice:** Reject enroll when course is full
    1. Set a test course `capacity` to current enrollment count (or 1 with one seat filled).
    2. In the same TX as enroll: count enrollments; if `>= capacity`, throw `ConflictException` and rollback.
    3. Test success on a course with open seats.
    4. Test reject on full course; confirm no new row.
    5. **Done when:** Both success and full-reject paths verified in DB.

- [ ] **Day 10 — Unenroll & conflict paths**
  - [ ] **Learn:** [DELETE](https://www.postgresql.org/docs/current/sql-delete.html) · [Postgres error codes](https://www.postgresql.org/docs/current/errcodes-appendix.html) · [Using Transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html)
  - [ ] **Practice:** Unenroll plus already-enrolled conflict
    1. Implement `unenroll(studentId, courseId)` with TX + DELETE; commit/rollback.
    2. Re-test duplicate enroll → conflict message (constraint + domain).
    3. Unenroll once; confirm row gone; unenroll again → not-found style error.
    4. After a forced rollback test, assert counts unchanged in `psql`.
    5. **Done when:** Unenroll works and conflict/not-found paths are demonstrated.

- [ ] **Day 11 — Multi-table reports**
  - [ ] **Learn:** [Joins](https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-JOIN) · [ResultSet](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/ResultSet.html)
  - [ ] **Practice:** Roster, schedule, and empty-course reports
    1. SQL + Java: course roster (student names + `enrolled_on`) for a `courseId`.
    2. Student schedule (course titles + credits) for a `studentId`.
    3. Courses with zero enrollments (`LEFT JOIN` … `WHERE e.id IS NULL`).
    4. Expose via service/menu printouts; map to DTOs (`RosterEntry`, etc.).
    5. **Done when:** All three reports print correct data vs `psql` checks.

- [ ] **Day 12 — Callable or GPA query**
  - [ ] **Learn:** [CallableStatement](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/CallableStatement.html) · [CREATE FUNCTION](https://www.postgresql.org/docs/current/sql-createfunction.html)
  - [ ] **Practice:** GPA via function call or SQL average
    1. Option A: create/use DB function for average grade; call with `CallableStatement`.
    2. Option B: `UPDATE` a grade in enrollments; `SELECT AVG(grade)` for a student in Java.
    3. Print result next to a manual `psql` check.
    4. If skipping callable, document why in README (still ship the AVG query).
    5. **Done when:** One average/GPA path works end-to-end from Java.

- [ ] **Day 13 — README & secrets hygiene**
  - [ ] **Learn:** [PostgreSQL JDBC Connecting](https://jdbc.postgresql.org/documentation/use/) · [HikariCP](https://github.com/brettwooldridge/HikariCP)
  - [ ] **Practice:** Document setup and scrub secrets
    1. Write README: create DB, run `schema.sql`/`seed.sql`, env vars (`DB_URL`, `DB_USER`, `DB_PASSWORD`), how to run the app.
    2. Add a manual demo script (numbered clicks/commands).
    3. Confirm `.gitignore` + example config; grep for passwords.
    4. Quick rename/package cleanup for clarity.
    5. **Done when:** A teammate could run from README alone and no secrets are committed.

- [ ] **Day 14 — Definition of Done & Phase 3/4 notes**
  - [ ] **Learn:** [JDBC Basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html) · [PostgreSQL docs](https://www.postgresql.org/docs/current/)
  - [ ] **Practice:** Full DoD pass and future-hooks notes
    1. Walk §12 Definition of Done; fix any failing item.
    2. Re-run fresh-DB path once: create → schema → seed → app demo.
    3. Notes: REST resources list for Phase 3; entity/FK sketch for Phase 4.
    4. Tick Phase 2 gate boxes you honestly meet.
    5. **Done when:** DoD items pass and Phase 3/4 notes exist next to the repo.

*On a 1-week pace: ship Days 1–7 plus Day 8–11 essentials; skip optional callable/GPA.*

---

## 5. Hands-on exercises

Use these as spikes before integrating into the main app:

1. Pure SQL: roster query for `course_id = 1` (join students + enrollments).
2. Pure SQL: students not enrolled in any course (`LEFT JOIN` … `IS NULL` or `NOT EXISTS`).
3. JDBC spike: insert student + fetch generated key in one method.
4. JDBC spike: transaction that inserts enrollment then throws — confirm rollback.
5. Map a join `ResultSet` into a `RosterEntry` record list.
6. Translate Postgres `23505` (unique_violation) into `AlreadyEnrolledException`.
7. HikariCP: print pool stats once (optional) after a few operations.
8. Batch seed: insert many enrollments with `executeBatch` inside one transaction.
9. Read-only report connection usage: ensure reports do not leave open TX accidentally.
10. Sketch REST resource list for Phase 3 (`GET /courses`, `POST /enrollments`, …) in notes only.

---

## 6. Mini-projects

The phase project **is** the deliverable, shipped as progressive versions.

**Build guide (checkboxes + steps):** [Student / Course Registry (JDBC)](../mini-projects/phase-02-data/phase-project/student-course-jdbc.md)

### Feature specification

**Schema (Must)**

- [Must] `students` — id, first_name, last_name, email (unique), created_at
- [Must] `courses` — id, code (unique), title, credits, capacity (nullable or default)
- [Must] `enrollments` — id, student_id FK, course_id FK, enrolled_on, grade (nullable)
- [Must] Unique `(student_id, course_id)`
- [Must] FKs prevent orphan enrollments
- [Should] `CHECK` on credits > 0 and grade range if grades used
- [Could] `instructors` + `courses.instructor_id`

**SQL tooling (Must)**

- [Must] `schema.sql` + `seed.sql` runnable from scratch
- [Must] Documented create-database steps
- [Should] `reset.sql` or documented drop/recreate order

**Java JDBC (Must)**

- [Must] Repository interfaces + JDBC implementations
- [Must] CRUD for students and courses
- [Must] Enroll / list enrollments
- [Must] All user-derived values bound via `PreparedStatement`
- [Must] Shared pooled `DataSource`
- [Must] Credentials not hard-coded in committed source

**Transactions (Must)**

- [Must] `enroll` (and `unenroll` if implemented) uses explicit commit/rollback
- [Must] Failure path demonstrated (rollback leaves DB consistent)

**Reports (Must)**

- [Must] At least one multi-table query used from Java (roster **or** student schedule)
- [Should] Enrollment count per course
- [Could] GPA / average grade per student

**Quality (Must)**

- [Must] Layered packages: UI ≠ service ≠ repository ≠ model
- [Must] Clear domain exceptions (not raw SQLException to the console)
- [Must] README with setup + demo script
- [Should] Try-with-resources / no connection leaks under normal use

---

## 7. Industry use cases

- **SIS / LMS cores:** Student–course–enrollment is the classic academic data model
- **SaaS entitlements:** Users ↔ plans ↔ subscriptions resemble enrollments with capacity
- **Marketplace bookings:** Buyer ↔ listing ↔ reservation with uniqueness + transactions
- **Internal admin tools:** Console/CRUD over SQL is how many ops tools start
- **Migration path to Spring:** Same repositories later become `@Repository` / JPA without redesigning the domain

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Designing schema only inside Java strings | Own `schema.sql` as source of truth |
| Enroll logic split across UI + random DAO methods | One service method owns the transaction |
| Catching SQLException and printing only “error” | Preserve SQLState/cause; map to domain types |
| Auto-commit left on for multi-step enroll | `setAutoCommit(false)` for the use-case |
| Duplicate enrollment allowed | Unique constraint **and** friendly conflict handling |
| Pool created inside every DAO call | One app-scoped `DataSource` |
| Secrets in `README` or committed `application.properties` | Env vars + example `*.env.example` without secrets |
| Giant `Main` with SQL | Thin menu → service → repository |
| Reports mutating data | Read-only queries; no surprise updates |
| Skipping seed data | Always ship a demo dataset for manual testing |
| Big-bang rewrite of Phase 1 Library into this | Keep domains separate; reuse *patterns*, not forced merges |
| No DoD pass | Use §12 as a checklist, not a vibe |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [07-sql-postgresql.md](07-sql-postgresql.md) | Schema, joins, constraints refresh |
| [08-jdbc.md](08-jdbc.md) | JDBC, TX, pooling refresh |
| [PostgreSQL docs](https://www.postgresql.org/docs/current/) | SQL correctness |
| [Oracle JDBC Basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html) | Connection/TX patterns |
| [PostgreSQL JDBC docs](https://jdbc.postgresql.org/documentation/) | Driver URL & types |
| [HikariCP Wiki](https://github.com/brettwooldridge/HikariCP/wiki) | Pool config |
| Phase 1 [phase-project.md](../phase-01-core-java/phase-project.md) | Layering patterns to reuse |

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Full index:

- PostgreSQL `CREATE TABLE`: https://www.postgresql.org/docs/current/sql-createtable.html
- Constraints: https://www.postgresql.org/docs/current/ddl-constraints.html
- Transactions (Postgres tutorial): https://www.postgresql.org/docs/current/tutorial-transactions.html
- Joins: https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-JOIN
- Postgres error codes: https://www.postgresql.org/docs/current/errcodes-appendix.html
- JDBC Basics: https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html
- Connecting (JDBC tutorial): https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
- Updating tables (JDBC): https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
- Transactions (JDBC): https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html
- `Connection` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Connection.html
- `PreparedStatement` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html
- `DataSource` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/javax/sql/DataSource.html
- HikariCP: https://github.com/brettwooldridge/HikariCP
- PostgreSQL JDBC use guide: https://jdbc.postgresql.org/documentation/use/
- Try-with-resources: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html

---

## 11. Practice roadmap

### Evolution path

```text
v1 Schema + seed + JDBC CRUD
  → v2 Enrollment transactions + conflict/capacity rules + reports
    → v3 Polish, optional callable/audit, Phase 3/4 prep notes
```

### Week plan (summary)

| Week | Deliverable |
|------|-------------|
| 1 | DB scripts + Student/Course CRUD + enroll insert from Java |
| 2 | Transactional enroll/unenroll, multi-table reports, DoD + README |

### Suggested schema (starter shape)

```sql
-- Illustrative; refine types/names as you learn
CREATE TABLE students (
  id            BIGSERIAL PRIMARY KEY,
  first_name    VARCHAR(100) NOT NULL,
  last_name     VARCHAR(100) NOT NULL,
  email         VARCHAR(255) NOT NULL UNIQUE,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE courses (
  id            BIGSERIAL PRIMARY KEY,
  code          VARCHAR(20) NOT NULL UNIQUE,
  title         VARCHAR(200) NOT NULL,
  credits       INT NOT NULL CHECK (credits > 0),
  capacity      INT CHECK (capacity IS NULL OR capacity > 0)
);

CREATE TABLE enrollments (
  id            BIGSERIAL PRIMARY KEY,
  student_id    BIGINT NOT NULL REFERENCES students(id),
  course_id     BIGINT NOT NULL REFERENCES courses(id),
  enrolled_on   DATE NOT NULL DEFAULT CURRENT_DATE,
  grade         NUMERIC(5,2) CHECK (grade IS NULL OR (grade >= 0 AND grade <= 100)),
  UNIQUE (student_id, course_id)
);

CREATE INDEX idx_enrollments_course ON enrollments(course_id);
CREATE INDEX idx_enrollments_student ON enrollments(student_id);
```

### Suggested package layout

```text
com.yourname.campus/
  app/             # Main, ConsoleUI
  config/          # DataSourceFactory
  model/           # Student, Course, Enrollment, roster DTOs
  repository/      # interfaces + jdbc/
  service/         # EnrollmentService, StudentService, ...
  exception/       # NotFoundException, ConflictException, ...
```

### How this prepares for Phase 3 (Frontend & HTTP)

| Phase 2 artifact | Phase 3 direction |
|------------------|-------------------|
| `Course` / `Student` / `Enrollment` models | JSON resources in a REST contract |
| Service methods (`enroll`, `listRoster`) | HTTP handlers / API operations |
| Domain exceptions | HTTP status mapping (404, 409, 400) later |
| Console UI | Temporary; replace with HTML/JS client calling API |
| Seed data | Same DB behind a local API stub or server |

**Phase 3 hint:** Keep services free of console printing so you can put an HTTP layer in front without rewriting rules.

### How this prepares for Phase 4 (Spring / JPA)

| Phase 2 artifact | Phase 4 direction |
|------------------|-------------------|
| Tables + FKs + unique constraints | JPA entities + relationship mappings |
| Repository interfaces | Spring Data or `@Repository` JDBC/JPA impls |
| HikariCP DataSource | Boot auto-configured pool |
| Hand-written transactions | `@Transactional` (you will already know the semantics) |
| `schema.sql` / `seed.sql` | Flyway/Liquibase migrations |
| SQLException SQLState handling | Translate persistence exceptions thoughtfully |

**Phase 4 hint:** Do not rush to JPA until this JDBC version feels boring. ORMs amplify both good and bad schemas.

### Prep checklist for Phase 3/4 (write into project notes)

- [ ] List intended REST resources and methods (even if unimplemented)
- [ ] Sketch which tables become entities and relationship cardinalities
- [ ] Note which service methods must stay transactional
- [ ] Keep an ER diagram next to `schema.sql`

---

## 12. Mastery checklist

### Definition of Done (project)

- [ ] Fresh machine path works: create DB → run `schema.sql` → run `seed.sql` → run app (documented)
- [ ] Students and courses support create/list/update (as specified) via JDBC
- [ ] Enroll works end-to-end from the UI/service
- [ ] Duplicate enrollment is rejected (DB constraint + clear message)
- [ ] Transaction rollback path demonstrated and verified in the database
- [ ] At least one multi-table report works from Java (roster and/or schedule)
- [ ] All SQL parameters bound (no input string-concat)
- [ ] Connection pool in use; resources closed appropriately
- [ ] Secrets not committed; example config without real passwords provided
- [ ] Packages layered; UI does not embed SQL
- [ ] README includes features, setup, env vars, and a manual demo script
- [ ] Personal notes include Phase 3 API sketch and Phase 4 entity/migration sketch

### Phase 2 gate (from learning path)

- [ ] Topics 07–08 checklists ≥90%
- [ ] Schema scripts run; CRUD works; ≥1 multi-table query
- [ ] Transactions demonstrated (commit and rollback)
- [ ] Credentials not hard-coded in committed source

### Personal mastery

- [ ] Can explain the ER diagram and every constraint’s purpose in 2 minutes
- [ ] Can add a new report query (e.g., courses below capacity) in under an hour
- [ ] Can point to the exact method that begins/commits/rolls back a transaction
- [ ] Can explain how this project will grow into REST (Phase 3) and Spring/JPA (Phase 4)
- [ ] Ready to start Phase 3 — Frontend & HTTP ([learning path](../00-master-guide/learning-path.md))

---

## Quick reference — success criteria

You are done with Phase 2 when the campus registry meets the Definition of Done, the Phase 2 gate boxes are checked, and swapping the console for an API later would not require redesigning tables or services. Move on only when SQL and JDBC feel routine—not mysterious.
