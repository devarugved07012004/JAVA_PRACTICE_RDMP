import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OrderService shop = new OrderService();
        seed(shop);

        try (Scanner in = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = in.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> listProducts(shop);
                        case "2" -> addProduct(shop, in);
                        case "3" -> listCustomers(shop);
                        case "4" -> registerCustomer(shop, in);
                        case "5" -> placeOrder(shop, in);
                        case "6" -> setPolicy(shop, in);
                        case "7" -> checkout(shop, in);
                        case "8" -> listOrders(shop);
                        case "0" -> running = false;
                        default -> System.out.println("Unknown option.");
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                System.out.println();
            }
        }
        System.out.println("Bye.");
    }

    private static void seed(OrderService shop) {
        shop.addProduct(new Product(1, "Widget", 50.0, 10));
        shop.addProduct(new Product(2, "Gadget", 30.0, 20));
        shop.addProduct(new Product(3, "Doohickey", 15.0, 50));
        shop.registerCustomer(new Customer(1, "Ada", "ada@example.com"));
        System.out.println("Seeded 3 products + 1 customer (Ada, id=1).");
    }

    private static void printMenu() {
        System.out.println("""
                === Mini Shop ===
                1. List products
                2. Add product
                3. List customers
                4. Register customer
                5. Place order
                6. Set discount policy
                7. Checkout
                8. List orders
                0. Exit
                Choice: """);
    }

    private static void listProducts(OrderService shop) {
        System.out.println("Products:");
        for (Product p : shop.listProducts()) {
            System.out.printf(Locale.US, "  #%d %s  price=%.2f  stock=%d%n",
                    p.getId(), p.getName(), p.getUnitPrice(), p.getStock());
        }
    }

    private static void addProduct(OrderService shop, Scanner in) {
        int id = readInt(in, "Product id: ");
        System.out.print("Name: ");
        String name = in.nextLine().trim();
        double price = readDouble(in, "Unit price: ");
        int stock = readInt(in, "Stock: ");
        shop.addProduct(new Product(id, name, price, stock));
        System.out.println("Product added.");
    }

    private static void listCustomers(OrderService shop) {
        System.out.println("Customers:");
        for (Customer c : shop.listCustomers()) {
            System.out.printf("  #%d %s <%s>%n", c.getId(), c.getName(), c.getEmail());
        }
    }

    private static void registerCustomer(OrderService shop, Scanner in) {
        int id = readInt(in, "Customer id: ");
        System.out.print("Name: ");
        String name = in.nextLine().trim();
        System.out.print("Email: ");
        String email = in.nextLine().trim();
        shop.registerCustomer(new Customer(id, name, email));
        System.out.println("Customer registered.");
    }

    private static void placeOrder(OrderService shop, Scanner in) {
        int customerId = readInt(in, "Customer id: ");
        System.out.println("Enter productId qty pairs (e.g. 1 2 3 1). Empty line to cancel.");
        System.out.print("> ");
        String line = in.nextLine().trim();
        if (line.isEmpty()) {
            System.out.println("Cancelled.");
            return;
        }
        String[] parts = line.split("\\s+");
        if (parts.length % 2 != 0)
            throw new IllegalArgumentException("need productId qty pairs");
        int[] pairs = new int[parts.length];
        for (int i = 0; i < parts.length; i++)
            pairs[i] = Integer.parseInt(parts[i]);

        Order order = shop.placeOrder(customerId, pairs);
        System.out.printf(Locale.US, "Order #%d created. Subtotal=%.2f (OPEN)%n",
                order.getOrderId(), order.getSubtotal());
    }

    private static void setPolicy(OrderService shop, Scanner in) {
        System.out.println("""
                Policy:
                1. None
                2. Percent
                3. Bulk (qty >= 5 → 20% off)""");
        System.out.print("Choice: ");
        String choice = in.nextLine().trim();
        switch (choice) {
            case "1" -> {
                shop.setPolicy(new NoDiscount());
                System.out.println("Policy: NoDiscount");
            }
            case "2" -> {
                double percent = readDouble(in, "Percent off: ");
                shop.setPolicy(new PercentDiscount(percent));
                System.out.println("Policy: PercentDiscount " + percent + "%");
            }
            case "3" -> {
                shop.setPolicy(new BulkDiscount());
                System.out.println("Policy: BulkDiscount (5+ items → 20% off)");
            }
            default -> System.out.println("Unknown policy option.");
        }
    }

    private static void checkout(OrderService shop, Scanner in) {
        int orderId = readInt(in, "Order id: ");
        Order order = shop.findOrder(orderId);
        if (order == null)
            throw new IllegalArgumentException("unknown order: " + orderId);
        double subtotal = order.getSubtotal();
        double total = shop.checkout(orderId);
        System.out.printf(Locale.US, "Subtotal=%.2f  Payable=%.2f  (PAID)%n", subtotal, total);
    }

    private static void listOrders(OrderService shop) {
        System.out.println("Orders:");
        for (Order order : shop.listOrders()) {
            Customer c = shop.findCustomer(order.getCustomerId());
            String who = c == null ? ("#" + order.getCustomerId()) : c.getName();
            System.out.printf(Locale.US, "  Order #%d  customer=%s  status=%s  subtotal=%.2f",
                    order.getOrderId(), who, order.getStatus(), order.getSubtotal());
            if (order.getStatus() == Order.Status.PAID)
                System.out.printf(Locale.US, "  paid=%.2f", order.getTotalPaid());
            System.out.println();
            for (OrderLine line : order.getLines()) {
                System.out.printf(Locale.US, "    product=%d  qty=%d  @%.2f  =%.2f%n",
                        line.getProductId(), line.getQuantity(),
                        line.getUnitPrice(), line.getLineTotal());
            }
        }
    }

    private static int readInt(Scanner in, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(in.nextLine().trim());
    }

    private static double readDouble(Scanner in, String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(in.nextLine().trim());
    }
}
