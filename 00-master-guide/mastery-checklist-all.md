# Mastery Checklist — All Topics

High-level aggregate checklist for Steps 01–21. Use this for a quick scan; **detailed** items live in each topic file under `../phase-*/`.

**Gate rule:** Treat a topic as ready when **≥90%** of its detailed checklist is done *and* you can demo the skills without copying. Phase advance also requires the phase project.

---

## How to use

1. Work from the topic file’s full checklist day to day.
2. Tick the bullets here when the *theme* is truly solid (not after one tutorial).
3. Re-open this file on Day 7 reviews and before each phase gate.

---

## Phase 1 — Core Java

### 01 · Java Fundamentals

Detail: [../phase-01-core-java/01-java-fundamentals.md](../phase-01-core-java/01-java-fundamentals.md)

- [ ] JDK 21 toolchain comfortable (`javac`/`java`, simple Maven or Gradle project)
- [ ] Types, variables, operators, control flow, methods, arrays
- [ ] Console I/O and basic program structure
- [ ] Read stack traces enough to fix compile/runtime errors you cause
- [ ] 3+ small CLI programs written from scratch

### 02 · OOP Concepts

Detail: [../phase-01-core-java/02-oops.md](../phase-01-core-java/02-oops.md)

- [ ] Classes, objects, constructors, encapsulation
- [ ] Inheritance and composition used intentionally
- [ ] Interfaces vs abstract classes — when you’d pick each
- [ ] Polymorphism in a small domain model
- [ ] Packages and access modifiers applied cleanly

### 03 · Collections Framework

Detail: [../phase-01-core-java/03-collections.md](../phase-01-core-java/03-collections.md)

- [ ] List / Set / Map — pick the right one for a problem
- [ ] Iteration, searching, sorting (`Comparable` / `Comparator`)
- [ ] Common implementations (ArrayList, HashSet, HashMap, etc.) and trade-offs
- [ ] Nested collections for real data shapes
- [ ] Avoid classic pitfalls (modifying while iterating, misuse of mutable keys)

### 04 · Exception Handling

Detail: [../phase-01-core-java/04-exception-handling.md](../phase-01-core-java/04-exception-handling.md)

- [ ] Checked vs unchecked — explain and use both appropriately
- [ ] try / catch / finally / try-with-resources
- [ ] Custom exceptions with clear messages
- [ ] Don’t swallow exceptions; fail in a controlled way
- [ ] Propagate vs handle decision makes sense in layered code

### 05 · Multithreading

Detail: [../phase-01-core-java/05-multithreading.md](../phase-01-core-java/05-multithreading.md)

- [ ] Threads vs Runnable; start/lifecycle basics
- [ ] ExecutorService for pooled tasks
- [ ] Shared mutable state hazards; synchronize or confine state
- [ ] Demo that was racy — then fixed
- [ ] Know limits: you are not claiming expert concurrency yet

### 06 · Java 8+ Features

Detail: [../phase-01-core-java/06-java8-plus.md](../phase-01-core-java/06-java8-plus.md)

- [ ] Lambdas and method references
- [ ] Stream API: filter / map / flatMap / collect / reduce as needed
- [ ] Optional used carefully (no cargo-cult)
- [ ] `java.time` for dates/times
- [ ] Modern language features you actually use (records, etc.)

### Phase 1 project — Library Manager

Detail: [../phase-01-core-java/phase-project.md](../phase-01-core-java/phase-project.md)

- [ ] Layered packages; core library flows work
- [ ] OOP + collections + exceptions visible in design
- [ ] Phase 1 gate criteria met

---

## Phase 2 — Data

### 07 · SQL & PostgreSQL

Detail: [../phase-02-data/07-sql-postgresql.md](../phase-02-data/07-sql-postgresql.md)

- [ ] DDL: tables, PKs, FKs, constraints
- [ ] DML: insert/update/delete/select confidently
- [ ] JOINs and aggregations for multi-table questions
- [ ] Indexes — what they’re for (practical level)
- [ ] Local Postgres workflow solid

### 08 · JDBC

