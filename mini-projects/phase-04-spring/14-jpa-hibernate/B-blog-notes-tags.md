# Mini-project B — Blog / Notes with Tags

**Topic:** [14 — JPA & Hibernate](../../../phase-04-spring/14-jpa-hibernate.md)

---

## Goal

Model Post–Tag as many-to-many (or association entity). Support search, pagination, and audit fields. Focus: join table ownership and fetch control without LazyInitializationException or N+1 on the happy path.

---

## Prerequisites

- [A — Catalog + Orders](A-catalog-orders-jpa.md) relationships experience
- Postgres available
- Boot Web + Data JPA

---

## Learn links

| Resource | Why |
|----------|-----|
| [Hibernate associations](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#associations) | Many-to-many |
| [Baeldung — Many-to-Many](https://www.baeldung.com/jpa-many-to-many) | Join table patterns |
| [Spring Data query methods](https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods.html) | Search |
| [JPA Auditing](https://docs.spring.io/spring-data/jpa/reference/auditing.html) | created/updated |

### start.spring.io settings

| Field | Value |
|-------|--------|
| Group | `com.learn` |
| Artifact | `blog-tags` |
| Package name | `com.learn.blogtags` |
| Dependencies | **Web**, **Data JPA**, **PostgreSQL**, **Validation** |

---

## Project layout

```text
com.learn.blogtags/
  BlogTagsApplication.java
  post/
    Post.java                   # title, body, @ManyToMany tags
    PostRepository.java
    PostService.java
    PostController.java
    dto/PostRequest.java
    dto/PostResponse.java
  tag/
    Tag.java                    # unique name
    TagRepository.java
  config/
    JpaAuditingConfig.java
src/main/resources/application.yml
README.md
```

**Mapping choice (pick one and document):**

1. `@ManyToMany` + `@JoinTable` on `Post` (owning side), or  
2. Association entity `PostTag` with two `@ManyToOne` (more flexible later)

---

## Build steps

- [ ] **1. Scaffold + DB**
  1. Initializr; DB `blog_tags`.
  2. YAML Postgres + `show-sql` + `ddl-auto: update`.
  3. **Done when:** App starts against Postgres.

- [ ] **2. Tag + Post entities**
  1. `Tag`: unique `name`.
  2. `Post`: title, body; many-to-many tags; LAZY collections.
  3. Helper methods `addTag` / `removeTag` keep both sides in sync if bidirectional.
  4. **Done when:** Schema shows join table (e.g. `post_tags`).

- [ ] **3. Auditing fields**
  1. `@CreatedDate` / `@LastModifiedDate` on Post (and optionally Tag).
  2. `@EnableJpaAuditing`.
  3. **Done when:** Create/update fills timestamps.

- [ ] **4. Create post with tags**
  1. `POST /api/posts` body `{"title","body","tags":["java","jpa"]}` → **201**.
  2. Service finds-or-creates tags by name; associates.
  3. Response DTO includes tag names (map inside transaction).
  4. **Done when:** Join rows visible in DB; JSON has tags.

- [ ] **5. Get by id + fetch control**
  1. `GET /api/posts/{id}` → **200** with tags; **404** if missing.
  2. Use join fetch / entity graph so tags load inside service TX.
  3. **Done when:** No `LazyInitializationException`; SQL shows join (or batched selects by design).

- [ ] **6. Search + page**
  1. `GET /api/posts?q=spring&page=0&size=5` → **200** page.
  2. Filter by title contains and/or tag name (JPQL or derived query).
  3. **Done when:** Pagination + filter work; empty q returns page of all.

- [ ] **7. List by tag**
  1. `GET /api/tags/{name}/posts` → **200** list/page.
  2. **Done when:** Only posts with that tag returned.

- [ ] **8. Update tags + README**
  1. `PUT /api/posts/{id}` can replace tag set → **200**.
  2. README: mapping choice, curl create/search/get, note on owning side.
  3. **Done when:** Cold demo from README works.

---

## Definition of Done

- [ ] Post–Tag many-to-many or association entity
- [ ] Search + pagination
- [ ] Audit fields populated
- [ ] Join table ownership documented
- [ ] Fetch controlled on read paths (no surprise lazy failures)
- [ ] DTO API responses
- [ ] README with curls and expected statuses

---

## Demo script

```bash
curl -i -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"JPA Tags\",\"body\":\"…\",\"tags\":[\"java\",\"jpa\"]}"
# → 201

curl -i http://localhost:8080/api/posts/1
# → 200 with tags ["java","jpa"]

curl -i "http://localhost:8080/api/posts?q=jpa&page=0&size=5"
# → 200 page

curl -i http://localhost:8080/api/tags/java/posts
# → 200 posts tagged java

curl -i -X PUT http://localhost:8080/api/posts/1 \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"JPA Tags\",\"body\":\"…\",\"tags\":[\"spring\"]}"
# → 200 tags replaced
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| No owning side clarity | Document `@JoinTable` side |
| Serializing lazy tags outside TX | Map DTO in service |
| Creating duplicate Tag rows | find-or-create by unique name |
| `CascadeType.REMOVE` wiping shared tags | Cascade persist carefully; don’t remove Tag with Post |

---

## After this

Optional [C — Migration starter](C-migration-starter.md). Then integrate everything in [BookStore API](../phase-project/bookstore-api.md).
