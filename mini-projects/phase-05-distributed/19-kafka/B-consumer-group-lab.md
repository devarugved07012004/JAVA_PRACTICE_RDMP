# Mini-project B — Consumer Group Lab

**Topic:** [19 Kafka](../../../phase-05-distributed/19-kafka.md) · **Phase 5**

---

## Goal

Create a topic with **3 partitions**; run **2** consumer instances in the **same group**; kill one; observe **rebalance**; document partition assignment before/after.

---

## Prerequisites

- Mini-project A Kafka Compose up
- Ability to run the Spring consumer twice (two processes or two Compose replicas) **or** use `kafka-console-consumer` with same `--group`

---

## Learn

| Resource | Why |
|----------|-----|
| Kafka consumer groups (official docs) | Partition assignment |
| Spring Kafka concurrency / multiple instances | Same `groupId` |

---

## Project layout

```text
consumer-group-lab/
  compose.yaml              # reuse A
  scripts/
    produce-burst.sh        # or .ps1 — send keyed messages
  NOTES.md                  # assignments + rebalance timeline
  README.md
```

---

## Build steps

- [ ] **1. Topic with 3 partitions**
  1. `kafka-topics.sh --create --topic lab-events --partitions 3 --replication-factor 1 ...`
  2. Describe topic; confirm partition count.
  3. **Done when:** `--describe` shows 3 partitions.

- [ ] **2. Same groupId**
  1. Two consumer instances with `spring.kafka.consumer.group-id=lab-group` (or console `--group lab-group`).
  2. **Done when:** Both start without separate groups by mistake.

- [ ] **3. Observe initial assignment**
  1. Use logs, Actuator, or `kafka-consumer-groups.sh --describe --group lab-group`.
  2. Record which instance owns which partitions (expect ~2+1 split).
  3. **Done when:** Table in NOTES: instance → partitions.

- [ ] **4. Produce burst**
  1. Send ≥30 messages with varied keys so partitions get traffic.
  2. Example: loop `kafka-console-producer` or HTTP producer.
  3. **Done when:** Both consumers process some messages (logs prove).

- [ ] **5. Kill one consumer**
  1. Stop instance B (Ctrl+C / `docker compose stop consumer-2`).
  2. Watch remaining consumer logs for rebalance.
  3. **Done when:** Rebalance completes; remaining instance owns all 3 (or documented assignment).

- [ ] **6. Describe group again**
  1. `kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group lab-group --describe`
  2. Paste output into NOTES.
  3. **Done when:** Before/after describe outputs saved.

- [ ] **7. Restart second instance**
  1. Start B again; note second rebalance; partitions split again.
  2. **Done when:** NOTES timeline: t0 assign → kill → reassign → restart → reassign.

- [ ] **8. Idle consumer insight**
  1. Optionally start a **4th** consumer; note it may get **zero** partitions (3 partitions, 4 members).
  2. **Done when:** Insight written (members &gt; partitions ⇒ idle).

- [ ] **9. README**
  1. Exact commands for topic, describe, two consumers, kill drill.
  2. **Done when:** Lab reproducible.

---

## Definition of Done

- [ ] 3 partitions; 2 instances same group
- [ ] Kill one; rebalance observed
- [ ] Before/after assignments documented
- [ ] README complete

---

## Demo

```bash
docker compose exec kafka kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic lab-events --partitions 3 --replication-factor 1
# Start consumer instance 1 and 2 with group-id=lab-group
docker compose exec kafka kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
  --group lab-group --describe
# Kill instance 2; describe again; restart; describe again
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Different `group-id`s | Same group for shared work |
| 1 partition topic | Can’t see meaningful split |
| Not waiting for rebalance | Give seconds; watch logs |
| Assuming 4 consumers all busy on 3 partitions | Extra stays idle |

---

## After this

- Stretch: [C-outbox-spike.md](./C-outbox-spike.md)
- Apply group sizing intuition in phase project notification consumers
