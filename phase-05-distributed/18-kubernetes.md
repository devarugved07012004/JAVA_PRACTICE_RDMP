# 18 — Kubernetes

**Duration:** ~3–4 weeks (21–28 days) · **Cadence:** 1 hour/day  
**Focus:** Pods, ReplicaSets, Deployments, Services, ConfigMaps, Secrets, Ingress, Helm, Monitoring · Deploy a Spring Boot application

---

## 1. Why this topic is important

Docker runs containers; **Kubernetes** runs fleets: desired state, restarts, scaling, service discovery inside the cluster, config injection, and external access patterns. For Spring Boot APIs, you will package the same image from topic 16 and declare how many replicas, how traffic reaches them, and how config/secrets are mounted.

Local clusters (**kind** or **minikube**) keep this free and practical. You will learn Pods → ReplicaSets → Deployments, Services, ConfigMaps/Secrets, Ingress, a taste of **Helm**, and basic **monitoring**—enough to deploy and reason about a Spring Boot app without drowning in every CRD in the ecosystem.

---

## 2. Prerequisites

- [16-docker.md](16-docker.md) mastery: image build, Compose comfort
- Spring Boot container image that runs locally
- kubectl mindset: YAML as desired state
- Optional: topic 15 multi-service awareness (deploy one service first, then more)

**Environment you will set up:**

- Docker Desktop with Kubernetes enabled **or** minikube **or** kind
- `kubectl` on PATH; `kubectl cluster-info` works
- Local image loading strategy (kind load / minikube image load / Docker Desktop shared images)
- Optional: Helm 3 CLI; metrics-server or kube-prometheus-stack later in the topic

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Why Kubernetes | Desired state, self-healing, portable scheduling |
| Cluster components (sketch) | API server, scheduler, kubelet, etcd (awareness) |
| Namespace | Isolation for labs (`bookstore-dev`) |
| Pod | Smallest deployable; one or more containers; ephemeral |
| Labels & selectors | How Services find Pods |
| kubectl essentials | `apply`, `get`, `describe`, `logs`, `delete` |
| YAML mental model | `apiVersion`, `kind`, `metadata`, `spec` |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| ReplicaSet | Maintains pod count (usually via Deployment) |
| Deployment | Rolling updates; rollback; declarative replicas |
| Service | ClusterIP / NodePort / LoadBalancer; stable DNS name |
| Probes | Liveness vs readiness (Spring Actuator health) |
| ConfigMap | Non-secret config as env or files |
| Secrets | Base64-encoded sensitive config; still protect RBAC |
| Resource requests/limits | CPU/memory; JVM heap awareness |
| Ingress | HTTP routing into cluster (ingress controller required) |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| Helm charts | Templates, values, releases for repeatable installs |
| Monitoring intro | Metrics-server; optional Prometheus/Grafana awareness |
| Multi-service deploy | Catalog + order + Postgres (or external DB) |
| ImagePullPolicy & local images | `IfNotPresent` / loading images into kind/minikube |
| Rollout strategy | `maxUnavailable` / `maxSurge` intuition |
| Why K8s sits above containers | Scheduler + reconciliation loop vs `docker run` |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Cluster, Pods, Deployments, Services (Days 1–7)

