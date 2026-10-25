# Mini-project B — In-Memory Catalog Engine

**Topic:** [11 — Spring Core](../../../phase-04-spring/11-spring-core.md) · **Stack:** Spring Framework only (no Boot)

---

## Goal

Wire an in-memory product catalog: `Product`, `InventoryService`, `PricingService`, `CatalogFacade`. All collaborators constructor-injected; `Main` talks only to the facade. Prove singleton pricing vs an optional prototype “request context” bean.

---

## Prerequisites

- Mini-project [A — Console Task Notifier](A-console-task-notifier.md) complete (or equivalent DI comfort)
- Topic 11 scopes/lifecycle practiced
- JDK 17+, Maven/Gradle, `spring-context` 6.x

---

## Learn links

| Resource | Why |
|----------|-----|
| [Bean scopes](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html) | Singleton vs prototype |
| [Dependencies / DI](https://docs.spring.io/spring-framework/reference/core/beans/dependencies.html) | Constructor injection |
| [Stereotype annotations](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html) | `@Service` / `@Component` |
| [Baeldung — Spring bean scopes](https://www.baeldung.com/spring-bean-scopes) | Prototype pitfalls |

**Project setup (not start.spring.io):**

| Setting | Value |
|---------|--------|
| Group / Artifact | `com.learn` / `catalog-engine` |
| Package | `com.learn.catalog` |
| Dependency | `org.springframework:spring-context` (6.x) |

---

## Project layout

```text
catalog-engine/
  pom.xml
  src/main/java/com/learn/catalog/
    Main.java
    AppConfig.java
    model/Product.java
    model/Money.java          (optional simple holder)
    inventory/InventoryService.java
    pricing/PricingService.java
    pricing/PricingStrategy.java
    pricing/StandardPricingStrategy.java
    facade/CatalogFacade.java
    scope/RequestContext.java   (prototype demo)
  README.md
```

---

## Build steps

- [ ] **1. Scaffold + Product model**
  1. Module `catalog-engine`, package `com.learn.catalog`, `spring-context` only.
  2. `Product`: `sku`, `name`, `basePrice` (BigDecimal), `stock`.
  3. **Done when:** Model compiles; no Spring required yet.

- [ ] **2. InventoryService**
  1. `@Service` holding `Map<String, Product>` (or ConcurrentHashMap).
  2. Methods: `add(Product)`, `Optional<Product> find(String sku)`, `boolean reserve(String sku, int qty)`.
  3. Seed 3 products in `@PostConstruct` or via facade bootstrap.
  4. **Done when:** Manual unit test / small main without Spring can add and find by SKU.

- [ ] **3. PricingStrategy + PricingService**
  1. Interface `PricingStrategy` with `BigDecimal price(Product p)`.
  2. `StandardPricingStrategy` `@Component` (singleton): e.g. return `basePrice` or apply 10% tax.
  3. `PricingService` constructor-injects `PricingStrategy`; method `quote(Product)`.
  4. **Done when:** Quote returns expected amount for a sample product.

- [ ] **4. CatalogFacade as sole API**
  1. `@Service CatalogFacade` constructor-injects `InventoryService` + `PricingService`.
  2. Methods: `listSkus()`, `getQuote(String sku)`, `purchase(String sku, int qty)` (reserve + quote).
  3. No other class uses inventory/pricing directly from `Main`.
  4. **Done when:** Facade methods work when hand-wired in a test.

- [ ] **5. Spring wiring**
  1. `AppConfig` `@Configuration` `@ComponentScan("com.learn.catalog")`.
  2. `Main`: open `AnnotationConfigApplicationContext`, get `CatalogFacade` only.
  3. Print list + quotes for two SKUs; attempt purchase that depletes stock.
  4. **Done when:** Full flow runs with zero `new` of services in `Main`.

- [ ] **6. Prove singleton pricing**
  1. Resolve `PricingStrategy` twice from context; print `System.identityHashCode`.
  2. Confirm same instance (singleton default).
  3. **Done when:** Notes record “same hash → singleton”.

- [ ] **7. Prototype RequestContext demo**
  1. `@Component` `@Scope("prototype") RequestContext` with field `String requestId = UUID.randomUUID().toString()` set in constructor/`@PostConstruct`.
  2. Inject `ObjectProvider<RequestContext>` (or `ObjectFactory`) into facade method `demoPrototype()`.
  3. Call `getObject()` twice; print two different `requestId`s.
  4. **Done when:** Two prototype instances differ; you wrote why injecting prototype into a singleton field once is a trap.

- [ ] **8. README + bean graph**
  1. Diagram: `Main` → `CatalogFacade` → (`InventoryService`, `PricingService` → `PricingStrategy`).
  2. Document how to run and what output to expect.
  3. **Done when:** README alone reproduces the demo.

---

## Definition of Done

- [ ] Products + inventory + pricing + facade all Spring-managed
- [ ] Constructor injection everywhere; no collaborator `new` in services
- [ ] `Main` calls only `CatalogFacade`
- [ ] Singleton pricing proven by identity hash
- [ ] Prototype demo via `ObjectProvider` / factory (not naive field inject)
- [ ] Readable config; package `com.learn.catalog`
- [ ] README with run steps and expected console output

---

## Demo script

```text
1. Run Main
   → seeded SKUs listed
2. getQuote for SKU-A
   → price printed (strategy applied)
3. purchase SKU-A qty=1
   → success; stock decreased on next quote/list
4. purchase beyond stock
   → clear failure message (no exception mystery)
5. demoPrototype()
   → two different requestIds printed
6. Print PricingStrategy identityHashCode x2
   → identical values
```

No HTTP.

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| `Main` calling `InventoryService` directly | Facade-only API |
| Prototype injected into singleton field | Use `ObjectProvider` each call |
| Mutable shared map without care | Document single-threaded demo or use concurrent map |
| Pricing logic inside inventory | Keep pricing strategy separate |
| Missing component scan package | Scan `com.learn.catalog` |

---

## After this

Optional: [C — Config-driven Greeting App](C-config-greeting-app.md). Otherwise finish topic 11 mastery checklist and start [12 — Spring Boot](../../../phase-04-spring/12-spring-boot.md) mini-project A.
