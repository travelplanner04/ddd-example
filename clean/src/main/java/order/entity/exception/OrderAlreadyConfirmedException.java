package order.entity.exception;

import order.entity.model.OrderId;

/**
 * Exception thrown when attempting to modify a confirmed Order.
 */
public class OrderAlreadyConfirmedException extends OrderException {

    public OrderAlreadyConfirmedException(OrderId orderId) {
        super("Order already confirmed: " + orderId);
    }
}
