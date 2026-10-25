# Capstone companion — Enterprise Learning Hub build steps

**Spec (source of truth):** [../../capstone/enterprise-platform.md](../../capstone/enterprise-platform.md)  
**Duration:** ~8–10 weeks · 1 hour/day  
**Outcome:** Auth → Catalog → Enrollment → Notification (Kafka) → AI chat/RAG → Compose → AWS path → Kubernetes → demo/harden

Use this file as the **checklist cockpit**. Day-level Learn links and Practice detail live in the capstone spec §4—open that file each day; tick steps here to see week progress at a glance.

---

## Goal

Ship Enterprise Learning Hub end-to-end: microservices behind Gateway, per-service Postgres, Eureka/Config, Feign + Resilience4j, Docker Compose, Kafka enrollment events, AI chatbot + RAG, documented AWS (EC2/RDS/S3) and Kubernetes paths.

---

## Prerequisites

- Phase gates 1–6 complete ([phase-06 phase project](../phase-06-architecture-ai/phase-project/bookstore-hld-rag.md) recommended)
- Docker Desktop; `kubectl` + kind/minikube optional until Week 8
- AWS account with budget alert (Week 7)
- LLM API key in env (Week 5)

---

## Learn

| Resource | When |
|----------|------|
| [enterprise-platform.md](../../capstone/enterprise-platform.md) §4 | Every day |
| Compose / Dockerfile docs | Weeks 1, 6 |
| Kafka + Spring Kafka | Week 4 |
| AWS EC2/RDS/S3 | Week 7 |
| kubectl / Deployments / Ingress | Week 8 |

---

## Project layout

```text
enterprise-learning-hub/
  platform/eureka-server|config-server|gateway/
  services/auth|catalog|enrollment|notification|ai-service/
  infra/k8s/  infra/aws/
  docs/  scripts/  materials/
  docker-compose.yml  .env.example  README.md
```

(Full tree in capstone spec §6.)

---

## Build steps (by week)

### Week 1 — Skeleton, Auth, Gateway (Days 1–7)

- [ ] **Step 1.1 — Monorepo skeleton**
  1. Create folders per capstone layout; root README, `.env.example`, `.gitignore`.
  2. **Done when:** Tree matches spec; no secrets committed.

- [ ] **Step 1.2 — Auth service + DB**
  1. Boot `auth-service` + `postgres-auth` / `auth_db`; User entity; health UP.
  2. **Done when:** `curl localhost:<auth-port>/actuator/health` → UP.

- [ ] **Step 1.3 — Register / login JWT**
  1. BCrypt register; login returns Bearer JWT; bad password → 401.
  2. **Done when:** DB stores hashes only; curls succeed.

- [ ] **Step 1.4 — Eureka + Config**
  1. Eureka `:8761`; Config `:8888`; auth registers as `auth-service`.
  2. **Done when:** Dashboard shows AUTH-SERVICE UP.

- [ ] **Step 1.5 — Gateway → auth**
  1. Gateway `:8080`; route `/api/auth/**` → `lb://auth-service`.
  2. **Done when:** Register/login via `http://localhost:8080` only for demos.

- [ ] **Step 1.6 — Compose edge stack**
  1. Dockerfiles; `docker compose up -d --build postgres-auth eureka auth-service gateway`.
  2. Env: `jdbc:postgresql://postgres-auth:5432/auth_db`, Eureka `http://eureka:8761/eureka`.
  3. **Done when:** Compose stack serves Gateway on 8080.

- [ ] **Step 1.7 — Week 1 demo**
  1. Paste curls into README; obtain JWT through Gateway.
  2. **Done when:** Week 1 exit: login via Gateway.

```bash
docker compose up -d --build postgres-auth eureka auth-service gateway
curl -s -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" \
  -d '{"email":"u1@example.com","password":"Secret123!"}'
curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" \
  -d '{"email":"u1@example.com","password":"Secret123!"}'
```

---

### Week 2 — Catalog (Days 8–14)

