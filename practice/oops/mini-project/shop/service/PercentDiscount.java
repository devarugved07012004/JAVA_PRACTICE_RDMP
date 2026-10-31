public class PercentDiscount implements DiscountPolicy {
    private final double percent;

    public PercentDiscount(double percent) {
        if (percent < 0 || percent > 100)
            throw new IllegalArgumentException("percent must be 0–100");
        this.percent = percent;
    }

    @Override
    public double apply(double subtotal, Order order) {
        return subtotal * (1 - percent / 100.0);
    }

    public double getPercent() {
        return percent;
    }
}
