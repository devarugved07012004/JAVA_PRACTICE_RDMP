# 19 — Apache Kafka

**Duration:** ~3 weeks (21 days) · **Cadence:** 1 hour/day  
**Focus:** Event Driven Architecture, Topics, Producers, Consumers, Partitions, Consumer Groups, Streams (intro), Spring Kafka · Build a real-time event application

---

## 1. Why this topic is important

Synchronous Feign calls couple services in time: if catalog is slow, orders wait. **Events** decouple producers and consumers—order placed, stock reserved, email sent—on their own schedules. Kafka is the industry-standard log for high-throughput event streaming in Java shops.

You will run Kafka locally (Compose), master **topics, partitions, producers, consumers, and consumer groups**, get a taste of **Kafka Streams**, and wire **Spring Kafka** into a small real-time application. Delivery semantics (at-least-once, duplicates, idempotency) become practical, not buzzwords—exactly what the Phase 5 BookStore project needs for order events.

---

## 2. Prerequisites

- Solid Spring Boot REST + messaging willingness
- [16-docker.md](16-docker.md): Compose comfort (recommended way to run Kafka)
- Topic 15 sync microservices helpful but a single Boot app can learn Kafka first
- JDK 17+; Maven/Gradle

**Environment you will set up:**

- Apache Kafka via Docker Compose (KRaft mode preferred in modern images; or ZooKeeper-based older stacks if following a specific guide)
- Optional UI: Kafka UI / similar free container for browsing topics
- Spring Boot app with `spring-kafka` dependency
- CLI tools from Kafka distribution or via container `exec`

---

## 3. Concepts to learn (beginner → intermediate → advanced)

### Beginner

| Concept | What to internalize |
|--------|----------------------|
| Event-driven architecture | Facts that happened; consumers decide reactions |
| Kafka as a distributed log | Append-only; retain; replay |
| Broker / topic / partition | Topic = category; partition = ordered slice |
| Producer | Writes records (key, value, headers) |
| Consumer | Reads from partitions; tracks offsets |
| Offset | Position in a partition; commit strategies |
| Local Compose Kafka | Bootstrap servers URL for clients |

### Intermediate

| Concept | What to internalize |
|--------|----------------------|
| Keys & partitioning | Same key → same partition → ordered per key |
| Consumer groups | Compete for partitions; scale consumers |
| Rebalancing | Members join/leave; pause awareness |
| At-least-once | Default reality; duplicates possible |
| Idempotent producer | Reduce dupes on producer retries |
| Serialization | JSON / String / Avro awareness (JSON fine for learning) |
| Spring Kafka | `@KafkaListener`, `KafkaTemplate`, error handlers |
| Dead-letter patterns | Failed records → DLT topic (intro) |

### Advanced

| Concept | What to internalize |
|--------|----------------------|
| Exactly-once / transactions (awareness) | When and why it’s hard; don’t fake it casually |
| Kafka Streams intro | KStream processing topology; local state idea |
| Compacted topics | Changelog / latest-value-per-key use cases |
| Backpressure & lag | Consumer lag as an ops metric |
| Event design | Past-tense names; payloads; versioning fields |
| Outbox pattern (awareness) | Reliably emit after DB commit—phase project prep |

---

## 4. Daily learning tasks (numbered day blocks)

**How to use:** Open the **Learn** links (10 min) → follow every numbered **Practice** step (35 min) → tick boxes when done.

**Daily template (60 min):** 10 min Learn · 35 min Practice steps · 10 min notes · 5 min mark boxes

### Week 1 — Broker, topics, CLI produce/consume (Days 1–7)

