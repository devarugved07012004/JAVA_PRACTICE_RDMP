# 08 — JDBC

**Duration:** ~2 weeks (14 days) · **Cadence:** 1 hour/day  
**Focus:** JDBC architecture, Connection, Statement, PreparedStatement, CallableStatement, ResultSet, transactions, batch processing, connection pooling

---

## 1. Why this topic is important

JDBC is the standard Java API for talking to relational databases. Even when you later use Spring Data JPA or jOOQ, those tools sit on JDBC (or the same ideas): open a connection, run parameterized SQL, map rows to objects, commit or roll back, close resources.

Mastering JDBC by hand teaches the failure modes that frameworks hide: leaked connections, SQL injection via string concat, missing rollbacks, and unclear transaction boundaries. The Student/Course phase project depends on this skill.

---

## 2. Prerequisites

- Topic [07-sql-postgresql.md](07-sql-postgresql.md) mastery checklist ≥90%
- Solid Java: packages, OOP, exceptions, try-with-resources ([01](../phase-01-core-java/01-java-fundamentals.md)–[04](../phase-01-core-java/04-exception-handling.md))
- Local PostgreSQL with a practice schema (students/courses/enrollments or equivalent)
- Build tool optional but recommended: Maven or Gradle for the Postgres JDBC driver dependency

**Environment:**

- JDK 17+
- PostgreSQL running locally
- JDBC driver: `org.postgresql:postgresql` (Maven Central)
- Config via environment variables or a local `db.properties` **ignored by Git** (never commit passwords)

**Minimal Maven dependency example:**

```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.7.4</version>
</dependency>
```

