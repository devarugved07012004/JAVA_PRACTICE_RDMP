# 17 — AWS Cloud

**Duration:** ~3–4 weeks (21–28 days) · **Cadence:** 1 hour/day  
**Focus:** Cloud Fundamentals, EC2, S3, IAM, RDS, VPC, CloudWatch, Elastic Beanstalk, Deploying Spring Boot · Prefer EC2 + RDS + S3 (free-tier focused)

---

## 1. Why this topic is important

Local Docker Compose proves the app; AWS teaches **where production systems live**: compute, managed databases, object storage, identity, networking, and monitoring. Mapping “Compose service → cloud building block” is a core backend skill.

This topic stays **free-tier focused** and favors a clear learning path: deploy a Spring Boot API on **EC2**, persist with **RDS PostgreSQL**, store files on **S3**, lock down access with **IAM**, place resources in a simple **VPC**, watch metrics/logs in **CloudWatch**, and optionally try **Elastic Beanstalk** as a higher-level deploy option. Tear-down and cost awareness are part of mastery—not optional.

---

## 2. Prerequisites

- [16-docker.md](16-docker.md) checklist ≥80% (you can deploy a JAR without Docker on EC2; Docker still helps)
- Spring Boot JAR that runs against PostgreSQL (Phase 4 BookStore ideal)
- Credit/debit card for AWS account signup (free tier has limits—monitor billing)
- SSH client on Windows (OpenSSH or PuTTY)
- Willingness to delete resources when done each week

**Environment you will set up:**

- AWS account + MFA on root; IAM user for daily work
- AWS CLI v2 configured with a region (e.g. `ap-south-1` or closest)
- Key pair for EC2 SSH
- Personal runbook: create → verify → **destroy** checklist

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Regions & AZs | Latency, resilience, data residency |
| Shared responsibility | AWS secures cloud; you secure in-cloud config/data |
| IAM users, groups, policies | Least privilege; no long-lived root keys |
| EC2 instance | VM: AMI, type, security group, key pair |
| Security groups | Stateful firewall: SSH/HTTP/HTTPS rules |
| S3 buckets | Object storage; keys/prefixes; public vs private |
| Billing & free tier | Budgets/alarms; always tear down labs |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| RDS PostgreSQL | Managed DB; subnet groups; security groups; endpoints |
| VPC basics | VPC, subnets (public/private idea), route tables, IGW |
| Deploy Spring Boot on EC2 | Java install or Docker; `systemd` or screen; nginx optional |
| Env-based config | DB URL, secrets via env—not hardcoded |
| S3 from Java | AWS SDK v2: upload/download; pre-signed URL idea |
| CloudWatch | EC2 metrics; log groups/agents (basic); alarms |
| Elastic Beanstalk | Platform as a service deploy of JAR/Docker (overview + one lab) |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| Private RDS | App in public subnet / RDS in private (learning diagram + attempt) |
| IAM roles for EC2 | Prefer instance role over access keys on the box |
| Least-privilege S3 policy | Bucket policy vs IAM identity policy |
| Cost control | Budgets, right-sizing, stop instances when idle |
| Map Compose → AWS | `app`→EC2/EB/ECS; `db`→RDS; files→S3; logs→CloudWatch |
| Production gaps (awareness) | ALB, HTTPS/ACM, Multi-AZ, backups—know they exist |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes + tear-down · 5 min mark boxes

### Week 1 — Account, IAM, EC2, S3 (Days 1–7)

