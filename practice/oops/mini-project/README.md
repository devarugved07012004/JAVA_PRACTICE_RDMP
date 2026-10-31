# Mini Shop

Console shop practicing OOP encapsulation and a strategy-style `DiscountPolicy`.

> Money uses `double` for learning only. Prefer `BigDecimal` for real money.

## Layout

```text
shop/
  app/Main.java
  model/Product.java, Customer.java, Order.java, OrderLine.java
  service/OrderService.java, DiscountPolicy.java,
         NoDiscount.java, PercentDiscount.java, BulkDiscount.java
```

## Compile & run

From `practice/oops/mini-project`:

```powershell
javac -d out shop\app\*.java shop\model\*.java shop\service\*.java
java -cp out Main
```

## Seed data

On startup:

| Id | Product   | Price | Stock |
|----|-----------|-------|-------|
| 1  | Widget    | 50.00 | 10    |
| 2  | Gadget    | 30.00 | 20    |
| 3  | Doohickey | 15.00 | 50    |

Customer: Ada (`id=1`)

## Discount examples

| Policy | Rule |
|--------|------|
| None | Pay full subtotal |
| Percent 10 | Pay 90% of subtotal |
| Bulk | If total qty ≥ 5, pay 80% of subtotal |

### Quick demo

1. List products (`1`)
2. Place order: customer `1`, items `1 2` → 2× Widget, subtotal `100`
3. Set PercentDiscount 10% (`6` → `2` → `10`)
4. Checkout order `1` → payable `90`
5. Place order with qty ≥ 5 (e.g. `3 5`), set Bulk, checkout → 20% off
6. Try oversell (e.g. Widget qty `999`) → rejected
