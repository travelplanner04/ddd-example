package order.usecase.boundary.input;

import order.usecase.dto.OrderResponse;
import order.usecase.dto.UpdateOrderRequest;

/**
 * Input Boundary (Use Case Interface) for updating an Order.
 */
public interface UpdateOrderInputBoundary {

    OrderResponse execute(Long orderId, UpdateOrderRequest request);
}