- [ ] **Step 2.1 — Catalog service + Course entity**
  1. `catalog-service` + `postgres-catalog` / `catalog_db`; Course fields per Must.
  2. **Done when:** Health UP against its DB.

- [ ] **Step 2.2 — CRUD / list APIs**
  1. List/get; admin create/update; seed courses.
  2. **Done when:** Direct curls work.

- [ ] **Step 2.3 — Eureka + Gateway routes**
  1. Register; `/api/catalog/**` via Gateway; JWT for admin writes.
  2. **Done when:** List courses through Gateway with token.

- [ ] **Step 2.4 — Compose integration**
  1. Add catalog + its Postgres to Compose; DNS hostname `postgres-catalog`.
  2. **Done when:** `docker compose up` includes catalog.

- [ ] **Step 2.5 — Week 2 demo**
  1. Login → `GET /api/catalog/courses` seeded.
  2. **Done when:** Week 2 exit criteria met.

```bash
TOKEN=...   # from login
curl -s http://localhost:8080/api/catalog/courses -H "Authorization: Bearer $TOKEN"
```

---

### Week 3 — Enrollment + Feign + Resilience4j (Days 15–21)

- [ ] **Step 3.1 — Enrollment service + DB**
  1. Own Postgres; enroll entity unique `(userId, courseId)`.
  2. **Done when:** Service starts healthy.

- [ ] **Step 3.2 — Feign → Catalog**
  1. Validate course exists/active before enroll.
  2. **Done when:** Enroll succeeds when catalog up.

- [ ] **Step 3.3 — Resilience4j**
  1. Timeouts + circuit breaker; controlled error when catalog down.
  2. **Done when:** Chaos: `docker compose stop catalog-service` → enroll fails fast.

- [ ] **Step 3.4 — Gateway + list mine**
  1. Routes `/api/enrollments/**`; JWT user ownership.
  2. **Done when:** List my enrollments via Gateway.

- [ ] **Step 3.5 — Week 3 demo**
  1. Enroll happy path + catalog-down path documented.
  2. **Done when:** Week 3 exit criteria met.

```bash
curl -s -X POST http://localhost:8080/api/enrollments -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" -d '{"courseId":"..."}'
docker compose stop catalog-service
# retry enroll → controlled failure
docker compose start catalog-service
```

---

### Week 4 — Kafka + Notification (Days 22–28)

- [ ] **Step 4.1 — Kafka in Compose**
  1. Add Kafka (+ ZooKeeper/KRaft per image); create topic `enrollment.created`.
  2. **Done when:** Broker healthy; topic listed.

```bash
docker compose up -d kafka
docker compose exec kafka kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --if-not-exists --topic enrollment.created --partitions 3 --replication-factor 1
docker compose exec kafka kafka-topics.sh --bootstrap-server localhost:9092 --list
```

- [ ] **Step 4.2 — Publish on enroll**
  1. After successful enroll, produce event (`eventId`, userId, courseId).
  2. Prefer outbox if you completed Kafka mini-project C.
  3. **Done when:** Console consumer sees event after enroll.

- [ ] **Step 4.3 — Notification service**
  1. `@KafkaListener`; persist in-app notification; idempotent on `eventId`.
  2. **Done when:** Duplicate event does not double-insert.

- [ ] **Step 4.4 — List notifications via Gateway**
  1. `GET /api/notifications`; mark-read Should.
  2. **Done when:** Enroll → notification appears for user.

- [ ] **Step 4.5 — Week 4 demo**
  1. Full path register → enroll → notify.
  2. **Done when:** Week 4 exit criteria met.

```bash
curl -s http://localhost:8080/api/notifications -H "Authorization: Bearer $TOKEN"
docker compose logs notification-service --tail 50
```

---

### Week 5 — AI chatbot + Document Q&A (Days 29–35)

- [ ] **Step 5.1 — AI service skeleton**
  1. `ai-service`; env `OPENAI_API_KEY`; JWT; Actuator.
  2. **Done when:** Health UP without requiring model for liveness (document dependency).

