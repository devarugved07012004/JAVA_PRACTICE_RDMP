# 07 — SQL & PostgreSQL

**Duration:** ~3–4 weeks (21–28 days) · **Cadence:** 1 hour/day  
**Focus:** Database concepts, SQL basics, CRUD, joins, functions, views, indexes, procedures, triggers, normalization, PostgreSQL administration

---

## 1. Why this topic is important

Almost every backend feature eventually reads or writes durable data. SQL is how you ask precise questions of that data: who is enrolled in a course, which orders are unpaid, which inventory rows changed today. PostgreSQL is a free, production-grade relational database used widely in industry and in later Spring/JPA work.

This topic trains you to **model** (tables, keys, constraints), **query** (SELECT/JOIN/aggregates), and **operate** a local Postgres instance (create DB, run scripts, inspect plans). JDBC (topic 08) and JPA (Phase 4) sit on top of this skill—weak SQL becomes slow debugging for the rest of the roadmap.

---

## 2. Prerequisites

- Phase 1 gate passed (Core Java + phase project)
- Comfortable with files, terminal, and basic logic
- No prior SQL required

**Environment you will set up:**

- PostgreSQL 15+ (or 16) installed locally **or** via Docker
- `psql` CLI available (or pgAdmin / DBeaver free edition for GUI)
- A scratch database, e.g. `learn_sql`, with a dedicated user or your OS user
- A folder for scripts: `schema/`, `seed/`, `queries/`

**Verify install:**

```text
psql --version
psql -U postgres -c "SELECT version();"
```

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Relational model | Tables = relations; rows = tuples; columns = attributes |
| Keys | Primary key (unique identity); foreign key (reference integrity) |
| Data types (Postgres) | `INTEGER`, `BIGINT`, `VARCHAR`/`TEXT`, `BOOLEAN`, `DATE`, `TIMESTAMP`, `NUMERIC`, `UUID` |
| DDL | `CREATE TABLE`, `ALTER TABLE`, `DROP TABLE` |
| Constraints | `NOT NULL`, `UNIQUE`, `CHECK`, `DEFAULT`, `PRIMARY KEY`, `FOREIGN KEY` |
| DML / CRUD | `INSERT`, `SELECT`, `UPDATE`, `DELETE` |
| Filtering | `WHERE`, `AND`/`OR`, `IN`, `BETWEEN`, `LIKE`/`ILIKE`, `IS NULL` |
| Sorting & limiting | `ORDER BY`, `LIMIT`, `OFFSET` |
| Basic aggregates | `COUNT`, `SUM`, `AVG`, `MIN`, `MAX` + `GROUP BY` / `HAVING` |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Joins | `INNER`, `LEFT`, `RIGHT`, `FULL`; join conditions vs filters |
| Multi-table design | 1:N and N:M (junction/bridge tables) |
| Subqueries | In `WHERE`, `FROM`, correlated vs non-correlated |
| Set operations | `UNION` / `UNION ALL`, `INTERSECT`, `EXCEPT` |
| Functions | String, date/time, numeric; `COALESCE`, `NULLIF`, `CAST` |
| Views | `CREATE VIEW` for reusable query shapes |
| Indexes | B-tree basics; when indexes help SELECT vs slow writes |
| Transactions (SQL) | `BEGIN` / `COMMIT` / `ROLLBACK`; atomic multi-statement changes |
| Upsert | `INSERT ... ON CONFLICT` (Postgres) |
| CTEs | `WITH` clauses for readable multi-step queries |

### Advanced (practical depth)

| Concept | What to internalize |
|--------|----------------------|
| Normalization | 1NF → 2NF → 3NF; when denormalization is intentional |
| Stored procedures / functions | `CREATE FUNCTION` / `PROCEDURE`; `plpgsql` basics |
| Triggers | `BEFORE`/`AFTER` `INSERT`/`UPDATE`/`DELETE`; audit patterns |
| Window functions | `ROW_NUMBER`, `RANK`, `SUM() OVER (...)` (intro level) |
| Explain plans | `EXPLAIN` / `EXPLAIN ANALYZE` to see seq scan vs index scan |
| Postgres admin basics | Roles, privileges (`GRANT`/`REVOKE`), databases vs schemas |
| Backup / restore | `pg_dump` / `psql` restore for learning DBs |
| Concurrency awareness | Dirty reads idea; isolation levels at a conceptual level |
| JSONB intro (optional) | When semi-structured columns appear in modern apps |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Platform, DDL, CRUD (Days 1–7)

