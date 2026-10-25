# 16 — Docker

**Duration:** ~2 weeks (14 days) · **Cadence:** 1 hour/day  
**Focus:** Docker Basics, Images, Containers, Dockerfile, Networks, Volumes, Docker Compose, Containerizing Spring Boot

---

## 1. Why this topic is important

Docker packages an application and its runtime so “works on my machine” becomes “works the same in Compose, CI, and cloud.” For distributed Java, you will run **Postgres, Eureka, Zipkin, Kafka, and multiple Spring services** without installing each by hand. Images, containers, networks, volumes, and Compose are the daily vocabulary of modern backend delivery.

This topic is deliberately practical: build Spring Boot JARs into images, wire services on a user-defined network, persist DB data with volumes, and bring a multi-service stack up with one Compose file—the same muscle you need for the Phase 5 project and for AWS/Kubernetes later.

---

## 2. Prerequisites

- [15-microservices.md](15-microservices.md) in progress or complete enough to have ≥1 Spring Boot JAR (even a single CRUD app is fine to start)
- JDK 17+; Maven/Gradle producing an executable JAR
- Comfortable with terminal (PowerShell, cmd, or bash)
- Basic networking intuition (host, port, localhost)

**Environment you will set up:**

- Docker Desktop (Windows) or Docker Engine + Compose plugin
- Verify: `docker version` and `docker compose version`
- A sample Spring Boot app (Phase 4 BookStore or a service from topic 15)

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Image vs container | Image = immutable template; container = running instance |
| Docker Hub / registries | Pull public images (`postgres`, `eclipse-temurin`) |
| `docker run` / `ps` / `logs` / `stop` / `rm` | Lifecycle basics |
| Ports | `-p host:container`; publishing vs exposing |
| Dockerfile | Layered build instructions; base image |
| `.dockerignore` | Keep build context small (skip `target/`, `.git`) |
| Ephemeral filesystem | Data in container vanishes unless volumes |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Multi-stage Dockerfile | Build with JDK image; run with JRE/slim image |
| Layers & cache | Order Dockerfile for faster rebuilds |
| Volumes & bind mounts | Persist Postgres data; mount config |
| User-defined bridge networks | Service DNS by container name |
| Environment variables | Pass Spring config (`SPRING_DATASOURCE_URL`, etc.) |
| Docker Compose | `services`, `ports`, `environment`, `volumes`, `depends_on` |
| Healthchecks | Wait until Postgres/app is ready |
| Logs & exec | `docker compose logs -f`; `exec` into container |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| Non-root user in image | Security baseline for Java containers |
| Resource limits | Memory limits matter for JVM (`JAVA_TOOL_OPTIONS`) |
| Compose profiles | Optional Zipkin/Kafka services |
| Image tagging & versions | Avoid `latest` in learning notes for reproducibility |
| Debugging containers | Override entrypoint; inspect networks |
| Bridge to K8s/AWS | Same image artifact moves to orchestrators/cloud |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Engine, images, Dockerfile (Days 1–7)

