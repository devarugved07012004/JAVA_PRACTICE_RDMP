# Mini-project C — Boot CLI sketch (optional)

**Topic:** [12 — Spring Boot](../../../phase-04-spring/12-spring-boot.md)

---

## Goal

Either (1) run one small Spring Boot CLI / non-web command-line sketch, **or** (2) document a deliberate skip with reason — time is better spent on the [Complete CRUD Application](A-complete-crud-application.md) if CLI tooling is unavailable or flaky.

---

## Prerequisites

- Mini-project A strongly preferred complete
- Curiosity about Boot outside servlet stack

---

## Learn links

| Resource | Why |
|----------|-----|
| [Spring Boot — Application exit / runners](https://docs.spring.io/spring-boot/reference/features/spring-application.html) | `ApplicationRunner` / `CommandLineRunner` |
| [Creating a CLI application](https://spring.io/guides) (search “command line”) | Patterns |
| Historical note | Classic “Spring Boot CLI” (`spring` Groovy shell) is uncommon in modern Boot 3 learning paths — prefer a **Jar + runner** sketch |

### start.spring.io settings (if building the runner sketch)

| Field | Value |
|-------|--------|
| Group | `com.learn` |
| Artifact | `boot-cli-sketch` |
| Package name | `com.learn.clisketch` |
| Dependencies | **Spring Boot** (core) — you may omit **Web**; add nothing else required |
| Packaging | Jar |

---

## Project layout

```text
com.learn.clisketch/
  BootCliSketchApplication.java
  HelloRunner.java              # ApplicationRunner or CommandLineRunner
src/main/resources/
  application.yml               # spring.main.web-application-type=none
README.md                       # OR SKIP.md with reason
```

---

## Build steps

Choose **Path R** (runner) or **Path S** (documented skip).

### Path R — ApplicationRunner sketch

- [ ] **1. Generate non-web Boot app**
  1. Initializr with settings above (no Web starter, or set web type none).
  2. `application.yml`: `spring.main.web-application-type: none`.
  3. **Done when:** App starts and exits (or stays up only if you add a runner that finishes).

- [ ] **2. Implement runner**
  1. `@Component class HelloRunner implements ApplicationRunner`.
  2. Read args: if `--name=Ada` present, print `Hello, Ada`; else `Hello, world`.
  3. Use `SpringApplication.exit` / return so process ends with code 0.
  4. **Done when:** `java -jar … --name=Ada` prints greeting and exits 0.

- [ ] **3. Optional property**
  1. `@Value("${cli.prefix:Hello}")` prefixes the line.
  2. Override with `--cli.prefix=Howdy`.
  3. **Done when:** Property override visible in output.

- [ ] **4. README**
  1. Document build, run, sample args, exit code.
  2. **Done when:** Cold run from README works.

### Path S — Documented skip

- [ ] **1. Write `SKIP.md` (or README section)**
  1. State: skipped Boot CLI / Groovy CLI in favor of CRUD depth.
  2. Note date + that ApplicationRunner pattern is understood from docs skim.
  3. Link to mini-project A as primary deliverable.
  4. **Done when:** Skip reason is written and topic checklist still tracks A as Must.

---

## Definition of Done

**Either:**

- [ ] Non-web Boot JAR runs a runner with CLI args and documented exit, **or**
- [ ] Written skip with reason; CRUD A remains the priority deliverable

---

## Demo script

**Path R:**

```bash
mvn -q -DskipTests package
java -jar target/boot-cli-sketch-*.jar --name=Ada
# → prints greeting; process exits 0

java -jar target/boot-cli-sketch-*.jar --cli.prefix=Howdy --name=Ada
# → Howdy, Ada (or equivalent)
```

No HTTP statuses.

**Path S:** Open `SKIP.md`; confirm reason + pointer to A.

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Spending days on obsolete Groovy CLI | Prefer ApplicationRunner JAR |
| Web server left running | `web-application-type: none` |
| Skip without writing it down | Path S requires a short doc |
| Skipping A to chase CLI | Never — A is Must |

---

## After this

Finish topic 12 mastery checklist. Proceed to [13 — Spring Security & JWT](../../../phase-04-spring/13-spring-security-jwt.md) → [Secure Login System](../13-spring-security-jwt/A-secure-login-system.md).