- [ ] **Day 1 — Sync vs async events**
  - [ ] **Learn:** [Kafka Introduction](https://kafka.apache.org/intro) · [Async message-based communication](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/architect-microservice-container-applications/asynchronous-message-based-communication)
  - [ ] **Practice:** Sketch sync vs async place-order and list events
    1. In notes, draw place-order **sync** path: client → order-service → catalog (stock) → response.
    2. Draw **async** path: order commits → publish event → consumers react later.
    3. List at least two events: `OrderPlaced`, `StockUpdated` (add fields you expect: ids, amounts, timestamps).
    4. Write one sentence each: when sync is required vs when async is better.
    5. Note one failure mode unique to async (duplicate, out-of-order, lag).
    6. **Done when:** Both sketches exist and the two events are named with key fields.

- [ ] **Day 2 — Compose Kafka (KRaft)**
  - [ ] **Learn:** [Kafka Quickstart](https://kafka.apache.org/quickstart) · [KRaft](https://kafka.apache.org/documentation/#kraft) · [apache/kafka Docker](https://hub.docker.com/r/apache/kafka)
  - [ ] **Practice:** Bring up Kafka via Compose and verify the broker
    1. Add a Kafka (KRaft) service to `compose.yaml` using an official/community quickstart snippet for Apache Kafka Docker.
    2. Run `docker compose up -d` for Kafka (and deps if any).
    3. Confirm the container is healthy: `docker compose ps` and `docker compose logs kafka` (or your service name).
    4. Note bootstrap servers for the host as `localhost:9092` (or the port you mapped) in `NOTES.md`.
    5. From a shell into the container (or host CLI if installed), run a broker API version / cluster id check if available.
    6. **Done when:** Kafka is up and you recorded the bootstrap address you will use all week.

- [ ] **Day 3 — Create topic**
  - [ ] **Learn:** [Kafka documentation (topics/partitions)](https://kafka.apache.org/documentation/)
  - [ ] **Practice:** Create and describe `demo-orders`
    1. Exec into the Kafka container (or use mounted scripts).
    2. Create topic: `kafka-topics.sh --bootstrap-server localhost:9092 --create --topic demo-orders --partitions 3 --replication-factor 1` (adjust script path/binary for your image).
    3. Describe: `kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic demo-orders`.
    4. Confirm partitions ≥ 3 in the describe output.
    5. List topics and confirm `demo-orders` appears.
    6. **Done when:** Describe shows `demo-orders` with at least 3 partitions.

- [ ] **Day 4 — Console produce/consume**
  - [ ] **Learn:** [Quickstart](https://kafka.apache.org/quickstart) (producer/consumer CLI)
  - [ ] **Practice:** Send and read messages with console tools
    1. Start a console consumer on `demo-orders` from earliest (or a fresh group).
    2. In a second terminal, start a console producer for `demo-orders`.
    3. Send 5 plain messages; confirm all appear on the consumer.
    4. Send 3 more to the same partition (no key / same partition) and note order preserved within that partition.
    5. Stop producer/consumer cleanly; paste 2 sample messages into notes.
    6. **Done when:** You produced ≥5 messages and saw them consumed in partition order.

- [ ] **Day 5 — Keys & partitioning**
  - [ ] **Learn:** [Producer API](https://kafka.apache.org/documentation/#producerapi)
  - [ ] **Practice:** Prove same key maps to one partition
    1. Produce messages with key `user-42` (console producer key support or a tiny Java producer).
    2. Produce several values with the same key; describe/consume and note partition id.
    3. Produce with a different key `user-99`; note a possibly different partition.
    4. Write the rule: “same key → same partition (for a fixed partition count).”
    5. Explain in notes why keys matter for per-user ordering.
    6. **Done when:** Notes show same-key messages landed on one partition.

- [ ] **Day 6 — Consumer groups**
  - [ ] **Learn:** [Consumer API](https://kafka.apache.org/documentation/#consumerapi)
  - [ ] **Practice:** Competing consumers vs fan-out
    1. Start two console consumers with the **same** `--group demo-cg` on `demo-orders`.
    2. Produce 10 messages; observe each message goes to only one consumer (competing).
    3. Stop them; start two consumers with **different** group ids.
    4. Produce again; observe both groups receive the messages (fan-out).
    5. Write 3–5 sentences contrasting the two modes.
    6. **Done when:** Notes record competing vs fan-out with the group ids you used.

- [ ] **Day 7 — Partition diagram review**
  - [ ] **Learn:** [Kafka intro](https://kafka.apache.org/intro) (recap)
  - [ ] **Practice:** Draw topic → partitions → consumer group
    1. Draw `demo-orders` with 3 partitions (P0–P2).
    2. Draw consumer group `demo-cg` with 2–3 members and arrows to assigned partitions.
    3. Label producer with key → partition.
    4. Annotate offset per partition briefly.
    5. Explain the diagram aloud without notes.
    6. **Done when:** Diagram exists and you can explain assignment without looking it up.

### Week 2 — Spring Kafka producers & consumers (Days 8–14)

- [ ] **Day 8 — KafkaTemplate**
  - [ ] **Learn:** [Spring for Apache Kafka](https://docs.spring.io/spring-kafka/reference/) · [Boot Kafka auto-config](https://docs.spring.io/spring-boot/reference/messaging/kafka.html)
  - [ ] **Practice:** Send to `demo-orders` with `KafkaTemplate`
    1. Create or open a Spring Boot app; add `spring-kafka` dependency.
    2. Set `spring.kafka.bootstrap-servers=localhost:9092` in `application.yml`.
    3. Inject `KafkaTemplate<String, String>` and send a test message to topic `demo-orders`.
    4. Trigger send from `CommandLineRunner` or a temporary REST endpoint.
    5. Confirm with a console consumer that the message arrived.
    6. **Done when:** A Spring-produced message appears on `demo-orders`.

- [ ] **Day 9 — @KafkaListener**
  - [ ] **Learn:** [Spring Kafka reference](https://docs.spring.io/spring-kafka/reference/)
  - [ ] **Practice:** Consume with `@KafkaListener` and a group id
    1. Add `@KafkaListener(topics = "demo-orders", groupId = "spring-demo")` that logs the payload.
    2. Set `spring.kafka.consumer.group-id` / `auto-offset-reset` as needed for learning.
    3. Produce a few messages (REST or console); confirm logs show each payload.
    4. Restart the app and produce again; note whether offsets advanced (no re-read of old if committed).
    5. Write the group id you used in notes.
    6. **Done when:** Listener logs show consumed messages for group `spring-demo`.

- [ ] **Day 10 — JSON events**
  - [ ] **Learn:** [Spring Kafka serialization](https://docs.spring.io/spring-kafka/reference/)
  - [ ] **Practice:** JSON ser/deser for `OrderPlacedEvent`
    1. Create record/DTO `OrderPlacedEvent` with `eventId`, `orderId`, `userId`, `total`, `occurredAt`.
    2. Configure JsonSerializer / JsonDeserializer (or Spring Boot Json Serde) for value type.
    3. Produce one typed event via `KafkaTemplate<String, OrderPlacedEvent>`.
    4. Consume with `@KafkaListener` into `OrderPlacedEvent` and log fields.
    5. Intentionally send bad JSON once; note the error (fix or document for Day 11).
    6. **Done when:** A typed event round-trips produce → consume as JSON.

- [ ] **Day 11 — Errors & DLT**
  - [ ] **Learn:** [Spring Kafka error handlers](https://docs.spring.io/spring-kafka/reference/)
  - [ ] **Practice:** Failing listener, retry, and a simple DLT
    1. Make the listener throw on a marker payload (e.g. `total < 0`).
    2. Observe default retry behavior in logs (or configure a small retry count).
    3. Configure a Dead Letter Topic / `DefaultErrorHandler` with DLT publish (keep config minimal).
    4. Produce a bad event; confirm it lands on the DLT topic (or is logged as exhausted).
    5. Write 3 sentences: what you would alert on in production.
    6. **Done when:** A failing message is retried and ends in DLT (or documented equivalent).

- [ ] **Day 12 — Scale consumers**
  - [ ] **Learn:** [Consumer groups](https://kafka.apache.org/documentation/#consumerapi)
  - [ ] **Practice:** Two app instances, one `group-id`
    1. Run two instances of the consumer app (different `server.port`) with the **same** `group-id`.
    2. Produce ≥10 keyed/unkeyed messages to `demo-orders` (3 partitions).
    3. Observe logs: partitions split across instances (not both getting every message).
    4. Stop one instance; produce more; note rebalance / remaining instance takes partitions.
    5. Record partition assignment observations in notes.
    6. **Done when:** Notes show two instances sharing work under one group id.

- [ ] **Day 13 — Ack / commit**
  - [ ] **Learn:** [Consumer offsets](https://kafka.apache.org/documentation/#consumerapi) · [Spring Kafka](https://docs.spring.io/spring-kafka/reference/)
  - [ ] **Practice:** Contrast auto vs manual ack (small experiment)
    1. In notes, define auto-commit vs manual ack in your own words.
    2. Configure a listener with `AckMode.RECORD` or `MANUAL` (pick one) and acknowledge after processing.
    3. Throw **before** ack once; restart and confirm the message can be redelivered.
    4. Process successfully and ack; confirm it is not redelivered after restart.
    5. Write the rule: “commit only after side effects succeed” (and the duplicate risk).
    6. **Done when:** Experiment shows redelivery when ack is skipped vs not when committed.

- [ ] **Day 14 — REST → event → projection**
  - [ ] **Learn:** Recap [Spring Kafka](https://docs.spring.io/spring-kafka/reference/) · [Boot Kafka](https://docs.spring.io/spring-boot/reference/messaging/kafka.html)
  - [ ] **Practice:** REST publishes; listener updates an in-memory projection
    1. Add `POST /api/orders/demo` that builds `OrderPlacedEvent` and sends via `KafkaTemplate`.
    2. Listener updates an in-memory `Map`/`List` projection (e.g. orders by id).
    3. Add `GET /api/orders/projection` that returns the projection.
    4. Curl POST then GET; confirm the event is reflected.
    5. Curl POST twice with different ids; confirm both appear.
    6. **Done when:** POST → Kafka → GET projection works end-to-end.

### Week 3 — Streams intro, real-time app, mastery (Days 15–21)

- [ ] **Day 15 — Kafka Streams intro**
  - [ ] **Learn:** [Kafka Streams](https://kafka.apache.org/documentation/streams/) · [Streams core concepts](https://kafka.apache.org/documentation/streams/core-concepts)
  - [ ] **Practice:** Run a word-count or event-count topology
    1. Add Kafka Streams dependency to a Boot/Java module.
    2. Implement a minimal topology: count events on `demo-orders` **or** classic word-count on a text topic.
    3. Write results to an output topic (e.g. `demo-orders-counts`).
    4. Produce input; consume the output topic and show counts increasing.
    5. Note Streams application id in config.
    6. **Done when:** Output topic shows aggregated counts from your topology.

- [ ] **Day 16 — Filter to derived topic**
  - [ ] **Learn:** [Kafka Streams](https://kafka.apache.org/documentation/streams/)
  - [ ] **Practice:** Filter `OrderPlaced` into a derived topic
    1. Create topic `demo-orders-high-value` (1+ partitions).
    2. Streams filter: keep events where `total >=` a threshold (or region match).
    3. Write filtered events to `demo-orders-high-value`.
    4. Produce mixed totals; console-consume the derived topic and confirm only matches appear.
    5. Document the filter rule in README/notes.
    6. **Done when:** Derived topic contains only filtered `OrderPlaced` events.

- [ ] **Day 17 — Mini-project A start**
  - [ ] **Learn:** §6A + [Spring Kafka](https://docs.spring.io/spring-kafka/reference/)
  - [ ] **Practice:** Scaffold the Real-time Event Application (§6A)
    1. Create project folder / modules per §6A (producer + consumer at minimum).
    2. Define the shared event contract class(es) in a shared module or duplicated carefully.
    3. Wire Kafka bootstrap from env/`application.yml`.
    4. Smoke: produce one event; consume and log it.
    5. Write a 5-bullet README of the intended demo flow.
    6. **Done when:** Skeleton runs and one event round-trips with README outline.

- [ ] **Day 18 — Producer + consumer services**
  - [ ] **Learn:** [Spring Kafka project](https://spring.io/projects/spring-kafka)
  - [ ] **Practice:** Finish producer and consumer with shared JSON contract
    1. Producer service: REST or domain action publishes `OrderPlacedEvent` JSON to `demo-orders` (or project topic).
    2. Consumer service/module: listener applies a visible side effect (log + projection/DB row).
    3. Agree field names in a short `events.md` contract.
    4. Run both; curl produce; verify consumer side effect.
    5. Add one negative test note (missing field / bad JSON).
    6. **Done when:** Two sides use the same JSON contract and the demo path works.

- [ ] **Day 19 — Idempotency**
  - [ ] **Learn:** [Kafka intro (delivery semantics)](https://kafka.apache.org/intro)
  - [ ] **Practice:** Deduplicate `OrderPlaced` by `eventId`
    1. Persist processed `eventId` (DB unique constraint or in-memory `Set` for learning).
    2. On consume, skip if `eventId` already processed.
    3. Publish the **same** event twice (same `eventId`).
    4. Confirm side effect applied once (one row / one projection entry).
    5. Document the strategy in notes (at-least-once + idempotent handler).
    6. **Done when:** Duplicate publish does not double-apply the side effect.

- [ ] **Day 20 — Consumer lag**
  - [ ] **Learn:** [Kafka documentation (ops/monitoring skim)](https://kafka.apache.org/documentation/)
  - [ ] **Practice:** Observe lag with a slow consumer
    1. Describe the group: `kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group spring-demo` (adjust names).
    2. Add a deliberate sleep in the listener (e.g. 2s) or pause consumption.
    3. Produce a burst of messages; re-describe the group and note LAG.
    4. Remove the sleep; let lag drain; describe again.
    5. Write what lag means and one mitigation (scale consumers, speed handler, alert).
    6. **Done when:** You captured describe output showing lag rise and fall.

- [ ] **Day 21 — Demo & gate**
  - [ ] **Learn:** Recap [Kafka docs](https://kafka.apache.org/documentation/) · [Spring Kafka](https://docs.spring.io/spring-kafka/reference/)
  - [ ] **Practice:** Demo script, checklist, and Phase project event notes
    1. Write a README demo: compose up → create topics → curl produce → show consumer/projection.
    2. Run the demo once from the README only; fix gaps.
    3. Tick mastery checklist items you can prove.
    4. Draft Phase project notes: topic name `bookstore.orders`, partitions ≥3, `OrderPlacedEvent` fields.
    5. List open questions for the Phase 5 project (outbox, DLT, multi-service).
    6. **Done when:** README demo works and Phase project event design notes exist.

---

## 5. Hands-on exercises

1. Create topic with 1 partition vs 3; compare parallel consumer throughput intuition.
2. Send 100 messages; consume from beginning (`auto.offset.reset=earliest`) in a new group.
3. Change group id; prove replay from earliest/latest settings.
4. Null key vs keyed messages—partition distribution experiment.
5. Produce invalid JSON; observe deserializer failures; fix with error handler.
6. Throw in listener after partial side effect; discuss duplicate risk.
7. Add header `correlationId`; log it on consume.
8. Two listeners on different topics in one app.
9. Streams: count events per product id into a KTable (intro level).
10. Delete consumer group / reset offsets carefully (learning only; know the command exists).
11. Compose: restart Kafka; discuss durability of committed data (retention settings skim).
12. Write event schema guidelines: required fields, version, occurredAt.

---

## 6. Mini-projects

> **Build guides:** [A Realtime Event App](../mini-projects/phase-05-distributed/19-kafka/A-realtime-event-app.md) · [B Consumer Group Lab](../mini-projects/phase-05-distributed/19-kafka/B-consumer-group-lab.md) · [C Outbox Spike](../mini-projects/phase-05-distributed/19-kafka/C-outbox-spike.md) · [Index](../mini-projects/README.md)

### A. Real-time Event Application (Must)

**Scenario ideas (pick one):**

| App | Producer | Consumer(s) |
|-----|----------|-------------|
| Order pulse | REST places order → `order-placed` | Inventory projection; notification logger |
| Live inventory | Stock adjust API → `stock-changed` | Dashboard in-memory view / WebSocket optional |
| Activity feed | User actions → `user-events` | Analytics counter service |

**Must:**

- Kafka via Docker Compose
- Spring `KafkaTemplate` producer
- `@KafkaListener` consumer in same or second service
- Documented topic + JSON event contract
- Demo: trigger via HTTP; see consumer effect within seconds
- Notes on at-least-once and how you avoid double-processing (event id)

**Should:** DLT for poison messages  
**Could:** Kafka Streams aggregate; simple UI poll of projection

### B. Consumer Group Lab

- Three partitions; two instances; kill one; watch rebalance; document observed behavior

### C. (Stretch) Outbox Spike

- Save `Order` + `Outbox` row in one DB transaction; separate publisher polls outbox → Kafka (concept for phase project reliability)

---

## 7. Industry use cases

- **Order / checkout pipelines:** OrderPlaced triggers inventory, email, analytics
- **CQRS / read models:** Events update search indexes and dashboards
- **Integration backbone:** Many systems subscribe without point-to-point coupling
- **Audit & replay:** Reprocess from offset for new consumers
- **Metrics & activity streams:** High-volume append with consumer lag monitoring
- **Microservices choreography:** Services react to events instead of orchestrating every HTTP call

---

## 8. Common mistakes

| Mistake | Fix |
|--------|-----|
| Treating Kafka like a request/response queue only | Design durable events; consumers may be offline |
| No keys when per-entity order matters | Key by `orderId` / `userId` |
| Huge consumer group with more members than partitions | Extra consumers stay idle |
| Ignoring duplicates | Idempotent handlers; unique event ids |
| Blocking forever in listeners | Timeouts; offload heavy work carefully |
| Giant payloads | Keep events reasonably small; store blobs in S3 + reference |
| Undocumented schemas | Version fields; shared contract module or schema registry later |
| Committing offset before side effects succeed | Process then commit (at-least-once friendly) |
| One mega-topic for unrelated events | Clear bounded topic names |
| Skipping local retention/disk notes | Know data isn’t infinite by default settings |

---

## 9. Best free resources

> **Primary use:** Learn links are already on each day in **§4**. Use this table only as a backup index.

| Resource | Use for |
|----------|---------|
| [Apache Kafka Documentation](https://kafka.apache.org/documentation/) | Official source of truth |
| [Kafka Introduction](https://kafka.apache.org/intro) | Conceptual overview |
| [Kafka Quickstart](https://kafka.apache.org/quickstart) | Broker + CLI |
| [Spring for Apache Kafka Reference](https://docs.spring.io/spring-kafka/reference/) | Spring integration |
| [Spring Kafka Tips / guides on spring.io](https://spring.io/projects/spring-kafka) | Project hub |
| [Confluent free Kafka tutorials (optional)](https://developer.confluent.io/learn-kafka/) | Extra free learning (verify free access) |
| [Kafka Streams docs](https://kafka.apache.org/documentation/streams/) | Streams API |
| Docker Hub Bitnami/Apache Kafka images docs | Compose setups (pin a guide and versions) |

---

## 10. Documentation links (real URLs)

> Day-specific links live in **§4**. Full index:

- Apache Kafka home: https://kafka.apache.org/
- Introduction: https://kafka.apache.org/intro
- Documentation: https://kafka.apache.org/documentation/
- Quickstart: https://kafka.apache.org/quickstart
- Producer basics: https://kafka.apache.org/documentation/#producerapi
- Consumer basics: https://kafka.apache.org/documentation/#consumerapi
- Kafka Streams: https://kafka.apache.org/documentation/streams/
- Streams core concepts: https://kafka.apache.org/documentation/streams/core-concepts
- Spring Kafka reference: https://docs.spring.io/spring-kafka/reference/
- Spring Kafka project: https://spring.io/projects/spring-kafka
- Spring Boot Kafka auto-config: https://docs.spring.io/spring-boot/reference/messaging/kafka.html
- Kafka Docker official images (Apache): https://hub.docker.com/r/apache/kafka
- KRaft mode notes in Kafka docs: https://kafka.apache.org/documentation/#kraft
- Event-driven microservices (Microsoft Learn, free architecture ideas): https://learn.microsoft.com/en-us/dotnet/architecture/microservices/architect-microservice-container-applications/asynchronous-message-based-communication

---

## 11. Practice roadmap

| Stage | Goal | Exit criteria |
|-------|------|----------------|
| 1. Broker | Compose Kafka up | CLI produce/consume works |
| 2. Partitions & groups | Keying + competing consumers | Diagram explained |
| 3. Spring produce | KafkaTemplate from REST | Message in topic |
| 4. Spring consume | Listener side effect | End-to-end demo |
| 5. Reliability | Dupes + DLT notes | Idempotency strategy written |
| 6. Streams taste | One topology | Derived topic or count |
| 7. Gate | Mini-project A | Checklist ≥90% |

**Suggested topics for BookStore prep:**

| Topic | Event | Key |
|-------|-------|-----|
| `bookstore.orders` | `OrderPlaced` | `orderId` |
| `bookstore.inventory` | `StockReserved` / `StockReleased` | `bookId` |

**Weekly rhythm:** Week 1 CLI fluency → Week 2 Spring → Week 3 application + Streams intro.

---

## 12. Mastery checklist

- [ ] Event-driven vs request/response trade-offs explained in plain language
- [ ] Topics, producers, consumers understood hands-on
- [ ] Local broker running (Compose or equivalent)
- [ ] Partitions and keys: can explain ordering guarantees
- [ ] Consumer groups: competing consumers demonstrated
- [ ] Java/Spring producer + consumer demo working
- [ ] At-least-once / duplicate handling awareness with a concrete mitigation
- [ ] One domain event flows through the system end-to-end
- [ ] JSON event contract documented
- [ ] Kafka Streams intro exercised (even a small topology)
- [ ] Consumer lag / ops basic commands tried
- [ ] Real-time event mini-project completed
- [ ] Ready to integrate Kafka order events in [phase-project.md](phase-project.md)
