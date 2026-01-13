package order.core.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity - Bestellbestätigung.
 *
 * Ergebnis der Bestätigung mit berechneten Werten.
 */
public class OrderConfirmation {

    private final Long id;
    private final OrderId orderId;
    private final Money totalAmount;
    private final Money taxAmount;
    private final Money shippingCost;
    private final LocalDateTime confirmedAt;

    private OrderConfirmation(
            Long id,
            OrderId orderId,
            Money totalAmount,
            Money taxAmount,
            Money shippingCost,
            LocalDateTime confirmedAt) {
        this.id = id;
        this.orderId = Objects.requireNonNull(orderId);
        this.totalAmount = Objects.requireNonNull(totalAmount);
        this.taxAmount = Objects.requireNonNull(taxAmount);
        this.shippingCost = Objects.requireNonNull(shippingCost);
        this.confirmedAt = Objects.requireNonNull(confirmedAt);
    }

    public static OrderConfirmation create(
            Long id,
            OrderId orderId,
            Money totalAmount,
            Money taxAmount,
            Money shippingCost,
            LocalDateTime confirmedAt) {
        return new OrderConfirmation(id, orderId, totalAmount, taxAmount, shippingCost, confirmedAt);
    }

    public Money getGrandTotal() {
        return totalAmount.add(taxAmount).add(shippingCost);
    }

    public Long getId() {
        return id;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public Money getTaxAmount() {
        return taxAmount;
    }

    public Money getShippingCost() {
        return shippingCost;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }
}
