# 11 — Spring Core

**Duration:** ~2–3 weeks (14–21 days) · **Cadence:** 1 hour/day  
**Focus:** IoC, DI, Beans, Bean Lifecycle, Annotations, Java Config, Component Scan, Spring Architecture

---

## 1. Why this topic is important

Spring Boot looks like “magic” until you understand Spring Core. IoC (Inversion of Control) and DI (Dependency Injection) are how Spring wires objects: you declare what a class needs; the container creates and injects collaborators. Beans, scopes, lifecycle callbacks, and configuration styles (XML historically, Java `@Configuration`, and stereotype annotations) are the vocabulary of every Spring application.

Learning Core **before** Boot means you can explain *why* a bean is missing, why a circular dependency appears, why `@Autowired` fails, and how component scanning decides what becomes a bean. That debugging skill saves days in Boot, Security, and JPA later—those layers sit on the same container.

This topic trains a practical habit: small non-Boot Spring apps you can start, inspect, and reason about—then Boot becomes a convenience layer, not a black box.

---

## 2. Prerequisites

- Phase 1 Core Java complete (especially OOP, interfaces, packages, Java 8+ lambdas/streams comfort)
- Phase 2–3 gates met (SQL/JDBC and REST/HTTP awareness help later; not required for Core DI demos)
- Comfortable with Maven or Gradle basics (dependency declaration, build/run)
- JDK 17+ recommended
- IDE: IntelliJ Community or VS Code with Java extensions

**Environment you will set up:**

- Maven or Gradle project with `spring-context` (and later `spring-beans`, `spring-core` transitively)
- Optional: Spring Framework reference docs bookmarked from spring.io

**Do not skip:** Interface-based design and constructor injection habits. They carry into Boot and Security.

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Why DI exists | Hard-coded `new` couples classes; DI inverts creation and wiring |
| IoC container | `ApplicationContext` owns bean lifecycle and dependency graph |
| Bean | Object managed by Spring (id/name, type, dependencies) |
| Constructor injection | Preferred: required deps in constructor; immutable fields |
| Setter / field injection | When used; why field `@Autowired` is weaker for testing |
| Stereotype annotations | `@Component`, `@Service`, `@Repository`, `@Controller` |
| `@Autowired` / `@Inject` | Type-based wiring; `@Qualifier` when multiple candidates |
| Component scan | `@ComponentScan` / `@SpringBootApplication` later—packages matter |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Java `@Configuration` | `@Bean` methods; factory methods as explicit wiring |
| Annotation vs Java config | Mix carefully; prefer one primary style per module |
| Bean scopes | `singleton` (default), `prototype`; request/session (web) awareness |
| Bean lifecycle | Instantiation → DI → init callbacks → use → destroy |
| Lifecycle hooks | `@PostConstruct`, `@PreDestroy`, `InitializingBean`, `DisposableBean`, `init-method` |
| `@Primary` / `@Qualifier` | Disambiguation strategies |
| Profiles intro | `@Profile` on beans/config (deep dive in Boot) |
| Spring module map | `spring-core`, `beans`, `context`, `aop`, `web`, `tx` roles |

### Advanced (Core depth)

| Concept | What to internalize |
|--------|----------------------|
| ApplicationContext types | `AnnotationConfigApplicationContext`, classpath XML context (legacy awareness) |
| BeanDefinition & post-processors | How the container customizes beans (conceptual) |
| Circular dependencies | Why constructor cycles fail; redesign vs setter cycle pitfalls |
| Aware interfaces | `ApplicationContextAware`, `BeanNameAware`—use sparingly |
| Events | `ApplicationEventPublisher`, `@EventListener` for decoupled reactions |
| AOP intro (light) | Proxies power `@Transactional` later—know proxy vs target |
| Environment / PropertySource | Externalized values into beans without Boot yet |
| Architecture mental model | Client → Context → Bean factory → Bean graph |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — IoC, DI, first container (Days 1–7)