- [ ] **Day 1 — Install Docker**
  - [ ] **Learn:** [Docker Get started](https://docs.docker.com/get-started/) · [Install Docker Desktop (Windows)](https://docs.docker.com/desktop/setup/install/windows-install/)
  - [ ] **Practice:** Install Docker and prove the engine works
    1. Install Docker Desktop (Windows) or Docker Engine + Compose plugin for your OS; finish the installer reboot/WSL2 prompt if prompted.
    2. Open a **new** terminal. Run `docker version` and `docker compose version`. Both must print client/server versions (not “command not found”).
    3. Run `docker run --rm hello-world` and confirm the success message.
    4. Open Docker Desktop UI briefly; note whether the engine is running and (on Windows) that WSL2 backend is enabled.
    5. In notes, write one sentence: image vs container.
    6. **Done when:** `hello-world` succeeded and both version commands work.

- [ ] **Day 2 — Run Postgres**
  - [ ] **Learn:** [docker run reference](https://docs.docker.com/reference/cli/docker/container/run/) · [Postgres official image](https://hub.docker.com/_/postgres)
  - [ ] **Practice:** Pull and run Postgres with a password
    1. Run `docker pull postgres:16`.
    2. Start a named container: `docker run -d --name pg16 -e POSTGRES_PASSWORD=secret -p 5432:5432 postgres:16`.
    3. Check it is up: `docker ps` (look for `pg16`) and `docker logs pg16` until ready.
    4. Connect: either a host GUI/CLI to `localhost:5432` **or** `docker exec -it pg16 psql -U postgres` then `\conninfo`.
    5. Inside psql run `SELECT version();` then `\q`.
    6. **Done when:** You queried Postgres from the host tool or `psql` in the container.

- [ ] **Day 3 — Container lifecycle**
  - [ ] **Learn:** [docker container CLI](https://docs.docker.com/reference/cli/docker/container/)
  - [ ] **Practice:** Drill named-container lifecycle commands
    1. Run `docker run -d --name lifecycle-demo nginx:alpine`.
    2. Run `docker ps`, then `docker logs lifecycle-demo`, then `docker stop lifecycle-demo`.
    3. Run `docker start lifecycle-demo`, confirm with `docker ps`, then `docker stop lifecycle-demo`.
    4. Run `docker rm lifecycle-demo` and confirm it is gone with `docker ps -a`.
    5. In notes, list the six commands you used and what each does in one line.
    6. **Done when:** The named container was created, stopped, started, and removed without leftover `lifecycle-demo`.

- [ ] **Day 4 — First Dockerfile**
  - [ ] **Learn:** [Dockerfile reference](https://docs.docker.com/reference/dockerfile/) · [Build context](https://docs.docker.com/build/concepts/context/)
  - [ ] **Practice:** Build and run a minimal custom image
    1. Create folder `docker-lab/day04` with a tiny app (static `index.html` served by nginx **or** a one-class Java jar).
    2. Write a minimal `Dockerfile` (e.g. `FROM nginx:alpine` + `COPY index.html /usr/share/nginx/html/`).
    3. Build: `docker build -t hello-docker:day04 .`
    4. Run: `docker run --rm -p 8088:80 hello-docker:day04` (adjust port/cmd for Java).
    5. Hit `http://localhost:8088` (or curl) and confirm your content.
    6. **Done when:** Browser/curl shows your custom content from the image you built.

- [ ] **Day 5 — Multi-stage Spring Boot**
  - [ ] **Learn:** [Multi-stage builds](https://docs.docker.com/build/building/multi-stage/) · [Spring Boot Docker guide](https://spring.io/guides/topicals/spring-boot-docker/) · [eclipse-temurin](https://hub.docker.com/_/eclipse-temurin)
  - [ ] **Practice:** Multi-stage Dockerfile for a Spring Boot JAR
    1. Pick a Spring Boot app that builds an executable JAR (BookStore or topic-15 service).
    2. Add a multi-stage `Dockerfile`: stage 1 builds with Maven/Gradle JDK image; stage 2 uses `eclipse-temurin:17-jre` (or 21) and copies the JAR.
    3. Build: `docker build -t bookstore-api:day05 .`
    4. Run with a published port, e.g. `docker run --rm -p 8080:8080 bookstore-api:day05`.
    5. Curl `/actuator/health` or a known GET endpoint.
    6. **Done when:** Health/endpoint returns success from the multi-stage image.

- [ ] **Day 6 — .dockerignore & tags**
  - [ ] **Learn:** [.dockerignore](https://docs.docker.com/build/concepts/context/#dockerignore-files) · [Spring Boot container images](https://docs.spring.io/spring-boot/reference/packaging/container-images/index.html)
  - [ ] **Practice:** Shrink context and retag the image
    1. Add `.dockerignore` excluding `.git`, `target/`, `build/`, `.idea`, `*.md` (keep what the Dockerfile needs).
    2. Time a rebuild: `docker build -t bookstore-api:dev .` and note wall-clock seconds in notes.
    3. Change only app source; rebuild again and note whether layers were cached.
    4. Tag alias: `docker tag bookstore-api:dev bookstore-api:day06` and run `docker images | findstr bookstore` (or `grep`).
    5. Confirm `bookstore-api:dev` appears in the image list.
    6. **Done when:** `.dockerignore` exists, rebuild notes exist, and `bookstore-api:dev` is tagged.

- [ ] **Day 7 — Image vs container review**
  - [ ] **Learn:** [Docker Get started](https://docs.docker.com/get-started/) (recap layers)
  - [ ] **Practice:** Explain image vs container and layer cache
    1. In notes, define **image** vs **container** in your own words (2–3 sentences each).
    2. Draw or list Dockerfile layers for your Spring image (base → deps → app).
    3. Write when a layer rebuilds vs when cache hits (one concrete example from Day 5–6).
    4. Run `docker images` and `docker ps -a`; write which rows are images vs containers.
    5. Answer aloud (or in notes): “Can I run two containers from one image?”
    6. **Done when:** Notes cover image/container + cache example and you can explain without the docs open.

### Week 2 — Networks, volumes, Compose, Spring stack (Days 8–14)

- [ ] **Day 8 — User-defined network**
  - [ ] **Learn:** [Docker networking](https://docs.docker.com/engine/network/)
  - [ ] **Practice:** Wire Postgres + app on a custom bridge network
    1. Create network: `docker network create bookstore-net`.
    2. Run Postgres on it: `docker run -d --name db --network bookstore-net -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=bookstore postgres:16`.
    3. Run your Spring app on the same network with JDBC URL host `db` (not `localhost`), e.g. `SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/bookstore`.
    4. Publish app port to host (`-p 8080:8080`) and curl a DB-backed endpoint or `/actuator/health`.
    5. Intentionally break the URL host to `localhost` once; read app logs; fix back to `db`.
    6. **Done when:** App reaches Postgres via hostname `db` on `bookstore-net`.

- [ ] **Day 9 — Named volumes**
  - [ ] **Learn:** [Volumes](https://docs.docker.com/engine/storage/volumes/) · [Manage data in Docker](https://docs.docker.com/engine/storage/)
  - [ ] **Practice:** Prove Postgres data survives container recreate
    1. Create volume: `docker volume create pgdata`.
    2. Run Postgres with `-v pgdata:/var/lib/postgresql/data` (plus password/db env); create a table and insert one row via `psql`.
    3. `docker stop` + `docker rm` the Postgres container (keep the volume).
    4. Start a **new** Postgres container with the same volume mount.
    5. Query the table; confirm the row still exists. Run `docker volume ls` and note `pgdata`.
    6. **Done when:** Data survives container delete/recreate via the named volume.

- [ ] **Day 10 — Compose basics**
  - [ ] **Learn:** [Compose overview](https://docs.docker.com/compose/) · [Compose file reference](https://docs.docker.com/compose/compose-file/)
  - [ ] **Practice:** Write `compose.yaml` for `db` + `app`
    1. Create `compose.yaml` with services `db` (postgres:16) and `app` (build: . or image).
    2. Map ports (`5432` optional, `8080:8080`), set env for DB password and JDBC URL using service name `db`.
    3. Add a named volume for Postgres data under `volumes:`.
    4. Optionally use `env_file: .env` for passwords (do not commit real secrets).
    5. Run `docker compose up --build -d` then `docker compose ps` and curl the app.
    6. **Done when:** Both services are up via Compose and the app responds on the mapped port.

- [ ] **Day 11 — Healthchecks & depends_on**
  - [ ] **Learn:** [Compose depends_on](https://docs.docker.com/compose/compose-file/05-services/#depends_on) · [HEALTHCHECK](https://docs.docker.com/reference/dockerfile/#healthcheck)
  - [ ] **Practice:** Wait for healthy Postgres before starting the app
    1. Add a `healthcheck` on `db` (e.g. `pg_isready -U postgres`).
    2. Set `app.depends_on.db.condition: service_healthy` (Compose v2 syntax).
    3. Run `docker compose down` then `docker compose up --build`.
    4. Watch `docker compose ps` until `db` is healthy and `app` starts after.
    5. In notes, write what fails if you start the app before Postgres accepts connections.
    6. **Done when:** App starts only after DB healthcheck passes on a clean `up`.

- [ ] **Day 12 — Multi-service Compose**
  - [ ] **Learn:** [Compose services](https://docs.docker.com/compose/compose-file/05-services/)
  - [ ] **Practice:** Run two Spring services (or app + Zipkin) on one network
    1. Add a second service to Compose: another Spring Boot service **or** Zipkin (`openzipkin/zipkin`) on the default Compose network.
    2. Give each a unique host port; keep them on the Compose network for DNS.
    3. From service A, call service B by Compose service name (or open Zipkin UI from the host).
    4. Run `docker compose logs -f` and confirm both started cleanly.
    5. Document service names and ports in a 5-line README snippet.
    6. **Done when:** Both containers run on the same Compose network and you can reach each from the host.

- [ ] **Day 13 — JVM memory in containers**
  - [ ] **Learn:** [Spring Boot Docker topical](https://spring.io/guides/topicals/spring-boot-docker/) (JVM notes)
  - [ ] **Practice:** Set and verify JVM memory flags in the container
    1. Set `JAVA_TOOL_OPTIONS=-Xms128m -Xmx256m` (or `JAVA_OPTS`) on the app service in Compose/env.
    2. Recreate the app container: `docker compose up -d --force-recreate app`.
    3. Confirm flags in logs or `docker exec <app> ps aux` / print `ManagementFactory` memory if you add a tiny log line.
    4. Optionally lower `-Xmx` too far once and note OOM/restart behavior (then restore a sane limit).
    5. Write 3 sentences on why container memory limits matter for the JVM.
    6. **Done when:** JVM flags are applied in the running container and documented in notes.

- [ ] **Day 14 — Full Compose demo**
  - [ ] **Learn:** Recap [Compose](https://docs.docker.com/compose/) · [Dockerfile](https://docs.docker.com/reference/dockerfile/)
  - [ ] **Practice:** One-command demo and mastery checklist
    1. From a clean state run `docker compose down -v` (know this deletes volumes) **or** `down` without `-v` if you need data.
    2. Run `docker compose up --build` and wait until healthy.
    3. Execute a short demo: hit health + one business endpoint; capture commands in `README.md`.
    4. Tick this topic’s mastery checklist items you can honestly claim.
    5. Write start order and known limitations (ports, passwords via env) in the README.
    6. **Done when:** README has compose up/demo commands and checklist is marked for what you actually ran.

*Stretch if ahead: add Eureka/Gateway from topic 15 into Compose (preview of phase project).*

---

## 5. Hands-on exercises

1. `docker pull` / `docker images` / `docker rmi` cleanup drill (don’t delete needed images blindly).
2. Run Nginx briefly; browse localhost; stop and remove.
3. Postgres: create a table; `docker rm` without volume (data gone); repeat **with** volume (data stays).
4. Publish wrong port; diagnose with `docker ps` and browser/curl.
5. Spring Boot Dockerfile: single-stage vs multi-stage size comparison (`docker images`).
6. Pass `SPRING_PROFILES_ACTIVE=docker` and a profile-specific datasource URL.
7. `docker exec -it <db> psql -U ...` and list tables after app migration/ddl.
8. Compose: break JDBC URL hostname; read app logs; fix to service name.
9. Add a bind mount for a local `application-docker.yml` (optional learning).
10. `docker compose down -v` vs `down`—document difference (destructive volumes).
11. View container IP and DNS: `docker network inspect`.
12. Build arg vs env: demonstrate one build-time and one runtime setting.

---

## 6. Mini-projects

> **Build guides:** [A Boot + Postgres Compose](../mini-projects/phase-05-distributed/16-docker/A-boot-postgres-compose.md) · [B Two-App Network Lab](../mini-projects/phase-05-distributed/16-docker/B-two-app-network-lab.md) · [C Microservices Compose Preview](../mini-projects/phase-05-distributed/16-docker/C-microservices-compose-preview.md) · [Index](../mini-projects/README.md)

### A. Spring Boot + Postgres Compose Stack

- Dockerfile (multi-stage) for your BookStore or catalog service
- `compose.yaml` with `app` + `postgres`
- Volume for DB; network for service DNS
- README: `docker compose up --build`, sample curl, env vars

### B. Two-App Network Lab

- App A (API) + App B (dependency stub) or App + Zipkin
- Prove HTTP calls use **container DNS names**, not `localhost`
- Healthcheck so API starts after dependency is ready (best effort)

### C. (Stretch) Microservices Compose Preview

- Compose file skeleton for gateway + catalog + order + eureka + db
- Even if some services are stubs, practice service names and ports

---

## 7. Industry use cases

- **Local parity:** Same Compose stack for every developer
- **CI pipelines:** Build image, run tests against Compose Postgres
- **Cloud deploy artifact:** Image pushed to ECR/GHCR then run on ECS/K8s/Beanstalk
- **Sidecars & tooling:** Databases, brokers, collectors as companion containers
- **Reproducible demos:** Pin image tags for workshops and incident reproduction

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Using `localhost` inside a container to reach another service | Use Compose **service name** as hostname |
| Huge build context (copying whole repo) | `.dockerignore`; copy only what’s needed |
| Running as root with no thought | Add non-root user in final stage when practical |
| No memory limit + default JVM heap | Set `-XX:MaxRAMPercentage` or explicit `-Xmx` |
| Storing secrets in the image | Env / Compose env / secrets—never bake passwords into layers |
| Forgetting to publish ports | `-p` / Compose `ports` for host access |
| `depends_on` without health | App may start before Postgres accepts connections |
| Using `latest` everywhere | Pin versions in learning notes for repeatability |
| `compose down -v` wiping learning data by accident | Know when volumes are deleted |
| Single-stage fat JDK image in “prod-like” runs | Prefer multi-stage slim runtime |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Docker Docs — Get started](https://docs.docker.com/get-started/) | Official orientation |
| [Dockerfile reference](https://docs.docker.com/reference/dockerfile/) | Instruction semantics |
| [Compose specification](https://docs.docker.com/compose/compose-file/) | `compose.yaml` fields |
| [Docker Networking](https://docs.docker.com/engine/network/) | Bridge, DNS |
| [Manage data in Docker](https://docs.docker.com/engine/storage/) | Volumes vs bind mounts |
| [Eclipse Temurin images](https://hub.docker.com/_/eclipse-temurin) | Official Java base images |
| [Postgres official image](https://hub.docker.com/_/postgres) | Env vars, volumes |
| [Spring Boot Docker guide tips](https://spring.io/guides/topicals/spring-boot-docker/) | Spring-oriented packaging |
| [Buildpacks / layered jars (optional)](https://docs.spring.io/spring-boot/reference/packaging/container-images/index.html) | Boot container image support |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Docker Docs home: https://docs.docker.com/
- Install Docker Desktop (Windows): https://docs.docker.com/desktop/setup/install/windows-install/
- `docker run` reference: https://docs.docker.com/reference/cli/docker/container/run/
- Dockerfile reference: https://docs.docker.com/reference/dockerfile/
- `.dockerignore`: https://docs.docker.com/build/concepts/context/#dockerignore-files
- Multi-stage builds: https://docs.docker.com/build/building/multi-stage/
- Compose overview: https://docs.docker.com/compose/
- Compose file reference: https://docs.docker.com/compose/compose-file/
- Compose `depends_on`: https://docs.docker.com/compose/compose-file/05-services/#depends_on
- Networking: https://docs.docker.com/engine/network/
- Volumes: https://docs.docker.com/engine/storage/volumes/
- Docker Hub: https://hub.docker.com/
- eclipse-temurin: https://hub.docker.com/_/eclipse-temurin
- postgres: https://hub.docker.com/_/postgres
- Spring Boot container images: https://docs.spring.io/spring-boot/reference/packaging/container-images/index.html
- Spring Boot Docker topical guide: https://spring.io/guides/topicals/spring-boot-docker/

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Install | Engine works | `hello-world` OK |
| 2. Run others’ images | Postgres lifecycle | Can connect and query |
| 3. Build your image | Dockerfile for Boot JAR | Container serves HTTP |
| 4. Persist & network | Volume + user network | Data survives; DNS works |
| 5. Compose | One-file stack | `compose up --build` demo |
| 6. Gate | Mini-project A | Checklist ≥90% |

**Daily habit:** Prefer `docker compose` over long `docker run` flags once Compose is introduced.

---

## 12. Mastery checklist

- [ ] Image vs container mental model explained clearly
- [ ] Can pull, run, log, stop, and remove containers confidently
- [ ] Dockerfile for a Java/Spring Boot service builds successfully
- [ ] Multi-stage (or equivalently slim) image used for runtime
- [ ] `.dockerignore` keeps context reasonable
- [ ] Ports published and understood (`host:container`)
- [ ] Named volume used for Postgres (or similar) persistence
- [ ] User-defined network; services reach each other by name
- [ ] Compose multi-service stack (app + DB at minimum) works
- [ ] Env vars configure Spring datasource/profile without rebuilding for every change
- [ ] Healthcheck / startup ordering understood at a basic level
- [ ] Rebuild/run loop (`compose up --build`) is muscle memory
- [ ] Know difference between `compose down` and `down -v`
- [ ] Mini-project A completed with README
- [ ] Ready for cloud mapping in [17-aws.md](17-aws.md)