Detail: [../phase-02-data/08-jdbc.md](../phase-02-data/08-jdbc.md)

- [ ] Connections and PreparedStatements (no string-concat SQL for inputs)
- [ ] DAO (or similar) mapping rows ↔ objects
- [ ] Transactions: commit and rollback paths
- [ ] Resource cleanup (try-with-resources)
- [ ] Config/secrets not committed

### Phase 2 project — Student/Course

Detail: [../phase-02-data/phase-project.md](../phase-02-data/phase-project.md)

- [ ] Schema + JDBC CRUD + report query
- [ ] Phase 2 gate criteria met

---

## Phase 3 — Frontend & HTTP

### 09 · HTML, CSS, JavaScript

Detail: [../phase-03-frontend-api/09-html-css-js.md](../phase-03-frontend-api/09-html-css-js.md)

- [ ] Semantic HTML structure
- [ ] CSS layout that works on desktop and a narrow viewport
- [ ] DOM events and dynamic list/UI updates
- [ ] `fetch` + JSON parsing
- [ ] Forms and basic client-side validation

### 10 · REST API & HTTP

Detail: [../phase-03-frontend-api/10-rest-http.md](../phase-03-frontend-api/10-rest-http.md)

- [ ] Methods, status codes, headers, body semantics
- [ ] Resource-oriented API design for a small domain
- [ ] Idempotency / safety intuition at a practical level
- [ ] Postman (or equivalent) collection for happy + error paths
- [ ] Can explain request/response for each endpoint from memory

### Phase 3 project — Task Board

Detail: [../phase-03-frontend-api/phase-project.md](../phase-03-frontend-api/phase-project.md)

- [ ] UI + REST contract + Postman collection working together
- [ ] Phase 3 gate criteria met

---

## Phase 4 — Spring Stack

### 11 · Spring Core

Detail: [../phase-04-spring/11-spring-core.md](../phase-04-spring/11-spring-core.md)

- [ ] IoC / DI explained in your own words
- [ ] Beans, wiring, scopes
- [ ] Annotation and/or Java config used deliberately
- [ ] Small app wired without “mystery magic”
- [ ] Ready to see how Boot builds on this

### 12 · Spring Boot

Detail: [../phase-04-spring/12-spring-boot.md](../phase-04-spring/12-spring-boot.md)

- [ ] Starters, auto-config intuition, project layout
- [ ] Controllers, DTOs, validation
- [ ] `application.yml` / properties + profiles
- [ ] Health/actuator or equivalent ops baseline
- [ ] Runnable API you can extend

### 13 · Spring Security & JWT

Detail: [../phase-04-spring/13-spring-security-jwt.md](../phase-04-spring/13-spring-security-jwt.md)

- [ ] Authentication vs authorization clear in your design
- [ ] JWT issued and validated on protected routes
- [ ] Public vs secured endpoint configuration
- [ ] Roles/authorities enforced somewhere meaningful
- [ ] Common failure modes debugged once (401/403)

### 14 · JPA & Hibernate

Detail: [../phase-04-spring/14-jpa-hibernate.md](../phase-04-spring/14-jpa-hibernate.md)

- [ ] Entities, IDs, repositories
- [ ] Associations (at least one real relationship)
- [ ] Transactional service layer
- [ ] Lazy vs eager awareness; one N+1 or LazyInitialization fixed
- [ ] Schema strategy understood (ddl-auto vs migrations)

### Phase 4 project — BookStore API

Detail: [../phase-04-spring/phase-project.md](../phase-04-spring/phase-project.md)

- [ ] Boot + JPA + JWT + Postgres end-to-end
- [ ] Phase 4 gate criteria met

---

## Phase 5 — Distributed Systems

### 15 · Microservices

Detail: [../phase-05-distributed/15-microservices.md](../phase-05-distributed/15-microservices.md)

- [ ] Service boundaries and data ownership reasoned out
- [ ] Sync inter-service communication working
- [ ] Failure/timeout thinking captured in notes
- [ ] Gateway / edge entry idea understood
- [ ] Know when *not* to split further

### 16 · Docker

Detail: [../phase-05-distributed/16-docker.md](../phase-05-distributed/16-docker.md)

