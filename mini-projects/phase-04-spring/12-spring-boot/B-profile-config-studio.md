# Mini-project B — Profile & Config Studio

**Topic:** [12 — Spring Boot](../../../phase-04-spring/12-spring-boot.md)

---

## Goal

Demonstrate profile-specific beans/properties (feature toggle, fake vs real mailer), type-safe `@ConfigurationProperties` with validation, and Actuator `info` contributing app metadata.

---

## Prerequisites

- [A — Complete CRUD Application](A-complete-crud-application.md) done (or a Boot Web app ready to extend)
- Comfort with `application-{profile}.yml`

---

## Learn links

| Resource | Why |
|----------|-----|
| [Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html) | Profile activation |
| [Type-safe config](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties) | `@ConfigurationProperties` |
| [Actuator info](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.info) | Custom info |
| [Baeldung — Spring Profiles](https://www.baeldung.com/spring-profiles) | Patterns |

### start.spring.io settings (fresh or reuse A)

| Field | Value |
|-------|--------|
| Group | `com.learn` |
| Artifact | `config-studio` |
| Package name | `com.learn.configstudio` |
| Dependencies | **Spring Web**, **Validation**, **Actuator**, **DevTools** (optional) |

If extending CRUD lab, keep package `com.learn.crud` and add modules below.

---

## Project layout

```text
com.learn.configstudio/
  ConfigStudioApplication.java
  mail/
    Mailer.java                 (interface)
    ConsoleMailer.java          (@Profile("dev") or feature off)
    SmtpMailerStub.java         (@Profile("prod"))
  config/
    StudioProperties.java       (@ConfigurationProperties + @Validated)
    MailerConfig.java
  web/
    StudioController.java       # POST /api/notify , GET /api/features
  actuator/
    BuildInfoContributor.java   (InfoContributor)
src/main/resources/
  application.yml
  application-dev.yml
  application-prod.yml
README.md
```

---

## Build steps

- [ ] **1. Scaffold or branch from CRUD**
  1. New Initializr app **or** feature branch of mini-project A.
  2. Base YAML: `spring.application.name: config-studio`, `management.endpoints.web.exposure.include: health,info`.
  3. **Done when:** App starts; `/actuator/health` → **200**.

- [ ] **2. StudioProperties with validation**
  1. Prefix `studio`: `displayName` `@NotBlank`, `featureFlags.mailEnabled` boolean, `maxNotificationsPerMinute` `@Min(1)`.
  2. `@ConfigurationProperties(prefix = "studio")` + `@Validated` + enable scanning.
  3. Invalid YAML (blank name) → fail fast on startup.
  4. **Done when:** Valid props bind; blank `displayName` prevents start.

- [ ] **3. Profile-specific Mailer beans**
  1. Interface `Mailer.send(String to, String body)`.
  2. `ConsoleMailer` `@Profile("dev")` `@Primary` — logs to console.
  3. `SmtpMailerStub` `@Profile("prod")` — logs `[SMTP-STUB] …` (no real SMTP required).
  4. **Done when:** Only one mailer bean exists per active profile.

- [ ] **4. Feature toggle endpoint**
  1. `POST /api/notify` body `{"to":"a@b.com","message":"hi"}`.
  2. If `studio.feature-flags.mail-enabled=false` → **503** or **409** with clear JSON (pick one; document it).
  3. If enabled → call `Mailer` → **202** Accepted.
  4. Validation failure → **400**.
  5. **Done when:** Toggle in YAML changes behavior without code change.

- [ ] **5. GET features / meta**
  1. `GET /api/features` → **200** JSON: displayName, mailEnabled, active profiles (`Environment.getActiveProfiles()`).
  2. **Done when:** Response matches active YAML.

- [ ] **6. Actuator info contributor**
  1. Implement `InfoContributor` adding `studio.displayName` + git-less `build.version` (hardcode `0.1.0` or use `BuildProperties` if available).
  2. `GET /actuator/info` → **200** includes your keys.
  3. **Done when:** Info endpoint shows custom metadata.

- [ ] **7. Dual-profile demo in README**
  1. Document: `--spring.profiles.active=dev` vs `prod`.
  2. Curl matrix for notify on/off and both profiles.
  3. **Done when:** README alone reproduces both mailer behaviors.

---

## Definition of Done

- [ ] Profile-specific beans (fake vs “real” stub mailer)
- [ ] Type-safe `@ConfigurationProperties` with validation
- [ ] Feature toggle affects HTTP behavior with documented status
- [ ] Actuator `info` contributes app metadata; health still works
- [ ] README with profile switch + curl examples

---

## Demo script

```bash
# DEV
java -jar app.jar --spring.profiles.active=dev
curl -i http://localhost:8080/api/features
# → 200 , mailEnabled per YAML, profiles include "dev"

curl -i -X POST http://localhost:8080/api/notify \
  -H "Content-Type: application/json" \
  -d "{\"to\":\"ada@example.com\",\"message\":\"hello\"}"
# → 202 (if enabled) ; console shows ConsoleMailer output

curl -i http://localhost:8080/actuator/info
# → 200 with studio metadata

# PROD
java -jar app.jar --spring.profiles.active=prod
curl -i -X POST http://localhost:8080/api/notify \
  -H "Content-Type: application/json" \
  -d "{\"to\":\"ada@example.com\",\"message\":\"hello\"}"
# → 202 ; logs show [SMTP-STUB]

# Toggle mail-enabled=false → restart → POST
# → 503 or 409 (as documented)
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Both mailers active | Exclusive `@Profile`s |
| Props without validation | `@Validated` + constraints |
| Exposing every Actuator endpoint | Include only `health,info` |
| Feature flag checked only in UI | Enforce in service/controller |
| Info empty | Register `InfoContributor` bean |

---

## After this

Optional [C — Boot CLI sketch](C-boot-cli-sketch.md). Then topic 12 mastery → [13 — Spring Security](../../../phase-04-spring/13-spring-security-jwt.md).
