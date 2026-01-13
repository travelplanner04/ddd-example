package order.application.port.input;

import order.application.dto.OrderResponse;
import order.domain.model.OrderId;

/**
 * Input Port: Use Case für Order abrufen.
 */
public interface GetOrderUseCase {
    OrderResponse getOrder(OrderId orderId);
}
