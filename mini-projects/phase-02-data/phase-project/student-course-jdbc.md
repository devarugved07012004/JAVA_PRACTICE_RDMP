# Mini-Project — Student / Course Registry (PostgreSQL + JDBC)
**Phase:** 2 — Data (phase project)  
**Topic:** [Phase 2 Project](../../../phase-02-data/phase-project.md)  
**Time:** ~1–2 weeks at 1 hour/day

## Goal

Ship an end-to-end campus registry: PostgreSQL schema + seed scripts, Java JDBC repositories, transactional enroll/unenroll with conflict/capacity rules, multi-table reports, and a thin console UI — meeting the Phase 2 Definition of Done.

## Prerequisites

- [ ] Topics [07](../../../phase-02-data/07-sql-postgresql.md) and [08](../../../phase-02-data/08-jdbc.md) mastery ≥90%
- [ ] Mini-projects: SQL pack A (+ B or C) and JDBC A + B complete
- [ ] Can commit/rollback a JDBC transaction and write multi-table joins without guessing
- [ ] HikariCP (or equivalent) working in a sample app

## Learn (open before coding)

- [CREATE TABLE / constraints](https://www.postgresql.org/docs/current/sql-createtable.html)
- [JDBC transactions](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html)
- [PreparedStatement](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP/wiki/Configuration)
- [Postgres error codes](https://www.postgresql.org/docs/current/errcodes-appendix.html)
- Full spec: [phase-project.md §6](../../../phase-02-data/phase-project.md)

## Project layout

```text
campus-registry/
  README.md
  db/
    schema.sql
    seed.sql
    reset.sql                 # optional
  .gitignore
  db.properties.example
  pom.xml
  src/main/java/com/yourname/campus/
    app/Main.java
    app/ConsoleUI.java
    config/DataSourceFactory.java
    model/Student.java
    model/Course.java
    model/Enrollment.java
    model/RosterEntry.java
    model/CourseSummary.java
    repository/StudentRepository.java
    repository/CourseRepository.java
    repository/EnrollmentRepository.java
    repository/jdbc/JdbcStudentRepository.java
    repository/jdbc/JdbcCourseRepository.java
    repository/jdbc/JdbcEnrollmentRepository.java
    service/EnrollmentService.java
    service/StudentService.java
    service/CourseService.java
    exception/NotFoundException.java
    exception/ConflictException.java
```

**JDBC URL:** `jdbc:postgresql://localhost:5432/campus_registry`

## Build steps

### Step 1 — Database & schema.sql
- [ ] `CREATE DATABASE campus_registry;`
- [ ] Write `db/schema.sql` (starter shape):
  ```sql
  CREATE TABLE students (
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
  );

  CREATE TABLE courses (
    id       BIGSERIAL PRIMARY KEY,
    code     VARCHAR(20) NOT NULL UNIQUE,
    title    VARCHAR(200) NOT NULL,
    credits  INT NOT NULL CHECK (credits > 0),
    capacity INT CHECK (capacity IS NULL OR capacity > 0)
  );

  CREATE TABLE enrollments (
    id          BIGSERIAL PRIMARY KEY,
    student_id  BIGINT NOT NULL REFERENCES students(id),
    course_id   BIGINT NOT NULL REFERENCES courses(id),
    enrolled_on DATE NOT NULL DEFAULT CURRENT_DATE,
    grade       NUMERIC(5,2) CHECK (grade IS NULL OR (grade >= 0 AND grade <= 100)),
    UNIQUE (student_id, course_id)
  );

  CREATE INDEX idx_enrollments_course ON enrollments(course_id);
  CREATE INDEX idx_enrollments_student ON enrollments(student_id);
  ```
- [ ] Apply: `psql -U postgres -d campus_registry -f db/schema.sql`
- [ ] Draw ER in notes (3 entities + N:M via enrollments)
- **Done when:** Schema applies cleanly; `\d enrollments` shows FKs + unique.

### Step 2 — seed.sql
- [ ] ≥10 students, ≥6 courses, ≥20 enrollments; valid FKs only
- [ ] At least one course with open capacity and one easy-to-fill test course
- [ ] Some null grades; one course with zero enrollments
- [ ] Run `-f db/seed.sql`; verify counts
- **Done when:** Counts meet minima; sample join returns rows.

### Step 3 — Java skeleton + HikariCP
- [ ] Maven/Gradle: `org.postgresql:postgresql`, `com.zaxxer:HikariCP`
- [ ] Packages: `config`, `model`, `repository`, `service`, `app`, `exception`
- [ ] `DataSourceFactory` reads `DB_URL` / `DB_USER` / `DB_PASSWORD`
  - Example: `DB_URL=jdbc:postgresql://localhost:5432/campus_registry`
- [ ] Smoke `SELECT 1`; `.gitignore` secrets; commit `*.example` only
- **Done when:** Pooled `SELECT 1` works; package tree exists.

### Step 4 — StudentRepository JDBC
- [ ] Interface: `insert`, `findById`, `findAll`, `updateEmail`
- [ ] `JdbcStudentRepository`: PreparedStatement + try-with-resources; `RETURN_GENERATED_KEYS` on insert
- [ ] Demo insert + findById from a small main/test class
- **Done when:** Inserted student readable by id from Java.

### Step 5 — CourseRepository + counts
- [ ] CRUD for courses
- [ ] `listWithEnrollmentCounts()` via `LEFT JOIN` + `COUNT` + `GROUP BY`
- [ ] Map to `CourseSummary(id, code, title, enrollmentCount)`
- [ ] Zero-enrollment course shows `0`
- **Done when:** Counts match `psql` for ≥2 courses.

### Step 6 — EnrollmentRepository
- [ ] `insert`, `findByStudentId`, `findByCourseId`
- [ ] Duplicate pair → SQLState `23505` → `ConflictException` / `AlreadyEnrolledException`
- [ ] Clear message when called from demo code
- **Done when:** Happy insert works; duplicate yields domain exception.

### Step 7 — Console UI (read + create)
- [ ] Menu: list/add students, list/add courses, list enrollments (read)
- [ ] UI calls services/repos only — **no SQL in ConsoleUI**
- [ ] Create one student + one course from menu; verify in `psql`
- **Done when:** Menu demos seed data and persists creates.

### Step 8 — EnrollmentService.enroll TX
- [ ] `enroll(studentId, courseId)` owns the transaction:
  - `setAutoCommit(false)` → validate exists → insert → `commit`
  - on error → `rollback` → rethrow domain/wrapped
- [ ] Demo success from menu
- **Done when:** Successful enroll commits; failure path coded to rollback.

### Step 9 — Capacity + unenroll + conflicts
- [ ] Same TX: count enrollments; if `>= capacity`, `ConflictException` + rollback
- [ ] `unenroll` with TX + DELETE; second call → NotFound
- [ ] Duplicate enroll still conflicts
- [ ] Forced rollback test: counts unchanged in `psql`
- **Done when:** Success, full-reject, unenroll, and conflict paths verified.

### Step 10 — Multi-table reports
- [ ] Course roster (names + `enrolled_on`) for `courseId`
- [ ] Student schedule (titles + credits) for `studentId`
- [ ] Courses with zero enrollments (`LEFT JOIN` … `WHERE e.id IS NULL`)
- [ ] Expose via menu; DTOs for rows
- **Done when:** All three reports match `psql` checks.

### Step 11 — GPA / average (callable or SQL)
- [ ] Option A: `CallableStatement` → `student_avg_grade(?)`
- [ ] Option B: `SELECT AVG(grade) ...` in Java
- [ ] Print next to manual `psql` check; document choice in README
- **Done when:** One average path works end-to-end from Java.

### Step 12 — README, secrets, DoD pass
- [ ] README: create DB, `schema.sql`/`seed.sql`, env vars, run app, numbered demo script
- [ ] Grep for passwords; example config only
- [ ] Notes: Phase 3 REST resource list; Phase 4 entity/FK sketch
- [ ] Walk [phase-project.md §12](../../../phase-02-data/phase-project.md) Definition of Done
- [ ] Fresh-DB path once: create → schema → seed → app demo
- **Done when:** DoD items pass; Phase 3/4 notes exist; no secrets committed.

## Definition of Done

- [ ] Fresh path: create DB → schema → seed → run app (documented)
- [ ] Student/course CRUD via JDBC
- [ ] Enroll end-to-end; duplicate rejected (constraint + message)
- [ ] Rollback path demonstrated in the database
- [ ] ≥1 multi-table report from Java (roster and/or schedule)
- [ ] All parameters bound; pooled DataSource; layered packages
- [ ] Secrets not committed; README + demo script present
- [ ] Phase 3 API sketch + Phase 4 entity notes written

## Demo script

1. `psql -U postgres -c "CREATE DATABASE campus_registry;"`
2. `psql -U postgres -d campus_registry -f db/schema.sql`
3. `psql -U postgres -d campus_registry -f db/seed.sql`
4. Export `DB_URL=jdbc:postgresql://localhost:5432/campus_registry` (+ user/password)
5. Start app → list students/courses
6. Enroll valid pair → show roster
7. Enroll duplicate → conflict message
8. Fill capacity course → reject; confirm count in `psql`
9. Unenroll → schedule updates
10. Show zero-enrollment courses report

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Schema only in Java strings | `db/schema.sql` is source of truth |
| Enroll logic split across UI + random DAOs | One service method owns the TX |
| Auto-commit on for enroll | `setAutoCommit(false)` for the use-case |
| Pool per DAO call | One app-scoped DataSource |
| Raw SQLException to console | Domain exceptions + cause |
| Skipping seed | Always ship demo data |

## After this

- Phase 2 gate complete → start Phase 3 ([09 HTML/CSS/JS](../../../phase-03-frontend-api/09-html-css-js.md))
- Keep services free of console printing so an HTTP layer can wrap them later
