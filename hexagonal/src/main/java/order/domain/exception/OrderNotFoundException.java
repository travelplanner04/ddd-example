package order.domain.exception;

import order.domain.model.OrderId;

public class OrderNotFoundException extends OrderException {

    private final OrderId orderId;

    public OrderNotFoundException(OrderId orderId) {
        super("Order not found: " + orderId.value());
        this.orderId = orderId;
    }

    public OrderId getOrderId() { return orderId; }
}
