package order.infrastructure.web;

import order.application.OrderApplicationService;
import order.application.dto.*;
import order.core.model.OrderId;

/**
 * Infrastructure - REST Controller.
 *
 * Äußerste Schicht: Web/UI.
 * In Onion gehört der Controller zur Infrastructure.
 */
public class OrderController {

    private final OrderApplicationService orderService;

    public OrderController(OrderApplicationService orderService) {
        this.orderService = orderService;
    }

    // GET /orders/{id}
    public OrderResponse getOrder(Long id) {
        return orderService.getOrder(OrderId.of(id));
    }

    // PUT /orders/{id}
    public OrderResponse updateOrder(Long id, UpdateOrderCommand command) {
        return orderService.updateOrder(OrderId.of(id), command);
    }

    // POST /orders/{id}/confirm
    public OrderConfirmationResponse confirmOrder(Long id) {
        return orderService.confirmOrder(OrderId.of(id));
    }
}
