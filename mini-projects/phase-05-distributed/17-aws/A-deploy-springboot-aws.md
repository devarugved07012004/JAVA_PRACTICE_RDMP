# Mini-project A — Deploy Spring Boot to AWS

**Topic:** [17 AWS](../../../phase-05-distributed/17-aws.md) · **Phase 5**

---

## Goal

Deploy a Spring Boot API to **EC2** with **RDS PostgreSQL**, one **S3** upload/download path, an **IAM instance role** (no long-lived keys on the box for S3), and a README that includes **destroy** steps. Stay free-tier-minded.

---

## Prerequisites

- AWS account with billing alerts / budget
- IAM user (not root) + MFA recommended
- Spring Boot JAR that talks to Postgres + optional S3
- Topic 17 EC2/RDS/S3/IAM basics
- AWS CLI configured: `aws configure` (or SSO)

---

## Learn

| Resource | Why |
|----------|-----|
| [EC2 User Guide](https://docs.aws.amazon.com/ec2/) | Instances, SG |
| [RDS User Guide](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Welcome.html) | Postgres |
| [S3 User Guide](https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html) | Objects |
| [IAM roles for EC2](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_switch-role-ec2.html) | Instance profile |

---

## Project layout

```text
deploy-springboot-aws/
  app/                      # Spring Boot project
  infra/
    notes-sg.md             # security group rules
    notes-rds.md
  scripts/
    deploy.sh               # or .ps1 — scp/systemd helpers
    destroy-checklist.md
  README.md                 # create → configure → deploy → destroy
```

**Architecture**

```text
Internet → EC2 (Spring Boot :8080)
              ↘ RDS PostgreSQL
              ↘ S3 (uploads)
IAM instance role → S3
CloudWatch → optional basic alarm
```

---

## Build steps

- [ ] **1. Guardrails**
  1. Create budget/alert; pick **one** region (e.g. `us-east-1`).
  2. Confirm free-tier-minded sizes (`t2.micro`/`t3.micro`, small RDS).
  3. **Done when:** Budget alert exists; region noted in README.

- [ ] **2. S3 bucket**
  1. `aws s3 mb s3://bookstore-lab-<unique> --region <region>`
  2. Block Public Access ON; no public ACLs.
  3. **Done when:** `aws s3 ls` shows bucket; public access blocked.

- [ ] **3. IAM role for EC2**
  1. Role with trust `ec2.amazonaws.com`; policy allowing `s3:PutObject`, `GetObject`, `ListBucket` on that bucket only.
  2. Create instance profile; attach to EC2 later.
  3. **Done when:** Role ARN noted; **no** access keys created for the app.

- [ ] **4. RDS Postgres**
  1. Create Postgres instance (free-tier class if eligible); strong password in password manager (not Git).
  2. Security group: inbound `5432` **only** from EC2 SG (not `0.0.0.0/0`).
  3. Prefer not publicly accessible if you can reach via EC2.
  4. **Done when:** From EC2 later, `psql` or app connects; world cannot.

- [ ] **5. EC2 + security group**
  1. Launch Amazon Linux 2023 (or Ubuntu) micro; attach IAM instance profile.
  2. SG: SSH `22` from **your IP only**; `8080` from your IP (or temporary for demo).
  3. Install JDK 17+: e.g. `sudo dnf install java-17-amazon-corretto -y`
  4. **Done when:** `ssh` works; `java -version` OK; role visible via `curl http://169.254.169.254/latest/meta-data/iam/security-credentials/`

- [ ] **6. Deploy app**
  1. Build JAR locally: `mvn -DskipTests package`
  2. Copy to EC2: `scp target/*.jar ec2-user@<host>:~/app.jar`
  3. Env vars / systemd unit: `SPRING_DATASOURCE_URL`, username/password, `AWS_REGION`, bucket name.
  4. Start: `java -jar app.jar` or systemd `Restart=on-failure`.
  5. **Done when:** `curl http://<ec2-public-dns>:8080/actuator/health` → UP.

- [ ] **7. S3 path**
  1. Endpoint that uploads a small file to S3 and downloads/lists it.
  2. Prove credentials come from instance role (no keys in env for S3).
  3. **Done when:** Object appears in console/`aws s3 ls` after API call.

- [ ] **8. (Should) CloudWatch alarm**
  1. Basic alarm on CPU or status check failed → SNS email optional.
  2. **Done when:** Alarm exists or explicitly skipped with note.

- [ ] **9. Runbook README**
  1. Create, configure env, deploy, smoke test, **destroy** sections.
  2. **Done when:** Destroy checklist is copy-pasteable.

- [ ] **10. Destroy**
  1. Terminate EC2; delete RDS (skip final snapshot for lab if acceptable); empty + delete S3 bucket; delete unused SG/roles if dedicated.
  2. `aws ce get-cost-and-usage` or console billing skim next day.
  3. **Done when:** Resources gone; NOTES timestamp the teardown.

---

## Definition of Done

- [ ] App HTTP-reachable from your machine
- [ ] RDS is primary datastore
- [ ] At least one S3 upload/download path
- [ ] IAM role used for S3 (no long-lived keys on instance)
- [ ] README create + destroy
- [ ] Free-tier-minded sizes

---

## Demo

```bash
# From your machine
curl -s http://<ec2-dns>:8080/actuator/health
curl -s -F "file=@./hello.txt" http://<ec2-dns>:8080/api/files
aws s3 ls s3://bookstore-lab-<unique>/

# Destroy (example — adjust names)
aws ec2 terminate-instances --instance-ids i-...
aws rds delete-db-instance --db-instance-identifier ... --skip-final-snapshot
aws s3 rb s3://bookstore-lab-<unique> --force
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| `0.0.0.0/0` on SSH | Your IP /22 only |
| Public RDS to world | SG from app SG only |
| Access keys on disk | Instance role |
| Leaving resources on | Destroy checklist + calendar |
| Secrets in Git | `.env` / SSM; `.gitignore` |

---

## After this

- Alternate path: [B-elastic-beanstalk-deploy.md](./B-elastic-beanstalk-deploy.md)
- Stretch diagram: [C-vpc-design-sketch.md](./C-vpc-design-sketch.md)
