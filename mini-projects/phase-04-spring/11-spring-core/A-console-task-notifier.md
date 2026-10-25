# Mini-project A — Console Task Notifier (Spring-wired)

**Topic:** [11 — Spring Core](../../../phase-04-spring/11-spring-core.md) · **Stack:** Spring Framework only (no Boot, no web)

---

## Goal

Build a console app where `TaskService` creates tasks and a swappable `Notifier` (`ConsoleNotifier` / `FileNotifier`) is wired by Spring Core. Switch implementations with `@Primary` or profiles; print bean lifecycle on startup/shutdown. `Main` stays thin.

---

## Prerequisites

- Topic 11 Weeks 1–2 practiced (DI, stereotypes, Java config, scopes/lifecycle intro)
- JDK 17+, Maven or Gradle
- Comfortable with interfaces and constructor injection

---

## Learn links

| Resource | Why |
|----------|-----|
| [IoC Container](https://docs.spring.io/spring-framework/reference/core/beans.html) | Beans and DI |
| [Java-based Container Config](https://docs.spring.io/spring-framework/reference/core/beans/java.html) | `@Configuration` / `@Bean` |
| [Classpath Scanning](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html) | Stereotypes + scan |
| [Bean lifecycle](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html) | Init/destroy |
| [Profiles (Environment)](https://docs.spring.io/spring-framework/reference/core/beans/environment.html) | `@Profile` switching |

**Project setup (not start.spring.io):** plain Maven/Gradle module — **no** Spring Boot.

| Setting | Value |
|---------|--------|
| Group / Artifact | `com.learn` / `task-notifier` |
| Package | `com.learn.tasks` |
| Dependency | `org.springframework:spring-context` (6.x) |
| Optional | `jakarta.annotation-api` if `@PostConstruct` / `@PreDestroy` need an explicit import on your JDK |

---

## Project layout

```text
task-notifier/
  pom.xml   (or build.gradle)
  src/main/java/com/learn/tasks/
    Main.java
    AppConfig.java
    model/Task.java
    service/TaskService.java
    notify/Notifier.java
    notify/ConsoleNotifier.java
    notify/FileNotifier.java
  src/main/resources/
    (optional) application.properties for profile name only if you load PropertySources
  out/tasks.log   (created at runtime by FileNotifier)
```

---

## Build steps

- [ ] **1. Scaffold Core module**
  1. Create Maven/Gradle project `task-notifier`, Java 17+, package `com.learn.tasks`.
  2. Add `spring-context` 6.x only (no `spring-boot-*`).
  3. `Main` prints `Task Notifier — bootstrap` and exits.
  4. **Done when:** Project compiles and runs without Spring yet.

- [ ] **2. Domain + Notifier interface**
  1. `Task` record/class: `id` (UUID or long), `title`, `createdAt`.
  2. `Notifier` interface: `void notify(Task task)`.
  3. `ConsoleNotifier` `@Component` `@Profile("console")` (or always + `@Primary`): prints `[NOTIFY] {title}`.
  4. **Done when:** You can unit-call `ConsoleNotifier` without Spring and see console output.

- [ ] **3. FileNotifier + TaskService**
  1. `FileNotifier` `@Component` `@Profile("file")`: append one line per task to `out/tasks.log` (create dirs if needed).
  2. `TaskService` `@Service`: constructor-inject `Notifier`; method `Task create(String title)` builds task then calls `notifier.notify(task)`.
  3. No `new ConsoleNotifier()` / `new FileNotifier()` inside the service.
  4. **Done when:** Hand-wiring `new TaskService(new ConsoleNotifier())` creates and prints a task.

- [ ] **4. AppConfig + component scan**
  1. `@Configuration` `@ComponentScan("com.learn.tasks")` class `AppConfig`.
  2. Optionally declare one notifier as `@Bean` `@Primary` for the default profile.
  3. **Done when:** Config class compiles; scan package is the root `com.learn.tasks`.

- [ ] **5. Main starts AnnotationConfigApplicationContext**
  1. `try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) { … }`.
  2. `TaskService service = ctx.getBean(TaskService.class);`
  3. Create 2–3 tasks; confirm console output (default profile).
  4. **Done when:** Tasks appear via Spring-injected notifier; no manual `new` of collaborators.

- [ ] **6. Profile switch console ↔ file**
  1. Ensure exactly one notifier active per profile (`console` vs `file`).
  2. Activate with `ctx.getEnvironment().setActiveProfiles("file");` **before** `ctx.register`/`refresh`, or `spring.profiles.active=file` via system property `-Dspring.profiles.active=file`.
  3. Run with `file` profile; open `out/tasks.log` and confirm lines.
  4. **Done when:** Same `Main` + profile flag routes notifications to file without editing `TaskService`.

- [ ] **7. Lifecycle callbacks**
  1. On `ConsoleNotifier` (or a dedicated `@Component LifecycleProbe`): `@PostConstruct` log `init: ConsoleNotifier`; `@PreDestroy` log `destroy: ConsoleNotifier`.
  2. Close context (try-with-resources or `ctx.close()`).
  3. **Done when:** Init prints on start and destroy prints on shutdown.

- [ ] **8. README + notes**
  1. README: how to run default vs `-Dspring.profiles.active=file`.
  2. Notes: bean graph (`AppConfig` → `TaskService` → `Notifier` impl).
  3. **Done when:** Someone else can run both profiles from README alone.

---

## Definition of Done

- [ ] Spring Core only (`spring-context`); no Boot, no embedded server
- [ ] `TaskService` + `Notifier` with `ConsoleNotifier` and `FileNotifier`
- [ ] Constructor injection; no `new` of Spring-managed collaborators in services
- [ ] Profile or `@Primary` switches notifier without changing `TaskService`
- [ ] Lifecycle init/destroy visible in logs
- [ ] `Main` only boots context and calls `TaskService`
- [ ] Package `com.learn.tasks` + `AppConfig` + `Main`
- [ ] README with both run modes

---

## Demo script

```text
1. mvn -q compile exec:java   (or IDE run Main)
   → console shows init lifecycle + 2–3 [NOTIFY] lines
2. Close / exit process
   → destroy lifecycle line appears
3. Run with -Dspring.profiles.active=file
   → out/tasks.log gains matching lines; console notifier inactive
4. Flip back to console profile
   → file notifier not used; console prints again
```

No HTTP — statuses N/A. Success = console/file output + clean context close.

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Adding `spring-boot-starter-*` | Keep Core-only for this project |
| `new ConsoleNotifier()` inside `TaskService` | Inject the interface |
| Setting active profile after context refresh | Set profiles before refresh |
| Both notifiers active → NoUniqueBeanDefinitionException | `@Profile` exclusivity or `@Primary` |
| Forgetting `ctx.close()` | Use try-with-resources |
| Fat `Main` with business logic | Call `TaskService` only |

---

## After this

Ship [B — In-Memory Catalog Engine](B-inmemory-catalog-engine.md) next (facade + scopes). Optional stretch: [C — Config-driven Greeting App](C-config-greeting-app.md). Then gate topic 11 and start [12 — Spring Boot](../../../phase-04-spring/12-spring-boot.md).
