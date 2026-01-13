package order.interfaceadapter.controller;

import order.usecase.boundary.input.ConfirmOrderInputBoundary;
import order.usecase.boundary.input.GetOrderInputBoundary;
import order.usecase.boundary.input.UpdateOrderInputBoundary;
import order.usecase.dto.OrderConfirmationResponse;
import order.usecase.dto.OrderResponse;
import order.usecase.dto.UpdateOrderRequest;

/**
 * Controller (Interface Adapter) for handling Order-related HTTP requests.
 * In Clean Architecture, Controllers convert requests from the external format
 * into the format expected by the Use Case (Input Boundary).
 */
public class OrderController {

    private final GetOrderInputBoundary getOrderUseCase;
    private final UpdateOrderInputBoundary updateOrderUseCase;
    private final ConfirmOrderInputBoundary confirmOrderUseCase;

    public OrderController(
            GetOrderInputBoundary getOrderUseCase,
            UpdateOrderInputBoundary updateOrderUseCase,
            ConfirmOrderInputBoundary confirmOrderUseCase) {
        this.getOrderUseCase = getOrderUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
    }

    /**
     * GET /orders/{id}
     */
    public OrderResponse getOrder(Long orderId) {
        return getOrderUseCase.execute(orderId);
    }

    /**
     * PUT /orders/{id}
     */
    public OrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        return updateOrderUseCase.execute(orderId, request);
    }

    /**
     * POST /orders/{id}/confirm
     */
    public OrderConfirmationResponse confirmOrder(Long orderId) {
        return confirmOrderUseCase.execute(orderId);
    }
}
