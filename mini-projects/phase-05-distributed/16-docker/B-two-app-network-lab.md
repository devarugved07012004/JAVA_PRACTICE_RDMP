# Mini-project B — Two-App Network Lab

**Topic:** [16 Docker](../../../phase-05-distributed/16-docker.md) · **Phase 5**

---

## Goal

Prove container DNS: App A (API) calls App B (dependency stub) **or** Zipkin by Compose service name—never `localhost`—with a healthcheck so A waits for B.

---

## Prerequisites

- Mini-project A Compose fluency
- Ability to add a second tiny Boot app or use `openzipkin/zipkin`

---

## Learn

| Resource | Why |
|----------|-----|
| [Docker networking](https://docs.docker.com/engine/network/) | Bridge + DNS |
| Compose `healthcheck` + `depends_on` | Startup ordering |
| Zipkin Docker image (optional path) | Real dependency hostname |

---

## Project layout

```text
two-app-network-lab/
  api/                 # App A — calls B
  stub/                # App B — returns fixed JSON  OR use zipkin image
  compose.yaml
  NOTES.md             # proof of DNS (curl from inside api)
  README.md
```

**Path choice:** Pick **stub** *or* **Zipkin**; document which.

---

## Build steps

- [ ] **1. Stub service (or Zipkin)**
  1. Stub: Boot app on `8081` with `GET /dependency/ping` → `{"ok":true}`.
  2. Or Zipkin: image `openzipkin/zipkin`, port `9411`.
  3. **Done when:** Container alone responds on its published port from host.

- [ ] **2. API service**
  1. Config base URL via env: `DEPENDENCY_URL=http://stub:8081` (or `http://zipkin:9411`).
  2. Endpoint `GET /api/probe` that HTTP-calls the dependency and returns status.
  3. **Done when:** Unit/manual test outside Docker still works with localhost override.

- [ ] **3. Compose network**
  1. Both services on same user-defined network.
  2. **No** `network_mode: host` for this lab.
  3. **Done when:** Compose file lists both under one network.

- [ ] **4. Healthcheck on B**
  1. Stub: `curl -f http://localhost:8081/dependency/ping` or Actuator.
  2. Zipkin: appropriate wget/curl against `/health` or UI port.
  3. **Done when:** `docker compose ps` shows healthy for B.

- [ ] **5. depends_on with condition**
  1. API `depends_on: stub: condition: service_healthy`.
  2. **Done when:** API starts after B is healthy (compose logs order).

- [ ] **6. Inside-container proof**
  1. `docker compose exec api sh -c "wget -qO- http://stub:8081/dependency/ping"` (or curl).
  2. Show that `http://localhost:8081` **fails** from inside `api`.
  3. Paste both results into `NOTES.md`.
  4. **Done when:** NOTES prove DNS name works and localhost doesn’t.

- [ ] **7. Host demo**
  1. `curl http://localhost:8080/api/probe` → success JSON.
  2. **Done when:** Host → API → B path works end-to-end.

- [ ] **8. README**
  1. Diagram of names vs ports; common localhost trap called out.
  2. **Done when:** README includes the two proof commands.

---

## Definition of Done

- [ ] Two containers on Compose network
- [ ] HTTP uses **service DNS names**
- [ ] Healthcheck + depends_on best effort
- [ ] NOTES prove localhost fails inside API container
- [ ] README complete

---

## Demo

```bash
docker compose up --build -d
docker compose exec api sh -c "wget -qO- http://stub:8081/dependency/ping || curl -s http://stub:8081/dependency/ping"
docker compose exec api sh -c "wget -qO- http://localhost:8081/dependency/ping" || echo "localhost failed as expected"
curl -s http://localhost:8080/api/probe
docker compose logs api --tail 30
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Hard-coding `localhost` in app config | Env var with Compose hostname |
| Publishing B’s port and thinking that’s how A reaches B | A uses internal DNS; publish is for host only |
| No healthcheck | Race on cold start |
| Different Compose projects/networks | One `compose.yaml` for the lab |

---

## After this

- Stretch: [C-microservices-compose-preview.md](./C-microservices-compose-preview.md)
- Apply DNS lessons to phase-project BookStore microservices Compose
