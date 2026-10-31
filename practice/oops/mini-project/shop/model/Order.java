import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    public enum Status {
        OPEN, PAID
    }

    private int orderId;
    private int customerId;
    private List<OrderLine> lines = new ArrayList<>();
    private Status status = Status.OPEN;
    private double totalPaid;

    public Order(int orderId, int customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public void addLine(OrderLine line) {
        if (status != Status.OPEN)
            throw new IllegalStateException("cannot modify paid order");
        lines.add(line);
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public Status getStatus() {
        return status;
    }

    public double getSubtotal() {
        double sum = 0;
        for (OrderLine line : lines)
            sum += line.getLineTotal();
        return sum;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void markPaid(double total) {
        if (status != Status.OPEN)
            throw new IllegalStateException("order already paid");
        this.totalPaid = total;
        this.status = Status.PAID;
    }
}
