# Mini-project A — Spring Boot + Postgres Compose Stack

**Topic:** [16 Docker](../../../phase-05-distributed/16-docker.md) · **Phase 5**

---

## Goal

Package a Spring Boot API (BookStore or catalog) in a multi-stage Dockerfile and run it with PostgreSQL via Docker Compose: named volume, bridge network DNS, documented `docker compose up --build`.

---

## Prerequisites

- Docker Desktop (Windows) or Docker Engine + Compose plugin
- A Spring Boot app that uses Postgres (Phase 4 BookStore ideal)
- Topic 16 Dockerfile + Compose basics

---

## Learn

| Resource | Why |
|----------|-----|
| [Dockerfile reference](https://docs.docker.com/reference/dockerfile/) | Multi-stage |
| [Compose specification](https://docs.docker.com/compose/compose-file/) | Services, volumes, networks |
| [Postgres Docker Hub](https://hub.docker.com/_/postgres) | Env vars |
| [Spring Boot Docker guide](https://spring.io/guides/topicals/spring-boot-docker/) | JVM in containers |

---

## Project layout

```text
boot-postgres-compose/
  app/                         # Spring Boot sources or submodule
  Dockerfile                   # multi-stage
  .dockerignore
  compose.yaml
  .env.example                 # POSTGRES_*, SPRING_DATASOURCE_*
  README.md
```

---

## Build steps

- [ ] **1. `.dockerignore`**
  1. Ignore `target/`, `.git/`, `.idea/`, `*.md` except if needed.
  2. **Done when:** Build context is small (no accidental huge copies).

- [ ] **2. Multi-stage Dockerfile**
  1. Stage 1: `eclipse-temurin:21-jdk` (or 17) — build JAR.
  2. Stage 2: `eclipse-temurin:21-jre` — copy JAR; `ENTRYPOINT ["java","-jar","/app.jar"]`.
  3. Optional: non-root user; `-XX:MaxRAMPercentage=75.0`.
  4. **Done when:** `docker build -t bookstore-api:local .` succeeds.

- [ ] **3. Compose Postgres**
  1. Service `postgres`: image `postgres:16`, env `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`.
  2. Volume `pgdata:/var/lib/postgresql/data`.
  3. Healthcheck: `pg_isready -U $POSTGRES_USER`.
  4. **Done when:** `docker compose up postgres -d` and `docker compose exec postgres pg_isready` OK.

- [ ] **4. Compose app**
  1. Service `app`: `build: .`, depends_on postgres with `condition: service_healthy`.
  2. Env: `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/<db>` (hostname = **service name**).
  3. Publish `8080:8080`.
  4. **Done when:** App connects using `postgres` hostname, not `localhost`.

- [ ] **5. Network**
  1. Implicit Compose network is fine; name it `bookstore-net` for clarity.
  2. **Done when:** `docker compose exec app ping -c1 postgres` works (or equivalent DNS check).

- [ ] **6. Bring stack up**
  1. `docker compose up --build -d`
  2. Wait for healthy; `curl http://localhost:8080/actuator/health`
  3. **Done when:** Health UP and one business curl works (e.g. `GET /api/books`).

- [ ] **7. Persist data proof**
  1. Insert a row via API; `docker compose restart app`; data still there.
  2. **Done when:** Volume persists across app restart (not `down -v`).

- [ ] **8. README**
  1. Commands: `up --build`, sample curl, env vars, `down` vs `down -v`.
  2. **Done when:** Clean machine can follow README without your memory.

- [ ] **9. Tear-down awareness**
  1. Run `docker compose down` (keep volume) once; document separately when `-v` is OK.
  2. **Done when:** NOTES warn that `-v` deletes learning data.

---

## Definition of Done

- [ ] Multi-stage Dockerfile builds a runnable image
- [ ] `compose.yaml` has `app` + `postgres` + volume + network
- [ ] App uses Compose DNS (`postgres`), not localhost
- [ ] README: `docker compose up --build`, curls, env
- [ ] Healthcheck / depends_on used best-effort

---

## Demo

```bash
docker compose up --build -d
docker compose ps
curl -s http://localhost:8080/actuator/health
curl -s http://localhost:8080/api/books
docker compose logs app --tail 50
docker compose down          # keep volume
# docker compose down -v   # WARNING: wipes DB volume
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| `localhost` in JDBC URL inside container | Host = Compose service name |
| `depends_on` without health | App races Postgres boot |
| Fat single-stage JDK image only | Multi-stage JRE runtime |
| Secrets baked into image layers | Env / Compose env only |
| Forgetting published ports | `ports: ["8080:8080"]` |

---

## After this

- [B-two-app-network-lab.md](./B-two-app-network-lab.md)
- Stretch: [C-microservices-compose-preview.md](./C-microservices-compose-preview.md)
