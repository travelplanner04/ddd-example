package order.repository;

import order.model.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Data Access Layer - Order Repository.
 *
 * In Layered Architecture: Konkrete Klasse, kein Interface.
 * Die Business-Schicht hängt direkt davon ab.
 */
public class OrderRepository {

    private final Map<Long, OrderData> database = new HashMap<>();
    private long idSequence = 1;

    public OrderRepository() {
        seedData();
    }

    public Optional<Order> findById(OrderId orderId) {
        OrderData data = database.get(orderId.value());
        if (data == null) {
            return Optional.empty();
        }
        return Optional.of(mapToDomain(data));
    }

    public Order save(Order order) {
        OrderData data = mapToData(order);

        if (data.id == null) {
            data.id = idSequence++;
        }

        database.put(data.id, data);
        return mapToDomain(data);
    }

    private Order mapToDomain(OrderData data) {
        List<OrderItem> items = data.items.stream()
            .map(this::mapItemToDomain)
            .toList();

        return Order.reconstitute(
            OrderId.of(data.id),
            CustomerId.of(data.customerId),
            items,
            OrderStatus.valueOf(data.status)
        );
    }

    private OrderItem mapItemToDomain(OrderItemData data) {
        return OrderItem.create(
            ProductId.of(data.productId),
            Quantity.of(data.quantity),
            Money.of(data.unitPrice)
        );
    }

    private OrderData mapToData(Order order) {
        OrderData data = new OrderData();
        data.id = order.getId() != null ? order.getId().value() : null;
        data.customerId = order.getCustomerId().value();
        data.status = order.getStatus().name();
        data.items = order.getItems().stream()
            .map(this::mapItemToData)
            .toList();
        return data;
    }

    private OrderItemData mapItemToData(OrderItem item) {
        OrderItemData data = new OrderItemData();
        data.productId = item.getProductId().value();
        data.quantity = item.getQuantity().value();
        data.unitPrice = item.getUnitPrice().amount();
        return data;
    }

    private void seedData() {
        OrderData order = new OrderData();
        order.id = idSequence++;
        order.customerId = "CUST-001";
        order.status = "DRAFT";
        order.items = List.of(
            createItemData(1L, 2, new BigDecimal("29.99")),
            createItemData(2L, 1, new BigDecimal("49.99"))
        );
        database.put(order.id, order);
    }

    private OrderItemData createItemData(Long productId, int qty, BigDecimal price) {
        OrderItemData item = new OrderItemData();
        item.productId = productId;
        item.quantity = qty;
        item.unitPrice = price;
        return item;
    }

    // Interne Daten-Klassen
    private static class OrderData {
        Long id;
        String customerId;
        String status;
        List<OrderItemData> items = new ArrayList<>();
    }

    private static class OrderItemData {
        Long productId;
        int quantity;
        BigDecimal unitPrice;
    }
}
