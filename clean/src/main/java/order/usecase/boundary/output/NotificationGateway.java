package order.usecase.boundary.output;

import order.entity.model.OrderConfirmation;

/**
 * Output Boundary (Gateway Interface) for sending notifications.
 */
public interface NotificationGateway {

    void sendOrderConfirmation(OrderConfirmation confirmation, String customerEmail);
}
