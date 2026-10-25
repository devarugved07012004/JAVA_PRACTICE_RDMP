# Mini-project B — Elastic Beanstalk Alternate Deploy

**Topic:** [17 AWS](../../../phase-05-distributed/17-aws.md) · **Phase 5**

---

## Goal

Deploy the same Spring Boot JAR once to **Elastic Beanstalk**, configure env properties for RDS (or Beanstalk-managed DB if used carefully), smoke-test HTTP, then **tear down the environment completely**.

---

## Prerequisites

- Mini-project A app JAR (or equivalent)
- AWS CLI + EB CLI optional (`pip install awsebcli`) or Console
- Billing alert still on

---

## Learn

| Resource | Why |
|----------|-----|
| [Elastic Beanstalk Developer Guide](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/Welcome.html) | Platforms, env vars |
| [Deploying Java applications](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/java-se-platform.html) | JAR on Java platform |

---

## Project layout

```text
elastic-beanstalk-deploy/
  app.jar                 # or link to build output
  .ebextensions/          # optional
  NOTES.md                # env property mapping
  README.md               # deploy + destroy
```

---

## Build steps

- [ ] **1. Package**
  1. `mvn -DskipTests package` → single executable JAR.
  2. **Done when:** JAR runs locally with profile pointing at reachable DB.

- [ ] **2. Create EB application**
  1. Console or: `eb init` (region, Java platform) / create application in Console.
  2. **Done when:** Application name exists in the region.

- [ ] **3. Create environment**
  1. Single-instance / free-tier-minded; load balancer only if you accept cost.
  2. Upload JAR; wait for Green/Ok.
  3. **Done when:** EB health is Ok/Green and URL responds.

- [ ] **4. Env properties for RDS**
  1. Set `SPRING_DATASOURCE_URL`, username, password in EB configuration (not in JAR).
  2. Ensure SG between EB instances and RDS allows `5432`.
  3. **Done when:** App health UP against RDS (or documented H2 fallback if RDS reuse is blocked—prefer RDS).

- [ ] **5. Smoke test**
  1. `curl https://<eb-url>/actuator/health` (or HTTP).
  2. Hit one business endpoint.
  3. **Done when:** NOTES include URL + status codes.

- [ ] **6. Compare notes to EC2 path**
  1. Write 5 bullets: what EB hid vs what you still configure (SG, env, IAM).
  2. **Done when:** Comparison is in `NOTES.md`.

- [ ] **7. Tear down completely**
  1. Terminate/delete EB environment; delete application if lab-only.
  2. Confirm no leftover load balancers / EC2 from EB.
  3. **Done when:** Console shows environment gone; billing risk reduced.

- [ ] **8. README**
  1. Deploy steps + destroy steps; warn about lingering CNAME/S3 EB buckets if any.
  2. **Done when:** Destroy is first-class, not an afterthought.

---

## Definition of Done

- [ ] Same JAR deployed to Beanstalk once
- [ ] Env properties documented for RDS
- [ ] Smoke test recorded
- [ ] Environment fully torn down

---

## Demo

```bash
# With EB CLI (example)
eb init -p "Java 17" bookstore-eb --region <region>
eb create bookstore-eb-env
eb setenv SPRING_DATASOURCE_URL=jdbc:postgresql://... SPRING_DATASOURCE_USERNAME=... 
eb deploy
curl -s http://$(eb status | findstr CNAME)/actuator/health
eb terminate bookstore-eb-env --force
```

*(Console-only deploy is fine—mirror the same steps in README.)*

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Leaving EB env running | Terminate after demo |
| Secrets in `.ebextensions` committed | Use EB console env / Secrets Manager awareness |
| Open SG on RDS | Only EB/instance SG |
| Ignoring EB-created resources | Check EC2, ELB, S3 after terminate |

---

## After this

- Stretch: [C-vpc-design-sketch.md](./C-vpc-design-sketch.md)
- Prefer documenting **one** primary path (usually EC2+RDS) for Capstone
