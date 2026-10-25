# 14 — JPA & Hibernate

**Duration:** ~3–4 weeks (21–28 days) · **Cadence:** 1 hour/day  
**Focus:** Entities, Relationships, CRUD, JPQL, HQL, Transactions, Cascade Types, Fetch Types, Pagination, Auditing

---

## 1. Why this topic is important

JPA (Jakarta Persistence) is the standard API for mapping Java objects to relational tables; Hibernate is the most common JPA provider. Together they replace hand-written JDBC for everyday CRUD while still demanding SQL literacy: you must understand joins, lazy loading, transactions, and when the ORM generates poor queries.

In Spring Boot, Spring Data JPA repositories turn interfaces into persistence operations, but **entities, relationships, fetch types, cascades, and `@Transactional` boundaries** decide whether your API is correct and fast—or full of `LazyInitializationException`, N+1 queries, and accidental deletes. This topic prepares you to persist the BookStore domain (books, users, orders) on PostgreSQL with confidence.

---

## 2. Prerequisites

- [12-spring-boot.md](12-spring-boot.md) complete; Security (13) recommended before or in parallel for user entities
- Phase 2 SQL & PostgreSQL fluency: keys, joins, constraints, transactions
- JDBC awareness: what a connection/transaction is under the hood
- OOP modeling: associations, ownership, equals/hashCode caution for entities

**Environment you will set up:**

- Spring Boot + `spring-boot-starter-data-jpa`
- Database: H2 for early drills; **PostgreSQL** for realistic work (Docker or local install)
- Optional: Hibernate SQL logging (`spring.jpa.show-sql`, format SQL) while learning
- Schema strategy: start with `ddl-auto=update` locally; understand why migrations (Flyway/Liquibase) are preferred later

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| ORM idea | Objects ↔ rows; EntityManager / persistence context |
| `@Entity` / `@Id` / `@GeneratedValue` | Identity and primary keys |
| `@Table` / `@Column` | Naming, nullability, length |
| Spring Data repositories | `JpaRepository`, derived query methods |
| Basic CRUD | `save`, `findById`, `findAll`, `delete` |
| Entity vs DTO | Don’t expose lazy graphs as JSON blindly |
| `ddl-auto` | `none` / `validate` / `update` / `create` — risks of each |
| Transactions intro | Service-layer `@Transactional`; default rollback rules |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Relationships | `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne` |
| Owning side / mappedBy | Who owns the FK |
| Cascade types | `PERSIST`, `MERGE`, `REMOVE`, `ALL` — danger of `ALL` + `REMOVE` |
| Orphan removal | When child rows should disappear |
| Fetch types | `LAZY` vs `EAGER`; default LAZY for collections |
| N+1 problem | Symptoms in SQL logs; fix with join fetch / `@EntityGraph` |
| JPQL | Object-query language; `SELECT`, joins, parameters |
| Pagination | `Pageable`, `Page<T>`, sorting |

### Advanced (JPA/Hibernate depth)

| Concept | What to internalize |
|--------|----------------------|
| Persistence context | Managed vs detached; flush; clear |
| HQL vs JPQL | Hibernate dialect extras—prefer JPQL/Criteria for portability |
| Criteria API / Specifications | Dynamic queries (awareness + one exercise) |
| Auditing | `@CreatedDate`, `@LastModifiedDate`, `AuditorAware` |
| Optimistic locking | `@Version` |
| Transaction propagation | `REQUIRED`, `REQUIRES_NEW` (know when) |
| Projection / interfaces | Lightweight read models |
| Migration mindset | Flyway/Liquibase vs `ddl-auto` for anything shared |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Entities & repository CRUD (Days 1–7)

