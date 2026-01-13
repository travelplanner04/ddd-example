package order.adapter.output.persistence;

import order.application.port.output.LoadOrderPort;
import order.application.port.output.SaveOrderPort;
import order.domain.model.*;

import java.util.*;

/**
 * Secondary Adapter - Persistenz.
 *
 * Implementiert Output Ports für Order-Persistierung.
 * In echter Anwendung: JPA Repository, JDBC, etc.
 */
public class OrderPersistenceAdapter implements LoadOrderPort, SaveOrderPort {

    // Simulierte Datenbank
    private final Map<Long, OrderEntity> database = new HashMap<>();
    private long idSequence = 1;

    public OrderPersistenceAdapter() {
        // Seed-Daten für Demo
        seedData();
    }

    @Override
    public Optional<Order> loadById(OrderId orderId) {
        OrderEntity entity = database.get(orderId.value());
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(mapToDomain(entity));
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = mapToEntity(order);

        // Neue Order bekommt generierte ID
        if (entity.id == null) {
            entity.id = idSequence++;
        }

        database.put(entity.id, entity);
        return mapToDomain(entity);
    }

    private Order mapToDomain(OrderEntity entity) {
        List<OrderItem> items = entity.items.stream()
            .map(this::mapItemToDomain)
            .toList();

        return Order.reconstitute(
            OrderId.of(entity.id),
            CustomerId.of(entity.customerId),
            items,
            OrderStatus.valueOf(entity.status)
        );
    }

    private OrderItem mapItemToDomain(OrderItemEntity entity) {
        return OrderItem.create(
            ProductId.of(entity.productId),
            Quantity.of(entity.quantity),
            Money.of(entity.unitPrice)
        );
    }

    private OrderEntity mapToEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.id = order.getId() != null ? order.getId().value() : null;
        entity.customerId = order.getCustomerId().value();
        entity.status = order.getStatus().name();
        entity.items = order.getItems().stream()
            .map(this::mapItemToEntity)
            .toList();
        return entity;
    }

    private OrderItemEntity mapItemToEntity(OrderItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.productId = item.getProductId().value();
        entity.quantity = item.getQuantity().value();
        entity.unitPrice = item.getUnitPrice().amount();
        return entity;
    }

    private void seedData() {
        OrderEntity order = new OrderEntity();
        order.id = idSequence++;
        order.customerId = "CUST-001";
        order.status = "DRAFT";
        order.items = List.of(
            createItemEntity(1L, 2, new java.math.BigDecimal("29.99")),
            createItemEntity(2L, 1, new java.math.BigDecimal("49.99"))
        );
        database.put(order.id, order);
    }

    private OrderItemEntity createItemEntity(Long productId, int qty, java.math.BigDecimal price) {
        OrderItemEntity item = new OrderItemEntity();
        item.productId = productId;
        item.quantity = qty;
        item.unitPrice = price;
        return item;
    }

    // Interne Entity-Klassen (in echter App: separate Dateien, JPA Annotations)
    private static class OrderEntity {
        Long id;
        String customerId;
        String status;
        List<OrderItemEntity> items = new ArrayList<>();
    }

    private static class OrderItemEntity {
        Long productId;
        int quantity;
        java.math.BigDecimal unitPrice;
    }
}
