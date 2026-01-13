package order.domain.exception;

import order.domain.model.OrderId;

public class OrderAlreadyExistsException extends OrderException {

    public OrderAlreadyExistsException(OrderId orderId) {
        super("Order " + orderId.value() + " already exists in external system");
    }
}
