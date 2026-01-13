package order.application.repository;

import order.core.model.Order;
import order.core.model.OrderId;

import java.util.Optional;

/**
 * Repository Interface - Order.
 *
 * In Onion: Interface ist in Application Layer,
 * Implementierung in Infrastructure.
 */
public interface OrderRepository {

    Optional<Order> findById(OrderId orderId);

    Order save(Order order);
}