- [ ] **Day 1 — Install Postgres & connect**
  - [ ] **Learn:** [Download PostgreSQL](https://www.postgresql.org/download/) · [psql](https://www.postgresql.org/docs/current/app-psql.html) · [Managing databases](https://www.postgresql.org/docs/current/managing-databases.html)
  - [ ] **Practice:** Install Postgres and create `learn_sql`
    1. Install PostgreSQL 15+ (installer or Docker `postgres:16`). Note the `postgres` user password you set.
    2. Open a new terminal. Run `psql --version`. Then: `psql -U postgres -c "SELECT version();"` (enter password when prompted).
    3. Create the practice DB: `CREATE DATABASE learn_sql;` (from `psql -U postgres`), then `\c learn_sql`.
    4. Create folder `sql-practice/` with subfolders `schema/`, `seed/`, `queries/`.
    5. In notes, write your connection string shape: host `localhost`, port `5432`, db `learn_sql`, user, and where the password lives (not in Git).
    6. **Done when:** `SELECT version();` runs inside `learn_sql` and your notes list host/port/db/user.

- [ ] **Day 2 — Relational concepts & ER sketch**
  - [ ] **Learn:** [Concepts (tutorial)](https://www.postgresql.org/docs/current/tutorial-concepts.html) · [Constraints overview](https://www.postgresql.org/docs/current/ddl-constraints.html)
  - [ ] **Practice:** Define relational terms and sketch students/courses
    1. In notes, write one sentence each for: table, row, column, primary key, foreign key.
    2. Sketch an ER diagram on paper/whiteboard: `students`, `courses`, and a many-to-many link via `enrollments`.
    3. Label 1:N vs N:M on the sketch; mark which side gets the FK for a 1:N.
    4. List candidate attributes (e.g. email, credits, enrolled_on, grade) next to each entity.
    5. **Done when:** Notes definitions + ER sketch exist and you can point to where the N:M junction goes.

- [ ] **Day 3 — Data types & CREATE TABLE**
  - [ ] **Learn:** [Data types](https://www.postgresql.org/docs/current/datatype.html) · [CREATE TABLE](https://www.postgresql.org/docs/current/sql-createtable.html)
  - [ ] **Practice:** Create `students` and `courses` tables
    1. `\c learn_sql`. Run:
       ```sql
       CREATE TABLE students (
         id         BIGSERIAL PRIMARY KEY,
         first_name VARCHAR(100) NOT NULL,
         last_name  VARCHAR(100) NOT NULL,
         email      VARCHAR(255) NOT NULL,
         created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
       );
       ```
    2. Create `courses` with `id BIGSERIAL PK`, `code VARCHAR(20)`, `title VARCHAR(200)`, `credits INT`, `capacity INT`.
    3. Verify with `\dt` and `\d students`.
    4. Save both `CREATE TABLE` statements into `schema/01_schema.sql`.
    5. **Done when:** Both tables exist in `learn_sql` and the SQL is saved in `01_schema.sql`.

- [ ] **Day 4 — Constraints**
  - [ ] **Learn:** [Constraints](https://www.postgresql.org/docs/current/ddl-constraints.html) · [ALTER TABLE](https://www.postgresql.org/docs/current/sql-altertable.html)
  - [ ] **Practice:** Add constraints and force failures
    1. Run: `ALTER TABLE students ADD CONSTRAINT uq_students_email UNIQUE (email);`
    2. Run: `ALTER TABLE courses ADD CONSTRAINT chk_credits_positive CHECK (credits > 0);` and make `code` `NOT NULL` + `UNIQUE` if missing.
    3. Intentionally fail: insert a student with `NULL` first_name; insert duplicate email; insert course with `credits = 0`. Copy each error into notes.
    4. Update `01_schema.sql` so constraints are in the `CREATE TABLE` (not only ALTER).
    5. **Done when:** All three violation attempts fail with clear errors and schema file includes the constraints.

- [ ] **Day 5 — INSERT / SELECT / UPDATE / DELETE**
  - [ ] **Learn:** [INSERT](https://www.postgresql.org/docs/current/sql-insert.html) · [SELECT](https://www.postgresql.org/docs/current/sql-select.html) · [UPDATE](https://www.postgresql.org/docs/current/sql-update.html) · [DELETE](https://www.postgresql.org/docs/current/sql-delete.html)
  - [ ] **Practice:** Full CRUD with safe WHERE clauses
    1. Insert 3 students with column lists; multi-row insert 2 more. Then insert 4 courses.
    2. Run `SELECT id, first_name, email FROM students ORDER BY last_name;` (no `SELECT *` in the saved query).
    3. `UPDATE students SET email = 'new@example.com' WHERE id = 1;` verify with `SELECT`.
    4. `DELETE FROM students WHERE id = 5;` then `SELECT COUNT(*) FROM students;`.
    5. Save today’s DML into `seed/02_seed.sql` and `queries/03_crud.sql`.
    6. **Done when:** Counts match expectations and every UPDATE/DELETE used a `WHERE`.

- [ ] **Day 6 — Filtering, sort, limit**
  - [ ] **Learn:** [SELECT (WHERE / ORDER BY / LIMIT)](https://www.postgresql.org/docs/current/sql-select.html) · [Pattern matching](https://www.postgresql.org/docs/current/functions-matching.html)
  - [ ] **Practice:** Filter drills on students and courses
    1. `SELECT * FROM students WHERE email ILIKE '%@example.com';`
    2. `SELECT * FROM courses WHERE credits BETWEEN 3 AND 5;` and `WHERE code IN ('CS101','MATH201');` (adjust codes to yours).
    3. Insert one student with `email` temporarily nullable test column or leave a grade null later — practice `IS NULL` on a column you can null (or add optional `middle_name` and leave it null).
    4. `SELECT title, credits FROM courses ORDER BY credits DESC, title ASC LIMIT 3;`
    5. Save queries to `queries/04_filters.sql`.
    6. **Done when:** All five filter styles run and you can explain `ILIKE` vs `LIKE`.

- [ ] **Day 7 — Week review: schema scripts**
  - [ ] **Learn:** [psql meta-commands](https://www.postgresql.org/docs/current/app-psql.html#APP-PSQL-META-COMMANDS) · [DROP TABLE](https://www.postgresql.org/docs/current/sql-droptable.html)
  - [ ] **Practice:** Drop/recreate from clean scripts
    1. Finish clean `schema/01_schema.sql` (DROP IF EXISTS in reverse FK order, then CREATE).
    2. Finish `seed/02_seed.sql` with ≥5 students and ≥4 courses.
    3. From shell: `psql -U postgres -d learn_sql -f schema/01_schema.sql` then `-f seed/02_seed.sql`.
    4. Verify with `\dt` and `SELECT COUNT(*) FROM students; SELECT COUNT(*) FROM courses;`
    5. **Done when:** Fresh run of both files rebuilds the DB with expected counts.

### Week 2 — Joins, aggregates, design (Days 8–14)

- [ ] **Day 8 — Junction table enrollments**
  - [ ] **Learn:** [Foreign keys](https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-FK) · [CREATE TABLE](https://www.postgresql.org/docs/current/sql-createtable.html)
  - [ ] **Practice:** Create `enrollments` with FKs and uniqueness
    1. Run:
       ```sql
       CREATE TABLE enrollments (
         id          BIGSERIAL PRIMARY KEY,
         student_id  BIGINT NOT NULL REFERENCES students(id),
         course_id   BIGINT NOT NULL REFERENCES courses(id),
         enrolled_on DATE NOT NULL DEFAULT CURRENT_DATE,
         grade       NUMERIC(5,2),
         UNIQUE (student_id, course_id)
       );
       ```
    2. Insert ≥8 enrollments linking real student/course ids.
    3. Try inserting a fake `student_id` — confirm FK error. Try duplicate pair — confirm unique error.
    4. Append DDL + seed to `01_schema.sql` / `02_seed.sql`.
    5. **Done when:** Enrollments exist, both bad inserts fail, scripts updated.

- [ ] **Day 9 — INNER JOIN**
  - [ ] **Learn:** [Joins](https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-JOIN) · [SELECT FROM](https://www.postgresql.org/docs/current/sql-select.html)
  - [ ] **Practice:** Students↔courses via INNER JOIN
    1. Run:
       ```sql
       SELECT s.first_name, s.last_name, c.title, e.enrolled_on
       FROM enrollments e
       INNER JOIN students s ON s.id = e.student_id
       INNER JOIN courses c ON c.id = e.course_id;
       ```
    2. Write a second query: course title + `COUNT(*)` of students using `INNER JOIN` + `GROUP BY`.
    3. Save both to `queries/05_inner_join.sql`.
    4. In notes, one sentence: when a student with no enrollments appears (answer: never, with INNER).
    5. **Done when:** Both queries return rows and the note explains missing unmatched rows.

- [ ] **Day 10 — LEFT JOIN**
  - [ ] **Learn:** [Joins (LEFT / OUTER)](https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-JOIN)
  - [ ] **Practice:** Find unmatched courses and students
    1. Ensure at least one course has zero enrollments (insert a course with no enrollments if needed).
    2. Run: courses with no students —
       ```sql
       SELECT c.title FROM courses c
       LEFT JOIN enrollments e ON e.course_id = c.id
       WHERE e.id IS NULL;
       ```
    3. Mirror for students with no courses (`LEFT JOIN` enrollments on `student_id`, `WHERE e.id IS NULL`).
    4. Compare row counts of INNER vs LEFT for the same base tables; write the difference in notes.
    5. **Done when:** Both “zero match” queries return the expected empty-side rows.

- [ ] **Day 11 — Aggregates & HAVING**
  - [ ] **Learn:** [Aggregate functions (tutorial)](https://www.postgresql.org/docs/current/tutorial-agg.html) · [Aggregate functions](https://www.postgresql.org/docs/current/functions-aggregate.html)
  - [ ] **Practice:** GROUP BY and HAVING on enrollments
    1. `SELECT course_id, COUNT(*) FROM enrollments GROUP BY course_id;`
    2. Add `HAVING COUNT(*) >= 2` (adjust N to your seed).
    3. `SELECT course_id, AVG(grade) FROM enrollments WHERE grade IS NOT NULL GROUP BY course_id;`
    4. Join to `courses` so the report shows `title` instead of only id.
    5. Save to `queries/06_aggregates.sql`.
    6. **Done when:** HAVING filters groups and AVG ignores null grades correctly.

- [ ] **Day 12 — Subqueries**
  - [ ] **Learn:** [Subqueries](https://www.postgresql.org/docs/current/functions-subquery.html) · [SELECT](https://www.postgresql.org/docs/current/sql-select.html)
  - [ ] **Practice:** Subquery filters for over-average and never-enrolled
    1. Students with more enrollments than average:
       ```sql
       SELECT student_id, COUNT(*) AS cnt FROM enrollments
       GROUP BY student_id
       HAVING COUNT(*) > (SELECT AVG(c) FROM (
         SELECT COUNT(*) AS c FROM enrollments GROUP BY student_id
       ) t);
       ```
    2. Courses never enrolled: `WHERE id NOT IN (SELECT course_id FROM enrollments)` (or `NOT EXISTS`).
    3. Rewrite one of them as a join if you can; note which reads clearer.
    4. Save to `queries/07_subqueries.sql`.
    5. **Done when:** Both queries run and you can explain the subquery in one sentence each.

- [ ] **Day 13 — Set operations**
  - [ ] **Learn:** [UNION / INTERSECT / EXCEPT](https://www.postgresql.org/docs/current/queries-union.html)
  - [ ] **Practice:** UNION vs UNION ALL with student lists
    1. Create a temp demo: `CREATE TABLE archived_students AS SELECT * FROM students WHERE false;` insert 2 rows (one email overlapping an active student if possible).
    2. `SELECT email FROM students UNION SELECT email FROM archived_students;` note distinct count.
    3. Same with `UNION ALL`; compare row counts.
    4. Try `INTERSECT` and `EXCEPT` on the email columns; write results in notes.
    5. **Done when:** Notes show UNION vs UNION ALL counts and at least one INTERSECT/EXCEPT result.

- [ ] **Day 14 — Normalize to 3NF**
  - [ ] **Learn:** [Table basics](https://www.postgresql.org/docs/current/ddl-basics.html) · [Constraints](https://www.postgresql.org/docs/current/ddl-constraints.html)
  - [ ] **Practice:** Schema design task A — normalize the flat spreadsheet
    1. Open §5 task A. Copy the flat columns into notes.
    2. Design 3NF tables: `students`, `instructors`, `courses`, `enrollments` (adjust names as needed).
    3. Write `CREATE TABLE` + small seed in `schema/14_normalize.sql`.
    4. Write one JOIN query that reconstructs the flat report.
    5. **Done when:** Script creates normalized tables and the flat report query returns joined rows.

### Week 3 — Functions, views, indexes, transactions (Days 15–21)

- [ ] **Day 15 — String & date functions**
  - [ ] **Learn:** [String functions](https://www.postgresql.org/docs/current/functions-string.html) · [Date/time functions](https://www.postgresql.org/docs/current/functions-datetime.html)
  - [ ] **Practice:** Format enrollment report with string/date functions
    1. `SELECT UPPER(last_name), CONCAT(first_name, ' ', last_name) FROM students;`
    2. `SELECT DATE_TRUNC('month', enrolled_on), COUNT(*) FROM enrollments GROUP BY 1;`
    3. `SELECT *, (CURRENT_DATE - enrolled_on) AS days_enrolled FROM enrollments;`
    4. Build a one-line “roster label”: `last_name || ', ' || first_name || ' — ' || title` via joins.
    5. Save to `queries/08_functions.sql`.
    6. **Done when:** All four queries run and roster labels print cleanly.

- [ ] **Day 16 — COALESCE / NULLIF / CAST**
  - [ ] **Learn:** [Conditional expressions](https://www.postgresql.org/docs/current/functions-conditional.html) · [Type conversion](https://www.postgresql.org/docs/current/typeconv.html)
  - [ ] **Practice:** Null-safe grade reports
    1. Ensure some enrollments have `grade IS NULL`.
    2. `SELECT student_id, COALESCE(grade::text, 'In progress') FROM enrollments;`
    3. Demo `NULLIF(grade, 0)` (or similar) and `CAST(grade AS INTEGER)` / `grade::INT`.
    4. Write a report query that shows average grade with nulls excluded, and a second column using `COALESCE` for display.
    5. **Done when:** Display never shows blank nulls awkwardly and CAST works without error.

- [ ] **Day 17 — Views**
  - [ ] **Learn:** [CREATE VIEW](https://www.postgresql.org/docs/current/sql-createview.html) · [Views](https://www.postgresql.org/docs/current/tutorial-views.html)
  - [ ] **Practice:** Create and query enrollment views only
    1. `CREATE VIEW v_student_enrollments AS` select student name, course title, enrolled_on, grade (joins).
    2. `CREATE VIEW v_course_roster AS` select course code/title, student name, enrolled_on.
    3. For the rest of the session, answer questions only with `SELECT ... FROM v_...` (no base-table joins).
    4. Save DDL to `schema/04_views.sql`.
    5. **Done when:** Both views exist (`\dv`) and you answered ≥3 questions via views only.

- [ ] **Day 18 — Indexes & EXPLAIN**
  - [ ] **Learn:** [Indexes](https://www.postgresql.org/docs/current/indexes.html) · [EXPLAIN](https://www.postgresql.org/docs/current/sql-explain.html) · [CREATE INDEX](https://www.postgresql.org/docs/current/sql-createindex.html)
  - [ ] **Practice:** Index enrollments and compare plans
    1. Pick a join/filter query on `enrollments.course_id`. Run `EXPLAIN ANALYZE` and save the plan text.
    2. `CREATE INDEX idx_enrollments_course ON enrollments(course_id);`
    3. Re-run `EXPLAIN ANALYZE` on the same query; paste both plans into notes.
    4. Note whether you see Index Scan vs Seq Scan (small data may still seq-scan — still record what happened).
    5. **Done when:** Index exists (`\di`) and notes contain before/after EXPLAIN output.

- [ ] **Day 19 — Transactions**
  - [ ] **Learn:** [Transactions (tutorial)](https://www.postgresql.org/docs/current/tutorial-transactions.html) · [BEGIN](https://www.postgresql.org/docs/current/sql-begin.html)
  - [ ] **Practice:** Commit and rollback a multi-statement change
    1. `BEGIN;` insert an enrollment; `UPDATE` a grade; `SELECT` to confirm visible in-session; `COMMIT;`
    2. `BEGIN;` insert another enrollment; `SELECT` it; then `ROLLBACK;` confirm it disappeared.
    3. Optional transfer demo: two updates in one TX (e.g. decrement/increment a demo `seats_taken` column) then rollback.
    4. Write the exact commands into `queries/09_transactions.sql` as comments + runnable blocks.
    5. **Done when:** You demonstrated one COMMIT path and one ROLLBACK path with verified row counts.

- [ ] **Day 20 — Upsert & CTEs**
  - [ ] **Learn:** [INSERT … ON CONFLICT](https://www.postgresql.org/docs/current/sql-insert.html) · [WITH (CTEs)](https://www.postgresql.org/docs/current/queries-with.html)
  - [ ] **Practice:** Upsert a grade and rewrite a report as CTE
    1. Upsert:
       ```sql
       INSERT INTO enrollments (student_id, course_id, grade)
       VALUES (1, 1, 88)
       ON CONFLICT (student_id, course_id)
       DO UPDATE SET grade = EXCLUDED.grade;
       ```
    2. Run it twice; confirm still one row and grade updates.
    3. Rewrite a multi-step report (e.g. counts then filter) as `WITH course_counts AS (...) SELECT ...`.
    4. Save both to `queries/10_upsert_cte.sql`.
    5. **Done when:** Upsert is idempotent on the unique key and the CTE query returns the report.

- [ ] **Day 21 — Week review: timed queries**
  - [ ] **Learn:** [SQL commands reference](https://www.postgresql.org/docs/current/sql-commands.html) · [SELECT](https://www.postgresql.org/docs/current/sql-select.html)
  - [ ] **Practice:** Timed §5 question set
    1. Set a 45-minute timer. Open §5 practice questions (warm-ups + joins).
    2. Answer questions 1–10 in `queries/11_timed.sql` without peeking at old solutions.
    3. Run each query; mark pass/fail in notes.
    4. Fix any failures after the timer; note weak spots (joins vs aggregates).
    5. **Done when:** ≥8/10 queries correct after review and weak spots are listed.

### Week 4 — Procedures, triggers, admin, consolidation (Days 22–28)

- [ ] **Day 22 — SQL functions (GPA)**
  - [ ] **Learn:** [CREATE FUNCTION](https://www.postgresql.org/docs/current/sql-createfunction.html) · [PL/pgSQL](https://www.postgresql.org/docs/current/plpgsql.html)
  - [ ] **Practice:** Create and call an average-grade function
    1. Write `CREATE FUNCTION student_avg_grade(p_student_id BIGINT) RETURNS NUMERIC AS $$ ... $$ LANGUAGE sql;` (or plpgsql) that `AVG(grade)` for that student.
    2. `SELECT student_avg_grade(1);` and compare to a hand-written `AVG` query.
    3. Call it in a set: `SELECT id, student_avg_grade(id) FROM students LIMIT 5;`
    4. Save to `schema/05_functions_triggers.sql`.
    5. **Done when:** Function returns the same number as the manual AVG for a known student.

- [ ] **Day 23 — Enroll with capacity check**
  - [ ] **Learn:** [CREATE PROCEDURE](https://www.postgresql.org/docs/current/sql-createprocedure.html) · [Errors and messages](https://www.postgresql.org/docs/current/plpgsql-errors-and-messages.html)
  - [ ] **Practice:** Enroll procedure that rejects when full
    1. Ensure `courses.capacity` is set (e.g. capacity 1 on a test course).
    2. Create a procedure/function `enroll_student(p_student_id, p_course_id)` that counts enrollments and `RAISE EXCEPTION` if `count >= capacity`.
    3. `CALL enroll_student(...)` (or `SELECT enroll_student(...)`) successfully once.
    4. Call again for another student on the full course — confirm exception; no new row.
    5. **Done when:** Success path inserts; full path errors and row count unchanged.

- [ ] **Day 24 — Audit trigger**
  - [ ] **Learn:** [Triggers](https://www.postgresql.org/docs/current/triggers.html) · [CREATE TRIGGER](https://www.postgresql.org/docs/current/sql-createtrigger.html) · [Trigger functions](https://www.postgresql.org/docs/current/plpgsql-trigger.html)
  - [ ] **Practice:** Grade-change audit log
    1. `CREATE TABLE grade_audit (id BIGSERIAL PRIMARY KEY, enrollment_id BIGINT, old_grade NUMERIC, new_grade NUMERIC, changed_at TIMESTAMPTZ DEFAULT NOW());`
    2. Write a trigger function that on `UPDATE OF grade` inserts `OLD.grade`, `NEW.grade`.
    3. `CREATE TRIGGER trg_grade_audit AFTER UPDATE OF grade ON enrollments FOR EACH ROW EXECUTE FUNCTION ...;`
    4. `UPDATE enrollments SET grade = 91 WHERE id = ...;` then `SELECT * FROM grade_audit;`
    5. **Done when:** Audit row shows old and new grades after the update.

- [ ] **Day 25 — Window functions**
  - [ ] **Learn:** [Window functions (tutorial)](https://www.postgresql.org/docs/current/tutorial-window.html) · [Window functions](https://www.postgresql.org/docs/current/functions-window.html)
  - [ ] **Practice:** Rank grades within each course
    1. Run:
       ```sql
       SELECT e.course_id, s.last_name, e.grade,
              RANK() OVER (PARTITION BY e.course_id ORDER BY e.grade DESC NULLS LAST) AS rnk
       FROM enrollments e
       JOIN students s ON s.id = e.student_id;
       ```
    2. Add `ROW_NUMBER()` in a second query and note how ties differ from `RANK()`.
    3. Filter to one course in an outer query / CTE where `rnk <= 3`.
    4. Save to `queries/12_windows.sql`.
    5. **Done when:** Ranking query runs and you can explain RANK vs ROW_NUMBER on a tie.

- [ ] **Day 26 — Roles & privileges**
  - [ ] **Learn:** [Database roles](https://www.postgresql.org/docs/current/user-manag.html) · [GRANT](https://www.postgresql.org/docs/current/sql-grant.html)
  - [ ] **Practice:** Read-only role against views
    1. As superuser: `CREATE ROLE learn_readonly LOGIN PASSWORD 'readonly_pass';`
    2. `GRANT CONNECT ON DATABASE learn_sql TO learn_readonly;` `GRANT USAGE ON SCHEMA public TO learn_readonly;`
    3. `GRANT SELECT ON v_student_enrollments, v_course_roster TO learn_readonly;`
    4. Connect: `psql -U learn_readonly -d learn_sql` — `SELECT` from a view works; `INSERT INTO students` fails.
    5. **Done when:** Read works on granted views and write attempt is denied.

- [ ] **Day 27 — Backup & restore**
  - [ ] **Learn:** [pg_dump](https://www.postgresql.org/docs/current/app-pgdump.html) · [Restoring](https://www.postgresql.org/docs/current/backup-dump.html)
  - [ ] **Practice:** Dump, drop, restore, verify counts
    1. Note counts: `SELECT COUNT(*) FROM students, courses, enrollments;` (three queries).
    2. `pg_dump -U postgres -d learn_sql -f backup_learn_sql.sql`
    3. `DROP DATABASE learn_sql;` then `CREATE DATABASE learn_sql;`
    4. `psql -U postgres -d learn_sql -f backup_learn_sql.sql`
    5. Re-check counts match step 1.
    6. **Done when:** Counts match pre-dump and backup file exists on disk.

- [ ] **Day 28 — Mastery dry-run & mini-projects**
  - [ ] **Learn:** [PostgreSQL docs home](https://www.postgresql.org/docs/current/) · [SQL commands](https://www.postgresql.org/docs/current/sql-commands.html)
  - [ ] **Practice:** Checklist dry-run and finish Mini-projects A+B
    1. Walk §12 mastery checklist; tick honest yes/no.
    2. Finish Mini-project A script pack (`01`–`04`) if gaps remain.
    3. Finish Mini-project B inventory (or C audit) enough to meet gate.
    4. Write a short “gaps before JDBC” note (max 10 lines).
    5. **Done when:** Checklist ≥90% intent met and A+B (or A+C) scripts run from scratch.

*If on a 3-week pace: merge Week 4 admin/triggers into Days 18–21 and keep one procedure + one trigger only.*

---

## 5. Hands-on exercises

### SQL practice questions (run against your student/course schema)

**Warm-ups**

1. List all students ordered by last name.
2. Insert 5 students and 4 courses; verify with `COUNT(*)`.
3. Update one student’s email; delete a student with no enrollments.
4. Find students whose email ends with `@example.com` (`ILIKE`).
5. List courses with credits ≥ 3, ordered by title.

**Joins & aggregates**

6. List each enrollment as: student name, course title, enrolled_on.
7. Count enrollments per course (include courses with zero via `LEFT JOIN`).
8. Students enrolled in more than one course.
9. Average grade per course (ignore null grades).
10. Top 3 courses by enrollment count.
11. Students who share at least one course with a given student id (self-join / subquery).
12. Courses that have no enrollments.

**Harder queries**

13. For each student: total courses, average grade, latest enrollment date.
14. Students whose average grade is above the overall average.
15. Using a CTE: roster for a course with row numbers ordered by last name.
16. Find duplicate emails if unique constraint were missing (group + having).
17. `EXPLAIN ANALYZE` a slow-ish join; add an index; re-run and note the change.

### Schema design tasks

**A. Normalize a flat table**

Given a denormalized dump like:

| student_name | student_email | course_title | instructor_name | instructor_email | grade |

Design 3NF tables (`students`, `instructors`, `courses`, `enrollments`), write `CREATE TABLE` + seed, and prove you can reconstruct the flat report with joins.

**B. Library-style (optional bridge from Phase 1)**

Design `books`, `members`, `loans` with FKs, unique ISBN, and a rule that a loan references existing book + member. Write queries: overdue loans (due_date < CURRENT_DATE), books never borrowed.

**C. Capacity & waitlist**

Add `courses.capacity` and `waitlist` table. Design constraints so enrollment count cannot exceed capacity *in application SQL*—document what the DB can and cannot enforce alone (leads into procedures/triggers).

### Constraint & integrity drills

18. Try inserting an enrollment with a fake `student_id` — confirm FK error.
19. Try two enrollments of the same student into the same course — confirm unique violation.
20. Add `CHECK (grade IS NULL OR (grade >= 0 AND grade <= 100))` and test boundaries.

---

## 6. Mini-projects

> **Build guides (checkboxes + steps):** open the linked markdown before coding.

### A. Campus Registry SQL Pack

**Guide:** [A — Campus Registry SQL Pack](../mini-projects/phase-02-data/07-sql-postgresql/A-campus-registry-sql-pack.md)

- Scripts: `01_schema.sql`, `02_seed.sql`, `03_queries.sql`, `04_views.sql`
- Entities: students, courses, instructors (optional), enrollments
- At least 15 seed students, 8 courses, 40+ enrollments
- Query pack covering join, aggregate, left join, subquery, CTE
- README: how to create DB and run scripts in order

### B. Inventory Lite (SQL-only)

**Guide:** [B — Inventory Lite](../mini-projects/phase-02-data/07-sql-postgresql/B-inventory-lite.md)

- Tables: `products`, `warehouses`, `stock_levels` (product_id, warehouse_id, qty)
- Constraints: non-negative qty; unique (product, warehouse)
- Queries: low-stock report; transfer stock between warehouses inside a transaction
- View: `v_inventory_value` (qty × unit_price)

### C. (Optional stretch) Audit-ready grades

**Guide:** [C — Audit-Ready Grades](../mini-projects/phase-02-data/07-sql-postgresql/C-audit-ready-grades.md)

- `grade_audit` table + trigger on `enrollments` grade changes
- Function to recompute a student’s average
- Dump/restore once to prove recoverability

---

## 7. Industry use cases

- **SaaS multi-tenant apps:** Accounts, users, subscriptions as related tables with FKs
- **E-commerce:** Orders, line items, inventory — classic 1:N and transactions
- **Education / LMS:** Students, courses, enrollments (this phase’s domain)
- **Reporting & analytics:** Aggregations, views, and indexed filter columns
- **Ops & compliance:** Roles/grants, audit triggers, backups (`pg_dump`)
- **Later Spring/JPA:** Entity mappings mirror well-designed SQL schemas

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| `SELECT *` everywhere in “final” scripts | Project only needed columns |
| Forgetting `WHERE` on `UPDATE`/`DELETE` | Always filter; practice in a transaction first |
| Using `=` with `NULL` | Use `IS NULL` / `IS NOT NULL` |
| Putting filter conditions only in `ON` vs `WHERE` carelessly on outer joins | Know how each changes `LEFT JOIN` results |
| Storing comma-separated lists in one column | Use junction tables (N:M) |
| No primary keys / surrogate keys everywhere without thought | Every table needs a clear identity strategy |
| Indexing every column “just in case” | Index FK/join/filter columns you actually query |
| String-concatenating dynamic SQL later in JDBC | Learn parameters now; never trust raw input |
| One giant unnormalized table | Normalize first; denormalize only with a reason |
| Ignoring transaction boundaries on multi-step writes | `BEGIN` … success `COMMIT` / failure `ROLLBACK` |
| Hard-deleting without understanding FKs | Decide cascade vs restrict deliberately |
| Learning only a GUI click-path | Keep `psql` + `.sql` files as source of truth |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [PostgreSQL Official Documentation](https://www.postgresql.org/docs/current/) | Source of truth for syntax & behavior |
| [PostgreSQL Tutorial (official-adjacent community hub)](https://www.postgresqltutorial.com/) | Guided free SQL/Postgres practice |
| [SQLBolt](https://sqlbolt.com/) | Interactive SQL basics (free) |
| [Mode SQL Tutorial](https://mode.com/sql-tutorial/) | Free narrative SQL lessons |
| [Select Star SQL](https://selectstarsql.com/) | Free story-driven SQL practice |
| [pgExercises](https://pgexercises.com/) | Free Postgres-flavored exercises |
| [Use The Index, Luke](https://use-the-index-luke.com/) | Free, deep-but-practical indexing |
| [Docker Hub — postgres image](https://hub.docker.com/_/postgres) | Optional local Postgres via Docker |

Prefer official Postgres docs when syntax disagrees with a tutorial.

---

## 10. Documentation links (real URLs)

> **Primary use:** Learn links are already on each day in **§4**. Full index:

- PostgreSQL docs home (current): https://www.postgresql.org/docs/current/
- SQL commands reference: https://www.postgresql.org/docs/current/sql-commands.html
- `CREATE TABLE`: https://www.postgresql.org/docs/current/sql-createtable.html
- Constraints: https://www.postgresql.org/docs/current/ddl-constraints.html
- `SELECT`: https://www.postgresql.org/docs/current/sql-select.html
- Joins: https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-JOIN
- Aggregates: https://www.postgresql.org/docs/current/tutorial-agg.html
- Indexes: https://www.postgresql.org/docs/current/indexes.html
- Views: https://www.postgresql.org/docs/current/sql-createview.html
- Transactions: https://www.postgresql.org/docs/current/tutorial-transactions.html
- `WITH` (CTEs): https://www.postgresql.org/docs/current/queries-with.html
- `INSERT ... ON CONFLICT`: https://www.postgresql.org/docs/current/sql-insert.html
- `CREATE FUNCTION`: https://www.postgresql.org/docs/current/sql-createfunction.html
- Triggers: https://www.postgresql.org/docs/current/triggers.html
- Window functions: https://www.postgresql.org/docs/current/tutorial-window.html
- `EXPLAIN`: https://www.postgresql.org/docs/current/sql-explain.html
- Roles & privileges: https://www.postgresql.org/docs/current/user-manag.html
- `pg_dump`: https://www.postgresql.org/docs/current/app-pgdump.html
- `psql`: https://www.postgresql.org/docs/current/app-psql.html
- Data types: https://www.postgresql.org/docs/current/datatype.html
- Download PostgreSQL: https://www.postgresql.org/download/

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Install & connect | Local Postgres usable daily | Create/drop DB; run `.sql` files |
| 2. DDL + CRUD | Tables with constraints | Violations fail as expected |
| 3. Joins + aggregates | Multi-table questions | 10 join/aggregate queries correct |
| 4. Design | Normalize messy data | Schema design tasks A (+ B optional) |
| 5. Views / indexes / tx | Readable + safer writes | View pack + one `EXPLAIN` win + rollback demo |
| 6. Functions / triggers / admin | Ops literacy | One function, one trigger, dump/restore |
| 7. Gate | Ready for JDBC | Mastery checklist ≥90%; Mini-projects A+B done |

**Weekly rhythm:** Mon–Fri new SQL skills; Sat mixed query set; Sun refactor scripts + ER diagram cleanup.

**Suggested script layout:**

```text
sql-practice/
  01_schema.sql
  02_seed.sql
  03_queries.sql
  04_views_indexes.sql
  05_functions_triggers.sql
  notes.md
```

---

## 12. Mastery checklist

- [ ] PostgreSQL installed (or Docker); can connect with `psql` (or documented GUI + scripts)
- [ ] Explains tables, PK, FK, and 1:N / N:M in plain language
- [ ] Chooses sensible Postgres types for ids, text, money-like amounts, dates
- [ ] Writes DDL with `NOT NULL`, `UNIQUE`, `CHECK`, PK, FK
- [ ] Performs INSERT / SELECT / UPDATE / DELETE confidently with safe `WHERE` usage
- [ ] Uses `ORDER BY`, `LIMIT`, `LIKE`/`ILIKE`, `IN`, `BETWEEN`, null-safe filters
- [ ] Writes `INNER` and `LEFT` joins correctly; explains the difference with an example
- [ ] Uses `GROUP BY` / `HAVING` and common aggregates
- [ ] Writes at least one subquery and one CTE that you can explain
- [ ] Creates and queries views for recurring report shapes
- [ ] Adds a useful index and shows `EXPLAIN` difference on one query
- [ ] Demonstrates `BEGIN` / `COMMIT` / `ROLLBACK` for a multi-statement change
- [ ] Uses `INSERT ... ON CONFLICT` at least once
- [ ] Writes one SQL function and one trigger (audit or validation-style)
- [ ] Creates a restricted DB role and grants appropriate privileges
- [ ] Backs up and restores a learning database with `pg_dump` / `psql`
- [ ] Completes schema design task A (normalize flat data)
- [ ] Completes Mini-projects A and B (or A + C)
- [ ] Keeps all practice in versionable `.sql` files (not only GUI history)
- [ ] Ready to start [08-jdbc.md](08-jdbc.md)
