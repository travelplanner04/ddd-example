package order.domain.exception;

import order.domain.model.OrderId;

public class EmptyOrderException extends OrderException {

    public EmptyOrderException(OrderId orderId) {
        super("Order " + orderId.value() + " has no items");
    }
}
