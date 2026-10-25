# Mini-project B — Role-Guard Demo API

**Topic:** [13 — Spring Security & JWT](../../../phase-04-spring/13-spring-security-jwt.md)

---

## Goal

Small notes/posts API where `USER` CRUD owns their rows and `ADMIN` can list all. Focus: authorization in the service layer plus security annotations — not only HTTP matchers.

---

## Prerequisites

- [A — Secure Login System](A-secure-login-system.md) complete (JWT auth working)
- Comfortable distinguishing 401 vs 403

---

## Learn links

| Resource | Why |
|----------|-----|
| [Method Security](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html) | `@PreAuthorize` |
| [Authorization architecture](https://docs.spring.io/spring-security/reference/servlet/authorization/architecture.html) | AuthZ model |
| [Baeldung — Method Security](https://www.baeldung.com/spring-security-method-security) | Practical patterns |

### start.spring.io settings

Reuse secure-login app **or**:

| Field | Value |
|-------|--------|
| Group | `com.learn` |
| Artifact | `role-guard` |
| Package name | `com.learn.roleguard` |
| Dependencies | **Web**, **Security**, **Validation** + JWT libs as in A |

---

## Project layout

```text
com.learn.roleguard/
  … (auth/security from A, trimmed)
  note/
    Note.java                   # id, title, body, ownerUsername, createdAt
    NoteRequest.java
    NoteResponse.java
    NoteRepository.java         # in-memory Map
    NoteService.java            # ownership checks
    NoteController.java
  security/
    SecurityConfig.java         # enable method security
```

---

## Build steps

- [ ] **1. Extend A or clone packages**
  1. Keep JWT login; seed `alice` (USER), `bob` (USER), `admin` (ADMIN).
  2. Enable `@EnableMethodSecurity`.
  3. **Done when:** Login still returns **200** + token for all three.

- [ ] **2. Note model + in-memory store**
  1. Fields: `id`, `title`, `body`, `ownerUsername`.
  2. Repository CRUD by id + `findByOwner(username)` + `findAll()`.
  3. **Done when:** Store works without HTTP.

- [ ] **3. USER create / list mine**
  1. `POST /api/notes` authenticated → owner = current username → **201**.
  2. `GET /api/notes/mine` → **200** only caller’s notes.
  3. Unauthenticated → **401**.
  4. **Done when:** Alice cannot see Bob’s notes on `/mine`.

- [ ] **4. USER get/update/delete with ownership**
  1. `GET /api/notes/{id}` → **200** if owner or admin; **403** if other user; **404** if missing.
  2. `PUT` / `DELETE` → same ownership rules (**200**/**204** vs **403**).
  3. Enforce in **service** (not only controller ifs); optionally `@PreAuthorize`.
  4. **Done when:** Bob editing Alice’s note → **403**.

- [ ] **5. ADMIN list all**
  1. `GET /api/notes` → `hasRole("ADMIN")` → **200** all notes.
  2. USER calling list-all → **403**.
  3. Prefer `@PreAuthorize("hasRole('ADMIN')")` on service or controller method.
  4. **Done when:** Admin sees everyone’s notes; user blocked.

- [ ] **6. Document matrix in README**
  1. Table: actor × endpoint → status.
  2. **Done when:** Matrix matches real curl results.

---

## Definition of Done

- [ ] Notes resource with owner field
- [ ] USER CRUD limited to owned rows
- [ ] ADMIN can list all
- [ ] Authorization rules in service and/or method security
- [ ] Clear **401** / **403** / **404** behavior
- [ ] README status matrix

---

## Demo script

```bash
# Alice creates
curl -i -X POST http://localhost:8080/api/notes \
  -H "Authorization: Bearer $ALICE" -H "Content-Type: application/json" \
  -d "{\"title\":\"A1\",\"body\":\"secret\"}"
# → 201  (note id=1)

# Bob tries get
curl -i http://localhost:8080/api/notes/1 -H "Authorization: Bearer $BOB"
# → 403

# Alice get
curl -i http://localhost:8080/api/notes/1 -H "Authorization: Bearer $ALICE"
# → 200

# Alice list mine
curl -i http://localhost:8080/api/notes/mine -H "Authorization: Bearer $ALICE"
# → 200 […only alice…]

# Alice list all
curl -i http://localhost:8080/api/notes -H "Authorization: Bearer $ALICE"
# → 403

# Admin list all
curl -i http://localhost:8080/api/notes -H "Authorization: Bearer $ADMIN"
# → 200 [alice + bob notes]

# Bob delete alice note
curl -i -X DELETE http://localhost:8080/api/notes/1 -H "Authorization: Bearer $BOB"
# → 403
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| AuthZ only in frontend | Enforce in service every time |
| Returning **404** to hide others’ notes always | Pick a policy; **403** is fine for authenticated-but-forbidden |
| Admin rules only on HTTP matcher | Add method security / service checks |
| Owner taken from request body | Owner from `SecurityContext` |

---

## After this

Add [C — Security regression script](C-security-regression-script.md) and run it after every security config change. Then [14 — JPA](../../../phase-04-spring/14-jpa-hibernate.md).
