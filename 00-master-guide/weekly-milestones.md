# Weekly Milestones — Full Journey

Actionable checkpoints for ~72–83 weeks at 1 hour/day. Ranges absorb pace differences; **finish the deliverable**, then move on.

Mark each row when the deliverable runs or the notes exist — not when you’ve only watched/read.

---

## Phase 1 — Core Java (Weeks 1–20)

### Weeks 1–2 · Topic 01 kickoff

- [x] JDK 21 + IDE + Git smoke test complete ([daily-schedule.md](./daily-schedule.md))
- [x] Variables, types, operators, `if`/`switch`, loops
- [x] Methods + arrays; 3+ small CLI programs committed

### Weeks 3–4 · Topic 01 finish → Topic 02 start

- [ ] File I/O basics or scanner-driven menus working
- [ ] Topic 01 checklist ≥90%
- [ ] First classes/objects; constructors; `this`



### Weeks 5–6 · Topic 02 OOP

- [ ] Inheritance + interfaces in a tiny domain model
- [ ] Encapsulation (private fields, getters where needed)
- [ ] Polymorphism demo (same call, different behavior)



### Weeks 7–8 · Topic 02 finish → Topic 03

- [ ] Topic 02 checklist ≥90%
- [ ] List/Set/Map: add, iterate, lookup
- [ ] Implement at least one “choose the right collection” exercise



### Weeks 9–10 · Topic 03 Collections

- [ ] Sorting/searching with `Comparable`/`Comparator`
- [ ] Nested structures (e.g. `Map<String, List<T>>`)
- [ ] Topic 03 checklist ≥90%



### Weeks 11–12 · Topic 04 → Topic 05 start

- [ ] try/catch/finally; custom exceptions; topic 04 ≥90%
- [ ] `Thread` / `Runnable`; start/join mental model
- [ ] Identify a race in a broken demo and fix it



### Weeks 13–14 · Topic 05 Multithreading

- [ ] `ExecutorService` for a small task pool
- [ ] `synchronized` (or concurrent collection) used correctly
- [ ] Topic 05 checklist ≥90%



### Weeks 15–16 · Topic 06 Java 8+

- [ ] Lambdas + Stream pipelines (filter/map/collect)
- [ ] `Optional` used without abuse; `java.time` for dates
- [ ] Topic 06 checklist ≥90%



### Weeks 17–20 · Phase 1 project — Library Manager

- [ ] Package layers: domain / service / repository
- [ ] Core flows: add, list, search, borrow/return (or equivalent)
- [ ] Exceptions + collections throughout; optional concurrent report
- [ ] **Phase 1 gate passed** ([learning-path.md](./learning-path.md))

---



## Phase 2 — Data (Weeks 21–26)



### Weeks 21–22 · Topic 07 SQL

- [ ] Local Postgres up; practice DB created
- [ ] CREATE/INSERT/UPDATE/DELETE; PRIMARY/FOREIGN KEY
- [ ] Multi-table JOINs written by hand



### Weeks 23–24 · Topic 07 finish → Topic 08

- [ ] Indexes/constraints notes; topic 07 ≥90%
- [ ] JDBC connection + `PreparedStatement` SELECT/INSERT
- [ ] First DAO class committed



### Weeks 25–26 · Topic 08 + Student/Course project

- [ ] Transactions: commit + rollback path demonstrated
- [ ] Topic 08 checklist ≥90%
- [ ] Student/Course CRUD + one report query
- [ ] **Phase 2 gate passed**

---



## Phase 3 — Frontend & HTTP (Weeks 27–33)



### Weeks 27–29 · Topic 09 HTML/CSS/JS

- [ ] Semantic page structure; basic responsive layout
- [ ] DOM events; forms; client-side list rendering
- [ ] `fetch` calling a public or local endpoint



### Weeks 30–31 · Topic 09 finish → Topic 10

- [ ] Topic 09 checklist ≥90%
- [ ] HTTP methods, status codes, headers, JSON bodies
- [ ] Resource design for tasks (or equivalent) on paper



### Weeks 32–33 · Topic 10 + Task Board project

- [ ] Postman collection: happy path + one 4xx
- [ ] Topic 10 checklist ≥90%
- [ ] Task Board UI ↔ API contract working
- [ ] **Phase 3 gate passed**

---



## Phase 4 — Spring Stack (Weeks 34–47)



### Weeks 34–35 · Topic 11 Spring Core

- [ ] Manual DI vs container DI explained in notes
- [ ] Beans wired in a small non-Boot or minimal context app
- [ ] Topic 11 checklist ≥90%



### Weeks 36–38 · Topic 12 Spring Boot

- [ ] Boot app with controller + validation + `application.yml`
- [ ] Profiles (dev/local); actuator or health endpoint
- [ ] Topic 12 checklist ≥90%