- [ ] **Day 1 — First entity & repository**
  - [ ] **Learn:** [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/) · [Spring Data JPA Repositories](https://docs.spring.io/spring-data/jpa/reference/jpa/repositories.html) · [Baeldung — Spring Data JPA](https://www.baeldung.com/spring-data-jpa-tutorial)
  - [ ] **Practice:** Data JPA + H2 + first entity CRUD
    1. Initializr or add deps: **Spring Web**, **Spring Data JPA**, **H2**, **Validation**. Package `com.learn.jpa`.
    2. YAML: `spring.datasource.url: jdbc:h2:mem:jpalab;DB_CLOSE_DELAY=-1`, `spring.jpa.hibernate.ddl-auto: update`, `spring.h2.console.enabled: true`.
    3. `@Entity Book` with `@Id @GeneratedValue Long id`, `title`, `author`.
    4. `BookRepository extends JpaRepository<Book, Long>`; `@Service` with save/findAll/findById.
    5. Thin controller or `ApplicationRunner` that saves one book and lists all.
    6. **Done when:** Restart shows the book still creatable/listable via repo (H2 mem OK for today).

- [ ] **Day 2 — Columns & constraints**
  - [ ] **Learn:** [Hibernate — Mapping entities](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#entity) · [Baeldung — JPA Entity](https://www.baeldung.com/jpa-entities)
  - [ ] **Practice:** Unique + not-null; verify schema
    1. Map `@Column(nullable = false, length = 200)` on title; add `isbn` with `@Column(unique = true, nullable = false)`.
    2. Set `spring.jpa.show-sql: true` and `spring.jpa.properties.hibernate.format_sql: true`.
    3. Save two books with same ISBN → expect `DataIntegrityViolationException`.
    4. Open H2 console at `/h2-console` (JDBC URL from YAML) **or** read CREATE TABLE from logs.
    5. Confirm unique constraint appears on `isbn`.
    6. **Done when:** Schema shows constraints and duplicate ISBN fails.

- [ ] **Day 3 — PostgreSQL datasource**
  - [ ] **Learn:** [Spring Boot — SQL / JPA](https://docs.spring.io/spring-boot/reference/data/sql.html) · [Accessing Data with MySQL guide](https://spring.io/guides/gs/accessing-data-mysql/) (pattern transfers to Postgres) · [PostgreSQL docs](https://www.postgresql.org/docs/current/)
  - [ ] **Practice:** Switch to Postgres; INSERT succeeds
    1. Create DB: `createdb jpalab` (or Docker Postgres); user with password.
    2. Add `postgresql` driver; remove H2 for this profile (or use `application-postgres.yml`).
    3. YAML: `spring.datasource.url: jdbc:postgresql://localhost:5432/jpalab`, username/password, `spring.jpa.hibernate.ddl-auto: update`, `spring.jpa.database-platform: org.hibernate.dialect.PostgreSQLDialect` (Boot often auto-detects).
    4. Activate profile; start app; save a `Book`.
    5. Verify with `psql -d jpalab -c "SELECT * FROM book;"` (table name may be `book`/`books`).
    6. **Done when:** Row visible in Postgres and dialect/logs look PostgreSQL.

- [ ] **Day 4 — Derived query methods**
  - [ ] **Learn:** [Query methods](https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods.html) · [Baeldung — Query methods](https://www.baeldung.com/spring-data-derived-queries)
  - [ ] **Practice:** `findByTitleContainingIgnoreCase` from API
    1. Add `List<Book> findByTitleContainingIgnoreCase(String title);` to repository.
    2. Optional: `Optional<Book> findByIsbn(String isbn);` / `boolean existsByIsbn(String isbn);`.
    3. `GET /api/books/search?q=dune` → service calls derived method → DTO list.
    4. Seed 2–3 books; curl search expecting **200** and filtered results.
    5. Empty match → **200** + `[]`.
    6. **Done when:** Search endpoint uses the derived method (not manual filter in memory).

- [ ] **Day 5 — `@Transactional`**
  - [ ] **Learn:** [Declarative transaction management](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative.html) · [Baeldung — Spring Transactional](https://www.baeldung.com/transaction-configuration-with-jpa-spring)
  - [ ] **Practice:** Rollback demo — neither save persists
    1. `@Transactional` method on service: save book A, save book B, then `throw new RuntimeException("boom")`.
    2. Call the method; catch at controller/runner.
    3. `findAll` / SQL count → **0** new rows from that call (both rolled back).
    4. Remove the throw; call again → both rows persist.
    5. Notes: runtime vs checked exceptions rollback defaults.
    6. **Done when:** You proved both-or-neither persistence on rollback.

- [ ] **Day 6 — DTOs vs entities**
  - [ ] **Learn:** [Baeldung — Entity to DTO](https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application) · Re-skim [Repositories](https://docs.spring.io/spring-data/jpa/reference/jpa/repositories.html)
  - [ ] **Practice:** Controllers return DTOs only
    1. `BookResponse` / `BookRequest` records; mapper methods in service or `BookMapper`.
    2. Controllers never return `Book` entity types.
    3. GET by id missing → **404** JSON via advice.
    4. POST returns **201** + `BookResponse`.
    5. Confirm JSON has no Hibernate lazy proxies / unexpected fields.
    6. **Done when:** All book endpoints use DTOs end-to-end.

- [ ] **Day 7 — SQL logging & persistence context**
  - [ ] **Learn:** [Hibernate — Logging](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#logging) · [Persistence Context concepts](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#architecture-persistence-context) · [Boot JPA properties](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data)
  - [ ] **Practice:** Watch INSERT/SELECT; write PC notes
    1. Keep `show-sql` + format on for `dev` profile.
    2. POST a book; POST again; GET by id — copy INSERT/SELECT lines into notes.
    3. Write one page: what the persistence context is; when flush/commit happen.
    4. Optional: `logging.level.org.hibernate.SQL: DEBUG` and bind params logger.
    5. Turn down SQL noise for non-dev profiles.
    6. **Done when:** Notes include real SQL samples + persistence-context summary.

### Week 2 — Relationships, fetch, cascade (Days 8–14)

- [ ] **Day 8 — ManyToOne / OneToMany**
  - [ ] **Learn:** [Hibernate — Associations](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#associations) · [Baeldung — JPA relationships](https://www.baeldung.com/jpa-hibernate-associations)
  - [ ] **Practice:** Author–Book (or Category–Product) persist
    1. `@Entity Author` with `@OneToMany(mappedBy = "author")` collection.
    2. `Book` has `@ManyToOne(optional = false) Author author` + `@JoinColumn(name = "author_id")`.
    3. Service: create author, create 2 books linked to author, save.
    4. Verify FK `author_id` in DB / SQL logs.
    5. GET author by id returning DTO with book titles (fetch inside `@Transactional`).
    6. **Done when:** Parent + children persist with correct FK.

- [ ] **Day 9 — Owning side & mappedBy**
  - [ ] **Learn:** [Bidirectional associations](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#associations-one-to-many) · [Baeldung — `mappedBy`](https://www.baeldung.com/hibernate-onetoone)
  - [ ] **Practice:** Helpers keep both sides in sync
    1. Confirm owning side is `Book.author` (`mappedBy` on Author.books).
    2. Add `author.addBook(book)` helper setting both `books.add` and `book.setAuthor`.
    3. Persist only via helper path; avoid orphan inconsistent links.
    4. Intentionally set only one side once; note DB vs memory mismatch in notes.
    5. Fix all create paths to use helpers.
    6. **Done when:** Helpers exist and you documented owning vs inverse side.

- [ ] **Day 10 — Fetch types & LazyInitializationException**
  - [ ] **Learn:** [Hibernate fetching](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching) · [Baeldung — LazyInitializationException](https://www.baeldung.com/hibernate-lazyinitializationexception)
  - [ ] **Practice:** Provoke lazy error; fix transactionally
    1. Keep collections `FetchType.LAZY` (default for OneToMany).
    2. Load author outside transaction and access `getBooks().size()` in controller → catch `LazyInitializationException`.
    3. Fix: `@Transactional(readOnly = true)` service method that maps to DTO **inside** the transaction.
    4. Re-test endpoint → **200** without exception.
    5. Do **not** “fix” by making everything EAGER permanently.
    6. **Done when:** Exception reproduced once and fixed with transactional fetch/map.

- [ ] **Day 11 — Cascade & orphanRemoval**
  - [ ] **Learn:** [Cascading](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#pc-cascade) · [Baeldung — Cascade Types](https://www.baeldung.com/jpa-cascade-types)
  - [ ] **Practice:** Cascade persist; document REMOVE danger
    1. On `Author.books`: `cascade = CascadeType.PERSIST` (or `ALL` carefully for demo).
    2. Save author with transient books attached → children insert without separate save.
    3. Demo `orphanRemoval = true`: remove book from collection and flush → DELETE child.
    4. Notes: why `CascadeType.ALL` + `REMOVE` on large graphs is dangerous.
    5. Prefer explicit deletes for production-like paths after the demo.
    6. **Done when:** Cascade persist works and danger notes exist.

- [ ] **Day 12 — ManyToMany**
  - [ ] **Learn:** [Many-to-many](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#associations-many-to-many) · [Baeldung — Many-to-Many](https://www.baeldung.com/jpa-many-to-many)
  - [ ] **Practice:** Book–Tag join table **or** deepen OneToMany
    1. **Option A:** `@ManyToMany` Book↔Tag with `@JoinTable(name = "book_tags", …)`.
    2. Persist book with 2 tags; verify join table rows in SQL/psql.
    3. **Option B (time-box):** Skip M2M; add a second OneToMany depth and document skip.
    4. Return tags as DTO strings on GET book.
    5. Avoid cascade remove across M2M unless intentional.
    6. **Done when:** Join table populated **or** deliberate deepen+skip note exists.

- [ ] **Day 13 — JPQL `@Query`**
  - [ ] **Learn:** [Defining query methods / `@Query`](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html) · [Baeldung — Spring Data `@Query`](https://www.baeldung.com/spring-data-jpa-query)
  - [ ] **Practice:** Parameterized JPQL only
    1. `@Query("select b from Book b where lower(b.author.name) = lower(:name)") List<Book> findByAuthorName(@Param("name") String name);`
    2. Wire to `GET /api/books/by-author?name=…` → **200**.
    3. Never build JPQL with string concatenation of user input.
    4. Confirm bind parameters in SQL logs.
    5. Add one count query: `@Query("select count(b) from Book b where …")`.
    6. **Done when:** Parameterized `@Query` used from an endpoint successfully.

- [ ] **Day 14 — N+1 & join fetch**
  - [ ] **Learn:** [Fetching strategies](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching) · [Entity Graphs](https://docs.spring.io/spring-data/jpa/reference/jpa/entity-graph.html) · [Baeldung — N+1](https://www.baeldung.com/hibernate-n-plus-one-problem)
  - [ ] **Practice:** Spot N+1; fix with join fetch / EntityGraph
    1. List authors and touch books in a loop with default LAZY — count SELECTs in logs (1 + N).
    2. Add `@Query("select distinct a from Author a left join fetch a.books")` **or** `@EntityGraph`.
    3. Re-run list; SELECT count drops (ideally ~1–2).
    4. Paste before/after SQL counts in notes.
    5. Keep DTO mapping to avoid accidental re-lazy access.
    6. **Done when:** Notes show N+1 before and reduced queries after.

### Week 3 — JPQL, pagination, auditing (Days 15–21)

- [ ] **Day 15 — Pagination & sorting**
  - [ ] **Learn:** [Pagination and sorting](https://docs.spring.io/spring-data/commons/reference/repositories/core-concepts.html) · [Baeldung — Pagination](https://www.baeldung.com/spring-data-jpa-pagination-sorting)
  - [ ] **Practice:** `Page` → DTO page response
    1. Controller: `Pageable` from `?page=0&size=2&sort=title,asc`.
    2. `bookRepository.findAll(pageable)` → map `Page<Book>` to `{ content, totalElements, totalPages, number, size }`.
    3. Seed ≥5 books; `curl "http://localhost:8080/api/books?page=0&size=2&sort=title,asc"` → **200**, 2 items.
    4. Confirm `totalElements` matches DB count.
    5. Reject absurd `size` (e.g. >100) → **400** or clamp.
    6. **Done when:** Paginated DTO response verified with curl.

- [ ] **Day 16 — Reporting JPQL**
  - [ ] **Learn:** Revisit [`@Query`](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html) · [Baeldung — JPQL](https://www.baeldung.com/spring-data-jpa-query)
  - [ ] **Practice:** Count/filter reporting query
    1. JPQL grouping or count-by-author: e.g. `select a.name, count(b) from Author a left join a.books b group by a.name`.
    2. Expose `GET /api/reports/books-per-author` returning DTO list.
    3. Verify SQL in logs matches intent.
    4. Compare result to manual psql count.
    5. Keep parameterized if any filter input.
    6. **Done when:** Report endpoint matches DB counts.

- [ ] **Day 17 — JPA auditing**
  - [ ] **Learn:** [Auditing](https://docs.spring.io/spring-data/jpa/reference/auditing.html) · [Baeldung — Spring Data JPA Auditing](https://www.baeldung.com/spring-data-jpa-auditing)
  - [ ] **Practice:** Created/updated dates populate
    1. `@EnableJpaAuditing` on config; entity fields `@CreatedDate` / `@LastModifiedDate` with `@EntityListeners(AuditingEntityListener.class)`.
    2. Use `LocalDateTime` or `Instant`; enable if needed `spring.jpa.properties…` / `@EnableJpaAuditing`.
    3. Save book → `createdAt` set; update title → `updatedAt` changes.
    4. Return timestamps in DTO; curl prove non-null.
    5. Optional: `@CreatedBy` later with Security `AuditorAware`.
    6. **Done when:** Create/update timestamps populate automatically.

- [ ] **Day 18 — Optimistic locking**
  - [ ] **Learn:** [Optimistic locking / `@Version`](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#locking-optimistic) · [Baeldung — Optimistic Locking](https://www.baeldung.com/jpa-optimistic-locking)
  - [ ] **Practice:** `@Version` conflict → conscious handling
    1. Add `@Version Long version` on `Book` (or stock-bearing entity).
    2. Load same entity twice (two service calls simulating concurrent updates).
    3. Update both and save — second save throws `OptimisticLockException` / `ObjectOptimisticLockingFailureException`.
    4. Map to **409** Conflict in `@ControllerAdvice`.
    5. Notes: when optimistic locking helps (inventory).
    6. **Done when:** Conflict is reproducible and returns 409 JSON.

- [ ] **Day 19 — Mini-project A domain**
  - [ ] **Learn:** Re-skim [Associations](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#associations) + [Repositories](https://docs.spring.io/spring-data/jpa/reference/jpa/repositories.html)
  - [ ] **Practice:** Product/Book + Order + OrderLine mapped
    1. Entities: `Product`/`Book` (stock), `Order`, `OrderLine` (qty, price snapshot, FK product, FK order).
    2. Relationships: Order OneToMany lines; Line ManyToOne product/order.
    3. Repositories for each; compile-clean mappings.
    4. Persist sample order with 2 lines via service helpers.
    5. Verify FKs in Postgres.
    6. **Done when:** Multi-entity graph saves without mapping errors.

- [ ] **Day 20 — Place-order transaction**
  - [ ] **Learn:** [Declarative transactions](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative.html)
  - [ ] **Practice:** Stock decrement + rollback on shortfall
    1. `@Transactional placeOrder(…)`: check stock ≥ qty; decrement; create order + lines.
    2. Happy path: `POST /api/orders` → **201**; stock decreased in DB.
    3. Insufficient stock → throw; expect **400/409**; **no** order row; stock unchanged.
    4. Prove rollback with SQL select before/after failure.
    5. DTOs only in API.
    6. **Done when:** Success and stock-failure paths both proven.

- [ ] **Day 21 — README & seed**
  - [ ] **Learn:** [Database initialization](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.initialization) · [Baeldung — `data.sql`](https://www.baeldung.com/spring-boot-data-sql-and-schema-sql)
  - [ ] **Practice:** Seed data + run docs
    1. Seed via `ApplicationRunner` **or** `data.sql` (note Boot 2.5+ / 3.x deferral settings if needed).
    2. Seed ≥3 products/books and maybe 1 author.
    3. README: Postgres URL keys, profiles, sample curls for books + place-order.
    4. Document `ddl-auto` choice for local vs later validate.
    5. Restart on empty DB → seed appears.
    6. **Done when:** Fresh start seeds data and README runs the happy path.

### Week 4 — Hardening & Hibernate depth (Days 22–28)

- [ ] **Day 22 — Fetching strategies deep dive**
  - [ ] **Learn:** [Hibernate fetching chapter](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching) · [Hibernate ORM docs hub](https://hibernate.org/orm/documentation/)
  - [ ] **Practice:** Map strategies to your use cases
    1. Skim fetching chapter; list LAZY, EAGER, join fetch, batch, subselect (brief).
    2. For each, write one use case in **your** app (or “avoid”).
    3. Mark current associations’ fetch types in a table.
    4. Note one place you still risk N+1.
    5. No need for new features — notes quality matters.
    6. **Done when:** Strategy→use-case table exists for your domain.

- [ ] **Day 23 — Specifications / Criteria**
  - [ ] **Learn:** [Specifications](https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html) · [Baeldung — Specifications](https://www.baeldung.com/spring-data-criteria-queries)
  - [ ] **Practice:** Dynamic filter on one search endpoint
    1. `BookRepository extends JpaSpecificationExecutor<Book>`.
    2. Build Specification: optional title contains, optional author id, optional min price.
    3. `GET /api/books?title=&authorId=` combines non-null params.
    4. Curl with one filter, then two filters; results narrow.
    5. Keep Pageable support if easy.
    6. **Done when:** Dynamic AND filters work without string-built JPQL.

- [ ] **Day 24 — Flyway intro**
  - [ ] **Learn:** [Flyway docs](https://documentation.red-gate.com/fd) · [Spring Boot Flyway](https://docs.spring.io/spring-boot/reference/howto/data-initialization.html#howto.data-initialization.migration-tool.flyway)
  - [ ] **Practice:** First migration + `ddl-auto=validate`
    1. Add Flyway dependency; create `db/migration/V1__init.sql` matching core tables.
    2. Profile `migrate`: `spring.jpa.hibernate.ddl-auto: validate`, Flyway enabled.
    3. Drop/recreate local schema or use fresh DB; start app — migrations apply.
    4. Mismatch entity vs SQL → startup fail; fix migration or entity.
    5. If skipping: write deliberate skip note with reason.
    6. **Done when:** `validate` profile starts clean **or** skip is documented.

- [ ] **Day 25 — Mini-project B / projections**
  - [ ] **Learn:** [Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html) · [Auditing](https://docs.spring.io/spring-data/jpa/reference/auditing.html)
  - [ ] **Practice:** Blog/Notes+Tags **or** interface projections
    1. **Option A:** Post–Tag M2M mini API (create/list/search).
    2. **Option B:** Interface projection `BookTitleView { String getTitle(); String getIsbn(); }` query method.
    3. Expose read endpoint returning projection/DTO only.
    4. Confirm SQL selects only needed columns when using projections (check logs).
    5. Document choice in README.
    6. **Done when:** Option A or B delivers a working read path.

- [ ] **Day 26 — equals/hashCode for entities**
  - [ ] **Learn:** [Baeldung — JPA entity equals/hashCode](https://www.baeldung.com/jpa-entity-equality) · Skim Hibernate entity chapter
  - [ ] **Practice:** Consistent equality rule
    1. Read pitfalls of ID-based equals before persist (null id).
    2. Pick a rule (e.g. business key = ISBN; or ID-only after persist) and document it.
    3. Implement consistently on `Book` / `OrderLine` if used in Sets.
    4. Avoid putting mutable fields in hashCode.
    5. Short note in README or `NOTES-entities.md`.
    6. **Done when:** Written rule + matching implementation on at least one entity.

- [ ] **Day 27 — Indexes & EXPLAIN**
  - [ ] **Learn:** [PostgreSQL — Indexes](https://www.postgresql.org/docs/current/indexes.html) · [EXPLAIN](https://www.postgresql.org/docs/current/using-explain.html)
  - [ ] **Practice:** Index hot filter; read one plan
    1. Add `@Table(indexes = @Index(name = "idx_book_title", columnList = "title"))` or Flyway `CREATE INDEX`.
    2. Restart / migrate so index exists.
    3. In `psql`: `EXPLAIN ANALYZE SELECT * FROM book WHERE title ILIKE '%a%';` (adjust).
    4. Paste plan summary into notes (seq scan vs index).
    5. Index ISBN unique if not already.
    6. **Done when:** Index exists and one EXPLAIN note is captured.

- [ ] **Day 28 — Mastery & BookStore sketch**
  - [ ] **Learn:** [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/reference/) · [Hibernate docs](https://hibernate.org/orm/documentation/)
  - [ ] **Practice:** Checklist + BookStore entity sketch
    1. Complete §12 mastery checklist.
    2. Sketch entities for phase project: `User`, `Book`, `Order`, `OrderItem` with fields + relationships.
    3. Note JWT user ↔ `User` table join.
    4. List Must transactions (place-order) to reuse.
    5. Bookmark Data JPA + Hibernate fetching docs.
    6. **Done when:** Checklist ≥90% and BookStore sketch is ready for `phase-project.md`.

*On a 3-week pace: fold Week 4 highlights into Days 19–21 (auditing + N+1 fix + Postgres).*

---

## 5. Hands-on exercises

### Warm-ups

1. Entity with UUID vs identity ID—try both; pick one for the project.
2. Unique ISBN constraint; handle `DataIntegrityViolationException` as 409.
3. Soft delete flag vs hard delete—implement one deliberately.
4. `existsByEmail` derived method for registration checks.
5. Transaction rollback: throw after two saves; confirm neither persisted.

### Relationships & queries

6. Bidirectional OneToMany; helper `addBook` keeps both sides consistent.
7. LAZY collection accessed outside transaction → exception → fix with fetch join service method.
8. JPQL: find books by author name with join.
9. Page of results mapped to DTOs (content + totalElements).
10. `@EntityGraph` on a repository method to fetch association in one go.

### Stretch

11. `@ManyToMany` with extra attributes → prefer association entity instead.
12. HQL-only feature read in Hibernate docs; rewrite as JPQL if possible.
13. AuditorAware returning current username from SecurityContext.
14. Flyway `V1__init.sql` matching your entities; set `ddl-auto=validate`.

---

## 6. Mini-projects

Detailed build guides (Goal → Build steps → DoD → Demo + curl/status): [`../mini-projects/phase-04-spring/14-jpa-hibernate/`](../mini-projects/phase-04-spring/14-jpa-hibernate/).

### A. Catalog + Orders (JPA)

**Guide:** [A-catalog-orders-jpa.md](../mini-projects/phase-04-spring/14-jpa-hibernate/A-catalog-orders-jpa.md)

- Entities: Product/Book, Category or Author, Order, OrderLine
- [Must] Relationships mapped correctly; CRUD via repositories
- [Must] Place-order service method is `@Transactional` (stock check + lines)
- [Must] Pagination on catalog listing
- [Must] At least one JPQL `@Query`
- [Must] SQL logging used to verify fetch behavior
- [Should] Auditing timestamps on Order
- [Should] DTO responses only
- [Could] Optimistic locking on Product stock

### B. Blog / Notes with Tags

**Guide:** [B-blog-notes-tags.md](../mini-projects/phase-04-spring/14-jpa-hibernate/B-blog-notes-tags.md)

- Post–Tag many-to-many or association entity
- Search + page + audit fields
- Focus: join table ownership and fetch control

### C. (Optional) Migration starter

**Guide:** [C-migration-starter.md](../mini-projects/phase-04-spring/14-jpa-hibernate/C-migration-starter.md)

- Introduce Flyway; baseline schema; disable reckless `ddl-auto` for that profile

---

## 7. Industry use cases

- **E-commerce / catalog APIs:** Products, categories, carts, orders—classic JPA relationships.
- **SaaS multi-entity domains:** Users, orgs, memberships with constraints and transactions.
- **Reporting reads:** JPQL/projections for dashboards (know when native SQL is better).
- **Auditing & compliance:** Created/modified metadata on critical records.
- **PostgreSQL production apps:** Dialects, indexes, and migrations alongside Hibernate.

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| `EAGER` everywhere “to avoid lazy errors” | Keep LAZY; fetch what you need per use case |
| `CascadeType.ALL` + `REMOVE` on large graphs | Cascade only intentional children; prefer explicit deletes |
| Exposing entities directly as JSON | DTOs; control serialization of associations |
| Business logic in repositories | Keep in `@Transactional` services |
| Ignoring N+1 | Watch SQL logs; join fetch / graphs / batch size |
| `ddl-auto=create` on shared DB | Never on real data; use migrations |
| Broken equals/hashCode on entities | Avoid collections of entities keyed by mutable fields; document ID strategy |
| Lazy load in view/filter after TX ends | Open session carefully or fetch inside service |
| Giant transactions | Keep transactional methods focused |
| Skipping SQL skills | ORM does not replace understanding joins and indexes |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/reference/) | Repositories, queries, pagination |
| [Spring Boot — Data access](https://docs.spring.io/spring-boot/reference/data/sql.html) | Boot + JPA properties |
| [Hibernate ORM User Guide](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html) | Provider deep dive |
| [Jakarta Persistence spec / overview](https://jakarta.ee/specifications/persistence/) | API standard |
| [Baeldung — Spring Data JPA](https://www.baeldung.com/spring-data-jpa-tutorial) | Free practical series |
| [Baeldung — Hibernate](https://www.baeldung.com/hibernate) | Relationships, fetch, cascading |
| [spring.io — Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/) | Official guide |
| PostgreSQL docs (your version) | Constraints, indexes, EXPLAIN |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Spring Data JPA: https://docs.spring.io/spring-data/jpa/reference/
- Repositories: https://docs.spring.io/spring-data/jpa/reference/jpa/repositories.html
- Query methods: https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods.html
- Pagination and sorting: https://docs.spring.io/spring-data/commons/reference/repositories/core-concepts.html
- Spring Boot JPA properties: https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data
- Guide — Accessing Data with JPA: https://spring.io/guides/gs/accessing-data-jpa/
- Guide — Accessing Data with MySQL (pattern transfers to Postgres): https://spring.io/guides/gs/accessing-data-mysql/
- Hibernate ORM documentation: https://hibernate.org/orm/documentation/
- Hibernate User Guide (current): https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html
- Hibernate fetching: https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching
- Jakarta Persistence: https://jakarta.ee/specifications/persistence/
- Baeldung JPA/Hibernate hub: https://www.baeldung.com/hibernate
- PostgreSQL docs: https://www.postgresql.org/docs/current/
- Flyway (free docs, optional migrations): https://documentation.red-gate.com/fd

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Single entity | Repository CRUD on Postgres/H2 | Inserts visible in DB |
| 2. Transactions | Service boundaries + rollback | Proven in DB |
| 3. Associations | Correct FK ownership | Bidirectional helpers work |
| 4. Fetch control | No surprise lazy/N+1 in happy paths | SQL logs make sense |
| 5. JPQL + pages | Filtered, paged APIs | Stable contracts |
| 6. Auditing / version | Metadata + optional `@Version` | Fields populated |
| 7. Gate | Ready for BookStore phase project | Mastery checklist ≥90% |

**Weekly rhythm:** Mon–Fri model + code; Sat read SQL logs; Sun refactor entities + checklist.

```text
CRUD entity → Postgres → Relationships → Fetch/N+1 → JPQL/Pagination/Auditing → Domain mini-project
```

---

## 12. Mastery checklist

- [ ] Map entities with correct IDs, columns, and constraints
- [ ] Use Spring Data `JpaRepository` and derived queries confidently
- [ ] Run against PostgreSQL (not only H2) at least once
- [ ] Explain and use `@Transactional` on the service layer
- [ ] Model at least one real association (ManyToOne/OneToMany) with clear owning side
- [ ] Choose LAZY/EAGER deliberately; fix one `LazyInitializationException`
- [ ] Identify and fix one N+1 (or prevent it with a fetch strategy)
- [ ] Use cascade/orphanRemoval only where intentional
- [ ] Write JPQL `@Query` methods with parameters
- [ ] Implement pagination and sorting on a list endpoint
- [ ] Add auditing timestamps (and optional `@Version`)
- [ ] Keep API responses as DTOs, not raw entity graphs
- [ ] Understand `ddl-auto` risks; prefer migrations for shared DBs
- [ ] Completed Mini-project A with README and seed data
- [ ] Entity sketch ready for [phase-project.md](phase-project.md) BookStore API