- [ ] **Step 5.2 — Chatbot**
  1. `POST /api/ai/chat` session memory last-N.
  2. **Done when:** Two-turn memory demo works via Gateway.

- [ ] **Step 5.3 — Ingest materials**
  1. Seed `materials/`; chunk → embed → vector store.
  2. **Done when:** Ingest endpoint/CLI succeeds.

- [ ] **Step 5.4 — RAG ask + citations**
  1. `POST /api/ai/ask`; sources returned; empty-hit refusal.
  2. **Done when:** In-corpus + out-of-corpus demos pass.

- [ ] **Step 5.5 — Week 5 demo**
  1. Ask + chat through Gateway; filter `courseId` Should.
  2. **Done when:** Week 5 exit criteria met.

```bash
curl -s -X POST http://localhost:8080/api/ai/ingest -H "Authorization: Bearer $TOKEN"
curl -s -X POST http://localhost:8080/api/ai/ask -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"question":"What does module 1 cover?","courseId":"JAVA-101"}'
```

---

### Week 6 — Docker polish + local DoD (Days 36–42)

- [ ] **Step 6.1 — Dockerfiles all services**
  1. Multi-stage images; `.dockerignore`; pin base tags.
  2. **Done when:** Each service builds.

- [ ] **Step 6.2 — Full Compose**
  1. All Postgres × N, Kafka, Eureka, Config, Gateway, five business services.
  2. Healthchecks + `depends_on` conditions; **no localhost** JDBC/Kafka URLs inside containers.
  3. **Done when:** `docker compose up --build -d` brings platform up.

- [ ] **Step 6.3 — Smoke script**
  1. `scripts/smoke.sh` or `.ps1`: register → catalog → enroll → notify → ask.
  2. **Done when:** Script green on cold Compose.

- [ ] **Step 6.4 — README Local path**
  1. Ports table; env; `down` vs `down -v`.
  2. **Done when:** Clean machine can follow Local section only.

- [ ] **Step 6.5 — Week 6 demo**
  1. Full smoke; known limits listed.
  2. **Done when:** Local platform DoD satisfied.

```bash
docker compose up --build -d
docker compose ps
./scripts/smoke.sh   # or pwsh ./scripts/smoke.ps1
docker compose logs gateway --tail 30
```

---

### Week 7 — AWS learning deploy path (Days 43–49)

- [ ] **Step 7.1 — Guardrails**
  1. Budget alert; single region; free-tier-minded sizes.
  2. **Done when:** Alert + region in `infra/aws/NOTES.md`.

- [ ] **Step 7.2 — RDS + S3 + IAM role**
  1. RDS Postgres (SG from EC2 only); S3 materials bucket Block Public Access; EC2 instance role for S3.
  2. **Done when:** No long-lived keys on instance for S3.

- [ ] **Step 7.3 — EC2 host**
  1. Install Docker; copy compose/project; set env for RDS endpoints / S3.
  2. Remap JDBC hosts from Compose DNS to RDS endpoints as documented.
  3. **Done when:** `curl http://<ec2>:8080/actuator/health` (or Gateway) works from your IP.

- [ ] **Step 7.4 — Remote smoke**
  1. Register → enroll → ask against EC2 URL; materials from S3 if wired.
  2. **Done when:** Documented remote smoke test passes.

- [ ] **Step 7.5 — Destroy runbook**
  1. Terminate EC2; delete RDS (lab snapshot policy); empty/delete bucket; confirm billing risk down.
  2. **Done when:** Destroy checklist executed or scheduled same day as demo.

```bash
# Examples — adjust IDs/names
scp -r . ec2-user@<host>:~/enterprise-learning-hub
ssh ec2-user@<host> "cd ~/enterprise-learning-hub && docker compose up -d --build"
curl -s http://<ec2-dns>:8080/api/catalog/courses -H "Authorization: Bearer $TOKEN"
aws s3 ls s3://learning-hub-materials-<unique>/
# Destroy when done — see infra/aws/destroy-checklist.md
```

---

