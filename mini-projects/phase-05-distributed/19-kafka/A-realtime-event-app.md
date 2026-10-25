# Mini-project A — Real-time Event Application

**Topic:** [19 Kafka](../../../phase-05-distributed/19-kafka.md) · **Phase 5**

---

## Goal

Run Kafka via Docker Compose; produce an event from a Spring Boot HTTP API (`KafkaTemplate`); consume with `@KafkaListener`; document topic + JSON contract; demo HTTP trigger → consumer effect within seconds; note at-least-once + event id idempotency.

---

## Prerequisites

- Docker Compose
- Spring Boot + `spring-kafka`
- Topic 19 producer/consumer basics

**Pick one scenario**

| App | Producer | Consumer |
|-----|----------|----------|
| Order pulse | REST place order → `order-placed` | Inventory / notification logger |
| Live inventory | Stock adjust → `stock-changed` | In-memory dashboard view |
| Activity feed | User action → `user-events` | Analytics counter |

---

## Learn

| Resource | Why |
|----------|-----|
| [Kafka Intro](https://kafka.apache.org/intro) | Topics, partitions, consumer groups |
| [Spring for Apache Kafka](https://docs.spring.io/spring-kafka/reference/) | Template + listeners |
| Bitnami/Apache Kafka Compose docs | Local broker |

---

## Project layout

```text
realtime-event-app/
  compose.yaml              # Kafka (+ Zookeeper or KRaft per image docs)
  producer-api/             # Spring Boot HTTP → KafkaTemplate
  consumer-svc/             # @KafkaListener (same app OK for learning)
  contracts/
    order-placed-v1.json    # example event schema notes
  README.md
  NOTES.md                  # at-least-once + eventId
```

---

## Build steps

- [ ] **1. Kafka Compose**
  1. Add Kafka (and ZooKeeper if required by chosen image) to `compose.yaml`.
  2. Publish `9092` to host; set advertised listeners for Docker + host clients carefully.
  3. `docker compose up -d`
  4. **Done when:** Broker accepts connections (see logs healthy).

- [ ] **2. Create topic**
  1. Example: `docker compose exec kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic order-placed --partitions 3 --replication-factor 1`
  2. (Adjust script path/binary to your image.)
  3. **Done when:** `kafka-topics.sh --list` shows `order-placed`.

- [ ] **3. Event contract**
  1. JSON fields: `eventId`, `occurredAt`, payload (`orderId`, `bookId`, `qty`, …), `version`.
  2. Save in `contracts/`.
  3. **Done when:** Producer and consumer share the same shape.

- [ ] **4. Producer API**
  1. `POST /api/orders` (or chosen trigger) builds event with UUID `eventId`.
  2. `KafkaTemplate.send("order-placed", key, json)` — key by `orderId` if ordering matters.
  3. Config: `spring.kafka.bootstrap-servers=localhost:9092` (host) or `kafka:9092` (in Compose network).
  4. **Done when:** HTTP 201 and message visible via console consumer.

- [ ] **5. Console consumer proof**
  1. `kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order-placed --from-beginning`
  2. **Done when:** JSON appears after one HTTP call.

- [ ] **6. Spring `@KafkaListener`**
  1. Consumer group `inventory-projection` (example).
  2. On message: log + update in-memory map / DB row.
  3. **Done when:** Second HTTP call updates projection within seconds.

- [ ] **7. Idempotency note**
  1. Store processed `eventId` (set/table); skip duplicates.
  2. Document at-least-once: redelivery possible → need idempotency.
  3. **Done when:** NOTES explain duplicate handling.

- [ ] **8. (Should) DLT**
  1. Configure Dead Letter Topic for deserialization/handler failures.
  2. **Done when:** Poison message lands on DLT or explicitly deferred.

- [ ] **9. README demo**
  1. `compose up`, run apps, curl, show consumer log.
  2. **Done when:** Demo reproducible cold.

---

## Definition of Done

- [ ] Kafka via Docker Compose
- [ ] `KafkaTemplate` producer + `@KafkaListener` consumer
- [ ] Topic + JSON contract documented
- [ ] HTTP trigger → consumer effect in seconds
- [ ] Notes on at-least-once + event id

---

## Demo

```bash
docker compose up -d
# create topic if not auto-created
curl -s -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"quantity":2}'
# Watch consumer logs / projection endpoint
curl -s http://localhost:8081/api/projections/inventory
docker compose exec kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 --topic order-placed --from-beginning --max-messages 1
```

---

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Wrong `bootstrap-servers` from containers | Use Compose service hostname inside network |
| No message key when order matters | Key by entity id |
| Offset commit before side effect | Process then ack (default Spring patterns awareness) |
| Undocumented schema | Version field + contract file |

---

## After this

- [B-consumer-group-lab.md](./B-consumer-group-lab.md)
- Stretch: [C-outbox-spike.md](./C-outbox-spike.md)
