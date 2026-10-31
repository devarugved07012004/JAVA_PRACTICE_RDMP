import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final Map<Integer, Product> products = new LinkedHashMap<>();
    private final Map<Integer, Customer> customers = new LinkedHashMap<>();
    private final Map<Integer, Order> orders = new LinkedHashMap<>();
    private DiscountPolicy policy = new NoDiscount();
    private int nextOrderId = 1;

    public void setPolicy(DiscountPolicy policy) {
        if (policy == null)
            throw new IllegalArgumentException("policy required");
        this.policy = policy;
    }

    public DiscountPolicy getPolicy() {
        return policy;
    }

    public void addProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("product required");
        if (products.containsKey(product.getId()))
            throw new IllegalArgumentException("product id already exists: " + product.getId());
        products.put(product.getId(), product);
    }

    public void registerCustomer(Customer customer) {
        if (customer == null)
            throw new IllegalArgumentException("customer required");
        if (customers.containsKey(customer.getId()))
            throw new IllegalArgumentException("customer id already exists: " + customer.getId());
        customers.put(customer.getId(), customer);
    }

    public Collection<Product> listProducts() {
        return products.values();
    }

    public Collection<Customer> listCustomers() {
        return customers.values();
    }

    public Collection<Order> listOrders() {
        return orders.values();
    }

    public Product findProduct(int productId) {
        return products.get(productId);
    }

    public Customer findCustomer(int customerId) {
        return customers.get(customerId);
    }

    public Order findOrder(int orderId) {
        return orders.get(orderId);
    }

    /**
     * @param productIdQtyPairs even-length array: productId, qty, productId, qty, ...
     */
    public Order placeOrder(int customerId, int... productIdQtyPairs) {
        if (!customers.containsKey(customerId))
            throw new IllegalArgumentException("unknown customer: " + customerId);
        if (productIdQtyPairs == null || productIdQtyPairs.length == 0)
            throw new IllegalArgumentException("cart is empty");
        if (productIdQtyPairs.length % 2 != 0)
            throw new IllegalArgumentException("pairs required: productId, qty, ...");

        List<int[]> items = new ArrayList<>();
        for (int i = 0; i < productIdQtyPairs.length; i += 2) {
            int productId = productIdQtyPairs[i];
            int qty = productIdQtyPairs[i + 1];
            if (qty <= 0)
                throw new IllegalArgumentException("quantity must be > 0");
            Product product = products.get(productId);
            if (product == null)
                throw new IllegalArgumentException("unknown product: " + productId);
            if (product.getStock() < qty)
                throw new IllegalArgumentException(
                        "insufficient stock for product " + productId
                                + " (have " + product.getStock() + ", need " + qty + ")");
            items.add(new int[] { productId, qty });
        }

        Order order = new Order(nextOrderId++, customerId);
        for (int[] item : items) {
            Product product = products.get(item[0]);
            int qty = item[1];
            product.reduceStock(qty);
            order.addLine(new OrderLine(product.getId(), qty, product.getUnitPrice()));
        }
        orders.put(order.getOrderId(), order);
        return order;
    }

    public double checkout(int orderId) {
        Order order = orders.get(orderId);
        if (order == null)
            throw new IllegalArgumentException("unknown order: " + orderId);
        if (order.getStatus() != Order.Status.OPEN)
            throw new IllegalStateException("order already paid: " + orderId);
        if (order.getLines().isEmpty())
            throw new IllegalStateException("order has no lines");

        double subtotal = order.getSubtotal();
        double total = policy.apply(subtotal, order);
        order.markPaid(total);
        return total;
    }
}