- [ ] Image vs container mental model
- [ ] Dockerfile for a Java service
- [ ] Compose multi-service stack (app + DB at minimum)
- [ ] Volumes/networks used at a basic level
- [ ] Rebuild/run loop is muscle memory

### 17 · AWS Cloud

Detail: [../phase-05-distributed/17-aws.md](../phase-05-distributed/17-aws.md)

- [ ] Core building blocks for hosting a Java app identified
- [ ] Hands-on lab: compute and/or data/object storage
- [ ] IAM least-privilege mindset; no secrets in Git
- [ ] Tear-down / cost awareness habit
- [ ] Map “what I run in Compose” → “what I’d run in cloud”

### 18 · Kubernetes

Detail: [../phase-05-distributed/18-kubernetes.md](../phase-05-distributed/18-kubernetes.md)

- [ ] Pod / Deployment / Service basics
- [ ] Local cluster deploy of at least one service
- [ ] ConfigMaps / Secrets for config
- [ ] Rollout mental model (desired vs current state)
- [ ] Can explain why K8s sits above containers

### 19 · Kafka

Detail: [../phase-05-distributed/19-kafka.md](../phase-05-distributed/19-kafka.md)

- [ ] Topics, producers, consumers
- [ ] Local broker running (Compose or similar)
- [ ] Java/Spring producer + consumer demo
- [ ] At-least-once / duplicate handling awareness
- [ ] One domain event flows through the system

### Phase 5 project — BookStore Microservices

Detail: [../phase-05-distributed/phase-project.md](../phase-05-distributed/phase-project.md)

- [ ] ≥2 services + Docker + Kafka event path
- [ ] Phase 5 gate criteria met

---

## Phase 6 — Architecture & AI

### 20 · System Design

Detail: [../phase-06-architecture-ai/20-system-design.md](../phase-06-architecture-ai/20-system-design.md)

- [ ] Requirements → constraints → HLD process practiced
- [ ] Component + data-flow diagrams for a real target system
- [ ] Storage, caching, sync/async trade-offs written down
- [ ] LLD for key components you can implement
- [ ] Designs grounded in systems you already built

### 21 · AI Integration with Java

Detail: [../phase-06-architecture-ai/21-ai-integration.md](../phase-06-architecture-ai/21-ai-integration.md)

- [ ] HTTP/API client to an LLM or embedding service from Java
- [ ] Timeouts, errors, and empty-result handling
- [ ] Simple RAG or doc Q&A pipeline working
- [ ] Prompt/context hygiene basics
- [ ] Know what is demo-quality vs production-hardened

### Phase 6 project — HLD/LLD + RAG Q&A

Detail: [../phase-06-architecture-ai/phase-project.md](../phase-06-architecture-ai/phase-project.md)

- [ ] Design package + working AI slice
- [ ] Phase 6 gate criteria met

---

## Capstone

### Enterprise Learning Hub

Detail: [../capstone/enterprise-platform.md](../capstone/enterprise-platform.md)

- [ ] Auth + core learning domain flows end-to-end
- [ ] API + UI + persistence integrated
- [ ] Containerized demo path documented
- [ ] Architecture matches what runs
- [ ] README: setup, demo script, known limits
- [ ] Capstone gate criteria met

---

## Roll-up status

| Area | Topics ready (≥90%) | Phase project done | Gate passed |
|------|---------------------|--------------------|-------------|
| Phase 1 Core Java | [ ] | [ ] | [ ] |
| Phase 2 Data | [ ] | [ ] | [ ] |
| Phase 3 Frontend + HTTP | [ ] | [ ] | [ ] |
| Phase 4 Spring | [ ] | [ ] | [ ] |
| Phase 5 Distributed | [ ] | [ ] | [ ] |
| Phase 6 Design + AI | [ ] | [ ] | [ ] |
| Capstone | — | [ ] | [ ] |

---

## Related docs

- [learning-path.md](./learning-path.md)  
- [progress-tracker.md](./progress-tracker.md)  
- [weekly-milestones.md](./weekly-milestones.md)  
