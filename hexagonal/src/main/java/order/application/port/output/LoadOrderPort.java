package order.application.port.output;

import order.domain.model.Order;
import order.domain.model.OrderId;

import java.util.Optional;

/**
 * Output Port: Order aus Persistenz laden.
 */
public interface LoadOrderPort {
    Optional<Order> loadById(OrderId orderId);
}
