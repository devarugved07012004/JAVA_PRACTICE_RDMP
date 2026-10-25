# Mini-project C — Migration starter (optional)

**Topic:** [14 — JPA & Hibernate](../../../phase-04-spring/14-jpa-hibernate.md)

---

## Goal

Introduce Flyway: baseline schema as versioned SQL, and disable reckless `ddl-auto` for that profile so schema changes are intentional.

---

## Prerequisites

- Working JPA app ([A](A-catalog-orders-jpa.md) or [B](B-blog-notes-tags.md) preferred)
- Postgres
- Willingness to reset a **dev** database once

---

## Learn links

| Resource | Why |
|----------|-----|
| [Flyway docs](https://documentation.red-gate.com/fd) | Migrations |
| [Spring Boot — Flyway](https://docs.spring.io/spring-boot/reference/howto/data-initialization.html#howto.data-initialization.migration-tool.flyway) | Boot integration |
| [Hibernate ddl-auto](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data) | Why not `create` in shared envs |

### start.spring.io settings

Add to existing app **or** regenerate:

| Field | Value |
|-------|--------|
| Dependencies | existing + **Flyway Migration** + **PostgreSQL** |
| Package | keep `com.learn.catalogorders` (or your app) |

---

## Project layout

```text
src/main/resources/
  application.yml                 # shared defaults
  application-migrate.yml         # ddl-auto: validate (or none)
  db/migration/
    V1__baseline_schema.sql
    V2__add_product_sku_index.sql # optional second change
README.md                         # migration profile instructions
```

---

## Build steps

- [ ] **1. Add Flyway dependency**
  1. `org.flywaydb:flyway-core` (+ `flyway-database-postgresql` if required by your Boot/Flyway version).
  2. **Done when:** Dependency resolves.

- [ ] **2. Export baseline SQL**
  1. From a known-good schema (or write by hand): tables for your entities + FKs.
  2. File: `V1__baseline_schema.sql` (Flyway naming: `V<version>__<desc>.sql`).
  3. **Done when:** SQL runs cleanly in `psql` on an empty DB.

- [ ] **3. Profile `migrate` (or `prod`-like)**
  1. `spring.jpa.hibernate.ddl-auto: validate` (or `none`).
  2. `spring.flyway.enabled: true`.
  3. Keep a `dev` profile with `ddl-auto: update` **only** if you still want Hibernate to invent schema — document that migrate profile is the disciplined path.
  4. **Done when:** Two profiles documented with different ddl-auto.

- [ ] **4. Empty DB + first migrate**
  1. Drop/recreate DB `catalog_orders` (dev only!).
  2. Start with `--spring.profiles.active=migrate`.
  3. Confirm `flyway_schema_history` table and app tables exist.
  4. **Done when:** App starts; `ddl-auto` did not create tables — Flyway did.

- [ ] **5. Second migration**
  1. `V2__…sql` adds an index or nullable column.
  2. Restart; Flyway applies V2 once.
  3. Restart again → no re-apply.
  4. **Done when:** History shows V1+V2; second boot is no-op.

- [ ] **6. Failure drill**
  1. Break V2 SQL; start → app fails fast.
  2. Fix SQL; repair per Flyway docs if needed; succeed.
  3. **Done when:** You saw migrate-on-startup failure and recovery once.

- [ ] **7. README**
  1. When to use `migrate` vs loose `dev`.
  2. How to add `V3__…`.
  3. Warning: never `ddl-auto=create` on shared data.
  4. **Done when:** README alone explains the workflow.

---

## Definition of Done

- [ ] Flyway on the classpath; migrations in `db/migration`
- [ ] Baseline schema versioned
- [ ] Profile disables reckless `ddl-auto` (`validate`/`none`)
- [ ] Fresh DB boots via migrations
- [ ] At least one follow-up migration applied
- [ ] README documents profile + how to add versions

---

## Demo script

```bash
dropdb catalog_orders; createdb catalog_orders

java -jar app.jar --spring.profiles.active=migrate
# → Flyway runs V1 (and V2); app UP

psql -d catalog_orders -c "SELECT version, description, success FROM flyway_schema_history;"
# → rows for V1/V2

curl -i http://localhost:8080/actuator/health
# → 200 UP  (if Actuator present)

# Restart — no pending migrations
java -jar app.jar --spring.profiles.active=migrate
# → starts cleanly
```

HTTP optional; success is schema history + app start.

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Editing applied migration files | Always add a new version |
| `ddl-auto=update` + Flyway fighting | One owner of schema per profile |
| Wrong naming `v1_baseline.sql` | Use `V1__baseline_schema.sql` |
| Running dropdb on shared/prod | Dev DB only |

---

## After this

Topic 14 mastery checklist. Start the phase deliverable: [BookStore API](../phase-project/bookstore-api.md).
