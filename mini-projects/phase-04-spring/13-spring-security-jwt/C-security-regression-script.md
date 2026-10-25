# Mini-project C — Security regression script (optional but highly recommended)

**Topic:** [13 — Spring Security & JWT](../../../phase-04-spring/13-spring-security-jwt.md)

---

## Goal

Ordered HTTP calls documenting expected status codes. Re-run after every security config change so `permitAll` accidents and broken filters fail fast.

---

## Prerequisites

- [A — Secure Login System](A-secure-login-system.md) running locally
- [B — Role-Guard Demo](B-role-guard-demo.md) optional but included steps if present
- curl + bash **or** PowerShell; jq optional

---

## Learn links

| Resource | Why |
|----------|-----|
| Topic A/B README curl sections | Baseline happy path |
| [OWASP AuthN cheat sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html) | What to probe |
| Shell scripting basics | Exit non-zero on mismatch |

### start.spring.io

N/A — this deliverable is a script + checklist against an existing app (`com.learn.securelogin` or `com.learn.roleguard`).

---

## Project layout

```text
secure-login/   (or role-guard/)
  scripts/
    security-regression.sh      # or .ps1
    expected-status.md          # human-readable matrix
  README.md                     # link to script + how to run
```

---

## Build steps

- [ ] **1. Freeze the matrix**
  1. Write `expected-status.md` with rows: step, method, path, auth, expected status.
  2. Include at least: public register/login, me, user route, admin route, bad token, no token, wrong role.
  3. **Done when:** Matrix has ≥10 rows.

- [ ] **2. Script scaffolding**
  1. `BASE_URL` env default `http://localhost:8080`.
  2. Helper: `assert_status METHOD URL AUTH_HEADER BODY EXPECTED`.
  3. Fail script with exit code 1 on first mismatch; print expected vs actual.
  4. **Done when:** Deliberately wrong expected status fails the script.

- [ ] **3. Auth setup section**
  1. Register unique user (timestamp suffix) → expect **201** (or **409** then login).
  2. Login → parse `accessToken` (jq or PowerShell `ConvertFrom-Json`).
  3. Login admin (seeded) → `ADMIN_TOKEN`.
  4. **Done when:** Tokens captured for later steps.

- [ ] **4. Negative cases**
  1. No `Authorization` on secured route → **401**.
  2. `Bearer eyJhbGciOiJbad` → **401**.
  3. User token on admin route → **403**.
  4. Wrong password login → **401**.
  5. **Done when:** All negatives assert correctly.

- [ ] **5. Positive cases**
  1. `/api/auth/me` with user token → **200**.
  2. User hello / notes mine → **200**.
  3. Admin stats / notes all → **200** with admin token.
  4. **Done when:** Positives pass on a healthy app.

- [ ] **6. Wire into README**
  1. “After every SecurityConfig change: `./scripts/security-regression.sh`”.
  2. Document required seed users.
  3. **Done when:** README points to script; script is executable / documented for PowerShell.

- [ ] **7. Break-and-fix drill**
  1. Temporarily `permitAll` on `/api/admin/**`; run script → must fail.
  2. Revert; script passes.
  3. **Done when:** You saw the script catch the regression once.

---

## Definition of Done

- [ ] Ordered HTTP script with expected statuses
- [ ] Covers auth success, 401, 403, and at least one admin vs user case
- [ ] Non-zero exit on failure
- [ ] Documented in README; run after config changes
- [ ] One intentional break proved the script catches regressions

---

## Demo script

Example assertions (bash sketch):

```bash
#!/usr/bin/env bash
set -euo pipefail
BASE="${BASE_URL:-http://localhost:8080}"

assert() {
  local got="$1" expect="$2" label="$3"
  if [[ "$got" != "$expect" ]]; then
    echo "FAIL $label: expected $expect got $got" >&2
    exit 1
  fi
  echo "OK   $label ($got)"
}

code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE/api/user/hello")
assert "$code" "401" "user hello without token"

# … register, login, export TOKEN …

code=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer $TOKEN" "$BASE/api/auth/me")
assert "$code" "200" "me with token"

code=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer $TOKEN" "$BASE/api/admin/stats")
assert "$code" "403" "admin with user token"

echo "All security regression checks passed."
```

PowerShell: use `Invoke-WebRequest` and check `.StatusCode` the same way.

| Step | Expected |
|------|----------|
| GET secured, no token | **401** |
| Login bad password | **401** |
| Login good | **200** |
| GET me + Bearer | **200** |
| GET admin + user Bearer | **403** |
| GET admin + admin Bearer | **200** |
| GET secured + garbage Bearer | **401** |

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Script always exits 0 | Assert and `exit 1` |
| Hard-coding one token | Login each run |
| Only happy path | Negatives catch `permitAll` |
| Not re-running after edits | Make it a habit / README step |

---

## After this

Topic 13 mastery checklist ≥90%. Start [14 — JPA & Hibernate](../../../phase-04-spring/14-jpa-hibernate.md) → [Catalog + Orders JPA](../14-jpa-hibernate/A-catalog-orders-jpa.md).
