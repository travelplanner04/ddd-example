package order.application.port.input;

import order.application.dto.OrderResponse;
import order.application.dto.UpdateOrderCommand;
import order.domain.model.OrderId;

/**
 * Input Port: Use Case für Order aktualisieren.
 */
public interface UpdateOrderUseCase {
    OrderResponse updateOrder(OrderId orderId, UpdateOrderCommand command);
}
