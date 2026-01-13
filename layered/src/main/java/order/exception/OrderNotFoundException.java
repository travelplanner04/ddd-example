package order.exception;

import order.model.OrderId;

/**
 * Exception - Order nicht gefunden.
 */
public class OrderNotFoundException extends RuntimeException {

    private final OrderId orderId;

    public OrderNotFoundException(OrderId orderId) {
        super("Order not found: " + orderId.value());
        this.orderId = orderId;
    }

    public OrderId getOrderId() {
        return orderId;
    }
}
