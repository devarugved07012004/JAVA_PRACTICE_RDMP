public class NoDiscount implements DiscountPolicy {
    @Override
    public double apply(double subtotal, Order order) {
        return subtotal;
    }
}
