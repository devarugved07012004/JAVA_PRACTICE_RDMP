public class OrderLine {
    private int productId;
    private int quantity;
    private double unitPrice;

    public OrderLine(int productId, int quantity, double unitPrice) {
        if (quantity <= 0)
            throw new IllegalArgumentException("quantity must be > 0");
        if (unitPrice <= 0)
            throw new IllegalArgumentException("unitPrice must be > 0");
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getLineTotal() {
        return quantity * unitPrice;
    }
}
