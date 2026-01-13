package order.usecase.boundary.input;

import order.usecase.dto.OrderConfirmationResponse;

/**
 * Input Boundary (Use Case Interface) for confirming an Order.
 */
public interface ConfirmOrderInputBoundary {

    OrderConfirmationResponse execute(Long orderId);
}
