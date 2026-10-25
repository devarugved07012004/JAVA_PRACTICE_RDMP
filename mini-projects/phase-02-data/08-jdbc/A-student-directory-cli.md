# Mini-Project A — JDBC Student Directory CLI
**Phase:** 2 — Data  
**Topic:** [08 — JDBC](../../../phase-02-data/08-jdbc.md)  
**Time:** ~5–7 hours

## Goal

Build a console app that lists, adds, updates email, and deletes students via a `StudentDao` JDBC implementation — parameterized SQL, try-with-resources, credentials from the environment.

## Prerequisites

- [ ] Topic 07 schema with a `students` table (campus registry or `learn_sql`)
- [ ] JDK 17+; Maven or Gradle project
- [ ] PostgreSQL JDBC driver on the classpath
- [ ] Topic 08 Days 1–6 practiced (connect, PreparedStatement, DAO)

## Learn (open before coding)

- [Connecting to the Database](https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html)
- [PreparedStatement](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html)
- [Try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html)
- [PostgreSQL JDBC — Connecting](https://jdbc.postgresql.org/documentation/use/)
- [RETURN_GENERATED_KEYS](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/Statement.html#RETURN_GENERATED_KEYS)

## Project layout

```text
student-directory-cli/
  pom.xml                         # or build.gradle
  README.md
  db.properties.example           # no real passwords
  .gitignore                      # ignore db.properties / .env
  src/main/java/com/yourname/directory/
    app/Main.java
    app/ConsoleMenu.java
    config/DbConfig.java
    model/Student.java
    dao/StudentDao.java
    dao/JdbcStudentDao.java
    exception/DataAccessException.java
```

**Example JDBC URL:**

```text
jdbc:postgresql://localhost:5432/campus_registry
```

**Env vars (preferred):**

```text
DB_URL=jdbc:postgresql://localhost:5432/campus_registry
DB_USER=postgres
DB_PASSWORD=********
```

## Build steps

### Step 1 — Project + driver
- [ ] Create Maven/Gradle app; add:
  ```xml
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.4</version>
  </dependency>
  ```
- [ ] Add `.gitignore` entries for `db.properties`, `.env`
- [ ] Copy `db.properties.example` with placeholder keys only
- **Done when:** Project builds and driver dependency resolves.

### Step 2 — Config loader
- [ ] Implement `DbConfig` reading `DB_URL` / `DB_USER` / `DB_PASSWORD` (fallback: local untracked properties)
- [ ] Never print the password
- [ ] Document required env vars in README
- **Done when:** Config loads without hard-coded secrets in source.

### Step 3 — Smoke connection
- [ ] In a small demo (or Main temporarily):
  ```java
  try (Connection c = DriverManager.getConnection(url, user, password);
       PreparedStatement ps = c.prepareStatement("SELECT version()");
       ResultSet rs = ps.executeQuery()) {
    if (rs.next()) System.out.println(rs.getString(1));
  }
  ```
- [ ] Confirm Postgres version prints
- **Done when:** Java prints a version string against your DB.

### Step 4 — Student model + DAO interface
- [ ] `Student` record/class: `long id`, `String firstName`, `String lastName`, `String email`
- [ ] Interface methods: `List<Student> findAll()`, `Optional<Student> findById(long id)`, `Student insert(...)`, `boolean updateEmail(long id, String email)`, `boolean delete(long id)`
- **Done when:** Interface compiles; model matches table columns.

### Step 5 — JdbcStudentDao: findAll / findById
- [ ] Implement SELECT with named columns (no `SELECT *` in final code):
  ```sql
  SELECT id, first_name, last_name, email FROM students ORDER BY id
  ```
- [ ] Map `ResultSet` → `Student` inside try-with-resources
- [ ] `findById` uses `WHERE id = ?` + `setLong`
- [ ] Smoke-test from a temporary main
- **Done when:** Printed list matches `psql` count for `students`.

### Step 6 — Insert with generated keys
- [ ] `INSERT INTO students (first_name, last_name, email) VALUES (?, ?, ?)`
- [ ] `prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)`
- [ ] Read generated `id`; return populated `Student`
- [ ] Verify in `psql`: `SELECT * FROM students WHERE id = <id>;`
- **Done when:** Java prints new id and `psql` shows the row.

### Step 7 — Update email & delete
- [ ] `UPDATE students SET email = ? WHERE id = ?` — print/return `executeUpdate()` count
- [ ] `DELETE FROM students WHERE id = ?` — expect `1` then `0` on second call
- [ ] Catch one deliberate bad SQL once; log `getSQLState()` in notes (not in menu forever)
- **Done when:** Update count 1; delete counts 1 then 0.

### Step 8 — Console menu (no SQL in UI)
- [ ] Menu loop:
  1. List students  
  2. Add student  
  3. Update email  
  4. Delete student  
  5. Exit  
- [ ] `ConsoleMenu` calls only `StudentDao` methods
- [ ] Grep: no SQL strings in `app/` package
- **Done when:** All four operations work from the menu against real data.

### Step 9 — Exception wrapping
- [ ] Wrap `SQLException` in `DataAccessException` (unchecked or checked — pick one and stay consistent)
- [ ] On unique email violation (SQLState `23505`), print a friendly “email already exists” message
- **Done when:** Duplicate email shows a clear message without raw stack-only UX.

### Step 10 — README & hygiene
- [ ] README: JDK version, create/use which DB, env vars, run command (`mvn -q exec:java` or similar)
- [ ] Grep for password-looking strings; remove any committed secrets
- [ ] Add short demo script (numbered menu choices)
- **Done when:** Fresh clone + env vars + README is enough to run.

## Definition of Done

- [ ] Console menu: list / add / update email / delete
- [ ] `StudentDao` + `JdbcStudentDao` with parameterized SQL only
- [ ] Config from environment (or untracked local file)
- [ ] Try-with-resources on Connection / PreparedStatement / ResultSet
- [ ] README with run instructions; no secrets in Git

## Demo script

1. Export `DB_URL`, `DB_USER`, `DB_PASSWORD`
2. Start app; choose **List** — show seed students
3. **Add** a student; **List** again — new row appears
4. **Update email**; confirm in menu and optionally `psql`
5. **Delete** that student; list no longer shows them

## Common mistakes

| Mistake | Fix |
|--------|-----|
| SQL concatenation with names/emails | Always `?` + setters |
| Leaving connections open | Try-with-resources every path |
| SQL inside `ConsoleMenu` | DAO only |
| Committing `db.properties` with password | `.gitignore` + example file only |
| Ignoring duplicate email errors | Map SQLState `23505` to a clear message |

## After this

- Build [B — Enrollment transactions](B-enrollment-transactions.md)
- Optional: [C — Batch seeder](C-batch-seeder.md)
