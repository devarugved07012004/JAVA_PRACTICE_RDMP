# Mini-project A — Secure Login System

**Topic:** [13 — Spring Security & JWT](../../../phase-04-spring/13-spring-security-jwt.md)

---

## Goal

Ship register (recommended) + login issuing JWT, BCrypt passwords, stateless JWT filter, public auth routes vs secured resources, ≥2 roles on meaningful endpoints, JSON 401/403, and README curl flow (login → Bearer → admin vs user).

---

## Prerequisites

- Topic 12 CRUD app (or equivalent REST API) available to secure
- Topic 13 Weeks 1–2 practiced (filter chain, passwords, roles, JWT issue/validate intro)
- JDK 17+, curl

---

## Learn links

| Resource | Why |
|----------|-----|
| [SecurityFilterChain](https://docs.spring.io/spring-security/reference/servlet/configuration/java.html) | HTTP security |
| [Password Storage](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html) | BCrypt |
| [authorizeHttpRequests](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html) | Route rules |
| [Baeldung — JWT with Spring Security](https://www.baeldung.com/spring-security-oauth-jwt) | Patterns (adapt to Boot 3) |
| [RFC 7519 JWT](https://datatracker.ietf.org/doc/html/rfc7519) | Claims semantics |

### start.spring.io settings

| Field | Value |
|-------|--------|
| Group | `com.learn` |
| Artifact | `secure-login` |
| Package name | `com.learn.securelogin` |
| Spring Boot | 3.x · Java 17+ |
| Dependencies | **Spring Web**, **Spring Security**, **Validation**, **Actuator** (optional) |

Add JWT library manually, e.g. `io.jsonwebtoken:jjwt-api` / `jjwt-impl` / `jjwt-jackson` (0.12.x) **or** `com.nimbusds:nimbus-jose-jwt` — pick one and stick to it.

---

## Project layout

```text
com.learn.securelogin/
  SecureLoginApplication.java
  auth/
    AuthController.java         # /api/auth/register, /login, /me
    AuthService.java
    dto/RegisterRequest.java
    dto/LoginRequest.java
    dto/TokenResponse.java
  user/
    AppUser.java                # in-memory or simple store
    UserService.java
  security/
    SecurityConfig.java
    JwtService.java
    JwtAuthFilter.java
    RestAuthEntryPoint.java     # 401 JSON
    RestAccessDeniedHandler.java # 403 JSON
  resource/
    AdminController.java        # /api/admin/**
    UserResourceController.java # /api/user/** or secure books
src/main/resources/
  application.yml               # jwt.secret, jwt.ttl-minutes
README.md
```

---

## Build steps

- [ ] **1. Scaffold + lock default**
  1. Generate app with Web + Security; start → unauthenticated API → **401**.
  2. **Done when:** Default security observed once.

- [ ] **2. PasswordEncoder + in-memory users**
  1. `@Bean PasswordEncoder` → BCrypt.
  2. User store: map username → `{passwordHash, roles}`.
  3. Seed `alice`/`USER` and `admin`/`ADMIN` **or** rely on register only.
  4. **Done when:** Encoded passwords only; plaintext never stored.

- [ ] **3. Register + Login (no JWT filter yet)**
  1. `POST /api/auth/register` body `{"username","password"}` → **201** (or **409** if duplicate).
  2. `POST /api/auth/login` → temporarily return stub; next step adds JWT.
  3. Public permit for `/api/auth/**` in `SecurityFilterChain`; CSRF off for API; `SessionCreationPolicy.STATELESS`.
  4. **Done when:** Register creates BCrypt user; duplicate → **409**.

- [ ] **4. JwtService issue + parse**
  1. Config: `app.jwt.secret` (long random, env-overridable), `app.jwt.ttl-minutes` (e.g. 60).
  2. Issue token with subject=username, claim `roles`, exp.
  3. Login returns **200** `{"accessToken":"…","tokenType":"Bearer"}`.
  4. Bad credentials → **401** JSON.
  5. **Done when:** Login returns verifiable JWT; password never in response.

- [ ] **5. JwtAuthFilter + SecurityContext**
  1. Filter reads `Authorization: Bearer …`; validates; sets `UsernamePasswordAuthenticationToken`.
  2. Register filter before `UsernamePasswordAuthenticationFilter`.
  3. Missing/invalid/expired → **401** JSON (entry point).
  4. **Done when:** Bearer token accesses a secured ping; bad token → **401**.

- [ ] **6. Role-secured endpoints**
  1. `GET /api/user/hello` → authenticated **USER** or **ADMIN** → **200**.
  2. `GET /api/admin/stats` → `hasRole("ADMIN")` → **200** for admin; **403** for alice.
  3. No token → **401**; user token on admin → **403**.
  4. **Done when:** 401 vs 403 demonstrated with curl.

- [ ] **7. `/api/auth/me` (Should)**
  1. Returns username + roles from principal/claims; **no** password/hash/raw secret.
  2. Unauthenticated → **401**.
  3. **Done when:** Me matches logged-in user.

- [ ] **8. README curl script**
  1. Register → login → me → user hello → admin as user (403) → admin as admin (200).
  2. Document env: `APP_JWT_SECRET`, TTL.
  3. **Done when:** Cold run of script matches expected statuses.

---

## Definition of Done

- [ ] Register (recommended) + Login issuing JWT
- [ ] Passwords hashed with `PasswordEncoder`
- [ ] JWT filter + stateless security config
- [ ] Public auth routes; secured resource routes
- [ ] ≥2 roles enforced on meaningful endpoints
- [ ] JSON **401** / **403** responses
- [ ] README with curl examples
- [ ] Should: `/api/auth/me`; configurable secret/TTL
- [ ] Could: method-level `@PreAuthorize` on a service

---

## Demo script

```bash
BASE=http://localhost:8080

curl -i -X POST $BASE/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"alice\",\"password\":\"password123\"}"
# → 201

curl -i -X POST $BASE/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"alice\",\"password\":\"password123\"}"
# → 200 {"accessToken":"…","tokenType":"Bearer"}
# export TOKEN=…

curl -i $BASE/api/auth/me -H "Authorization: Bearer $TOKEN"
# → 200 claims/username/roles

curl -i $BASE/api/user/hello -H "Authorization: Bearer $TOKEN"
# → 200

curl -i $BASE/api/admin/stats -H "Authorization: Bearer $TOKEN"
# → 403

# login as admin (seeded or registered with ADMIN in your design)
curl -i $BASE/api/admin/stats -H "Authorization: Bearer $ADMIN_TOKEN"
# → 200

curl -i $BASE/api/user/hello
# → 401

curl -i $BASE/api/user/hello -H "Authorization: Bearer not-a-jwt"
# → 401
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Plaintext passwords | Always BCrypt |
| `permitAll` on everything | Open only `/api/auth/**` (+ actuator if needed) |
| 403 when not logged in | Missing auth → **401**; authz fail → **403** |
| JWT secret in git | Env var; `.env.example` placeholder only |
| Roles trusted from client body | Roles from server store / signed claims |
| Logging tokens | Redact |

---

## After this

Build [B — Role-Guard Demo](B-role-guard-demo.md). Keep [C — Security regression script](C-security-regression-script.md) next to every config change. Then topic 14 JPA.
