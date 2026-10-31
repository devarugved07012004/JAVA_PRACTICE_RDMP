public class BulkDiscount implements DiscountPolicy {
    private final int minQty;
    private final double percentOff;

    public BulkDiscount() {
        this(5, 20);
    }

    public BulkDiscount(int minQty, double percentOff) {
        if (minQty <= 0)
            throw new IllegalArgumentException("minQty must be > 0");
        if (percentOff < 0 || percentOff > 100)
            throw new IllegalArgumentException("percentOff must be 0–100");
        this.minQty = minQty;
        this.percentOff = percentOff;
    }

    @Override
    public double apply(double subtotal, Order order) {
        int qty = 0;
        for (OrderLine line : order.getLines())
            qty += line.getQuantity();
        if (qty >= minQty)
            return subtotal * (1 - percentOff / 100.0);
        return subtotal;
    }
}