- [ ] **Day 1 — Local cluster**
  - [ ] **Learn:** [Kubernetes docs home](https://kubernetes.io/docs/home/) · [kind quick start](https://kind.sigs.k8s.io/docs/user/quick-start/) · [minikube start](https://minikube.sigs.k8s.io/docs/start/)
  - [ ] **Practice:** Install kubectl and a local cluster
    1. Install `kubectl` and verify with `kubectl version --client`.
    2. Choose one: kind, minikube, or Docker Desktop Kubernetes; enable/start the cluster.
    3. For kind example: `kind create cluster --name learn` (or follow minikube/Desktop docs).
    4. Run `kubectl cluster-info` and `kubectl get nodes`.
    5. Note cluster type and context name (`kubectl config current-context`) in notes.
    6. **Done when:** `kubectl cluster-info` succeeds and at least one node is Ready.

- [ ] **Day 2 — First Pod**
  - [ ] **Learn:** [Pods](https://kubernetes.io/docs/concepts/workloads/pods/) · [kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)
  - [ ] **Practice:** Run, inspect, and delete an nginx Pod
    1. Run `kubectl run nginx --image=nginx:alpine --restart=Never`.
    2. Watch status: `kubectl get pods -w` until Running.
    3. Inspect: `kubectl describe pod nginx` and `kubectl logs nginx`.
    4. Optional: `kubectl exec -it nginx -- curl -s localhost` (or wget).
    5. Delete: `kubectl delete pod nginx` and confirm it is gone.
    6. **Done when:** You have describe/logs output and the Pod was deleted cleanly.

- [ ] **Day 3 — Deployment & scale**
  - [ ] **Learn:** [Deployments](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/) · [ReplicaSet](https://kubernetes.io/docs/concepts/workloads/controllers/replicaset/)
  - [ ] **Practice:** Deploy nginx and scale 1→3
    1. Write `k8s/nginx-deploy.yaml` with a Deployment (`replicas: 1`, label `app: nginx`).
    2. Apply: `kubectl apply -f k8s/nginx-deploy.yaml`.
    3. Scale: `kubectl scale deployment/nginx --replicas=3` (match your name).
    4. Run `kubectl get rs,pods -l app=nginx` and note ReplicaSet owns the Pods.
    5. Scale back to 1; observe Pods terminating.
    6. **Done when:** You scaled to 3 Pods and saw the ReplicaSet update.

- [ ] **Day 4 — ClusterIP Service**
  - [ ] **Learn:** [Services](https://kubernetes.io/docs/concepts/services-networking/service/)
  - [ ] **Practice:** Expose nginx with ClusterIP and port-forward
    1. Create a ClusterIP Service selecting `app: nginx`, port 80.
    2. Apply and run `kubectl get svc` — note CLUSTER-IP (not reachable from host directly).
    3. Port-forward: `kubectl port-forward svc/nginx 8080:80`.
    4. Curl `http://localhost:8080` and confirm nginx page.
    5. Stop port-forward; note why ClusterIP alone is not enough from your laptop.
    6. **Done when:** Port-forward returns nginx HTML via the Service.

- [ ] **Day 5 — Selectors & access**
  - [ ] **Learn:** [Services](https://kubernetes.io/docs/concepts/services-networking/service/) (selectors) · [Learn Kubernetes Basics](https://kubernetes.io/docs/tutorials/kubernetes-basics/)
  - [ ] **Practice:** Break and fix Service selectors; try NodePort or port-forward
    1. Change the Service selector to a wrong label; apply.
    2. Run `kubectl get endpoints` (or `kubectl describe svc`) — endpoints empty.
    3. Fix the selector; confirm endpoints list Pod IPs.
    4. Optionally convert to NodePort and hit via node IP:NodePort, **or** stick to port-forward demo.
    5. Write 3 sentences on label selectors matching Pods.
    6. **Done when:** Empty endpoints diagnosed and fixed; access works again.

- [ ] **Day 6 — Spring Boot on cluster**
  - [ ] **Learn:** [Spring Boot cloud deployment](https://docs.spring.io/spring-boot/reference/deployment/cloud.html) · [Deployments](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
  - [ ] **Practice:** Load a Spring Boot image and deploy it
    1. Build image locally: `docker build -t bookstore-api:k8s .`
    2. Load into cluster: kind `kind load docker-image bookstore-api:k8s --name learn` (or minikube `minikube image load ...` / Desktop uses local images).
    3. Write Deployment + ClusterIP Service YAML for the API (containerPort 8080, `imagePullPolicy: IfNotPresent` if local).
    4. Apply; wait `kubectl rollout status deployment/bookstore-api`.
    5. `kubectl port-forward svc/bookstore-api 8080:8080` and curl `/actuator/health` or a GET.
    6. **Done when:** Health/endpoint succeeds against the in-cluster Service via port-forward.

- [ ] **Day 7 — Workload review**
  - [ ] **Learn:** [Concepts overview](https://kubernetes.io/docs/concepts/)
  - [ ] **Practice:** Explain Pod vs Deployment vs Service
    1. In notes, define Pod, Deployment, Service in your own words (2–3 sentences each).
    2. Draw arrows: Deployment → ReplicaSet → Pods; Service → Pods via labels.
    3. Answer: what happens if you delete a Pod managed by a Deployment?
    4. Answer: what does a Service give you that a Pod IP does not?
    5. Explain aloud without notes for 60 seconds.
    6. **Done when:** Notes + diagram exist and you can explain the three objects cleanly.

### Week 2 — Config, Secrets, Probes, Ingress (Days 8–14)

- [ ] **Day 8 — ConfigMaps**
  - [ ] **Learn:** [ConfigMaps](https://kubernetes.io/docs/concepts/configuration/configmap/)
  - [ ] **Practice:** Inject non-secret config as env
    1. Create ConfigMap: `kubectl create configmap app-config --from-literal=SPRING_PROFILES_ACTIVE=k8s --from-literal=APP_MESSAGE=hello-k8s`.
    2. Patch Deployment to `envFrom` / `env.valueFrom.configMapKeyRef` for those keys.
    3. Apply and restart/rollout the Deployment.
    4. `kubectl exec` into the Pod and `printenv | findstr SPRING` (or `grep`).
    5. Confirm app behavior reflects the profile/message (log or endpoint).
    6. **Done when:** Pod env shows ConfigMap values and the app uses them.

- [ ] **Day 9 — Secrets**
  - [ ] **Learn:** [Secrets](https://kubernetes.io/docs/concepts/configuration/secret/)
  - [ ] **Practice:** Wire a DB password Secret and note sensitivity
    1. Create Secret: `kubectl create secret generic db-secret --from-literal=POSTGRES_PASSWORD=learn-pass`.
    2. Mount as env on the Deployment (`secretKeyRef`).
    3. Apply; verify env exists in Pod (`printenv POSTGRES_PASSWORD`) — then avoid pasting secrets into git notes.
    4. Run `kubectl get secret db-secret -o yaml` and note base64 (not encryption).
    5. Write 3 sentences on etcd/RBAC why Secrets need access control.
    6. **Done when:** App/Pod receives the Secret as env and notes cover base64 ≠ encryption.

- [ ] **Day 10 — Probes**
  - [ ] **Learn:** [Liveness, readiness, startup probes](https://kubernetes.io/docs/concepts/configuration/liveness-readiness-startup-probes/) · [Actuator health](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  - [ ] **Practice:** Add readiness and liveness probes
    1. Ensure Actuator `/actuator/health` (or `/health`) is exposed in the image.
    2. Add `readinessProbe` and `livenessProbe` httpGet on that path (sensible `initialDelaySeconds`).
    3. Apply; `kubectl describe pod` and confirm probes are configured.
    4. Temporarily point readiness at a wrong path; observe Pod not Ready / endpoints empty.
    5. Fix the path; confirm Ready again.
    6. **Done when:** Correct probes keep Pod Ready; wrong path demonstrated and fixed.

- [ ] **Day 11 — Resources & JVM**
  - [ ] **Learn:** [Manage resources for containers](https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/)
  - [ ] **Practice:** Set requests/limits and JVM flags
    1. Add `resources.requests/limits` for cpu/memory on the Spring container (e.g. 256Mi–512Mi).
    2. Set `JAVA_TOOL_OPTIONS=-XX:MaxRAMPercentage=75.0` (or `-Xmx`) via env.
    3. Apply and confirm Pod schedules (`kubectl describe pod` events if Pending).
    4. Note what happens if requests exceed node capacity (Pending).
    5. Write why limits matter for noisy neighbors / OOMKilled.
    6. **Done when:** Resources + JVM env are on the live Deployment and documented.

- [ ] **Day 12 — Ingress controller**
  - [ ] **Learn:** [Ingress controllers](https://kubernetes.io/docs/concepts/services-networking/ingress-controllers/) · [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/)
  - [ ] **Practice:** Install ingress-nginx for your local cluster
    1. Follow kind/minikube/Desktop docs to install ingress-nginx (manifest or Helm).
    2. Wait: `kubectl get pods -n ingress-nginx` until Running.
    3. Note how traffic reaches the controller on your setup (localhost, NodePort, etc.).
    4. Run `kubectl get ingressclass` if available.
    5. Record install commands in `k8s/README.md`.
    6. **Done when:** Ingress controller Pods are Running and install steps are saved.

- [ ] **Day 13 — Ingress routes**
  - [ ] **Learn:** [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/)
  - [ ] **Practice:** Route host/path to the Spring Service
    1. Write an Ingress resource: path `/` or `/api` → `bookstore-api` Service port 8080.
    2. Apply; `kubectl get ingress` and describe for address/rules.
    3. Test via localhost mapping / curl with Host header as required by your controller.
    4. Confirm response from the Spring app (not default backend only).
    5. Fix path rewrite issues if the app expects a prefix.
    6. **Done when:** Curl through Ingress hits your Spring Service successfully.

- [ ] **Day 14 — Rolling update**
  - [ ] **Learn:** [Deployments (rolling update)](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
  - [ ] **Practice:** Roll out a new image tag and undo
    1. Build/tag a new image `bookstore-api:k8s-v2` (tiny visible change, e.g. banner/log).
    2. Load into cluster; set Deployment image to `k8s-v2`.
    3. Run `kubectl rollout status deployment/bookstore-api`.
    4. Verify new behavior; then `kubectl rollout undo deployment/bookstore-api`.
    5. Confirm previous revision restored (`kubectl rollout history deployment/bookstore-api`).
    6. **Done when:** Status succeeded for both new rollout and undo.

### Week 3 — Helm, data, monitoring, Spring depth (Days 15–21)

- [ ] **Day 15 — Helm install**
  - [ ] **Learn:** [Helm quickstart](https://helm.sh/docs/intro/quickstart/) · [Helm docs](https://helm.sh/docs/)
  - [ ] **Practice:** Install Helm and a learning chart
    1. Install Helm; verify `helm version`.
    2. `helm repo add bitnami https://charts.bitnami.com/bitnami` (or another safe learning repo) && `helm repo update`.
    3. Install a tiny chart into a throwaway namespace (e.g. `nginx` chart) with a release name.
    4. `helm list -n <ns>` and `kubectl get all -n <ns>`.
    5. Uninstall the release when done learning: `helm uninstall ...`.
    6. **Done when:** You installed and listed a Helm release successfully.

- [ ] **Day 16 — App Helm chart**
  - [ ] **Learn:** [Helm charts guide](https://helm.sh/docs/topics/charts/)
  - [ ] **Practice:** Minimal chart for your Spring app
    1. `helm create bookstore-api` (or hand-write Chart.yaml + templates).
    2. Trim templates to Deployment + Service; set image repository/tag in `values.yaml`.
    3. `helm template` to preview YAML; fix label selectors.
    4. `helm upgrade --install bookstore ./bookstore-api -n learn --create-namespace`.
    5. Port-forward/curl health.
    6. **Done when:** Helm install serves your Spring app from values-driven image tags.

- [ ] **Day 17 — Postgres in-cluster**
  - [ ] **Learn:** [Helm](https://helm.sh/docs/) · [Persistent volumes (skim)](https://kubernetes.io/docs/concepts/storage/persistent-volumes/)
  - [ ] **Practice:** Deploy Postgres (or document external DB carefully)
    1. Install Postgres via Bitnami/Helm **or** apply a simple Postgres Deployment+PVC+Service YAML for learning.
    2. Create Service DNS name (e.g. `postgres`) in the namespace.
    3. Store password in a Secret; do not commit it.
    4. From a throwaway Pod, `psql` or `pg_isready` against the Service.
    5. If using external DB instead: document connection + why PVCs still matter for in-cluster.
    6. **Done when:** Postgres is reachable in-cluster by Service DNS (or external path fully documented).

- [ ] **Day 18 — Spring + DB path**
  - [ ] **Learn:** [DNS for Services and Pods](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/)
  - [ ] **Practice:** Connect Spring to DB inside a namespace
    1. Set JDBC URL to `jdbc:postgresql://postgres:5432/<db>` (match Service name).
    2. Wire user/password from Secret/ConfigMap.
    3. Apply in namespace `learn` (or your ns); wait for Ready probes.
    4. Run migrations/ddl; seed one row.
    5. Curl a DB-backed API via port-forward/Ingress.
    6. **Done when:** API reads/writes Postgres using in-cluster DNS.

- [ ] **Day 19 — metrics-server**
  - [ ] **Learn:** [metrics-server](https://github.com/kubernetes-sigs/metrics-server)
  - [ ] **Practice:** Install metrics-server and run `kubectl top`
    1. Install metrics-server per kind/minikube notes (often needs insecure TLS flags on kind).
    2. Wait until `kubectl top nodes` works (may take a minute).
    3. Run `kubectl top pods -n learn`.
    4. Note CPU/memory for your Spring Pod in notes.
    5. If top fails, capture the error and fix (common kind metrics-server patch).
    6. **Done when:** `kubectl top pods` shows your app Pod metrics.

- [ ] **Day 20 — Monitoring awareness**
  - [ ] **Learn:** [Kubernetes monitoring overview (docs concepts)](https://kubernetes.io/docs/tasks/debug/)
  - [ ] **Practice:** Skim Prometheus/Grafana path and take notes
    1. Skim kube-prometheus-stack docs **or** open one free Grafana dashboard screenshot/docs page.
    2. List 5 signals you’d watch (restarts, Ready replicas, 5xx, lag, CPU throttling).
    3. Map each to a kubectl command or Actuator endpoint you already have.
    4. Optional: install a minimal metrics stack only if time allows (don’t block the week).
    5. Write a half-page “what I’d alert on” note.
    6. **Done when:** Alerting notes exist with ≥5 concrete signals.

- [ ] **Day 21 — Self-healing demo**
  - [ ] **Learn:** [Deployments](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
  - [ ] **Practice:** Delete a Pod and watch recreation
    1. `kubectl get pods -l app=bookstore-api` and note the Pod name.
    2. `kubectl delete pod <name>`.
    3. Watch `kubectl get pods -w` until a new Pod is Running.
    4. Confirm Deployment replica count unchanged.
    5. Document self-healing in notes (controller reconciles desired state).
    6. **Done when:** Deleted Pod was recreated automatically by the Deployment.

### Week 4 — Multi-service & mastery (Days 22–28)

- [ ] **Day 22 — Second service + DNS**
  - [ ] **Learn:** [DNS for Services and Pods](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/)
  - [ ] **Practice:** Call catalog from order via Service DNS
    1. Deploy a second Spring service (e.g. `catalog`) with Service name `catalog`.
    2. From an `order` Pod: `kubectl exec -it <order-pod> -- wget -qO- http://catalog:8080/actuator/health` (or curl).
    3. Point order app config at `http://catalog:8080` for Feign/WebClient.
    4. Hit an order endpoint that calls catalog; confirm success in logs.
    5. Break the DNS name once; read failure; fix.
    6. **Done when:** In-cluster HTTP call `http://catalog:8080` works from the order Pod.

- [ ] **Day 23 — Shared Ingress paths**
  - [ ] **Learn:** [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/)
  - [ ] **Practice:** Path-route `/api/catalog` and `/api/orders`
    1. Update Ingress rules: `/api/catalog` → catalog Service; `/api/orders` → order Service (gateway optional).
    2. Apply and test both paths via Ingress/localhost.
    3. Fix strip-prefix / path rewrite so controllers match.
    4. Document the routing table in `k8s/README.md`.
    5. Confirm 404 on an unmatched path behaves as expected.
    6. **Done when:** Both `/api/catalog` and `/api/orders` reach the correct services.

- [ ] **Day 24 — Helm values per env**
  - [ ] **Learn:** [Helm values files](https://helm.sh/docs/chart_template_guide/values_files/)
  - [ ] **Practice:** Use `values-dev.yaml` for image tags
    1. Create `values-dev.yaml` with `image.tag: k8s-v2` (or `dev`).
    2. `helm upgrade --install ... -f values-dev.yaml`.
    3. Confirm running Pods use the expected tag (`kubectl describe pod` image).
    4. Note how prod would use a different values file (don’t need a real prod cluster).
    5. Commit chart + values examples **without** secrets.
    6. **Done when:** Dev values file controls the deployed image tag.

- [ ] **Day 25 — Alerting notes**
  - [ ] **Learn:** Recap [Probes](https://kubernetes.io/docs/concepts/configuration/liveness-readiness-startup-probes/)
  - [ ] **Practice:** Write concrete alert ideas for your stack
    1. List alerts: Pod restart loops, Ready replicas < desired, Ingress 5xx, probe failures.
    2. For each, write the kubectl/Actuator check you’d use while learning.
    3. Tie readiness failures to “Service stops sending traffic.”
    4. Add one app-level alert idea (place-order error rate).
    5. Keep it to one page in `NOTES-monitoring.md`.
    6. **Done when:** Monitoring notes cover restarts, ready replicas, and 5xx at minimum.

- [ ] **Day 26 — k8s/ layout**
  - [ ] **Learn:** [kubectl apply](https://kubernetes.io/docs/reference/kubectl/)
  - [ ] **Practice:** Organize manifests and document apply order
    1. Arrange `k8s/` (namespace, config, secrets stubs, deploys, services, ingress, helm/).
    2. Write README apply order: ns → secrets/config → db → apps → ingress.
    3. Prefer `kubectl apply -f k8s/` or a small script with ordered files.
    4. Remove dead/duplicate YAML.
    5. Dry-run once: `kubectl apply --dry-run=client -f k8s/`.
    6. **Done when:** Clean `k8s/` layout + README apply order exists.

- [ ] **Day 27 — Tear down & recreate**
  - [ ] **Learn:** [Namespaces](https://kubernetes.io/docs/concepts/overview/working-with-objects/namespaces/)
  - [ ] **Practice:** Delete namespace and recreate from docs/commands
    1. `kubectl delete namespace learn` (or your ns) and wait until gone.
    2. Recreate using only README/script commands.
    3. Re-apply secrets (from password manager — not from git).
    4. Smoke-test health via port-forward/Ingress.
    5. Note any missing steps and fix the README.
    6. **Done when:** Full recreate from docs works on a wiped namespace.

- [ ] **Day 28 — Mastery gate**
  - [ ] **Learn:** [Kubernetes concepts](https://kubernetes.io/docs/concepts/) · §11 Compose↔K8s table
  - [ ] **Practice:** Checklist and Compose ↔ K8s mapping
    1. Tick mastery checklist items you can demonstrate.
    2. Fill Compose↔K8s table: service→Deployment+Service, env_file→ConfigMap/Secret, etc.
    3. Rewrite one fuzzy note from Week 1 in clearer language.
    4. List gaps deferred to Capstone (multi-service Helm, prod ingress TLS).
    5. Keep a “commands I actually use” cheatsheet (10 lines).
    6. **Done when:** Checklist ≥ your honest threshold and mapping table is complete.

*On a 3-week pace: merge Week 4 into Days 19–21 (one Spring deploy + ConfigMap/Secret + Ingress; Helm chart can be minimal).*

---

## 5. Hands-on exercises

1. `kubectl apply -f` twice (idempotent desired state).
2. Typo in selector labels so Service matches nothing; diagnose endpoints empty.
3. Crash loop: bad command in container; read `describe` events.
4. Scale Deployment to 0 and back to 2.
5. ConfigMap update: does the app see it live? (Document restart requirement for env vars.)
6. Secret decoded with `kubectl get secret -o yaml`—discuss why RBAC matters.
7. Probe failure: point readiness at wrong path; see Service stop sending traffic.
8. `kubectl rollout undo` after a bad image.
9. Exec into pod: `kubectl exec -it ... -- sh` (if image has shell); curl another service DNS.
10. Helm: `helm upgrade --install` with a values override for replica count.
11. NetworkPolicy awareness read (optional)—know zero-trust direction.
12. Compare: Compose `depends_on` vs K8s readiness—write 5 lines.

---

## 6. Mini-projects

> **Build guides:** [A Spring Boot on K8s](../mini-projects/phase-05-distributed/18-kubernetes/A-springboot-on-k8s.md) · [B Helm Chart API](../mini-projects/phase-05-distributed/18-kubernetes/B-helm-chart-api.md) · [C Two-Service Demo](../mini-projects/phase-05-distributed/18-kubernetes/C-two-service-demo.md) · [Index](../mini-projects/README.md)

### A. Spring Boot on Kubernetes

- Deployment + Service + ConfigMap + Secret
- Probes on Actuator health
- Ingress or documented port-forward access
- README: build image, load image, apply manifests, test curl

### B. Helm Chart for the API

- Chart with templated name, image, replicas, env
- `values.yaml` for local cluster
- Upgrade and rollback once

### C. (Stretch) Two-Service Mesh-less Demo

- `catalog` + `order` Deployments
- Order calls catalog via in-cluster DNS
- Optional Postgres Helm release

---

## 7. Industry use cases

- **Platform standard:** Most large Java shops run containers on Kubernetes or a managed flavor (EKS/GKE/AKS)
- **Horizontal scale:** More replicas under load via Deployment
- **Config separation:** Same image, different ConfigMaps per environment
- **Safe releases:** Rolling updates and instant rollback
- **Edge routing:** Ingress / Gateway API for HTTP apps
- **Observability:** Metrics and restarts as first operational signals

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Treating Pod YAML as the long-term unit | Use Deployments for apps |
| `localhost` between pods | Use Service DNS names |
| No readiness probe | Traffic hits pods that are still starting |
| Secrets in ConfigMaps or Git plaintext | Secrets objects + don’t commit real values |
| Image not in cluster | kind load / minikube cache / local registry workflow |
| `imagePullPolicy: Always` with missing remote image | Match policy to local learning setup |
| Ignoring resource limits | One Java pod can starve the node |
| Ingress without controller | Install ingress-nginx (or similar) first |
| Applying random YAML from the internet | Read every field; start minimal |
| Expecting Compose files to run as-is | Translate to K8s objects deliberately |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Kubernetes Docs — Get started](https://kubernetes.io/docs/home/) | Official home |
| [Learn Kubernetes Basics](https://kubernetes.io/docs/tutorials/kubernetes-basics/) | Interactive concepts |
| [kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/) | Daily commands |
| [Deployments](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/) | Rollouts |
| [Services](https://kubernetes.io/docs/concepts/services-networking/service/) | Networking |
| [ConfigMaps & Secrets](https://kubernetes.io/docs/concepts/configuration/) | Configuration |
| [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/) | HTTP routing |
| [Helm Docs](https://helm.sh/docs/) | Packaging |
| [kind](https://kind.sigs.k8s.io/) / [minikube](https://minikube.sigs.k8s.io/docs/) | Local clusters |
| [Spring Boot on Kubernetes tips](https://docs.spring.io/spring-boot/reference/deployment/cloud.html) | Boot deployment notes |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Kubernetes documentation: https://kubernetes.io/docs/home/
- Concepts overview: https://kubernetes.io/docs/concepts/
- Pods: https://kubernetes.io/docs/concepts/workloads/pods/
- ReplicaSet: https://kubernetes.io/docs/concepts/workloads/controllers/replicaset/
- Deployments: https://kubernetes.io/docs/concepts/workloads/controllers/deployment/
- Services: https://kubernetes.io/docs/concepts/services-networking/service/
- DNS for Services: https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/
- ConfigMaps: https://kubernetes.io/docs/concepts/configuration/configmap/
- Secrets: https://kubernetes.io/docs/concepts/configuration/secret/
- Probes: https://kubernetes.io/docs/concepts/configuration/liveness-readiness-startup-probes/
- Ingress: https://kubernetes.io/docs/concepts/services-networking/ingress/
- Ingress controllers: https://kubernetes.io/docs/concepts/services-networking/ingress-controllers/
- Resource management: https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/
- kubectl reference: https://kubernetes.io/docs/reference/kubectl/
- kind quick start: https://kind.sigs.k8s.io/docs/user/quick-start/
- minikube start: https://minikube.sigs.k8s.io/docs/start/
- Helm getting started: https://helm.sh/docs/intro/quickstart/
- metrics-server: https://github.com/kubernetes-sigs/metrics-server
- Spring Boot deployment: https://docs.spring.io/spring-boot/reference/deployment/index.html

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Cluster | kubectl works | `get nodes` Ready |
| 2. Workloads | Deployment + scale | Pods recreate on delete |
| 3. Network | Service + port-forward | HTTP to Spring OK |
| 4. Config | ConfigMap + Secret | App configured without rebuild |
| 5. Edge | Ingress | Path/host routing works |
| 6. Package | Helm chart | upgrade/rollback once |
| 7. Observe | top/metrics or notes | Restart/self-heal demo |
| 8. Gate | Mini-project A | Checklist ≥90% |

**Object translation cheat sheet:**

| Docker Compose | Kubernetes |
|----------------|------------|
| `service` + `build` | Deployment + image |
| `ports` | Service + Ingress/NodePort |
| `environment` | ConfigMap/Secret → env |
| `volumes` | Volume/PVC (DB stretch) |
| `depends_on` | Readiness probes + retries |
| `replicas` (swarm/compose) | Deployment `replicas` |

---

## 12. Mastery checklist

- [ ] Can explain why Kubernetes sits **above** containers
- [ ] Pod / ReplicaSet / Deployment basics demonstrated with YAML
- [ ] Service routes traffic to Pods via selectors
- [ ] Local cluster deploy of at least one Spring Boot service
- [ ] ConfigMaps used for non-secret configuration
- [ ] Secrets used for sensitive values (with caution noted)
- [ ] Readiness and/or liveness probes configured
- [ ] Rollout mental model: desired vs current; `rollout undo` tried
- [ ] Ingress working **or** clearly documented port-forward fallback after attempting controller install
- [ ] Helm used at least once (install existing chart or your app chart)
- [ ] Monitoring awareness: `kubectl top` or metrics notes captured
- [ ] Can delete a Pod and watch it come back under a Deployment
- [ ] Personal `k8s/` manifests or chart + README apply steps
- [ ] Ready for async messaging in [19-kafka.md](19-kafka.md)
