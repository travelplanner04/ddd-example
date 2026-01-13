package order.core.model;

import java.util.Objects;

/**
 * Entity - OrderItem.
 *
 * Teil des Order Aggregates.
 */
public class OrderItem {

    private final ProductId productId;
    private final Quantity quantity;
    private final Money unitPrice;

    private OrderItem(ProductId productId, Quantity quantity, Money unitPrice) {
        this.productId = Objects.requireNonNull(productId);
        this.quantity = Objects.requireNonNull(quantity);
        this.unitPrice = Objects.requireNonNull(unitPrice);
    }

    public static OrderItem create(ProductId productId, Quantity quantity, Money unitPrice) {
        return new OrderItem(productId, quantity, unitPrice);
    }

    public Money calculateSubtotal() {
        return unitPrice.multiply(quantity.value());
    }

    public ProductId getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }
}