### Weeks 39–41 · Topic 13 Security & JWT

- [ ] Register/login issuing JWT
- [ ] Protected routes reject missing/invalid tokens
- [ ] Roles or authorities enforced on at least one endpoint
- [ ] Topic 13 checklist ≥90%



### Weeks 42–44 · Topic 14 JPA & Hibernate

- [ ] Entities + repositories; one relationship (e.g. 1-N)
- [ ] Transaction boundary understood on service methods
- [ ] Lazy-loading pitfall reproduced and fixed once
- [ ] Topic 14 checklist ≥90%



### Weeks 45–47 · BookStore API phase project

- [ ] Auth + books/orders (or equivalent) end-to-end
- [ ] Postgres persistence; validation; clear errors
- [ ] README: run instructions + env vars
- [ ] **Phase 4 gate passed**

---



## Phase 5 — Distributed Systems (Weeks 48–65)



### Weeks 48–51 · Topic 15 Microservices

- [ ] Split plan: what becomes which service + data ownership
- [ ] Sync call between two services working locally
- [ ] Notes on failure modes (timeouts, partial failure)
- [ ] Topic 15 checklist ≥90% (may finish into week 52)



### Weeks 52–53 · Topic 16 Docker

- [ ] Dockerfile for a Spring service
- [ ] Compose: app + Postgres healthy
- [ ] Topic 16 checklist ≥90%



### Weeks 54–56 · Topic 17 AWS

- [ ] IAM basics understood; no long-lived keys in Git
- [ ] Deploy or run a lab: compute + datastore or object storage
- [ ] Cost/safety notes (free tier / tear-down habit)
- [ ] Topic 17 checklist ≥90%



### Weeks 57–59 · Topic 18 Kubernetes

- [ ] Local cluster (minikube/kind) running
- [ ] Deployment + Service for one app
- [ ] ConfigMap/Secret used for config
- [ ] Topic 18 checklist ≥90%



### Weeks 60–62 · Topic 19 Kafka

- [ ] Local Kafka (Compose or similar)
- [ ] Producer + consumer in Java/Spring
- [ ] Topic 19 checklist ≥90%



### Weeks 63–65 · BookStore Microservices project

- [ ] ≥2 services; Docker Compose up
- [ ] Kafka event on a domain action (e.g. order placed)
- [ ] Short architecture note committed
- [ ] **Phase 5 gate passed**

---



## Phase 6 — Architecture & AI (Weeks 66–73)



### Weeks 66–69 · Topic 20 System Design

- [ ] Requirements → constraints → HLD for Learning Hub vision
- [ ] Component diagram + data flow
- [ ] Trade-off notes (DB, cache, sync/async) grounded in what you built
- [ ] Topic 20 checklist ≥90%



### Weeks 70–73 · Topic 21 AI + phase project

- [ ] Java client to LLM/embedding API with timeouts/errors handled
- [ ] RAG or Q&A over a small doc set
- [ ] Topic 21 checklist ≥90%
- [ ] HLD/LLD + RAG demo package complete
- [ ] **Phase 6 gate passed**

---



## Capstone — Enterprise Learning Hub (Weeks 74–83)



### Weeks 74–75 · Foundation

- [ ] Repo structure, Compose skeleton, env template
- [ ] Auth vertical slice working
- [ ] Core schema in place



### Weeks 76–77 · Core product

- [ ] Main domain flows (courses/content/progress or equivalent)
- [ ] API + UI connected for happy path



### Weeks 78–79 · Hardening

- [ ] Validation, errors, basic observability (logs/health)
- [ ] Persistence edge cases; seed data for demo



### Weeks 80–81 · Distributed / optional extras

- [ ] Containerized demo path reliable
- [ ] Optional: Kafka event or AI feature wired from Phase 5–6



### Weeks 82–83 · Polish & gate

- [ ] README: setup, demo script, known limits
- [ ] Architecture doc matches running system
- [ ] **Capstone gate passed** — journey complete

---



## Milestone dashboard (copy into tracker)


| Checkpoint    | Target week band | Done? |
| ------------- | ---------------- | ----- |
| Phase 1 gate  | 17–20            | [ ]   |
| Phase 2 gate  | 25–26            | [ ]   |
| Phase 3 gate  | 32–33            | [ ]   |
| Phase 4 gate  | 45–47            | [ ]   |
| Phase 5 gate  | 63–65            | [ ]   |
| Phase 6 gate  | 70–73            | [ ]   |
| Capstone gate | 80–83            | [ ]   |


---



## Related docs

- [timeline-estimates.md](./timeline-estimates.md)  
- [progress-tracker.md](./progress-tracker.md)  
- [learning-path.md](./learning-path.md)

