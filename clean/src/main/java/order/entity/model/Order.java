package order.entity.model;

import order.entity.exception.EmptyOrderException;
import order.entity.exception.OrderAlreadyConfirmedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Aggregate Root representing an Order.
 * Contains all business rules related to Order management.
 */
public class Order {

    private final OrderId id;
    private final CustomerId customerId;
    private final List<OrderItem> items;
    private OrderStatus status;

    private Order(OrderId id, CustomerId customerId, List<OrderItem> items, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.status = status;
    }

    /**
     * Factory method to create a new Order in DRAFT status.
     */
    public static Order create(OrderId id, CustomerId customerId) {
        return new Order(id, customerId, new ArrayList<>(), OrderStatus.DRAFT);
    }

    /**
     * Factory method to reconstruct an Order from persistence.
     */
    public static Order reconstitute(OrderId id, CustomerId customerId, List<OrderItem> items, OrderStatus status) {
        return new Order(id, customerId, items, status);
    }

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

    /**
     * Adds an item to the order or updates quantity if product already exists.
     */
    public void addItem(ProductId productId, Quantity quantity, Money price) {
        ensureModifiable();

        Optional<OrderItem> existingItem = findItemByProductId(productId);
        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            item.updateQuantity(item.getQuantity().add(quantity));
            item.updatePrice(price);
        } else {
            items.add(new OrderItem(productId, quantity, price));
        }
    }

    /**
     * Removes an item from the order.
     */
    public void removeItem(ProductId productId) {
        ensureModifiable();
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    /**
     * Updates the quantity of an existing item.
     */
    public void updateItemQuantity(ProductId productId, Quantity newQuantity) {
        ensureModifiable();

        findItemByProductId(productId)
                .ifPresent(item -> item.updateQuantity(newQuantity));
    }

    /**
     * Confirms the order. Throws exception if order is empty.
     */
    public void confirm() {
        ensureModifiable();

        if (items.isEmpty()) {
            throw new EmptyOrderException(id);
        }

        this.status = OrderStatus.CONFIRMED;
    }

    /**
     * Cancels the order.
     */
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    /**
     * Calculates the total amount of the order.
     */
    public Money calculateTotal() {
        return items.stream()
                .map(OrderItem::calculateTotal)
                .reduce(Money.zero(), Money::add);
    }

    /**
     * Returns the total number of items in the order.
     */
    public int getTotalItemCount() {
        return items.stream()
                .mapToInt(item -> item.getQuantity().getValue())
                .sum();
    }

    private Optional<OrderItem> findItemByProductId(ProductId productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    private void ensureModifiable() {
        if (status == OrderStatus.CONFIRMED) {
            throw new OrderAlreadyConfirmedException(id);
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", items=" + items.size() +
                ", status=" + status +
                '}';
    }
}
