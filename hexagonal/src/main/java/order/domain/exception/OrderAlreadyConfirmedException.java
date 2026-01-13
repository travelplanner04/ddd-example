package order.domain.exception;

import order.domain.model.OrderId;

public class OrderAlreadyConfirmedException extends OrderException {

    public OrderAlreadyConfirmedException(OrderId orderId) {
        super("Order " + orderId.value() + " is already confirmed");
    }
}