(Check [Maven Central](https://central.sonatype.com/) for the current patch version when you start.)

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| JDBC architecture | Application → JDBC API → Driver → Database |
| Driver & URL | Postgres URL shape: `jdbc:postgresql://host:5432/dbname` |
| `DriverManager` | `getConnection(url, user, password)` for learning apps |
| `Connection` | Session to the DB; must be closed |
| `Statement` vs `PreparedStatement` | Prefer prepared for any parameter; Statement only for fixed SQL |
| `ResultSet` | Cursor over rows; `next()`, getters by column |
| CRUD via JDBC | Insert/select/update/delete from Java |
| Try-with-resources | Auto-close Connection / Statement / ResultSet |
| `SQLException` | Checked; get message, SQLState, error code |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Parameter binding | `setInt`, `setString`, `setObject`; never concatenate user input into SQL |
| DAO / repository pattern | Isolate SQL behind interfaces (`StudentDao`, etc.) |
| Mapping rows → objects | Manual mapping methods; null handling |
| Generated keys | `RETURN_GENERATED_KEYS` after insert |
| Transactions | `setAutoCommit(false)`; `commit()` / `rollback()` |
| Transaction boundaries | One use-case = one transaction (e.g., enroll) |
| `CallableStatement` | Call Postgres functions/procedures with `?` params |
| Batch processing | `addBatch` / `executeBatch` for bulk inserts/updates |
| Query vs update | `executeQuery` vs `executeUpdate` vs `execute` |

### Advanced (practical)

| Concept | What to internalize |
|--------|----------------------|
| Connection pooling | Why `DriverManager` per request does not scale; pool concepts |
| HikariCP (free) | Industry-common pool; configure max size, timeouts |
| Isolation levels (awareness) | Read committed vs others; dirty/non-repeatable/phantom at a high level |
| Savepoints | Partial rollback inside a transaction (know they exist) |
| Soft failures | Translate SQLState (e.g., unique_violation `23505`) to domain exceptions |
| Config hygiene | Env vars / external props; `.gitignore` secrets |
| Layering for Phase 4 | Same repository interfaces later backed by JPA |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Core JDBC & CRUD (Days 1–7)

- [ ] **Day 1 — Driver, config & first connection**
  - [ ] **Learn:** [Connecting to the Database](https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html) · [PostgreSQL JDBC — Connecting](https://jdbc.postgresql.org/documentation/use/) · [DriverManager](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/DriverManager.html)
  - [ ] **Practice:** Connect with DriverManager and print version
    1. Create a Maven/Gradle project; add dependency `org.postgresql:postgresql` (e.g. 42.7.x).
    2. Put URL/user/password in env vars or untracked `db.properties` (`jdbcUrl=jdbc:postgresql://localhost:5432/learn_sql`).
    3. In `ConnectionDemo`, load config; call `DriverManager.getConnection(url, user, password)`.
    4. Inside try-with-resources on `Connection`, run `SELECT version()` via `PreparedStatement` / `ResultSet` and print it.
    5. Confirm connection closes (no extra open sessions left hanging in notes/checklist).
    6. **Done when:** Java prints a Postgres version string and secrets are not in committed source.

- [ ] **Day 2 — JDBC architecture**
  - [ ] **Learn:** [JDBC Overview](https://docs.oracle.com/javase/tutorial/jdbc/overview/index.html) · [JDBC Basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
  - [ ] **Practice:** Diagram the call path for one query
    1. In notes, draw boxes: Application → JDBC API → PostgreSQL Driver → Database.
    2. Label which types you used yesterday (`DriverManager`, `Connection`, `PreparedStatement`, `ResultSet`).
    3. Write 3–5 sentences: who loads the driver, who owns the TCP session, who parses SQL.
    4. List two failure modes (wrong URL, wrong password) and which layer surfaces them.
    5. **Done when:** Diagram + paragraph exist and you can narrate one SELECT end-to-end.

- [ ] **Day 3 — PreparedStatement SELECT & mapping**
  - [ ] **Learn:** [PreparedStatement](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html) · [Retrieving results](https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html) · [ResultSet](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/ResultSet.html)
  - [ ] **Practice:** Map students ResultSet to a Student type
    1. Create `Student` record/class: `id`, `firstName`, `lastName`, `email`.
    2. SQL: `SELECT id, first_name, last_name, email FROM students ORDER BY id`.
    3. Use try-with-resources for `Connection`, `PreparedStatement`, `ResultSet`.
    4. While `rs.next()`, map columns with `getLong`/`getString` into `Student`; collect in a `List`.
    5. Print the list from `main`.
    6. **Done when:** Printed students match `psql` row count for `students`.

- [ ] **Day 4 — INSERT & generated keys**
  - [ ] **Learn:** [Updating tables](https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html) · [Statement.RETURN_GENERATED_KEYS](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Statement.html#RETURN_GENERATED_KEYS) · [Connection](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Connection.html)
  - [ ] **Practice:** Insert student and read generated id
    1. Prepare: `INSERT INTO students (first_name, last_name, email) VALUES (?, ?, ?)`.
    2. Use `connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)`.
    3. `setString` for each `?`; `executeUpdate()`; then `getGeneratedKeys()` and read `id`.
    4. Print the new id; verify in `psql`: `SELECT * FROM students WHERE id = <id>;`
    5. **Done when:** Java prints the id and `psql` shows the same row.

- [ ] **Day 5 — UPDATE & DELETE**
  - [ ] **Learn:** [PreparedStatement](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html) · [SQLException](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/SQLException.html)
  - [ ] **Practice:** Parameterized update and delete with row counts
    1. `UPDATE students SET email = ? WHERE id = ?` — bind email + id; print `executeUpdate()` count (expect `1`).
    2. Verify with SELECT in Java or `psql`.
    3. `DELETE FROM students WHERE id = ?` for a row safe to remove; print count.
    4. Run DELETE again for same id; confirm count `0`.
    5. Catch `SQLException` once on purpose (bad SQL) and print `getSQLState()`.
    6. **Done when:** Update count 1, delete counts 1 then 0, and you logged one SQLState.

- [ ] **Day 6 — StudentDao interface + JDBC impl**
  - [ ] **Learn:** [Try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) · [JDBC Basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
  - [ ] **Practice:** Extract StudentDao with JDBC implementation
    1. Create interface `StudentDao` with `findAll`, `findById`, `insert`, `updateEmail`, `delete`.
    2. Implement `JdbcStudentDao` taking a way to get `Connection` (method or simple supplier for now).
    3. Move all SQL into the DAO; `main` only calls DAO methods.
    4. Ensure every method uses try-with-resources.
    5. Smoke-test all five methods from `main`.
    6. **Done when:** `main` contains no SQL strings and all DAO methods work.

- [ ] **Day 7 — CourseDao CRUD & resource audit**
  - [ ] **Learn:** [Try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) · [Connection](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Connection.html)
  - [ ] **Practice:** CourseDao CRUD and close-path audit
    1. Create `Course` model + `CourseDao` / `JdbcCourseDao` (insert, findById, findAll, update, delete).
    2. Implement parameterized SQL for each; map `ResultSet` to `Course`.
    3. Audit: search project for `.getConnection` / `DriverManager` — every path must close via try-with-resources.
    4. Run a quick CRUD script from `main` against seed data.
    5. **Done when:** Course CRUD works and you cannot find an unclosed Connection path.

### Week 2 — Transactions, callable, batch, pooling (Days 8–14)

- [ ] **Day 8 — Transactions: enroll commit**
  - [ ] **Learn:** [Using Transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html) · [Connection.setAutoCommit](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Connection.html#setAutoCommit(boolean))
  - [ ] **Practice:** Transactional enroll with commit
    1. Add `enroll(long studentId, long courseId)` on a service or enrollment DAO.
    2. `conn.setAutoCommit(false);` then `INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)`.
    3. On success `conn.commit();` in `finally` restore auto-commit if you changed it.
    4. Call enroll for a valid pair; verify row in `psql`.
    5. **Done when:** Successful enroll commits and the row is visible outside the Java process.

- [ ] **Day 9 — Rollback path**
  - [ ] **Learn:** [Using Transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html) · [Postgres error codes](https://www.postgresql.org/docs/current/errcodes-appendix.html)
  - [ ] **Practice:** Force FK failure and rollback
    1. Note enrollment count before the test.
    2. Call enroll with a nonexistent `student_id` (or course_id) inside `setAutoCommit(false)`.
    3. Catch `SQLException`; call `conn.rollback()`; print SQLState (expect FK `23503`).
    4. Re-check count in `psql` — unchanged.
    5. **Done when:** Rollback leaves DB unchanged and SQLState is recorded in notes.

- [ ] **Day 10 — CallableStatement**
  - [ ] **Learn:** [CallableStatement](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/CallableStatement.html) · [Calling stored procedures](https://docs.oracle.com/javase/tutorial/jdbc/basics/storedprocedures.html)
  - [ ] **Practice:** Call a Postgres function from Java
    1. Ensure topic-07 style function exists (e.g. `student_avg_grade(BIGINT)`); create a simple one if missing.
    2. `CallableStatement cs = conn.prepareCall("{ ? = call student_avg_grade(?) }");`
    3. `cs.registerOutParameter(1, Types.NUMERIC);` `cs.setLong(2, studentId);` `cs.execute();`
    4. Print `cs.getBigDecimal(1)`; compare to SQL `SELECT student_avg_grade(...)`.
    5. Use try-with-resources for Connection + CallableStatement.
    6. **Done when:** Java and `psql` return the same average for one student.

- [ ] **Day 11 — Batch processing**
  - [ ] **Learn:** [Using Batch Updates](https://docs.oracle.com/javase/tutorial/jdbc/basics/batch.html) · [Statement.addBatch](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Statement.html#addBatch(java.lang.String))
  - [ ] **Practice:** Batch insert enrollments
    1. Prepare insert into `enrollments` with `?` placeholders.
    2. Loop ~100 valid (student_id, course_id) pairs: `setLong`… `addBatch()` (use combinations that won’t all unique-conflict; or `ON CONFLICT DO NOTHING`).
    3. `int[] counts = ps.executeBatch();` print sum/length.
    4. Wrap in one transaction (`setAutoCommit(false)` + commit).
    5. **Done when:** Batch completes and enrollment count rises as expected in `psql`.

- [ ] **Day 12 — HikariCP DataSource**
  - [ ] **Learn:** [DataSource](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/javax/sql/DataSource.html) · [HikariCP](https://github.com/brettwooldridge/HikariCP) · [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP/wiki/Configuration)
  - [ ] **Practice:** One shared HikariCP DataSource
    1. Add HikariCP dependency. Create `DataSourceFactory` building `HikariDataSource`.
    2. Set `jdbcUrl`, `username`, `password`, `maximumPoolSize` (e.g. 5).
    3. Replace DAO `DriverManager.getConnection` with `dataSource.getConnection()`.
    4. Keep try-with-resources (close returns connection to pool).
    5. Run findAll students + courses once through the pool.
    6. **Done when:** App uses only the pool (no DriverManager in DAOs) and queries succeed.

- [ ] **Day 13 — Domain exceptions from SQLState**
  - [ ] **Learn:** [SQLException](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/SQLException.html) · [Postgres error codes](https://www.postgresql.org/docs/current/errcodes-appendix.html)
  - [ ] **Practice:** Map SQLState to domain exceptions
    1. Create `AlreadyExistsException` / `NotFoundException` (or `ConflictException` / `DataIntegrityException`).
    2. In DAO catch `SQLException`: if SQLState `23505` → conflict; `23503` → integrity/FK.
    3. From `main`/menu: trigger duplicate enrollment; trigger bad FK; print friendly messages (not raw stack only).
    4. Preserve cause: wrap with `initCause` or constructor taking `Throwable`.
    5. **Done when:** Both paths show domain messages and you know which SQLState maps where.

- [ ] **Day 14 — Mastery dry-run & config hygiene**
  - [ ] **Learn:** [PostgreSQL JDBC docs](https://jdbc.postgresql.org/documentation/) · [Connecting](https://jdbc.postgresql.org/documentation/use/)
  - [ ] **Practice:** Checklist dry-run and secret hygiene
    1. Walk §12 mastery checklist honestly.
    2. Confirm `.gitignore` covers `db.properties` / `.env`; add `db.properties.example` without real passwords.
    3. Grep project for password-looking strings; remove any committed secrets.
    4. Write a short “ready for phase project” note: packages, DataSource, TX enroll method name.
    5. **Done when:** Checklist ≥90% path clear and no secrets in source control.

*Stretch if ahead: read-only transaction hint, or a tiny integration test that starts from a known seed.*

---

## 5. Hands-on exercises

### Connection & statements

1. Connect and print `current_database()` and `current_user`.
2. Deliberately use a wrong password; catch `SQLException` and log SQLState (no password in logs).
3. Compare a static `Statement` query vs the same query as `PreparedStatement` (prefer prepared even if no params, for habit).
4. SELECT with `WHERE id = ?`; bind id from console input **only** via `setLong` / `setInt`.

### ResultSet mapping

5. Map a join query (student + course titles) into a DTO/`record` list.
6. Handle SQL `NULL` grades with `wasNull()` or `getObject`.
7. Paginate with `LIMIT ? OFFSET ?` parameters.

### Transactions

8. Transfer-style exercise: decrement one stock row, increment another; rollback if either fails.
9. Enroll + write an audit row in the same transaction; fail the audit insert and prove enroll rolled back.
10. Nest logic with a savepoint (optional): rollback to savepoint, then commit outer work.

### Callable & batch

11. Call a DB function that returns a scalar; print it.
12. Call a procedure/function with `INOUT`/`OUT` if you defined one in topic 07.
13. Batch-update many grades; check `executeBatch` return array for per-statement counts.
14. Demonstrate why string-concat SQL is dangerous: show a parameter binding that safely handles a name containing `'` .

### Pooling & structure

15. Configure HikariCP (`jdbcUrl`, `username`, `password`, `maximumPoolSize`).
16. Borrow 2 connections from the pool in sequence; confirm app still works after closes.
17. Extract `DataSourceFactory` so DAOs never see raw passwords.

### Anti-goal drills (do once, then never ship)

18. Write a bad concat-based query in a scratch file; rewrite it correctly with `?` placeholders; delete the bad version.

---

## 6. Mini-projects

> **Build guides (checkboxes + steps):** open the linked markdown before coding.

### A. JDBC Student Directory CLI

**Guide:** [A — Student Directory CLI](../mini-projects/phase-02-data/08-jdbc/A-student-directory-cli.md)

- Console menu: list / add / update email / delete student
- `StudentDao` + `JdbcStudentDao`
- Config from environment variables
- Try-with-resources everywhere
- README with run instructions

### B. Enrollment Service with Transactions

**Guide:** [B — Enrollment Transactions](../mini-projects/phase-02-data/08-jdbc/B-enrollment-transactions.md)

- Methods: `enroll`, `unenroll`, `listRoster(courseId)`, `listSchedule(studentId)`
- Single transaction per enroll/unenroll
- Domain exceptions for not-found / already-enrolled / capacity (if modeled)
- Optional: call a Postgres capacity-check function via `CallableStatement`

### C. (Optional stretch) Batch Seeder

**Guide:** [C — Batch Seeder](../mini-projects/phase-02-data/08-jdbc/C-batch-seeder.md)

- Read a CSV of students/courses
- Batch insert with periodic commit every N rows
- HikariCP-backed `DataSource`
- Idempotent re-run strategy (`ON CONFLICT DO NOTHING` in SQL)

---

## 7. Industry use cases

- **Legacy and microservice data access:** Many services still use JDBC templates or plain JDBC for hot paths
- **Batch jobs:** Nightly imports and migrations lean on batch updates and explicit transactions
- **Reporting tools:** Read-only connections + carefully parameterized SQL
- **Framework foundation:** Spring `JdbcTemplate`, JPA providers, and connection pools all assume JDBC literacy
- **Ops debugging:** Reading SQLException SQLState when production constraints fire

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| SQL string concatenation with user input | Always `PreparedStatement` parameters |
| Forgetting to close Connection | Try-with-resources; or pool + close (returns to pool) |
| Leaving `autoCommit=true` for multi-step writes | Explicit transaction around the use-case |
| Catching and ignoring `SQLException` | Log SQLState; rethrow as domain/unchecked with cause |
| Putting SQL in the UI/`main` | DAO/repository layer |
| Hard-coding passwords in source | Env vars / local untracked config |
| Holding ResultSet after Connection closes | Map to objects inside the try block |
| One giant God class “DatabaseManager” | Small DAOs + shared DataSource |
| Calling `commit` after every statement in a batch use-case | Batch inside one transaction when atomicity matters |
| Using `SELECT *` and fragile column indexes | Prefer named columns; get by label |
| Creating a new pool per request | One `DataSource` per application |
| Assuming frameworks remove the need to understand JDBC | Learn it once; debug forever faster |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Oracle JDBC Basics trail](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html) | Official JDBC learning path |
| [PostgreSQL JDBC documentation](https://jdbc.postgresql.org/documentation/) | Driver-specific URL, types, behavior |
| [HikariCP Wiki (GitHub)](https://github.com/brettwooldridge/HikariCP/wiki) | Free pooling configuration guidance |
| [Baeldung JDBC series](https://www.baeldung.com/java-jdbc) | Free practical articles (cross-check with Oracle docs) |
| Topic 07 SQL scripts | Schema you will call from Java |
| [Maven Central — PostgreSQL driver](https://central.sonatype.com/artifact/org.postgresql/postgresql) | Dependency versions |

Official Oracle + PostgreSQL JDBC docs win when tutorials disagree.

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Full index:

- JDBC Basics (Oracle tutorial): https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html
- JDBC Overview: https://docs.oracle.com/javase/tutorial/jdbc/overview/index.html
- `Connection` API (Java 17): https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Connection.html
- `PreparedStatement` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html
- `CallableStatement` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/CallableStatement.html
- `Statement` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Statement.html
- `ResultSet` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/ResultSet.html
- `DriverManager` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/DriverManager.html
- `DataSource` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/javax/sql/DataSource.html
- `SQLException` API: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/SQLException.html
- Try-with-resources: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
- PostgreSQL JDBC home: https://jdbc.postgresql.org/
- PostgreSQL JDBC docs: https://jdbc.postgresql.org/documentation/
- Connecting with JDBC (Postgres): https://jdbc.postgresql.org/documentation/use/
- HikariCP: https://github.com/brettwooldridge/HikariCP
- HikariCP configuration: https://github.com/brettwooldridge/HikariCP/wiki/Configuration
- Postgres error codes (SQLSTATE): https://www.postgresql.org/docs/current/errcodes-appendix.html
- Java SE `java.sql` package: https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/package-summary.html

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Connect | Driver + URL + config hygiene | `SELECT 1` from Java without secrets in Git |
| 2. CRUD | Prepared statements + mapping | Student + Course DAO CRUD works |
| 3. Layering | Interfaces + JDBC impls | `main` has no SQL strings |
| 4. Transactions | Commit and rollback paths | Enroll demo both success and failure |
| 5. Callable + batch | DB routines + bulk ops | One callable + one batch demo |
| 6. Pooling | HikariCP DataSource | App uses pool; DAOs share it |
| 7. Gate | Ready for phase project | Mastery checklist ≥90%; Mini A+B done |

**Weekly rhythm:** Week 1 = correctness and layering; Week 2 = transactions, performance-ish batching, pooling, safety.

**Suggested package layout:**

```text
com.yourname.campus/
  app/              # Main, console UI
  config/           # DataSourceFactory
  model/            # Student, Course, Enrollment
  dao/              # interfaces + jdbc impl
  exception/        # DataAccessException, NotFoundException, ...
```

---

## 12. Mastery checklist

- [ ] Explains JDBC architecture (app → API → driver → DB) in plain language
- [ ] Builds a correct Postgres JDBC URL and connects successfully
- [ ] Loads credentials from env/local config; nothing secret committed
- [ ] Uses try-with-resources for Connection / PreparedStatement / ResultSet
- [ ] Performs parameterized SELECT / INSERT / UPDATE / DELETE
- [ ] Never concatenates user input into SQL
- [ ] Maps `ResultSet` rows to Java objects/records cleanly (including nulls)
- [ ] Retrieves generated keys after insert
- [ ] Implements DAO/repository interfaces with JDBC implementations
- [ ] Controls transactions with `setAutoCommit(false)`, `commit`, `rollback`
- [ ] Demonstrates a rollback path that leaves the database unchanged
- [ ] Uses `CallableStatement` to invoke at least one DB function/procedure
- [ ] Runs a batch insert or update with `addBatch` / `executeBatch`
- [ ] Configures a connection pool (HikariCP) and uses `DataSource`
- [ ] Translates at least one constraint violation into a clear domain exception
- [ ] Keeps SQL and UI separated (no business SQL in menu code)
- [ ] Completes Mini-projects A and B
- [ ] Ready to start the Phase 2 project — [phase-project.md](phase-project.md)
