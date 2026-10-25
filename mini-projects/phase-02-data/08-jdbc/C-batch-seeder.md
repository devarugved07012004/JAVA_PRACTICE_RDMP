# Mini-Project C — Batch Seeder (stretch)
**Phase:** 2 — Data  
**Topic:** [08 — JDBC](../../../phase-02-data/08-jdbc.md)  
**Time:** ~3–5 hours

## Goal

Read CSV files of students (and optionally courses/enrollments), batch-insert with JDBC `addBatch` / `executeBatch`, commit every N rows, use HikariCP, and make re-runs idempotent with `ON CONFLICT DO NOTHING`.

## Prerequisites

- [ ] Mini-projects A/B patterns (DataSource, PreparedStatement)
- [ ] Topic 08 Day 11 (batch) and Day 12 (HikariCP)
- [ ] Target tables with unique keys (`students.email`, `courses.code`, etc.)

## Learn (open before coding)

- [Using Batch Updates](https://docs.oracle.com/javase/tutorial/jdbc/basics/batch.html)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP/wiki/Configuration)
- [INSERT … ON CONFLICT](https://www.postgresql.org/docs/current/sql-insert.html)
- [PostgreSQL JDBC Connecting](https://jdbc.postgresql.org/documentation/use/)

## Project layout

```text
batch-seeder/
  pom.xml
  README.md
  data/
    students.csv
    courses.csv           # optional
    enrollments.csv       # optional
  src/main/java/.../seeder/
    SeederApp.java
    config/DataSourceFactory.java
    StudentCsvSeeder.java
```

**Example `students.csv`:**

```text
first_name,last_name,email
Ada,Lovelace,ada@example.com
Alan,Turing,alan@example.com
```

**JDBC URL:** `jdbc:postgresql://localhost:5432/campus_registry`

## Build steps

### Step 1 — HikariCP DataSource
- [ ] Add dependencies: `postgresql`, `HikariCP`
- [ ] Configure:
  ```text
  jdbcUrl=jdbc:postgresql://localhost:5432/campus_registry
  username=...
  password=...
  maximumPoolSize=5
  ```
- [ ] Load from env vars; never commit secrets
- **Done when:** `SELECT 1` works through HikariCP.

### Step 2 — CSV sample data
- [ ] Create `data/students.csv` with header + ≥100 rows (generator script or copy-paste OK)
- [ ] Unique emails; realistic names
- [ ] Optional: `courses.csv` with unique `code`
- **Done when:** CSV opens cleanly; row count known.

### Step 3 — Parse CSV safely
- [ ] Read lines; skip header; split carefully (simple CSV without embedded commas is fine for learning)
- [ ] Validate required fields; skip or fail-fast on bad rows (document choice)
- [ ] Do **not** build SQL by concatenating CSV fields
- **Done when:** Parser yields a list/stream of field tuples ready for binding.

### Step 4 — Batch insert SQL
- [ ] Prepare:
  ```sql
  INSERT INTO students (first_name, last_name, email)
  VALUES (?, ?, ?)
  ON CONFLICT (email) DO NOTHING
  ```
- [ ] Loop: `setString` × 3 → `addBatch()`
- [ ] `int[] counts = ps.executeBatch();`
- **Done when:** One batch run inserts new rows without SQL injection risk.

### Step 5 — Commit every N rows
- [ ] `conn.setAutoCommit(false)`
- [ ] Choose `N` (e.g. 50 or 100): after every N `addBatch` groups (or every N rows before `executeBatch`), `commit()`
- [ ] On error: `rollback()` current chunk; log which row range failed
- [ ] Final `commit()` for remainder
- **Done when:** Large file loads in chunks; failure of one chunk does not require redoing everything blindly (document restart strategy).

### Step 6 — Idempotent re-run
- [ ] Run seeder twice on the same CSV
- [ ] Second run: row count for `students` unchanged (or only truly new emails added)
- [ ] Print summary: attempted, inserted (estimate from batch counts), skipped
- **Done when:** Re-run is safe thanks to `ON CONFLICT DO NOTHING`.

### Step 7 — Optional courses / enrollments batches
- [ ] Batch-insert courses with `ON CONFLICT (code) DO NOTHING`
- [ ] Enrollment batch needs real student/course ids — resolve by email/code lookups first, or seed ids in CSV carefully
- [ ] Prefer separate pass: load students → load courses → load enrollments
- **Done when:** Optional second entity type seeds cleanly, or README marks students-only scope.

### Step 8 — Timing & README
- [ ] Log elapsed millis for the full seed
- [ ] README: CSV format, env vars, `N` commit size, how to reset table for a full reload demo
- [ ] Warn: do not point this at production DBs
- **Done when:** Documented run produces measurable insert throughput on your machine.

## Definition of Done

- [ ] CSV of students read from disk
- [ ] Batch insert via `addBatch` / `executeBatch`
- [ ] Periodic commit every N rows inside an explicit transaction strategy
- [ ] HikariCP-backed `DataSource`
- [ ] Idempotent re-run (`ON CONFLICT DO NOTHING` or equivalent)
- [ ] README with format + run steps

## Demo script

1. Note `SELECT COUNT(*) FROM students;`
2. Run seeder on `students.csv`
3. Show new count and elapsed time
4. Run seeder again — count stable; summary shows skips
5. Optional: `TRUNCATE students CASCADE;` (learning DB only) and full reload

## Common mistakes

| Mistake | Fix |
|--------|-----|
| One giant auto-commit per row | Batch + chunked commit |
| String-concat INSERT from CSV | Always bind `?` parameters |
| No unique key → duplicates on re-run | Unique email + `ON CONFLICT` |
| New Hikari pool per batch | One DataSource for the app |
| Holding one TX open for millions of rows without chunking | Commit every N to bound WAL/memory risk in learning demos |

## After this

- Apply batch ideas in Phase 2 project seed if useful
- Continue to [student-course-jdbc.md](../phase-project/student-course-jdbc.md)
