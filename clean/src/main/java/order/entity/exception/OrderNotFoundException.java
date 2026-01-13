package order.entity.exception;

import order.entity.model.OrderId;

/**
 * Exception thrown when an Order cannot be found.
 */
public class OrderNotFoundException extends OrderException {

    public OrderNotFoundException(OrderId orderId) {
        super("Order not found: " + orderId);
    }
}
