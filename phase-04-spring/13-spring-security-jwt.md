# 13 — Spring Security & JWT

**Duration:** ~3 weeks (21 days) · **Cadence:** 1 hour/day  
**Focus:** Authentication, Authorization, Roles, Permissions, JWT, Filters, Password Encoding, Security Configuration · Build a Secure Login System

---

## 1. Why this topic is important

Almost every real API must answer two questions: **who are you?** (authentication) and **what may you do?** (authorization). Spring Security is the standard way to enforce those answers in Spring Boot: filter chains, password encoding, user details, method security, and HTTP security rules.

JWT (JSON Web Token) is a common pattern for stateless API auth: the server issues a signed token at login; clients send it on later requests; a filter validates signature and claims before the controller runs. Mastering this topic means you can build a **secure login system**, protect routes by role, debug 401 vs 403, and avoid classic mistakes (plaintext passwords, overly permissive `permitAll`, weak secrets).

You will reuse this exact pattern in the BookStore phase project and in every authenticated microservice later.

---

## 2. Prerequisites

- [12-spring-boot.md](12-spring-boot.md) mastery checklist ≥90% (REST, validation, profiles)
- [11-spring-core.md](11-spring-core.md) comfort with filters/beans as container-managed objects
- HTTP headers, cookies vs Authorization header awareness (Phase 3)
- Basic cryptography literacy: hashing ≠ encryption; why salts matter (high level)

**Environment you will set up:**

- Boot app with `spring-boot-starter-security` (+ Web, Validation)
- In-memory or simple user store first; then DB-backed users when combining with JPA
- A JWT library commonly used with Spring (e.g. jjwt) via Maven/Gradle—read its docs; prefer well-maintained versions
- Tooling: curl/HTTPie or IDE HTTP client to send `Authorization: Bearer …`

**Safety habit:** Use strong passwords only in local test data; never commit real secrets; rotate JWT signing keys via env vars.

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Authentication vs authorization | Identity vs permissions; 401 vs 403 |
| Spring Security filter chain | Request passes filters before controllers |
| Default Boot Security | Generated password / login form—why demos lock down suddenly |
| `SecurityFilterChain` bean | Modern component-based config (replace legacy `WebSecurityConfigurerAdapter`) |
| Password encoding | `PasswordEncoder` (`BCryptPasswordEncoder`); never store plaintext |
| `UserDetails` / `UserDetailsService` | How Security loads users and authorities |
| HTTP security rules | `authorizeHttpRequests`, `permitAll`, `authenticated`, `hasRole` |
| CSRF | Critical for browser session forms; often disabled for pure JWT APIs (know why) |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Roles vs authorities | `ROLE_ADMIN` convention; `hasRole` vs `hasAuthority` |
| Method security | `@PreAuthorize`, `@EnableMethodSecurity` |
| JWT structure | Header.payload.signature; claims (`sub`, `exp`, `roles`) |
| Login flow | Authenticate credentials → issue JWT → client stores token |
| JWT filter | Extract Bearer token → validate → set `SecurityContext` |
| Stateless session | `SessionCreationPolicy.STATELESS` for APIs |
| CORS + Security | Preflight and credentialed requests basics |
| Exception handling | AuthenticationEntryPoint / AccessDeniedHandler for JSON APIs |

### Advanced (Security depth)

| Concept | What to internalize |
|--------|----------------------|
| Filter order | Where your JWT filter sits relative to `UsernamePasswordAuthenticationFilter` |
| Refresh tokens (conceptual) | Tradeoffs; optional stretch—access token expiry first |
| Password upgrade / encoding id | Delegating password encoder awareness |
| Principle of least privilege | Default deny; open only what must be public |
| Token invalidation limits | Stateless JWT logout realities; short TTL + rotation mindset |
| Testing security | `@WithMockUser`, mockMvc security tests (smoke level) |
| Hardening | Secret length, HTTPS assumption, not logging tokens |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Security foundations (Days 1–7)

