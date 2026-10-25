# 12 — Spring Boot

**Duration:** ~3–4 weeks (21–28 days) · **Cadence:** 1 hour/day  
**Focus:** Auto Configuration, Starter Dependencies, Profiles, Properties, Validation, Logging, Spring Boot CLI, Actuator, DevTools · Build a complete CRUD application

---

## 1. Why this topic is important

Spring Boot is how most modern Spring APIs are built: opinionated defaults, starter dependencies, and auto-configuration turn a working web service into a weekend (or a day’s) scaffold instead of days of XML and manual DispatcherServlet setup. Controllers, `application.yml`, validation, logging, profiles, Actuator, and DevTools are the everyday toolkit for shipping and operating a backend.

After Spring Core, Boot stops feeling magical: starters pull libraries; auto-config registers beans when classpath + properties match; you override only what you need. This topic ends with a **complete CRUD application**—the shape you will harden with Security (13) and persist with JPA (14), then combine in the BookStore phase project.

---

## 2. Prerequisites

- [11-spring-core.md](11-spring-core.md) mastery checklist ≥90%
- REST & HTTP basics (Phase 3 topic 10): methods, status codes, JSON, idempotency awareness
- Maven or Gradle fluency for dependencies and running the app
- JDK 17+; PostgreSQL optional here (H2 is fine for Boot CRUD; Postgres in phase project)
- Familiarity with DTOs vs entities (entities deepen in topic 14)

**Environment you will set up:**

