package order.domain.model;

/**
 * Order Item - Teil eines Aggregates.
 *
 * WICHTIG: Referenziert Product nur über ProductId (Value Object).
 * Kein Import aus Product-Modul = keine Kopplung zwischen Bounded Contexts.
 */
public class OrderItem {

    private final ProductId productId;
    private final Quantity quantity;
    private final Money unitPrice;

    private OrderItem(ProductId productId, Quantity quantity, Money unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderItem create(ProductId productId, Quantity quantity, Money unitPrice) {
        if (!unitPrice.isPositive()) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        return new OrderItem(productId, quantity, unitPrice);
    }

    // Factory für Rekonstruktion aus Persistenz
    public static OrderItem reconstitute(ProductId productId, Quantity quantity, Money unitPrice) {
        return new OrderItem(productId, quantity, unitPrice);
    }

    public Money calculateSubtotal() {
        return unitPrice.multiply(quantity.value());
    }

    public ProductId getProductId() { return productId; }
    public Quantity getQuantity() { return quantity; }
    public Money getUnitPrice() { return unitPrice; }
}
