public class Product {
    private int id;
    private String name;
    private double unitPrice;
    private int stock;

    public Product(int id, String name, double unitPrice, int stock) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name required");
        if (unitPrice <= 0)
            throw new IllegalArgumentException("unitPrice must be > 0");
        if (stock < 0)
            throw new IllegalArgumentException("stock must be >= 0");
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getStock() {
        return stock;
    }

    public void reduceStock(int qty) {
        if (qty <= 0)
            throw new IllegalArgumentException("qty must be > 0");
        if (stock < qty)
            throw new IllegalArgumentException("insufficient stock");
        stock -= qty;
    }
}
