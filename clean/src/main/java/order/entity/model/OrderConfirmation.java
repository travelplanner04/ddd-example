package order.entity.model;

import java.time.LocalDateTime;

/**
 * Value Object representing the confirmation details of an Order.
 */
public class OrderConfirmation {

    private final OrderId orderId;
    private final LocalDateTime confirmedAt;
    private final Money subtotal;
    private final Money tax;
    private final Money shipping;
    private final Money total;

    public OrderConfirmation(OrderId orderId, LocalDateTime confirmedAt, Money subtotal, Money tax, Money shipping, Money total) {
        this.orderId = orderId;
        this.confirmedAt = confirmedAt;
        this.subtotal = subtotal;
        this.tax = tax;
        this.shipping = shipping;
        this.total = total;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public Money getSubtotal() {
        return subtotal;
    }

    public Money getTax() {
        return tax;
    }

    public Money getShipping() {
        return shipping;
    }

    public Money getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "OrderConfirmation{" +
                "orderId=" + orderId +
                ", confirmedAt=" + confirmedAt +
                ", subtotal=" + subtotal +
                ", tax=" + tax +
                ", shipping=" + shipping +
                ", total=" + total +
                '}';
    }
}
