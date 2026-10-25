# Mini-Project A — Campus Registry SQL Pack
**Phase:** 2 — Data  
**Topic:** [07 — SQL & PostgreSQL](../../../phase-02-data/07-sql-postgresql.md)  
**Time:** ~6–8 hours (spread across Week 2–4 of topic 07)

## Goal

Ship a versionable SQL pack that creates a campus registry from scratch: schema, seed data, a query pack (joins/aggregates/CTEs), and reusable views — runnable with `psql -f` in order.

## Prerequisites

- [ ] PostgreSQL 15+ installed; `psql` works
- [ ] Scratch DB available (you will create `campus_registry`)
- [ ] Topic 07 Weeks 1–2 practice done (DDL, CRUD, joins, aggregates)
- [ ] Folder habit: keep SQL in files, not only GUI history

## Learn (open before coding)

- [CREATE TABLE](https://www.postgresql.org/docs/current/sql-createtable.html)
- [Constraints](https://www.postgresql.org/docs/current/ddl-constraints.html)
- [Joins](https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-JOIN)
- [CREATE VIEW](https://www.postgresql.org/docs/current/sql-createview.html)
- [WITH (CTEs)](https://www.postgresql.org/docs/current/queries-with.html)
- [psql](https://www.postgresql.org/docs/current/app-psql.html)

## Project layout

```text
campus-registry-sql/
  README.md
  01_schema.sql
  02_seed.sql
  03_queries.sql
  04_views.sql
  notes.md          # optional ER sketch notes
```

## Build steps

### Step 1 — Create database and folder
- [ ] Create project folder `campus-registry-sql/` with the four `.sql` files (empty stubs OK)
- [ ] From `psql -U postgres`:
  ```sql
  CREATE DATABASE campus_registry;
  ```
- [ ] Connect: `\c campus_registry` or `psql -U postgres -d campus_registry`
- [ ] Write README stub: host `localhost`, port `5432`, db `campus_registry`, user, script order
- **Done when:** You can `\c campus_registry` and the four files exist on disk.

### Step 2 — Schema: students & courses
- [ ] In `01_schema.sql`, start with teardown (reverse FK order) then creates:
  ```sql
  DROP VIEW IF EXISTS v_course_roster;
  DROP VIEW IF EXISTS v_student_enrollments;
  DROP TABLE IF EXISTS enrollments;
  DROP TABLE IF EXISTS courses;
  DROP TABLE IF EXISTS instructors;
  DROP TABLE IF EXISTS students;

  CREATE TABLE students (
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
  );

  CREATE TABLE courses (
    id       BIGSERIAL PRIMARY KEY,
    code     VARCHAR(20)  NOT NULL UNIQUE,
    title    VARCHAR(200) NOT NULL,
    credits  INT NOT NULL CHECK (credits > 0),
    capacity INT CHECK (capacity IS NULL OR capacity > 0)
  );
  ```
- [ ] Run: `psql -U postgres -d campus_registry -f 01_schema.sql`
- [ ] Verify with `\dt` and `\d students`
- **Done when:** Both tables exist with constraints; re-running the file does not leave orphans.

### Step 3 — Schema: instructors (optional) & enrollments
- [ ] Add optional `instructors` + `courses.instructor_id` **or** skip instructors and note why in README
- [ ] Add enrollments with uniqueness and FKs:
  ```sql
  CREATE TABLE enrollments (
    id          BIGSERIAL PRIMARY KEY,
    student_id  BIGINT NOT NULL REFERENCES students(id),
    course_id   BIGINT NOT NULL REFERENCES courses(id),
    enrolled_on DATE NOT NULL DEFAULT CURRENT_DATE,
    grade       NUMERIC(5,2) CHECK (grade IS NULL OR (grade >= 0 AND grade <= 100)),
    UNIQUE (student_id, course_id)
  );

  CREATE INDEX idx_enrollments_course  ON enrollments(course_id);
  CREATE INDEX idx_enrollments_student ON enrollments(student_id);
  ```
- [ ] Intentionally fail once: fake `student_id`; duplicate pair — paste errors into `notes.md`
- **Done when:** Bad inserts fail; good structure is in `01_schema.sql`.

### Step 4 — Seed volume targets
- [ ] Write `02_seed.sql` with **≥15 students**, **≥8 courses**, **≥40 enrollments**
- [ ] Use multi-row `INSERT` and only real FK ids (or `RETURNING` patterns carefully)
- [ ] Leave some `grade` NULL and at least one course with zero enrollments
- [ ] Run: `psql -U postgres -d campus_registry -f 02_seed.sql`
- [ ] Verify:
  ```sql
  SELECT COUNT(*) FROM students;      -- >= 15
  SELECT COUNT(*) FROM courses;       -- >= 8
  SELECT COUNT(*) FROM enrollments;   -- >= 40
  ```
- **Done when:** Counts meet minima and seed re-runs cleanly after a fresh schema apply.

### Step 5 — Query pack: joins & left joins
- [ ] In `03_queries.sql`, add labeled sections with comments
- [ ] INNER JOIN roster:
  ```sql
  -- Q1 roster
  SELECT s.first_name, s.last_name, c.code, c.title, e.enrolled_on, e.grade
  FROM enrollments e
  INNER JOIN students s ON s.id = e.student_id
  INNER JOIN courses c  ON c.id = e.course_id
  ORDER BY c.code, s.last_name;
  ```
- [ ] LEFT JOIN: courses with zero students; students with zero courses
- [ ] Run each query; confirm the empty-side rows appear
- **Done when:** Both INNER and LEFT join queries return expected shapes.

### Step 6 — Query pack: aggregates & subqueries
- [ ] Enrollment count per course (include zeros via `LEFT JOIN` + `COUNT(e.id)`)
- [ ] Average grade per course (`WHERE grade IS NOT NULL`)
- [ ] Students enrolled in more than one course (`GROUP BY` + `HAVING COUNT(*) > 1`)
- [ ] Subquery: courses never enrolled (`NOT EXISTS` or `NOT IN`)
- [ ] Save all under clear `-- Qn` comments in `03_queries.sql`
- **Done when:** ≥4 aggregate/subquery queries run and match manual spot-checks.

### Step 7 — Query pack: CTE
- [ ] Add at least one CTE report, e.g. top courses by enrollment:
  ```sql
  WITH course_counts AS (
    SELECT c.id, c.code, c.title, COUNT(e.id) AS enrolled
    FROM courses c
    LEFT JOIN enrollments e ON e.course_id = c.id
    GROUP BY c.id, c.code, c.title
  )
  SELECT * FROM course_counts
  ORDER BY enrolled DESC, code
  LIMIT 5;
  ```
- [ ] Optional second CTE: roster with `ROW_NUMBER()` per course
- **Done when:** CTE query is in `03_queries.sql` and you can explain it in one sentence.

### Step 8 — Views
- [ ] In `04_views.sql`:
  ```sql
  CREATE OR REPLACE VIEW v_student_enrollments AS
  SELECT s.id AS student_id,
         s.first_name || ' ' || s.last_name AS student_name,
         c.code, c.title, e.enrolled_on, e.grade
  FROM enrollments e
  JOIN students s ON s.id = e.student_id
  JOIN courses c  ON c.id = e.course_id;

  CREATE OR REPLACE VIEW v_course_roster AS
  SELECT c.code, c.title,
         s.last_name, s.first_name, e.enrolled_on, e.grade
  FROM enrollments e
  JOIN courses c  ON c.id = e.course_id
  JOIN students s ON s.id = e.student_id;
  ```
- [ ] Run `-f 04_views.sql`; verify `\dv`
- [ ] Answer ≥3 questions using **only** `SELECT ... FROM v_...` (no base-table joins)
- **Done when:** Both views exist and view-only queries work.

### Step 9 — Fresh-run proof
- [ ] Drop and recreate (or new empty DB), then run in order:
  ```text
  psql -U postgres -d campus_registry -f 01_schema.sql
  psql -U postgres -d campus_registry -f 02_seed.sql
  psql -U postgres -d campus_registry -f 04_views.sql
  psql -U postgres -d campus_registry -f 03_queries.sql
  ```
- [ ] Confirm counts and that queries/views still work
- **Done when:** Ordered run from empty DB succeeds with no manual GUI clicks.

### Step 10 — README finish
- [ ] Document: create DB command, script order, example connection (`localhost:5432/campus_registry`)
- [ ] List entities and seed volume targets
- [ ] Note one known limitation (e.g. capacity not enforced in SQL yet)
- **Done when:** A teammate could recreate the DB from README alone.

## Definition of Done

- [ ] `01`–`04` scripts exist and run in order on a fresh database
- [ ] Seed: ≥15 students, ≥8 courses, ≥40 enrollments
- [ ] Query pack covers join, aggregate, left join, subquery, CTE
- [ ] Two views usable for recurring reports
- [ ] README documents create-DB + run order
- [ ] No passwords committed in files

## Demo script

1. `CREATE DATABASE campus_registry;` (if needed)
2. Run `01` → `02` → `04` → `03`
3. Show `\dt`, `\dv`, and `SELECT COUNT(*)` for three tables
4. Run roster INNER JOIN and “courses with zero enrollments”
5. `SELECT * FROM v_course_roster LIMIT 10;`

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Seed with hard-coded ids that break after `TRUNCATE` | Prefer stable codes/emails; document id assumptions |
| Views created before tables | Always schema → seed → views |
| `SELECT *` only in “final” pack | Project columns; keep `*` for exploratory notes only |
| Forgetting unique `(student_id, course_id)` | Add it; prove with a failing duplicate insert |
| Scripts only work once (no DROP) | Idempotent teardown at top of `01_schema.sql` |

## After this

- Do **B — Inventory Lite** (transactions + stock) or **C — Audit-ready grades**
- Carry this schema forward into [08 JDBC](../../../phase-02-data/08-jdbc.md) mini-projects
