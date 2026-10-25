# Mini-Project B — Mini Shop (OOP + DiscountPolicy)

**Phase:** 1 Core Java  
**Topic:** [../../phase-01-core-java/02-oops.md](../../phase-01-core-java/02-oops.md)  
**Code folder suggestion:** `projects/phase-01/02-oops/mini-shop/`  
**Time:** ~8–10 hours at 1 hr/day  

## Goal

Build a console shop with `Product`, `Customer`, `Order`, and `OrderService`. Apply discounts through a strategy-like `DiscountPolicy` interface (polymorphic discount calculation). Thin menu UI; in-memory only.

## Prerequisites

- [ ] OOP encapsulation + interfaces (Week 2–3)
- [ ] Mini Library recommended first; this is the alternative/companion project

## Learn (open before coding)

- [Interfaces](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html)
- [Polymorphism](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html)
- [Classes & Objects](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html)
- [Defining Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
- [Access Control](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html)

## Project layout

```text
mini-shop/
  README.md
  src/
    com/learn/shop/
      app/
        Main.java
      model/
        Product.java
        Customer.java
        Order.java
        OrderLine.java
      service/
        OrderService.java
        DiscountPolicy.java
        NoDiscount.java
        PercentDiscount.java
        BulkDiscount.java
```

## Build steps

### Step 1 — Models with encapsulation

- [ ] `Product`: id, name, unitPrice (> 0), stock (≥ 0)
- [ ] `Customer`: id, name, email
- [ ] Validate in constructors; private fields
- [ ] Compile stub `Main`
  ```bash
  javac -d out src/com/learn/shop/model/*.java src/com/learn/shop/app/Main.java
  java -cp out com.learn.shop.app.Main
  ```
- **Done when:** Invalid price/stock rejected; valid products construct.

### Step 2 — Order & line items (composition)

- [ ] `OrderLine`: productId, quantity, unitPrice snapshot
- [ ] `Order`: orderId, customerId, list/array of lines, status (OPEN/PAID)
- [ ] Order “has” lines (composition)—lines don’t exist without the order conceptually
- **Done when:** You can build an order with 2 lines in code.

### Step 3 — Catalog in `OrderService`

- [ ] Service holds products + customers + orders
- [ ] `addProduct`, `registerCustomer`, `listProducts`
- [ ] Menu wiring for catalog ops
- **Done when:** Add 3 products via menu; list shows them.

### Step 4 — Place order (stock rules)

- [ ] `placeOrder(customerId, productIdQty pairs)`:
  - Validate customer exists
  - Validate stock ≥ qty; decrement stock
  - Create `Order` with line snapshots
- [ ] Reject oversell with clear message
- **Done when:** Ordering 2 of stock 1 fails; valid order reduces stock.

### Step 5 — `DiscountPolicy` interface

- [ ] `interface DiscountPolicy { double apply(double subtotal, Order order); }`
- [ ] Implementations:
  - `NoDiscount` → return subtotal
  - `PercentDiscount(percent)` → e.g. 10% off
  - `BulkDiscount` → if total qty ≥ N, apply percent or flat off
- [ ] Service field `DiscountPolicy policy` settable via constructor or setter
- **Done when:** Same order subtotal yields different totals under two policies.

### Step 6 — Polymorphic checkout

- [ ] `checkout(orderId)` computes subtotal → `policy.apply(...)` → store/print total
- [ ] Call site uses `DiscountPolicy` reference only (no `instanceof` for apply)
- [ ] Menu: choose policy (None / Percent / Bulk) before checkout
- **Done when:** Switching policy changes payable amount without rewriting checkout logic.

### Step 7 — List orders & harden

- [ ] List orders with customer, lines, subtotal, discount total
- [ ] Handle missing ids, qty ≤ 0, empty cart
- [ ] Keep `Main` free of pricing formulas
- **Done when:** Bad input doesn’t crash; happy path end-to-end works.

### Step 8 — README & demo data

- [ ] Seed 3 products + 1 customer on startup
- [ ] Document compile/run + discount examples
- [ ] Note: money as `double` is OK for learning; mention `BigDecimal` for later
- **Done when:** Fresh run demos discount switch in <2 minutes.

## Definition of Done

- [ ] Product / Customer / Order (+ lines) modeled with encapsulation
- [ ] Stock-safe place order
- [ ] `DiscountPolicy` with ≥2 implementations used polymorphically
- [ ] Thin UI; service owns rules
- [ ] README verified

## Demo script

1. List products (seeded)
2. Place order: 2× widget
3. Set PercentDiscount 10%
4. Checkout → total = 90% of subtotal
5. New order with BulkDiscount → show different rule firing
6. Attempt oversell → rejected

## Common mistakes

| Mistake | Fix |
|--------|-----|
| Discount `if/else` on string codes in checkout | Use interface implementations |
| Mutating product price on the line incorrectly | Snapshot unit price on line |
| Negative stock | Validate before decrement |
| UI computes totals | Service + policy only |
| God `OrderService` + UI mixed | Split packages |

## After this

Return to [02-oops.md](../../phase-01-core-java/02-oops.md) §12 · Next: [03-collections.md](../../phase-01-core-java/03-collections.md) · Or deepen with [A — Mini Library](A-mini-library-domain.md)
