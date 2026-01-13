package order.presentation;

import order.dto.*;
import order.model.OrderId;
import order.service.OrderService;

/**
 * Presentation Layer - REST Controller.
 *
 * Oberste Schicht in Layered Architecture.
 * Hängt vom Service Layer ab.
 */
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // GET /orders/{id}
    public OrderResponse getOrder(Long id) {
        return orderService.getOrder(OrderId.of(id));
    }

    // PUT /orders/{id}
    public OrderResponse updateOrder(Long id, UpdateOrderRequest request) {
        return orderService.updateOrder(OrderId.of(id), request);
    }

    // POST /orders/{id}/confirm
    public OrderConfirmationResponse confirmOrder(Long id) {
        return orderService.confirmOrder(OrderId.of(id));
    }
}
