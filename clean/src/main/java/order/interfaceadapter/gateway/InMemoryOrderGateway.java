package order.interfaceadapter.gateway;

import order.entity.model.Order;
import order.entity.model.OrderId;
import order.usecase.boundary.output.OrderGateway;

import java.util.*;

/**
 * In-memory implementation of the OrderGateway.
 * In Clean Architecture, Gateway implementations live in the Interface Adapter layer.
 */
public class InMemoryOrderGateway implements OrderGateway {

    private final Map<OrderId, Order> orders = new HashMap<>();

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order save(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public void delete(OrderId orderId) {
        orders.remove(orderId);
    }
}
