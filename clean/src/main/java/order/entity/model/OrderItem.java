package order.entity.model;

import java.util.Objects;

/**
 * Entity representing an item within an Order.
 *
 * DDD: Immutable - alle Felder sind final. Änderungen erzeugen neue Instanzen.
 */
public class OrderItem {

    private final ProductId productId;
    private final Quantity quantity;
    private final Money price;

    private OrderItem(ProductId productId, Quantity quantity, Money price) {
        this.productId = Objects.requireNonNull(productId, "ProductId cannot be null");
        this.quantity = Objects.requireNonNull(quantity, "Quantity cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
    }

    /**
     * Factory für neue OrderItems.
     */
    public static OrderItem create(ProductId productId, Quantity quantity, Money price) {
        return new OrderItem(productId, quantity, price);
    }

    /**
     * Factory für Rekonstruktion aus Persistenz.
     */
    public static OrderItem reconstitute(ProductId productId, Quantity quantity, Money price) {
        return new OrderItem(productId, quantity, price);
    }

    public ProductId getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    /**
     * Erzeugt neues OrderItem mit aktualisierter Quantity.
     * DDD: Immutable - gibt neue Instanz zurück statt zu mutieren.
     */
    public OrderItem withQuantity(Quantity newQuantity) {
        return new OrderItem(this.productId, newQuantity, this.price);
    }

    /**
     * Erzeugt neues OrderItem mit aktualisiertem Preis.
     * DDD: Immutable - gibt neue Instanz zurück statt zu mutieren.
     */
    public OrderItem withPrice(Money newPrice) {
        return new OrderItem(this.productId, this.quantity, newPrice);
    }

    public Money calculateTotal() {
        return price.multiply(quantity.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(productId, orderItem.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