- [ ] **Day 1 — Spring overview & first module**
  - [ ] **Learn:** [Spring Framework — Overview](https://docs.spring.io/spring-framework/reference/overview.html) · [IoC Container intro](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html) · [Baeldung — IoC & DI](https://www.baeldung.com/inversion-control-and-dependency-injection-in-spring)
  - [ ] **Practice:** Create a `spring-context` module and empty Main
    1. Create folder `spring-core-lab/` with Maven or Gradle (Java 17+). Group/artifact: `com.learn` / `spring-core-lab`.
    2. Add dependency `org.springframework:spring-context` (latest 6.x). No Boot yet.
    3. Create package `com.learn.core` and class `Main` with `public static void main` that only prints `Spring Core Day 1`.
    4. Run `Main` from the IDE or `mvn -q exec:java` / Gradle equivalent — confirm output.
    5. In notes, paste the exact Maven/Gradle coords for `spring-context` you used.
    6. **Done when:** App runs, dependency resolves, and notes list the coords (no beans yet).

- [ ] **Day 2 — Manual DI without Spring**
  - [ ] **Learn:** [Dependencies and configuration](https://docs.spring.io/spring-framework/reference/core/beans/dependencies.html) (skim DI motivation) · [Baeldung — Spring tutorial hub](https://www.baeldung.com/spring-tutorial)
  - [ ] **Practice:** Hand-wire interfaces vs hard-coded `new`
    1. In `com.learn.core.manual`, create interface `MessageService` with `String getMessage()`.
    2. Implement `HelloMessageService` returning `"Hello from manual DI"`.
    3. Create `Greeter` with constructor `Greeter(MessageService service)` and method `greet()` that prints the message.
    4. In `ManualMain`, wire with `new Greeter(new HelloMessageService())` — no Spring.
    5. Write 5 lines in notes: what Spring’s container will replace (object creation + wiring).
    6. **Done when:** Manual app prints the greeting and notes contrast `new` vs inject.

- [ ] **Day 3 — `@Configuration` + `@Bean`**
  - [ ] **Learn:** [Java-based Container Configuration](https://docs.spring.io/spring-framework/reference/core/beans/java.html) · [Instantiating a Container](https://docs.spring.io/spring-framework/reference/core/beans/basics.html#beans-factory-instantiation)
  - [ ] **Practice:** Two beans, one depends on the other
    1. Create `@Configuration` class `AppConfig` in `com.learn.core.config`.
    2. `@Bean MessageService messageService()` → return `new HelloMessageService()`.
    3. `@Bean Greeter greeter(MessageService messageService)` → return `new Greeter(messageService)`.
    4. In `Main`, open `try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class))`.
    5. Call `ctx.getBean(Greeter.class).greet()` and print bean names via `ctx.getBeanDefinitionNames()`.
    6. **Done when:** Greeting prints from a container-managed `Greeter` (no manual `new Greeter`).

- [ ] **Day 4 — Stereotypes & component scan**
  - [ ] **Learn:** [Classpath Scanning and Managed Components](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html) · [Annotation-based Container Config](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html)
  - [ ] **Practice:** `@Component` / `@Service` + prove scan package
    1. Annotate `HelloMessageService` with `@Component` and `Greeter` with `@Service`; remove the matching `@Bean` methods.
    2. Create `@Configuration` + `@ComponentScan("com.learn.core")` (or scan root package containing those classes).
    3. Start context with that config; retrieve `Greeter` by type and call `greet()`.
    4. Temporarily change scan to `"com.learn.wrong"`; start context and note the failure (`NoSuchBeanDefinitionException`).
    5. Restore correct scan package and re-run successfully.
    6. **Done when:** Discovery works with stereotypes, and you recorded the wrong-package failure + fix.

- [ ] **Day 5 — Injection styles**
  - [ ] **Learn:** [Dependency injection](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html) · [Using `@Autowired`](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html#beans-autowired-annotation) · [Baeldung — Constructor vs Field Injection](https://www.baeldung.com/spring-injection-lombok)
  - [ ] **Practice:** Constructor vs setter vs field injection demos
    1. Keep constructor injection on `Greeter` (preferred).
    2. Add a throwaway bean `SetterWired` with a setter `@Autowired` for `MessageService`; call it from `Main` once.
    3. Add a throwaway bean `FieldWired` with `@Autowired` on a private field; call it once.
    4. In notes: 3 bullets — why constructor wins (required deps, testability, immutability).
    5. Delete or mark `FieldWired` / `SetterWired` as demo-only so production style stays constructor.
    6. **Done when:** All three styles ran once and notes state constructor preference.

- [ ] **Day 6 — `@Qualifier` & `@Primary`**
  - [ ] **Learn:** [`@Primary` and `@Qualifier`](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html#beans-autowired-annotation-qualifiers) · [Baeldung — Spring Qualifier](https://www.baeldung.com/spring-qualifier-annotation)
  - [ ] **Practice:** Two implementations, resolve both ways
    1. Create interface `Notifier` with `void notify(String msg)`.
    2. `@Component("consoleNotifier")` and `@Component("fileNotifier")` implementations (file can append to `target/notify.log` or print `"FILE: …"`).
    3. Inject with `@Qualifier("consoleNotifier")` into a `NotifyDemo` service; run and confirm console path.
    4. Mark one impl `@Primary`; inject without `@Qualifier` into another demo bean; confirm primary is used.
    5. Temporarily remove `@Primary` and `@Qualifier` so startup fails with “expected single matching bean” — then restore.
    6. **Done when:** Qualifier path and Primary path both work; ambiguous failure was observed once.

- [ ] **Day 7 — Week review**
  - [ ] **Learn:** [IoC Container hub](https://docs.spring.io/spring-framework/reference/core/beans.html) (quick recap)
  - [ ] **Practice:** Bean graph + IoC explanation
    1. Draw boxes: `AppConfig` / scan → `MessageService` → `Greeter` → `Notifier` (if present).
    2. Rename unclear classes/packages to `com.learn.core.*` consistently.
    3. Ensure `Main` only starts the context and calls one facade/service.
    4. Explain IoC aloud in under 2 minutes (or record voice notes): who creates objects, who injects.
    5. Re-run the Day 3–6 graph once to confirm nothing broke.
    6. **Done when:** Diagram exists, naming is clean, and you can explain IoC without reading notes.

### Week 2 — Lifecycle, scopes, architecture (Days 8–14)

- [ ] **Day 8 — Bean scopes**
  - [ ] **Learn:** [Bean scopes](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html) · [Baeldung — Spring Bean Scopes](https://www.baeldung.com/spring-bean-scopes)
  - [ ] **Practice:** Singleton vs prototype proof
    1. Create `@Component` `CounterBean` with an `AtomicInteger` incrementing in the constructor or `next()`.
    2. Default (singleton): get bean twice from context; print identity hash / counter — same instance.
    3. Annotate a second bean `@Scope("prototype")`; get twice — different instances.
    4. Inject the prototype into a singleton holder; get the holder twice and check if prototype instance changes (it typically does not).
    5. Write 3–5 sentences explaining why prototype-into-singleton is “stuck.”
    6. **Done when:** Console proves singleton reuse, prototype new instances, and stuck-injection note exists.

- [ ] **Day 9 — Lifecycle callbacks**
  - [ ] **Learn:** [Customization with callbacks / lifecycle](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html) · [Baeldung — Spring Bean Lifecycle](https://www.baeldung.com/spring-bean-life-cycle)
  - [ ] **Practice:** `@PostConstruct` / `@PreDestroy` and close context
    1. On one bean add `@PostConstruct void init()` printing `INIT` and `@PreDestroy void destroy()` printing `DESTROY`.
    2. Start context, get the bean, then `ctx.close()` (or try-with-resources end).
    3. Confirm log order: construct → `INIT` → use → `DESTROY`.
    4. Note what happens if you never close the context (destroy may not run).
    5. Optional: `@Bean(initMethod=…, destroyMethod=…)` on one `@Bean` method for comparison.
    6. **Done when:** You see `INIT` and `DESTROY` in the expected order after close.

- [ ] **Day 10 — Spring architecture**
  - [ ] **Learn:** [Spring Framework Overview](https://docs.spring.io/spring-framework/reference/overview.html) · [Core Technologies](https://docs.spring.io/spring-framework/reference/core.html)
  - [ ] **Practice:** Module + container diagram in your words
    1. In notes, list modules: `core`, `beans`, `context`, `aop`, `web`, `tx` — one sentence each.
    2. Draw: Client → `ApplicationContext` → BeanFactory → Bean graph.
    3. Mark where your Day 3–9 code sits (`context` + `beans`).
    4. Write which modules Boot will pull in later (`web`, `tx`, etc.).
    5. Bookmark the overview page for later Boot debugging.
    6. **Done when:** Diagram + six one-liners exist (no code change required).

- [ ] **Day 11 — Application events**
  - [ ] **Learn:** [Application events](https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events) · [Baeldung — Spring Events](https://www.baeldung.com/spring-events)
  - [ ] **Practice:** Publish event; listener collects results
    1. Create `TaskCreatedEvent` (extends `ApplicationEvent` or use a simple record + `ApplicationEventPublisher`).
    2. In `TaskService`, inject `ApplicationEventPublisher` and publish after “creating” a task.
    3. Create `@Component` listener with `@EventListener` that appends the task id/name to an in-memory `List`.
    4. From `Main`, call create twice; print the list size (expect 2).
    5. Note: publisher runs before listener returns (sync by default unless `@Async`).
    6. **Done when:** List has two entries matching the two publishes.

- [ ] **Day 12 — Environment & `@Value`**
  - [ ] **Learn:** [Environment abstraction](https://docs.spring.io/spring-framework/reference/core/beans/environment.html) · [Using `@Value`](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html#beans-value-annotations)
  - [ ] **Practice:** Inject properties; fail on missing required
    1. Add `src/main/resources/application.properties` with `app.name=Spring Core Lab`.
    2. Enable placeholder support (`@PropertySource("classpath:application.properties")` on config if needed).
    3. Inject `@Value("${app.name}")` into a bean; print it from `Main`.
    4. Inject `@Value("${app.missing:}")` with empty default once; then remove default and use a required key that is absent — confirm startup fails.
    5. Restore valid properties so the app starts again.
    6. **Done when:** `app.name` prints and you observed a loud failure for a missing required property.

- [ ] **Day 13 — Circular dependencies**
  - [ ] **Learn:** [Circular dependencies](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html#beans-dependency-resolution) · [Baeldung — Circular Dependencies in Spring](https://www.baeldung.com/circular-dependencies-in-spring)
  - [ ] **Practice:** Create a cycle, then redesign (no field-injection “fix”)
    1. Create `ServiceA` and `ServiceB` with constructor deps on each other; start context — expect failure.
    2. Copy the exception message into notes (BeanCurrentlyInCreationException / related).
    3. Introduce `Mediator` / `Facade` that both depend on, or move shared logic to a third bean — break the cycle.
    4. Confirm context starts with constructor injection only.
    5. Write why field injection “fixing” cycles is a design smell.
    6. **Done when:** Cycle failure reproduced once, then redesigned graph starts cleanly.

- [ ] **Day 14 — Mini-project A start**
  - [ ] **Learn:** Re-skim [Java config](https://docs.spring.io/spring-framework/reference/core/beans/java.html) + [Classpath scanning](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html)
  - [ ] **Practice:** Console Task Notifier — core wiring
    1. Create package `com.learn.tasks` with `AppConfig` (`@ComponentScan`) and `Main`.
    2. Interface `Notifier` + `ConsoleNotifier` (`@Component`).
    3. `@Service TaskService` with constructor-injected `Notifier`; method `create(String title)` notifies.
    4. From `Main`, start context and call `create("Buy milk")` — see console notify.
    5. Stub `FileNotifier` class (not wired yet) for Day 15.
    6. **Done when:** `TaskService` + `ConsoleNotifier` run via Spring with no `new` of collaborators in the service.

### Week 3 — Consolidation & mini-projects (Days 15–21)

- [ ] **Day 15 — Mini-project A finish**
  - [ ] **Learn:** [`@Profile`](https://docs.spring.io/spring-framework/reference/core/beans/environment.html#beans-definition-profiles) · [Baeldung — Spring Profiles](https://www.baeldung.com/spring-profiles)
  - [ ] **Practice:** Swap notifier via `@Primary` or profiles
    1. Finish `FileNotifier` writing lines to `target/tasks.log` (or project `logs/tasks.log`).
    2. Option A: mark one notifier `@Primary` and switch which is primary; or Option B: `@Profile("console")` / `@Profile("file")` on each.
    3. If profiles: set active profile in `Main` via `ctx.getEnvironment().setActiveProfiles("file")` before refresh, or JVM `-Dspring.profiles.active=file`.
    4. Run both modes; confirm console vs file output.
    5. Add `@PostConstruct` / `@PreDestroy` logs on `TaskService` for lifecycle demo.
    6. **Done when:** Switching Primary/profile changes notifier without editing `TaskService`.

- [ ] **Day 16 — Mini-project B start**
  - [ ] **Learn:** [Stereotype annotations](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html#beans-stereotype-annotations)
  - [ ] **Practice:** Layered catalog packages; facade-only Main
    1. Packages: `com.learn.catalog.model`, `.service`, `.config`, `.repo` (in-memory).
    2. Model `Product(id, name, price)`; in-memory `ProductRepository` bean.
    3. `CatalogFacade` is the only type `Main` retrieves and calls.
    4. Wire with constructor injection + component scan.
    5. From facade, `add` one product and `list` — print to console.
    6. **Done when:** `Main` has zero business logic beyond context + facade call.

- [ ] **Day 17 — Mini-project B services**
  - [ ] **Learn:** [Dependencies](https://docs.spring.io/spring-framework/reference/core/beans/dependencies.html) (collection injection skim)
  - [ ] **Practice:** Multiple services + shared repository
    1. Add `InventoryService` and `PricingService` both depending on the same `ProductRepository`.
    2. Facade orchestrates: add product → inventory stock init → pricing label.
    3. Confirm both services receive the **same** repository singleton (print identity hash).
    4. Optional: inject `List<Plugin>` of all `@Component` plugins implementing `Plugin`.
    5. No `new` of collaborators inside services.
    6. **Done when:** Shared repo identity matches and facade flow prints product + stock/price.

- [ ] **Day 18 — AOP awareness (light)**
  - [ ] **Learn:** [AOP — Spring’s proxy model](https://docs.spring.io/spring-framework/reference/core/aop.html) · [Understanding AOP Proxies](https://docs.spring.io/spring-framework/reference/core/aop/proxying.html)
  - [ ] **Practice:** Proxy notes for later `@Transactional`
    1. Read the proxying page; sketch “caller → proxy → target” in notes.
    2. Write why self-invocation inside a class can skip the proxy (one paragraph).
    3. Write one sentence: `@Transactional` works because Spring wraps the bean in a proxy.
    4. No AspectJ coding required — bookmark for topic 14.
    5. Optional: print `bean.getClass().getName()` for a `@Service` and note if it looks like a CGLIB proxy (may not until AOP/tx on classpath).
    6. **Done when:** One-page notes exist covering proxy vs target and self-invocation pitfall.

- [ ] **Day 19 — Config style comparison**
  - [ ] **Learn:** [Annotation config](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html) · [Java `@Bean` config](https://docs.spring.io/spring-framework/reference/core/beans/java.html)
  - [ ] **Practice:** Same graph: stereotypes vs pure `@Bean`
    1. Rebuild a tiny `MessageService` → `Greeter` graph with stereotypes + scan (branch or package `stereotype`).
    2. Rebuild the same graph with only `@Configuration` `@Bean` methods (no stereotypes) in package `javaconfig`.
    3. Run both from two `Main` classes or two config classes.
    4. In notes: pick a primary style for daily work and why (Boot favors stereotypes + scan).
    5. Keep one style as the default for remaining days.
    6. **Done when:** Both styles produce the same greeting and a written preference exists.

- [ ] **Day 20 — Docs drill**
  - [ ] **Learn:** [BeanFactory vs ApplicationContext](https://docs.spring.io/spring-framework/reference/core/beans/beanfactory.html) · [ApplicationContext](https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html)
  - [ ] **Practice:** Five differences in your own words
    1. Open the BeanFactory vs ApplicationContext docs on spring.io.
    2. Write 5 bullets: e.g. events, i18n, AOP, eager singleton loading, Environment.
    3. Note which type you used all week (`AnnotationConfigApplicationContext`).
    4. Bookmark Core Technologies home for Boot debugging.
    5. Skim one unfamiliar subsection and add one “I don’t know yet” question for Boot week.
    6. **Done when:** Five original bullets exist (not copy-pasted paragraphs).

- [ ] **Day 21 — Mastery dry-run**
  - [ ] **Learn:** [Spring Framework docs home](https://docs.spring.io/spring-framework/reference/) (bookmark for Boot)
  - [ ] **Practice:** Checklist + Boot preview notes
    1. Walk §12 Mastery checklist; tick what you can demo live.
    2. Fix any gap (qualifier, scope, lifecycle) with a 15-minute redo.
    3. Write notes titled “What Boot will automate”: starters, auto-config, embedded server, YAML.
    4. List 3 Core concepts you must still understand when Boot “does nothing.”
    5. Zip or commit your `spring-core-lab` for later reference.
    6. **Done when:** Checklist ≥90% honest ticks and Boot-preview notes exist.

*If on a 2-week pace: compress Week 3 into Days 12–14 by shipping Mini-project A only and skimming events/AOP.*

---

## 5. Hands-on exercises

### Warm-ups

1. Wire `MessageService` → `NotificationClient` with constructor injection only.
2. Two `@Bean` methods of the same type; resolve with `@Qualifier`.
3. Log every lifecycle phase for one bean (construct, post-construct, destroy on context close).
4. Prototype bean injected into singleton—observe and explain instance identity.
5. Component-scan the wrong package; fix the scan base package.

### Wiring & design

6. Interface `PaymentGateway` with `Mock` and `Logging` impls; switch via `@Primary`.
7. Replace field injection with constructor injection; keep behavior identical.
8. Publish an `OrderCreatedEvent`; listener writes to an in-memory list.
9. Inject a `List<Plugin>` of all beans implementing `Plugin` (collection injection).
10. Use `@Value("${app.name}")` with a simple `PropertySourcesPlaceholderConfigurer` or modern Environment setup.

### Stretch

11. Recreate the same graph once with `@Bean` methods only (no stereotypes).
12. Trigger a circular constructor dependency; refactor into a mediator/facade bean.
13. Register a bean programmatically on `AnnotationConfigApplicationContext` and retrieve by type/name.
14. Sketch which classes would become `@RestController` / `@Service` / `@Repository` in a future web app.

---

## 6. Mini-projects

Detailed build guides (Goal → Build steps → DoD → Demo): [`../mini-projects/phase-04-spring/11-spring-core/`](../mini-projects/phase-04-spring/11-spring-core/).

### A. Console Task Notifier (Spring-wired)

**Guide:** [A-console-task-notifier.md](../mini-projects/phase-04-spring/11-spring-core/A-console-task-notifier.md)

- `TaskService` creates tasks; `Notifier` interface with `ConsoleNotifier` and `FileNotifier`.
- Wire via Spring Core (no Boot, no web).
- Switch notifier with `@Primary` or `@Profile("file")` / `@Profile("console")`.
- Print bean lifecycle on startup/shutdown.
- Package: `com.learn.tasks` + `AppConfig` + `Main`.

### B. In-Memory Catalog Engine

**Guide:** [B-inmemory-catalog-engine.md](../mini-projects/phase-04-spring/11-spring-core/B-inmemory-catalog-engine.md)

- `Product`, `InventoryService`, `PricingService`, `CatalogFacade`.
- All dependencies constructor-injected; no `new` of collaborators in services.
- Facade is the only API `Main` calls.
- Prove scopes: pricing strategy as singleton; optional prototype “request context” object for demos.
- Focus: readable config, clear bean graph, zero framework mystery.

### C. (Optional stretch) Config-driven Greeting App

**Guide:** [C-config-greeting-app.md](../mini-projects/phase-04-spring/11-spring-core/C-config-greeting-app.md)

- External properties for greeting template and locale.
- Multiple `GreetingStrategy` beans; select by property name + `@Qualifier` map or custom selector bean.

---

## 7. Industry use cases

- **Enterprise service wiring:** Large codebases depend on constructor DI for testability and clear ownership.
- **Plugin architectures:** Multiple implementations of one interface selected by profile, qualifier, or conditions.
- **Batch / CLI tools:** Spring Context without a web server for scheduled or command-line jobs.
- **Foundation for Boot:** Auto-configuration registers `@Bean` definitions—you are learning that registry by hand.
- **Cross-cutting readiness:** Lifecycle and events underpin auditing, cache warm-up, and graceful shutdown hooks.

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| `new`ing a `@Service` collaborator | Let the container create and inject it |
| Field injection everywhere | Prefer constructors; easier tests and required deps |
| Component scan missing the package | Scan the root package that contains components |
| Multiple candidates, vague error | Add `@Primary` or `@Qualifier`; name beans clearly |
| Assuming prototype works like “new each call” when injected into singleton | Prototype is resolved once at singleton creation unless ObjectFactory/`@Lookup` |
| Ignoring context close | Call `close()` / try-with-resources so destroy callbacks run |
| Circular dependencies “fixed” with field injection | Redesign; cycles are a design smell |
| Mixing XML + annotations randomly | Pick Java config + annotations; treat XML as legacy reading skill |
| Fat `Main` with business logic | `Main` only starts context and calls a facade |
| Learning Boot first, skipping Core | Finish this topic; Boot will make more sense |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Spring Framework — Overview](https://docs.spring.io/spring-framework/reference/overview.html) | Official big picture |
| [IoC Container](https://docs.spring.io/spring-framework/reference/core/beans.html) | Beans, DI, scopes, lifecycle |
| [Annotation-based Container Config](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html) | `@Autowired`, stereotypes |
| [Java-based Container Config](https://docs.spring.io/spring-framework/reference/core/beans/java.html) | `@Configuration`, `@Bean` |
| [Classpath Scanning](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html) | Component scan rules |
| [Baeldung — Spring Core (free articles)](https://www.baeldung.com/spring-tutorial) | Practical DI / annotations walkthroughs |
| [Baeldung — Intro to Inversion of Control and Dependency Injection](https://www.baeldung.com/inversion-control-and-dependency-injection-in-spring) | Mental model |
| [spring.io Guides](https://spring.io/guides) | Short official getting-started style (use Core-relevant later with Boot) |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Spring Framework docs home: https://docs.spring.io/spring-framework/reference/
- Core Technologies (IoC): https://docs.spring.io/spring-framework/reference/core.html
- Bean overview: https://docs.spring.io/spring-framework/reference/core/beans/introduction.html
- Dependencies / DI: https://docs.spring.io/spring-framework/reference/core/beans/dependencies.html
- Bean scopes: https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html
- Bean lifecycle: https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html
- `@Configuration` and `@Bean`: https://docs.spring.io/spring-framework/reference/core/beans/java.html
- Annotation config: https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html
- Classpath scanning and components: https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html
- Environment abstraction: https://docs.spring.io/spring-framework/reference/core/beans/environment.html
- Application events: https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events
- Spring project portal: https://spring.io/projects/spring-framework
- Maven Central — spring-context (dependency coords): https://central.sonatype.com/artifact/org.springframework/spring-context

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Mental model | Explain IoC/DI without Spring jargon soup | Can contrast `new` vs inject |
| 2. Java config | `@Configuration` + `@Bean` graph runs | Two-level dependency works |
| 3. Annotations | Stereotypes + scan + qualifiers | Swap impl without rewriting callers |
| 4. Lifecycle & scopes | Predict instances and init/destroy | Demos + notes match behavior |
| 5. Architecture | Name core modules and container role | Diagram in your notes |
| 6. Mini-projects | A (+ B if 3-week pace) complete | No mystery wiring |
| 7. Gate | Ready for Spring Boot | Mastery checklist ≥90% |

**Weekly rhythm:** Mon–Fri new wiring skills; Sat refactor bean graph; Sun docs + checklist.

**Suggested note prompt each day:** “What would break if Spring did not create this object?”

---

## 12. Mastery checklist

- [ ] Explain IoC and DI in plain language with a non-Spring analogy
- [ ] Create an `AnnotationConfigApplicationContext` and retrieve beans by type
- [ ] Wire dependencies with constructor injection deliberately
- [ ] Use `@Configuration` / `@Bean` and stereotype + component scan successfully
- [ ] Resolve ambiguous beans with `@Qualifier` and/or `@Primary`
- [ ] Demonstrate singleton vs prototype behavior and explain it
- [ ] Use at least one lifecycle callback and shut down the context cleanly
- [ ] Draw Spring’s high-level architecture (context, beans, modules) from memory
- [ ] Avoid circular dependencies by redesign (not by hiding them)
- [ ] Keep `Main` thin; business logic in injected services/facades
- [ ] Completed Mini-project A (and B if on 3-week pace)
- [ ] Can read official IoC docs and find answers without random copying
- [ ] Written notes: “What Boot auto-config will hide—and how I’d debug it”
- [ ] Ready to start [12-spring-boot.md](12-spring-boot.md)
