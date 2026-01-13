package order.domain.model;

import java.time.LocalDateTime;

/**
 * Order Confirmation - Ergebnis der Bestätigung.
 */
public class OrderConfirmation {

    private Long id;
    private final OrderId orderId;
    private final Money totalAmount;
    private final Money taxAmount;
    private final Money shippingCost;
    private final LocalDateTime confirmedAt;

    private OrderConfirmation(OrderId orderId, Money totalAmount,
                              Money taxAmount, Money shippingCost) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.taxAmount = taxAmount;
        this.shippingCost = shippingCost;
        this.confirmedAt = LocalDateTime.now();
    }

    public static OrderConfirmation create(OrderId orderId, Money totalAmount,
                                           Money taxAmount, Money shippingCost) {
        return new OrderConfirmation(orderId, totalAmount, taxAmount, shippingCost);
    }

    public Money getGrandTotal() {
        return totalAmount.add(taxAmount).add(shippingCost);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public OrderId getOrderId() { return orderId; }
    public Money getTotalAmount() { return totalAmount; }
    public Money getTaxAmount() { return taxAmount; }
    public Money getShippingCost() { return shippingCost; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
}
