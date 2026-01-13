package order.adapter.input.rest;

import order.application.dto.*;
import order.application.port.input.*;
import order.domain.model.OrderId;

/**
 * Primary Adapter - REST Controller.
 *
 * Übersetzt HTTP-Requests in Use Case Aufrufe.
 * Kennt nur Input Ports, nicht die konkrete Implementierung.
 */
public class OrderController {

    private final GetOrderUseCase getOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;

    public OrderController(
            GetOrderUseCase getOrderUseCase,
            UpdateOrderUseCase updateOrderUseCase,
            ConfirmOrderUseCase confirmOrderUseCase) {
        this.getOrderUseCase = getOrderUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
    }

    // GET /orders/{id}
    public OrderResponse getOrder(Long id) {
        return getOrderUseCase.getOrder(OrderId.of(id));
    }

    // PUT /orders/{id}
    public OrderResponse updateOrder(Long id, UpdateOrderCommand command) {
        return updateOrderUseCase.updateOrder(OrderId.of(id), command);
    }

    // POST /orders/{id}/confirm
    public OrderConfirmationResponse confirmOrder(Long id) {
        return confirmOrderUseCase.confirmOrder(OrderId.of(id));
    }
}
