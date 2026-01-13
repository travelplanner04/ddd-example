package order.exception;

import order.model.OrderId;

/**
 * Exception - Order existiert bereits im externen System.
 */
public class OrderAlreadyExistsException extends RuntimeException {

    private final OrderId orderId;

    public OrderAlreadyExistsException(OrderId orderId) {
        super("Order already exists in external system: " + orderId.value());
        this.orderId = orderId;
    }

    public OrderId getOrderId() {
        return orderId;
    }
}