- [ ] **Day 1 — Account hygiene**
  - [ ] **Learn:** [AWS Free Tier](https://aws.amazon.com/free/) · [IAM best practices](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html) · [AWS Budgets](https://docs.aws.amazon.com/cost-management/latest/userguide/budgets-managing-costs.html)
  - [ ] **Practice:** Secure the account and set a budget alarm
    1. Confirm root account MFA is enabled in the AWS console (IAM / security credentials).
    2. Create an IAM admin user (or role) for daily work; do **not** use root for labs.
    3. Enable MFA on that IAM user.
    4. Create an AWS Budget or billing alarm (e.g. $5–10 threshold) with email notification.
    5. Write a one-line “stop resources nightly” habit in notes.
    6. **Done when:** MFA is on, IAM admin exists, and a budget/alarm is active.

- [ ] **Day 2 — AWS CLI**
  - [ ] **Learn:** [AWS CLI install](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html) · [IAM User Guide intro](https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction.html)
  - [ ] **Practice:** Install CLI and verify identity
    1. Install AWS CLI v2 for your OS.
    2. Run `aws configure` with access key for the IAM user, default region (e.g. `ap-south-1` or `us-east-1`), and `json` output.
    3. Run `aws sts get-caller-identity` and confirm Account/Arn.
    4. Pick a home region and write it in `NOTES-aws.md` — use it consistently.
    5. Run `aws configure get region` to verify.
    6. **Done when:** `get-caller-identity` succeeds and home region is recorded.

- [ ] **Day 3 — Launch EC2**
  - [ ] **Learn:** [EC2 Get Started](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/EC2_GetStarted.html)
  - [ ] **Practice:** Launch Amazon Linux and SSH in
    1. In console: Launch Instance → Amazon Linux (free-tier eligible) → t2.micro/t3.micro.
    2. Create/download a key pair (`.pem`); store it safely (not in git).
    3. Launch; note **public IPv4** in notes.
    4. SSH: `ssh -i your-key.pem ec2-user@<public-ip>` (fix username for Ubuntu).
    5. Run `sudo dnf update -y` (or `yum`/`apt`) and `uname -a`.
    6. **Done when:** You are on the instance shell and packages updated.

- [ ] **Day 4 — Security groups**
  - [ ] **Learn:** [EC2 security groups](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-security-groups.html)
  - [ ] **Practice:** Lock SSH to your IP; open 8080 for apps
    1. Open the instance’s security group inbound rules.
    2. Set SSH (22) source to **My IP** only (remove `0.0.0.0/0` if present).
    3. Add Custom TCP 8080 from My IP (or temporary broader only if you understand the risk).
    4. Verify SSH still works; optionally deny your IP briefly to see failure then restore.
    5. Screenshot/list rules into notes (no secrets).
    6. **Done when:** Inbound SSH is IP-restricted and 8080 is open for your experiments.

- [ ] **Day 5 — Spring JAR on EC2**
  - [ ] **Learn:** [EC2 User Guide](https://docs.aws.amazon.com/ec2/) (instance ops skim)
  - [ ] **Practice:** Run a Spring JAR and hit health remotely
    1. On EC2 install JDK 17+: `sudo dnf install java-17-amazon-corretto -y` (or equivalent).
    2. `java -version` must print 17+.
    3. Copy a Spring Boot JAR (scp/`aws s3 cp`) to the instance.
    4. Run `java -jar app.jar` bound to `0.0.0.0:8080` (default Boot).
    5. From your laptop browser/curl: `http://<public-ip>:8080/actuator/health`.
    6. **Done when:** Health returns UP from your browser against the public IP.

- [ ] **Day 6 — Private S3 bucket**
  - [ ] **Learn:** [S3 User Guide](https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html) · [Block Public Access](https://docs.aws.amazon.com/AmazonS3/latest/userguide/access-control-block-public-access.html)
  - [ ] **Practice:** Create a private bucket and upload/download
    1. Console → S3 → Create bucket (globally unique name) in your home region.
    2. Keep **Block Public Access** ON (all four settings).
    3. Upload a small file via console; download it back.
    4. CLI check: `aws s3 ls s3://your-bucket/` and `aws s3 cp` one file.
    5. Confirm public URL access fails / is blocked.
    6. **Done when:** Private bucket holds a file you uploaded and Block Public Access is ON.

- [ ] **Day 7 — Cost & stop habit**
  - [ ] **Learn:** [Billing & Cost Management](https://docs.aws.amazon.com/cost-management/latest/userguide/what-is-costmanagement.html)
  - [ ] **Practice:** Stop idle EC2 and write a costs checklist
    1. In EC2 console **Stop** (not terminate) the instance if you won’t use it tonight.
    2. Confirm state = stopped; note public IP may change on start (unless Elastic IP).
    3. Write checklist: stop EC2, check RDS, empty unused S3 if needed, review Budgets.
    4. Open Billing/Cost Explorer briefly; note any charges.
    5. Week-1 recap: 5 bullets of what you created.
    6. **Done when:** EC2 is stopped (if idle) and costs checklist exists in notes.

### Week 2 — RDS, VPC literacy, wire the app (Days 8–14)

- [ ] **Day 8 — VPC tour**
  - [ ] **Learn:** [What is Amazon VPC](https://docs.aws.amazon.com/vpc/latest/userguide/what-is-amazon-vpc.html)
  - [ ] **Practice:** Tour default VPC and draw the mental model
    1. Console → VPC → Your VPCs; open the default VPC.
    2. List subnets; note AZs and which are public (route to IGW).
    3. Find the Internet Gateway attached to the VPC.
    4. Draw: Internet → IGW → public subnet → EC2 (security group).
    5. Write where RDS will live relative to EC2 (same VPC, SG-controlled).
    6. **Done when:** Diagram + notes cover default VPC, subnet, IGW.

- [ ] **Day 9 — RDS PostgreSQL**
  - [ ] **Learn:** [RDS PostgreSQL](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_PostgreSQL.html) · [RDS User Guide](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Welcome.html)
  - [ ] **Practice:** Create a learning RDS Postgres instance
    1. Create RDS PostgreSQL; choose free-tier-eligible class if available (e.g. `db.t3.micro`).
    2. Set master username/password; store password in a password manager (not git).
    3. Prefer **not** publicly accessible for learning-hardening (you’ll connect via EC2).
    4. Wait until status = Available; copy the endpoint hostname to notes.
    5. Note engine version and SG id.
    6. **Done when:** RDS is Available and endpoint + password are stored safely.

- [ ] **Day 10 — EC2 ↔ RDS connectivity**
  - [ ] **Learn:** [RDS security groups](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Overview.RDSSecurityGroups.html)
  - [ ] **Practice:** Allow EC2 SG to RDS on 5432 and test
    1. On RDS SG inbound: PostgreSQL 5432 **source = EC2 security group id** (not 0.0.0.0/0).
    2. Start EC2 if stopped; SSH in.
    3. Install client: `sudo dnf install postgresql15 -y` (version may vary).
    4. `psql -h <rds-endpoint> -U <user> -d postgres` and run `SELECT 1;`.
    5. If timeout: recheck SG, subnet, “publicly accessible”, and that you’re on EC2 not laptop.
    6. **Done when:** `SELECT 1` works from EC2 to RDS.

- [ ] **Day 11 — App to RDS**
  - [ ] **Learn:** [Spring Boot externalized config](https://docs.spring.io/spring-boot/reference/features/external-config.html)
  - [ ] **Practice:** Point Spring datasource at RDS and verify tables
    1. Set `SPRING_DATASOURCE_URL=jdbc:postgresql://<rds-endpoint>:5432/<db>` plus user/password via env on EC2.
    2. Create the database if needed (`CREATE DATABASE bookstore;`).
    3. Start the Spring app; let Flyway/Liquibase/ddl-auto create schema.
    4. From `psql` on EC2: `\dt` and confirm tables.
    5. Curl a DB-backed endpoint on port 8080.
    6. **Done when:** App uses RDS and tables are visible in `psql`.

- [ ] **Day 12 — Persist process on EC2**
  - [ ] **Learn:** [EC2 User Guide](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/) (ops skim)
  - [ ] **Practice:** Survive SSH logout with systemd or Docker
    1. Choose **systemd** unit for `java -jar` **or** run the app container with Docker on EC2.
    2. For systemd: create `/etc/systemd/system/bookstore.service` with `Restart=on-failure`, enable, start.
    3. `sudo systemctl status bookstore` (or `docker ps`) shows running.
    4. Exit SSH; from laptop curl health again — still UP.
    5. Document start/stop commands in runbook.
    6. **Done when:** App stays up after you disconnect SSH.

- [ ] **Day 13 — S3 from Java**
  - [ ] **Learn:** [AWS SDK for Java S3 examples](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-s3.html) · [SDK for Java home](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
  - [ ] **Practice:** Upload a sample file from the API to S3
    1. Add AWS SDK v2 S3 dependency to the Spring app.
    2. Implement upload endpoint or service method putting an object into your bucket.
    3. For today, temporary access keys via env are OK **or** skip ahead if role is ready (Day 15 preferred).
    4. Call the endpoint; verify object in S3 console/`aws s3 ls`.
    5. Download/get object metadata to confirm.
    6. **Done when:** API upload creates a visible object in your private bucket.

- [ ] **Day 14 — E2E demo & cleanup**
  - [ ] **Learn:** Recap [EC2](https://docs.aws.amazon.com/ec2/) · [RDS](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Welcome.html) · [S3](https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html)
  - [ ] **Practice:** Demo script then stop/delete overnight waste
    1. Write a short demo: health → DB call → S3 upload (commands + expected results).
    2. Run the demo once end-to-end.
    3. Stop EC2; decide whether RDS can stop/delete for the night (RDS stop limits apply — know cost of leaving it up).
    4. Delete unused test objects in S3 if any.
    5. Check Budgets/billing alarm still enabled.
    6. **Done when:** Demo script works and idle spend is reduced overnight.

### Week 3 — IAM roles, CloudWatch, Beanstalk (Days 15–21)

- [ ] **Day 15 — EC2 instance role**
  - [ ] **Learn:** [IAM roles for EC2](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-roles-for-amazon-ec2.html)
  - [ ] **Practice:** Attach an instance role for S3 and drop static keys
    1. Create IAM role for EC2 with policy allowing `s3:GetObject`/`PutObject`/`ListBucket` on **your bucket ARN only**.
    2. Attach the instance profile to your EC2 instance.
    3. Remove access keys from app env/files on the instance.
    4. Restart app; upload again using default credential provider chain.
    5. Confirm `~/.aws/credentials` on the instance is unused/absent for the app.
    6. **Done when:** S3 upload works via instance role with no static keys on disk.

- [ ] **Day 16 — CloudWatch alarm**
  - [ ] **Learn:** [CloudWatch alarms](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/AlarmThatSendsEmail.html) · [What is CloudWatch](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/WhatIsCloudWatch.html)
  - [ ] **Practice:** Create a CPU alarm on EC2
    1. CloudWatch → Alarms → Create alarm on EC2 CPUUtilization.
    2. Set a threshold (e.g. > 70% for 5 minutes) — learning values OK.
    3. Optional: create SNS topic and subscribe your email; confirm subscription.
    4. Save alarm; verify state OK/INSUFFICIENT_DATA initially.
    5. Note alarm name in README/runbook.
    6. **Done when:** CPU alarm exists on your instance (SNS email optional but preferred).

- [ ] **Day 17 — CloudWatch Logs**
  - [ ] **Learn:** [CloudWatch Logs](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/WhatIsCloudWatchLogs.html)
  - [ ] **Practice:** Ship logs or document the manual path
    1. Prefer: install CloudWatch agent **or** redirect app logs to a file and document `aws logs` upload script.
    2. Create a log group e.g. `/bookstore/app`.
    3. Produce log lines; confirm they appear in CloudWatch Logs console.
    4. If agent is too heavy today: document exact local log path + how you’d ship later.
    5. Add log group name to runbook.
    6. **Done when:** Logs visible in CloudWatch **or** manual shipping path fully documented.

- [ ] **Day 18 — Elastic Beanstalk deploy**
  - [ ] **Learn:** [Elastic Beanstalk Java](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/create_deploy_Java.html) · [EB Developer Guide](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/Welcome.html)
  - [ ] **Practice:** Deploy a JAR once to Beanstalk
    1. Create an Elastic Beanstalk application + environment (Java/Tomcat or Docker platform).
    2. Upload/deploy your Spring Boot JAR (or Docker image flow).
    3. Wait for environment health Green/Ok.
    4. Hit the Beanstalk URL `/actuator/health`.
    5. Note what EB provisioned for you (EC2, SG, etc.) in notes.
    6. **Done when:** Health succeeds on the Beanstalk environment URL.

- [ ] **Day 19 — Beanstalk vs EC2 notes**
  - [ ] **Learn:** [Elastic Beanstalk concepts](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/Welcome.html)
  - [ ] **Practice:** Compare Beanstalk vs raw EC2
    1. Table: ops burden, control, deploy steps, learning value (EC2 vs EB).
    2. List what you still don’t control on EB.
    3. List when you’d pick raw EC2 for learning.
    4. Decide which path you’ll standardize for Week 4 mini-project (usually EC2+RDS for clarity).
    5. Half-page conclusion in notes.
    6. **Done when:** Comparison table + chosen path for Week 4 exist.

- [ ] **Day 20 — Harden & HTTPS awareness**
  - [ ] **Learn:** [IAM best practices](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html)
  - [ ] **Practice:** Harden notes — HTTPS awareness, no keys in git
    1. Write HTTPS options: ALB + ACM certificate (awareness-level steps, no need full setup).
    2. Scan project for accidental AWS keys (`AKIA...`); rotate if any were committed.
    3. Confirm `.gitignore` excludes `.env`, `*.pem`, credentials.
    4. Reaffirm IAM least privilege on the S3 role.
    5. Add “never commit keys” to README security section.
    6. **Done when:** Harden notes + git hygiene checks are done.

- [ ] **Day 21 — Full tear-down**
  - [ ] **Learn:** [Cost Management](https://docs.aws.amazon.com/cost-management/latest/userguide/what-is-costmanagement.html)
  - [ ] **Practice:** Tear down Week 1–3 resources and verify billing
    1. Terminate/delete EB environment if created.
    2. Stop/terminate EC2; delete RDS (final snapshot optional for learning — know cost).
    3. Empty/delete S3 test objects; delete unused security groups if safe.
    4. Delete leftover load balancers/volumes if EB left any.
    5. Check Billing/Budgets; confirm no surprise running resources in EC2/RDS/EB consoles.
    6. **Done when:** Consoles show no leftover paid lab resources you intended to delete.

### Week 4 — Consolidation & mini-project (Days 22–28)

- [ ] **Day 22 — Rebuild from runbook**
  - [ ] **Learn:** Your runbook + [EC2 Get Started](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/EC2_GetStarted.html)
  - [ ] **Practice:** Rebuild EC2 + RDS + S3 from runbook only
    1. Using only your runbook, launch EC2, recreate/connect RDS, ensure S3 bucket exists.
    2. Do not invent steps not in the runbook — fix the runbook when stuck.
    3. SSH, JDK, app start, `psql` connectivity.
    4. Curl health from laptop.
    5. Note every runbook gap you fixed.
    6. **Done when:** Clean stack is up following the runbook alone.

- [ ] **Day 23 — Deploy BookStore**
  - [ ] **Learn:** Recap [RDS PostgreSQL](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_PostgreSQL.html)
  - [ ] **Practice:** Deploy BookStore/catalog fully on the AWS path
    1. Deploy BookStore or catalog service JAR/container to EC2 with RDS datasource.
    2. Apply schema; seed at least one book/course row.
    3. Exercise list/get (and one write) via public IP/SG.
    4. Record exact deploy commands in `docs/aws-deploy.md`.
    5. Verify systemd/Docker restart survival.
    6. **Done when:** Core CRUD/list path works against RDS on EC2.

- [ ] **Day 24 — S3 + IAM role verify**
  - [ ] **Learn:** [IAM roles for EC2](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-roles-for-amazon-ec2.html) · [S3 SDK examples](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-s3.html)
  - [ ] **Practice:** Verify S3 feature with role only
    1. Re-attach/confirm instance role with bucket-scoped S3 permissions.
    2. Wire cover/material upload feature to the bucket.
    3. Remove any leftover access keys from the instance.
    4. Upload via API; list object in console.
    5. Attempt upload with role detached once (should fail); re-attach and confirm success.
    6. **Done when:** Upload works with role credentials only (no keys on disk).

- [ ] **Day 25 — Alarm + health docs**
  - [ ] **Learn:** [CloudWatch alarms](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/AlarmThatSendsEmail.html)
  - [ ] **Practice:** Document alarm and health URL in README
    1. Ensure CPU (or status check) alarm is active on the instance.
    2. Put health URL `http://<ip>:8080/actuator/health` in README.
    3. Put alarm name + console path in README.
    4. Run health curl; paste expected JSON shape (no secrets).
    5. Link Budgets reminder in README.
    6. **Done when:** README lists health URL + CloudWatch alarm clearly.

- [ ] **Day 26 — Private RDS attempt**
  - [ ] **Learn:** [VPC](https://docs.aws.amazon.com/vpc/latest/userguide/what-is-amazon-vpc.html) · [RDS security groups](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Overview.RDSSecurityGroups.html)
  - [ ] **Practice:** Diagram private RDS and harden public access
    1. Draw: public subnet EC2 + RDS with publicly accessible = No, SG only from EC2.
    2. Confirm RDS “Publicly accessible” = No (modify if needed; apply window).
    3. Prove laptop `psql` to RDS fails/times out; EC2 `psql` still works.
    4. Document why this is safer for learning deploys.
    5. Note NAT/bastion awareness for private EC2 (optional future).
    6. **Done when:** Diagram exists and RDS is not publicly reachable from the internet.

- [ ] **Day 27 — Compose → AWS mapping**
  - [ ] **Learn:** §11 mapping table in this file
  - [ ] **Practice:** Write Compose → AWS mapping for your stack
    1. Table rows: app container → EC2/EB; Postgres → RDS; volume → RDS storage; network → VPC/SG; env file → SSM/Secrets Manager awareness.
    2. Fill with **your** service names.
    3. Add one column “what changes in Capstone.”
    4. Keep the table in `NOTES-aws.md` or §11 area.
    5. Review for accuracy against what you actually deployed.
    6. **Done when:** Mapping table is complete for your stack.

- [ ] **Day 28 — Final tear-down & gate**
  - [ ] **Learn:** [AWS Budgets](https://docs.aws.amazon.com/cost-management/latest/userguide/budgets-managing-costs.html)
  - [ ] **Practice:** Final tear-down, checklist, cost confirm
    1. Tear down EC2/RDS/EB/unused SGs per runbook (keep S3 empty or delete lab bucket if desired).
    2. Verify consoles clear of lab resources.
    3. Tick mastery checklist honestly.
    4. Check Billing — no unexpected running charges.
    5. Write “what I’d do differently next time” (5 bullets).
    6. **Done when:** Resources cleaned, checklist done, billing looks sane.

*On a 3-week pace: fold Week 4 into Days 18–21 (one clean deploy + tear-down + mapping notes).*

---

## 5. Hands-on exercises

1. Create IAM policy that allows `s3:ListBucket` + `s3:GetObject` on one bucket only; prove denial elsewhere.
2. SSH fails: diagnose SG, key, username (`ec2-user` vs `ubuntu`).
3. Change RDS SG incorrectly; observe connection timeout; fix.
4. Rotate: stop EC2 nightly; start next day; confirm Elastic IP vs changing public IP lesson.
5. S3 Block Public Access; attempt public object (should fail).
6. Generate pre-signed URL (CLI or SDK) for temporary download.
7. CloudWatch alarm: force CPU (stress) or lower threshold temporarily to see notification path.
8. Deploy wrong Java version on EC2; read error; install correct JDK.
9. Beanstalk health dashboard: cause a bad deploy; roll back or re-deploy good JAR.
10. Cost explorer / bills: identify every resource still running.
11. Delete RDS final snapshot decision: understand irreversible delete.
12. Document incident: “I left an instance running”—prevention checklist.

---

## 6. Mini-projects

> **Build guides:** [A Deploy Spring Boot AWS](../mini-projects/phase-05-distributed/17-aws/A-deploy-springboot-aws.md) · [B Elastic Beanstalk Deploy](../mini-projects/phase-05-distributed/17-aws/B-elastic-beanstalk-deploy.md) · [C VPC Design Sketch](../mini-projects/phase-05-distributed/17-aws/C-vpc-design-sketch.md) · [Index](../mini-projects/README.md)

### A. Deploy Spring Boot to AWS (primary)

**Architecture (learning-clear):**

```text
Internet → EC2 (Spring Boot :8080)
                ↘ RDS PostgreSQL
                ↘ S3 (uploads/static assets)
IAM instance role → S3
CloudWatch → basic alarm on EC2
```

**Must:**

- App reachable (HTTP) from your machine
- RDS as primary datastore
- At least one S3 upload/download path
- IAM role (no long-lived keys on instance for S3)
- README runbook: create, configure env, deploy, **destroy**
- Free-tier-minded instance/DB sizes

**Should:** CloudWatch alarm; systemd or Docker restart policy  
**Could:** Nginx reverse proxy; HTTPS via simple tunnel/docs-only ACM path

### B. Elastic Beanstalk Alternate Deploy

- Deploy same JAR to Beanstalk once
- Note env property configuration for RDS
- Tear down environment completely

### C. (Stretch) VPC Design Sketch

- Diagram: public subnet EC2 + private subnet RDS + NAT awareness (even if NAT not deployed to save cost)

---

## 7. Industry use cases

- **Company APIs on VMs:** EC2 + RDS still common for smaller systems
- **Object storage:** Images, PDFs, backups, static assets on S3
- **Managed Postgres:** RDS backups, patching, Multi-AZ for production
- **Identity boundary:** IAM roles for compute—no secrets in Git
- **Ops visibility:** CloudWatch metrics/logs/alarms as first line of monitoring
- **PaaS lane:** Elastic Beanstalk / higher abstractions when teams want less server babysitting

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Using root account daily | IAM user + MFA; lock root |
| `0.0.0.0/0` on SSH | Restrict to your IP; prefer SSM later |
| Publicly accessible RDS open to world | SG only from app SG; prefer not publicly accessible |
| Access keys in Git or AMI | Instance roles; `.gitignore`; rotate if leaked |
| Leaving resources on | Stop/terminate; budgets; calendar reminder |
| Hardcoding secrets in `application.yml` committed | Env vars / SSM Parameter Store awareness |
| Wrong region chaos | Stick to one region for labs |
| Ignoring free-tier limits | Read free tier docs; prefer t2/t3.micro equivalents |
| No tear-down in README | Destroy steps are part of Definition of Done |
| Expecting Compose networking to “just work” identically | Re-map hostnames, SGs, and DNS deliberately |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [AWS Free Tier](https://aws.amazon.com/free/) | What’s included |
| [AWS Skill Builder (free digital)](https://skillbuilder.aws/) | Cloud practitioner-style modules |
| [EC2 User Guide](https://docs.aws.amazon.com/ec2/) | Instances, SG, networking |
| [IAM User Guide](https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction.html) | Users, roles, policies |
| [Amazon S3 User Guide](https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html) | Buckets, objects, Block Public Access |
| [Amazon RDS User Guide](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Welcome.html) | Postgres on RDS |
| [VPC User Guide](https://docs.aws.amazon.com/vpc/latest/userguide/what-is-amazon-vpc.html) | Subnets, routing |
| [CloudWatch User Guide](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/WhatIsCloudWatch.html) | Metrics, alarms, logs |
| [Elastic Beanstalk Developer Guide](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/Welcome.html) | PaaS deploys |
| [AWS SDK for Java 2.x](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html) | S3 from Spring |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- AWS Documentation hub: https://docs.aws.amazon.com/
- Free Tier: https://aws.amazon.com/free/
- AWS CLI install: https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
- IAM best practices: https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html
- EC2 Linux get started: https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/EC2_GetStarted.html
- Security groups: https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-security-groups.html
- IAM roles for EC2: https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-roles-for-amazon-ec2.html
- S3 User Guide: https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html
- S3 Block Public Access: https://docs.aws.amazon.com/AmazonS3/latest/userguide/access-control-block-public-access.html
- RDS PostgreSQL: https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_PostgreSQL.html
- RDS security groups connectivity: https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Overview.RDSSecurityGroups.html
- VPC fundamentals: https://docs.aws.amazon.com/vpc/latest/userguide/what-is-amazon-vpc.html
- CloudWatch alarms: https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/AlarmThatSendsEmail.html
- CloudWatch Logs: https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/WhatIsCloudWatchLogs.html
- Elastic Beanstalk Java: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/create_deploy_Java.html
- AWS SDK for Java S3: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-s3.html
- Billing & Cost Management: https://docs.aws.amazon.com/cost-management/latest/userguide/what-is-costmanagement.html
- AWS Budgets: https://docs.aws.amazon.com/cost-management/latest/userguide/budgets-managing-costs.html

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Account hygiene | MFA, IAM user, budget | No daily root use |
| 2. Compute | EC2 + SSH + JAR | Health URL works |
| 3. Data | RDS + SG wiring | App talks to Postgres |
| 4. Objects | S3 + IAM role | Upload without static keys |
| 5. Observe | CloudWatch alarm | Alarm path understood |
| 6. Alternate | Beanstalk once | Contrast notes written |
| 7. Gate | Mini-project A + tear-down | Checklist ≥90%; bill clean |

**Non-negotiable habit:** Every lab ends with “is anything still billing?”

**Compose → AWS mapping (memorize):**

| Local Compose | AWS learning target |
|---------------|---------------------|
| `app` service | EC2 (or Elastic Beanstalk) |
| `postgres` service | RDS PostgreSQL |
| Local files / uploads | S3 |
| `docker logs` | CloudWatch Logs (concept) |
| Compose network/SG-ish | VPC + Security Groups |
| Env files | Env on instance / EB env properties |

---

## 12. Mastery checklist

- [ ] Core building blocks for hosting a Java app identified (EC2, RDS, S3, IAM, VPC, CloudWatch)
- [ ] Hands-on: Spring Boot running on EC2 (or Beanstalk) against RDS
- [ ] Hands-on: object stored/retrieved via S3
- [ ] IAM least-privilege mindset; **no secrets in Git**
- [ ] Instance role used for S3 (preferred over access keys on disk)
- [ ] Security groups understood for SSH, app port, and DB port
- [ ] VPC public/private idea explained with a diagram
- [ ] CloudWatch metric or alarm configured at least once
- [ ] Elastic Beanstalk tried once **or** explicitly compared in notes after reading docs + one deploy attempt
- [ ] Tear-down / cost awareness habit demonstrated (resources deleted; budget set)
- [ ] Can map “what I run in Compose” → “what I’d run in cloud”
- [ ] Personal deploy + destroy runbook written
- [ ] Ready for orchestration concepts in [18-kubernetes.md](18-kubernetes.md)
