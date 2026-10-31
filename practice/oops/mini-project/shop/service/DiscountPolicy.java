public interface DiscountPolicy {
    double apply(double subtotal, Order order);
}
