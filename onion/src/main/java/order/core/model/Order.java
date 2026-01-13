package order.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate Root - Order.
 *
 * Im innersten Kern der Onion.
 * Enthält Geschäftsregeln und Invarianten.
 */
public class Order {

    private final OrderId id;
    private final CustomerId customerId;
    private final List<OrderItem> items;
    private OrderStatus status;

    private Order(OrderId id, CustomerId customerId, List<OrderItem> items, OrderStatus status) {
        this.id = Objects.requireNonNull(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.items = new ArrayList<>(items);
        this.status = status;
    }

    // Factory: Neue Order erstellen
    public static Order create(OrderId id, CustomerId customerId) {
        return new Order(id, customerId, new ArrayList<>(), OrderStatus.DRAFT);
    }

    // Factory: Aus Persistenz rekonstruieren
    public static Order reconstitute(OrderId id, CustomerId customerId, List<OrderItem> items, OrderStatus status) {
        return new Order(id, customerId, items, status);
    }

    // Geschäftslogik
    public void addItem(OrderItem item) {
        validateModifiable();
        items.add(item);
    }

    public void replaceItems(List<OrderItem> newItems) {
        validateModifiable();
        items.clear();
        items.addAll(newItems);
    }

    public void confirm() {
        validateModifiable();
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm empty order");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public Money calculateTotal() {
        return items.stream()
            .map(OrderItem::calculateSubtotal)
            .reduce(Money.ZERO, Money::add);
    }

    private void validateModifiable() {
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Order cannot be modified in status: " + status);
        }
    }

    // Getters
    public OrderId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
