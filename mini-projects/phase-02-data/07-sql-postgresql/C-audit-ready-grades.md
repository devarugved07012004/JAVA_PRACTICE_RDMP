# Mini-Project C — Audit-Ready Grades (stretch)
**Phase:** 2 — Data  
**Topic:** [07 — SQL & PostgreSQL](../../../phase-02-data/07-sql-postgresql.md)  
**Time:** ~3–5 hours

## Goal

Add operational depth on top of the campus registry: an audit trail for grade changes (trigger), a SQL function to recompute a student’s average, and a dump/restore proof that the learning DB is recoverable.

## Prerequisites

- [ ] Mini-project A schema/seed available (`campus_registry` or equivalent)
- [ ] Topic 07 Days 22–24 and 27 concepts reviewed (functions, triggers, `pg_dump`)
- [ ] Comfortable editing `schema` scripts without breaking FKs

## Learn (open before coding)

- [Triggers](https://www.postgresql.org/docs/current/triggers.html)
- [Trigger functions (PL/pgSQL)](https://www.postgresql.org/docs/current/plpgsql-trigger.html)
- [CREATE FUNCTION](https://www.postgresql.org/docs/current/sql-createfunction.html)
- [pg_dump](https://www.postgresql.org/docs/current/app-pgdump.html)
- [Restoring dumps](https://www.postgresql.org/docs/current/backup-dump.html)

## Project layout

```text
campus-registry-sql/          # extend A, or copy into audit-grades/
  01_schema.sql               # already has enrollments
  02_seed.sql
  05_functions_triggers.sql   # NEW
  06_audit_demo.sql           # NEW
  backups/
    campus_registry_before.sql
  README.md                   # add audit + restore section
```

## Build steps

### Step 1 — Baseline counts
- [ ] Connect to the campus DB; record counts:
  ```sql
  SELECT COUNT(*) FROM students;
  SELECT COUNT(*) FROM courses;
  SELECT COUNT(*) FROM enrollments;
  SELECT COUNT(*) FROM enrollments WHERE grade IS NOT NULL;
  ```
- [ ] Pick one enrollment id with a non-null grade for demos; note `id`, old grade
- **Done when:** Baseline numbers and a demo enrollment id are written in notes/README.

### Step 2 — Audit table
- [ ] In `05_functions_triggers.sql`:
  ```sql
  CREATE TABLE IF NOT EXISTS grade_audit (
    id             BIGSERIAL PRIMARY KEY,
    enrollment_id  BIGINT NOT NULL REFERENCES enrollments(id),
    old_grade      NUMERIC(5,2),
    new_grade      NUMERIC(5,2),
    changed_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
  );
  ```
- [ ] Apply; `\d grade_audit`
- **Done when:** Table exists with FK to `enrollments`.

### Step 3 — Trigger function
- [ ] Add:
  ```sql
  CREATE OR REPLACE FUNCTION log_grade_change()
  RETURNS TRIGGER AS $$
  BEGIN
    IF TG_OP = 'UPDATE' AND OLD.grade IS DISTINCT FROM NEW.grade THEN
      INSERT INTO grade_audit (enrollment_id, old_grade, new_grade)
      VALUES (NEW.id, OLD.grade, NEW.grade);
    END IF;
    RETURN NEW;
  END;
  $$ LANGUAGE plpgsql;
  ```
- [ ] Use `IS DISTINCT FROM` so NULL ↔ value changes are audited
- **Done when:** Function created (`\df log_grade_change`).

### Step 4 — Attach trigger
- [ ] Create trigger:
  ```sql
  DROP TRIGGER IF EXISTS trg_grade_audit ON enrollments;
  CREATE TRIGGER trg_grade_audit
  AFTER UPDATE OF grade ON enrollments
  FOR EACH ROW
  EXECUTE FUNCTION log_grade_change();
  ```
  *(On older Postgres docs you may see `EXECUTE PROCEDURE` — use what your version accepts.)*
- [ ] Verify `\d enrollments` shows the trigger
- **Done when:** Trigger is attached to `UPDATE OF grade`.

### Step 5 — Prove audit on UPDATE
- [ ] In `06_audit_demo.sql`:
  ```sql
  UPDATE enrollments SET grade = 91.00 WHERE id = 1;  -- use your id
  SELECT * FROM grade_audit ORDER BY id DESC LIMIT 5;
  ```
- [ ] Update the same row again to a different grade; confirm a second audit row
- [ ] Update a non-grade column only (e.g. `enrolled_on`) — confirm **no** new audit row
- **Done when:** Grade changes leave old/new audit rows; unrelated updates do not.

### Step 6 — Average-grade function
- [ ] Add:
  ```sql
  CREATE OR REPLACE FUNCTION student_avg_grade(p_student_id BIGINT)
  RETURNS NUMERIC AS $$
    SELECT ROUND(AVG(grade), 2)
    FROM enrollments
    WHERE student_id = p_student_id
      AND grade IS NOT NULL;
  $$ LANGUAGE sql;
  ```
- [ ] Compare:
  ```sql
  SELECT student_avg_grade(1);
  SELECT ROUND(AVG(grade), 2) FROM enrollments WHERE student_id = 1 AND grade IS NOT NULL;
  ```
- [ ] Call for several students: `SELECT id, student_avg_grade(id) FROM students LIMIT 5;`
- **Done when:** Function matches hand-written `AVG` for a known student.

### Step 7 — Optional: reject illegal grades in trigger
- [ ] Stretch: `BEFORE UPDATE OF grade` that `RAISE EXCEPTION` if `NEW.grade` outside 0–100 (in addition to CHECK)
- [ ] Or document that CHECK already covers it and skip
- **Done when:** Either BEFORE trigger demo works or README explains CHECK-only policy.

### Step 8 — Dump
- [ ] From shell (adjust user/path):
  ```text
  pg_dump -U postgres -d campus_registry -f backups/campus_registry_before.sql
  ```
- [ ] Confirm file size > 0; open top of file and see `CREATE TABLE` / `COPY` / inserts
- [ ] Note row counts again immediately before dump
- **Done when:** Backup file exists and counts are recorded.

### Step 9 — Drop, restore, verify
- [ ] As superuser (careful — learning DB only):
  ```text
  # disconnect other sessions first if needed
  psql -U postgres -c "DROP DATABASE campus_registry;"
  psql -U postgres -c "CREATE DATABASE campus_registry;"
  psql -U postgres -d campus_registry -f backups/campus_registry_before.sql
  ```
- [ ] Re-check table counts, `grade_audit` rows, and `SELECT student_avg_grade(...);`
- [ ] Confirm trigger still fires on a new grade update
- **Done when:** Counts match pre-dump and audit/function still work after restore.

### Step 10 — README section
- [ ] Document: how to apply `05`/`06`, how to dump/restore, what audit captures
- [ ] Note Postgres version and any `EXECUTE FUNCTION` vs `PROCEDURE` quirk
- **Done when:** Restore steps are copy-pasteable from README.

## Definition of Done

- [ ] `grade_audit` table + AFTER UPDATE trigger on `enrollments.grade`
- [ ] Updating a grade inserts old/new audit rows
- [ ] `student_avg_grade(student_id)` returns correct average
- [ ] `pg_dump` + restore once; counts and behavior verified
- [ ] Scripts committed; README updated

## Demo script

1. Show current grade for one enrollment
2. `UPDATE` grade; `SELECT * FROM grade_audit ORDER BY id DESC LIMIT 3;`
3. `SELECT student_avg_grade(<id>);` vs manual AVG
4. Mention backup file path; show restore was already proven (or live restore if time)

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Trigger on all UPDATE columns | Prefer `UPDATE OF grade` + `IS DISTINCT FROM` |
| Auditing before knowing OLD/NEW | Use AFTER ROW trigger; read `OLD`/`NEW` carefully |
| Function includes NULL grades in AVG | Filter `grade IS NOT NULL` (AVG already skips NULL — still be explicit) |
| Dumping while connected wrongly | Specify `-d campus_registry`; restore into empty DB |
| Committing real passwords in dump notes | Never paste credentials into README |

## After this

- Counts toward topic 07 gate as **A + C** alternative to A + B
- Next: [08 JDBC](../../../phase-02-data/08-jdbc.md) — call `student_avg_grade` via `CallableStatement`
