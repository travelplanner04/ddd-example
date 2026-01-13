package order.entity.model;

import java.util.Objects;

/**
 * Entity representing an item within an Order.
 */
public class OrderItem {

    private final ProductId productId;
    private Quantity quantity;
    private Money price;

    public OrderItem(ProductId productId, Quantity quantity, Money price) {
        if (productId == null) {
            throw new IllegalArgumentException("ProductId cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
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

    public void updateQuantity(Quantity newQuantity) {
        this.quantity = newQuantity;
    }

    public void updatePrice(Money newPrice) {
        this.price = newPrice;
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
