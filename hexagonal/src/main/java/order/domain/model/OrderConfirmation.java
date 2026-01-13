package order.domain.model;

import java.time.LocalDateTime;

/**
 * Order Confirmation - Ergebnis der Bestätigung.
 *
 * DDD: Immutable Entity - alle Felder sind final, keine Setter.
 */
public class OrderConfirmation {

    private final Long id;
    private final OrderId orderId;
    private final Money totalAmount;
    private final Money taxAmount;
    private final Money shippingCost;
    private final LocalDateTime confirmedAt;

    private OrderConfirmation(Long id, OrderId orderId, Money totalAmount,
                              Money taxAmount, Money shippingCost, LocalDateTime confirmedAt) {
        this.id = id;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.taxAmount = taxAmount;
        this.shippingCost = shippingCost;
        this.confirmedAt = confirmedAt;
    }

    /**
     * Factory für neue Confirmations.
     *
     * HINWEIS: LocalDateTime.now() hier ist vereinfacht. In Produktion sollte
     * eine Clock/TimeProvider-Abstraktion injiziert werden für Testbarkeit.
     */
    public static OrderConfirmation create(Long id, OrderId orderId, Money totalAmount,
                                           Money taxAmount, Money shippingCost) {
        return new OrderConfirmation(id, orderId, totalAmount, taxAmount, shippingCost, LocalDateTime.now());
    }

    /**
     * Factory für Rekonstruktion aus Persistenz.
     */
    public static OrderConfirmation reconstitute(Long id, OrderId orderId, Money totalAmount,
                                                  Money taxAmount, Money shippingCost, LocalDateTime confirmedAt) {
        return new OrderConfirmation(id, orderId, totalAmount, taxAmount, shippingCost, confirmedAt);
    }

    public Money getGrandTotal() {
        return totalAmount.add(taxAmount).add(shippingCost);
    }

    public Long getId() { return id; }
    public OrderId getOrderId() { return orderId; }
    public Money getTotalAmount() { return totalAmount; }
    public Money getTaxAmount() { return taxAmount; }
    public Money getShippingCost() { return shippingCost; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
}
