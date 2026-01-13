package order.usecase.boundary.input;

import order.usecase.dto.OrderResponse;

/**
 * Input Boundary (Use Case Interface) for getting an Order.
 * In Clean Architecture, Input Boundaries define the application-specific business rules
 * that the outer layers can invoke.
 */
public interface GetOrderInputBoundary {

    OrderResponse execute(Long orderId);
}
