# Mini-project A — Complete CRUD Application

**Topic:** [12 — Spring Boot](../../../phase-04-spring/12-spring-boot.md) · **Domain pick one:** Books · Products · Employees (examples below use **Books**)

---

## Goal

Ship a layered Spring Boot REST CRUD API with validation, consistent error JSON, `dev`/`prod` profiles, Actuator health, and a README with sample HTTP calls. This app is the backbone you will secure (topic 13) and/or persist with JPA (topic 14).

---

## Prerequisites

- Topic 11 Core DI mental model
- Topic 12 Weeks 1–2 practiced (REST, YAML, profiles, validation intro)
- JDK 17+, IDE, curl or HTTP client

---

## Learn links

| Resource | Why |
|----------|-----|
| [start.spring.io](https://start.spring.io/) | Project generation |
| [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/) | Controllers + JSON |
| [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html) | YAML + profiles |
| [Validation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-validation.html) | `@Valid` |
| [Actuator endpoints](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html) | Health |
| [Configuration properties](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties) | `@ConfigurationProperties` |

### start.spring.io settings

| Field | Value |
|-------|--------|
| Project | Maven (or Gradle) |
| Language | Java |
| Spring Boot | 3.x |
| Group | `com.learn` |
| Artifact | `crud-lab` |
| Name | `crud-lab` |
| Package name | `com.learn.crud` |
| Packaging | Jar |
| Java | 17+ |
| Dependencies | **Spring Web**, **Validation**, **Spring Boot Actuator**, **Spring Boot DevTools** (optional but Should) |

---

## Project layout

```text
com.learn.crud/
  CrudLabApplication.java
  book/
    BookController.java
    BookService.java
    BookStore.java          (in-memory Map/List)
    dto/BookRequest.java
    dto/BookResponse.java
  config/
    AppProperties.java      (@ConfigurationProperties)
  common/
    ApiError.java
    GlobalExceptionHandler.java
src/main/resources/
  application.yml
  application-dev.yml
  application-prod.yml
README.md
```

---

## Build steps

- [ ] **1. Generate & Hello**
  1. Generate from start.spring.io with settings above; open in IDE.
  2. Confirm app starts; optional `GET /api/hello` → **200**.
  3. **Done when:** Boot starts on default port without errors.

- [ ] **2. Layered CRUD (in-memory)**
  1. DTOs: `BookRequest` (`title`, `author`, `isbn` optional); `BookResponse` (`id`, …).
  2. `BookStore` / service holds `ConcurrentHashMap<Long, Book>`.
  3. Endpoints under `/api/books`:
     | Method | Path | Expected status |
     |--------|------|-----------------|
     | `GET` | `/api/books` | **200** + array |
     | `GET` | `/api/books/{id}` | **200** or **404** |
     | `POST` | `/api/books` | **201** + body |
     | `PUT` | `/api/books/{id}` | **200** or **404** |
     | `DELETE` | `/api/books/{id}` | **204** or **404** |
  4. Controller thin; service owns logic.
  5. **Done when:** Full CRUD works via curl with correct statuses.

- [ ] **3. Validation + error JSON**
  1. Annotate request: `@NotBlank` title/author; length limits.
  2. `@Valid @RequestBody` on POST/PUT.
  3. `@RestControllerAdvice`: validation → **400** with field errors; not found → **404**; generic → **500** message safe.
  4. Shape e.g. `{"status":400,"error":"Bad Request","message":"…","fields":{…}}`.
  5. **Done when:** Empty title POST returns **400** JSON (not **500**).

- [ ] **4. Profiles `dev` / `prod`**
  1. `application-dev.yml`: `server.port: 8081`, `logging.level.com.learn.crud: DEBUG`.
  2. `application-prod.yml`: `server.port: 8080`, `logging.level.root: WARN`.
  3. `spring.profiles.active: dev` in base YAML (overrideable).
  4. **Done when:** Switching profile changes port and log verbosity without code edits.

- [ ] **5. Actuator health**
  1. `management.endpoints.web.exposure.include: health` (add `info` only if intentional).
  2. `GET /actuator/health` → **200** `{"status":"UP"}` (or component detail if enabled).
  3. Document URL + profile port in README.
  4. **Done when:** Health returns **200** UP on the active profile port.

- [ ] **6. `@ConfigurationProperties` (Should)**
  1. `app.branding.name`, `app.limits.maxPageSize` (even if list is in-memory).
  2. `@EnableConfigurationProperties` / `@ConfigurationPropertiesScan`.
  3. Expose branding on `GET /api/meta` → **200**.
  4. **Done when:** Changing YAML changes `/api/meta` without recompiling Java constants.

- [ ] **7. Optional search (Could)**
  1. `GET /api/books?q=dune` filters title/author (case-insensitive) → **200**.
  2. **Done when:** Filter returns subset; empty q returns all.

- [ ] **8. README + sample script**
  1. Run instructions, active profile, Actuator URL.
  2. Paste curl matrix (create/list/get/update/delete/validation/health).
  3. **Done when:** Cold start from README reproduces all Must behaviors.

- [ ] **9. Executable JAR**
  1. `mvn -q package` (or Gradle); `java -jar target/crud-lab-*.jar --spring.profiles.active=prod`.
  2. Hit health on **8080**.
  3. **Done when:** JAR serves CRUD + health without IDE.

---

## Definition of Done

- [ ] Layered Boot app: controller / service / in-memory store
- [ ] Full CRUD + validation + consistent error JSON
- [ ] `dev` and `prod` profiles (logging/port differences at minimum)
- [ ] Actuator health enabled and documented
- [ ] README with run steps + sample HTTP calls
- [ ] Should: `@ConfigurationProperties` for branding/limits
- [ ] Should: DevTools used during development
- [ ] Could: search/filter endpoint
- [ ] Package root `com.learn.crud`

---

## Demo script

```bash
# assume dev profile → port 8081
curl -i http://localhost:8081/api/books
# → 200 []

curl -i -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Dune\",\"author\":\"Herbert\"}"
# → 201 + body with id

curl -i http://localhost:8081/api/books/1
# → 200

curl -i -X PUT http://localhost:8081/api/books/1 \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Dune Messiah\",\"author\":\"Herbert\"}"
# → 200

curl -i -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"\",\"author\":\"X\"}"
# → 400 + field errors

curl -i -X DELETE http://localhost:8081/api/books/1
# → 204

curl -i http://localhost:8081/api/books/1
# → 404

curl -i http://localhost:8081/actuator/health
# → 200 UP
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Business logic in controller | Move to `@Service` |
| Validation failures as 500 | `@ControllerAdvice` → 400 |
| Wrong statuses (POST → 200) | Create → **201**; delete → **204** |
| Exposing all Actuator endpoints | Explicit `exposure.include` |
| Secrets in YAML | N/A for in-memory; still use profiles cleanly |
| No README | Required for DoD |

---

## After this

Do [B — Profile & Config Studio](B-profile-config-studio.md) for deeper config practice, or optional [C — Boot CLI sketch](C-boot-cli-sketch.md). Then start [13 — Spring Security & JWT](../../../phase-04-spring/13-spring-security-jwt.md) on top of this CRUD (or a fresh copy).
