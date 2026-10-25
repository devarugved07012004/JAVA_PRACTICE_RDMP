# Mini-project B — Helm Chart for the API

**Topic:** [18 Kubernetes](../../../phase-05-distributed/18-kubernetes.md) · **Phase 5**

---

## Goal

Package the Spring Boot API as a Helm chart with templated name, image, replicas, and env; install with local `values.yaml`; perform one **upgrade** and one **rollback**.

---

## Prerequisites

- Mini-project A manifests working
- Helm 3 installed: `helm version`
- Local cluster still available

---

## Learn

| Resource | Why |
|----------|-----|
| [Helm Docs](https://helm.sh/docs/) | Charts, values, releases |
| [helm create](https://helm.sh/docs/helm/helm_create/) | Scaffold |
| [Rollback](https://helm.sh/docs/helm/helm_rollback/) | Recovery |

---

## Project layout

```text
helm-chart-api/
  charts/
    bookstore-api/
      Chart.yaml
      values.yaml
      templates/
        deployment.yaml
        service.yaml
        configmap.yaml
        secret.yaml          # or document external secret
        _helpers.tpl
  values-local.yaml
  README.md
```

---

## Build steps

- [ ] **1. Scaffold chart**
  1. `helm create bookstore-api` then strip unused templates (ingress/hpa if not needed).
  2. **Done when:** `helm lint charts/bookstore-api` passes.

- [ ] **2. Template Deployment/Service**
  1. Parameterize `image.repository`, `image.tag`, `replicaCount`, ports.
  2. Wire probes to Actuator paths from values.
  3. **Done when:** `helm template bookstore charts/bookstore-api -f values-local.yaml` shows expected YAML.

- [ ] **3. values-local.yaml**
  1. Set image to `bookstore-api:1.0.0`, replicas `1`, env for profile.
  2. **Done when:** Values file is the single place you edit for local cluster.

- [ ] **4. Install release**
  1. Load image into kind/minikube if needed.
  2. `helm upgrade --install bookstore charts/bookstore-api -n bookstore --create-namespace -f values-local.yaml`
  3. **Done when:** Pods Ready; port-forward curl works.

- [ ] **5. Upgrade**
  1. Change a visible value (e.g. replicaCount `2` or a ConfigMap banner).
  2. `helm upgrade bookstore charts/bookstore-api -n bookstore -f values-local.yaml`
  3. **Done when:** `helm history bookstore -n bookstore` shows revision 2; change visible.

- [ ] **6. Rollback**
  1. `helm rollback bookstore 1 -n bookstore`
  2. Confirm prior behavior restored.
  3. **Done when:** History shows rollback; NOTES record revision numbers.

- [ ] **7. README**
  1. install / upgrade / rollback / uninstall commands.
  2. **Done when:** Uninstall documented: `helm uninstall bookstore -n bookstore`.

- [ ] **8. Compare to raw kubectl**
  1. Three bullets: what Helm improved for you.
  2. **Done when:** In NOTES.

---

## Definition of Done

- [ ] Chart templates name, image, replicas, env
- [ ] `values.yaml` / `values-local.yaml` for local cluster
- [ ] One upgrade + one rollback demonstrated
- [ ] README complete

---

## Demo

```bash
helm lint charts/bookstore-api
helm upgrade --install bookstore charts/bookstore-api -n bookstore --create-namespace -f values-local.yaml
helm history bookstore -n bookstore
# edit values-local.yaml replicaCount: 2
helm upgrade bookstore charts/bookstore-api -n bookstore -f values-local.yaml
helm rollback bookstore 1 -n bookstore
helm uninstall bookstore -n bookstore
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Committing real secrets in values | Use sealed/external secrets later; lab placeholders |
| Forgetting `--create-namespace` | Pass flag or create ns first |
| Templating without `helm template` dry-run | Always render before apply |
| Chart name ≠ release name confusion | Document both |

---

## After this

- Stretch: [C-two-service-demo.md](./C-two-service-demo.md)
