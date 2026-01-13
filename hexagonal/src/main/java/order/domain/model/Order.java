package order.domain.model;

import order.domain.exception.OrderAlreadyConfirmedException;
import order.domain.exception.EmptyOrderException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order Aggregate Root.
 *
 * Alle Änderungen am Order gehen über diese Klasse.
 * Schützt Invarianten des Aggregates.
 */
public class Order {

    private final OrderId id;
    private final CustomerId customerId;
    private List<OrderItem> items;
    private OrderStatus status;

    private Order(OrderId id, CustomerId customerId, List<OrderItem> items, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.status = status;
    }

    // Factory für neue Orders
    public static Order create(OrderId id, CustomerId customerId) {
        return new Order(id, customerId, new ArrayList<>(), OrderStatus.DRAFT);
    }

    // Factory für Rekonstruktion aus Persistenz
    public static Order reconstitute(OrderId id, CustomerId customerId,
                                      List<OrderItem> items, OrderStatus status) {
        return new Order(id, customerId, items, status);
    }

    public void addItem(OrderItem item) {
        ensureModifiable();
        this.items.add(item);
    }

    public void replaceItems(List<OrderItem> newItems) {
        ensureModifiable();
        this.items = new ArrayList<>(newItems);
    }

    public void confirm() {
        ensureModifiable();
        if (items.isEmpty()) {
            throw new EmptyOrderException(id);
        }
        this.status = OrderStatus.CONFIRMED;
    }

    private void ensureModifiable() {
        if (!status.isModifiable()) {
            throw new OrderAlreadyConfirmedException(id);
        }
    }

    public Money calculateTotal() {
        return items.stream()
            .map(OrderItem::calculateSubtotal)
            .reduce(Money.ZERO, Money::add);
    }

    public int getItemCount() {
        return items.size();
    }

    // Getters - immutable views
    public OrderId getId() { return id; }
    public CustomerId getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public OrderStatus getStatus() { return status; }
    public boolean isConfirmed() { return status == OrderStatus.CONFIRMED; }
}