- [ ] **Day 1 — Default Security lock**
  - [ ] **Learn:** [Spring Security Reference](https://docs.spring.io/spring-security/reference/) · [Servlet Architecture](https://docs.spring.io/spring-security/reference/servlet/architecture.html) · [Baeldung — Spring Security intro](https://www.baeldung.com/security-spring)
  - [ ] **Practice:** Add Security starter; observe default 401
    1. On your Boot CRUD (or a fresh Initializr app with **Web** + **Security**): add `spring-boot-starter-security`.
    2. Restart; watch logs for `Using generated security password: …`.
    3. `curl -i http://localhost:8080/api/hello` (or any API) → expect **401** Unauthorized.
    4. `curl -i -u user:<generated-password> http://localhost:8080/api/hello` → expect **200** (default user).
    5. In notes: default form login page exists for browsers; APIs get 401 without creds.
    6. **Done when:** You reproduced 401 without auth and 200 with basic user/password from logs.

- [ ] **Day 2 — `SecurityFilterChain`**
  - [ ] **Learn:** [Java Configuration](https://docs.spring.io/spring-security/reference/servlet/configuration/java.html) · [Authorize HttpRequests](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html)
  - [ ] **Practice:** permitAll public; authenticate the rest
    1. Create `com.learn.security.config.SecurityConfig` with `@Bean SecurityFilterChain`.
    2. `csrf(csrf -> csrf.disable())` for API learning; `authorizeHttpRequests`: `/api/public/**` → `permitAll`, any other → `authenticated`.
    3. Add `@GetMapping("/api/public/ping")` returning `{"ok":true}`.
    4. `curl -i http://localhost:8080/api/public/ping` → **200**; `curl -i http://localhost:8080/api/books` → **401**.
    5. Keep HTTP Basic temporarily so you can still auth with `-u`.
    6. **Done when:** Public ping is 200 without creds; protected route is 401.

- [ ] **Day 3 — Users & password encoding**
  - [ ] **Learn:** [Password Storage](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html) · [UserDetailsService](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/user-details-service.html) · [Baeldung — BCrypt](https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)
  - [ ] **Practice:** In-memory users + BCrypt only
    1. `@Bean PasswordEncoder` → `PasswordEncoderFactories.createDelegatingPasswordEncoder()` or `new BCryptPasswordEncoder()`.
    2. `@Bean UserDetailsService` with `User.withUsername("alice").password(encoder.encode("password")).roles("USER").build()` and `admin` / `ADMIN`.
    3. Never put plaintext in the password field without encoding.
    4. `curl -i -u alice:password http://localhost:8080/api/books` → **200**.
    5. Wrong password → **401**.
    6. **Done when:** Two users work via Basic and passwords are BCrypt-encoded in config.

- [ ] **Day 4 — Form login vs HTTP Basic**
  - [ ] **Learn:** [Form Login](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html) · [HTTP Basic](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html) · [Guide — Securing a Web Application](https://spring.io/guides/gs/securing-web/)
  - [ ] **Practice:** Try both; note API preference for Bearer
    1. Enable `.httpBasic(Customizer.withDefaults())`; confirm curl `-u` works.
    2. Temporarily enable `.formLogin(Customizer.withDefaults())`; open `/login` in a browser.
    3. Note: browser session cookies vs API clients sending JSON.
    4. Disable form login again for API focus; keep Basic until JWT week.
    5. Write 4–6 sentences: why SPAs/mobile prefer `Authorization: Bearer` over form sessions.
    6. **Done when:** Both mechanisms tried once and Bearer preference is written.

- [ ] **Day 5 — Roles on routes**
  - [ ] **Learn:** [Authorization Architecture](https://docs.spring.io/spring-security/reference/servlet/authorization/architecture.html) · [Authorize HttpRequests](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html) · [Baeldung — Granted Authority vs Role](https://www.baeldung.com/spring-security-granted-authority-vs-role)
  - [ ] **Practice:** USER 403 vs ADMIN 200 on admin route
    1. Add `GET /api/admin/stats` returning a stub JSON.
    2. In filter chain: `/api/admin/**` → `hasRole("ADMIN")` (or `hasAuthority("ROLE_ADMIN")`).
    3. `curl -i -u alice:password http://localhost:8080/api/admin/stats` → **403**.
    4. `curl -i -u admin:password http://localhost:8080/api/admin/stats` → **200**.
    5. Note ROLE_ prefix behavior in notes.
    6. **Done when:** USER→403 and ADMIN→200 are proven with curl.

- [ ] **Day 6 — Method security**
  - [ ] **Learn:** [Method Security](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html) · [Baeldung — `@PreAuthorize`](https://www.baeldung.com/spring-security-method-security)
  - [ ] **Practice:** `@PreAuthorize` on a service method
    1. `@EnableMethodSecurity` on a config class.
    2. On a `@Service` method: `@PreAuthorize("hasRole('ADMIN')")`.
    3. Expose via a controller that calls the service (not only HTTP `hasRole`).
    4. Call as alice → **403**; as admin → **200**.
    5. Confirm the denial happens even if the URL were misconfigured later (defense in depth).
    6. **Done when:** Wrong role gets 403 from method security.

- [ ] **Day 7 — JSON 401/403 handlers**
  - [ ] **Learn:** [AuthenticationEntryPoint](https://docs.spring.io/spring-security/reference/servlet/authentication/entrypoint.html) · [Access Denied Handler](https://docs.spring.io/spring-security/reference/servlet/authorization/access-decision.html) · [CSRF](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html)
  - [ ] **Practice:** JSON 401/403 + CSRF decision
    1. Custom `AuthenticationEntryPoint` writing JSON `{"status":401,"error":"Unauthorized"}` with **401**.
    2. Custom `AccessDeniedHandler` writing JSON with **403**.
    3. Wire both in `SecurityFilterChain`.
    4. Curl unauthenticated protected + authenticated USER on admin — both return JSON (not HTML login).
    5. Notes: AuthN vs AuthZ with your own URL examples; CSRF disabled for Bearer APIs (when/why).
    6. **Done when:** 401 and 403 responses are JSON and CSRF stance is written.

### Week 2 — JWT login system (Days 8–14)

- [ ] **Day 8 — JWT concepts & JwtService**
  - [ ] **Learn:** [RFC 7519 — JWT](https://datatracker.ietf.org/doc/html/rfc7519) · [Baeldung — JWT with Spring Security](https://www.baeldung.com/spring-security-oauth-jwt) · [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html) (skim)
  - [ ] **Practice:** jjwt (or similar) create/parse with `sub` + exp
    1. Add JWT library (e.g. `io.jsonwebtoken:jjwt-api` + impl + jackson) matching your Java version.
    2. Create `com.learn.security.jwt.JwtService` with `generateToken(username, roles)` and `parseClaims(token)`.
    3. Secret: long random string in `application.yml` under `app.jwt.secret` (dev only).
    4. TTL: `app.jwt.expiration-ms: 3600000` (1h).
    5. Unit-style main/test: create token, parse, print `sub` and expiration.
    6. **Done when:** Round-trip create/parse works and claims include `sub` + expiry.

- [ ] **Day 9 — Login (and register)**
  - [ ] **Learn:** Revisit [Password Storage](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html) · [Baeldung — Spring Security login](https://www.baeldung.com/spring-security-login)
  - [ ] **Practice:** `/api/auth/login` returns JWT
    1. DTOs: `LoginRequest(username, password)`, `AuthResponse(accessToken, tokenType="Bearer")`.
    2. `POST /api/auth/login` — validate `@NotBlank`; authenticate via `AuthenticationManager` or manual `UserDetails` + encoder match.
    3. On success: return **200** + JWT; never return password hash.
    4. Optional `POST /api/auth/register`: encode password, store in-memory map, return **201**.
    5. `curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"alice\",\"password\":\"password\"}"` → token JSON.
    6. **Done when:** Login returns a JWT string and bad password returns **401**.

- [ ] **Day 10 — JWT filter & stateless session**
  - [ ] **Learn:** [Servlet Architecture / Filter chain](https://docs.spring.io/spring-security/reference/servlet/architecture.html) · [Session Management](https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html) · [Baeldung — JWT filter pattern](https://www.baeldung.com/spring-security-oauth-jwt)
  - [ ] **Practice:** Filter + STATELESS session
    1. Create `JwtAuthFilter extends OncePerRequestFilter`: read `Authorization: Bearer …`, validate, set `SecurityContext`.
    2. Register filter with `http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)`.
    3. `sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))`.
    4. Permit `/api/auth/**` and `/api/public/**`; authenticate others.
    5. Disable default Basic if it conflicts (or leave for debugging — your call; document it).
    6. **Done when:** Valid Bearer sets authentication; app creates no HTTP session for API calls.

- [ ] **Day 11 — Protect CRUD with Bearer**
  - [ ] **Learn:** [Authorize HttpRequests](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html)
  - [ ] **Practice:** CRUD 401 without token; 200 with token
    1. Require authentication for `/api/books/**` (or your CRUD base path).
    2. `curl -i http://localhost:8080/api/books` → **401** JSON.
    3. Login; save token: `TOKEN=…`.
    4. `curl -i http://localhost:8080/api/books -H "Authorization: Bearer $TOKEN"` → **200**.
    5. Malformed header `Authorization: Bearer` alone → **401**.
    6. **Done when:** Missing vs valid Bearer behave as 401 vs 200.

- [ ] **Day 12 — Roles in JWT claims**
  - [ ] **Learn:** [Method Security](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html) · [OWASP Authorization Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html) (skim)
  - [ ] **Practice:** Admin-only from token roles
    1. Put roles claim in JWT (e.g. `roles: ["ROLE_ADMIN"]` or space-separated).
    2. Filter maps claims → `GrantedAuthority` list on `UsernamePasswordAuthenticationToken`.
    3. Protect `DELETE /api/books/{id}` or `/api/admin/**` with `hasRole("ADMIN")`.
    4. Login as alice → delete → **403**; login as admin → delete → **204**.
    5. Confirm roles come from **validated** token, not client-supplied headers.
    6. **Done when:** Admin-only endpoint enforces roles from JWT claims.

- [ ] **Day 13 — Expiry & invalid signature**
  - [ ] **Learn:** [RFC 7519 — exp claim](https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.4) · Re-skim [Baeldung JWT](https://www.baeldung.com/spring-security-oauth-jwt)
  - [ ] **Practice:** Reject expired and tampered tokens
    1. Temporarily set `app.jwt.expiration-ms: 1000`; login; wait 2s; call API → **401**.
    2. Restore normal TTL.
    3. Take a valid token; flip one character in the signature segment; call API → **401**.
    4. Ensure JSON error body (not stack trace to client).
    5. Notes: short TTL mindset; logout of stateless JWT is limited.
    6. **Done when:** Expired and tampered tokens both yield 401 JSON.

- [ ] **Day 14 — CORS + Security**
  - [ ] **Learn:** [CORS](https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html) · [Baeldung — CORS with Spring Security](https://www.baeldung.com/spring-cors)
  - [ ] **Practice:** Allow local frontend origin
    1. In `SecurityFilterChain`, `.cors(Customizer.withDefaults())` and a `CorsConfigurationSource` bean.
    2. Allowed origin: `http://localhost:5173` (or `http://localhost:3000`); methods: GET,POST,PUT,DELETE,OPTIONS; headers: `Authorization`, `Content-Type`.
    3. `curl -i -X OPTIONS http://localhost:8080/api/books -H "Origin: http://localhost:5173" -H "Access-Control-Request-Method: GET"` — check CORS headers.
    4. Document allowed origins/methods/headers in notes even if UI comes later.
    5. Do not use `*` origin together with credentials.
    6. **Done when:** CORS config is wired and documented for one local origin.

### Week 3 — Secure Login System polish (Days 15–21)

- [ ] **Day 15 — Mini-project A vertical slice**
  - [ ] **Learn:** Re-skim [Java Configuration](https://docs.spring.io/spring-security/reference/servlet/configuration/java.html) + [Password Storage](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)
  - [ ] **Practice:** Register + login + `/me` end-to-end
    1. Finish §6 Mini-project A packages: `auth`, `security`, `user` (in-memory OK).
    2. `POST /api/auth/register` → **201**; `POST /api/auth/login` → **200** + token.
    3. `GET /api/auth/me` with Bearer → **200** username + roles (no password/hash).
    4. Without token `/me` → **401**.
    5. Script the three calls in a shell block.
    6. **Done when:** register → login → `/me` works in one scripted flow.

- [ ] **Day 16 — Ownership rules**
  - [ ] **Learn:** [Method Security](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html) · [Authorize HttpRequests](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html)
  - [ ] **Practice:** User edits self; admin edits all
    1. Resource e.g. `Note` or profile update tied to username.
    2. USER can `PUT /api/users/me` only; cannot change another user’s id.
    3. ADMIN can `PUT /api/users/{id}` for any user.
    4. Prove with two accounts: alice cannot edit bob → **403**; admin can → **200**.
    5. Prefer service-layer checks + `@PreAuthorize` where clean.
    6. **Done when:** Cross-user edit denied; admin override works.

- [ ] **Day 17 — Password rules & response hygiene**
  - [ ] **Learn:** [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html) · [Password Storage](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)
  - [ ] **Practice:** Validate passwords; scrub responses
    1. Register DTO: `@Size(min=8)` password; reject weak/blank → **400**.
    2. Grep responses: no `password`, no BCrypt hash fields in JSON.
    3. Login response: only `accessToken` + `tokenType` (and optional expiresIn).
    4. Logging: never log raw passwords or full JWTs.
    5. Duplicate username on register → **409**.
    6. **Done when:** Weak password → 400; responses/logs have no secrets.

- [ ] **Day 18 — Security test script**
  - [ ] **Learn:** [Testing with Spring Security](https://docs.spring.io/spring-security/reference/servlet/test/index.html) · [Baeldung — `@WithMockUser`](https://www.baeldung.com/spring-security-withmockuser)
  - [ ] **Practice:** 200/401/403 coverage
    1. Create `security-test.http` or `scripts/security-smoke.sh` with ordered calls.
    2. Cases: public 200; protected no token 401; user admin-route 403; admin 200; login bad creds 401.
    3. Optional: one `@WebMvcTest` + `@WithMockUser(roles="ADMIN")` asserting 200.
    4. Run script after every security config change this week.
    5. Record expected statuses next to each curl.
    6. **Done when:** Script documents and passes 200/401/403 cases.

- [ ] **Day 19 — Externalize JWT secret/TTL**
  - [ ] **Learn:** [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html) · [Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html)
  - [ ] **Practice:** Secret via env; fail fast in prod
    1. YAML: `app.jwt.secret: ${JWT_SECRET:dev-only-change-me-use-long-secret}` and `app.jwt.expiration-ms`.
    2. Run with env `JWT_SECRET` set to a long random value; confirm tokens still validate.
    3. `prod` profile: `@PostConstruct` or `ApplicationRunner` fails startup if secret length < 32 (or missing).
    4. Ensure secret is not committed as a real prod value.
    5. Document env vars in README draft.
    6. **Done when:** Env override works and weak/missing secret fails in `prod`.

- [ ] **Day 20 — README security section**
  - [ ] **Learn:** Skim [OWASP AuthN](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html) + your filter chain notes
  - [ ] **Practice:** Token howto + threat notes
    1. README: how to register/login and copy Bearer token.
    2. Sample curls: login → CRUD → admin vs user.
    3. List public vs authenticated routes.
    4. Learning-level threat notes: weak secrets, XSS stealing tokens, long TTL.
    5. Link to `JWT_SECRET` / TTL config keys.
    6. **Done when:** README alone gets a reader from zero to authenticated CRUD.

- [ ] **Day 21 — Mastery & JPA prep**
  - [ ] **Learn:** [Spring Security docs home](https://docs.spring.io/spring-security/reference/) (bookmark)
  - [ ] **Practice:** Checklist + User entity sketch
    1. Complete §12 mastery checklist.
    2. Sketch `User` entity fields for topic 14: `id`, `username`, `passwordHash`, `roles`, timestamps.
    3. Note what becomes a JPA `@Entity` vs stays in-memory.
    4. Fix any Must gaps in Mini-project A.
    5. Bookmark Security + Password Storage docs.
    6. **Done when:** Checklist ≥90% and User field sketch exists.

*Stay on 3 weeks; do not skip Week 1—JWT without AuthN/AuthZ clarity produces insecure apps.*

---

## 5. Hands-on exercises

### Warm-ups

1. Permit actuator health (or not)—decide consciously and document.
2. Encode a password with BCrypt; verify matches; show plaintext compare fails.
3. Two users, one admin route; prove USER gets 403 and ADMIN gets 200.
4. Disable CSRF for API; explain when you would keep it enabled.
5. Log authentication success/failure **without** logging passwords or full tokens.

### JWT drills

6. Create token with `sub` + roles; parse and print claims in a unit-style main/test.
7. Expired token rejected; clock-skew note in docs.
8. Tampered payload rejected (signature fail).
9. Missing `Authorization` header vs malformed Bearer scheme—both 401.
10. `@PreAuthorize("hasAuthority('ORDER_WRITE')")` with custom authority (not only roles).

### Stretch

11. Refresh-token sketch (separate opaque token store)—optional, keep simple.
12. `@WithMockUser(roles="ADMIN")` mockMvc test for one secured endpoint.
13. Rate-limit login attempts conceptually (filter or note for later infrastructure).
14. Migrate in-memory users to a repository interface ready for JPA.

---

## 6. Mini-projects

Detailed build guides (Goal → Build steps → DoD → Demo + curl/status): [`../mini-projects/phase-04-spring/13-spring-security-jwt/`](../mini-projects/phase-04-spring/13-spring-security-jwt/).

### A. Secure Login System (required)

**Guide:** [A-secure-login-system.md](../mini-projects/phase-04-spring/13-spring-security-jwt/A-secure-login-system.md)

- [Must] Register (optional but recommended) + Login issuing JWT
- [Must] Password hashing with `PasswordEncoder`
- [Must] JWT filter + stateless security config
- [Must] Public auth routes; secured resource routes
- [Must] At least two roles enforced on meaningful endpoints
- [Must] JSON 401/403 responses
- [Must] README with curl examples (login → use token → admin vs user)
- [Should] `/api/auth/me` returning current principal claims (no secrets)
- [Should] Configurable JWT secret/TTL
- [Could] Method-level security on service layer

### B. Role-Guard Demo API

**Guide:** [B-role-guard-demo.md](../mini-projects/phase-04-spring/13-spring-security-jwt/B-role-guard-demo.md)

- Small resource (notes/posts) where `USER` CRUD owns rows and `ADMIN` lists all.
- Focus: authorization rules in service + security annotations.

### C. (Optional) Security regression script

**Guide:** [C-security-regression-script.md](../mini-projects/phase-04-spring/13-spring-security-jwt/C-security-regression-script.md)

- Ordered HTTP calls documenting expected status codes; run after every config change.

---

## 7. Industry use cases

- **SaaS APIs:** Login, role-based admin panels, tenant user permissions.
- **Mobile / SPA backends:** Bearer JWT to stateless servers horizontally scaled.
- **Internal tools:** Basic auth or form login for server-rendered admin (know the difference).
- **Gateway-ready services:** Same JWT validation patterns appear at API gateways later.
- **Compliance baseline:** Encoded passwords, least privilege, auditable auth failures.

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Storing plaintext passwords | Always `PasswordEncoder` |
| `permitAll` on everything “to make it work” | Open only auth + truly public routes |
| Confusing 401 and 403 | 401 = not authenticated; 403 = authenticated but not allowed |
| Weak JWT secret in repo | Env var; long random secret; prod profile checks |
| Long-lived tokens forever | Short access TTL; plan rotation |
| Putting passwords or tokens in logs/responses | Redact; never echo secrets |
| CSRF disabled without understanding | OK for bearer-token APIs; not a free pass for cookie session apps |
| Roles checked only in UI | Enforce on server every time |
| Filter not registered / wrong order | Verify chain; test with curl |
| Trusting client-sent roles without signature | Roles come from validated token claims or server DB |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Spring Security Reference](https://docs.spring.io/spring-security/reference/) | Official source of truth |
| [Servlet Security / Filter chain](https://docs.spring.io/spring-security/reference/servlet/architecture.html) | Architecture |
| [Password Storage](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html) | Encoders |
| [Authorization](https://docs.spring.io/spring-security/reference/servlet/authorization/index.html) | HTTP & method security |
| [Spring.io — Securing a Web Application](https://spring.io/guides/gs/securing-web/) | Official guide (session-oriented baseline) |
| [Baeldung — Spring Security](https://www.baeldung.com/security-spring) | Free practical articles |
| [Baeldung — JWT with Spring Security](https://www.baeldung.com/spring-security-oauth-jwt) | Patterns (read critically; adapt to your Boot version) |
| [RFC 7519 — JWT](https://datatracker.ietf.org/doc/html/rfc7519) | Token semantics |
| [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html) | Free hardening practices |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Spring Security docs: https://docs.spring.io/spring-security/reference/
- Spring Security project: https://spring.io/projects/spring-security
- Architecture (filters): https://docs.spring.io/spring-security/reference/servlet/architecture.html
- SecurityFilterChain: https://docs.spring.io/spring-security/reference/servlet/configuration/java.html
- Password encoding: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
- Authorization / authorizeHttpRequests: https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
- Method security: https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html
- CSRF: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html
- CORS: https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
- Guide — Securing a Web Application: https://spring.io/guides/gs/securing-web/
- JWT RFC 7519: https://datatracker.ietf.org/doc/html/rfc7519
- OWASP AuthN cheat sheet: https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html
- OWASP Authorization cheat sheet: https://cheatsheetseries.owasp.org/cheatsheets/Authorization_Cheat_Sheet.html
- Baeldung Spring Security index: https://www.baeldung.com/security-spring

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Lock & unlock | Filter chain permits/denies deliberately | Predict 401/403 |
| 2. Users & passwords | BCrypt + UserDetailsService | Login works (basic or form) |
| 3. Roles | HTTP + method rules | Admin vs user proven |
| 4. JWT issue | Login returns signed token | Claims readable after verify |
| 5. JWT consume | Filter sets SecurityContext | Bearer access to CRUD |
| 6. Secure Login System | Mini-project A complete | README + test script |
| 7. Gate | Ready for JPA user persistence | Mastery checklist ≥90% |

**Weekly rhythm:** Mon–Fri implement; Sat attack your own API with bad tokens; Sun README + checklist.

```text
Default Security → Explicit chain + roles → JWT issue/validate → Secure Login System → Harden secrets/TTL
```

---

## 12. Mastery checklist

- [ ] Explain authentication vs authorization with your own API examples
- [ ] Configure `SecurityFilterChain` with public and secured routes
- [ ] Store passwords only with a `PasswordEncoder` (BCrypt or better)
- [ ] Implement `UserDetailsService` (in-memory or DB-ready)
- [ ] Enforce roles/authorities on at least one HTTP route and understand 403
- [ ] Issue a JWT on login and validate it on protected requests
- [ ] JWT filter integrates cleanly; session policy is stateless for the API
- [ ] Handle expired/invalid/missing tokens with consistent JSON errors
- [ ] Do not log or return secrets, password hashes, or raw tokens unnecessarily
- [ ] Externalize JWT secret and TTL; document env setup
- [ ] Completed Secure Login System (§6A) with README and sample requests
- [ ] Debugged at least one real 401 and one 403 to root cause
- [ ] Notes for JPA: user/role tables you will map next
- [ ] Ready to start [14-jpa-hibernate.md](14-jpa-hibernate.md)
