package order.usecase.boundary.output;

import order.entity.model.Order;
import order.entity.model.OrderId;

import java.util.List;
import java.util.Optional;

/**
 * Output Boundary (Gateway Interface) for Order persistence.
 * In Clean Architecture, Gateways define the interface for external data sources.
 * The actual implementation is provided by the Framework layer.
 */
public interface OrderGateway {

    Optional<Order> findById(OrderId orderId);

    List<Order> findAll();

    Order save(Order order);

    void delete(OrderId orderId);
}
