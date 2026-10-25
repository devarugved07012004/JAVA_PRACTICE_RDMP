# Mini-Project B — Inventory Lite (SQL-only)
**Phase:** 2 — Data  
**Topic:** [07 — SQL & PostgreSQL](../../../phase-02-data/07-sql-postgresql.md)  
**Time:** ~4–6 hours

## Goal

Model a tiny multi-warehouse inventory in pure SQL: products, warehouses, stock levels, a low-stock report, a transactional stock transfer, and a value view.

## Prerequisites

- [ ] Mini-project A complete (or equivalent comfort with schema scripts + joins)
- [ ] Topic 07 Day 19 transactions practiced (`BEGIN` / `COMMIT` / `ROLLBACK`)
- [ ] Separate DB or schema recommended: `inventory_lite`

## Learn (open before coding)

- [Transactions tutorial](https://www.postgresql.org/docs/current/tutorial-transactions.html)
- [CHECK constraints](https://www.postgresql.org/docs/current/ddl-constraints.html)
- [CREATE VIEW](https://www.postgresql.org/docs/current/sql-createview.html)
- [UPDATE](https://www.postgresql.org/docs/current/sql-update.html)
- [Aggregates](https://www.postgresql.org/docs/current/tutorial-agg.html)

## Project layout

```text
inventory-lite-sql/
  README.md
  01_schema.sql
  02_seed.sql
  03_queries.sql
  04_transfer_demo.sql
  05_views.sql
```

## Build steps

### Step 1 — Database & entities sketch
- [ ] `CREATE DATABASE inventory_lite;` then `\c inventory_lite`
- [ ] Sketch ER: `products` 1—N `stock_levels` N—1 `warehouses`
- [ ] Create folder + empty scripts; README lists script order
- **Done when:** DB exists and ER notes name the unique key `(product_id, warehouse_id)`.

### Step 2 — Schema with constraints
- [ ] Write `01_schema.sql`:
  ```sql
  DROP VIEW IF EXISTS v_inventory_value;
  DROP TABLE IF EXISTS stock_levels;
  DROP TABLE IF EXISTS products;
  DROP TABLE IF EXISTS warehouses;

  CREATE TABLE products (
    id          BIGSERIAL PRIMARY KEY,
    sku         VARCHAR(40) NOT NULL UNIQUE,
    name        VARCHAR(200) NOT NULL,
    unit_price  NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0)
  );

  CREATE TABLE warehouses (
    id       BIGSERIAL PRIMARY KEY,
    code     VARCHAR(20) NOT NULL UNIQUE,
    name     VARCHAR(100) NOT NULL,
    city     VARCHAR(100)
  );

  CREATE TABLE stock_levels (
    id            BIGSERIAL PRIMARY KEY,
    product_id    BIGINT NOT NULL REFERENCES products(id),
    warehouse_id  BIGINT NOT NULL REFERENCES warehouses(id),
    qty           INT NOT NULL CHECK (qty >= 0),
    UNIQUE (product_id, warehouse_id)
  );
  ```
- [ ] Apply with `psql -U postgres -d inventory_lite -f 01_schema.sql`
- [ ] Prove: insert `qty = -1` fails; duplicate (product, warehouse) fails
- **Done when:** Constraints reject bad qty and duplicate pairs.

### Step 3 — Seed realistic rows
- [ ] `02_seed.sql`: ≥8 products, ≥3 warehouses, stock rows covering most product×warehouse pairs
- [ ] Leave at least two rows with `qty` below a low-stock threshold you will use (e.g. `< 10`)
- [ ] Include one product present in only one warehouse
- [ ] Verify counts after `-f 02_seed.sql`
- **Done when:** Seed loads and you know which SKUs are “low stock.”

### Step 4 — Low-stock report
- [ ] In `03_queries.sql`:
  ```sql
  -- Low stock: qty below threshold
  SELECT p.sku, p.name, w.code AS warehouse, s.qty
  FROM stock_levels s
  JOIN products p   ON p.id = s.product_id
  JOIN warehouses w ON w.id = s.warehouse_id
  WHERE s.qty < 10
  ORDER BY s.qty ASC, p.sku;
  ```
- [ ] Add a second query: total units per product across all warehouses (`SUM(qty)`)
- [ ] Add a third: warehouses with zero rows for a given product (`LEFT JOIN` / `NOT EXISTS`)
- **Done when:** Low-stock list matches the seed rows you planted.

### Step 5 — Inventory value view
- [ ] In `05_views.sql`:
  ```sql
  CREATE OR REPLACE VIEW v_inventory_value AS
  SELECT p.sku,
         p.name,
         w.code AS warehouse,
         s.qty,
         p.unit_price,
         (s.qty * p.unit_price) AS line_value
  FROM stock_levels s
  JOIN products p   ON p.id = s.product_id
  JOIN warehouses w ON w.id = s.warehouse_id;
  ```
- [ ] Query: `SELECT warehouse, SUM(line_value) FROM v_inventory_value GROUP BY warehouse;`
- [ ] Spot-check one line: `qty * unit_price` by hand
- **Done when:** View exists (`\dv`) and warehouse totals look correct.

### Step 6 — Transfer inside a transaction (happy path)
- [ ] In `04_transfer_demo.sql`, document a transfer of `N` units from warehouse A → B for one SKU:
  ```sql
  BEGIN;

  UPDATE stock_levels
  SET qty = qty - 5
  WHERE product_id = (SELECT id FROM products WHERE sku = 'SKU-001')
    AND warehouse_id = (SELECT id FROM warehouses WHERE code = 'WH-A')
    AND qty >= 5;

  UPDATE stock_levels
  SET qty = qty + 5
  WHERE product_id = (SELECT id FROM products WHERE sku = 'SKU-001')
    AND warehouse_id = (SELECT id FROM warehouses WHERE code = 'WH-B');

  -- optional: INSERT if destination row missing (advanced)

  COMMIT;
  ```
- [ ] Record before/after qty with `SELECT` around the TX
- **Done when:** Total units for the SKU across warehouses are unchanged; A down 5, B up 5.

### Step 7 — Transfer rollback path
- [ ] Start a transfer that would drive source `qty` negative (omit the `qty >= N` guard once on purpose)
- [ ] Or: `BEGIN;` update source; `ROLLBACK;`
- [ ] Confirm counts unchanged after rollback
- [ ] Restore the safe version with `AND qty >= N` (or `CHECK` already prevents negative — note which layer caught it)
- **Done when:** You demonstrated rollback (or CHECK abort) leaving DB consistent.

### Step 8 — Upsert stock row (optional polish)
- [ ] Practice receiving stock with:
  ```sql
  INSERT INTO stock_levels (product_id, warehouse_id, qty)
  VALUES (1, 1, 20)
  ON CONFLICT (product_id, warehouse_id)
  DO UPDATE SET qty = stock_levels.qty + EXCLUDED.qty;
  ```
- [ ] Run twice; confirm qty increases, still one row
- **Done when:** Upsert is idempotent on the unique pair.

### Step 9 — Fresh-run & README
- [ ] Recreate DB from scripts in order: `01` → `02` → `05` → `03` → `04`
- [ ] README: create DB, run order, low-stock threshold, how to demo transfer + rollback
- **Done when:** Clean replay works; README alone is enough to demo.

## Definition of Done

- [ ] Tables `products`, `warehouses`, `stock_levels` with non-negative qty + unique (product, warehouse)
- [ ] Seed data supports a visible low-stock report
- [ ] View `v_inventory_value` = qty × unit_price
- [ ] Stock transfer demonstrated inside `BEGIN`/`COMMIT`
- [ ] Rollback (or constraint abort) path verified
- [ ] Scripts + README runnable from scratch

## Demo script

1. Apply schema + seed + view
2. Show low-stock query results
3. `SELECT * FROM v_inventory_value ORDER BY line_value DESC LIMIT 5;`
4. Run transfer TX; show before/after qty for one SKU
5. Show a rolled-back attempt leaving totals unchanged

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Transfer as two auto-committed updates | Wrap both in one transaction |
| Allowing negative qty | `CHECK (qty >= 0)` + guard in UPDATE |
| Value view without join to price | Always join `products.unit_price` |
| Transferring more than available | `AND qty >= N` or check `FOUND`/rowcount |
| Mixing inventory into campus_registry DB | Prefer dedicated `inventory_lite` DB |

## After this

- Optional stretch: [C — Audit-ready grades](C-audit-ready-grades.md)
- Return to topic 07 mastery checklist; then start [08 JDBC](../../../phase-02-data/08-jdbc.md)
