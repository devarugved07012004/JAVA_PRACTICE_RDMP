# Mini-Project A — Contact Book

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/03-collections.md](../../phase-01-core-java/03-collections.md)  
**Code folder suggestion:** `projects/phase-01/03-collections/contact-book/`  
**Time:** ~8 hours at 1 hr/day  

## Goal

Store contacts in a `List`, index by phone/email with `Map` for O(1) lookup, tag contacts with `Set`, and search by name prefix (list scan OK). Console menu; in-memory.

## Prerequisites

- [ ] OOP packages + encapsulation
- [ ] `List`, `Map`, `Set`, generics basics from Collections Weeks 1–2

## Learn (open before coding)

- [List Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/list.html)
- [Map Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html)
- [Set Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/set.html)
- [`ArrayList` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html)
- [`HashMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashMap.html)
- [`HashSet` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashSet.html)

## Project layout

```text
contact-book/
  README.md
  src/
    com/learn/contacts/
      app/Main.java
      model/Contact.java
      service/ContactBook.java
```

## Build steps

### Step 1 — `Contact` model

- [ ] Fields: id, name, phone, email, `Set<String> tags`
- [ ] Validate non-blank name; phone/email format lightly (non-blank, maybe contains `@` for email)
- [ ] `equals`/`hashCode` by id (or phone)—document choice
- [ ] Compile model + stub main
  ```bash
  javac -d out src/com/learn/contacts/model/*.java src/com/learn/contacts/app/Main.java
  java -cp out com.learn.contacts.app.Main
  ```
- **Done when:** Contact with tags constructs; blank name rejected.

### Step 2 — Primary list store

- [ ] `ContactBook` holds `List<Contact> contacts`
- [ ] `add(Contact)`, `listAll()`, `size()`
- [ ] Reject duplicate id
- **Done when:** Add 2 contacts; list prints both.

### Step 3 — Phone & email indexes

- [ ] `Map<String, Contact> byPhone`, `Map<String, Contact> byEmail`
- [ ] On add: put into both maps; on conflict (phone taken) reject
- [ ] `findByPhone`, `findByEmail` → Contact or null/`Optional` if you know it
- [ ] Do **not** scan the list for these lookups
- **Done when:** Lookup by phone is O(1) and finds the contact after add.

### Step 4 — Tags with `Set`

- [ ] `addTag(contactId, tag)`, `removeTag(...)`
- [ ] `findByTag(tag)` → list scan or secondary `Map<String, Set<id>>` (stretch)
- [ ] Normalize tags (lowercase/trim)
- **Done when:** Tag `work` on two contacts; query returns both.

### Step 5 — Name prefix search

- [ ] `searchByNamePrefix(String prefix)` scans list; case-insensitive `startsWith`
- [ ] Menu option Search by name
- **Done when:** Prefix `Al` finds `Alice` and `Albert`, not `Bob`.

### Step 6 — Update & delete with index maintenance

- [ ] Update phone/email: remove old map keys, put new
- [ ] Delete: remove from list and both maps
- [ ] Verify indexes never point at deleted contacts
- **Done when:** After phone change, old phone lookup fails; new succeeds.

### Step 7 — Console menu

- [ ] Add / List / Find phone / Find email / Search name / Tag ops / Quit
- [ ] Thin UI; all collection logic in `ContactBook`
- **Done when:** Full session works without crashing on missing keys.

### Step 8 — Seed + README

- [ ] Seed ≥5 contacts with overlapping tags
- [ ] README: why List + Maps + Set; compile/run; demo
- [ ] Note Big-O: list search vs map lookup
- **Done when:** Demo runs from seed in under 2 minutes.

## Definition of Done

- [ ] List is source of truth for iteration
- [ ] Phone/email Maps kept in sync
- [ ] Tags use `Set`
- [ ] Prefix search works
- [ ] README explains structure choices

## Demo script

1. Seed loads 5 contacts
2. Find by phone → instant hit
3. Add tag `family` to one contact; find by tag
4. Search name prefix `Jo`
5. Update email; old email miss / new hit
6. Quit

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Only using List for phone lookup | Maintain HashMap indexes |
| Forgetting to update maps on edit/delete | Treat indexes as part of the write path |
| Mutable tags exposed directly | Return copy or unmodifiable view |
| Case-sensitive emails as different keys | Normalize keys |
| UI owning the ArrayList | Service owns collections |

## After this

Next: [B — Warehouse Inventory](B-warehouse-inventory.md) · Topic: [03-collections.md](../../phase-01-core-java/03-collections.md)
