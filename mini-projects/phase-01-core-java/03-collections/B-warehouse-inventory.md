# Mini-Project B — Warehouse Inventory

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/03-collections.md](../../phase-01-core-java/03-collections.md)  
**Code folder suggestion:** `projects/phase-01/03-collections/warehouse-inventory/`  
**Time:** ~8–10 hours at 1 hr/day  

## Goal

Track SKU → quantity with `HashMap`, categories → set of SKUs, low-stock suggestions via `PriorityQueue`, and export sorted reports (`TreeMap` or sorted list). Console app; justify each collection choice.

## Prerequisites

- [ ] Maps, Sets, Queues / PriorityQueue from Collections Weeks 2–3
- [ ] Contact Book helpful for multi-structure habits

## Learn (open before coding)

- [Map Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html)
- [Set Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/set.html)
- [Queue Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/queue.html)
- [`PriorityQueue` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html)
- [`TreeMap` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/TreeMap.html)
- [Object Ordering](https://docs.oracle.com/javase/tutorial/collections/interfaces/order.html)

## Project layout

```text
warehouse-inventory/
  README.md
  src/
    com/learn/warehouse/
      app/Main.java
      model/SkuItem.java
      service/InventoryService.java
      service/LowStockComparator.java
```

## Build steps

### Step 1 — Stock map

- [ ] `Map<String, Integer> stockBySku` (`HashMap`)
- [ ] `receive(sku, qty)`, `issue(sku, qty)` with validation (qty > 0; issue ≤ on-hand)
- [ ] `getQty(sku)` default 0 if missing
- [ ] Compile/run stub
  ```bash
  javac -d out src/com/learn/warehouse/**/*.java
  java -cp out com.learn.warehouse.app.Main
  ```
- **Done when:** Receive 10, issue 3 → qty 7; oversell rejected.

### Step 2 — SKU metadata

- [ ] `SkuItem` or parallel maps: name, category, reorderLevel
- [ ] `registerSku(sku, name, category, reorderLevel)`
- [ ] Reject blank sku; reject duplicate register unless intentional upsert (document)
- **Done when:** Register 3 SKUs; list metadata + qty.

### Step 3 — Category → SKUs

- [ ] `Map<String, Set<String>> skusByCategory`
- [ ] On register, add sku to category set (`HashSet`)
- [ ] `listSkusInCategory(category)`
- **Done when:** Category `hardware` lists only its SKUs.

### Step 4 — Low-stock priority queue

- [ ] Define “low stock” as `qty <= reorderLevel`
- [ ] Build `PriorityQueue` of SKUs (or items) ordered by qty ascending (most urgent first)—custom `Comparator`
- [ ] Menu: “Restock suggestions” rebuilds queue from current stock and prints top N
- **Done when:** Lowest qty low-stock SKU prints first.

### Step 5 — Sorted inventory report

- [ ] Export report sorted by SKU: use `TreeMap` copy of stock **or** sort entry list
- [ ] Print sku, name, qty, category
- [ ] Optional: write to console section titled REPORT
- **Done when:** Report order is sorted by SKU code.

### Step 6 — Adjust & category report

- [ ] Menu: receive / issue / register / list category / low-stock / full report / quit
- [ ] Category summary: for each category, count SKUs and total units (nested loops or maps)
- **Done when:** After stock moves, reports reflect new quantities.

### Step 7 — Edge cases

- [ ] Unknown SKU on issue → clear error
- [ ] Empty warehouse report
- [ ] Category with empty set after bad data—don’t NPE
- [ ] Comparator consistency (no qty mutation while iterating queue carelessly—rebuild each time is fine)
- **Done when:** No crashes on empty/missing data.

### Step 8 — README: justify structures

- [ ] Document: HashMap for qty, HashSet for category members, PriorityQueue for urgency, TreeMap/sort for report
- [ ] Seed 6–8 SKUs across 3 categories with mixed stock levels
- [ ] Compile/run + demo script
- **Done when:** Another reader understands *why* each collection was chosen.

## Definition of Done

- [ ] SKU→qty HashMap works with receive/issue
- [ ] Category→Set<SKU> index works
- [ ] Low-stock PriorityQueue suggestions work
- [ ] Sorted report works
- [ ] Structure justifications in README

## Demo script

1. Seed loads mixed stock
2. Issue until one SKU is low
3. Restock suggestions → that SKU near top
4. Full sorted report
5. List category `electronics`
6. Quit

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Using List scan for every qty check | HashMap get |
| PriorityQueue assumed fully sorted on iterate | Poll or document heap behavior; rebuild for display |
| Mutating shared sets returned to UI | Return copy/unmodifiable |
| Negative stock | Validate issue |
| TreeMap for everything “just in case” | Use HashMap when order not needed |

## After this

Next: [C — Task Board](C-task-board.md) · Or mastery gate in [03-collections.md](../../phase-01-core-java/03-collections.md)