### Week 8 — Kubernetes (Days 50–56)

- [ ] **Step 8.1 — Local cluster**
  1. `kind create cluster --name hub` (or minikube); `kubectl get nodes`.
  2. **Done when:** Node Ready.

- [ ] **Step 8.2 — Images + core manifests**
  1. Build/load images; Deployments + Services + ConfigMaps/Secrets for Gateway + Auth + Catalog (minimum), expand toward full core path.
  2. Probes on `/actuator/health`.
  3. **Done when:** Pods Ready; Service DNS works.

- [ ] **Step 8.3 — Ingress or port-forward**
  1. Document access; Ingress controller if used.
  2. **Done when:** Login + list courses via documented URL.

- [ ] **Step 8.4 — Expand services**
  1. Enrollment, notification, Kafka notes (Kafka on K8s may be Compose-hybrid for learning—document honesty).
  2. **Done when:** Core path works or limits clearly stated.

- [ ] **Step 8.5 — Week 8 demo**
  1. `kubectl get pods,svc,ingress -A`; curl happy path.
  2. **Done when:** K8s path exit criteria met.

```bash
kind create cluster --name hub
docker build -t auth-service:1.0.0 ./services/auth-service
kind load docker-image auth-service:1.0.0 --name hub
kubectl apply -f infra/k8s/
kubectl get pods -n learning-hub
kubectl -n learning-hub port-forward svc/gateway 8080:80
curl -s http://localhost:8080/api/catalog/courses -H "Authorization: Bearer $TOKEN"
```

---

### Weeks 9–10 — Harden, demo, mastery (Days 57–70)

- [ ] **Step 9.1 — Idempotency & timeouts audit**
  1. Kafka consumers idempotent; Feign timeouts everywhere; correlation id notes.
  2. **Done when:** Checklist in `docs/hardening.md`.

- [ ] **Step 9.2 — Architecture sync**
  1. Mermaid in docs matches running Compose.
  2. **Done when:** No phantom services in diagram.

- [ ] **Step 9.3 — Clean-clone rebuild**
  1. Fresh checkout; follow README only; time to first successful enroll.
  2. **Done when:** Timed result recorded.

- [ ] **Step 9.4 — Full demo rehearsal**
  1. Capstone §6 demo script (~10 min) including circuit + AI + optional kubectl.
  2. **Done when:** Rehearsed twice without notes panic.

- [ ] **Step 9.5 — DoD sign-off**
  1. Tick capstone §12 / Definition of Done; known limits honest; next-polish ≤5 items (no scope explosion).
  2. **Done when:** Capstone marked complete in progress tracker.

```text
Demo spine:
0. docker compose up -d
1. register + login → JWT
2. GET courses
3. POST enrollment
4. GET notifications
5. POST ai/ask + ai/chat
6. stop catalog; enroll → controlled failure
7. health probes
8. optional kubectl get pods
```

---

## Definition of Done (Capstone)

Mirror [enterprise-platform.md](../../capstone/enterprise-platform.md) short DoD:

- [ ] Happy path: register → browse → enroll → notify → ask docs
- [ ] Compose works from clean env
- [ ] K8s manifests apply for core path
- [ ] AWS path documented (EC2 + RDS + S3) with destroy
- [ ] Architecture doc matches reality
- [ ] Demo script rehearsed; known limits honest

---

## Demo

See Week 9–10 spine above and capstone §6 demo script. Prefer Gateway as single base URL for all curls.

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Shared DB across services | Separate Postgres per service |
| Business logic in Gateway | Routes + JWT edge only |
| Non-idempotent Kafka consumers | Dedupe `eventId` |
| Secrets in images/git | `.env.example` only |
| K8s before Compose stable | Week 6 before Week 8 |
| Leaving AWS resources on | Destroy checklist same day |
| Scope explosion | Must features only |

---

## After this

- Celebrate; archive/rotate learning secrets if sharing the repo
- Optional polish from the ≤5 item list—**outside** the gated path
- Revisit weak phase mini-projects only if you choose deeper practice