- [Spring Initializr](https://start.spring.io/) project: Web, Validation, Actuator, DevTools (add Data JPA + H2 when ready for persistence slice)
- IDE run configuration or `./mvnw spring-boot:run` / Gradle equivalent
- Optional: Spring Boot CLI only if you want scripting experiments (not required for mastery)

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| What Boot adds | Auto-config, starters, embedded server, production-ready defaults |
| Initializr & project layout | `src/main/java`, `resources`, tests; main `@SpringBootApplication` class |
| Starter dependencies | `spring-boot-starter-web`, `-validation`, `-actuator`, etc. |
| Embedded Tomcat | App is the server; `server.port` |
| `@RestController` | `@GetMapping` / `@PostMapping` / …; `@RequestBody`, `@PathVariable`, `@RequestParam` |
| JSON & Jackson | Request/response body mapping; common gotchas |
| `application.properties` / `.yml` | Externalized config; YAML structure |
| Logging | SLF4J + Logback defaults; `logging.level.*` |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Auto-configuration | `@EnableAutoConfiguration` idea; conditional beans; `spring-boot-autoconfigure` |
| Customizing vs fighting Boot | Prefer properties and `@Bean` overrides over copy-paste exclusions |
| Profiles | `dev` / `test` / `prod`; `spring.profiles.active`; profile-specific YAML |
| Validation | `jakarta.validation` annotations; `@Valid`; `MethodArgumentNotValidException` handling |
| Exception handling | `@ControllerAdvice` / `@ExceptionHandler`; consistent error JSON |
| Layering | Controller → Service → (in-memory or repository); no business rules in controllers |
| DevTools | Restart/livereload habits; what not to rely on in prod |
| Actuator | `/actuator/health`, info, metrics awareness; exposure configuration |

### Advanced (Boot depth)

| Concept | What to internalize |
|--------|----------------------|
| Configuration properties | `@ConfigurationProperties` type-safe config |
| Actuator security baseline | Don’t expose everything publicly; later tie to Security topic |
| Failure analysis / startup logs | Read “Conditions Evaluation Report” when debugging auto-config |
| Packaging | Executable JAR; `java -jar` |
| Testing slice awareness | `@SpringBootTest`, `@WebMvcTest` (smoke-level this topic) |
| Boot CLI (optional) | Groovy scripts / rapid prototypes—know it exists |
| Production checklist | Profiles, logging, health, graceful config for DB URLs/secrets |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Boot basics & first API (Days 1–7)

- [ ] **Day 1 — Initializr & Hello API**
  - [ ] **Learn:** [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot/) · [Spring Boot Reference — Getting Started](https://docs.spring.io/spring-boot/reference/index.html) · [start.spring.io](https://start.spring.io/)
  - [ ] **Practice:** Generate Boot Web app and hit Hello
    1. Open [start.spring.io](https://start.spring.io/): **Project** Maven · **Language** Java · **Spring Boot** 3.x · **Java** 17+ · **Group** `com.learn` · **Artifact** `boot-lab` · **Package name** `com.learn.boot`.
    2. Dependencies: **Spring Web** only. Generate → unzip → open in IDE.
    3. Add `@RestController` `HelloController` with `@GetMapping("/api/hello")` returning `{"message":"Hello, Boot"}`.
    4. Run the app; `curl -i http://localhost:8080/api/hello` (expect **200** and JSON body).
    5. In notes, list starters from `pom.xml` / `build.gradle` (at least `spring-boot-starter-web`).
    6. **Done when:** Hello returns 200 and notes list the starters on the classpath.

- [ ] **Day 2 — First REST stubs**
  - [ ] **Learn:** [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/) · [`@RestController`](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-restcontroller.html)
  - [ ] **Practice:** In-memory GET all / GET by id / POST
    1. Create record/DTO `Book` (`id`, `title`, `author`) and an in-memory `List` or `Map` in a `@RestController` (or thin service).
    2. `GET /api/books` → 200 + JSON array (start empty or seed 1–2 items).
    3. `GET /api/books/{id}` → return book or placeholder 404 for now.
    4. `POST /api/books` with `@RequestBody Book` → assign id, add to list, return body.
    5. Prove with: `curl -s http://localhost:8080/api/books` and `curl -s -X POST http://localhost:8080/api/books -H "Content-Type: application/json" -d "{\"title\":\"Dune\",\"author\":\"Herbert\"}"`.
    6. **Done when:** POST adds an item and GET all shows it.

- [ ] **Day 3 — Full CRUD status codes**
  - [ ] **Learn:** [Annotated Controllers](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html) · [Baeldung — Spring REST status codes](https://www.baeldung.com/spring-response-entity)
  - [ ] **Practice:** PUT/PATCH/DELETE with correct statuses
    1. Return `ResponseEntity` from create: **201** + `Location` header optional + body.
    2. `PUT /api/books/{id}` full replace → **200** if found, **404** if missing.
    3. `DELETE /api/books/{id}` → **204** if deleted, **404** if missing.
    4. Missing GET by id → **404** with empty body or simple JSON.
    5. Script curl checks: create 201, get 200, delete 204, get again 404.
    6. **Done when:** All four statuses behave as above on real calls.

- [ ] **Day 4 — application.yml**
  - [ ] **Learn:** [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html) · [Application properties appendix](https://docs.spring.io/spring-boot/appendix/application-properties/index.html)
  - [ ] **Practice:** Port, name, logging in YAML
    1. Replace/create `src/main/resources/application.yml` with:
       `server.port: 8081`, `spring.application.name: boot-lab`, `logging.level.com.learn.boot: DEBUG`.
    2. Add a nested custom key e.g. `app.greeting: Hello from YAML` and a list `app.tags: [api, demo]`.
    3. Restart; hit `http://localhost:8081/api/hello` (not 8080).
    4. Confirm DEBUG logs for your package appear on a request.
    5. Optional: expose greeting via a controller reading `@Value("${app.greeting}")`.
    6. **Done when:** App listens on **8081** and YAML keys are verified.

- [ ] **Day 5 — Profiles**
  - [ ] **Learn:** [Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html) · [Baeldung — Spring Profiles](https://www.baeldung.com/spring-profiles)
  - [ ] **Practice:** `dev` vs `prod` YAML switch
    1. Create `application-dev.yml`: `server.port: 8081`, `logging.level.root: DEBUG`.
    2. Create `application-prod.yml`: `server.port: 8080`, `logging.level.root: WARN`.
    3. In `application.yml` set `spring.profiles.active: dev` (or pass `--spring.profiles.active=dev`).
    4. Run as `dev` — confirm port 8081 + verbose logs.
    5. Rerun with `prod` — confirm port 8080 + quieter logs.
    6. **Done when:** Switching `spring.profiles.active` changes port and log level as designed.

- [ ] **Day 6 — Validation**
  - [ ] **Learn:** [Validation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-validation.html) · [Baeldung — Spring Validation](https://www.baeldung.com/spring-boot-bean-validation)
  - [ ] **Practice:** DTO constraints → 400
    1. Add dependency **Validation** (`spring-boot-starter-validation`) if missing (Initializr or manual).
    2. Create `BookRequest` with `@NotBlank` title, `@Size(min=1,max=200)`, `@NotBlank` author.
    3. Controller method: `@PostMapping` with `@Valid @RequestBody BookRequest`.
    4. POST invalid JSON (empty title): expect **400**.
    5. POST valid body: expect **201** (or 200 if you kept that).
    6. **Done when:** Invalid → 400, valid → success status.

- [ ] **Day 7 — `@ControllerAdvice`**
  - [ ] **Learn:** [Exceptions](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-rest-exceptions.html) · [Baeldung — `@ControllerAdvice`](https://www.baeldung.com/exception-handling-for-rest-with-spring)
  - [ ] **Practice:** Uniform error JSON + curl demo
    1. Create `ApiError` record: `timestamp`, `status`, `error`, `message`, `path` (optional `fieldErrors`).
    2. `@RestControllerAdvice` handles `MethodArgumentNotValidException` → **400** with field errors.
    3. Handle custom `NotFoundException` → **404** JSON.
    4. Throw `NotFoundException` from GET by missing id.
    5. Curl invalid POST and missing GET; both return consistent JSON shape.
    6. **Done when:** 400 and 404 share the same error envelope.

### Week 2 — Auto-config, Actuator, DevTools (Days 8–14)

- [ ] **Day 8 — Auto-configuration**
  - [ ] **Learn:** [Auto-configuration](https://docs.spring.io/spring-boot/reference/features/auto-configuration.html) · [Baeldung — Spring Boot Auto Configuration](https://www.baeldung.com/spring-autoconfiguration)
  - [ ] **Practice:** Read condition report once
    1. Start with `--debug` or set `debug: true` in YAML temporarily.
    2. Find `CONDITIONS EVALUATION REPORT` in the console.
    3. Pick 5 lines (positive/negative matches) related to web, jackson, or servlet.
    4. Summarize each in notes: what matched / why skipped.
    5. Turn `debug` off again so logs stay readable.
    6. **Done when:** Notes list 5 real conditions from your run.

- [ ] **Day 9 — Actuator health**
  - [ ] **Learn:** [Actuator endpoints](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html) · [Production-ready features](https://docs.spring.io/spring-boot/reference/actuator/)
  - [ ] **Practice:** Expose and hit health
    1. Add **Spring Boot Actuator** dependency.
    2. In YAML: `management.endpoints.web.exposure.include: health` (start minimal).
    3. `curl -i http://localhost:8081/actuator/health` → expect **200** and `"status":"UP"`.
    4. Temporarily include `info` or `*` once; note what appears; tighten back to `health`.
    5. Document in notes exactly which endpoints are exposed and why.
    6. **Done when:** Health returns UP and exposure is intentional (not “all by accident”).

- [ ] **Day 10 — DevTools**
  - [ ] **Learn:** [Developer Tools](https://docs.spring.io/spring-boot/reference/using/devtools.html) · [Baeldung — Spring Boot DevTools](https://www.baeldung.com/spring-boot-devtools)
  - [ ] **Practice:** Auto-restart on controller change
    1. Add `spring-boot-devtools` (runtime, optional scope in Maven).
    2. Run the app; change the Hello message string; save (IDE build if needed).
    3. Watch console for restart; re-curl `/api/hello` for new text.
    4. Note: DevTools is for local only — do not ship as a prod dependency assumption.
    5. Write one sentence: what restart does not replace (e.g. schema migrations later).
    6. **Done when:** You saw an automatic restart and verified the new response.

- [ ] **Day 11 — `@ConfigurationProperties`**
  - [ ] **Learn:** [Type-safe Configuration Properties](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties) · [Baeldung — `@ConfigurationProperties`](https://www.baeldung.com/configuration-properties-in-spring-boot)
  - [ ] **Practice:** Bind `app.*` and inject
    1. YAML: `app: { title: Boot Lab, max-page-size: 50 }`.
    2. Create `@ConfigurationProperties(prefix = "app")` class `AppProperties` with fields + getters/setters (or records + `@EnableConfigurationProperties`).
    3. Enable with `@EnableConfigurationProperties(AppProperties.class)` or `@ConfigurationPropertiesScan`.
    4. Inject into a service/controller; return `title` from an endpoint or log it at startup.
    5. Stretch: `@Validated` + `@Min(1)` on `maxPageSize`; remove YAML key and confirm startup fails.
    6. **Done when:** Bound properties are readable in code (and optional fail-fast works).

- [ ] **Day 12 — Logging**
  - [ ] **Learn:** [Logging](https://docs.spring.io/spring-boot/reference/features/logging.html) · [Baeldung — Spring Boot Logging](https://www.baeldung.com/spring-boot-logging)
  - [ ] **Practice:** Levels, pattern, request log
    1. Set `logging.level.com.learn.boot: DEBUG` and optionally `logging.pattern.console`.
    2. Add a `OncePerRequestFilter` or `HandlerInterceptor` that logs `METHOD path status` (status after completion if interceptor).
    3. Hit 2–3 endpoints; confirm logs appear once per request.
    4. Do not log request bodies with secrets (none yet — still good habit).
    5. Tune root to INFO so noise stays down.
    6. **Done when:** Each request leaves a clear log line with method + path.

- [ ] **Day 13 — Layering refactor**
  - [ ] **Learn:** Re-skim [REST service guide](https://spring.io/guides/gs/rest-service/) · [Building a Spring Boot app](https://spring.io/guides/gs/spring-boot/)
  - [ ] **Practice:** Controller / Service / store + DTOs
    1. Packages: `com.learn.boot.web`, `.service`, `.domain`, `.dto`.
    2. Move list/map into a store or service; controller only maps HTTP ↔ DTO.
    3. Map `BookRequest`/`BookResponse` separately from domain `Book`.
    4. Controllers contain no unique-title / price rules — those live in service (even if stubbed).
    5. Re-run Day 3 curl matrix; statuses unchanged.
    6. **Done when:** Layer packages exist and CRUD still returns correct statuses.

- [ ] **Day 14 — Complete CRUD start**
  - [ ] **Learn:** [Spring Boot Features hub](https://docs.spring.io/spring-boot/reference/features.html) (bookmark)
  - [ ] **Practice:** Scaffold Mini-project A (Books or Products)
    1. Pick domain: **Books** or **Products**. Rename packages if needed (`com.learn.boot.books`).
    2. Scaffold: controller, service, in-memory repository, DTOs, exception types.
    3. Wire empty CRUD method stubs that compile.
    4. Keep `dev`/`prod` profiles and Actuator health from prior days.
    5. Commit/snapshot folder name clearly (`boot-crud-books`).
    6. **Done when:** Project skeleton compiles and `/actuator/health` still UP.

### Week 3 — Complete CRUD application (Days 15–21)

- [ ] **Day 15 — Domain & service rules**
  - [ ] **Learn:** Revisit [Validation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-validation.html)
  - [ ] **Practice:** Service constraints (unique title, non-negative price)
    1. Domain fields: e.g. `title`, `author`/`sku`, `price` (≥ 0).
    2. On create: reject duplicate title → throw conflict exception (map to **409** tomorrow or today).
    3. On update: same uniqueness rule excluding self.
    4. Unit-style calls from a temporary test or main path with 2 creates — second duplicate fails.
    5. Keep validation annotations on DTOs for blank/size.
    6. **Done when:** Duplicate title is rejected in the service layer (not only in the controller).

- [ ] **Day 16 — Full CRUD + errors**
  - [ ] **Learn:** [Exceptions / `@RestControllerAdvice`](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-rest-exceptions.html)
  - [ ] **Practice:** Complete verbs + consistent errors
    1. Finish GET/POST/PUT/DELETE with **200/201/204/404/400/409** as designed.
    2. Ensure `@ControllerAdvice` maps validation, not-found, conflict.
    3. Curl: create → list → get → update → delete → get 404.
    4. Curl: invalid body → 400; duplicate → 409.
    5. Confirm error JSON shape matches Day 7.
    6. **Done when:** Full happy path + 400/404/409 all proven with curl.

- [ ] **Day 17 — Pagination params**
  - [ ] **Learn:** [Baeldung — Pagination in Spring](https://www.baeldung.com/spring-data-jpa-pagination-sorting) (concepts; in-memory helper OK) · [Request params](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestparam.html)
  - [ ] **Practice:** In-memory `page` / `size` / `sort`
    1. `GET /api/books?page=0&size=2&sort=title,asc` (param names your choice; document them).
    2. Implement slice helper over the in-memory list (subList + total count).
    3. Response JSON: `{ "content": [...], "page": 0, "size": 2, "totalElements": N }`.
    4. Seed ≥5 items; verify page 0 size 2 returns 2 elements and correct total.
    5. Invalid `size` above `app.max-page-size` → 400 or clamp — pick one and document.
    6. **Done when:** Paginated response shape is documented and verified.

- [ ] **Day 18 — Actuator info**
  - [ ] **Learn:** [Actuator — Application Info](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.info) · [Health](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.health)
  - [ ] **Practice:** Health + info exposure
    1. YAML: `management.endpoints.web.exposure.include: health,info`.
    2. Set `info.app.version: 0.1.0` and `info.app.name: ${spring.application.name}` (or `management.info.env.enabled: true` per Boot version).
    3. `curl http://localhost:8081/actuator/info` → see version.
    4. Reconfirm `/actuator/health` → UP.
    5. Note: do not expose `env`/`beans` publicly.
    6. **Done when:** Both health and info return 200 with expected content.

- [ ] **Day 19 — Profile seed / flags**
  - [ ] **Learn:** [Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html) · [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html)
  - [ ] **Practice:** Dev seed vs prod empty
    1. `application-dev.yml`: `app.seed-enabled: true`.
    2. `application-prod.yml`: `app.seed-enabled: false`.
    3. `@Component` `DataSeeder` implements `ApplicationRunner`; only runs when property true.
    4. Start `dev` → list endpoint has seed books; start `prod` → empty (or no seed).
    5. Optional feature flag: `app.features.search: true` gates a search endpoint.
    6. **Done when:** Profile switch changes seed behavior without code edits.

- [ ] **Day 20 — README**
  - [ ] **Learn:** Skim your app’s YAML + [Building with Spring Boot](https://spring.io/guides/gs/spring-boot/)
  - [ ] **Practice:** Run docs + curl/HTTPie script
    1. README sections: prerequisites, how to run (`./mvnw spring-boot:run`), profiles.
    2. Sample curls for GET list, GET id, POST, PUT, DELETE with expected status codes.
    3. Actuator URLs and exposure note.
    4. Paste a full happy-path script block a teammate could run.
    5. Link to `application.yml` keys that matter (`server.port`, profiles).
    6. **Done when:** Another person could run CRUD from README alone.

- [ ] **Day 21 — Manual test matrix**
  - [ ] **Learn:** Re-skim [status codes article](https://www.baeldung.com/spring-response-entity)
  - [ ] **Practice:** Happy / validation / 404 matrix
    1. Table in notes: case → request → expected status (at least 8 rows).
    2. Execute every row; mark pass/fail.
    3. Fix any failing status or error shape.
    4. Tick progress toward §12 mastery checklist.
    5. Snapshot the working project.
    6. **Done when:** Matrix all pass and checklist gaps are listed.

### Week 4 — Hardening & Boot CLI (Days 22–28)

- [ ] **Day 22 — Executable JAR**
  - [ ] **Learn:** [Packaging](https://docs.spring.io/spring-boot/reference/packaging.html) · [Installing Spring Boot apps](https://docs.spring.io/spring-boot/reference/packaging/installation.html)
  - [ ] **Practice:** `java -jar` with prod-like profile
    1. `./mvnw -DskipTests package` (or Gradle `bootJar`).
    2. Find JAR under `target/*.jar` (not `.jar.original`).
    3. Run: `java -jar target/boot-lab-*.jar --spring.profiles.active=prod`.
    4. Curl health and one API endpoint on the prod port.
    5. Note JVM command in README.
    6. **Done when:** Packaged JAR serves API under `prod` profile.

- [ ] **Day 23 — Smoke tests**
  - [ ] **Learn:** [`@SpringBootTest`](https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html) · [`@WebMvcTest`](https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#testing.spring-boot-applications.spring-mvc-tests) · [Baeldung — `@WebMvcTest`](https://www.baeldung.com/spring-boot-testing)
  - [ ] **Practice:** One controller smoke test
    1. Add test dependency if missing (`spring-boot-starter-test`).
    2. Write `@WebMvcTest(BookController.class)` **or** `@SpringBootTest` + `MockMvc` / `TestRestTemplate`.
    3. Mock service if `@WebMvcTest`; stub one GET to return a book.
    4. Assert status **200** and JSON path `$.title` (or similar).
    5. Run `./mvnw test` — green.
    6. **Done when:** At least one automated test passes for a controller path.

- [ ] **Day 24 — Boot CLI or Actuator metrics**
  - [ ] **Learn:** [Spring Boot CLI (optional)](https://docs.spring.io/spring-boot/docs/current/reference/html/cli.html) · [Metrics](https://docs.spring.io/spring-boot/reference/actuator/metrics.html)
  - [ ] **Practice:** CLI hello **or** metrics endpoint
    1. **Option A:** Install Boot CLI if easy; run a tiny Groovy script hello — or document skip reason.
    2. **Option B (recommended):** Expose `health,info,metrics` briefly; `curl /actuator/metrics` then one metric name.
    3. Note JVM memory or `http.server.requests` if present.
    4. Tighten exposure back to `health,info` after exploring.
    5. Write 3 bullets: what metrics are for in prod.
    6. **Done when:** Either CLI demo or metrics notes + one successful metrics curl.

- [ ] **Day 25 — Config Studio / search**
  - [ ] **Learn:** [`@ConfigurationProperties`](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties) · [Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html)
  - [ ] **Practice:** Mini-project B **or** search on CRUD
    1. **Option A — Config Studio:** profile-specific bean (e.g. `Mailer` fake vs logging) + validated `@ConfigurationProperties`.
    2. **Option B — Search:** `GET /api/books?q=dune` filters title/author contains (ignore case).
    3. Prove with curl: match returns subset; empty q returns all/paginated.
    4. Keep DTOs only in responses.
    5. Document the chosen option in README.
    6. **Done when:** Option A or B works end-to-end with at least two curl proofs.

- [ ] **Day 26 — Secrets & env overrides**
  - [ ] **Learn:** [Externalized Configuration — Accessing](https://docs.spring.io/spring-boot/reference/features/external-config.html) · [Baeldung — Properties with Spring](https://www.baeldung.com/properties-with-spring)
  - [ ] **Practice:** Env overrides YAML; no secrets in git
    1. Pick a key e.g. `app.title`; override with env `APP_TITLE=FromEnv` (relaxed binding) or `java -jar … -Dapp.title=FromEnv`.
    2. Confirm endpoint/log shows override wins over YAML.
    3. Add `application-local.yml` to `.gitignore`; put a fake “secret” only there.
    4. README: document env vars for future DB URL / JWT (placeholders).
    5. Scan repo: no real passwords committed.
    6. **Done when:** Override proven and gitignore pattern documented.

- [ ] **Day 27 — Security prep notes**
  - [ ] **Learn:** Skim [Spring Security Reference](https://docs.spring.io/spring-security/reference/) (what comes next)
  - [ ] **Practice:** Public vs authenticated route map
    1. List every current endpoint in a table.
    2. Mark planned: public (`/api/hello`, `/actuator/health`) vs authenticated (CRUD write) vs admin-only (delete?).
    3. Note which will become `/api/auth/login` in topic 13.
    4. Mark candidates in code comments or a `SECURITY-PLAN.md` note.
    5. Skim filter-chain concept once (no coding required).
    6. **Done when:** Written route map exists for topic 13.

- [ ] **Day 28 — Mastery gate**
  - [ ] **Learn:** [Spring Boot docs home](https://docs.spring.io/spring-boot/reference/) (bookmark for JPA next)
  - [ ] **Practice:** Checklist + JPA replacement list
    1. Complete §12 mastery checklist with honest ticks.
    2. Fix any Must gap from Mini-project A.
    3. Write: “JPA will replace …” (in-memory list/map, manual ids, etc.).
    4. Bookmark Boot + Actuator docs.
    5. Keep this CRUD app — you will secure it next.
    6. **Done when:** Checklist ≥90% and JPA-replacement notes exist.

*On a 3-week pace: merge Week 4 into Days 19–21 (JAR + README + checklist).*

---

## 5. Hands-on exercises

### Warm-ups

1. Change port and context-path via YAML; verify with a browser or curl.
2. Return `ResponseEntity` with explicit headers and status.
3. Global exception handler mapping a custom `NotFoundException` to 404 JSON.
4. Activate `dev` profile and log which DataSource auto-config would kick in (with H2 on classpath).
5. Hit `/actuator/health` before and after changing exposure properties.

### API & validation

6. DTO with nested validation (`@Valid` on nested object).
7. `@RequestParam` with defaults and required flags.
8. Idempotent PUT vs non-idempotent POST—document behavior for your resource.
9. `@ConfigurationProperties` validation with `@Validated` on the properties class.
10. Structured logging: log correlation-friendly messages on create/update.

### Stretch

11. Custom Actuator health indicator (e.g., “disk” or “in-memory store size”).
12. `@WebMvcTest` verifying JSON path for GET by id.
13. Fail startup if a required property is missing (`@ConfigurationProperties` + validation).
14. Compare properties vs YAML for the same config; note readability tradeoffs.

---

## 6. Mini-projects

Detailed build guides (Goal → Build steps → DoD → Demo + curl/status): [`../mini-projects/phase-04-spring/12-spring-boot/`](../mini-projects/phase-04-spring/12-spring-boot/).

### A. Complete CRUD Application (required)

**Guide:** [A-complete-crud-application.md](../mini-projects/phase-04-spring/12-spring-boot/A-complete-crud-application.md)

Pick one domain (Books, Products, or Employees) and ship:

- [Must] Layered Boot app: controller, service, in-memory or simple persistence
- [Must] Full CRUD + validation + consistent error JSON
- [Must] `dev` and `prod` profiles (logging/port differences at minimum)
- [Must] Actuator health enabled; document how to reach it
- [Must] README with run instructions and sample HTTP calls
- [Should] `@ConfigurationProperties` for app branding/limits
- [Should] DevTools used during development
- [Could] Simple search/filter endpoint

This CRUD app is the backbone you will secure (topic 13) and/or persist with JPA (topic 14).

### B. Profile & Config Studio

**Guide:** [B-profile-config-studio.md](../mini-projects/phase-04-spring/12-spring-boot/B-profile-config-studio.md)

- Demonstrate profile-specific beans or properties (feature toggle, fake vs real mailer).
- Type-safe configuration properties with validation.
- Actuator `info` contributing app metadata.

### C. (Optional) Boot CLI sketch

**Guide:** [C-boot-cli-sketch.md](../mini-projects/phase-04-spring/12-spring-boot/C-boot-cli-sketch.md)

- One small Groovy/CLI experiment **or** a documented skip with reason—time is better on the CRUD app if CLI is unavailable.

---

## 7. Industry use cases

- **Customer-facing REST APIs:** Controllers + validation + uniform errors are the public contract.
- **Internal microservices:** Same Boot shape; Actuator health for orchestrators and load balancers.
- **Environment promotion:** Profiles and env-based properties for dev → staging → prod.
- **Ops baseline:** Health, metrics, and logging levels without reinventing monitoring hooks.
- **Fast iteration:** DevTools and Initializr shorten feedback loops for feature teams.

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Business logic in controllers | Move to `@Service` |
| Returning entities as API contracts too early | Use DTOs; map explicitly |
| Swallowing validation errors as 500 | `@ControllerAdvice` + 400 with field errors |
| Committing secrets in YAML | Env vars / external config; sample `application-local.yml` gitignored |
| Exposing all Actuator endpoints | Explicit `management.endpoints.web.exposure.include` |
| Fighting auto-config blindly | Read condition report; adjust classpath/properties first |
| One giant `application.yml` | Split by profile; keep shared defaults thin |
| Ignoring status codes | Align with HTTP semantics (create → 201, no body → 204) |
| No README | Future-you (and phase project) needs run steps |
| Skipping Core understanding | When Boot “does nothing,” debug missing beans like Core |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Spring Boot Reference](https://docs.spring.io/spring-boot/reference/) | Official source of truth |
| [Spring Boot — Features / Auto-config](https://docs.spring.io/spring-boot/reference/features/auto-configuration.html) | How starters wire beans |
| [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html) | Properties, profiles, env |
| [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/) | Production-ready endpoints |
| [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot/) | Official guide |
| [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/) | Controllers + JSON |
| [Baeldung — Spring Boot tutorials (free)](https://www.baeldung.com/spring-boot) | Practical articles |
| [Baeldung — Spring Profiles](https://www.baeldung.com/spring-profiles) | Profile patterns |
| [start.spring.io](https://start.spring.io/) | Project generation |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Spring Boot docs: https://docs.spring.io/spring-boot/reference/
- Spring Boot project: https://spring.io/projects/spring-boot
- Auto-configuration: https://docs.spring.io/spring-boot/reference/features/auto-configuration.html
- Externalized configuration: https://docs.spring.io/spring-boot/reference/features/external-config.html
- Profiles: https://docs.spring.io/spring-boot/reference/features/profiles.html
- Logging: https://docs.spring.io/spring-boot/reference/features/logging.html
- Developer tools: https://docs.spring.io/spring-boot/reference/using/devtools.html
- Actuator: https://docs.spring.io/spring-boot/reference/actuator/endpoints.html
- Application properties appendix: https://docs.spring.io/spring-boot/appendix/application-properties/index.html
- Validation in Spring MVC: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-validation.html
- `@RestController`: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-restcontroller.html
- Spring Initializr: https://start.spring.io/
- Guide — REST service: https://spring.io/guides/gs/rest-service/
- Guide — Spring Boot: https://spring.io/guides/gs/spring-boot/
- Configuration properties: https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Run Boot | Initializr app serves HTTP | Explain starters used |
| 2. REST CRUD | In-memory resource API | Correct status codes |
| 3. Config | YAML + profiles + logging | Switch env without code edits |
| 4. Validation & errors | DTOs + advice | Predictable 400/404 JSON |
| 5. Ops lite | Actuator + DevTools | Health works; restart understood |
| 6. Complete CRUD | Mini-project A done | README + manual test script |
| 7. Gate | Ready for Security | Mastery checklist ≥90% |

**Weekly rhythm:** Mon–Fri features; Sat curl test matrix; Sun README + checklist.

```text
Hello API → Validated CRUD → Profiles/Actuator → Complete CRUD app → JAR + notes for Security/JPA
```

---

## 12. Mastery checklist

- [ ] Create and run a Spring Boot app from Initializr
- [ ] Explain starters and auto-configuration in your own words
- [ ] Build REST endpoints with proper HTTP status codes
- [ ] Use `application.yml` / properties and at least two profiles
- [ ] Validate request bodies and return structured 400 errors
- [ ] Implement `@ControllerAdvice` for consistent API errors
- [ ] Separate controller / service / storage concerns
- [ ] Configure and call Actuator health (with conscious endpoint exposure)
- [ ] Use DevTools or document an equivalent fast-feedback workflow
- [ ] Bind custom settings with `@ConfigurationProperties`
- [ ] Package and run an executable JAR
- [ ] Completed Complete CRUD Application (§6A) with README and sample requests
- [ ] Notes ready: public vs protected routes for topic 13; what becomes JPA entities for topic 14
- [ ] Ready to start [13-spring-security-jwt.md](13-spring-security-jwt.md)
