# Mini-Project B — Enrollment Service with Transactions
**Phase:** 2 — Data  
**Topic:** [08 — JDBC](../../../phase-02-data/08-jdbc.md)  
**Time:** ~6–8 hours

## Goal

Implement an enrollment service with explicit JDBC transactions: `enroll`, `unenroll`, roster and schedule reports, plus domain exceptions for not-found / already-enrolled / capacity.

## Prerequisites

- [ ] Mini-project A patterns (DAO, config, try-with-resources)
- [ ] DB with `students`, `courses`, `enrollments` (+ optional `capacity`)
- [ ] Topic 08 Days 8–9 and 13 practiced (TX, SQLState mapping)
- [ ] Unique `(student_id, course_id)` constraint present

## Learn (open before coding)

- [Using Transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html)
- [Connection.setAutoCommit](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Connection.html#setAutoCommit(boolean))
- [Postgres error codes](https://www.postgresql.org/docs/current/errcodes-appendix.html)
- [CallableStatement](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/CallableStatement.html) *(optional capacity function)*
- [Joins](https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-JOIN)

## Project layout

```text
enrollment-service/
  pom.xml
  README.md
  src/main/java/com/yourname/campus/
    app/Main.java
    config/DataSourceFactory.java      # DriverManager OK; HikariCP better
    model/Student.java
    model/Course.java
    model/Enrollment.java
    model/RosterEntry.java
    model/ScheduleEntry.java
    dao/...                            # thin data access if needed
    service/EnrollmentService.java
    exception/NotFoundException.java
    exception/ConflictException.java
    exception/ValidationException.java
```

**JDBC URL example:** `jdbc:postgresql://localhost:5432/campus_registry`

## Build steps

### Step 1 — Shared DataSource
- [ ] Create `DataSourceFactory` (HikariCP preferred):
  ```text
  jdbcUrl = jdbc:postgresql://localhost:5432/campus_registry
  maximumPoolSize = 5
  ```
- [ ] Smoke: `dataSource.getConnection()` + `SELECT 1`
- [ ] Ensure try-with-resources returns connections to the pool
- **Done when:** App obtains connections only via the shared DataSource.

### Step 2 — Domain exceptions
- [ ] `NotFoundException` — missing student/course/enrollment
- [ ] `ConflictException` — already enrolled / course full
- [ ] Optionally map SQLState in one helper: `23505` → conflict, `23503` → integrity/not-found-ish
- **Done when:** Exception types exist and one mapping helper is ready.

### Step 3 — Existence checks
- [ ] Methods (service or DAO): `requireStudent(conn, id)`, `requireCourse(conn, id)` using `SELECT 1 ... WHERE id = ?`
- [ ] Throw `NotFoundException` when absent
- [ ] Keep these callable **inside** an open transaction connection
- **Done when:** Bad ids throw NotFound without inserting anything.

### Step 4 — enroll happy path
- [ ] `EnrollmentService.enroll(long studentId, long courseId)`:
  ```java
  try (Connection conn = dataSource.getConnection()) {
    conn.setAutoCommit(false);
    try {
      // validate student + course
      // INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)
      conn.commit();
    } catch (Exception e) {
      conn.rollback();
      throw e;
    } finally {
      conn.setAutoCommit(true);
    }
  }
  ```
- [ ] Verify row in `psql` after success
- **Done when:** Valid enroll commits and is visible outside Java.

### Step 5 — Duplicate enrollment conflict
- [ ] Enroll the same pair again
- [ ] Catch unique violation `23505` → `ConflictException("Already enrolled")`
- [ ] Confirm friendly message from menu/main
- **Done when:** Second enroll fails cleanly; DB still has one row.

### Step 6 — Capacity rule (same TX)
- [ ] Ensure `courses.capacity` is set on a test course
- [ ] Before insert, `SELECT COUNT(*) FROM enrollments WHERE course_id = ?`
- [ ] If `count >= capacity`, throw `ConflictException` and rollback
- [ ] Test: open seat succeeds; full course rejects with no new row
- **Done when:** Both success and full-reject paths verified in DB.

### Step 7 — Rollback on FK failure
- [ ] Call enroll with nonexistent student id (skip require check once in a scratch test, or force bad insert)
- [ ] Ensure `rollback()` runs; enrollment count unchanged
- [ ] Record SQLState `23503` in notes
- **Done when:** Forced failure leaves DB unchanged.

### Step 8 — unenroll
- [ ] `unenroll(studentId, courseId)` in a TX: `DELETE FROM enrollments WHERE student_id = ? AND course_id = ?`
- [ ] If `executeUpdate() == 0` → `NotFoundException`
- [ ] Commit on success; demo unenroll then re-enroll
- **Done when:** Unenroll removes the row; second unenroll is NotFound.

### Step 9 — listRoster & listSchedule
- [ ] Roster SQL:
  ```sql
  SELECT s.first_name, s.last_name, e.enrolled_on, e.grade
  FROM enrollments e
  JOIN students s ON s.id = e.student_id
  WHERE e.course_id = ?
  ORDER BY s.last_name, s.first_name
  ```
- [ ] Schedule SQL: course code/title/credits for a `student_id`
- [ ] Map to `RosterEntry` / `ScheduleEntry`; print from menu
- [ ] Compare one course roster to `psql`
- **Done when:** Both reports match database joins.

### Step 10 — Optional CallableStatement
- [ ] If topic-07 capacity or GPA function exists:
  ```java
  try (CallableStatement cs = conn.prepareCall("{ ? = call student_avg_grade(?) }")) {
    cs.registerOutParameter(1, Types.NUMERIC);
    cs.setLong(2, studentId);
    cs.execute();
    System.out.println(cs.getBigDecimal(1));
  }
  ```
- [ ] Or skip and document “AVG via SELECT” instead
- **Done when:** One callable **or** documented SQL AVG path works from Java.

### Step 11 — Thin menu + README
- [ ] Menu: enroll, unenroll, roster by course id, schedule by student id
- [ ] README: env vars, capacity behavior, demo of conflict + rollback
- **Done when:** Full demo runs from README steps.

## Definition of Done

- [ ] `enroll` / `unenroll` / `listRoster` / `listSchedule` implemented
- [ ] Single explicit transaction per enroll/unenroll
- [ ] Domain exceptions for not-found / already-enrolled / capacity (if modeled)
- [ ] Rollback path verified against real DB counts
- [ ] No SQL string-concat with user input; pool or shared DataSource in use

## Demo script

1. List roster for a course (seed data)
2. Enroll a new valid pair → success; show roster again
3. Enroll same pair → conflict message
4. Fill a capacity-1 course → next enroll rejected; `psql` count unchanged
5. Unenroll → row gone; schedule for that student updates

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Auto-commit left on for multi-step enroll | `setAutoCommit(false)` for the use-case |
| Validating on one connection, inserting on another | One `Connection` for the whole TX |
| Swallowing SQLException | Rollback + rethrow domain/wrapped error |
| Capacity checked outside TX | Count + insert in the same transaction |
| Printing raw stacks only | Map to NotFound/Conflict messages |

## After this

- Optional stretch: [C — Batch seeder](C-batch-seeder.md)
- Start Phase 2 project: [student-course-jdbc.md](../phase-project/student-course-jdbc.md)
