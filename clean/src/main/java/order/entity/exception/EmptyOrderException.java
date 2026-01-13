package order.entity.exception;

import order.entity.model.OrderId;

/**
 * Exception thrown when attempting to confirm an empty Order.
 */
public class EmptyOrderException extends OrderException {

    public EmptyOrderException(OrderId orderId) {
        super("Cannot confirm empty order: " + orderId);
    }
}
