# Mini-project A — Spring Boot on Kubernetes

**Topic:** [18 Kubernetes](../../../phase-05-distributed/18-kubernetes.md) · **Phase 5**

---

## Goal

Run a Spring Boot API on a local cluster (kind / minikube / k3d) with Deployment, Service, ConfigMap, Secret, Actuator probes, and either Ingress or documented `kubectl port-forward`.

---

## Prerequisites

- Docker running
- `kubectl` installed; local cluster (kind recommended on Windows with Docker Desktop)
- Container image of your Boot app (topic 16 Dockerfile)
- Topic 18 core objects

---

## Learn

| Resource | Why |
|----------|-----|
| [Deployments](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/) | Rollouts |
| [Services](https://kubernetes.io/docs/concepts/services-networking/service/) | ClusterIP |
| [ConfigMaps & Secrets](https://kubernetes.io/docs/concepts/configuration/) | Config |
| [Probes](https://kubernetes.io/docs/concepts/configuration/liveness-readiness-startup-probes/) | Actuator |
| [kind](https://kind.sigs.k8s.io/) / [minikube](https://minikube.sigs.k8s.io/docs/) | Local cluster |

---

## Project layout

```text
springboot-on-k8s/
  Dockerfile
  k8s/
    namespace.yaml
    configmap.yaml
    secret.yaml
    deployment.yaml
    service.yaml
    ingress.yaml            # optional
  README.md
```

---

## Build steps

- [ ] **1. Local cluster**
  1. `kind create cluster --name boot-lab` (or `minikube start`).
  2. `kubectl cluster-info`
  3. **Done when:** `kubectl get nodes` shows Ready.

- [ ] **2. Build & load image**
  1. `docker build -t bookstore-api:1.0.0 .`
  2. kind: `kind load docker-image bookstore-api:1.0.0 --name boot-lab`
  3. minikube: `minikube image load bookstore-api:1.0.0`
  4. **Done when:** Image is available inside the cluster (no remote pull required).

- [ ] **3. Namespace + ConfigMap + Secret**
  1. Apply namespace `bookstore`.
  2. ConfigMap: non-secret props (`SPRING_PROFILES_ACTIVE=k8s`).
  3. Secret: DB password / JWT secret (base64 via `kubectl create secret generic`).
  4. **Done when:** `kubectl -n bookstore get cm,secret` lists them.

- [ ] **4. Deployment**
  1. Pod template: image `bookstore-api:1.0.0`, `imagePullPolicy: IfNotPresent`.
  2. EnvFrom ConfigMap/Secret; container port `8080`.
  3. Resources requests/limits (small).
  4. **Done when:** `kubectl -n bookstore get pods` → Running (or CrashLoop with logs to fix).

- [ ] **5. Probes**
  1. Readiness + liveness HTTP GET `/actuator/health` (or `/actuator/health/readiness` if split).
  2. Tune `initialDelaySeconds` for JVM startup.
  3. **Done when:** Pod Ready; killing process causes restart (liveness) without serving during startup (readiness).

- [ ] **6. Service**
  1. ClusterIP Service selecting app pods, port `80` → `8080`.
  2. **Done when:** `kubectl -n bookstore get svc` shows endpoints.

- [ ] **7. Access path**
  1. Option A: `kubectl -n bookstore port-forward svc/bookstore-api 8080:80`
  2. Option B: Ingress + ingress-nginx; document install.
  3. `curl http://localhost:8080/actuator/health`
  4. **Done when:** Health + one API curl succeed via documented path.

- [ ] **8. README**
  1. Build → load → apply → test → delete cluster.
  2. **Done when:** Clean clone/follow works.

- [ ] **9. Cleanup**
  1. `kubectl delete ns bookstore` or `kind delete cluster --name boot-lab`
  2. **Done when:** Cluster/resources removed when lab ends.

---

## Definition of Done

- [ ] Deployment + Service + ConfigMap + Secret
- [ ] Probes on Actuator health
- [ ] Ingress **or** port-forward documented
- [ ] README: build, load, apply, curl

---

## Demo

```bash
kind create cluster --name boot-lab
docker build -t bookstore-api:1.0.0 .
kind load docker-image bookstore-api:1.0.0 --name boot-lab
kubectl apply -f k8s/
kubectl -n bookstore get pods -w
kubectl -n bookstore port-forward svc/bookstore-api 8080:80
curl -s http://localhost:8080/actuator/health
kubectl -n bookstore logs deploy/bookstore-api --tail=50
kind delete cluster --name boot-lab
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Image not in cluster | `kind load` / minikube load |
| `localhost` to other pods | Use Service DNS |
| No readiness probe | Traffic hits cold JVM |
| Secrets in ConfigMap/Git | Secret objects; example only in Git |
| `imagePullPolicy: Always` with local-only tag | `IfNotPresent` for kind labs |

---

## After this

- [B-helm-chart-api.md](./B-helm-chart-api.md)
- Stretch: [C-two-service-demo.md](./C-two-service-demo.md)
