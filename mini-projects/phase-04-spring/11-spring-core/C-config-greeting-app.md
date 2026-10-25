# Mini-project C — Config-driven Greeting App (optional stretch)

**Topic:** [11 — Spring Core](../../../phase-04-spring/11-spring-core.md) · **Stack:** Spring Framework only (no Boot)

---

## Goal

External properties drive a greeting template and locale. Multiple `GreetingStrategy` beans exist; a selector chooses one by property name using `@Qualifier` map or a custom selector bean.

---

## Prerequisites

- [A](A-console-task-notifier.md) complete; [B](B-inmemory-catalog-engine.md) recommended
- Comfort with `@Qualifier`, `Environment` / `PropertySource`
- JDK 17+, `spring-context` 6.x

---

## Learn links

| Resource | Why |
|----------|-----|
| [Environment abstraction](https://docs.spring.io/spring-framework/reference/core/beans/environment.html) | Properties into beans |
| [`@Qualifier`](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html) | Named strategies |
| [Baeldung — Spring @Qualifier](https://www.baeldung.com/spring-qualifier-annotation) | Disambiguation patterns |

**Project setup (not start.spring.io):**

| Setting | Value |
|---------|--------|
| Group / Artifact | `com.learn` / `greeting-app` |
| Package | `com.learn.greeting` |
| Dependency | `org.springframework:spring-context` (6.x) |

---

## Project layout

```text
greeting-app/
  pom.xml
  src/main/java/com/learn/greeting/
    Main.java
    AppConfig.java
    GreetingStrategy.java
    FormalGreetingStrategy.java
    CasualGreetingStrategy.java
    LoudGreetingStrategy.java
    GreetingSelector.java
    GreetingService.java
  src/main/resources/
    greeting.properties
  README.md
```

---

## Build steps

- [ ] **1. Scaffold + properties file**
  1. Create module; add `src/main/resources/greeting.properties`:
     ```properties
     greeting.strategy=casual
     greeting.template=Hello, {name}!
     greeting.locale=en-US
     ```
  2. **Done when:** Properties file is on the classpath.

- [ ] **2. PropertySource on AppConfig**
  1. `@Configuration` `@ComponentScan("com.learn.greeting")` `@PropertySource("classpath:greeting.properties")`.
  2. Inject `Environment` or `@Value("${greeting.template}")` into a config/`@Bean`.
  3. **Done when:** A bean can read `greeting.template` at runtime.

- [ ] **3. GreetingStrategy implementations**
  1. Interface `String greet(String name, String template)`.
  2. Three `@Component` beans with qualifiers: `@Qualifier("formal")`, `"casual"`, `"loud"` (loud uppercases result).
  3. **Done when:** Each strategy produces a distinct string for the same inputs.

- [ ] **4. Selector bean**
  1. `GreetingSelector` takes `Map<String, GreetingStrategy>` (Spring can inject by bean name/`@Qualifier`) **or** list + name lookup.
  2. Read `greeting.strategy` from `Environment`; resolve strategy; fail fast with clear message if unknown.
  3. **Done when:** Changing property name switches strategy without code edits to callers.

- [ ] **5. GreetingService + Main**
  1. `GreetingService` uses selector + template property; method `greet(String name)`.
  2. `Main` boots context, greets `"Ada"` twice.
  3. **Done when:** Output matches active strategy + template.

- [ ] **6. Swap strategy via properties**
  1. Set `greeting.strategy=formal` (and optionally change template).
  2. Re-run; confirm different output.
  3. Try invalid strategy name → clear exception/message.
  4. **Done when:** Valid swap works; invalid name fails loudly.

- [ ] **7. Document locale note**
  1. Read `greeting.locale`; print it (full i18n optional — printing locale is enough for stretch).
  2. README lists property keys and valid strategy names.
  3. **Done when:** README documents all keys and how to switch.

---

## Definition of Done

- [ ] External `greeting.properties` loaded via `@PropertySource`
- [ ] ≥2 (prefer 3) `GreetingStrategy` beans with clear qualifiers/names
- [ ] Selector chooses strategy by property value
- [ ] Template and locale come from properties
- [ ] Unknown strategy fails with a readable error
- [ ] README documents keys and run command

---

## Demo script

```text
1. greeting.strategy=casual → run Main
   → "Hello, Ada!" (or your template)
2. Change to formal → re-run
   → formal variant (e.g. "Good day, Ada.")
3. Change to loud → re-run
   → uppercased greeting
4. Set strategy=nope → re-run
   → clear failure naming valid options
```

No HTTP.

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Hard-coding strategy in service | Read property + selector |
| Map injection key mismatch | Align `@Qualifier` / bean names with property values |
| Forgetting `@PropertySource` | File on classpath alone is not enough |
| Silent default on bad name | Fail fast with allowed values listed |

---

## After this

Return to topic 11 mastery checklist. Start [12 — Spring Boot](../../../phase-04-spring/12-spring-boot.md) → [Complete CRUD Application](../12-spring-boot/A-complete-crud-application.md).
