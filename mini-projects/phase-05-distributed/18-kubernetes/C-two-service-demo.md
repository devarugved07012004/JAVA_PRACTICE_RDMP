# Mini-project C — Two-Service Mesh-less Demo (Stretch)

**Topic:** [18 Kubernetes](../../../phase-05-distributed/18-kubernetes.md) · **Phase 5**

---

## Goal

Deploy `catalog` and `order` as separate Deployments; order calls catalog via **in-cluster DNS** (`http://catalog:80/...`). Optional Postgres via Bitnami Helm chart.

---

## Prerequisites

- Mini-projects A/B
- Two Boot apps (or stubs) with HTTP dependency
- Optional: Helm for Postgres

---

## Learn

| Resource | Why |
|----------|-----|
| [DNS for Services](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/) | `catalog.bookstore.svc.cluster.local` |
| [Bitnami PostgreSQL chart](https://github.com/bitnami/charts/tree/main/bitnami/postgresql) | Optional DB |

---

## Project layout

```text
two-service-demo/
  k8s/
    catalog-deploy.yaml
    catalog-svc.yaml
    order-deploy.yaml
    order-svc.yaml
  README.md
  NOTES.md
```

---

## Build steps

- [ ] **1. Images**
  1. Build `catalog:1.0.0` and `order:1.0.0`; load into kind/minikube.
  2. **Done when:** Both images loaded.

- [ ] **2. Catalog Deployment + Service**
  1. Service name **`catalog`** (short DNS label).
  2. Expose `/books` or stub `/ping`.
  3. **Done when:** From a debug pod, `wget -qO- http://catalog/books` works.

- [ ] **3. Order Deployment**
  1. Env `CATALOG_BASE_URL=http://catalog`.
  2. `POST /orders` calls catalog.
  3. **Done when:** Port-forward order → place order succeeds with catalog up.

- [ ] **4. Prove not localhost**
  1. Temporarily set URL to `http://localhost:8081` → fail inside cluster.
  2. Restore Service DNS; succeed.
  3. **Done when:** NOTES capture both outcomes.

- [ ] **5. (Optional) Postgres Helm**
  1. `helm repo add bitnami https://charts.bitnami.com/bitnami`
  2. `helm install pg bitnami/postgresql -n bookstore --set auth.postgresPassword=lab`
  3. Point catalog JDBC at `pg-postgresql` Service DNS.
  4. **Done when:** Catalog persists a row across pod restart **or** explicitly skipped with note.

- [ ] **6. README**
  1. Apply order, DNS names, cleanup.
  2. **Done when:** Demo curl script included.

- [ ] **7. Cleanup**
  1. Delete workloads; uninstall Helm Postgres if used.
  2. **Done when:** Namespace empty or cluster deleted.

---

## Definition of Done

- [ ] `catalog` + `order` Deployments
- [ ] Order → catalog via in-cluster DNS
- [ ] Optional Postgres Helm noted or done
- [ ] README + NOTES

---

## Demo

```bash
kubectl apply -f k8s/
kubectl -n bookstore get pods,svc
kubectl -n bookstore run curl --rm -it --image=curlimages/curl --restart=Never -- \
  curl -s http://catalog/books
kubectl -n bookstore port-forward svc/order 8082:80
curl -s -X POST http://localhost:8082/orders -H "Content-Type: application/json" -d '{"bookId":1,"quantity":1}'
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Service name typos | Match env URL to Service `metadata.name` |
| Cross-namespace DNS without FQDN | Same namespace or use FQDN |
| Skipping load of second image | Both images must be in cluster |

---

## After this

- Fold patterns into BookStore microservices phase project / Capstone `infra/k8s`
