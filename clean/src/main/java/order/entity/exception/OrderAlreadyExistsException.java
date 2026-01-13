package order.entity.exception;

import order.entity.model.OrderId;

/**
 * Exception thrown when an Order with the same ID already exists.
 */
public class OrderAlreadyExistsException extends OrderException {

    public OrderAlreadyExistsException(OrderId orderId) {
        super("Order already exists: " + orderId);
    }
}
